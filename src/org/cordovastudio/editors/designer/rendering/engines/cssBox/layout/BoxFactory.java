/*
 * BoxFactory.java
 * Copyright (c) 2005-2010 Radek Burget
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
 * Created on 10.4.2010, 17:13:30 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 *  – Added support for ViewInfos
 */

package org.cordovastudio.editors.designer.rendering.engines.cssBox.layout;

import com.intellij.psi.PsiElement;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.impl.source.xml.XmlTextImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlText;
import cz.vutbr.web.css.*;
import cz.vutbr.web.css.CSSProperty.Overflow;
import cz.vutbr.web.css.Selector.PseudoDeclaration;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.DOMAnalyzer;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DOMSource;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;


/**
 * A factory for creating the box tree. The usual way of creating the box tree is creating the viewport using the
 * {@link org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.BoxFactory#createViewportTree(HtmlTag, java.awt.Graphics2D, VisualContext, int, int)}. However, the factory can be used for creating
 * the individual nodes or subtrees.
 * <p>
 * <p>Usually, a single factory is created for each viewport. Then, this factory is accessible using
 * the {@link Viewport#getFactory()} method.
 *
 * @author burgetr
 */
public class BoxFactory {
    private static Logger log = LoggerFactory.getLogger(BoxFactory.class);

    protected final XmlElementFactory elementFactory;

    protected BrowserConfig config;
    protected HTMLBoxFactory html;

    protected DOMAnalyzer decoder;
    protected URL baseurl;
    protected Viewport viewport;

    protected int next_order;

    /**
     * Create a new factory.
     *
     * @param decoder The CSS decoder used for obtaining the DOM styles.
     * @param baseurl Base URL used for completing the relative URLs in the document.
     */
    public BoxFactory(DOMAnalyzer decoder, URL baseurl, XmlElementFactory elementFactory) {
        this.decoder = decoder;
        this.baseurl = baseurl;
        this.elementFactory = elementFactory;
        this.next_order = 0;
        this.config = new BrowserConfig();
        this.html = new HTMLBoxFactory(this);
    }

    /**
     * Obtains the current browser configuration.
     *
     * @return current configuration.
     */
    public BrowserConfig getConfig() {
        return config;
    }

    /**
     * Sets the browser configuration used for rendering.
     *
     * @param config the new configuration.
     */
    public void setConfig(BrowserConfig config) {
        this.config = config;
    }

    /**
     * Sets whether the engine should use the HTML extensions or not. Currently, the HTML
     * extensions include following:
     * <ul>
     * <li>Creating replaced boxes for <code>&lt;img&gt;</code> elements
     * <li>Using the <code>&lt;body&gt;</code> element background for the whole canvas according to the HTML specification
     * </ul>
     *
     * @param useHTML <code>false</code> if the extensions should be switched off (default is on)
     */
    public void setUseHTML(boolean useHTML) {
        config.setUseHTML(useHTML);
    }

    /**
     * Checks if the HTML extensions are enabled for the factory.
     *
     * @return <code>true</code> if the HTML extensions are enabled
     * @see #setUseHTML(boolean)
     */
    public boolean getUseHTML() {
        return config.getUseHTML();
    }

    /**
     * Obtains the base URL used by this factory.
     *
     * @return the base URL.
     */
    public URL getBaseURL() {
        return baseurl;
    }

    /**
     * Reset the factory for creating a new tree.
     */
    public void reset() {
        next_order = 0;
    }

    /**
     * Create the viewport and the underlying box tree from a DOM tree.
     *
     * @param root   the root element of the source DOM tree.
     * @param g      the root graphic context. Copies of this context will be used for the individual boxes.
     * @param ctx    the visual context (computed style). Copies of this context will be used for the individual boxes.
     * @param width  preferred viewport width.
     * @param height preferred viewport height.
     * @return the created viewport box with the corresponding box subtrees.
     */
    public Viewport createViewportTree(HtmlTag root, Graphics2D g, VisualContext ctx, int width, int height) {
        HtmlTag vp = createAnonymousElement("Xdiv", "block");
        viewport = new Viewport(vp, g, ctx, this, root, width, height);
        viewport.setConfig(config);
        BoxTreeCreationStatus stat = new BoxTreeCreationStatus(viewport);
        createSubtree(root, stat, viewport.getViewInfo());
        log.debug("Root box is: " + viewport.getRootBox());

        return viewport;
    }

