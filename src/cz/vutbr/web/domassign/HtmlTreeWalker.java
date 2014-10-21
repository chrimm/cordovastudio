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

import com.intellij.psi.html.HtmlTag;

/**
 * Created by cti on 21.10.14.
 */
public class HtmlTreeWalker {

    /**
     * The current Node.
     */
    HtmlTag currentTag;

    /**
     * The root Node.
     */
    HtmlTag root;

    public HtmlTreeWalker(HtmlTag root) {
        this.root = root;
        this.currentTag = root;
    }

    public HtmlTag getRoot() {
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
    public HtmlTag getCurrentTag() {
        return currentTag;
    }

    /**
     * Return the current Node.
     */
    public void setCurrentTag(HtmlTag tag) {
        currentTag = tag;
    }

    /**
     * Return the parent Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag parentTag() {

        if (currentTag == null)
            return null;

        HtmlTag tag = (HtmlTag) currentTag.getParentTag();

        if (tag != null)
            currentTag = tag;

        return tag;

    }

    /**
     * Return the first child Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag firstChild() {

        if (currentTag == null)
            return null;

        HtmlTag tag = (HtmlTag) currentTag.getFirstChild();

        if (tag != null)
            currentTag = tag;

        return tag;
    }

    /**
     * Return the last child Node from the current nod. If result is not null, set the current Node.
     */
    public HtmlTag lastChild() {

        if (currentTag == null)
            return null;

        HtmlTag tag = (HtmlTag) currentTag.getLastChild();

        if (tag != null)
            currentTag = tag;

        return tag;
    }

    /**
     * Return the previous sibling Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag previousSibling() {

        if (currentTag == null)
            return null;

        HtmlTag node = (HtmlTag) currentTag.getPrevSibling();

        if (node != null)
            currentTag = node;

        return node;
    }

    /**
     * Return the next sibling Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag nextSibling() {

        if (currentTag == null)
            return null;

        HtmlTag tag = (HtmlTag) currentTag.getNextSibling();

        if (tag != null)
            currentTag = tag;

        return tag;
    }

    /**
     * Return the previous Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag previousTag() {

        if (currentTag == null)
            return null;

        // get sibling
        HtmlTag result = (HtmlTag) currentTag.getPrevSibling();

        if (result == null) {
            result = (HtmlTag) currentTag.getParentTag();
            if (result != null) {
                currentTag = result;
                return result;
            }
            return null;
        }

        // get the lastChild of result.
        HtmlTag lastChild = (HtmlTag) result.getLastChild();

        HtmlTag prev = lastChild;
        while (lastChild != null) {
            prev = lastChild;
            lastChild = (HtmlTag) prev.getLastChild();
        }

        lastChild = prev;

        // if there is a lastChild which passes filters return it.
        if (lastChild != null) {
            currentTag = lastChild;
            return lastChild;
        }

        // otherwise return the previous sibling.
        currentTag = result;
        return result;
    }

    /**
     * Return the next Node from the current node. If result is not null, set the current Node.
     */
    public HtmlTag nextTag() {

        if (currentTag == null)
            return null;

        HtmlTag result = (HtmlTag) currentTag.getFirstChild();

        if (result != null) {
            currentTag = result;
            return result;
        }

        result = (HtmlTag) currentTag.getNextSibling();

        if (result != null) {
            currentTag = result;
            return result;
        }

        // return parent's 1st sibling.
        HtmlTag parent = (HtmlTag) currentTag.getParentTag();

        while (parent != null) {
            result = (HtmlTag) parent.getNextSibling();
            if (result != null) {
                currentTag = result;
                return result;
            } else {
                parent = (HtmlTag) parent.getParentTag();
            }
        }

        // end , return null
        return null;
    }
}

