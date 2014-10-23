/*
 * HTMLNorm.java
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
 * Created on 6. �nor 2005, 18:52
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */

package org.cordovastudio.editors.designer.rendering.engines.cssBox.css;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.TermLength;
import cz.vutbr.web.css.TermLengthOrPercent;
import cz.vutbr.web.css.TermPercent;

import java.util.Vector;

/**
 * This class provides a mechanism of converting some HTML presentation
 * atributes to the CSS styles and other methods related to HTML specifics.
 *
 * @author radek
 */
public class HTMLNorm {
    /**
     * Recursively converts some HTML presentation attributes to the inline style of the element.
     * The original attributes are left in the DOM tree, the <code>XDefaultStyle</code> attribute is
     * modified appropriately. Some of the values (e.g. the font sizes) are converted approximately
     * since their exact interpretation is not defined.
     *
     * @param tag     the root node of the DOM subtree where the conversion is done
     * @param tab_inh the inline style inherited from a parent table, empty if we're not in a table
     */
    public static void attributesToStyles(HtmlTag tag, String tab_inh) {
        //Analyze HTML attributes
        String attrs = "";
        //background
        if (tag.getLocalName().equals("table") ||
                tag.getLocalName().equals("tr") ||
                tag.getLocalName().equals("th") ||
                tag.getLocalName().equals("td") ||
                tag.getLocalName().equals("body")) {
            if (tag.getAttribute("bgcolor") != null)
                attrs = attrs + "background-color: " + tag.getAttribute("bgcolor") + ";";
        }
        //setting table and cell borders
        if (tag.getLocalName().equals("table")) {
            String border = "0";
            String frame = "void";
            String rules = "none";
            int cpadding = 0;
            int cspacing = 0;
            tab_inh = ""; //new table has its own settings

            //cell padding
            if (tag.getAttribute("cellpadding") != null) {
                try {
                    cpadding = Integer.parseInt(tag.getAttribute("cellpadding").getValue());
                    tab_inh = tab_inh + "padding: " + cpadding + "px; ";
                } catch (NumberFormatException e) {
                }
            }
            //cell spacing
            if (tag.getAttribute("cellspacing") != null) {
                try {
                    cspacing = Integer.parseInt(tag.getAttribute("cellspacing").getValue());
                    attrs = attrs + "border-spacing: " + cspacing + "px; ";
                } catch (NumberFormatException e) {
                }
            }
            //borders
            if (tag.getAttribute("border") != null) {
                border = tag.getAttribute("border").getValue();
                if (!border.equals("0")) {
                    frame = "border";
                    rules = "all";
                }
            }
            if (tag.getAttribute("frame") != null)
                frame = tag.getAttribute("frame").getValue().toLowerCase();
            if (tag.getAttribute("rules") != null)
                rules = tag.getAttribute("rules").getValue().toLowerCase();

            if (!border.equals("0")) {
                String fstyle = "border-@-style:solid;border-@-width:" + border + "px;";
                if (frame.equals("above"))
                    attrs = attrs + applyBorders(fstyle, "top");
                if (frame.equals("below"))
                    attrs = attrs + applyBorders(fstyle, "bottom");
                if (frame.equals("hsides")) {
                    attrs = attrs + applyBorders(fstyle, "left");
                    attrs = attrs + applyBorders(fstyle, "right");
                }
                if (frame.equals("lhs"))
                    attrs = attrs + applyBorders(fstyle, "left");
                if (frame.equals("rhs"))
                    attrs = attrs + applyBorders(fstyle, "right");
                if (frame.equals("vsides")) {
                    attrs = attrs + applyBorders(fstyle, "top");
                    attrs = attrs + applyBorders(fstyle, "bottom");
                }
                if (frame.equals("box")) {
                    attrs = attrs + applyBorders(fstyle, "left");
                    attrs = attrs + applyBorders(fstyle, "right");
                    attrs = attrs + applyBorders(fstyle, "top");
                    attrs = attrs + applyBorders(fstyle, "bottom");
                }
                if (frame.equals("border")) {
                    attrs = attrs + applyBorders(fstyle, "left");
                    attrs = attrs + applyBorders(fstyle, "right");
                    attrs = attrs + applyBorders(fstyle, "top");
                    attrs = attrs + applyBorders(fstyle, "bottom");
                }

                //when 'rules' are set, 1px border is inherited by the cells
                fstyle = "border-@-style:solid;border-@-width:1px;";
                if (rules.equals("rows")) {
                    tab_inh = tab_inh + applyBorders(fstyle, "top");
                    tab_inh = tab_inh + applyBorders(fstyle, "bottom");
                    attrs = attrs + "border-collapse:collapse;"; //seems to cause table border collapsing
                } else if (rules.equals("cols")) {
                    tab_inh = tab_inh + applyBorders(fstyle, "left");
                    tab_inh = tab_inh + applyBorders(fstyle, "right");
                    attrs = attrs + "border-collapse:collapse;";
                } else if (rules.equals("all")) {
                    tab_inh = tab_inh + applyBorders(fstyle, "top");
                    tab_inh = tab_inh + applyBorders(fstyle, "bottom");
                    tab_inh = tab_inh + applyBorders(fstyle, "left");
                    tab_inh = tab_inh + applyBorders(fstyle, "right");
                }
            }
        }
        //inherited cell properties
        if (tag.getLocalName().equals("th") ||
                tag.getLocalName().equals("td")) {
            if (tab_inh.length() > 0)
                attrs = tab_inh + attrs;
        }
        //other borders
        if (tag.getLocalName().equals("img") ||
                tag.getLocalName().equals("object")) {
            if (tag.getAttribute("border") != null) {
                String border = tag.getAttribute("border").getValue();
                String fstyle;
                if (border.equals("0"))
                    fstyle = "border-@-style:none;";
                else
                    fstyle = "border-@-style:solid;border-@-width:" + border + "px;";
                attrs = attrs + applyBorders(fstyle, "top");
                attrs = attrs + applyBorders(fstyle, "right");
                attrs = attrs + applyBorders(fstyle, "bottom");
                attrs = attrs + applyBorders(fstyle, "left");
            }
        }
        //object alignment
        if (tag.getLocalName().equals("img") ||
                tag.getLocalName().equals("object") ||
                tag.getLocalName().equals("applet") ||
                tag.getLocalName().equals("iframe") ||
                tag.getLocalName().equals("input")) {
            if (tag.getAttribute("align") != null) {
                String align = tag.getAttribute("align").getValue();
                if (align.equals("left"))
                    attrs = attrs + "float:left;";
                else if (align.equals("right"))
                    attrs = attrs + "float:right;";
            }
        }
        //table alignment
        if (tag.getLocalName().equals("col") ||
                tag.getLocalName().equals("colgroup") ||
                tag.getLocalName().equals("tbody") ||
                tag.getLocalName().equals("td") ||
                tag.getLocalName().equals("tfoot") ||
                tag.getLocalName().equals("th") ||
                tag.getLocalName().equals("thead") ||
                tag.getLocalName().equals("tr")) {
            if (tag.getAttribute("align") != null) {
                String align = tag.getAttribute("align").getValue();
                if (align.equals("left"))
                    attrs = attrs + "text-align:left;";
                else if (align.equals("right"))
                    attrs = attrs + "text-align:right;";
                else if (align.equals("center"))
                    attrs = attrs + "text-align:center;";
                else if (align.equals("justify"))
                    attrs = attrs + "text-align:justify;";
            }
            if (tag.getAttribute("valign") != null) {
                String align = tag.getAttribute("valign").getValue();
                if (align.equals("top"))
                    attrs = attrs + "vertical-align:top;";
                else if (align.equals("middle"))
                    attrs = attrs + "vertical-align:middle;";
                else if (align.equals("bottom"))
                    attrs = attrs + "vertical-align:bottom;";
                else if (align.equals("baseline"))
                    attrs = attrs + "vertical-align:baseline;";
            }
        }
        //Text properties
        if (tag.getLocalName().equals("font")) {
            if (tag.getAttribute("color") != null)
                attrs = attrs + "color: " + tag.getAttribute("color") + ";";
            if (tag.getAttribute("face") != null)
                attrs = attrs + "font-family: " + tag.getAttribute("face") + ";";
            if (tag.getAttribute("size") != null) {
                String sz = tag.getAttribute("size").getValue();
                String ret = "normal";
                if (sz.equals("1")) ret = "xx-small";
                else if (sz.equals("2")) ret = "x-small";
                else if (sz.equals("3")) ret = "small";
                else if (sz.equals("4")) ret = "normal";
                else if (sz.equals("5")) ret = "large";
                else if (sz.equals("6")) ret = "x-large";
                else if (sz.equals("7")) ret = "xx-large";
                else if (sz.startsWith("+")) {
                    String sn = sz.substring(1);
                    if (sn.equals("1")) ret = "120%";
                    else if (sn.equals("2")) ret = "140%";
                    else if (sn.equals("3")) ret = "160%";
                    else if (sn.equals("4")) ret = "180%";
                    else if (sn.equals("5")) ret = "200%";
                    else if (sn.equals("6")) ret = "210%";
                    else if (sn.equals("7")) ret = "220%";
                } else if (sz.startsWith("-")) {
                    String sn = sz.substring(1);
                    if (sn.equals("1")) ret = "90%";
                    else if (sn.equals("2")) ret = "80%";
                    else if (sn.equals("3")) ret = "70%";
                    else if (sn.equals("4")) ret = "60%";
                    else if (sn.equals("5")) ret = "50%";
                    else if (sn.equals("6")) ret = "40%";
                    else if (sn.equals("7")) ret = "30%";
                }
                attrs = attrs + "font-size: " + ret;
            }
        }

        if (attrs.length() > 0)
            tag.setAttribute("XDefaultStyle", tag.getAttribute("XDefaultStyle") + ";" + attrs);

        for (XmlTag child : tag.getSubTags()) {
            attributesToStyles((HtmlTag) child, tab_inh);
        }
    }

