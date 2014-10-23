/*
 * HTMLBoxFactory.java
 * Copyright (c) 2005-2012 Radek Burget
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
 * Created on 23.11.2012, 15:52:00 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package org.cordovastudio.editors.designer.rendering.engines.cssBox.layout;

import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import cz.vutbr.web.css.NodeData;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DOMSource;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DefaultDOMSource;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * A factory for creating box subtrees for HTML-specific elements.
 *
 * @author burgetr
 */
public class HTMLBoxFactory {
    private static Logger log = LoggerFactory.getLogger(HTMLBoxFactory.class);

    private BoxFactory factory;
    private Set<String> supported;

    /**
     * Creates a new subtree factory for a given main factory.
     *
     * @param parent The main factory to be used.
     */
    public HTMLBoxFactory(BoxFactory parent) {
        this.factory = parent;
        supported = new HashSet<String>(2);
        supported.add("object");
        supported.add("img");
    }

    /**
     * Checks whether the given tag (DOM element) processing is supported by this factory.
     *
     * @param tag The DOM element to be checked.
     * @return <code>true</code> when the element may be processed by this factory.
     */
    public boolean isTagSupported(HtmlTag tag) {
        if (tag.getName() != null && supported.contains(tag.getName().toLowerCase())) //completely supported tags
            return true;
        else //special cases
        {
            //empty anchor elements must be preserved
            if (tag.getName().toLowerCase().equals("a") && tag.getAttribute("name") != null
                    && (tag.getText() == null || tag.getText().trim().length() == 0))
                return true;
            else
                return false;
        }
    }

    /**
     * Creates the box according to the HTML element.
     *
     * @param parent   The box in the main box tree to be used as a parent box for the new box.
     * @param tag      The element to be processed.
     * @param viewport The viewport to be used for the new box.
     * @param style    The style of the element.
     * @return The newly created box or <code>null</code> when the element is not supported
     * or cannot be created.
     */
    public ElementBox createBox(ElementBox parent, HtmlTag tag, Viewport viewport, NodeData style) {
        String name = tag.getName().toLowerCase();
        if (name.equals("object"))
            return createSubtreeObject(parent, tag, viewport, style);
        else if (name.equals("img"))
            return createSubtreeImg(parent, tag, viewport, style);
        else if (name.equals("a") && tag.getAttribute("name") != null
                && (tag.getText() == null || tag.getText().trim().length() == 0)) {
            //make the named anchors sticky
            ElementBox eb = factory.createElementInstance(parent, tag, style);
            eb.setSticky(true);
            return eb;
        } else
            return null;
    }

    protected ElementBox createSubtreeImg(ElementBox parent, HtmlTag tag, Viewport viewport, NodeData style) {
        InlineReplacedBox rbox = new InlineReplacedBox(tag, (Graphics2D) parent.getGraphics().create(), parent.getVisualContext().create());
        rbox.setViewport(viewport);
        rbox.setStyle(style);

        String src = tag.getAttributeValue("src");
        rbox.setContentObj(new ReplacedImage(rbox, rbox.getVisualContext(), factory.getBaseURL(), src));

        if (rbox.isBlock())
            return new BlockReplacedBox(rbox);
        else
            return rbox;
    }

    protected ElementBox createSubtreeObject(ElementBox parent, HtmlTag tag, Viewport viewport, NodeData style) {
        //create the replaced box
        InlineReplacedBox rbox = new InlineReplacedBox(tag, (Graphics2D) parent.getGraphics().create(), parent.getVisualContext().create());
        rbox.setViewport(viewport);
        rbox.setStyle(style);

        //try to create the content object based on the mime type
        try {
            String mime = tag.getAttributeValue("type").toLowerCase();
            String cb = tag.getAttributeValue("codebase");
            String dataurl = tag.getAttributeValue("data");
            URL base = new URL(factory.getBaseURL(), cb);

            if (!dataurl.trim().isEmpty()) {
                DocumentSource src = factory.createDocumentSource(base, dataurl);
                if (mime.isEmpty()) {
                    mime = src.getContentType();
                    if (mime == null || mime.isEmpty())
                        mime = "text/html";
                }
                log.debug("ctype=" + mime);

                ReplacedContent content = null;
                if (mime.startsWith("image/")) {
                    content = new ReplacedImage(rbox, rbox.getVisualContext(), base, dataurl);
                } else if (mime.equals("text/html")) {
                    log.info("Parsing: " + src.getURL());

                    DOMSource parser = new DefaultDOMSource(src);

                    Document doc = parser.parse();

                    // TODO: This is embarrassingly ugly... Please go away. Don't look at this.
                    //
                    // Okaaay, you're still here. So, do YOU know whether there is any neater way to do this?
                    // We need an XmlDocument but all we got is a org.w3c.dom.Document. It's up to you now.

                    XmlFile xfile = (XmlFile)PsiFileFactory.getInstance(null).createFileFromText("dummyXmlFile.xml", doc.getTextContent());

                    XmlDocument xdoc = xfile.getDocument();

                    String encoding = parser.getCharset();
                    content = new ReplacedText(rbox, xdoc, src.getURL(), encoding);
                }
                rbox.setContentObj(content);
            }
        } catch (MalformedURLException e1) {
            //something failed, no content object is created => the we should use the object element contents
        } catch (SAXException e1) {
            //parsing failed
        } catch (IOException e1) {
            //document reading failed
        }

        if (rbox.getContentObj() != null) //the content object has been sucessfuly created
        {
            //convert the type
            if (rbox.getDisplay() == ElementBox.DISPLAY_BLOCK)
                return new BlockReplacedBox(rbox);
            else //inline boxes are not allowed -- we must create a block formatting context
                return new InlineBlockReplacedBox(rbox);
        } else //no content object - fallback to a normal box (the default object behavior)
        {
            return null;
        }
    }

}
