/**
 * DeclarationMap.java
 * Copyright (C) Radek Burget
 *
 * Created on 22.1.2010, 16:23:07 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.domassign;

import com.intellij.psi.html.HtmlTag;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.Selector.PseudoDeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a map that assigns a sorted list of declarations to each element
 * and an optional pseudo-element.
 *
 * @author burgetr
 */
public class DeclarationMap extends MultiMap<HtmlTag, PseudoDeclaration, List<Declaration>> {

    /**
     * Adds a declaration for a specified list. If the list does not exist yet, it is created.
     *
     * @param tag    the element that the declaration belongs to
     * @param pseudo an optional pseudo-element or null
     * @param decl   the new declaration
     */
    public void addDeclaration(HtmlTag tag, PseudoDeclaration pseudo, Declaration decl) {
        List<Declaration> list = getOrCreate(tag, pseudo);
        list.add(decl);
    }

    /**
     * Sorts the given list according to the rule specificity.
     *
     * @param tag    the element to which the list is assigned
     * @param pseudo an optional pseudo-element or null
     */
    public void sortDeclarations(HtmlTag tag, PseudoDeclaration pseudo) {
        List<Declaration> list = get(tag, pseudo);
        if (list != null)
            Collections.sort(list);
    }

    @Override
    protected List<Declaration> createDataInstance() {
        return new ArrayList<Declaration>();
    }


}

