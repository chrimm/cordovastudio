/*
 * DOMAnalyzer.java
 * Copyright (c) 2005-2007 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 20. leden 2005, 14:08
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */

package org.cordovastudio.editors.designer.rendering.engines.cssBox.css;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import cz.vutbr.web.css.*;
import cz.vutbr.web.css.Selector.PseudoDeclaration;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.StyleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;


/**
 * This class implements methods for the DOM tree style analysis.
 *
 * @author burgetr
 */
public class DOMAnalyzer {
    private static Logger log = LoggerFactory.getLogger(DOMAnalyzer.class);
    private static final String DEFAULT_MEDIA = "screen";

    private XmlDocument doc;   //the root node of the DOM tree
    private URL baseUrl;    //base URL
    private MediaSpec media;   //media type
    private String encoding; //default character encoding for style sheet parsing

    private Vector<StyleSheet> styles;  //vector of StyleSheet sheets
    private Analyzer analyzer; //style sheet analyzer
    private StyleMap stylemap; //style map for DOM nodes
    private StyleMap istylemap; //style map with inheritance

    /**
     * The origin of a style sheet
     */
    public enum Origin {
        /**
         * An author style sheet (this is the default in most cases)
         */
        AUTHOR,
        /**
         * A user agent style sheet
         */
        AGENT,
        /**
         * A user tstyle sheet
         */
        USER
    }

    ;

    /**
     * Creates a new DOM analyzer.
     *
     * @param doc the document to be analyzed
     */
    public DOMAnalyzer(XmlDocument doc) {
        this.doc = doc;
        this.media = new MediaSpec(DEFAULT_MEDIA);
        baseUrl = null;
        encoding = null;
        styles = new Vector<StyleSheet>();
        stylemap = null;
        istylemap = null;
    }

    /**
     * Creates a new DOM analyzer.
     *
     * @param doc     the document to be analyzed
     * @param baseUrl the base URL for loading the style sheets. This URL may be redefined by the <code>&lt;base&gt;</code> tag used in the
     *                document header.
     */
    public DOMAnalyzer(XmlDocument doc, URL baseUrl) {
        this(doc, baseUrl, true);
    }

    /**
     * Creates a new DOM analyzer.
     *
     * @param doc        the document to be analyzed
     * @param baseUrl    the base URL for loading the style sheets. If <code>detectBase</code>, this URL may be redefined by the <code>&lt;base&gt;</code> tag used in the
     *                   document header.
     * @param detectBase sets whether to try to accept the <code>&lt;base&gt;</code> tags in the document header.
     */
    public DOMAnalyzer(XmlDocument doc, URL baseUrl, boolean detectBase) {
        this.doc = doc;
        this.encoding = null;
        this.media = new MediaSpec(DEFAULT_MEDIA);
        styles = new Vector<StyleSheet>();
        this.baseUrl = baseUrl;
        if (detectBase) {
            String docbase = getDocumentBase();
            if (docbase != null) {
                try {
                    this.baseUrl = new URL(baseUrl, docbase);
                    log.info("Using specified document base " + this.baseUrl);
                } catch (MalformedURLException e) {
                    log.warn("Malformed base URL " + docbase);
                }
            }
        }
        stylemap = null;
        istylemap = null;
    }

    /**
     * Obtains the default character encoding used for parsing the style sheets. This encoding is used when there is
     * no other encoding specified inside the style sheet using the <code>@charset</code> rule. When there
     * is no default encoding specified, this method returns <code>null</code> and the system default encoding
     * is used for style sheets.
     *
     * @return The encoding name or <code>null</code> when there is no default encoding specified.
     */
    public String getDefaultEncoding() {
        return encoding;
    }

    /**
     * Sets the default character encoding used for parsing the referenced style sheets. This encoding is used when there is
     * no other encoding specified inside the style sheet using the <code>@charset</code> rule.
     *
     * @param encoding The encoding string or <code>null</code> for using the system default encoding.
     */
    public void setDefaultEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns the type of the current medium used.
     *
     * @return the media type according to CSS
     */
    public String getMedia() {
        return media.getType();
    }

    /**
     * Set the type of the medium used for computing the style. Default is "screen".
     *
     * @param media the medium to set
     */
    public void setMedia(String media) {
        this.media = new MediaSpec(media);
    }

    /**
     * Returns the full specification of the currently used medium.
     *
     * @return the media specification according to CSS
     */
    public MediaSpec getMediaSpec() {
        return media;
    }

