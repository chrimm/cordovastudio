/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.designer.model.MetaModel)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Added support for models that are distinguished by class and/or type instead of tag-only
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
package org.cordovastudio.editors.designer.model;

import com.intellij.designer.model.Property;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ArrayUtil;
import org.cordovastudio.editors.designer.palette.DefaultPaletteItem;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.cordovastudio.editors.designer.propertyTable.IPropertyDecorator;
import org.cordovastudio.editors.designer.propertyTable.properties.AttributeProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * @author Alexander Lobas
 */
public class MetaModel {
    private final Class<RadComponent> myModel;
    private Class<RadLayout> myLayout;
    private final String myHtmlClass;
    private final String myHtmlType;
    private final String myTag;
    private PaletteItem myPaletteItem;
    private String myTitle;
    private String myIconPath;
    private Icon myIcon;
    private String myCreation;
    private boolean myDelete = true;
    private boolean myShowInComponentTree = true;
    private boolean myIsDeprecated = false;
    private Map<String, Property> myInplaceProperties = Collections.emptyMap();
    private Map<String, Property> myTopProperties = Collections.emptyMap();
    private Map<String, Property> myNormalProperties = Collections.emptyMap();
    private Map<String, Property> myImportantProperties = Collections.emptyMap();
    private Map<String, Property> myExpertProperties = Collections.emptyMap();
    private Map<String, Property> myDeprecatedProperties = Collections.emptyMap();
    private List<MetaModel> myMorphingModels = Collections.emptyList();

    public MetaModel(Class<RadComponent> model, @NotNull String tag, @Nullable String htmlClass, @Nullable String htmlType) {
        myModel = model;
        myTag = tag;
        myHtmlClass = htmlClass;
        myHtmlType = htmlType;
    }

    public Class<RadComponent> getModel() {
        return myModel;
    }

    public Class<RadLayout> getLayout() {
        return myLayout;
    }

    public void setLayout(Class<RadLayout> layout) {
        myLayout = layout;
    }