    /**
     * Creates the box subtrees for all the child nodes of the DOM node corresponding to the box creatin status. Recursively creates the child boxes
     * from the child nodes.
     *
     * @param stat current tree creation status used for determining the parents
     */
    public void createBoxTree(BoxTreeCreationStatus stat, ViewInfo parentViewInfo) {
        boolean generated = false;
        do {
            if (stat.parent.isDisplayed()) {
                //add previously created boxes (the rest from the last twin)
                if (stat.parent.preadd != null)
                    addToTree(stat.parent.preadd, stat);

                //create :before elements
                if (stat.parent.previousTwin == null) {
                    PsiElement element = createPseudoElement(stat.parent, PseudoDeclaration.BEFORE);
                    if (element != null && (element instanceof HtmlTag || element instanceof XmlText)) {
                        stat.curchild = -1;
                        createSubtree(element, stat, parentViewInfo);
                    }
                }

                //create normal elements
                PsiElement[] children = stat.parent.getElement().getChildren();
                for (int child = stat.parent.firstDOMChild; child < stat.parent.lastDOMChild; child++) {
                    PsiElement childElement = children[child];
                    if (childElement instanceof HtmlTag || childElement instanceof XmlText) {
                        stat.curchild = child;
                        createSubtree(childElement, stat, parentViewInfo);
                    }
                }

                //create :after elements
                if (stat.parent.nextTwin == null) {
                    PsiElement element = createPseudoElement(stat.parent, PseudoDeclaration.AFTER);
                    if (element != null && (element instanceof HtmlTag || element instanceof XmlText)) {
                        stat.curchild = children.length;
                        createSubtree(element, stat, parentViewInfo);
                    }
                }

                normalizeBox(stat.parent);
            }

            //if a twin box has been created, continue creating the unprocessed boxes in the twin box
            if (stat.parent.nextTwin != null) {
                stat.parent = stat.parent.nextTwin;
                generated = true;
            } else
                generated = false;

        } while (generated);
    }

    /**
     * Creates a subtree of a parent box that corresponds to a single child DOM node of this box and adds the subtree to the complete tree.
     *
     * @param subTreeRoot the root DOM node of the subtree being created
     * @param stat        current box creation status for obtaining the containing boxes
     */
    private void createSubtree(PsiElement subTreeRoot, BoxTreeCreationStatus stat, ViewInfo parentViewInfo) {
        //store current status for the parent
        stat.parent.curstat = new BoxTreeCreationStatus(stat);

        //Create the new box for the child
        Box newbox;

        ViewInfo viewInfo = null;

        boolean istext = false;
        if (subTreeRoot instanceof XmlText) {
            newbox = createTextBox((XmlText) subTreeRoot, stat);
            istext = true;
        } else {
            newbox = createElementBox((HtmlTag) subTreeRoot, stat);
            viewInfo = createViewInfo(newbox);
            newbox.setViewInfo(viewInfo);
            List<ViewInfo> subViewInfos = parentViewInfo.getChildren();
            subViewInfos.add(viewInfo);
        }


        //Create the child subtree
        if (!istext) {
            //Determine the containing boxes of the children
            BoxTreeCreationStatus newstat = new BoxTreeCreationStatus(stat);
            newstat.parent = (ElementBox) newbox;
            if (((ElementBox) newbox).mayContainBlocks()) //the new box forms a block context
            {
                BlockBox block = (BlockBox) newbox;
                //positioned element?
                if (block.position == BlockBox.POS_ABSOLUTE ||
                        block.position == BlockBox.POS_RELATIVE ||
                        block.position == BlockBox.POS_FIXED) {
                    //A positioned box forms a content box for following absolutely positioned boxes
                    newstat.absbox = block;
                    //update clip box for the block
                    BlockBox cblock = block.getContainingBlock();
                    if (cblock.overflow != Overflow.VISIBLE || cblock.getClipBlock() == null)
                        block.setClipBlock(cblock);
                    else
                        block.setClipBlock(cblock.getClipBlock());
                    //A box with overflow:hidden creates a clipping box
                    if (block.overflow != BlockBox.OVERFLOW_VISIBLE || block.clipRegion != null)
                        newstat.clipbox = block;
                    else
                        newstat.clipbox = block.getClipBlock();
                } else //not positioned element
                {
                    //A box with overflow:hidden creates a clipping box
                    if (block.overflow != BlockBox.OVERFLOW_VISIBLE)
                        newstat.clipbox = block;
                }
                //Any block box forms a containing box for not positioned elements
                newstat.contbox = block;
                //Last inflow box is local for block boxes
                newstat.lastinflow = null; //TODO this does not work in some cases (absolute.html)
                //create the subtree
                createBoxTree(newstat, viewInfo);
                //remove trailing whitespaces in blocks
                removeTrailingWhitespaces(block);
            } else
                createBoxTree(newstat, viewInfo);
        }

        //Add the new box to the parent according to its type
        addToTree(newbox, stat);
    }

