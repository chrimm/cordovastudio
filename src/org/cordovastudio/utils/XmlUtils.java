/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.utils;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.cordovastudio.GlobalConstants;
import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

import static org.cordovastudio.GlobalConstants.*;
import static com.google.common.base.Charsets.*;

/** XML Utilities */
public class XmlUtils {
    public static final String XML_COMMENT_BEGIN = "<!--";
    public static final String XML_COMMENT_END = "-->";
    public static final String XML_PROLOG =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    /**
     * Separator for xml namespace and localname
     */
    public static final char NS_SEPARATOR = ':';

    /**
     * Returns the namespace prefix matching the requested namespace URI.
     * If no such declaration is found, returns the default "android" prefix for
     * the Android URI, and "app" for other URI's. By default the app namespace
     * will be created. If this is not desirable, call
     * {@link #lookupNamespacePrefix(org.w3c.dom.Node, String, boolean)} instead.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found,
     *              e.g. {@link GlobalConstants#CORDOVASTUDIO_URI}
     * @return The first prefix declared or the default "android" prefix
     *              (or "app" for non-Android URIs)
     */
    @NotNull
    public static String lookupNamespacePrefix(@NotNull Node node, @NotNull String nsUri) {
        String defaultPrefix = CORDOVASTUDIO_URI.equals(nsUri) ? CORDOVASTUDIO_NS_PREFIX : CORDOVA_NS_PREFIX;
        return lookupNamespacePrefix(node, nsUri, defaultPrefix, true /*create*/);
    }

    /**
     * Returns the namespace prefix matching the requested namespace URI. If no
     * such declaration is found, returns the default "android" prefix for the
     * Android URI, and "app" for other URI's.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found, e.g.
     *            {@link GlobalConstants#CORDOVASTUDIO_URI}
     * @param create whether the namespace declaration should be created, if
     *            necessary
     * @return The first prefix declared or the default "android" prefix (or
     *         "app" for non-Android URIs)
     */
    @NotNull
    public static String lookupNamespacePrefix(@NotNull Node node, @NotNull String nsUri,
            boolean create) {
        String defaultPrefix = CORDOVASTUDIO_URI.equals(nsUri) ? CORDOVASTUDIO_NS_PREFIX : CORDOVA_NS_PREFIX;
        return lookupNamespacePrefix(node, nsUri, defaultPrefix, create);
    }

    /**
     * Returns the namespace prefix matching the requested namespace URI. If no
     * such declaration is found, returns the default "android" prefix.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found, e.g.
     *            {@link GlobalConstants#CORDOVASTUDIO_URI}
     * @param defaultPrefix The default prefix (root) to use if the namespace is
     *            not found. If null, do not create a new namespace if this URI
     *            is not defined for the document.
     * @param create whether the namespace declaration should be created, if
     *            necessary
     * @return The first prefix declared or the provided prefix (possibly with a
     *            number appended to avoid conflicts with existing prefixes.
     */
    public static String lookupNamespacePrefix(
            @Nullable Node node, @Nullable String nsUri, @Nullable String defaultPrefix,
            boolean create) {
        // Note: Node.lookupPrefix is not implemented in wst/xml/core NodeImpl.java
        // The following code emulates this simple call:
        //   String prefix = node.lookupPrefix(NS_RESOURCES);

        // if the requested URI is null, it denotes an attribute with no namespace.
        if (nsUri == null) {
            return null;
        }

        // per XML specification, the "xmlns" URI is reserved
        if (XMLNS_URI.equals(nsUri)) {
            return XMLNS;
        }

        HashSet<String> visited = new HashSet<String>();
        Document doc = node == null ? null : node.getOwnerDocument();

        // Ask the document about it. This method may not be implemented by the Document.
        String nsPrefix = null;
        try {
            nsPrefix = doc != null ? doc.lookupPrefix(nsUri) : null;
            if (nsPrefix != null) {
                return nsPrefix;
            }
        } catch (Throwable t) {
            // ignore
        }

        // If that failed, try to look it up manually.
        // This also gathers prefixed in use in the case we want to generate a new one below.
        for (; node != null && node.getNodeType() == Node.ELEMENT_NODE;
               node = node.getParentNode()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int n = attrs.getLength() - 1; n >= 0; --n) {
                Node attr = attrs.item(n);
                if (XMLNS.equals(attr.getPrefix())) {
                    String uri = attr.getNodeValue();
                    nsPrefix = attr.getLocalName();
                    // Is this the URI we are looking for? If yes, we found its prefix.
                    if (nsUri.equals(uri)) {
                        return nsPrefix;
                    }
                    visited.add(nsPrefix);
                }
            }
        }