    /**
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Nullable
    public String getHtmlClass() {
        return myHtmlClass;
    }

    /**
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Nullable
    public String geHtmlType() {
        return myHtmlType;
    }

    public String getTag() {
        return myTag;
    }

    public boolean isTag(@NotNull String tag) {
        return tag.equals(myTag);
    }

    public boolean isTag(String... tags) {
        return ArrayUtil.contains(myTag, tags);
    }

    public String getCreation() {
        return myCreation;
    }

    public void setCreation(String creation) {
        myCreation = creation;
    }

    public boolean canDelete() {
        return myDelete;
    }

    public void setDelete(boolean delete) {
        myDelete = delete;
    }

    public String getTitle() {
        return myTitle;
    }

    public Icon getIcon() {
        if (myIcon == null) {
            if (myIconPath == null) {
                return myPaletteItem == null ? null : myPaletteItem.getIcon();
            }
            myIcon = IconLoader.findIcon(myIconPath, myModel);
        }
        return myIcon;
    }

    public void setPresentation(String title, String iconPath) {
        myTitle = title;
        myIconPath = iconPath;
        myIcon = null;
    }

    public PaletteItem getPaletteItem() {
        return myPaletteItem;
    }

    public void setPaletteItem(@NotNull DefaultPaletteItem paletteItem) {
        myPaletteItem = paletteItem;
        myPaletteItem.setMetaModel(this);
    }

    public boolean isShownInComponentTree() {
        return myShowInComponentTree;
    }

    public void setShownInComponentTree(boolean showInComponentTree) {
        this.myShowInComponentTree = showInComponentTree;
    }

    public boolean isDeprecated() {
        return myIsDeprecated;
    }

    public void setDeprecated(boolean isDeprecated) {
        this.myIsDeprecated = isDeprecated;
    }

    public List<MetaModel> getMorphingModels() {
        return myMorphingModels;
    }

    public void setMorphingModels(List<MetaModel> morphingModels) {
        myMorphingModels = morphingModels;
    }

    public List<Property> getInplaceProperties() {
        return new ArrayList<>(myInplaceProperties.values());
    }

    public void setInplaceProperties(String[] inplacePropertyNames) {
        List<String> inplacePropertyNamesList = Arrays.asList(inplacePropertyNames);
        List<Property> properties = getAllProperties();
        List<Property> inplaceProperties = new ArrayList<>(inplacePropertyNames.length);

        for(Property property : properties) {
            if(inplacePropertyNamesList.contains(((AttributeProperty) property).getAttributeName())) {

                inplaceProperties.add(property);
            }
        }

        setInplaceProperties(inplaceProperties);
    }

    public void setInplaceProperties(List<Property> inplaceProperties) {
        myInplaceProperties = new HashMap<>(inplaceProperties.size());
        for (Property property : inplaceProperties) {
            myInplaceProperties.put(property.getName(), property);
        }
    }

    public List<Property> getTopProperties() {
        return new ArrayList<>(myTopProperties.values());
    }

    public void setTopProperties(List<Property> topProperties) {
        myTopProperties = new HashMap<>(topProperties.size());
        for (Property property : topProperties) {
            myTopProperties.put(property.getName(), property);
        }
    }

    public List<Property> getNormalProperties() {
        return new ArrayList<>(myNormalProperties.values());
    }

    public void setNormalProperties(List<Property> normalProperties) {
        myNormalProperties = new HashMap<>(normalProperties.size());
        for (Property property : normalProperties) {
            myNormalProperties.put(property.getName(), property);
        }
    }

    public List<Property> getImportantProperties() {
        return new ArrayList<>(myImportantProperties.values());
    }

    public boolean isImportantProperty(String name) {
        return myImportantProperties.containsKey(name);
    }

    public void setImportantProperties(List<Property> importantProperties) {
        myImportantProperties = new HashMap<>(importantProperties.size());
        for (Property property : importantProperties) {
            myImportantProperties.put(property.getName(), property);
        }
    }

    public List<Property> getExpertProperties() {
        return new ArrayList<>(myExpertProperties.values());
    }

    public boolean isExpertProperty(String name) {
        return myExpertProperties.containsKey(name);
    }

    public void setExpertProperties(List<Property> expertProperties) {
        myExpertProperties = new HashMap<>(expertProperties.size());
        for (Property property : expertProperties) {
            myExpertProperties.put(property.getName(), property);
        }
    }

    public List<Property> getDeprecatedProperties() {
        return new ArrayList<>(myDeprecatedProperties.values());
    }

    public boolean isDeprecatedProperty(String name) {
        return myDeprecatedProperties.containsKey(name);
    }

    public void setDeprecatedProperties(List<Property> deprecatedProperties) {
        myDeprecatedProperties = new HashMap<>(deprecatedProperties.size());
        for (Property property : deprecatedProperties) {
            myDeprecatedProperties.put(property.getName(), property);
        }
    }

    public List<Property> getAllProperties() {
        return getAllProperties(true);
    }

    public List<Property> getAllProperties(boolean includeDeprecatedProperties) {
        List<Property> allProperties = new ArrayList<>(
                myTopProperties.size()
                        + myImportantProperties.size()
                        + myNormalProperties.size()
                        + myExpertProperties.size()
                        + myDeprecatedProperties.size()
        );

        allProperties.addAll(myTopProperties.values());
        allProperties.addAll(myImportantProperties.values());
        allProperties.addAll(myNormalProperties.values());
        allProperties.addAll(myExpertProperties.values());

        if(includeDeprecatedProperties)
            allProperties.addAll(myDeprecatedProperties.values());

        return allProperties;
    }

    public void decorate0(Property property, String name) {
        property.setImportant(isImportantProperty(name));
        property.setExpert(isExpertProperty(name));
        property.setDeprecated(isDeprecatedProperty(name));
    }

    public void decorate(Property property, String name) {
        decorate0(property, name);

        if (property instanceof IPropertyDecorator) {
            ((IPropertyDecorator) property).decorate(this);
        }
    }

    public Property decorateWithOverride(Property property) {
        String name = property.getName();

        if (myNormalProperties.containsKey(name) ||
                myImportantProperties.containsKey(name) ||
                myExpertProperties.containsKey(name) ||
                myDeprecatedProperties.containsKey(name)) {
            property = property.createForNewPresentation();
            decorate(property, name);
        }

        return property;
    }
}