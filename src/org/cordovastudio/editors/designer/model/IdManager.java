/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.model.IdManager)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova Studio (i.e. removed unneded functions and adjusted names)
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
package org.cordovastudio.editors.designer.model;

import com.intellij.lang.LanguageNamesValidation;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.utils.CordovaPsiUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.cordovastudio.GlobalConstants.*;
/**
 * @author Alexander Lobas
 */
public class IdManager {
  public static final String KEY = "IdManager";

  private final Set<String> myIdList = new HashSet<String>();

  @Nullable
  public static IdManager get(RadComponent component) {
    return RadModelBuilder.getIdManager(component);
  }

  public void addComponent(RadViewComponent component) {
    String idValue = component.getId();
    if (idValue != null) {
      myIdList.add(idValue);
    }
  }

  public void removeComponent(RadViewComponent component, boolean withChildren) {
    String idValue = component.getId();
    if (idValue != null) {
      myIdList.remove(idValue); // Uh oh. What if it appears more than once? This would incorrectly assume it's no longer there! Needs to be a list or have a count!
    }

    if (withChildren) {
      for (RadComponent child : component.getChildren()) {
        removeComponent((RadViewComponent)child, true);
      }
    }
  }

  public String createId(RadViewComponent component) {
    String idValue = StringUtil.decapitalize(component.getMetaModel().getTag());

    XmlTag tag = component.getTag();
    Module module = CordovaPsiUtils.getModuleSafely(tag);

    String nextIdValue = idValue;
    int index = 0;

    // Ensure that we don't create something like "switch" as an id, which won't compile when used
    // in the R class
    NamesValidator validator = LanguageNamesValidation.INSTANCE.forLanguage(JavaLanguage.INSTANCE);

    Project project = tag.getProject();
    while (myIdList.contains(nextIdValue) || validator != null && validator.isKeyword(nextIdValue, project)) {
      ++index;
      if (index == 1 && (validator == null || !validator.isKeyword(nextIdValue, project))) {
        nextIdValue = idValue;
      } else {
        nextIdValue = idValue + Integer.toString(index);
      }
    }

    myIdList.add(nextIdValue);
    String newId = idValue + (index == 0 ? "" : Integer.toString(index));
    tag.setAttribute(ATTR_ID, CORDOVASTUDIO_URI, newId);
    return newId;
  }
}