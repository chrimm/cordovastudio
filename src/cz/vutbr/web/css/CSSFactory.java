/*
 * Copyright (C) kapy
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.css;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlDocument;
import cz.vutbr.web.csskit.MatchConditionImpl;
import cz.vutbr.web.csskit.antlr.CSSParserFactory;
import cz.vutbr.web.csskit.antlr.CSSParserFactory.SourceType;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.DeclarationTransformer;
import cz.vutbr.web.domassign.StyleMap;
import cz.vutbr.web.domassign.Traversal;
import org.fit.net.DataURLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This class is abstract factory for other factories used during CSS parsing.
 * Use it, for example, to retrieve current(default) TermFactory,
 * current(default) SupportedCSS implementation and so on.
 * <p>
 * Factories need to be registered first. By default, CSSFactory uses
 * automatically for implementations:
 * <code>cz.vutbr.web.csskit.TermFactoryImpl</code>
 * <code>cz.vutbr.web.domassign.SupportedCSS3</code>
 * <code>cz.vutbr.web.csskit.RuleFactoryImpl</code>
 * <code>cz.vutbr.web.domassign.SingleMapNodeData</code>
 * <p>
 * Other usage of this factory is to parse either string or file into
 * StyleSheet.
 * <p>
 * Whole conversion between DOM tree and mapping of CSS to DOM tree elements
 * could be done by invoking method {@code assignDOM()}
 *
 * @author kapy
 */
public final class CSSFactory {
    private static Logger log = LoggerFactory.getLogger(CSSFactory.class);

    private static final String DEFAULT_TERM_FACTORY = "cz.vutbr.web.csskit.TermFactoryImpl";
    private static final String DEFAULT_SUPPORTED_CSS = "cz.vutbr.web.domassign.SupportedCSS3";
    private static final String DEFAULT_RULE_FACTORY = "cz.vutbr.web.csskit.RuleFactoryImpl";
    private static final String DEFAULT_DECLARATION_TRANSFORMER = "cz.vutbr.web.domassign.DeclarationTransformer";
    private static final String DEFAULT_NODE_DATA_IMPL = "cz.vutbr.web.domassign.SingleMapNodeData";

    /**
     * Default instance of TermFactory implementation
     */
    private static TermFactory tf;

    /**
     * Default instance of SupportedCSS implementation
     */
    private static SupportedCSS css;

    /**
     * Default instance of RuleFactory implementation
     */
    private static RuleFactory rf;

    /**
     * Default instance of DeclarationTransformer
     */
    private static DeclarationTransformer dt;

    /**
     * Used NodeData class
     */
    private static Class<? extends NodeData> ndImpl;

    /**
     * Default match condition
     */
    private static MatchCondition dcond;

    /**
     * Whether to allow lengths with no units and interpret them as pixels.
     */
    private static boolean implyPixelLengths = false;


    /**
     * Media specification used for automatically importing style sheets.
     */
    private static MediaSpec autoImportMedia = null;

    /**
     * Sets whether to allow lengths with no units and interpret them as pixels. The default value is {@code false}.
     *
     * @param b {@code true} when the lengths with no units should be allowed.
     */
    public static final void setImplyPixelLength(boolean b) {
        implyPixelLengths = b;
    }

    /**
     * Says whether to allow lengths with no units and interpret them as pixels.
     *
     * @return {@code true} if the lengths with no units are allowed
     */
    public static final boolean getImplyPixelLength() {
        return implyPixelLengths;
    }

    /**
     * Obtains the media specification used for automatical style import. The parser will
     * automatically download the style sheets imported using the {@code @import} rules
     * when they corespond to the given media specification.
     *
     * @return The media specification.
     */
    public static MediaSpec getAutoImportMedia() {
        if (autoImportMedia == null)
            autoImportMedia = new MediaSpecAll();
        return autoImportMedia;
    }

