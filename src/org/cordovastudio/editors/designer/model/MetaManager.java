/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.designer.model.MetaManager)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Added support for models that are distinguished by class and/or type instead of tag-only
 *  – Added support for multiple meta model definition files
 *  – Added support for meta model definition files lacking palette definitions
 *  – Added support for global properties in meta model definition files
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package org.cordovastudio.editors.designer.model;

import com.intellij.designer.model.Property;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.cordovastudio.dom.AttributeDefinition;
import org.cordovastudio.dom.AttributeFormat;
import org.cordovastudio.editors.designer.palette.DefaultPaletteItem;
import org.cordovastudio.editors.designer.palette.PaletteGroup;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.cordovastudio.editors.designer.palette.VariationPaletteItem;
import org.cordovastudio.editors.designer.propertyTable.properties.AttributeProperty;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Alexander Lobas
 */
public abstract class MetaManager extends ModelLoader {
    protected static final String META = "meta";
    protected static final String PALETTE = "palette";
    protected static final String GROUP = "group";
    protected static final String NAME = "name";
    protected static final String DISPLAY_NAME = "displayName";
    protected static final String ITEM = "item";
    protected static final String TAG = "tag";
    protected static final String CLASS = "class";
    protected static final String TYPE = "type";
    protected static final String WRAP_IN = "wrap-in";
    protected static final String PROPERTY = "property";
    protected static final String PROPERTIES = "properties";
    protected static final String GLOBAL_PROPERTIES = "global-properties";
    protected static final String INPLACE = "inplace";
    protected static final String TOP = "top";
    protected static final String IMPORTANT = "important";
    protected static final String NORMAL = "normal";
    protected static final String EXPERT = "expert";
    protected static final String DEPRECATED = "deprecated";

    protected static final String MORPHING = "morphing";
    protected static final String SHOW_IN_COMPONENT_TREE = "showInComponentTree";
    protected static final String OBSOLETE = "obsolete";
    protected static final String LAYOUT = "layout";
    protected static final String DELETE = "delete";
    protected static final String PRESENTATION = "presentation";
    protected static final String TITLE = "title";
    protected static final String ICON = "icon";
    protected static final String CREATION = "creation";

    private final TagModelMap myTag2Model = new TagModelMap();
    private final List<PaletteGroup> myPaletteGroups = new ArrayList<>();
    private final List<MetaModel> myWrapModels = new ArrayList<>();
    private final List<Property> myGlobalTopProperties = new ArrayList<>();
    private final List<Property> myGlobalImportantProperties = new ArrayList<>();
    private final List<Property> myGlobalNormalProperties = new ArrayList<>();
    private final List<Property> myGlobalExpertProperties = new ArrayList<>();
    private final List<Property> myGlobalDeprecatedProperties = new ArrayList<>();

    private Map<Object, Object> myCache = new HashMap<>();

    protected MetaManager(Project project) {
        super(project);
    }

    @Override
    protected void loadDocument(Element rootElement) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        Map<MetaModel, List<String>> modelToMorphing = new HashMap<>();

        /*
        Element globalPropertiesElement = rootElement.getChild(GLOBAL_PROPERTIES);
        if(globalPropertiesElement != null) {
            loadGlobalProperties(globalPropertiesElement);
        }
        */

        for (Element element : rootElement.getChildren(META)) {
            loadModel(classLoader, element, modelToMorphing);
        }

        Element paletteElement = rootElement.getChild(PALETTE);
        if(paletteElement != null) {
            for (Element element : rootElement.getChild(PALETTE).getChildren(GROUP)) {
                loadGroup(element);
            }
        }

        Element wrapInElement = rootElement.getChild(WRAP_IN);
        if (wrapInElement != null) {
            for (Element element : wrapInElement.getChildren(ITEM)) {
                myWrapModels.add(myTag2Model.get(element.getAttributeValue(TAG)));
            }
        }