    /**
     * Creates a new {@link ViewInfo} for the specified {@link Box} and attach it to the given parent.
     *
     * @param newbox The {@link Box} for which the ViewInfo shall be created.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private ViewInfo createViewInfo(Box newbox) {
        ViewInfo viewInfo = new ViewInfo(((HtmlTag)newbox.getElement()).getName(), newbox.getElement(), 0, 0, 0, 0);
        viewInfo.setChildren(new ArrayList<>());

        if(newbox instanceof BlockBox) {
            LengthSet margins = ((BlockBox) newbox).getMargin();
            if(margins == null) {
                viewInfo.setExtendedInfo(0, 0, 0, 0, 0);
            } else {
                viewInfo.setExtendedInfo(((BlockBox) newbox).getFirstInlineBoxBaseline(), margins.left, margins.top, margins.right, margins.bottom);
            }
        } else {
            viewInfo.setExtendedInfo(0, 0, 0, 0, 0);
        }

        return viewInfo;
    }

    /**
     * Adds a bew box to the tree according to its type and the tree creation status.
     *
     * @param newbox the box to be added
     * @param stat   current box tree creation status used for determining the appropriate parent boxes
     */
    private void addToTree(Box newbox, BoxTreeCreationStatus stat) {
        if (newbox.isBlock()) {
            if (!((BlockBox) newbox).isPositioned()) {
                if (stat.parent.mayContainBlocks()) //block in block
                {
                    stat.parent.addSubBox(newbox);
                    stat.lastinflow = newbox;
                } else //block in inline box -- split the inline box
                {
                    ElementBox iparent = null; //last inline ancestor
                    ElementBox grandpa = stat.parent; //first block ancestor
                    ElementBox prev = null;
                    do {
                        //start next level
                        iparent = grandpa;
                        grandpa = iparent.getParent();
                        //finish inline parent and create another one
                        int lastchild = iparent.lastDOMChild;
                        iparent.lastDOMChild = iparent.curstat.curchild; //this will finish the iteration just now
                        if (iparent.curstat.curchild + 1 < lastchild || prev != null) //some children are remaning or there is some content already created -- split the inline boxes up to the block level
                        {
                            ElementBox newparent = iparent.copyBox();
                            newparent.removeAllSubBoxes();
                            newparent.firstDOMChild = iparent.curstat.curchild + 1;
                            iparent.nextTwin = newparent;
                            newparent.previousTwin = iparent;
                            if (prev != null) //queue the previously created child to be added to the new box
                                newparent.preadd = prev;
                            prev = newparent;
                        }
                    } while (grandpa != null && !grandpa.mayContainBlocks());

                    if (grandpa != null) {
                        //queue the block box and the next twin to be put to the block level
                        iparent.postadd = new Vector<Box>(2);
                        iparent.postadd.add(newbox);
                        if (iparent.nextTwin != null)
                            iparent.postadd.add(iparent.nextTwin);
                    } else
                        log.error("(internal error) grandpa is missing for %s", newbox);
                }
            } else //positioned box
            {
                ((BlockBox) newbox).domParent = newbox.getParent(); //set the DOM parent
                ((BlockBox) newbox).absReference = stat.lastinflow; //set the reference box for computing the static position
                newbox.getContainingBlock().addSubBox(newbox);
            }
        } else //inline elements -- always in flow
        {
            //spaces may be collapsed when the last inflow box ends with a whitespace and it allows collapsing whitespaces
            boolean lastwhite = (stat.lastinflow == null) || stat.lastinflow.isBlock() || (stat.lastinflow.endsWithWhitespace() && stat.lastinflow.collapsesSpaces());
            //the new box may be collapsed if it allows collapsing whitespaces and it is a whitespace
            boolean collapse = lastwhite && newbox.isWhitespace() && newbox.collapsesSpaces() && !newbox.isSticky() && !(newbox instanceof InlineBlockBox);
            if (!collapse) {
                stat.parent.addSubBox(newbox);
                stat.lastinflow = newbox;
            } else
                newbox.setContainingBlock(null); //indicate that the box is not part of the box tree (collapsed)
        }

        //Recursively process the eventual boxes that should be added tohether with the new box
        if (newbox instanceof ElementBox && ((ElementBox) newbox).postadd != null) {
            for (Box box : ((ElementBox) newbox).postadd)
                addToTree(box, stat);
        }

    }