    /**
     * Computes a length defined using an HTML attribute (e.g. width for tables).
     *
     * @param value The attribute value
     * @param whole the value used as 100% when value is a percentage
     * @return the computed length
     */
    public static int computeAttributeLength(String value, int whole) throws NumberFormatException {
        String sval = value.trim().toLowerCase();
        if (sval.endsWith("%")) {
            double val = Double.parseDouble(sval.substring(0, sval.length() - 1));
            return (int) Math.round(val * whole / 100.0);
        } else if (sval.endsWith("px")) {
            return (int) Math.rint(Double.parseDouble(sval.substring(0, sval.length() - 2)));
        } else {
            return (int) Math.rint(Double.parseDouble(sval));
        }
    }

    /**
     * Creates a CSS length or percentage from a string.
     *
     * @param spec The string length or percentage according to CSS
     * @return the length or percentage
     */
    public static TermLengthOrPercent createLengthOrPercent(String spec) {
        spec = spec.trim();
        if (spec.endsWith("%")) {
            try {
                float val = Float.parseFloat(spec.substring(0, spec.length() - 1));
                TermPercent perc = CSSFactory.getTermFactory().createPercent(val);
                return perc;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            try {
                float val = Float.parseFloat(spec);
                TermLength len = CSSFactory.getTermFactory().createLength(val, TermLength.Unit.px);
                return len;
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private static String applyBorders(String template, String dir) {
        return template.replaceAll("@", dir);
    }

    //=======================================================================

    /**
     * Provides a cleanup of a HTML DOM tree according to the HTML syntax restrictions.
     * Currently, following actions are implemented:
     * <ul>
     * <li>Table cleanup
     * <ul>
     * <li>elements that are not acceptable witin a table are moved before the table</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param doc the processed DOM Document.
     */
    public static void normalizeHTMLTree(XmlDocument doc) {
        //normalize tables
        XmlTag[] tables = doc.getRootTag().findSubTags("table");
        for (XmlTag table : tables) {
            Vector<HtmlTag> tags = new Vector<>();

            recursiveFindBadNodesInTable((HtmlTag)table, null, tags);

            for (HtmlTag tag : tags) {
                moveSubtreeBefore(tag, (HtmlTag)table);
            }
        }
    }

    /**
     * Finds all the nodes in a table that cannot be contained in the table according to the HTML syntax.
     *
     * @param tag      table root
     * @param cellroot last cell root
     * @param out      resulting list of nodes
     */
    private static void recursiveFindBadNodesInTable(HtmlTag tag, HtmlTag cellroot, Vector<HtmlTag> out) {
        tag.getName();

        if (tag.getName().equalsIgnoreCase("table")) {
            if (cellroot != null) //do not enter nested tables
                return;
        } else if (tag.getName().equalsIgnoreCase("tbody") ||
                tag.getName().equalsIgnoreCase("thead") ||
                tag.getName().equalsIgnoreCase("tfoot") ||
                tag.getName().equalsIgnoreCase("tr") ||
                tag.getName().equalsIgnoreCase("col") ||
                tag.getName().equalsIgnoreCase("colgroup")) {
        } else if (tag.getName().equalsIgnoreCase("td") ||
                tag.getName().equalsIgnoreCase("th") ||
                tag.getName().equalsIgnoreCase("caption")) {
            cellroot = tag;
        } else //other elements
        {
            if (cellroot == null) {
                out.add(tag);
                return;
            }
        }

        for (XmlTag child : tag.getSubTags()) {
            recursiveFindBadNodesInTable((HtmlTag)child, cellroot, out);
        }
    }

    private static void moveSubtreeBefore(HtmlTag root, HtmlTag ref) {
        ref.getParentTag().addBefore(root, ref);
    }

}
