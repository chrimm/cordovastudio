/*
 * Copyright (c) 2008-2014 Radek Burget
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed node class from org.w3c.dom.Node to com.intellij.psi.PsiElement
 */

package cz.vutbr.web.domassign;

import com.intellij.psi.PsiElement;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import cz.vutbr.web.css.*;
import cz.vutbr.web.css.Selector.PseudoDeclaration;
import cz.vutbr.web.csskit.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Analyzer allows to apply the given style to any document.
 * During the initialization, it divides rules of stylesheet into maps accoring to
 * medias and their type. Afterwards, it is able to return CSS declaration for any
 * DOM tree and media. It allows to use or not to use inheritance.
 *
 * @author Karel Piwko 2008
 * @author Radek Burget 2008-2014
 */
public class Analyzer {

    private static final Logger log = LoggerFactory.getLogger(Analyzer.class);

    /**
     * The style sheets to be processed.
     */
    protected List<StyleSheet> sheets;

    /**
     * Holds maps of declared rules classified into groups of
     * HolderItem (ID, CLASS, ELEMENT, OTHER).
     */
    protected Holder rules;

    /**
     * Creates the analyzer for a single style sheet.
     *
     * @param sheet The stylesheet that will be used as the source of rules.
     */
    public Analyzer(StyleSheet sheet) {
        sheets = new ArrayList<StyleSheet>(1);
        sheets.add(sheet);
    }

    /**
     * Creates the analyzer for multiple style sheets.
     *
     * @param sheets A list of stylesheets that will be used as the source of rules.
     */
    public Analyzer(List<StyleSheet> sheets) {
        this.sheets = sheets;
    }

    /**
     * Evaluates CSS properties of DOM tree
     *
     * @param doc     Document tree
     * @param media   Media
     * @param inherit Use inheritance
     * @return Map where each element contains its CSS properties
     */
    public StyleMap evaluateDOM(XmlDocument doc, MediaSpec media, final boolean inherit) {

        DeclarationMap declarations = assingDeclarationsToDOM(doc, media, inherit);

        StyleMap nodes = new StyleMap(declarations.size());

        Traversal<StyleMap> traversal = new Traversal<StyleMap>(doc, (Object) declarations) {

            @Override
            protected void processElement(StyleMap result, PsiElement current, Object source) {

                NodeData main = CSSFactory.createNodeData();

                // for all declarations available in the main list (pseudo=null)
                List<Declaration> declarations = ((DeclarationMap) source).get(current, null);
                if (declarations != null) {
                    for (Declaration d : declarations) {
                        main.push(d);
                    }
                    if (inherit)
                        main.inheritFrom(result.get(walker.parentElement(), null));
                }
                // concretize values and store them
                result.put(current, null, main.concretize());

                //repeat for the pseudo classes (if any)
                for (PseudoDeclaration pseudo : ((DeclarationMap) source).pseudoSet(current)) {
                    NodeData pdata = CSSFactory.createNodeData();
                    declarations = ((DeclarationMap) source).get(current, pseudo);
                    if (declarations != null) {
                        for (Declaration d : declarations) {
                            pdata.push(d);
                        }
                        pdata.inheritFrom(main); //always inherit from the main element style
                    }
                    // concretize values and store them
                    result.put(current, pseudo, pdata.concretize());
                }

            }
        };

        traversal.levelTraversal(nodes);

        return nodes;
    }

    public StyleMap evaluateDOM(XmlDocument doc, String media, final boolean inherit) {
        return evaluateDOM(doc, new MediaSpec(media), inherit);
    }

    /**
     * Creates map of declarations assigned to each element of a DOM tree
     *
     * @param doc     DOM document
     * @param media   Media type to be used for declarations
     * @param inherit Inheritance (cascade propagation of values)
     * @return Map of elements as keys and their declarations
     */
    protected DeclarationMap assingDeclarationsToDOM(XmlDocument doc, MediaSpec media, final boolean inherit) {

        // classify the rules
        classifyAllSheets(media);

        // resulting map
        DeclarationMap declarations = new DeclarationMap();

        // if the holder is empty skip evaluation
        if (rules != null && !rules.isEmpty()) {

            Traversal<DeclarationMap> traversal = new Traversal<DeclarationMap>(doc, (Object) rules) {
                protected void processElement(DeclarationMap result, PsiElement current, Object source) {
                    if(current instanceof HtmlTag) {
                        assignDeclarationsToElement(result, walker, current, (Holder) source);
                    }
                }
            };

            // list traversal will be enough
            if (!inherit)
                traversal.listTraversal(declarations);
                // we will do level traversal to economize blind returning
                // in tree
            else
                traversal.levelTraversal(declarations);
        }

        return declarations;
    }