    /**
     * Removes the block box trailing inline whitespace child boxes if allowed by the white-space values.
     *
     * @param block the block box to be processed
     */
    private void removeTrailingWhitespaces(ElementBox block) {
        if (block.collapsesSpaces()) {
            for (ListIterator<Box> it = block.getSubBoxList().listIterator(block.getSubBoxNumber()); it.hasPrevious(); ) {
                Box subbox = it.previous();
                if (subbox.isInFlow()) {
                    if (!subbox.isBlock() && subbox.collapsesSpaces()) {
                        if (subbox.isWhitespace() && !(subbox instanceof InlineBlockBox))
                            it.remove();
                        else if (subbox instanceof ElementBox) {
                            removeTrailingWhitespaces((ElementBox) subbox);
                            break; //the whole box is not whitespace
                        } else if (subbox instanceof TextBox) {
                            ((TextBox) subbox).removeTrailingWhitespaces();
                            break;
                        }
                    } else
                        break;
                }
            }
            block.setEndChild(block.getSubBoxList().size());
        }
    }

    /**
     * Creates a new box for an element node and sets the containing boxes accordingly.
     *
     * @param tag  The element node
     * @param stat The box tree creation status used for obtaining the containing boxes
     * @return the newly created element box
     */
    public ElementBox createElementBox(HtmlTag tag, BoxTreeCreationStatus stat) {
        ElementBox ret = createBox(stat.parent, tag, null);
        ret.setClipBlock(stat.clipbox);
        if (ret.isBlock()) {
            BlockBox block = (BlockBox) ret;
            //Setup my containing box
            if (block.position == BlockBox.POS_ABSOLUTE)
                ret.setContainingBlock(stat.absbox);
            else if (block.position == BlockBox.POS_FIXED)
                ret.setContainingBlock(viewport);
            else
                ret.setContainingBlock(stat.contbox);
        } else
            ret.setContainingBlock(stat.contbox);

        //mark the root visual context
        if (tag.getParentTag() == null)
            ret.getVisualContext().makeRootContext();

        return ret;
    }

    /**
     * Creates a new box for a text node and sets the containing boxes accordingly.
     *
     * @param textElement The element node
     * @param stat        Current box tree creation status for obtaining the containing boxes
     * @return the newly created text box
     */
    private TextBox createTextBox(XmlText textElement, BoxTreeCreationStatus stat) {
        //TODO: in some whitespace processing modes, multiple boxes may be created
        TextBox textBox = new TextBox(textElement, (Graphics2D) stat.parent.getGraphics().create(), stat.parent.getVisualContext().create());
        textBox.setOrder(next_order++);
        textBox.setContainingBlock(stat.contbox);
        textBox.setClipBlock(stat.clipbox);
        textBox.setViewport(viewport);
        textBox.setBase(baseurl);
        textBox.setParent(stat.parent);
        return textBox;
    }

