/*
 * Copyright (C) kapy
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.domassign;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlDocument;

/**
 * This class implements traversal of DOM tree with simplified Visitor
 * pattern.
 *
 * @author kapy
 */
public abstract class Traversal<T> {
    protected Object source;
    protected HtmlTreeWalker walker;

    public Traversal(HtmlTreeWalker walker, Object source) {
        this.source = source;
        this.walker = walker;
    }

    public Traversal(XmlDocument doc, Object source) {
        this.walker = new HtmlTreeWalker((HtmlTag) doc.getRootTag());
        this.source = source;
    }

    public void listTraversal(T result) {

        // tree traversal as nodes are found inside
        HtmlTag current, checkpoint = null;
        current = walker.nextTag();
        while (current != null) {
            // this method can change position in walker
            checkpoint = walker.getCurrentTag();
            processTag(result, current, source);
            walker.setCurrentTag(checkpoint);
            current = walker.nextTag();
        }
    }

    public void levelTraversal(T result) {

        // this method can change position in walker
        HtmlTag current, checkpoint = null;
        current = checkpoint = walker.getCurrentTag();
        processTag(result, current, source);
        walker.setCurrentTag(checkpoint);

        // traverse children:
        for (HtmlTag t = walker.firstChild(); t != null; t = walker.nextSibling()) {
            levelTraversal(result);
        }

        // return position to the current (level up):
        walker.setCurrentTag(checkpoint);
    }

    protected abstract void processTag(T result, HtmlTag current, Object source);

    public Traversal<T> reset(HtmlTreeWalker walker, Object source) {
        this.walker = walker;
        this.source = source;
        return this;
    }

}
