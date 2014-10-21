/**
 * MatchCondition.java
 * Copyright (C) 2013 Radek Burget
 *
 * Created on 1.7.2013, 11:03:32 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.css;

import com.intellij.psi.html.HtmlTag;
import cz.vutbr.web.css.Selector.SelectorPart;

/**
 * An additional condition for matching the selectors.
 *
 * @author burgetr
 */
public interface MatchCondition {

    /**
     * Checks whether the condition is satisfied for the given element and selector part.
     *
     * @param tag     The element to be tested.
     * @param selpart The selector part.
     * @return <code>true</code> when the condition is satisfied
     */
    public boolean isSatisfied(HtmlTag tag, SelectorPart selpart);

}