    /**
     * Checks the newly created box and creates anonymous block boxes above the children if necessary.
     *
     * @param root the box to be checked
     * @return the modified root box
     */
    private ElementBox normalizeBox(ElementBox root) {
        //anonymous inline and block elements if necessary
        if (root.mayContainBlocks() && ((BlockBox) root).containsBlocks())
            createAnonymousBlocks((BlockBox) root);
        else if (root.containsMixedContent())
            createAnonymousInline(root);
        //table cells require a row parent
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_CELL,
                ElementBox.DISPLAY_TABLE_ROW, ElementBox.DISPLAY_ANY, ElementBox.DISPLAY_ANY,
                "tr", "table-row");
        //table rows require a group parent
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_ROW,
                ElementBox.DISPLAY_TABLE_ROW_GROUP, ElementBox.DISPLAY_TABLE_HEADER_GROUP, ElementBox.DISPLAY_TABLE_FOOTER_GROUP,
                "tbody", "table-row-group");
        //table columns require a table parent
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_COLUMN,
                ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_INLINE_TABLE, ElementBox.DISPLAY_TABLE_COLUMN_GROUP,
                "table", "table");
        //table row groups require a table parent
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_ROW_GROUP,
                ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_INLINE_TABLE, ElementBox.DISPLAY_ANY,
                "table", "table");
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_HEADER_GROUP,
                ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_INLINE_TABLE, ElementBox.DISPLAY_ANY,
                "table", "table");
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_FOOTER_GROUP,
                ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_INLINE_TABLE, ElementBox.DISPLAY_ANY,
                "table", "table");
        createAnonymousBoxes(root,
                ElementBox.DISPLAY_TABLE_CAPTION,
                ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_INLINE_TABLE, ElementBox.DISPLAY_ANY,
                "table", "table");
        return root;
    }

    /**
     * Creates anonymous inline boxes if the a block box contains both the inline
     * and the text child boxes. The child boxes of the specified root
     * are processed and the text boxes are grouped in a newly created
     * anonymous <code>span</code> boxes.
     *
     * @param root the root box
     */
    private void createAnonymousInline(ElementBox root) {
        Vector<Box> nest = new Vector<Box>();
        for (int i = 0; i < root.getSubBoxNumber(); i++) {
            Box sub = root.getSubBox(i);
            if (sub instanceof ElementBox)
                nest.add(sub);
            else {
                ElementBox anbox = createAnonymousBox(root, sub, false);
                anbox.addSubBox(sub);
                nest.add(anbox);
            }
        }
        root.nested = nest;
        root.endChild = nest.size();
    }

    /**
     * Creates anonymous block boxes if the a block box contains both the inline
     * and the block child boxes. The child boxes of the specified root
     * are processed and the inline boxes are grouped in a newly created
     * anonymous <code>div</code> boxes.
     *
     * @param root the root box
     */
    private void createAnonymousBlocks(BlockBox root) {
        Vector<Box> nest = new Vector<Box>();
        ElementBox adiv = null;
        for (int i = 0; i < root.getSubBoxNumber(); i++) {
            Box sub = root.getSubBox(i);
            if (sub.isBlock()) {
                if (adiv != null && !adiv.isempty) {
                    normalizeBox(adiv); //normalize even the newly created blocks
                    removeTrailingWhitespaces(adiv);
                }
                adiv = null;
                nest.add(sub);
            } else if (adiv != null || !sub.isWhitespace()) //omit whitespace boxes at the beginning of the blocks
            {
                if (adiv == null) {
                    adiv = createAnonymousBox(root, sub, true);
                    nest.add(adiv);
                }
                if (sub.isDisplayed() && !sub.isEmpty()) {
                    adiv.isempty = false;
                    adiv.displayed = true;
                }
                adiv.addSubBox(sub);
            }
        }
        if (adiv != null && !adiv.isempty) {
            normalizeBox(adiv); //normalize even the newly created blocks
            removeTrailingWhitespaces(adiv);
        }
        root.nested = nest;
        root.endChild = nest.size();
    }

    /**
     * Checks the child boxes of the specified root box wheter they require creating an anonymous
     * parent box.
     *
     * @param root     the box whose child boxes are checked
     * @param type     the required display type of the child boxes. The remaining child boxes are skipped.
     * @param reqtype1 the first required display type of the root. If the root type doesn't correspond
     *                 to any of the required types, an anonymous parent is created for the selected children.
     * @param reqtype2 the second required display type of the root.
     * @param reqtype3 the third required display type of the root.
     * @param name     the element name of the created anonymous box
     * @param display  the display type of the created anonymous box
     */
    private void createAnonymousBoxes(ElementBox root,
                                      CSSProperty.Display type,
                                      CSSProperty.Display reqtype1,
                                      CSSProperty.Display reqtype2,
                                      CSSProperty.Display reqtype3,
                                      String name, String display) {
        if (root.getDisplay() != reqtype1 && root.getDisplay() != reqtype2 && root.getDisplay() != reqtype3) {
            Vector<Box> nest = new Vector<Box>();
            ElementBox adiv = null;
            for (int i = 0; i < root.getSubBoxNumber(); i++) {
                Box sub = root.getSubBox(i);
                if (sub instanceof ElementBox) {
                    ElementBox subel = (ElementBox) sub;
                    if (subel.getDisplay() != type) {
                        adiv = null;
                        nest.add(sub);
                    } else {
                        if (adiv == null) {
                            HtmlTag elem = createAnonymousElement(name, display);
                            adiv = createBox(root, elem, display);
                            adiv.isblock = true;
                            adiv.isempty = true;
                            adiv.setContainingBlock(sub.getContainingBlock());
                            adiv.setClipBlock(sub.getClipBlock());
                            nest.add(adiv);
                        }
                        if (sub.isDisplayed() && !sub.isEmpty()) {
                            adiv.isempty = false;
                            adiv.displayed = true;
                        }
                        sub.setParent(adiv);
                        sub.setContainingBlock((BlockBox) adiv);
                        adiv.addSubBox(sub);
                    }
                } else
                    return; //first box is TextBox => all the boxes are TextBox, nothing to do.
            }
            root.nested = nest;
            root.endChild = nest.size();
        }
    }

    /**
     * Creates an empty anonymous block or inline box that can be placed between an optional parent and its child.
     * The corresponding properties of the box are taken from the child. The child is inserted NOT as the child box of the new box.
     * The new box is NOT inserted as a subbox of the parent.
     *
     * @param parent an optional parent node. When used, the parent of the new box is set to this node and the style is inherited from the parent.
     * @param child  the child node
     * @param block  when set to <code>true</code>, a {@link BlockBox} is created. Otherwise, a {@link InlineBox} is created.
     * @return the new created block box
     */
    protected ElementBox createAnonymousBox(ElementBox parent, Box child, boolean block) {
        ElementBox anbox;
        if (block) {
            HtmlTag anelem = createAnonymousElement("Xdiv", "block");
            anbox = new BlockBox(anelem, (Graphics2D) child.getGraphics().create(), child.getVisualContext().create());
            anbox.setViewport(viewport);
            anbox.setStyle(createAnonymousStyle("block"));
            ((BlockBox) anbox).contblock = false;
            anbox.isblock = true;
        } else {
            HtmlTag anelem = createAnonymousElement("Xspan", "inline");
            anbox = new InlineBox(anelem, (Graphics2D) child.getGraphics().create(), child.getVisualContext().create());
            anbox.setViewport(viewport);
            anbox.setStyle(createAnonymousStyle("inline"));
            anbox.isblock = false;
        }
        if (parent != null) {
            computeInheritedStyle(anbox, parent);
            anbox.setParent(parent);
        }
        anbox.setOrder(next_order++);
        anbox.isempty = true;
        anbox.setBase(child.getBase());
        anbox.setContainingBlock(child.getContainingBlock());
        anbox.setClipBlock(child.getClipBlock());
        return anbox;
    }

    /**
     * Creates a single new box from an element.
     *
     * @param tag     The source DOM element
     * @param display the display: property value that is used when the box style is not known (e.g. anonymous boxes)
     * @return A new box of a subclass of {@link ElementBox} based on the value of the 'display' CSS property
     */
    public ElementBox createBox(ElementBox parent, HtmlTag tag, String display) {
        ElementBox root = null;

        //New box style
        NodeData style = decoder.getElementStyleInherited(tag);
        if (style == null)
            style = createAnonymousStyle(display);

        //Special (HTML) tag names
        if (config.getUseHTML() && html.isTagSupported(tag)) {
            root = html.createBox(parent, tag, viewport, style);
        }
        //Not created yet -- create a box according to the display value
        if (root == null) {
            root = createElementInstance(parent, tag, style);
        }
        root.setBase(baseurl);
        root.setViewport(viewport);
        root.setParent(parent);
        root.setOrder(next_order++);
        return root;
    }

    /**
     * Creates a new box for a pseudo-element.
     *
     * @param box    the parent box of the pseudo element
     * @param pseudo The pseudo element name
     * @return A new box of a subclass of ElementBox based on the value of the 'display' CSS property
     */
    private HtmlTag createPseudoElement(ElementBox box, Selector.PseudoDeclaration pseudo) {
        if (box instanceof HtmlTag) {
            log.error("Could not create pseudo element, target element is of type " + box.getClass().getName() + ", but should be of type HtmlTag.", box);
            return null;
        }

        HtmlTag tag = (HtmlTag) box.getElement();
        //New box style
        NodeData style = decoder.getElementStyleInherited(tag, pseudo);
        if (style != null) {
            TermList cont = style.getValue(TermList.class, "content");
            if (cont != null && cont.size() > 0) {
                //create the DOM tree for the pseudo element
                //parent
                HtmlTag pelem = createAnonymousElement("XPspan", "inline");
                //content elements
                for (Term<?> c : cont) {
                    if (c instanceof TermIdent) {
                    } else if (c instanceof TermString) {
                        //Text txt = tag.getOwnerDocument().createTextNode(((TermString) c).getValue());
                        //pelem.appendChild(txt);
                        XmlText txt = new XmlTextImpl();
                        txt.insertText(((TermString) c).getValue(), 0);
                        pelem.add(txt);
                    } else if (c instanceof TermURI) {
                    } else if (c instanceof TermFunction) {
                        TermFunction f = (TermFunction) c;
                        if (f.getFunctionName().equals("attr")) {
                            List<Term<?>> params = f.getValue();
                            if (params.size() > 0) {
                                Term<?> param = params.get(0);

                                if (param != null) {
                                    XmlAttribute attr = tag.getAttribute(param.toString());

                                    if (attr != null) {
                                        String val = attr.getValue();

                                        XmlText txt = new XmlTextImpl();
                                        txt.insertText(val, 0);
                                        pelem.add(txt);
                                    }
                                }
                            }
                        }
                    }
                }

                //use the pseudo element style for the new (main) element
                decoder.useStyle(pelem, null, style);

                return pelem;
            } else
                return null; //no contents
        } else
            return null; //no pseudo declaration
    }

    /**
     * Creates an instance of ElementBox. According to the display: property of the style, the appropriate
     * subclass of ElementBox is created (e.g. BlockBox, TableBox, etc.)
     *
     * @param tag   The source DOM element
     * @param style Style definition for the node
     * @return The created instance of ElementBox
     */
    public ElementBox createElementInstance(ElementBox parent, HtmlTag tag, NodeData style) {
        ElementBox root = new InlineBox(tag, (Graphics2D) parent.getGraphics().create(), parent.getVisualContext().create());
        root.setViewport(viewport);
        root.setStyle(style);
        if (root.getDisplay() == ElementBox.DISPLAY_LIST_ITEM)
            root = new ListItemBox((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE)
            root = new BlockTableBox((InlineBox) root);
        /*else if (root.getDisplay() == ElementBox.DISPLAY_INLINE_TABLE)
            root = new InlineTableBox((InlineBox) root);*/
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_CAPTION)
            root = new TableCaptionBox((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_ROW_GROUP
                || root.getDisplay() == ElementBox.DISPLAY_TABLE_HEADER_GROUP
                || root.getDisplay() == ElementBox.DISPLAY_TABLE_FOOTER_GROUP)
            root = new TableBodyBox((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_ROW)
            root = new TableRowBox((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_CELL)
            root = new TableCellBox((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_COLUMN)
            root = new TableColumn((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_COLUMN_GROUP)
            root = new TableColumnGroup((InlineBox) root);
        else if (root.getDisplay() == ElementBox.DISPLAY_INLINE_BLOCK)
            root = new InlineBlockBox((InlineBox) root);
        else if (root.isBlock())
            root = new BlockBox((InlineBox) root);
        return root;
    }

    /**
     * Creates a new DOM element that represents an anonymous box in a document.
     *
     * @param name    the anonymous element name (generally arbitrary)
     * @param display the display style value for the block
     * @return the new element
     */
    public HtmlTag createAnonymousElement(String name, String display) {
        return (HtmlTag)elementFactory.createHTMLTagFromText("<"+name+" class=\"Xanonymous\" style=\"display:"+display+"\">");
        /*
        HtmlTag div = new HtmlTagImpl();
        div.setName(name);
        div.setAttribute("class", "Xanonymous");
        div.setAttribute("style", "display:" + display);

        return div;
        */
    }

    /**
     * Creates the style definition for an anonymous box. It contains only the class name set to "Xanonymous"
     * and the display: property set according to the parametres.
     *
     * @param display <code>display:</code> property value of the resulting style.
     * @return Resulting style definition
     */
    public NodeData createAnonymousStyle(String display) {
        NodeData ret = CSSFactory.createNodeData();

        Declaration cls = CSSFactory.getRuleFactory().createDeclaration();
        cls.unlock();
        cls.setProperty("class");
        cls.add(CSSFactory.getTermFactory().createString("Xanonymous"));
        ret.push(cls);

        Declaration disp = CSSFactory.getRuleFactory().createDeclaration();
        disp.unlock();
        disp.setProperty("display");
        disp.add(CSSFactory.getTermFactory().createIdent(display));
        ret.push(disp);

        return ret;
    }

    /**
     * Computes the style of a node based on its parent using the CSS inheritance.
     *
     * @param dest   the box whose style should be computed
     * @param parent the parent box
     */
    private void computeInheritedStyle(ElementBox dest, ElementBox parent) {
        NodeData newstyle = dest.getStyle().inheritFrom(parent.getStyle());
        dest.setStyle(newstyle);
    }

    /**
     * Creates a new instance of the {@link org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DocumentSource} class registered in the browser configuration
     * ({@link BrowserConfig}).
     *
     * @param urlstring the URL to be given to the document source.
     * @return the document source.
     */
    public DocumentSource createDocumentSource(URL base, String urlstring) {
        try {
            Constructor<? extends DocumentSource> constr = config.getDocumentSourceClass().getConstructor(URL.class, String.class);
            return constr.newInstance(base, urlstring);
        } catch (Exception e) {
            log.warn("Could not create the DocumentSource instance: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a new instance of the {@link org.cordovastudio.editors.designer.rendering.engines.cssBox.io.DOMSource} class registered in the browser configuration
     * ({@link BrowserConfig}).
     *
     * @param src the document source to be given to the DOM source.
     * @return the DOM source.
     */
    public DOMSource createDOMSource(DocumentSource src) {
        try {
            Constructor<? extends DOMSource> constr = config.getDOMSourceClass().getConstructor(DocumentSource.class);
            return constr.newInstance(src);
        } catch (Exception e) {
            log.warn("BoxFactory: Warning: could not create the DOMSource instance: " + e.getMessage());
            return null;
        }
    }

}

/**
 * The box tree creation status holds all the ancestor boxes that might be necessary for creating the child boxes
 * and adding them to the resulting tree
 *
 * @author burgetr
 */
class BoxTreeCreationStatus {
    /**
     * Normal flow parent box
     */
    public ElementBox parent;

    /**
     * Containing block for normal flow
     */
    public BlockBox contbox;

    /**
     * Containing block for absolutely positioned boxes
     */
    public BlockBox absbox;

    /**
     * Clipping box based on overflow property
     */
    public BlockBox clipbox;

    /**
     * Last in-flow box
     */
    public Box lastinflow;

    /**
     * The index of the DOM node within its parent node
     */
    int curchild;

    /**
     * Creates a new initial creation status
     *
     * @param viewport the root viewport box
     */
    public BoxTreeCreationStatus(Viewport viewport) {
        parent = contbox = absbox = clipbox = viewport;
        lastinflow = null;
        curchild = 0;
    }

    /**
     * Creates a copy of the status
     *
     * @param stat original status
     */
    public BoxTreeCreationStatus(BoxTreeCreationStatus stat) {
        this.parent = stat.parent;
        this.contbox = stat.contbox;
        this.absbox = stat.absbox;
        this.clipbox = stat.clipbox;
        this.lastinflow = stat.lastinflow;
        this.curchild = stat.curchild;
    }

}
