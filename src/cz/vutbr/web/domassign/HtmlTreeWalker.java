/*
 * Copyright (C) 2014 Christoffer T. Timm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.vutbr.web.domassign;


import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlDocument;

/**
 * Created by cti on 21.10.14.
 */
public class HtmlTreeWalker {

    /**
     * The current Node.
     */
    PsiElement currentElement;

    /**
     * The root Node.
     */
    PsiElement root;

    public HtmlTreeWalker(PsiElement root) {
        this.root = root;
        this.currentElement = root;
    }

    public PsiElement getRoot() {
        return root;
    }

    /**
     * Return whether children entity references are included in the iterator.
     */
    public boolean getExpandEntityReferences() {
        return true;
    }

    /**
     * Return the current Node.
     */
    public PsiElement getCurrentElement() {
        return currentElement;
    }

    /**
     * Return the current Node.
     */
    public void setCurrentElement(PsiElement tag) {
        currentElement = tag;
    }

    /**
     * Return the parent Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement parentElement() {

        if (currentElement == null)
            return null;

        PsiElement tag = currentElement.getParent();

        if (tag != null)
            currentElement = tag;

        return tag;

    }

    /**
     * Return the first child Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement firstChild() {

        if (currentElement == null)
            return null;

        PsiElement tag = currentElement.getFirstChild();

        if (tag != null)
            currentElement = tag;

        return tag;
    }

    /**
     * Return the last child Node from the current nod. If result is not null, set the current Node.
     */
    public PsiElement lastChild() {

        if (currentElement == null)
            return null;

        PsiElement tag = currentElement.getLastChild();

        if (tag != null)
            currentElement = tag;

        return tag;
    }

    /**
     * Return the previous sibling Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement previousSibling() {

        if (currentElement == null)
            return null;

        PsiElement node = currentElement.getPrevSibling();

        if (node != null)
            currentElement = node;

        return node;
    }

    /**
     * Return the next sibling Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement nextSibling() {

        if (currentElement == null)
            return null;

        PsiElement tag = currentElement.getNextSibling();

        if (tag != null)
            currentElement = tag;

        return tag;
    }

    /**
     * Return the previous Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement previousElement() {

        if (currentElement == null)
            return null;

        // get sibling
        PsiElement result = currentElement.getPrevSibling();

        if (result == null) {
            result = currentElement.getParent();
            if (result != null) {
                currentElement = result;
                return result;
            }
            return null;
        }

        // get the lastChild of result.
        PsiElement lastChild = result.getLastChild();

        PsiElement prev = lastChild;
        while (lastChild != null) {
            prev = lastChild;
            lastChild = prev.getLastChild();
        }

        lastChild = prev;

        // if there is a lastChild which passes filters return it.
        if (lastChild != null) {
            currentElement = lastChild;
            return lastChild;
        }

        // otherwise return the previous sibling.
        currentElement = result;
        return result;
    }

    /**
     * Return the next Node from the current node. If result is not null, set the current Node.
     */
    public PsiElement nextElement() {

        if (currentElement == null)
            return null;

        PsiElement result = currentElement.getFirstChild();

        if (result != null) {
            currentElement = result;
            return result;
        }

        result = currentElement.getNextSibling();

        if (result != null) {
            currentElement = result;
            return result;
        }

        // return parent's 1st sibling.
        PsiElement parent = currentElement.getParent();

        while (parent != null && !(parent instanceof XmlDocument)) {
            result = parent.getNextSibling();
            if (result != null) {
                currentElement = result;
                return result;
            } else {
                parent = (PsiElement) parent.getParent();
            }
        }

        // end , return null
        return null;
    }
}

