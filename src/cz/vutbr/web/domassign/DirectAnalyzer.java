/**
 * SimpleAnalyzer.java
 * Copyright (C) 2012 Radek Burget
 *
 * Created on 6.6.2012, 13:57:00 by burgetr
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */
package cz.vutbr.web.domassign;

import com.intellij.psi.PsiElement;
import com.intellij.psi.html.HtmlTag;
import cz.vutbr.web.css.*;
import cz.vutbr.web.css.Selector.PseudoDeclaration;
import cz.vutbr.web.csskit.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A simple ananalyzer that computes a style for the individual DOM nodes with no mapping and caching.
 * This analyzer is suitable for obtaining the style of individual elements without computing the style
 * for the whole DOM tree. However, in larger scale, the performance of the individual computation
 * is significantly worse.
 *
 * @author burgetr
 */
public class DirectAnalyzer extends Analyzer {
    private static final Logger log = LoggerFactory.getLogger(DirectAnalyzer.class);

    /**
     * Creates the analyzer for a single style sheet.
     *
     * @param sheet The stylesheet that will be used as the source of rules.
     */
    public DirectAnalyzer(StyleSheet sheet) {
        super(sheet);
    }

    /**
     * Creates the analyzer for multiple style sheets.
     *
     * @param sheets A list of stylesheets that will be used as the source of rules.
     */
    public DirectAnalyzer(List<StyleSheet> sheets) {
        super(sheets);
    }

    /**
     * Computes the style of an element with an eventual pseudo element for the given media.
     *
     * @param tag    The DOM element.
     * @param pseudo A pseudo element that should be used for style computation or <code>null</code> if no pseudo element should be used (e.g. :after).
     * @param media  Used media specification.
     * @return The relevant declarations from the registered style sheets.
     */
    public NodeData getElementStyle(HtmlTag tag, PseudoDeclaration pseudo, MediaSpec media) {
        if (rules == null)
            classifyAllSheets(media);

        List<Declaration> decls = getDeclarationsForElement(tag, pseudo, rules);

        NodeData main = CSSFactory.createNodeData();
        for (Declaration d : decls)
            main.push(d);

        return main;
    }

    /**
     * Computes the style of an element with an eventual pseudo element for the given media.
     *
     * @param tag    The DOM element.
     * @param pseudo A pseudo element that should be used for style computation or <code>null</code> if no pseudo element should be used (e.g. :after).
     * @param media  Used media name (e.g. "screen" or "all")
     * @return The relevant declarations from the registered style sheets.
     */
    public NodeData getElementStyle(HtmlTag tag, PseudoDeclaration pseudo, String media) {
        return getElementStyle(tag, pseudo, new MediaSpec(media));
    }

    //==========================================================================================

    protected List<Declaration> getDeclarationsForElement(HtmlTag tag, PseudoDeclaration pseudo, Holder holder) {
        if (log.isDebugEnabled()) {
            log.debug("Traversal of {} {}.", tag.getName(), tag.getValue());
        }

        // create set of possible candidates applicable to given element
        // set is automatically filtered to not contain duplicates
        Set<RuleSet> candidates = new HashSet<RuleSet>();

        // match element classes
        for (String cname : TagUtil.elementClasses(tag)) {
            // holder contains rule with given class
            List<RuleSet> rules = holder.get(HolderItem.CLASS, cname.toLowerCase());
            if (rules != null)
                candidates.addAll(rules);
        }
        log.trace("After CLASSes {} total candidates.", candidates.size());

        // match IDs
        String id = TagUtil.elementID(tag);
        if (id != null && id.length() != 0) {
            List<RuleSet> rules = holder.get(HolderItem.ID, id.toLowerCase());
            if (rules != null)
                candidates.addAll(rules);
        }
        log.trace("After IDs {} total candidates.", candidates.size());

        // match elements
        String name = TagUtil.elementName(tag);
        if (name != null) {
            List<RuleSet> rules = holder.get(HolderItem.ELEMENT, name.toLowerCase());
            if (rules != null)
                candidates.addAll(rules);
        }
        log.trace("After ELEMENTs {} total candidates.", candidates.size());

        // others
        candidates.addAll(holder.get(HolderItem.OTHER, null));

        // transform to list to speed up traversal
        // and sort rules in order as they were found in CSS definition
        List<RuleSet> clist = new ArrayList<RuleSet>(candidates);
        Collections.sort(clist);

        log.debug("Totally {} candidates.", candidates.size());
        log.trace("With values: {}", clist);

        // resulting list of declaration for this element with no pseudo-selectors (main list)(local cache)
        List<Declaration> eldecl = new ArrayList<Declaration>();

        // for all candidates
        for (RuleSet rule : clist) {

            StyleSheet sheet = rule.getStyleSheet();
            StyleSheet.Origin origin = (sheet == null) ? StyleSheet.Origin.AGENT : sheet.getOrigin();

            // for all selectors inside
            for (CombinedSelector s : rule.getSelectors()) {

                if (!matchSelector(s, tag)) {
                    log.trace("CombinedSelector \"{}\" NOT matched!", s);
                    continue;
                }

                log.trace("CombinedSelector \"{}\" matched", s);

                PseudoDeclaration psel = s.getPseudoElement();
                CombinedSelector.Specificity spec = s.computeSpecificity();
                if (psel == pseudo) {
                    // add to the resulting list
                    for (Declaration d : rule)
                        eldecl.add(new AssignedDeclaration(d, spec, origin));
                }
            }
        }

        // sort declarations
        Collections.sort(eldecl); //sort the main list
        log.debug("Sorted {} declarations.", eldecl.size());
        log.trace("With values: {}", eldecl);

        return eldecl;
    }


    protected boolean matchSelector(CombinedSelector sel, HtmlTag tag) {
        boolean retval = false;
        Selector.Combinator combinator = null;
        // traverse simple selector backwards
        for (int i = sel.size() - 1; i >= 0; i--) {
            // last simple selector
            Selector s = sel.get(i);
            log.trace("Iterating loop with selector {}, combinator {}",
                    s, combinator);

            // decide according to combinator anti-pattern
            if (combinator == null) {
                retval = s.matches(tag);
            } else if (combinator == Selector.Combinator.ADJACENT) {
                PsiElement adjacent = tag.getPrevSibling();
                retval = false;
                if (adjacent != null && adjacent instanceof HtmlTag)
                    retval = s.matches((HtmlTag) adjacent);
            } else if (combinator == Selector.Combinator.PRECEDING) {
                PsiElement preceding = tag.getPrevSibling();
                retval = false;
                do {
                    if (preceding != null) {
                        if (preceding instanceof HtmlTag && s.matches((HtmlTag) preceding))
                            retval = true;
                        else
                            preceding = preceding.getPrevSibling();
                    }
                } while (!retval && preceding != null);
            } else if (combinator == Selector.Combinator.DESCENDANT) {
                PsiElement ancestor = tag.getParent();
                retval = false;
                do {
                    if (ancestor != null) {
                        if (ancestor instanceof HtmlTag && s.matches((HtmlTag) ancestor))
                            retval = true;
                        else
                            ancestor = ancestor.getParent();
                    }
                } while (!retval && ancestor != null);
            } else if (combinator == Selector.Combinator.CHILD) {
                PsiElement parent = tag.getParent();
                retval = false;
                if (parent != null && parent instanceof HtmlTag)
                    retval = s.matches((HtmlTag) parent);
            }

            // set combinator for next loop
            combinator = s.getCombinator();

            // leave loop if not matched
            if (retval == false)
                break;
        }
        return retval;
    }


}
