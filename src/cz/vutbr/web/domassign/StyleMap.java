/**
 * StyleMap.java
 * Copyright (C) 2010 Radek Burget
 *
 * Created on 22.1.2010, 16:06:07 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.domassign;

import com.intellij.psi.html.HtmlTag;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.Selector.PseudoDeclaration;

/**
 * This is a map that assigns a style to a particular elements and moreover, it
 * gathers the information about the pseudo elements.
 *
 * @author burgetr
 */
public class StyleMap extends MultiMap<HtmlTag, PseudoDeclaration, NodeData> {

    public StyleMap(int size) {
        super(size);
    }

    @Override
    protected NodeData createDataInstance() {
        return CSSFactory.createNodeData();
    }

}