    /**
     * Assigns declarations to one element.
     *
     * @param declarations Declarations of all processed elements
     * @param walker       Tree walker
     * @param element          DOM Element
     * @param holder       Wrap
     */
    protected void assignDeclarationsToElement(DeclarationMap declarations, HtmlTreeWalker walker, PsiElement element, Holder holder) {

        if(!(element instanceof XmlTag)) {
            log.error("Element '"+element.toString()+"' is of type "+element.getClass().getName()+" but should be of type XmlTag.", element);
        }

        HtmlTag tag = (HtmlTag)element;
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

        // existing pseudo selectors found
        Set<PseudoDeclaration> pseudos = new HashSet<PseudoDeclaration>();

        // for all candidates
        for (RuleSet rule : clist) {

            StyleSheet sheet = rule.getStyleSheet();
            if (sheet == null)
                log.warn("No source style sheet set for rule: {}", rule.toString());
            StyleSheet.Origin origin = (sheet == null) ? StyleSheet.Origin.AGENT : sheet.getOrigin();

            // for all selectors inside
            for (CombinedSelector s : rule.getSelectors()) {
                // this method does automatic rewind of walker
                if (!matchSelector(s, tag, walker)) {
                    log.trace("CombinedSelector \"{}\" NOT matched!", s);
                    continue;
                }

                log.trace("CombinedSelector \"{}\" matched", s);

                PseudoDeclaration pseudo = s.getPseudoElement();
                CombinedSelector.Specificity spec = s.computeSpecificity();
                if (pseudo == null) {
                    // add to main list
                    for (Declaration d : rule)
                        eldecl.add(new AssignedDeclaration(d, spec, origin));
                } else {
                    // remember the pseudo element
                    pseudos.add(pseudo);
                    // add to pseudo lists
                    for (Declaration d : rule)
                        declarations.addDeclaration(tag, pseudo, new AssignedDeclaration(d, spec, origin));
                }

            }
        }

        // sort declarations
        Collections.sort(eldecl); //sort the main list
        log.debug("Sorted {} declarations.", eldecl.size());
        log.trace("With values: {}", eldecl);
        for (PseudoDeclaration p : pseudos)
            declarations.sortDeclarations(tag, p); //sort pseudos

        // set the main list
        declarations.put(element, null, eldecl);
    }

    protected boolean matchSelector(CombinedSelector sel, HtmlTag tag, HtmlTreeWalker w) {

        // store current walker position
        HtmlTag current = (HtmlTag)w.getCurrentElement();

        boolean retval = false;
        Selector.Combinator combinator = null;
        // traverse simple selector backwards
        for (int i = sel.size() - 1; i >= 0; i--) {
            // last simple selector
            Selector s = sel.get(i);
            //log.trace("Iterating loop with selector {}, combinator {}",	s, combinator);

            // decide according to combinator anti-pattern
            if (combinator == null) {
                retval = s.matches(tag);
            } else if (combinator == Selector.Combinator.ADJACENT) {
                PsiElement adjacent = w.previousSibling();
                retval = false;
                if (adjacent != null && adjacent instanceof HtmlTag)
                    retval = s.matches((HtmlTag)adjacent);
            } else if (combinator == Selector.Combinator.PRECEDING) {
                retval = false;
                PsiElement preceding = w.previousSibling();
                while (!retval && preceding != null && preceding instanceof HtmlTag) {
                    retval = s.matches((HtmlTag)preceding);
                    preceding = w.previousSibling();
                }
            } else if (combinator == Selector.Combinator.DESCENDANT) {
                retval = false;
                PsiElement ancestor = w.parentElement();
                while (!retval && ancestor != null && ancestor instanceof HtmlTag) {
                    retval = s.matches((HtmlTag)ancestor);
                    ancestor = w.parentElement();
                }
            } else if (combinator == Selector.Combinator.CHILD) {
                PsiElement parent = w.parentElement();
                retval = false;
                if (parent != null && (parent instanceof HtmlTag))
                    retval = s.matches((HtmlTag)parent);
            }

            // set combinator for next loop
            combinator = s.getCombinator();

            // leave loop if not matched
            if (!retval)
                break;
        }

        // restore walker position
        w.setCurrentElement(current);
        return retval;
    }

