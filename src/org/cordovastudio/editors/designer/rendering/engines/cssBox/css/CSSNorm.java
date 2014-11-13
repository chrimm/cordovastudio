/*
 * CSSNorm.java
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
 * Created on 21. leden 2005, 22:28
 */

package org.cordovastudio.editors.designer.rendering.engines.cssBox.css;

/**
 * This class provides standard style sheets for the browser.
 *
 * @author radek
 */
public class CSSNorm {

    /**
     * Returns a style sheet string containig the W3C Standard Rendering styles
     * according to http://www.w3.org/TR/2014/REC-html5-20141028/rendering.html
     *
     * @return The standard rendering styles for HTML5 documents
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public static String html5StdStyleSheet() {
        return "[hidden], area, base, basefont, datalist, head, link,\n" +
                "meta, noembed, noframes, param, rp, script, source, style, template, track, title {\n" +
                "  display: none;\n" +
                "}\n" +
                "\n" +
                "embed[hidden] { display: inline; height: 0; width: 0; }\n" +
                "html, body { display: block; }\n" +
                "address, blockquote, center, div, figure, figcaption, footer, form,\n" +
                "header, hr, legend, listing, main, p, plaintext, pre, xmp {\n" +
                "  display: block;\n" +
                "}\n" +
                "\n" +
                "blockquote, figure, listing, p, plaintext, pre, xmp {\n" +
                "  margin-top: 1em; margin-bottom: 1em;\n" +
                "}\n" +
                "\n" +
                "blockquote, figure { margin-left: 40px; margin-right: 40px; }\n" +
                "\n" +
                "address { font-style: italic; }\n" +
                "listing, plaintext, pre, xmp {\n" +
                "  font-family: monospace; white-space: pre;\n" +
                "}" +
                "pre[wrap] { white-space: pre-wrap; }\n" +
                "form { margin-bottom: 1em; }\n" +
                "cite, dfn, em, i, var { font-style: italic; }\n" +
                "b, strong { font-weight: bolder; }\n" +
                "code, kbd, samp, tt { font-family: monospace; }\n" +
                "big { font-size: larger; }\n" +
                "small { font-size: smaller; }\n" +
                "\n" +
                "sub { vertical-align: sub; }\n" +
                "sup { vertical-align: super; }\n" +
                "sub, sup { line-height: normal; font-size: smaller; }\n" +
                "\n" +
                "\n" +
                "ruby { display: ruby; }\n" +
                "rb   { display: ruby-base; white-space: nowrap; }\n" +
                "rt   {\n" +
                "    display: ruby-text;\n" +
                "    white-space: nowrap;\n" +
                "    font-size: 50%;\n" +
                "    font-variant-east-asian: ruby;\n" +
                "    text-emphasis: none;\n" +
                "}\n" +
                "rbc  { display: ruby-base-container; }\n" +
                "rtc  { display: ruby-text-container; }\n" +
                "ruby, rb, rt, rbc, rtc { unicode-bidi: isolate; }\n" +
                "\n" +
                "\n" +
                ":link { color: #0000EE; }\n" +
                ":visited { color: #551A8B; }\n" +
                ":link, :visited { text-decoration: underline; }\n" +
                "a:link[rel~=help], a:visited[rel~=help],\n" +
                "area:link[rel~=help], area:visited[rel~=help] { cursor: help; }\n" +
                "\n" +
                ":focus { outline: auto; }\n" +
                "\n" +
                "mark { background: yellow; color: black; } /* this color is just a suggestion and can be changed based on implementation feedback */\n" +
                "\n" +
                "abbr[title], acronym[title] { text-decoration: dotted underline; }\n" +
                "ins, u { text-decoration: underline; }\n" +
                "del, s, strike { text-decoration: line-through; }\n" +
                "blink { text-decoration: blink; }\n" +
                "\n" +
                "q::before { content: open-quote; }\n" +
                "q::after { content: close-quote; }\n" +
                "\n" +
                "br { content: '\\A'; white-space: pre; } /* this also has bidi implications */\n" +
                "nobr { white-space: nowrap; }\n" +
                "wbr { content: '\\200B'; } /* this also has bidi implications */\n" +
                "nobr wbr { white-space: normal; }\n" +
                "br[clear=left i] { clear: left; }\n" +
                "br[clear=right i] { clear: right; }\n" +
                "br[clear=all i], br[clear=both i] { clear: both; }\n" +
                "[dir]:dir(ltr), bdi:dir(ltr), input[type=tel]:dir(ltr) { direction: ltr; }\n" +
                "[dir]:dir(rtl), bdi:dir(rtl) { direction: rtl; }\n" +
                "\n" +
                "address, blockquote, center, div, figure, figcaption, footer, form,\n" +
                "header, hr, legend, listing, p, plaintext, pre, xmp, article,\n" +
                "aside, h1, h2, h3, h4, h5, h6, hgroup, main, nav, section, table, caption,\n" +
                "colgroup, col, thead, tbody, tfoot, tr, td, th, dir, dd, dl, dt,\n" +
                "ol, ul, li, bdi, output, [dir=ltr i], [dir=rtl i], [dir=auto i] {\n" +
                "  unicode-bidi: isolate; \n" +
                "}\n" +
                "\n" +
                "bdo, bdo[dir] { unicode-bidi: isolate-override; } \n" +
                "\n" +
                "textarea[dir=auto i], input[type=text][dir=auto i], input[type=search][dir=auto i],\n" +
                "input[type=tel][dir=auto i], input[type=url][dir=auto i], input[type=email][dir=auto i],\n" +
                "pre[dir=auto i] { unicode-bidi: plaintext; }\n" +
                "article, aside, h1, h2, h3, h4, h5, h6, hgroup, nav, section {\n" +
                "  display: block;\n" +
                "}\n" +
                "\n" +
                "h1 { margin-top: 0.67em; margin-bottom: 0.67em; font-size: 2.00em; font-weight: bold; }\n" +
                "h2 { margin-top: 0.83em; margin-bottom: 0.83em; font-size: 1.50em; font-weight: bold; }\n" +
                "h3 { margin-top: 1.00em; margin-bottom: 1.00em; font-size: 1.17em; font-weight: bold; }\n" +
                "h4 { margin-top: 1.33em; margin-bottom: 1.33em; font-size: 1.00em; font-weight: bold; }\n" +
                "h5 { margin-top: 1.67em; margin-bottom: 1.67em; font-size: 0.83em; font-weight: bold; }\n" +
                "h6 { margin-top: 2.33em; margin-bottom: 2.33em; font-size: 0.67em; font-weight: bold; }\n" +
                "dir, dd, dl, dt, ol, ul { display: block; }\n" +
                "li { display: list-item; }\n" +
                "\n" +
                "dir, dl, ol, ul { margin-top: 1em; margin-bottom: 1em; }\n" +
                "\n" +
                "dir dir, dir dl, dir ol, dir ul,\n" +
                "dl dir, dl dl, dl ol, dl ul,\n" +
                "ol dir, ol dl, ol ol, ol ul,\n" +
                "ul dir, ul dl, ul ol, ul ul {\n" +
                "  margin-top: 0; margin-bottom: 0;\n" +
                "}\n" +
                "\n" +
                "dd { margin-left: 40px; } /* LTR-specific: use 'margin-right' for rtl elements */\n" +
                "dir, ol, ul { padding-left: 40px; } /* LTR-specific: use 'padding-right' for rtl elements */\n" +
                "\n" +
                "ol { list-style-type: decimal; }\n" +
                "\n" +
                "dir, ul { list-style-type: disc; }\n" +
                "\n" +
                "dir dir, dir ul,\n" +
                "ol dir, ol ul,\n" +
                "ul dir, ul ul {\n" +
                "  list-style-type: circle;\n" +
                "}\n" +
                "\n" +
                "dir dir dir, dir dir ul,\n" +
                "dir ol dir, dir ol ul,\n" +
                "dir ul dir, dir ul ul,\n" +
                "ol dir dir, ol dir ul,\n" +
                "ol ol dir, ol ol ul,\n" +
                "ol ul dir, ol ul ul,\n" +
                "ul dir dir, ul dir ul,\n" +
                "ul ol dir, ul ol ul,\n" +
                "ul ul dir, ul ul ul {\n" +
                "  list-style-type: square;\n" +
                "}\n" +
                "ol[type=1], li[type=1] { list-style-type: decimal; }\n" +
                "ol[type=a], li[type=a] { list-style-type: lower-alpha; }\n" +
                "ol[type=A], li[type=A] { list-style-type: upper-alpha; }\n" +
                "ol[type=i], li[type=i] { list-style-type: lower-roman; }\n" +
                "ol[type=I], li[type=I] { list-style-type: upper-roman; }\n" +
                "ul[type=disc i], li[type=disc i] { list-style-type: disc; }\n" +
                "ul[type=circle i], li[type=circle i] { list-style-type: circle; }\n" +
                "ul[type=square i], li[type=square i] { list-style-type: square; }\n" +
                "table { display: table; }\n" +
                "caption { display: table-caption; }\n" +
                "colgroup, colgroup[hidden] { display: table-column-group; }\n" +
                "col, col[hidden] { display: table-column; }\n" +
                "thead, thead[hidden] { display: table-header-group; }\n" +
                "tbody, tbody[hidden] { display: table-row-group; }\n" +
                "tfoot, tfoot[hidden] { display: table-footer-group; }\n" +
                "tr, tr[hidden] { display: table-row; }\n" +
                "td, th, td[hidden], th[hidden] { display: table-cell; }\n" +
                "\n" +
                "colgroup[hidden], col[hidden], thead[hidden], tbody[hidden],\n" +
                "tfoot[hidden], tr[hidden], td[hidden], th[hidden] {\n" +
                "  visibility: collapse;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "  box-sizing: border-box;\n" +
                "  border-spacing: 2px;\n" +
                "  border-collapse: separate;\n" +
                "  text-indent: initial;\n" +
                "}\n" +
                "td, th { padding: 1px; }\n" +
                "th { font-weight: bold; }\n" +
                "\n" +
                "thead, tbody, tfoot, table > tr { vertical-align: middle; }\n" +
                "tr, td, th { vertical-align: inherit; }\n" +
                "\n" +
                "table, td, th { border-color: gray; }\n" +
                "thead, tbody, tfoot, tr { border-color: inherit; }\n" +
                "table[rules=none i], table[rules=groups i], table[rules=rows i],\n" +
                "table[rules=cols i], table[rules=all i], table[frame=void i],\n" +
                "table[frame=above i], table[frame=below i], table[frame=hsides i],\n" +
                "table[frame=lhs i], table[frame=rhs i], table[frame=vsides i],\n" +
                "table[frame=box i], table[frame=border i],\n" +
                "table[rules=none i] > tr > td, table[rules=none i] > tr > th,\n" +
                "table[rules=groups i] > tr > td, table[rules=groups i] > tr > th,\n" +
                "table[rules=rows i] > tr > td, table[rules=rows i] > tr > th,\n" +
                "table[rules=cols i] > tr > td, table[rules=cols i] > tr > th,\n" +
                "table[rules=all i] > tr > td, table[rules=all i] > tr > th,\n" +
                "table[rules=none i] > thead > tr > td, table[rules=none i] > thead > tr > th,\n" +
                "table[rules=groups i] > thead > tr > td, table[rules=groups i] > thead > tr > th,\n" +
                "table[rules=rows i] > thead > tr > td, table[rules=rows i] > thead > tr > th,\n" +
                "table[rules=cols i] > thead > tr > td, table[rules=cols i] > thead > tr > th,\n" +
                "table[rules=all i] > thead > tr > td, table[rules=all i] > thead > tr > th,\n" +
                "table[rules=none i] > tbody > tr > td, table[rules=none i] > tbody > tr > th,\n" +
                "table[rules=groups i] > tbody > tr > td, table[rules=groups i] > tbody > tr > th,\n" +
                "table[rules=rows i] > tbody > tr > td, table[rules=rows i] > tbody > tr > th,\n" +
                "table[rules=cols i] > tbody > tr > td, table[rules=cols i] > tbody > tr > th,\n" +
                "table[rules=all i] > tbody > tr > td, table[rules=all i] > tbody > tr > th,\n" +
                "table[rules=none i] > tfoot > tr > td, table[rules=none i] > tfoot > tr > th,\n" +
                "table[rules=groups i] > tfoot > tr > td, table[rules=groups i] > tfoot > tr > th,\n" +
                "table[rules=rows i] > tfoot > tr > td, table[rules=rows i] > tfoot > tr > th,\n" +
                "table[rules=cols i] > tfoot > tr > td, table[rules=cols i] > tfoot > tr > th,\n" +
                "table[rules=all i] > tfoot > tr > td, table[rules=all i] > tfoot > tr > th {\n" +
                "  border-color: black;\n" +
                "}\n" +
                "table[align=left i] { float: left; }\n" +
                "table[align=right i] { float: right; }\n" +
                "table[align=center i] { margin-left: auto; margin-right: auto; }\n" +
                "thead[align=absmiddle i], tbody[align=absmiddle i], tfoot[align=absmiddle i],\n" +
                "tr[align=absmiddle i], td[align=absmiddle i], th[align=absmiddle i] {\n" +
                "  text-align: center;\n" +
                "}\n" +
                "\n" +
                "caption[align=bottom i] { caption-side: bottom; }\n" +
                "p[align=left i], h1[align=left i], h2[align=left i], h3[align=left i],\n" +
                "h4[align=left i], h5[align=left i], h6[align=left i] {\n" +
                "  text-align: left;\n" +
                "}\n" +
                "p[align=right i], h1[align=right i], h2[align=right i], h3[align=right i],\n" +
                "h4[align=right i], h5[align=right i], h6[align=right i] {\n" +
                "  text-align: right;\n" +
                "}\n" +
                "p[align=center i], h1[align=center i], h2[align=center i], h3[align=center i],\n" +
                "h4[align=center i], h5[align=center i], h6[align=center i] {\n" +
                "  text-align: center;\n" +
                "}\n" +
                "p[align=justify i], h1[align=justify i], h2[align=justify i], h3[align=justify i],\n" +
                "h4[align=justify i], h5[align=justify i], h6[align=justify i] {\n" +
                "  text-align: justify;\n" +
                "}\n" +
                "thead[valign=top i], tbody[valign=top i], tfoot[valign=top i],\n" +
                "tr[valign=top i], td[valign=top i], th[valign=top i] {\n" +
                "  vertical-align: top;\n" +
                "}\n" +
                "thead[valign=middle i], tbody[valign=middle i], tfoot[valign=middle i],\n" +
                "tr[valign=middle i], td[valign=middle i], th[valign=middle i] {\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                "thead[valign=bottom i], tbody[valign=bottom i], tfoot[valign=bottom i],\n" +
                "tr[valign=bottom i], td[valign=bottom i], th[valign=bottom i] {\n" +
                "  vertical-align: bottom;\n" +
                "}\n" +
                "thead[valign=baseline i], tbody[valign=baseline i], tfoot[valign=baseline i],\n" +
                "tr[valign=baseline i], td[valign=baseline i], th[valign=baseline i] {\n" +
                "  vertical-align: baseline;\n" +
                "}\n" +
                "\n" +
                "td[nowrap], th[nowrap] { white-space: nowrap; }\n" +
                "\n" +
                "table[rules=none i], table[rules=groups i], table[rules=rows i],\n" +
                "table[rules=cols i], table[rules=all i] {\n" +
                "  border-style: hidden;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table[border] { border-style: outset; } /* only if border is not equivalent to zero */\n" +
                "table[frame=void i] { border-style: hidden; }\n" +
                "table[frame=above i] { border-style: outset hidden hidden hidden; }\n" +
                "table[frame=below i] { border-style: hidden hidden outset hidden; }\n" +
                "table[frame=hsides i] { border-style: outset hidden outset hidden; }\n" +
                "table[frame=lhs i] { border-style: hidden hidden hidden outset; }\n" +
                "table[frame=rhs i] { border-style: hidden outset hidden hidden; }\n" +
                "table[frame=vsides i] { border-style: hidden outset; }\n" +
                "table[frame=box i], table[frame=border i] { border-style: outset; }\n" +
                "\n" +
                "table[border] > tr > td, table[border] > tr > th,\n" +
                "table[border] > thead > tr > td, table[border] > thead > tr > th,\n" +
                "table[border] > tbody > tr > td, table[border] > tbody > tr > th,\n" +
                "table[border] > tfoot > tr > td, table[border] > tfoot > tr > th {\n" +
                "  /* only if border is not equivalent to zero */\n" +
                "  border-width: 1px;\n" +
                "  border-style: inset;\n" +
                "}\n" +
                "table[rules=none i] > tr > td, table[rules=none i] > tr > th,\n" +
                "table[rules=none i] > thead > tr > td, table[rules=none i] > thead > tr > th,\n" +
                "table[rules=none i] > tbody > tr > td, table[rules=none i] > tbody > tr > th,\n" +
                "table[rules=none i] > tfoot > tr > td, table[rules=none i] > tfoot > tr > th,\n" +
                "table[rules=groups i] > tr > td, table[rules=groups i] > tr > th,\n" +
                "table[rules=groups i] > thead > tr > td, table[rules=groups i] > thead > tr > th,\n" +
                "table[rules=groups i] > tbody > tr > td, table[rules=groups i] > tbody > tr > th,\n" +
                "table[rules=groups i] > tfoot > tr > td, table[rules=groups i] > tfoot > tr > th,\n" +
                "table[rules=rows i] > tr > td, table[rules=rows i] > tr > th,\n" +
                "table[rules=rows i] > thead > tr > td, table[rules=rows i] > thead > tr > th,\n" +
                "table[rules=rows i] > tbody > tr > td, table[rules=rows i] > tbody > tr > th,\n" +
                "table[rules=rows i] > tfoot > tr > td, table[rules=rows i] > tfoot > tr > th {\n" +
                "  border-width: 1px;\n" +
                "  border-style: none;\n" +
                "}\n" +
                "table[rules=cols i] > tr > td, table[rules=cols i] > tr > th,\n" +
                "table[rules=cols i] > thead > tr > td, table[rules=cols i] > thead > tr > th,\n" +
                "table[rules=cols i] > tbody > tr > td, table[rules=cols i] > tbody > tr > th,\n" +
                "table[rules=cols i] > tfoot > tr > td, table[rules=cols i] > tfoot > tr > th {\n" +
                "  border-width: 1px;\n" +
                "  border-style: none solid;\n" +
                "}\n" +
                "table[rules=all i] > tr > td, table[rules=all i] > tr > th,\n" +
                "table[rules=all i] > thead > tr > td, table[rules=all i] > thead > tr > th,\n" +
                "table[rules=all i] > tbody > tr > td, table[rules=all i] > tbody > tr > th,\n" +
                "table[rules=all i] > tfoot > tr > td, table[rules=all i] > tfoot > tr > th {\n" +
                "  border-width: 1px;\n" +
                "  border-style: solid;\n" +
                "}\n" +
                "\n" +
                "table[rules=groups i] > colgroup {\n" +
                "  border-left-width: 1px;\n" +
                "  border-left-style: solid;\n" +
                "  border-right-width: 1px;\n" +
                "  border-right-style: solid;\n" +
                "}\n" +
                "table[rules=groups i] > thead,\n" +
                "table[rules=groups i] > tbody,\n" +
                "table[rules=groups i] > tfoot {\n" +
                "  border-top-width: 1px;\n" +
                "  border-top-style: solid;\n" +
                "  border-bottom-width: 1px;\n" +
                "  border-bottom-style: solid;\n" +
                "}\n" +
                "\n" +
                "table[rules=rows i] > tr, table[rules=rows i] > thead > tr,\n" +
                "table[rules=rows i] > tbody > tr, table[rules=rows i] > tfoot > tr {\n" +
                "  border-top-width: 1px;\n" +
                "  border-top-style: solid;\n" +
                "  border-bottom-width: 1px;\n" +
                "  border-bottom-style: solid;\n" +
                "}\n" +
                "input, select, option, optgroup, button, textarea, keygen {\n" +
                "  text-indent: initial;\n" +
                "}\n" +
                "\n" +
                "textarea { white-space: pre-wrap; }\n" +
                "\n" +
                "input[type=\"radio\"], input[type=\"checkbox\"], input[type=\"reset\"], input[type=\"button\"],\n" +
                "input[type=\"submit\"], select, button {\n" +
                "  box-sizing: border-box;\n" +
                "}\n" +
                "hr { color: gray; border-style: inset; border-width: 1px; margin: 0.5em auto; }\n" +
                "hr[align=left] { margin-left: 0; margin-right: auto; }\n" +
                "hr[align=right] { margin-left: auto; margin-right: 0; }\n" +
                "hr[align=center] { margin-left: auto; margin-right: auto; }\n" +
                "hr[color], hr[noshade] { border-style: solid; }\n" +
                "fieldset {\n" +
                "  margin-left: 2px; margin-right: 2px;\n" +
                "  border: groove 2px ThreeDFace;\n" +
                "  padding: 0.35em 0.625em 0.75em;\n" +
                "}\n" +
                "\n" +
                "legend {\n" +
                "  padding-left: 2px; padding-right: 2px;\n" +
                "}\n" +
                "iframe { border: 2px inset; }\n" +
                "video { object-fit: contain; }\n" +
                "iframe[frameborder=0], iframe[frameborder=no i] { border: none; }\n" +
                "\n" +
                "applet[align=left i], embed[align=left i], iframe[align=left i],\n" +
                "img[align=left i], input[type=image i][align=left i], object[align=left i] {\n" +
                "  float: left;\n" +
                "}\n" +
                "\n" +
                "applet[align=right i], embed[align=right i], iframe[align=right i],\n" +
                "img[align=right i], input[type=image i][align=right i], object[align=right i] {\n" +
                "  float: right;\n" +
                "}\n" +
                "\n" +
                "applet[align=top i], embed[align=top i], iframe[align=top i],\n" +
                "img[align=top i], input[type=image i][align=top i], object[align=top i] {\n" +
                "  vertical-align: top;\n" +
                "}\n" +
                "\n" +
                "applet[align=baseline i], embed[align=baseline i], iframe[align=baseline i],\n" +
                "img[align=baseline i], input[type=image i][align=baseline i], object[align=baseline i] {\n" +
                "  vertical-align: baseline;\n" +
                "}\n" +
                "\n" +
                "applet[align=texttop i], embed[align=texttop i], iframe[align=texttop i],\n" +
                "img[align=texttop i], input[type=image i][align=texttop i], object[align=texttop i] {\n" +
                "  vertical-align: text-top;\n" +
                "}\n" +
                "\n" +
                "applet[align=absmiddle i], embed[align=absmiddle i], iframe[align=absmiddle i],\n" +
                "img[align=absmiddle i], input[type=image i][align=absmiddle i], object[align=absmiddle i],\n" +
                "applet[align=abscenter i], embed[align=abscenter i], iframe[align=abscenter i],\n" +
                "img[align=abscenter i], input[type=image i][align=abscenter i], object[align=abscenter i] {\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                "\n" +
                "applet[align=bottom i], embed[align=bottom i], iframe[align=bottom i],\n" +
                "img[align=bottom i], input[type=image i][align=bottom i],\n" +
                "object[align=bottom i] {\n" +
                "  vertical-align: bottom;\n" +
                "}";
    }

    /**
     * Defines a standard HTML style sheet defining the basic style of the individual elements.
     * It corresponds to the CSS2.1 recommendation (Appendix D).
     *
     * @return the style string
     */
    public static String stdStyleSheet() {
        return
                "html, address," +
                        "blockquote," +
                        "body, dd, div," +
                        "dl, dt, fieldset, form," +
                        "frame, frameset," +
                        "h1, h2, h3, h4," +
                        "h5, h6, noframes," +
                        "ol, p, ul, center," +
                        "dir, hr, menu, pre   { display: block }" +
                        "li              { display: list-item }" +
                        "head            { display: none }" +
                        "table           { display: table }" +
                        "tr              { display: table-row }" +
                        "thead           { display: table-header-group }" +
                        "tbody           { display: table-row-group }" +
                        "tfoot           { display: table-footer-group }" +
                        "col             { display: table-column }" +
                        "colgroup        { display: table-column-group }" +
                        "td, th          { display: table-cell; }" +
                        "caption         { display: table-caption }" +
                        "th              { font-weight: bolder; text-align: center }" +
                        "caption         { text-align: center }" +
                        "body            { margin: 8px; line-height: 1.12 }" +
                        "h1              { font-size: 2em; margin: .67em 0 }" +
                        "h2              { font-size: 1.5em; margin: .75em 0 }" +
                        "h3              { font-size: 1.17em; margin: .83em 0 }" +
                        "h4, p," +
        /*"blockquote, ul,"+
        "fieldset, form,"+
        "ol, dl, dir,"+*/
                        "menu            { margin: 1.12em 0 }" +
                        "h5              { font-size: .83em; margin: 1.5em 0 }" +
                        "h6              { font-size: .75em; margin: 1.67em 0 }" +
                        "h1, h2, h3, h4," +
                        "h5, h6, b," +
                        "strong          { font-weight: bolder }" +
                        "blockquote      { margin-left: 40px; margin-right: 40px }" +
                        "i, cite, em," +
                        "var, address    { font-style: italic }" +
                        "pre, tt, code," +
                        "kbd, samp       { font-family: monospace }" +
                        "pre             { white-space: pre }" +
                        "button, textarea," +
                        "input, object," +
                        "select          { display:inline-block; }" +
                        "big             { font-size: 1.17em }" +
                        "small, sub, sup { font-size: .83em }" +
                        "sub             { vertical-align: sub }" +
                        "sup             { vertical-align: super }" +
                        "table           { border-spacing: 2px; }" +
                        "thead, tbody," +
                        "tfoot           { vertical-align: middle }" +
                        "tr, td, th      { vertical-align: inherit }" +
                        "s, strike, del  { text-decoration: line-through }" +
                        "hr              { border: 1px inset }" +
                        "ol, ul, dir," +
                        "menu, dd        { margin-left: 40px }" +
                        "ol              { list-style-type: decimal }" +
                        "ol ul, ul ol," +
                        "ul ul, ol ol    { margin-top: 0; margin-bottom: 0 }" +
                        "u, ins          { text-decoration: underline }" +
                        //"br:before       { content: \"\\A\" }"+
                        //":before, :after { white-space: pre-line }"+
                        "center          { text-align: center }" +
                        "abbr, acronym   { font-variant: small-caps; letter-spacing: 0.1em }" +
                        ":link, :visited { text-decoration: underline }" +
                        ":focus          { outline: thin dotted invert }" +

                        "BDO[DIR=\"ltr\"]  { direction: ltr; unicode-bidi: bidi-override }" +
                        "BDO[DIR=\"rtl\"]  { direction: rtl; unicode-bidi: bidi-override }" +

                        "*[DIR=\"ltr\"]    { direction: ltr; unicode-bidi: embed }" +
                        "*[DIR=\"rtl\"]    { direction: rtl; unicode-bidi: embed }";
    }

    /**
     * A style sheet defining the additional basic style not covered by the CSS recommendation.
     *
     * @return The style string
     */
    public static String userStyleSheet() {
        return
                "body   { color: black; background-color: #fafafa;}" +
                        "a[href]{ color: blue; text-decoration: underline; }" +
                        "script { display: none; }" +
                        "style  { display: none; }" +
                        "option { display: none; }" +
                        "br     { display: block; }" +
                        "hr     { display: block; margin-top: 1px solid; }" +

                        //standard <ul> margin according to Mozilla
                        "ul     { margin-left: 0; padding-left: 40px; }";
    }

    /**
     * A style sheet defining a basic style of form fields. This style sheet may be used
     * for a simple rendering of form fields when their functionality is not implemented
     * in another way.
     *
     * @return The style string
     */
    public static String formsStyleSheet() {
        return
                "input, textarea, select { " +
                        "  font-size: 80%;" +
                        "  color: black;" +
                        "  white-space: pre;" +
                        "}" +
                        "input[type='submit']," +
                        "input[type='reset']," +
                        "input[type='button'] {" +
                        "  display: inline-block;" +
                        "  box-sizing: border-box;" +
                        "  border-right: 1px solid black;" +
                        "  border-bottom: 1px solid black;" +
                        "  border-top: 1px solid white;" +
                        "  border-left: 1px solid white;" +
                        "  background-color: #ddd;" +
                        "  padding: 0 0.5em;" +
                        "}" +
                        "input[type='submit']:before," +
                        "input[type='reset']:before," +
                        "input[type='button']:before {" +
                        "  display: block;" +
                        "  text-align: center;" +
                        "  content: attr(value);" +
                        "}" +
                        "input[type='radio']," +
                        "input[type='checkbox'] {" +
                        "  display: inline-block;" +
                        "  border: 1px solid black;" +
                        "  background-color: white;" +
                        "  width: 6px;" +
                        "  height: 10px;" +
                        "  line-height: 9px;" +
                        "  font-size: 10px;" +
                        "  padding: 0 2px;" +
                        "}" +
                        "input[type='radio']:before," +
                        "input[type='checkbox']:before {" +
                        "  content: ' ';" +
                        "}" +
                        "input[checked]:before {" +
                        "  content: 'x';" +
                        "}" +
                        "input[type='text']," +
                        "input[type='password']," +
                        "textarea," +
                        "select {" +
                        "  display: inline-block;" +
                        "  border-right: 1px solid #eee;" +
                        "  border-bottom: 1px solid #eee;" +
                        "  border-top: 1px solid black;" +
                        "  border-left: 1px solid black;" +
                        "  background-color: #fff;" +
                        "  width: 15em;" +
                        "  overflow: hidden;" +
                        "  padding: 0;" +
                        "}" +
                        "input[type='text']:before," +
                        "input[type='password']:before {" +
                        "  content: attr(value);" +
                        "}" +
                        "input[type='hidden'] {" +
                        "  display: none;" +
                        "}" +
                        "textarea {" +
                        "  height: 2em;" +
                        "  width: 15em;" +
                        "  white-space: normal;" +
                        "}" +
                        "select {" +
                        "  white-space: normal;" +
                        "}" +
                        "select option {" +
                        "  display: none;" +
                        "}" +
                        "select option:nth-of-type(1) {" +
                        "  display: block;" +
                        "}" +
                        "select[size] option:nth-of-type(2)," +
                        "select[size] option:nth-of-type(3) {" +
                        "  display: block;" +
                        "}" +
                        "select[size='1'] option:nth-of-type(2)," +
                        "select[size='1'] option:nth-of-type(3) {" +
                        "  display: none;" +
                        "}" +
                        "select[size='2'] option:nth-of-type(3) {" +
                        "  display: none;" +
                        "}";
    }

}
