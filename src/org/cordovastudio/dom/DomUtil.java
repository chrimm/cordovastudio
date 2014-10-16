/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Removed everything not needed for Cordova Studio
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

package org.cordovastudio.dom;

import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.HashMap;
import com.intellij.util.xml.*;
import org.cordovastudio.editors.designer.model.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.cordovastudio.GlobalConstants.*;

/**
 * @author Eugene.Kudelevsky
 */
@SuppressWarnings({"EnumSwitchStatementWhichMissesCases"})
public class DomUtil {

  public static final Map<String, String> SPECIAL_RESOURCE_TYPES = Maps.newHashMapWithExpectedSize(20);

  static {
    // This section adds additional resource type registrations where the attrs metadata is lacking. For
    // example, attrs_manifest.xml tells us that the android:icon attribute can be a reference, but not
    // that it's a reference to a drawable.
    addSpecialResourceType(ResourceType.STRING.getName(), ATTR_LABEL, "description", ATTR_TITLE);
    addSpecialResourceType(ResourceType.DRAWABLE.getName(), ATTR_ICON);
    addSpecialResourceType(ResourceType.STYLE.getName(), ATTR_THEME);
    addSpecialResourceType(ResourceType.ANIM.getName(), "animation");
    addSpecialResourceType(ResourceType.ID.getName(), ATTR_ID, ATTR_LAYOUT_TO_RIGHT_OF, ATTR_LAYOUT_TO_LEFT_OF, ATTR_LAYOUT_ABOVE,
                           ATTR_LAYOUT_BELOW, ATTR_LAYOUT_ALIGN_BASELINE, ATTR_LAYOUT_ALIGN_LEFT, ATTR_LAYOUT_ALIGN_TOP,
                           ATTR_LAYOUT_ALIGN_RIGHT, ATTR_LAYOUT_ALIGN_BOTTOM, ATTR_LAYOUT_ALIGN_START, ATTR_LAYOUT_ALIGN_END,
                           ATTR_LAYOUT_TO_START_OF, ATTR_LAYOUT_TO_END_OF);
  }

  private DomUtil() {
  }

  // for special cases
  static void addSpecialResourceType(String type, String... attrs) {
    for (String attr : attrs) {
      SPECIAL_RESOURCE_TYPES.put(attr, type);
    }
  }
}