    /**
     * Sets the media specification used for automatical style import. The parser will
     * automatically download the style sheets imported using the {@code @import} rules
     * when they corespond to the given media specification. The default is "all" i.e.
     * all the imported style sheets are automatically parsed.
     *
     * @param autoImportMedia The media specification for checking the imports.
     */
    public static void setAutoImportMedia(MediaSpec autoImportMedia) {
        CSSFactory.autoImportMedia = autoImportMedia;
    }

    /**
     * Registers new TermFactory instance
     *
     * @param newFactory New TermFactory
     */
    public static final void registerTermFactory(TermFactory newFactory) {
        tf = newFactory;
    }

    /**
     * Returns TermFactory registered in step above
     *
     * @return TermFactory registered
     */
    public static final TermFactory getTermFactory() {
        if (tf == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends TermFactory> clazz = (Class<? extends TermFactory>) Class
                        .forName(DEFAULT_TERM_FACTORY);
                Method m = clazz.getMethod("getInstance");
                registerTermFactory((TermFactory) m.invoke(null));
                log.debug("Retrived {} as default TermFactory implementation.",
                        DEFAULT_TERM_FACTORY);
            } catch (Exception e) {
                log.error("Unable to get TermFactory from default", e);
                throw new RuntimeException(
                        "No TermFactory implementation registered!");
            }
        }
        return tf;
    }

    /**
     * Registers new SupportedCSS instance
     *
     * @param newCSS new SupportedCSS
     */
    public static final void registerSupportedCSS(SupportedCSS newCSS) {
        css = newCSS;
    }

    /**
     * Returns registered SupportedCSS
     *
     * @return SupportedCSS instance
     */
    public static final SupportedCSS getSupportedCSS() {
        if (css == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends SupportedCSS> clazz = (Class<? extends SupportedCSS>) Class
                        .forName(DEFAULT_SUPPORTED_CSS);
                Method m = clazz.getMethod("getInstance");
                registerSupportedCSS((SupportedCSS) m.invoke(null));
                log.debug(
                        "Retrived {} as default SupportedCSS implementation.",
                        DEFAULT_SUPPORTED_CSS);
            } catch (Exception e) {
                log.error("Unable to get SupportedCSS from default", e);
                throw new RuntimeException(
                        "No SupportedCSS implementation registered!");
            }
        }
        return css;
    }

    /**
     * Registers new RuleFactory
     *
     * @param newRuleFactory New RuleFactory instance
     */
    public static final void registerRuleFactory(RuleFactory newRuleFactory) {
        rf = newRuleFactory;
    }

    /**
     * Returns registered RuleFactory
     *
     * @return RuleFactory instance
     */
    public static final RuleFactory getRuleFactory() {
        if (rf == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends RuleFactory> clazz = (Class<? extends RuleFactory>) Class
                        .forName(DEFAULT_RULE_FACTORY);
                Method m = clazz.getMethod("getInstance");
                registerRuleFactory((RuleFactory) m.invoke(null));
                log.debug("Retrived {} as default RuleFactory implementation.",
                        DEFAULT_RULE_FACTORY);
            } catch (Exception e) {
                log.error("Unable to get RuleFactory from default", e);
                throw new RuntimeException(
                        "No RuleFactory implementation registered!");
            }
        }

        return rf;
    }

    /**
     * Registers new DeclarationTransformer
     *
     * @param newDeclarationTransformer New DeclarationTransformer instance
     */
    public static final void registerDeclarationTransformer(DeclarationTransformer newDeclarationTransformer) {
        dt = newDeclarationTransformer;
    }

    /**
     * Returns the registered DeclarationTransformer
     *
     * @return DeclarationTransformer instance
     */
    public static final DeclarationTransformer getDeclarationTransformer() {
        if (dt == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends DeclarationTransformer> clazz = (Class<? extends DeclarationTransformer>) Class
                        .forName(DEFAULT_DECLARATION_TRANSFORMER);
                Method m = clazz.getMethod("getInstance");
                registerDeclarationTransformer((DeclarationTransformer) m.invoke(null));
                log.debug("Retrived {} as default DeclarationTransformer implementation.",
                        DEFAULT_DECLARATION_TRANSFORMER);
            } catch (Exception e) {
                log.error("Unable to get DeclarationTransformer from default", e);
                throw new RuntimeException(
                        "No DeclarationTransformer implementation registered!");
            }
        }

        return dt;
    }

    /**
     * Registers a new default match condition to be used for matching the elements and selector parts.
     *
     * @param newMatchCondition the new match condition
     */
    public static final void registerDefaultMatchCondition(MatchCondition newMatchCondition) {
        dcond = newMatchCondition;
    }

    /**
     * Obtains the default match condition to be used for matching the elements and selector parts.
     *
     * @return the default match condition used by this factory.
     */
    public static final MatchCondition getDefaultMatchCondition() {
        if (dcond == null)
            dcond = new MatchConditionImpl(); //use the default match condition when nothing is registered
        return dcond;
    }

    /**
     * Registers node data instance. Instance must provide no-argument
     * Constructor
     *
     * @param clazz Instance class
     */
    public static final void registerNodeDataInstance(
            Class<? extends NodeData> clazz) {
        try {
            @SuppressWarnings("unused")
            NodeData test = clazz.newInstance();
            ndImpl = clazz;
        } catch (InstantiationException e) {
            throw new RuntimeException("NodeData implemenation ("
                    + clazz.getName() + ") doesn't provide sole constructor", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("NodeData implementation ("
                    + clazz.getName() + ") is not accesible", e);
        }

    }

    /**
     * Creates instance of NodeData
     *
     * @return Instance of NodeData
     */
    public static final NodeData createNodeData() {
        if (ndImpl == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends NodeData> clazz = (Class<? extends NodeData>) Class
                        .forName(DEFAULT_NODE_DATA_IMPL);
                registerNodeDataInstance(clazz);
                log.debug("Registered {} as default NodeData instance.",
                        DEFAULT_NODE_DATA_IMPL);
            } catch (Exception e) {
            }
        }

        try {
            return ndImpl.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No NodeData implementation registered");
        }
    }

    /**
     * Parses URL into StyleSheet
     *
     * @param url      URL of file to be parsed
     * @param encoding Encoding of file
     * @return Parsed StyleSheet
     * @throws CSSException When exception during parse occurs
     * @throws IOException  When file not found
     */
    public static final StyleSheet parse(URL url, String encoding)
            throws CSSException, IOException {
        return CSSParserFactory.parse((Object) url, encoding, SourceType.URL, url);
    }

    /**
     * Parses file into StyleSheet. Internally transforms file to URL
     *
     * @param fileName Name of file
     * @param encoding Encoding used to parse input
     * @return Parsed style sheet
     * @throws CSSException In case that parsing error occurs
     * @throws IOException  If file is not found or not readable
     */
    public static final StyleSheet parse(String fileName, String encoding)
            throws CSSException, IOException {

        try {
            File f = new File(fileName);
            URL url = f.toURI().toURL();
            return parse(url, encoding);
        } catch (MalformedURLException e) {
            String message = "Unable to construct URL from fileName: "
                    + fileName;
            log.error(message);
            throw new FileNotFoundException(message);
        }
    }

    /**
     * Parses text into StyleSheet
     *
     * @param css Text with CSS declarations
     * @return Parsed StyleSheet
     * @throws IOException  When exception during read occurs
     * @throws CSSException When exception during parse occurs
     */
    public static final StyleSheet parse(String css) throws IOException,
            CSSException {
        URL base = new URL("file:///base/url/is/not/specified"); //Cannot determine the base URI in this method but we need some base URI for relative URLs
        return CSSParserFactory.parse(css, null, SourceType.EMBEDDED, base);
    }

    /**
     * Loads all the style sheets used from the specified DOM tree.
     * The following style specifications are evaluated:
     * <ul>
     * <li>The style sheets included using the <code>link</code> and <code>style</code> tags.
     * <li>Inline styles specified using the <code>style</code> element attribute.
     * <li><strong>Proprietary extension:</strong> Default styles defined using the <code>XDefaultStyle</code>
     * element attribute. These styles behave the same way as the inline styles but they have the lowest priority
     * (the values are used only when not redefined by any other way)
     * </ul>
     *
     * @param doc      DOM tree
     * @param encoding The default encoding used for the referenced style sheets
     * @param base     Base URL against which all files are searched
     * @param media    Selected media for style sheet
     * @return the rules of all the style sheets used in the document including the inline styles
     */
    public static final StyleSheet getUsedStyles(XmlDocument doc, String encoding, URL base, MediaSpec media) {
        Pair pair = new Pair(base, media);

        Traversal<StyleSheet> traversal = new CSSAssignTraversal(doc, encoding, (Object) pair);

        StyleSheet style = (StyleSheet) getRuleFactory().createStyleSheet().unlock();
        traversal.listTraversal(style);
        return style;
    }

    /**
     * This is the same as {@link CSSFactory#getUsedStyles(XmlDocument, String, URL, MediaSpec)} but only the
     * media type is provided instead of the complete media specification.
     *
     * @param doc      DOM tree
     * @param encoding The default encoding used for the referenced style sheets
     * @param base     Base URL against which all files are searched
     * @param media    Selected media for style sheet
     * @return the rules of all the style sheets used in the document including the inline styles
     */
    public static final StyleSheet getUsedStyles(XmlDocument doc, String encoding, URL base, String media) {
        return getUsedStyles(doc, encoding, base, new MediaSpec(media));
    }

    @Deprecated
    public static final StyleSheet getUsedStyles(XmlDocument doc, URL base, String media) {
        return getUsedStyles(doc, null, base, media);
    }

    /**
     * Goes through a DOM tree and assigns the CSS declarations to the DOM elements.
     * The following style specifications are evaluated:
     * <ul>
     * <li>The style sheets included using the <code>link</code> and <code>style</code> tags.
     * <li>Inline styles specified using the <code>style</code> element attribute.
     * <li><strong>Proprietary extension:</strong> Default styles defined using the <code>XDefaultStyle</code>
     * element attribute. These styles behave the same way as the inline styles but they have the lowest priority
     * (the values are used only when not redefined by any other way)
     * </ul>
     *
     * @param doc            DOM tree
     * @param encoding       The default encoding used for the referenced style sheets
     * @param base           Base URL against which all files are searched
     * @param media          Current media specification used for evaluating the media queries
     * @param useInheritance Whether inheritance will be used to determine values
     * @return Map between DOM element nodes and data structure containing CSS
     * information
     */
    public static final StyleMap assignDOM(XmlDocument doc, String encoding,
                                           URL base, MediaSpec media, boolean useInheritance) {

        Pair pair = new Pair(base, media);

        Traversal<StyleSheet> traversal = new CSSAssignTraversal(doc, encoding, (Object) pair);

        StyleSheet style = (StyleSheet) getRuleFactory().createStyleSheet()
                .unlock();
        traversal.listTraversal(style);

        Analyzer analyzer = new Analyzer(style);
        return analyzer.evaluateDOM(doc, media, useInheritance);
    }

    /**
     * This is the same as {@link CSSFactory#assignDOM(XmlDocument, String, URL, MediaSpec, boolean)} but only the
     * media type is provided instead of the complete media specification.
     *
     * @param doc            DOM tree
     * @param encoding       The default encoding used for the referenced style sheets
     * @param base           Base URL against which all files are searched
     * @param media          Selected media type for style sheet
     * @param useInheritance Whether inheritance will be used to determine values
     * @return Map between DOM element nodes and data structure containing CSS
     * information
     */
    public static final StyleMap assignDOM(XmlDocument doc, String encoding,
                                           URL base, String media, boolean useInheritance) {

        return assignDOM(doc, encoding, base, new MediaSpec(media), useInheritance);
    }

    @Deprecated
    public static final StyleMap assignDOM(XmlDocument doc,
                                           URL base, String media, boolean useInheritance) {
        return assignDOM(doc, null, base, media, useInheritance);
    }

    // ========================================================================

    /**
     * Walks (X)HTML document and collects style information
     *
     * @author kapy
     */
    private static final class CSSAssignTraversal extends Traversal<StyleSheet> {

        private String encoding;

        public CSSAssignTraversal(XmlDocument doc, String encoding, Object source) {
            super(doc, source);
            this.encoding = encoding;
        }

        @Override
        protected void processTag(StyleSheet result, HtmlTag current, Object source) {
            // base uri
            URL base = ((Pair) source).base;
            // allowed media
            MediaSpec media = ((Pair) source).media;
            Element elem = (Element) current;

            try {
                // embedded style-sheet
                if (isEmbeddedStyleSheet(elem, media)) {
                    result = CSSParserFactory.append(extractElementText(elem), null,
                            SourceType.EMBEDDED, result, base);
                    log.debug("Matched embedded CSS style");
                }
                // linked style-sheet
                else if (isLinkedStyleSheet(elem, media)) {
                    URL uri = DataURLHandler.createURL(base, elem.getAttribute("href"));
                    result = CSSParserFactory.append(uri, encoding, SourceType.URL,
                            result, uri);
                    log.debug("Matched linked CSS style");
                }
                // in-line style and default style
                else {
                    if (elem.getAttribute("style") != null && elem.getAttribute("style").length() > 0) {
                        result = CSSParserFactory.append(
                                elem.getAttribute("style"), null, SourceType.INLINE,
                                elem, true, result, base);
                        log.debug("Matched inline CSS style");
                    }
                    if (elem.getAttribute("XDefaultStyle") != null && elem.getAttribute("XDefaultStyle").length() > 0) {
                        result = CSSParserFactory.append(
                                elem.getAttribute("XDefaultStyle"), null, SourceType.INLINE,
                                elem, false, result, base);
                        log.debug("Matched default CSS style");
                    }
                }
            } catch (CSSException ce) {
                log.error("THROWN:", ce);
            } catch (IOException ioe) {
                log.error("THROWN:", ioe);
            }

        }

        private static boolean isEmbeddedStyleSheet(Element e, MediaSpec media) {
            return "style".equalsIgnoreCase(e.getNodeName())
                    && isAllowedMedia(e, media);
        }

        private static boolean isLinkedStyleSheet(Element e, MediaSpec media) {
            return e.getNodeName().equalsIgnoreCase("link")
                    && (e.getAttribute("rel").toLowerCase().contains("stylesheet"))
                    && (e.getAttribute("type").isEmpty() || "text/css".equalsIgnoreCase(e.getAttribute("type")))
                    && isAllowedMedia(e, media);
        }

        /**
         * Extracts element's text, if any
         *
         * @param e Element
         * @return Element's text
         */
        private static String extractElementText(Element e) {
            Node text = e.getFirstChild();
            if (text != null && text.getNodeType() == Node.TEXT_NODE)
                return ((Text) text).getData();
            return "";
        }

        /**
         * Checks allowed media by searching for {@code media} attribute on
         * element and its content
         *
         * @param e     (STYLE) Element
         * @param media Current media specification used for parsing
         * @return {@code true} if allowed, {@code false} otherwise
         */
        private static boolean isAllowedMedia(Element e, MediaSpec media) {
            String attr = e.getAttribute("media");
            if (attr != null && attr.length() > 0) {
                attr = attr.trim();
                if (attr.length() > 0) {
                    List<MediaQuery> ql = CSSParserFactory.parseMediaQuery(attr);
                    if (ql != null) {
                        for (MediaQuery q : ql) {
                            if (media.matches(q))
                                return true; //found a matching media query
                        }
                        return false; //no matching media query
                    } else
                        return false; //no usable media queries (malformed string?)
                } else
                    return media.matchesEmpty(); //empty query string
            } else
                return media.matchesEmpty(); //no media queries
        }
    }

    // holds pair with URL base and the required media
    private static final class Pair {
        public URL base;
        public MediaSpec media;

        public Pair(URL base, MediaSpec media) {
            this.base = base;
            this.media = media;
        }
    }

}