    /**
     * Set the specification of the medium used for computing the style. The default is "screen" with
     * some standard features that correspond to a normal desktop station.
     *
     * @param media the medium to set
     */
    public void setMediaSpec(MediaSpec media) {
        this.media = media;
    }

    /**
     * Returns the root element of the document.
     */
    public HtmlTag getRoot() {
        return (HtmlTag) doc.getRootTag();
    }

    /**
     * Returns the &lt;head&gt; element.
     */
    public HtmlTag getHead() {
        HtmlTag root = getRoot();

        if (getRoot() != null) {
            return (HtmlTag) root.findFirstSubTag("head");
        }

        return null;
    }

    /**
     * Returns the &lt;body&gt; element.
     */
    public HtmlTag getBody() {
        HtmlTag root = getRoot();

        if (root != null) {
            return (HtmlTag) root.findFirstSubTag("body");
        }

        return null;
    }

    /**
     * Finds the explicitly specified base URL in the document according to the HTML specification
     *
     * @return the base URL string, when present in the document or null when not present
     * @see <a href="http://www.w3.org/TR/html4/struct/links.html#edef-BASE">the BASE element definition</a>
     */
    public String getDocumentBase() {
        HtmlTag head = getHead();

        if (head != null) {
            HtmlTag base = (HtmlTag) head.findFirstSubTag("base");

            if (base != null) {
                XmlAttribute href = base.getAttribute("href");

                if (href != null) {
                    return href.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Finds the explicitly specified character encoding using the <code>&lt;meta&gt;</code> tag according
     * to the HTML specifiaction.
     *
     * @return the encoding name when present in the document or <code>null</code> if not present.
     */
    public String getCharacterEncoding() {
        HtmlTag head = getHead();

        if (head != null) {
            for (HtmlTag meta : (HtmlTag[]) head.findSubTags("meta")) {
                XmlAttribute httpEquiv = meta.getAttribute("http-equiv");
                XmlAttribute charset = meta.getAttribute("charset");

                if (httpEquiv != null) {
                    String httpEquivValue = httpEquiv.getValue();
                    if (httpEquivValue != null && httpEquivValue.equalsIgnoreCase("content-type")
                            && meta.getAttribute("content") != null) {
                        String ctype = httpEquiv.getValue().toLowerCase();
                        int cpos = ctype.indexOf("charset=");
                        if (cpos != -1)
                            return ctype.substring(cpos + 8);
                    }
                } else if (charset != null) {
                    return charset.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Formats the tag tree to an output stream. This is used mainly for debugging purposes.
     */
    public void printTagTree(PrintStream out) {
        recursivePrintTags((HtmlTag) doc.getRootTag(), 0, out);
    }

    /**
     * Encodes the efficient style of all the elements to their <code>style</code> attributes.
     * Inheritance is not applied.
     * This is currently not necessary for the rendering. It is practical mainly together with
     * the <code>printTagTree</code> method for debugging the resulting style.
     */
    public void stylesToDom() {
        recursiveStylesToDom(getBody());
    }

    /**
     * Encodes the efficient style of all the elements to their <code>style</code> attributes
     * while applying the inheritance.
     * This is currently not necessary for the rendering. It is practical mainly together with
     * the <code>printTagTree</code> method for debugging the resulting style.
     */
    public void stylesToDomInherited() {
        recursiveStylesToDomInherited(getBody());
    }

    /**
     * Converts the HTML presentation attributes in the document body to inline styles.
     */
    public void attributesToStyles() {
        //fix the DOM tree structure according to HTML syntax
        HTMLNorm.normalizeHTMLTree(doc);
        //convert attributes to style definitions
        HTMLNorm.attributesToStyles(getBody(), "");
    }

    /**
     * Removes all the external style sheet links and puts the whole style sheet locally
     * to the document head.
     */
    public void localizeStyles() {
        if (getHead() == null) {
            return;
        }

        /* Remove existing style definitions */
        Vector<HtmlTag> tags = new Vector<>();
        recursiveFindStyleElements(getRoot(), tags);

        for (HtmlTag tag : tags) {
            tag.delete();
        }

        /* Create style text */
        StringBuilder sb = new StringBuilder();
        for (StyleSheet sheet : styles) {
            for (Object o : sheet.asList()) {
                if (o instanceof RuleSet) {
                    //check the first part of the first selector
                    Selector.SelectorPart start = ((RuleSet) o).getSelectors().get(0).get(0).get(0);
                    //skip inline styles
                    if (!(start instanceof Selector.ElementDOM))
                        sb.append(o.toString());
                }
            }
        }

        /* Create new style tag */
        HtmlTag style = (HtmlTag) getHead().createChildTag("style", sb.toString(), null, false);
        style.setAttribute("type", "text/css");
    }

    /**
     * Returns a vector of CSSStyleSheet objects referenced from the document for the specified
     * media type with default values of the remaining media features. The internal style sheets
     * are read from the document directly, the external ones are downloaded and parsed automatically.
     *
     * @param media the media type string
     */
    public void getStyleSheets(String media) {
        this.media = new MediaSpec(media);
        StyleSheet newsheet = CSSFactory.getUsedStyles(doc, encoding, baseUrl, this.media);
        styles.add(newsheet);
    }

    /**
     * Returns a vector of CSSStyleSheet objects referenced from the document for the specified
     * media type and features. The internal style sheets are read from the document directly, the external
     * ones are downloaded and parsed automatically.
     *
     * @param media the media specification
     */
    public void getStyleSheets(MediaSpec media) {
        this.media = media;
        StyleSheet newsheet = CSSFactory.getUsedStyles(doc, encoding, baseUrl, this.media);
        styles.add(newsheet);
    }

    /**
     * Returns a vector of CSSStyleSheet objects referenced from the document for the media
     * type set by <code>setMedia()</code> (or "screen" by default). The internal style
     * sheets are read from the document directly, the external ones are downloaded
     * and parsed automatically.
     */
    public void getStyleSheets() {
        getStyleSheets(media);
    }

    /**
     * Loads a stylesheet from an URL as na author style sheet.
     * Imports all the imported style sheets before storing it.
     *
     * @param base     the document base url
     * @param href     the href specification
     * @param encoding the default encoding
     */
    public void loadStyleSheet(URL base, String href, String encoding) {
        loadStyleSheet(base, href, encoding, Origin.AUTHOR);
    }

    /**
     * Loads a stylesheet from an URL and specifies the origin.
     * Imports all the imported style sheets before storing it.
     *
     * @param base     the document base url
     * @param href     the href specification
     * @param encoding the default character encoding for the style sheet (use <code>null</code> for default system encoding)
     * @param origin   the style sheet origin (author, user agent or user)
     */
    public void loadStyleSheet(URL base, String href, String encoding, Origin origin) {
        try {
            StyleSheet newsheet = CSSFactory.parse(new URL(base, href), encoding);
            newsheet.setOrigin(translateOrigin(origin));
            styles.add(newsheet);
        } catch (IOException e) {
            log.error("I/O Error: " + e.getMessage());
        } catch (CSSException e) {
            log.error("CSS Error: " + e.getMessage());
        }
    }

    /**
     * Parses and adds an author style sheet represented as a string to the end of the used
     * stylesheet lists. It imports all the imported style sheets before storing
     * it.
     *
     * @param base    the document base URL
     * @param cssdata the style string
     * @deprecated This method does not specify the style sheet origin; use {@link #addStyleSheet(java.net.URL, String, Origin)} instead.
     */
    public void addStyleSheet(URL base, String cssdata) {
        addStyleSheet(base, cssdata, Origin.AUTHOR);
    }

    /**
     * Parses and adds an author style sheet represented as a string to the end of the used
     * stylesheet lists. It imports all the imported style sheets before storing
     * it. The origin of the style sheet is set to the author, agent or user. The origin
     * influences the rule priority according to the <a href="http://www.w3.org/TR/CSS21/cascade.html#cascading-order">CSS specification</a>.
     *
     * @param base    the document base URL
     * @param cssdata the style string
     * @param origin  the style sheet origin (AUTHOR, AGENT or USER)
     */
    public void addStyleSheet(URL base, String cssdata, Origin origin) {
        try {
            StyleSheet newsheet = CSSFactory.parse(cssdata);
            newsheet.setOrigin(translateOrigin(origin));
            styles.add(newsheet);
        } catch (IOException e) {
            log.error("I/O Error: " + e.getMessage());
        } catch (CSSException e) {
            log.error("DOMAnalyzer: CSS Error: " + e.getMessage());
        }
    }

    /**
     * Forces recomputing the element styles. This method should be called when a new style sheet
     * has been added or a DOM has changed after some styles have been read from the analyzer.
     */
    public void recomputeStyles() {
        //this forces re-creating the analyzer and stylemap upon next read
        analyzer = null;
        stylemap = null;
        istylemap = null;
    }

    /**
     * Gets all the style declarations for a particular element and computes
     * the resulting element style.
     *
     * @param tag the element for which the style should be computed
     * @return the resulting declaration
     */
    public NodeData getElementStyle(HtmlTag tag) {
        if (analyzer == null)
            analyzer = new Analyzer(styles);

        if (stylemap == null)
            stylemap = analyzer.evaluateDOM(doc, media, false);

        return stylemap.get(tag);
    }

    /**
     * Checks whether the inhetited style has been computed and computes it when necessary.
     */
    private void checkStylesInherited() {
        if (analyzer == null)
            analyzer = new Analyzer(styles);

        if (istylemap == null)
            istylemap = analyzer.evaluateDOM(doc, media, true);
    }

    /**
     * Gets all the style declarations for a particular element and computes
     * the resulting element style including the inheritance from the parent.
     *
     * @param tag the element for which the style should be computed
     * @return the resulting style declaration
     */
    public NodeData getElementStyleInherited(HtmlTag tag) {
        checkStylesInherited();
        return istylemap.get(tag);
    }

    /**
     * Gets all the style declarations for a particular element and a particular pseudo-element and computes
     * the resulting pseudo-element style including the inheritance from the parent.
     *
     * @param tag    the element for which the style should be computed
     * @param pseudo the pseudo class or element used for style computation
     * @return the resulting style declaration
     */
    public NodeData getElementStyleInherited(HtmlTag tag, PseudoDeclaration pseudo) {
        checkStylesInherited();
        return istylemap.get(tag, pseudo);
    }

    /**
     * Checks whether the element has some style defined for a specified pseudo-element.
     *
     * @param tag    The element to be checked
     * @param pseudo The pseudo element specification
     * @return <code>true</code> if there is some style defined for the given pseudo-element
     */
    public boolean hasPseudoDef(HtmlTag tag, PseudoDeclaration pseudo) {
        checkStylesInherited();
        return istylemap.hasPseudo(tag, pseudo);
    }

    /**
     * Assigns the given style for the specified pseudo-element.
     *
     * @param tag    the element to be assigned the style
     * @param pseudo The pseudo-element or <code>null</code> if none is required
     * @param style  the assigned style
     */
    public void useStyle(HtmlTag tag, PseudoDeclaration pseudo, NodeData style) {
        checkStylesInherited();
        istylemap.put(tag, pseudo, style);
    }

    //====================================================================

    private void recursiveStylesToDom(HtmlTag tag) {
        NodeData decl = getElementStyle(tag);
        if (decl != null) {
            String decls = decl.toString().replace("\n", "");
            tag.setAttribute("style", quote(decls));
        }

        for (HtmlTag child : (HtmlTag[]) tag.getSubTags()) {
            recursiveStylesToDom(child);
        }
    }

    private void recursiveStylesToDomInherited(HtmlTag tag) {
        NodeData decl = getElementStyleInherited(tag);
        if (decl != null) {
            String decls = decl.toString().replace("\n", "");
            tag.setAttribute("style", quote(decls));
        }

        for (HtmlTag child : (HtmlTag[]) tag.getSubTags()) {
            recursiveStylesToDomInherited(child);
        }
    }

    /**
     * Finds all the style definitions in the document.
     */
    private void recursiveFindStyleElements(HtmlTag tag, Vector<HtmlTag> out) {
        XmlAttribute rel = tag.getAttribute("rel");

        if ("style".equalsIgnoreCase(tag.getName()) ||
                (rel != null && "link".equalsIgnoreCase(tag.getName()) && "stylesheet".equalsIgnoreCase(rel.getValue()))) {
            out.add(tag);
        } else {
            for (HtmlTag child : (HtmlTag[]) tag.getSubTags()) {
                recursiveFindStyleElements(child, out);

            }
        }
    }

    private String quote(String s) {
        return s.replace('"', '\'');
    }

    //========================================================================

    private void recursivePrintTags(HtmlTag tag, int level, PrintStream p) {
        String mat = "";


        NodeData decl = getElementStyle(tag);
        if (decl != null) {
            mat = "style:\"" + decl.toString() + "\"";
        }


        String ind = "";
        for (int i = 0; i < level * 4; i++) ind = ind + ' ';

        p.println(ind + tag.getName() + " " + mat);

        HtmlTag[] children = (HtmlTag[]) tag.getSubTags();
        for (int i = 0; i < children.length; i++)
            recursivePrintTags(children[i], level + 1, p);
    }

    /**
     * Translates the origin from the CSSBox API to jStyleParser API
     * (in order not to expose the jStyleParser API in CSSBox)
     */
    private StyleSheet.Origin translateOrigin(Origin origin) {
        if (origin == Origin.AUTHOR)
            return StyleSheet.Origin.AUTHOR;
        else if (origin == Origin.AGENT)
            return StyleSheet.Origin.AGENT;
        else
            return StyleSheet.Origin.USER;
    }
}