        // Failed the find a prefix. Generate a new sensible default prefix, unless
        // defaultPrefix was null in which case the caller does not want the document
        // modified.
        if (defaultPrefix == null) {
            return null;
        }

        //
        // We need to make sure the prefix is not one that was declared in the scope
        // visited above. Pick a unique prefix from the provided default prefix.
        String prefix = defaultPrefix;
        String base = prefix;
        for (int i = 1; visited.contains(prefix); i++) {
            prefix = base + Integer.toString(i);
        }
        // Also create & define this prefix/URI in the XML document as an attribute in the
        // first element of the document.
        if (doc != null) {
            node = doc.getFirstChild();
            while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
                node = node.getNextSibling();
            }
            if (node != null && create) {
                // This doesn't work:
                //Attr attr = doc.createAttributeNS(XMLNS_URI, prefix);
                //attr.setPrefix(XMLNS);
                //
                // Xerces throws
                //org.w3c.dom.DOMException: NAMESPACE_ERR: An attempt is made to create or
                // change an object in a way which is incorrect with regard to namespaces.
                //
                // Instead pass in the concatenated prefix. (This is covered by
                // the UiElementNodeTest#testCreateNameSpace() test.)
                Attr attr = doc.createAttributeNS(XMLNS_URI, XMLNS_PREFIX + prefix);
                attr.setValue(nsUri);
                node.getAttributes().setNamedItemNS(attr);
            }
        }

        return prefix;
    }

    /**
     * Converts the given attribute value to an XML-attribute-safe value, meaning that
     * single and double quotes are replaced with their corresponding XML entities.
     *
     * @param attrValue the value to be escaped
     * @return the escaped value
     */
    @NotNull
    public static String toXmlAttributeValue(@NotNull String attrValue) {
        for (int i = 0, n = attrValue.length(); i < n; i++) {
            char c = attrValue.charAt(i);
            if (c == '"' || c == '\'' || c == '<' || c == '&') {
                StringBuilder sb = new StringBuilder(2 * attrValue.length());
                appendXmlAttributeValue(sb, attrValue);
                return sb.toString();
            }
        }

        return attrValue;
    }

    /**
     * Converts the given XML-attribute-safe value to a java string
     *
     * @param escapedAttrValue the escaped value
     * @return the unescaped value
     */
    @NotNull
    public static String fromXmlAttributeValue(@NotNull String escapedAttrValue) {
        String workingString = escapedAttrValue.replace(QUOT_ENTITY, "\"");
        workingString = workingString.replace(LT_ENTITY, "<");
        workingString = workingString.replace(APOS_ENTITY, "'");
        workingString = workingString.replace(AMP_ENTITY, "&");

        return workingString;
    }

    /**
     * Converts the given attribute value to an XML-text-safe value, meaning that
     * less than and ampersand characters are escaped.
     *
     * @param textValue the text value to be escaped
     * @return the escaped value
     */
    @NotNull
    public static String toXmlTextValue(@NotNull String textValue) {
        for (int i = 0, n = textValue.length(); i < n; i++) {
            char c = textValue.charAt(i);
            if (c == '<' || c == '&') {
                StringBuilder sb = new StringBuilder(2 * textValue.length());
                appendXmlTextValue(sb, textValue);
                return sb.toString();
            }
        }

        return textValue;
    }

    /**
     * Appends text to the given {@link StringBuilder} and escapes it as required for a
     * DOM attribute node.
     *
     * @param sb the string builder
     * @param attrValue the attribute value to be appended and escaped
     */
    public static void appendXmlAttributeValue(@NotNull StringBuilder sb,
            @NotNull String attrValue) {
        int n = attrValue.length();
        // &, ", ' and < are illegal in attributes; see http://www.w3.org/TR/REC-xml/#NT-AttValue
        // (' legal in a " string and " is legal in a ' string but here we'll stay on the safe
        // side)
        for (int i = 0; i < n; i++) {
            char c = attrValue.charAt(i);
            if (c == '"') {
                sb.append(QUOT_ENTITY);
            } else if (c == '<') {
                sb.append(LT_ENTITY);
            } else if (c == '\'') {
                sb.append(APOS_ENTITY);
            } else if (c == '&') {
                sb.append(AMP_ENTITY);
            } else {
                sb.append(c);
            }
        }
    }

    /**
     * Appends text to the given {@link StringBuilder} and escapes it as required for a
     * DOM text node.
     *
     * @param sb the string builder
     * @param textValue the text value to be appended and escaped
     */
    public static void appendXmlTextValue(@NotNull StringBuilder sb, @NotNull String textValue) {
        for (int i = 0, n = textValue.length(); i < n; i++) {
            char c = textValue.charAt(i);
            if (c == '<') {
                sb.append(LT_ENTITY);
            } else if (c == '&') {
                sb.append(AMP_ENTITY);
            } else {
                sb.append(c);
            }
        }
    }

    /**
     * Returns true if the given node has one or more element children
     *
     * @param node the node to test for element children
     * @return true if the node has one or more element children
     */
    public static boolean hasElementChildren(@NotNull Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a character reader for the given file, which must be a UTF encoded file.
     * <p>
     * The reader does not need to be closed by the caller (because the file is read in
     * full in one shot and the resulting array is then wrapped in a byte array input stream,
     * which does not need to be closed.)
     */
    public static Reader getUtfReader(@NotNull File file) throws IOException {
        byte[] bytes = Files.toByteArray(file);
        int length = bytes.length;
        if (length == 0) {
            return new StringReader("");
        }

        switch (bytes[0]) {
            case (byte)0xEF: {
                if (length >= 3
                        && bytes[1] == (byte)0xBB
                        && bytes[2] == (byte)0xBF) {
                    // UTF-8 BOM: EF BB BF: Skip it
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 3, length - 3),
                            UTF_8);
                }
                break;
            }
            case (byte)0xFE: {
                if (length >= 2
                        && bytes[1] == (byte)0xFF) {
                    // UTF-16 Big Endian BOM: FE FF
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 2, length - 2),
                            UTF_16BE);
                }
                break;
            }
            case (byte)0xFF: {
                if (length >= 2
                        && bytes[1] == (byte)0xFE) {
                    if (length >= 4
                            && bytes[2] == (byte)0x00
                            && bytes[3] == (byte)0x00) {
                        // UTF-32 Little Endian BOM: FF FE 00 00
                        return new InputStreamReader(new ByteArrayInputStream(bytes, 4,
                                length - 4), "UTF-32LE");
                    }

                    // UTF-16 Little Endian BOM: FF FE
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 2, length - 2),
                            UTF_16LE);
                }
                break;
            }
            case (byte)0x00: {
                if (length >= 4
                        && bytes[0] == (byte)0x00
                        && bytes[1] == (byte)0x00
                        && bytes[2] == (byte)0xFE
                        && bytes[3] == (byte)0xFF) {
                    // UTF-32 Big Endian BOM: 00 00 FE FF
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 4, length - 4),
                            "UTF-32BE");
                }
                break;
            }
        }

        // No byte order mark: Assume UTF-8 (where the BOM is optional).
        return new InputStreamReader(new ByteArrayInputStream(bytes), UTF_8);
    }

    /**
     * Parses the given XML string as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware.
     *
     * @param xml            the XML content to be parsed (must be well formed)
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document
     */
    @NotNull
    public static Document parseDocument(@NotNull String xml, boolean namespaceAware)
            throws ParserConfigurationException, IOException, SAXException {
        xml = stripBom(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource is = new InputSource(new StringReader(xml));
        factory.setNamespaceAware(namespaceAware);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    /**
     * Parses the given UTF file as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware.
     *
     * @param file           the UTF encoded file to parse
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document
     */
    @NotNull
    public static Document parseUtfXmlFile(@NotNull File file, boolean namespaceAware)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Reader reader = getUtfReader(file);
        try {
            InputSource is = new InputSource(reader);
            factory.setNamespaceAware(namespaceAware);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(is);
        } finally {
            reader.close();
        }
    }

    /** Strips out a leading UTF byte order mark, if present */
    @NotNull
    public static String stripBom(@NotNull String xml) {
        if (!xml.isEmpty() && xml.charAt(0) == '\uFEFF') {
            return xml.substring(1);
        }
        return xml;
    }

    /**
     * Parses the given XML string as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware. Any parsing errors are silently ignored.
     *
     * @param xml            the XML content to be parsed (must be well formed)
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document, or null
     */
    @Nullable
    public static Document parseDocumentSilently(@NotNull String xml, boolean namespaceAware) {
        try {
            return parseDocument(xml, namespaceAware);
        } catch (Exception e) {
            NullLogger.getLogger().error(e, null);
            e.printStackTrace(System.err);
            // pass
            // This method is deliberately silent; will return null
        }

        return null;
    }

    /**
     * Dump an XML tree to string. This does not perform any pretty printing.
     * To perform pretty printing, use {@code XmlPrettyPrinter.prettyPrint(node)} in
     * {@code sdk-common}.
     */
    public static String toXml(Node node, boolean preserveWhitespace) {
        StringBuilder sb = new StringBuilder(1000);
        append(sb, node, 0);
        return sb.toString();
    }

    /** Dump node to string without indentation adjustments */
    private static void append(
            @NotNull StringBuilder sb,
            @NotNull Node node,
            int indent) {
        short nodeType = node.getNodeType();
        switch (nodeType) {
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE: {
                sb.append(XML_PROLOG);
                NodeList children = node.getChildNodes();
                for (int i = 0, n = children.getLength(); i < n; i++) {
                    append(sb, children.item(i), indent);
                }
                break;
            }
            case Node.COMMENT_NODE:
                sb.append(XML_COMMENT_BEGIN);
                sb.append(node.getNodeValue());
                sb.append(XML_COMMENT_END);
                break;
            case Node.TEXT_NODE: {
                sb.append(toXmlTextValue(node.getNodeValue()));
                break;
            }
            case Node.ELEMENT_NODE: {
                sb.append('<');
                Element element = (Element) node;
                sb.append(element.getTagName());

                NamedNodeMap attributes = element.getAttributes();
                NodeList children = element.getChildNodes();
                int childCount = children.getLength();
                int attributeCount = attributes.getLength();

                if (attributeCount > 0) {
                    for (int i = 0; i < attributeCount; i++) {
                        Node attribute = attributes.item(i);
                        sb.append(' ');
                        sb.append(attribute.getNodeName());
                        sb.append('=').append('"');
                        sb.append(toXmlAttributeValue(attribute.getNodeValue()));
                        sb.append('"');
                    }
                }

                if (childCount == 0) {
                    sb.append('/');
                }
                sb.append('>');
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        Node child = children.item(i);
                        append(sb, child, indent + 1);
                    }
                    sb.append('<').append('/');
                    sb.append(element.getTagName());
                    sb.append('>');
                }
                break;
            }

            default:
                throw new UnsupportedOperationException(
                        "Unsupported node type " + nodeType + ": not yet implemented");
        }
    }

    /**
     * Finds all direct sub tags of the given node by attribute value.
     *
     * @param tag              The node in which to look for sub nodes
     * @param attributeName     The name of the attribute to look for
     * @param attributeValue    The attribute value that shall be found
     * @return A list of all direct sub nodes matching the specified attribute's value
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @NotNull
    public static XmlTag[] findSubNodeByAttribute(@NotNull XmlTag tag, @NotNull String attributeName, @NotNull String attributeValue) {
        List<XmlTag> retVal = new ArrayList<>();

        XmlTag[] subTags = tag.getSubTags();

        if(subTags.length > 0) {
            XmlAttribute attr;

            for (XmlTag candidate : subTags) {
                attr = candidate.getAttribute(attributeName);

                if (attr != null && attr.getValue().equals(attributeValue)) {
                    retVal.add(candidate);
                }
            }
        }

        return retVal.toArray(new XmlTag[retVal.size()]);
    }

    /**
     * Format the given floating value into an XML string, omitting decimals if
     * 0
     *
     * @param value the value to be formatted
     * @return the corresponding XML string for the value
     */
    public static String formatFloatAttribute(double value) {
        if (value != (int) value) {
            // Run String.format without a locale, because we don't want locale-specific
            // conversions here like separating the decimal part with a comma instead of a dot!
            return String.format((Locale) null, "%.2f", value); //$NON-NLS-1$
        } else {
            return Integer.toString((int) value);
        }
    }

    public static class ValueHelper {

        /**
         * Replaces escapes in an XML resource string with the actual characters,
         * performing unicode substitutions (replacing any {@code \\uNNNN} references in the
         * given string with the corresponding unicode characters), etc.
         *
         * @param s the string to unescape
         * @param escapeEntities XML entities
         * @param trim whether surrounding space and quotes should be trimmed
         * @return the string with the escape characters removed and expanded
         */
        @SuppressWarnings("UnnecessaryContinue")
        @Nullable
        public static String unescapeResourceString(
                @Nullable String s,
                boolean escapeEntities, boolean trim) {
            if (s == null) {
                return null;
            }

            // Trim space surrounding optional quotes
            int i = 0;
            int n = s.length();
            boolean quoted = false;
            if (trim) {
                while (i < n) {
                    char c = s.charAt(i);
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                    i++;
                }
                while (n > i) {
                    char c = s.charAt(n - 1);
                    if (!Character.isWhitespace(c)) {
                        //See if this was a \, and if so, see whether it was escaped
                        if (n < s.length() && isEscaped(s, n)) {
                            n++;
                        }
                        break;
                    }
                    n--;
                }

                // Trim surrounding quotes. Note that there can be *any* number of these, and
                // the left side and right side do not have to match; e.g. you can have
                //    """"f"" => f
                int quoteEnd = n;
                while (i < n) {
                    char c = s.charAt(i);
                    if (c != '"') {
                        break;
                    }
                    quoted = true;
                    i++;
                }
                // Searching backwards is slightly more complicated; make sure we don't trim
                // quotes that have been escaped.
                if (quoted) {
                    while (n > i) {
                        char c = s.charAt(n - 1);
                        if (c != '"') {
                            if (n < s.length() && isEscaped(s, n)) {
                                n++;
                            }
                            break;
                        }
                        n--;
                    }
                }
                if (n == i) {
                    return ""; //$NON-NLS-1$
                }

                // Only trim leading spaces if we didn't already process a leading quote:
                if (!quoted) {
                    while (i < n) {
                        char c = s.charAt(i);
                        if (!Character.isWhitespace(c)) {
                            break;
                        }
                        i++;
                    }

                    // Only trim trailing spaces if we didn't already process a trailing quote:
                    if (n == quoteEnd) {
                        while (n > i) {
                            char c = s.charAt(n - 1);
                            if (!Character.isWhitespace(c)) {
                                //See if this was a \, and if so, see whether it was escaped
                                if (n < s.length() && isEscaped(s, n)) {
                                    n++;
                                }
                                break;
                            }
                            n--;
                        }
                    }
                    if (n == i) {
                        return ""; //$NON-NLS-1$
                    }
                }
            }

            // Perform a single pass over the string and see if it contains
            // (1) spaces that should be converted (e.g. repeated spaces or a newline which
            // should be converted to a space)
            // (2) escape characters (\ and &) which will require expansions
            // If we find neither of these, we can simply return the string
            boolean rewriteWhitespace = false;
            if (!quoted) {
                // See if we need to fold adjacent spaces
                boolean prevSpace = false;
                boolean hasEscape = false;
                for (int curr = i; curr < n; curr++) {
                    char c = s.charAt(curr);
                    if (c == '\\' || c == '&') {
                        hasEscape = true;
                    }
                    boolean isSpace = Character.isWhitespace(c);
                    if (isSpace && prevSpace) {
                        // fold adjacent spaces
                        rewriteWhitespace = true;
                    } else if (c == '\n') {
                        // rewrite newlines as spaces
                        rewriteWhitespace = true;
                    }
                    prevSpace = isSpace;
                }

                if (!trim) {
                    rewriteWhitespace = false;
                }

                // If no surrounding whitespace and no escape characters, no need to do any
                // more work
                if (!rewriteWhitespace && !hasEscape && i == 0 && n == s.length()) {
                    return s;
                }
            }

            StringBuilder sb = new StringBuilder(n - i);
            boolean prevSpace = false;
            for (; i < n; i++) {
                char c = s.charAt(i);
                if (c == '\\' && i < n - 1) {
                    prevSpace = false;
                    char next = s.charAt(i + 1);
                    // Unicode escapes
                    if (next == 'u' && i < n - 5) { // case sensitive
                        String hex = s.substring(i + 2, i + 6);
                        try {
                            int unicodeValue = Integer.parseInt(hex, 16);
                            sb.append((char) unicodeValue);
                            i += 5;
                            continue;
                        } catch (NumberFormatException e) {
                            // Invalid escape: Just proceed to literally transcribe it
                            sb.append(c);
                        }
                    } else if (next == 'n') {
                        sb.append('\n');
                        i++;
                        continue;
                    } else if (next == 't') {
                        sb.append('\t');
                        i++;
                        continue;
                    } else {
                        sb.append(next);
                        i++;
                        continue;
                    }
                } else {
                    if (c == '&' && escapeEntities) {
                        prevSpace = false;
                        if (s.regionMatches(true, i, LT_ENTITY, 0, LT_ENTITY.length())) {
                            sb.append('<');
                            i += LT_ENTITY.length() - 1;
                            continue;
                        } else if (s.regionMatches(true, i, AMP_ENTITY, 0, AMP_ENTITY.length())) {
                            sb.append('&');
                            i += AMP_ENTITY.length() - 1;
                            continue;
                        } else if (s.regionMatches(true, i, QUOT_ENTITY, 0, QUOT_ENTITY.length())) {
                            sb.append('"');
                            i += QUOT_ENTITY.length() - 1;
                            continue;
                        } else if (s.regionMatches(true, i, APOS_ENTITY, 0, APOS_ENTITY.length())) {
                            sb.append('\'');
                            i += APOS_ENTITY.length() - 1;
                            continue;
                        } else if (s.regionMatches(true, i, GT_ENTITY, 0, GT_ENTITY.length())) {
                            sb.append('>');
                            i += GT_ENTITY.length() - 1;
                            continue;
                        } else if (i < n - 2 && s.charAt(i + 1) == '#') {
                            int end = s.indexOf(';', i + 1);
                            if (end != -1) {
                                char first = s.charAt(i + 2);
                                boolean hex = first == 'x' || first == 'X';
                                String number = s.substring(i + (hex ? 3 : 2), end);
                                try {
                                    int unicodeValue = Integer.parseInt(number, hex ? 16 : 10);
                                    sb.append((char) unicodeValue);
                                    i = end;
                                    continue;
                                } catch (NumberFormatException e) {
                                    // Invalid escape: Just proceed to literally transcribe it
                                    sb.append(c);
                                }
                            } else {
                                // Invalid escape: Just proceed to literally transcribe it
                                sb.append(c);
                            }
                        }
                    }

                    if (rewriteWhitespace) {
                        boolean isSpace = Character.isWhitespace(c);
                        if (isSpace) {
                            if (!prevSpace) {
                                sb.append(' '); // replace newlines etc with a plain space
                            }
                        } else {
                            sb.append(c);
                        }
                        prevSpace = isSpace;
                    } else {
                        sb.append(c);
                    }
                }
            }
            s = sb.toString();

            return s;
        }

        /**
         * Returns true if the character at the given offset in the string is escaped
         * (the previous character is a \, and that character isn't itself an escaped \)
         *
         * @param s the string
         * @param index the index of the character in the string to check
         * @return true if the character is escaped
         */
        static boolean isEscaped(String s, int index) {
            if (index == 0 || index == s.length()) {
                return false;
            }
            int prevPos = index - 1;
            char prev = s.charAt(prevPos);
            if (prev != '\\') {
                return false;
            }
            // The character *may* be escaped; not sure if the \ we ran into is
            // an escape character, or an escaped backslash; we have to search backwards
            // to be certain.
            int j = prevPos - 1;
            while (j >= 0) {
                if (s.charAt(j) != '\\') {
                    break;
                }
                j--;
            }
            // If we passed an odd number of \'s, the space is escaped
            return (prevPos - j) % 2 == 1;
        }

        /**
         * Escape a string value to be placed in a string resource file such that it complies with
         * the escaping rules described here:
         *   http://developer.android.com/guide/topics/resources/string-resource.html
         * More examples of the escaping rules can be found here:
         *   http://androidcookbook.com/Recipe.seam?recipeId=2219&recipeFrom=ViewTOC
         * This method assumes that the String is not escaped already.
         *
         * Rules:
         * <ul>
         * <li>Double quotes are needed if string starts or ends with at least one space.
         * <li>{@code @, ?} at beginning of string have to be escaped with a backslash.
         * <li>{@code ', ", \} have to be escaped with a backslash.
         * <li>{@code <, >, &} have to be replaced by their predefined xml entity.
         * <li>{@code \n, \t} have to be replaced by a backslash and the appropriate character.
         * </ul>
         * @param s the string to be escaped
         * @return the escaped string as it would appear in the XML text in a values file
         */
        public static String escapeResourceString(String s) {
            int n = s.length();
            if (n == 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder(s.length() * 2);
            boolean hasSpace = s.charAt(0) == ' ' || s.charAt(n - 1) == ' ';

            if (hasSpace) {
                sb.append('"');
            } else if (s.charAt(0) == '@' || s.charAt(0) == '?') {
                sb.append('\\');
            }

            for (int i = 0; i < n; ++i) {
                char c = s.charAt(i);
                switch (c) {
                    case '\'':
                        if (!hasSpace) {
                            sb.append('\\');
                        }
                        sb.append(c);
                        break;
                    case '"':
                    case '\\':
                        sb.append('\\');
                        sb.append(c);
                        break;
                    case '<':
                        sb.append(LT_ENTITY);
                        break;
                    case '&':
                        sb.append(AMP_ENTITY);
                        break;
                    case '\n':
                        sb.append("\\n"); //$NON-NLS-1$
                        break;
                    case '\t':
                        sb.append("\\t"); //$NON-NLS-1$
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }

            if (hasSpace) {
                sb.append('"');
            }

            return sb.toString();
        }
    }
}