    /**
     * Classifies the rules in all the style sheets.
     *
     * @param mediaspec The specification of the media for evaluating the media queries.
     */
    protected void classifyAllSheets(MediaSpec mediaspec) {
        rules = new Holder();
        for (StyleSheet sheet : sheets)
            classifyRules(sheet, mediaspec);
    }

    /**
     * Divides rules in sheet into different categories to be easily and more
     * quickly parsed afterward
     *
     * @param sheet     The style sheet to be classified
     * @param mediaspec The specification of the media for evaluating the media queries.
     */
    protected void classifyRules(StyleSheet sheet, MediaSpec mediaspec) {

        // create a new holder if it does not exist
        if (rules == null) {
            rules = new Holder();
        }

        for (Rule<?> rule : sheet) {
            // this rule conforms to all media
            if (rule instanceof RuleSet) {
                RuleSet ruleset = (RuleSet) rule;
                for (CombinedSelector s : ruleset.getSelectors()) {
                    insertClassified(rules, classifySelector(s), ruleset);
                }
            }
            // this rule conforms to different media
            else if (rule instanceof RuleMedia) {
                RuleMedia rulemedia = (RuleMedia) rule;

                boolean mediaValid = false;
                if (rulemedia.getMediaQueries() == null || rulemedia.getMediaQueries().isEmpty()) {
                    //no media queries actually
                    mediaValid = mediaspec.matchesEmpty();
                } else {
                    //find a matching query
                    for (MediaQuery media : rulemedia.getMediaQueries()) {
                        if (mediaspec.matches(media)) {
                            mediaValid = true;
                            break;
                        }
                    }
                }

                if (mediaValid) {
                    // for all rules in media set
                    for (RuleSet ruleset : rulemedia) {
                        // for all selectors in there
                        for (CombinedSelector s : ruleset.getSelectors()) {
                            insertClassified(rules, classifySelector(s), ruleset);
                        }
                    }
                }
            }
        }

        // logging
        if (log.isDebugEnabled()) {
            log.debug("For media \"{}\" we have {} rules", mediaspec, rules.contentCount());
            if (log.isTraceEnabled()) {
                log.trace("Detailed view: \n{}", rules);
            }
        }

    }

    /**
     * Classify CSS rule according its selector for to be of specified item(s)
     *
     * @param selector CombinedSelector of rules
     * @return List of HolderSelectors to which selectors conforms
     */
    private List<HolderSelector> classifySelector(CombinedSelector selector) {

        List<HolderSelector> hs = new ArrayList<HolderSelector>();

        try {
            // last simple selector decided about all selector
            Selector last = selector.getLastSelector();

            // is element or other (wildcard)
            String element = last.getElementName();
            if (element != null) {
                // wildcard
                if (Selector.ElementName.WILDCARD.equals(element))
                    hs.add(new HolderSelector(HolderItem.OTHER, null));
                    // element
                else
                    hs.add(new HolderSelector(HolderItem.ELEMENT, element
                            .toLowerCase()));
            }

            // is class name
            String className = last.getClassName();
            if (className != null)
                hs.add(new HolderSelector(HolderItem.CLASS, className
                        .toLowerCase()));

            // is id
            String id = last.getIDName();
            if (id != null)
                hs.add(new HolderSelector(HolderItem.ID, id.toLowerCase()));

            // is in others
            if (hs.size() == 0)
                hs.add(new HolderSelector(HolderItem.OTHER, null));

            return hs;

        } catch (UnsupportedOperationException e) {
            log
                    .error("CombinedSelector does not include any selector, this should not happen!");
            return Collections.emptyList();
        }
    }

    /**
     * Inserts rules into holder
     *
     * @param holder Wrap to be inserted
     * @param hs     Wrap's selector and key
     * @param value  Value to be inserted
     */
    private void insertClassified(Holder holder, List<HolderSelector> hs,
                                  RuleSet value) {
        for (HolderSelector h : hs)
            holder.insert(h.item, h.key, value);
    }

    /**
     * Decides about holder item
     *
     * @author kapy
     */
    protected enum HolderItem {
        ELEMENT(0), ID(1), CLASS(2), OTHER(3);

        private int type;

        private HolderItem(int type) {
            this.type = type;
        }

        public int type() {
            return type;
        }
    }

