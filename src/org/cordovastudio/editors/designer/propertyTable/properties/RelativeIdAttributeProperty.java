/*
 * Copyright 2000-2012 JetBrains s.r.o.
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

import com.intellij.designer.model.PropertiesContainer;
import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.designer.propertyTable.PropertyRenderer;
import org.cordovastudio.dom.AttributeDefinition;
import org.cordovastudio.dom.AttributeFormat;
import org.cordovastudio.editors.designer.designSurface.operations.RelativeLayoutDropOperation;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadRelativeLayoutComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.editors.designer.propertyTable.editors.ComponentEditor;
import org.cordovastudio.editors.designer.propertyTable.renderers.ComponentRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Lobas
 */
public class RelativeIdAttributeProperty extends AttributeProperty {
  public RelativeIdAttributeProperty(@NotNull AttributeProperty property) {
    super(property.getParent(), property.getName(), property.myDefinition);
  }

  private static ComponentRenderer createRenderer() {
    return new ComponentRenderer() {
      @Override
      @Nullable
      public RadComponent getComponentById(RadComponent component, String value) {
        String componentId = RadRelativeLayoutComponent.parseIdValue(value);
        if (componentId != null) {
          for (RadComponent childComponent : component.getParent().getChildren()) {
            String childComponentId = ((RadViewComponent)childComponent).getId();
            if (childComponentId != null && componentId.equals(RadRelativeLayoutComponent.parseIdValue(childComponentId))) {
              return childComponent;
            }
          }
        }
        return null;
      }
    };
  }

  @Override
  protected PropertyRenderer createResourceRenderer(AttributeDefinition definition, Set<AttributeFormat> formats) {
    return createRenderer();
  }

  @Override
  protected PropertyEditor createResourceEditor(final AttributeDefinition definition, Set<AttributeFormat> formats) {
    return new ComponentEditor(createRenderer()) {
      @Override
      protected List<RadComponent> getComponents(RadComponent component) {
        RadComponent layout = component.getParent();
        List<RadViewComponent> views = Arrays.asList((RadViewComponent)component);
        return RelativeLayoutDropOperation.getValidTargets(definition.getName(), (RadViewComponent) layout, views);
      }
    };
  }

  @Override
  public boolean availableFor(List<PropertiesContainer> components) {
    return false;
  }
}