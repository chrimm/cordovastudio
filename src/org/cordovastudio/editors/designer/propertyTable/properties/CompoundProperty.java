/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.propertyTable.CompoundProperty and com.intellij.android.designer.model.layout.relative.CompoundProperty)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.android.designer.propertyTable.CompoundProperty
 *    and com.intellij.android.designer.model.layout.relative.CompoundProperty
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
 */
package org.cordovastudio.editors.designer.propertyTable.properties;

import com.intellij.designer.model.Property;
import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.designer.propertyTable.PropertyRenderer;
import com.intellij.designer.propertyTable.renderers.LabelPropertyRenderer;
import com.intellij.psi.xml.XmlAttribute;
import org.cordovastudio.editors.designer.model.MetaModel;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.editors.designer.propertyTable.IPropertyDecorator;
import org.cordovastudio.editors.designer.propertyTable.IXmlAttributeLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public class CompoundProperty extends Property<RadViewComponent> implements IPropertyDecorator, IXmlAttributeLocator {
    private final String myJavadocText;

    private final List<Property<RadViewComponent>> myChildren = new ArrayList<Property<RadViewComponent>>();
    private PropertyRenderer myRenderer;

    public CompoundProperty(@NotNull String name) {
        this(name, null);
    }

    public CompoundProperty(@NotNull String name, String javadocText) {
        super(null, name);
        myJavadocText = javadocText;
        setImportant(true);
    }

    @Override
    public Property<RadViewComponent> createForNewPresentation(@Nullable Property parent, @NotNull String name) {
        CompoundProperty property = createForNewPresentation(name);
        List<Property<RadViewComponent>> children = property.getChildren(null);
        for (Property<RadViewComponent> childProperty : myChildren) {
            children.add(childProperty.createForNewPresentation(property, childProperty.getName()));
        }
        return property;
    }

    @Override
    public void decorate(@NotNull MetaModel model) {
        String name = getName();
        model.decorate0(this, name);
        for (Property<RadViewComponent> childProperty : myChildren) {
            model.decorate(childProperty, name + "." + childProperty.getName());
        }
    }

    @NotNull
    @Override
    public List<Property<RadViewComponent>> getChildren(@Nullable RadViewComponent component) {
        return myChildren;
    }

    protected CompoundProperty createForNewPresentation(@NotNull String name) {
        return new CompoundProperty(name, null);
    }

    @Override
    public void setDefaultValue(@NotNull RadViewComponent component) throws Exception {
        for (Property<RadViewComponent> childProperty : myChildren) {
            childProperty.setDefaultValue(component);
        }
    }

    @NotNull
    @Override
    public PropertyRenderer getRenderer() {
        if (myRenderer == null) {
            myRenderer = new LabelPropertyRenderer(null);
        }
        return myRenderer;
    }

    @Override
    public PropertyEditor getEditor() {
        return null;
    }

    @Override
    public boolean checkAttribute(RadViewComponent component, XmlAttribute attribute) {
        for (Property<RadViewComponent> childProperty : myChildren) {
            if (((IXmlAttributeLocator) childProperty).checkAttribute(component, attribute)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getValue(@NotNull RadViewComponent component) throws Exception {
        StringBuilder value = new StringBuilder("[");
        int index = 0;
        for (Property<RadViewComponent> child : getChildren(component)) {
            if (!child.isDefaultValue(component)) {
                if (index++ > 0) {
                    value.append(", ");
                }
                value.append(child.getName());
            }
        }
        return value.append("]").toString();
    }

    @Override
    public String getJavadocText() {
        return myJavadocText;
    }
}