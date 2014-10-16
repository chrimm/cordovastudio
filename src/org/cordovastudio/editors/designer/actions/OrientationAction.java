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
package org.cordovastudio.editors.designer.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.xml.XmlAttribute;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.cordovastudio.GlobalConstants.*;
/**
 * @author Alexander Lobas
 */
public class OrientationAction extends LayoutAction {
  private final RadViewComponent myLayout;
  /** The current orientation; we toggle to the opposite */
  private boolean myHorizontal;
  /** The default orientation for this layout */
  private final boolean myDefaultHorizontal;

  public OrientationAction(@NotNull CordovaDesignerEditorPanel designer,
                           @NotNull RadViewComponent layout,
                           boolean defaultHorizontal) {
    super(designer, "Change attribute 'orientation'", null, CordovaIcons.Designer.SwitchHorizontalLinear);
    myLayout = layout;
    myDefaultHorizontal = defaultHorizontal;
    update(getTemplatePresentation());

    myHorizontal = !VALUE_VERTICAL.equals(layout.getTag().getAttributeValue(ATTR_ORIENTATION, CORDOVASTUDIO_URI));
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    myHorizontal = !myHorizontal;
    update(e.getPresentation());
    super.actionPerformed(e);
  }

  @Override
  protected void performWriteAction() {
    String value = myHorizontal ? VALUE_HORIZONTAL : VALUE_VERTICAL;
    if (myHorizontal == myDefaultHorizontal) {
      XmlAttribute attribute = myLayout.getTag().getAttribute(ATTR_ORIENTATION, CORDOVASTUDIO_URI);
      if (attribute != null) {
        attribute.delete();
      }
    } else {
      myLayout.getTag().setAttribute(ATTR_ORIENTATION, CORDOVASTUDIO_URI, value);
    }
  }

  protected void update(Presentation presentation) {
    presentation.setDescription("Convert orientation to " + (myHorizontal ? VALUE_VERTICAL : VALUE_HORIZONTAL));
    Icon icon = myHorizontal ? CordovaIcons.Designer.SwitchHorizontalLinear : CordovaIcons.Designer.SwitchVerticalLinear;
    presentation.setIcon(icon);
    presentation.setHoveredIcon(icon);
  }
}