    /**
     * Holds holder item type and key value, that is two elements that are about
     * to be used for storing in holder
     *
     * @author kapy
     */
    protected class HolderSelector {
        public HolderItem item;
        public String key;

        public HolderSelector(HolderItem item, String key) {
            this.item = item;
            this.key = key;
        }
    }

    /**
     * Holds list of maps of list. This is used to classify rulesets into
     * structure which is easily accessible by analyzator
     *
     * @author kapy
     */
    protected static class Holder {

        /**
         * HolderItem.* except OTHER are stored there
         */
        private List<Map<String, List<RuleSet>>> items;

        /**
         * OTHER rules are stored there
         */
        private List<RuleSet> others;

        public Holder() {
            // create list of items
            this.items = new ArrayList<Map<String, List<RuleSet>>>(HolderItem
                    .values().length - 1);

            // fill maps in list
            for (HolderItem hi : HolderItem.values()) {
                // this is special case, it is not map
                if (hi == HolderItem.OTHER)
                    others = new ArrayList<RuleSet>();
                else
                    items.add(new HashMap<String, List<RuleSet>>());
            }
        }

        public boolean isEmpty() {
            for (HolderItem hi : HolderItem.values()) {
                if (hi == HolderItem.OTHER) {
                    if (!others.isEmpty()) return false;
                } else if (!items.get(hi.type).isEmpty())
                    return false;
            }
            return true;
        }


        public static Holder union(Holder one, Holder two) {

            Holder union = new Holder();
            if (one == null) one = new Holder();
            if (two == null) two = new Holder();

            for (HolderItem hi : HolderItem.values()) {
                if (hi == HolderItem.OTHER) {
                    union.others.addAll(one.others);
                    union.others.addAll(two.others);
                } else {

                    Map<String, List<RuleSet>> oneMap, twoMap, unionMap;
                    oneMap = one.items.get(hi.type);
                    twoMap = two.items.get(hi.type);
                    unionMap = union.items.get(hi.type);

                    unionMap.putAll(oneMap);
                    for (String key : twoMap.keySet()) {
                        // map already contains this as key, append to list
                        if (unionMap.containsKey(key)) {
                            unionMap.get(key).addAll(twoMap.get(key));
                        }
                        // we could directly add elements
                        else {
                            unionMap.put(key, twoMap.get(key));
                        }
                    }
                }

            }
            return union;
        }

        /**
         * Inserts Ruleset into group identified by HolderType, and optionally
         * by key value
         *
         * @param item  Identifier of holder's group
         * @param key   Key, used in case other than OTHER
         * @param value Value to be store inside
         */
        public void insert(HolderItem item, String key, RuleSet value) {

            // check others and if so, insert item
            if (item == HolderItem.OTHER) {
                others.add(value);
                return;
            }

            // create list if empty
            Map<String, List<RuleSet>> map = items.get(item.type);
            List<RuleSet> list = map.get(key);
            if (list == null) {
                list = new ArrayList<RuleSet>();
                map.put(key, list);
            }

            list.add(value);

        }

        /**
         * Returns list of rules (ruleset) for given holder and key
         *
         * @param item Type of item to be returned
         * @param key  Key or <code>null</code> in case of HolderItem.OTHER
         * @return List of rules or <code>null</code> if not found under given
         * combination of key and item
         */
        public List<RuleSet> get(HolderItem item, String key) {

            // check others
            if (item == HolderItem.OTHER)
                return others;

            return items.get(item.type()).get(key);
        }


        public String contentCount() {
            StringBuilder sb = new StringBuilder();

            for (HolderItem hi : HolderItem.values()) {
                if (hi == HolderItem.OTHER) {
                    sb.append(hi.name())
                            .append(": ")
                            .append(others.size())
                            .append(" ");
                } else {
                    sb.append(hi.name())
                            .append(":")
                            .append(items.get(hi.type).size())
                            .append(" ");
                }

            }

            return sb.toString();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (HolderItem hi : HolderItem.values()) {
                if (hi == HolderItem.OTHER) {
                    sb.append(hi.name())
                            .append(" (")
                            .append(others.size())
                            .append("): ")
                            .append(others).append("\n");
                } else {
                    sb.append(hi.name())
                            .append(" (")
                            .append(items.get(hi.type).size())
                            .append("): ")
                            .append(items.get(hi.type)).append("\n");
                }

            }

            return sb.toString();
        }
    }
}
