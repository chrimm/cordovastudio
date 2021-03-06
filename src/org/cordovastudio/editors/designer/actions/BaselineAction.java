/*
 * Copyright (C) 2013 The Android Open Source Project
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
package org.cordovastudio.editors.designer.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.NotNull;

import static org.cordovastudio.GlobalConstants.*;

public class BaselineAction extends LayoutAction {
  private final RadViewComponent myLayout;
  private boolean myAlign;

  public BaselineAction(@NotNull CordovaDesignerEditorPanel designer, @NotNull RadViewComponent layout) {
    super(designer, "Toggle baselineAligned", null, null);
    myAlign = isBaselineAligned(layout);
    myLayout = layout;
    Presentation presentation = getTemplatePresentation();
    updatePresentation(presentation);
  }

  private static boolean isBaselineAligned(RadViewComponent component) {
    XmlTag tag = component.getTag();
    XmlAttribute attribute = tag.getAttribute(ATTR_BASELINE_ALIGNED, CORDOVASTUDIO_URI);
    if (attribute != null) {
      String value = attribute.getValue();
      return Boolean.valueOf(value);
    } else {
      return true;
    }
  }

  private void updatePresentation(Presentation presentation) {
    presentation.setIcon(myAlign ? CordovaIcons.Designer.Baseline : CordovaIcons.Designer.NoBaseline);
    presentation.setDescription(myAlign ? "Align with the baseline" : "Do not align with the baseline");
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    myAlign = !myAlign;
    updatePresentation(e.getPresentation());
    super.actionPerformed(e);
  }

  @Override
  protected void performWriteAction() {
    XmlTag tag = myLayout.getTag();
    if (myAlign) {
      // TODO: Also set index?
      XmlAttribute attribute = tag.getAttribute(ATTR_BASELINE_ALIGNED, CORDOVASTUDIO_URI);
      if (attribute != null) {
        attribute.delete();
      }
    } else {
      tag.setAttribute(ATTR_BASELINE_ALIGNED, CORDOVASTUDIO_URI, VALUE_FALSE);
    }
  }
}