        for (Map.Entry<MetaModel, List<String>> entry : modelToMorphing.entrySet()) {
            MetaModel meta = entry.getKey();
            List<MetaModel> morphingModels = new ArrayList<>();

            for (String tag : entry.getValue()) {
                MetaModel morphingModel = myTag2Model.get(tag);
                if (morphingModel != null) {
                    morphingModels.add(morphingModel);
                }
            }

            if (!morphingModels.isEmpty()) {
                meta.setMorphingModels(morphingModels);
            }
        }
    }

    /**
     * Loads global properties
     *
     * @param globalPropertiesElement
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    protected void loadGlobalProperties(Element globalPropertiesElement) {
        /*
         * Load global top properties
         */
        Element globalTopPropertiesElement = globalPropertiesElement.getChild(TOP);
        if(globalTopPropertiesElement != null) {
            for (Element element : globalTopPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                property.setImportant(true);

                myGlobalTopProperties.add(property);
            }
        }

        /*
         * Load global important properties
         */
        Element globalImportantPropertiesElement = globalPropertiesElement.getChild(IMPORTANT);
        if(globalImportantPropertiesElement != null) {
            for (Element element : globalImportantPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                property.setImportant(true);

                myGlobalImportantProperties.add(property);
            }
        }

        /*
         * Load global normal properties
         */
        Element globalNormalPropertiesElement = globalPropertiesElement.getChild(NORMAL);
        if(globalNormalPropertiesElement != null) {
            for (Element element : globalNormalPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                myGlobalNormalProperties.add(property);
            }
        }

        /*
         * Load  global expert properties
         */
        Element globalExpertPropertiesElement = globalPropertiesElement.getChild(EXPERT);
        if(globalExpertPropertiesElement != null) {
            for (Element element : globalExpertPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                property.setExpert(true);

                myGlobalExpertProperties.add(property);
            }
        }

        /*
         * Load global deprecated properties
         */
        Element globalDeprecatedPropertiesElement = globalPropertiesElement.getChild(DEPRECATED);
        if(globalDeprecatedPropertiesElement != null) {
            for (Element element : globalDeprecatedPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                property.setDeprecated(true);

                myGlobalDeprecatedProperties.add(property);
            }
        }
    }

    @NotNull
    @SuppressWarnings("unchecked")
    protected MetaModel loadModel(ClassLoader classLoader, Element element, Map<MetaModel, List<String>> modelToMorphing) throws Exception {
        String modelValue = element.getAttributeValue("model");
        Class<RadComponent> model = modelValue == null ? null : (Class<RadComponent>) classLoader.loadClass(modelValue);
        String htmlClass = element.getAttributeValue(CLASS);
        String htmlType = element.getAttributeValue(TYPE);
        String tag = element.getAttributeValue(TAG);

        MetaModel meta = createModel(model, tag, htmlClass, htmlType);

        String showInComponentTree = element.getAttributeValue(SHOW_IN_COMPONENT_TREE);
        if(showInComponentTree != null) {
            meta.setShownInComponentTree(Boolean.parseBoolean(showInComponentTree));
        }

        String deprecated = element.getAttributeValue(OBSOLETE);
        if(deprecated != null) {
            meta.setDeprecated(Boolean.parseBoolean(deprecated));
        }

        String layout = element.getAttributeValue(LAYOUT);
        if (layout != null) {
            meta.setLayout((Class<RadLayout>) classLoader.loadClass(layout));
        }

        String delete = element.getAttributeValue(DELETE);
        if (delete != null) {
            meta.setDelete(Boolean.parseBoolean(delete));
        }

        Element presentation = element.getChild(PRESENTATION);
        if (presentation != null) {
            meta.setPresentation(presentation.getAttributeValue(TITLE), presentation.getAttributeValue(ICON));
        }

        Element palette = element.getChild(PALETTE);
        if (palette != null) {
            meta.setPaletteItem(createPaletteItem(palette));
        }

        Element creation = element.getChild(CREATION);
        if (creation != null) {
            meta.setCreation(creation.getTextTrim());
        }

        Element properties = element.getChild(PROPERTIES);
        if (properties != null) {
            loadProperties(meta, properties);
        }

        Element morphing = element.getChild(MORPHING);
        if (morphing != null) {
            modelToMorphing.put(meta, StringUtil.split(morphing.getAttribute("to").getValue(), " "));
        }

        loadOther(meta, element);

        if (tag != null) {
            myTag2Model.put(new TagDescriptor(tag, htmlClass, htmlType), meta);
        }

        return meta;
    }

    @NotNull
    protected MetaModel createModel(Class<RadComponent> model, String tag, String htmlClass, String htmlType) throws Exception {
        return new MetaModel(model, tag, htmlClass, htmlType);
    }

    @NotNull
    protected DefaultPaletteItem createPaletteItem(Element palette) {
        return new DefaultPaletteItem(palette);
    }

    @NotNull
    protected VariationPaletteItem createVariationPaletteItem(PaletteItem paletteItem, MetaModel model, Element itemElement) {
        return new VariationPaletteItem(paletteItem, model, itemElement);
    }

    @NotNull
    protected PaletteGroup createPaletteGroup(String name) {
        return new PaletteGroup(name);
    }

    protected void loadProperties(MetaModel meta, Element propertiesElement) throws Exception {
        /*
         * Load global properties into lists
         */
        List<Property> inplaceProperties = new ArrayList<>();
        List<Property> topProperties = new ArrayList<>(myGlobalTopProperties);
        List<Property> importantProperties = new ArrayList<>(myGlobalImportantProperties);
        List<Property> normalProperties = new ArrayList<>(myGlobalNormalProperties);
        List<Property> expertProperties = new ArrayList<>(myGlobalExpertProperties);
        List<Property> deprecatedProperties = new ArrayList<>(myGlobalDeprecatedProperties);

        // TODO: Should there really be a full specification of inplace properties?
        // For example the "text" property is available for many HTML5 tags and is inplace AND important property.
        // So what about specifying just the important entry fully and let inplace be just a (space separated) list of
        // property names. For example:
        //
        //  <properties inplace="text text2">
        //      <important>
        //          <property name="text" displayName="Text" type="String/>
        //          <property name="text2" displayName="Text" type="String/>
        //      </important>
        //  </properties>
        //
        // instead of:
        //
        //  <properties>
        //      <inplace>
        //          <property name="text" displayName="Text" type="String/>
        //          <property name="text2" displayName="Text" type="String/>
        //      </inplace>
        //      <important>
        //          <property name="text" displayName="Text" type="String/>
        //          <property name="text2" displayName="Text" type="String/>
        //      </important>
        //  </properties>

        /*
         * Load inplace properties
         */
        Element inplacePropertiesElement = propertiesElement.getChild(INPLACE);
        if(inplacePropertiesElement != null) {
            for (Element element : inplacePropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                /* See comment below */
                if(inplaceProperties.contains(property)) {
                    inplaceProperties.set(inplaceProperties.indexOf(property), property);
                } else {
                    inplaceProperties.add(property);
                }
            }
        }

        /*
         * Load top properties
         */
        Element topPropertiesElement = propertiesElement.getChild(TOP);
        if(topPropertiesElement != null) {
            for (Element element : topPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                /*
                 * At first sight, this looks ridiculously. But hey, don't misjudge it so hast'ly!
                 * If you take closer look into implementation of List.contains() and List.indexOf() as well as
                 * Property.equals(), you'll see that contains() and indexOf() use the equals() Method of the property,
                 * which in turn will compare properties only by its name (and its parent).
                 * Thus the following implementation will find for example a Property "propName" having type A and
                 * replaces it with another one having the same name "propName", but not necessarily the same type A,
                 * so it could be overwritten with a property having type B.
                 *
                 * If this property already exists in any other property list, remove it there and add it here, thus it
                 * is possible to up- or downgrade the property's importance.
                 *
                 * (Same comment applies for every other property list, but will not be repeated there.)
                 */
                if(topProperties.contains(property)) {
                    topProperties.set(topProperties.indexOf(property), property);
                } else if (importantProperties.contains(property)) {
                    importantProperties.remove(property);
                } else if (normalProperties.contains(property)) {
                    normalProperties.remove(property);
                } else if (expertProperties.contains(property)) {
                    expertProperties.remove(property);
                } else if (deprecatedProperties.contains(property)) {
                    deprecatedProperties.remove(property);
                } else {
                    topProperties.add(property);
                }
            }
        }

        /*
         * Load important properties
         */
        Element importantPropertiesElement = propertiesElement.getChild(IMPORTANT);
        if(importantPropertiesElement != null) {
            for (Element element : importantPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                if(importantProperties.contains(property)) {
                    importantProperties.set(importantProperties.indexOf(property), property);
                } else if (topProperties.contains(property)) {
                    topProperties.remove(property);
                } else if (normalProperties.contains(property)) {
                    normalProperties.remove(property);
                } else if (expertProperties.contains(property)) {
                    expertProperties.remove(property);
                } else if (deprecatedProperties.contains(property)) {
                    deprecatedProperties.remove(property);
                } else {
                    importantProperties.add(property);
                }
            }
        }

        /*
         * Load normal properties
         */
        Element normalPropertiesElement = propertiesElement.getChild(NORMAL);
        if(normalPropertiesElement != null) {
            for (Element element : normalPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                if(normalProperties.contains(property)) {
                    normalProperties.set(normalProperties.indexOf(property), property);
                } else if (importantProperties.contains(property)) {
                    importantProperties.remove(property);
                } else if (topProperties.contains(property)) {
                    topProperties.remove(property);
                } else if (expertProperties.contains(property)) {
                    expertProperties.remove(property);
                } else if (deprecatedProperties.contains(property)) {
                    deprecatedProperties.remove(property);
                } else {
                    normalProperties.add(property);
                }
            }
        }

        /*
         * Load expert properties
         */
        Element expertPropertiesElement = propertiesElement.getChild(EXPERT);
        if(expertPropertiesElement != null) {
            for (Element element : expertPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                if(expertProperties.contains(property)) {
                    expertProperties.set(expertProperties.indexOf(property), property);
                } else if (importantProperties.contains(property)) {
                    importantProperties.remove(property);
                } else if (normalProperties.contains(property)) {
                    normalProperties.remove(property);
                } else if (topProperties.contains(property)) {
                    topProperties.remove(property);
                } else if (deprecatedProperties.contains(property)) {
                    deprecatedProperties.remove(property);
                } else {
                    expertProperties.add(property);
                }
            }
        }

        /*
         * Load deprecated properties
         */
        Element deprecatedPropertiesElement = propertiesElement.getChild(DEPRECATED);
        if(deprecatedPropertiesElement != null) {
            for (Element element : deprecatedPropertiesElement.getChildren(PROPERTY)) {
                String name = element.getAttributeValue(NAME);
                String displayName = element.getAttributeValue(DISPLAY_NAME);
                AttributeFormat type = AttributeFormat.valueOf(element.getAttributeValue(TYPE));
                AttributeProperty property = new AttributeProperty(displayName, new AttributeDefinition(name,
                        Collections.singletonList(type)));

                if(deprecatedProperties.contains(property)) {
                    deprecatedProperties.set(deprecatedProperties.indexOf(property), property);
                } else if (importantProperties.contains(property)) {
                    importantProperties.remove(property);
                } else if (normalProperties.contains(property)) {
                    normalProperties.remove(property);
                } else if (expertProperties.contains(property)) {
                    expertProperties.remove(property);
                } else if (topProperties.contains(property)) {
                    topProperties.remove(property);
                } else {
                    deprecatedProperties.add(property);
                }
            }
        }

        /*
         * Write lists into meta model
         */
        meta.setInplaceProperties(inplaceProperties);
        meta.setTopProperties(topProperties);
        meta.setImportantProperties(importantProperties);
        meta.setNormalProperties(normalProperties);
        meta.setExpertProperties(expertProperties);
        meta.setDeprecatedProperties(deprecatedProperties);
    }

    protected void loadOther(MetaModel meta, Element element) throws Exception {
    }

    @NotNull
    protected PaletteGroup loadGroup(Element element) throws Exception {
        PaletteGroup group = createPaletteGroup(element.getAttributeValue(NAME));

        for (Element itemElement : element.getChildren(ITEM)) {
            MetaModel model = getModelByTag(itemElement.getAttributeValue(TAG));

            if (model == null) {
                LOG.warn("Cannot create Palette Item for tag: '" + itemElement.getAttributeValue(TAG) + "' in Group '" + element.getAttributeValue(NAME) + "'");
                continue;
            }

            PaletteItem paletteItem = model.getPaletteItem();

            if (!itemElement.getChildren().isEmpty()) {
                // Replace the palette item shown in the palette; it might provide a custom
                // title, icon or creation logic (and this is done here rather than in the
                // default palette item, since when loading elements back from XML, there's
                // no variation matching. We don't want for example to call the default
                // LinearLayout item "LinearLayout (Horizontal)", since that item would be
                // shown in the component tree for any <LinearLayout> found in the XML, including
                // those which set orientation="vertical". In the future, consider generalizing
                // this such that the {@link MetaModel} can hold multiple {@link PaletteItem}
                // instances, and perform attribute matching.
                if (itemElement.getAttribute("title") != null) {
                    paletteItem = createVariationPaletteItem(paletteItem, model, itemElement);
                }
                group.addItem(paletteItem);

                for (Element grandChild : itemElement.getChildren(ITEM)) {
                    group.addItem(createVariationPaletteItem(paletteItem, model, grandChild));
                }
            } else {
                group.addItem(paletteItem);
            }
        }

        myPaletteGroups.add(group);

        return group;
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getCache(Object key) {
        return (Map<K, V>) myCache.get(key);
    }

    public void setCache(Object key, Object value) {
        myCache.put(key, value);
    }

    @Nullable
    public MetaModel getModelByTag(String tag) {
        return myTag2Model.get(tag);
    }

    @Nullable
    public MetaModel getModel(String tag, String htmlClass, String htmlType) {
        return myTag2Model.get(tag, htmlClass, htmlType);
    }

    public List<MetaModel> getWrapInModels() {
        return myWrapModels;
    }

    public List<PaletteGroup> getPaletteGroups() {
        return myPaletteGroups;
    }
}