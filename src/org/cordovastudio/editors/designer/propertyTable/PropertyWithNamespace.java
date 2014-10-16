/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.designer.propertyTable;

import com.intellij.designer.model.Property;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.dom.AttributeDefinition;
import org.cordovastudio.dom.StyleableDefinition;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.cordovastudio.GlobalConstants.*;

public abstract class PropertyWithNamespace extends Property<RadViewComponent> {
  public PropertyWithNamespace(@Nullable Property parent, @NotNull String name) {
    super(parent, name);
  }

  protected abstract String getAttributeName();

  protected String getNamespace(RadViewComponent component, boolean createNamespaceIfNecessary) {
      return CORDOVASTUDIO_URI;
  }
}
