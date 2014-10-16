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
package org.cordovastudio.editors.designer.model;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.actions.ToggleSizeAction;
import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.decorators.EmptyComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.selection.NonResizeSelectionDecorator;

import java.util.List;

/**
 * @author Alexander Lobas
 */
public class RadViewLayout extends RadLayout {
  public static final RadLayout INSTANCE = new RadViewLayout();
  public static final ComponentDecorator NON_RESIZE_DECORATOR = new NonResizeSelectionDecorator(DrawingStyle.SELECTION);

  @Override
  public ComponentDecorator getChildSelectionDecorator(RadComponent component, List<RadComponent> selection) {
    if (component.isBackground()) {
      return EmptyComponentDecorator.INSTANCE;
    }
    return NON_RESIZE_DECORATOR;
  }

  /**
   * Adds in any applicable layout actions for this layout.
   *
   * @param designer the associated designer
   * @param actionGroup the action group to add the actions into
   * @param selection the selection of children in the layout (which may or may not be empty)
   */
  public void addContainerSelectionActions(CordovaDesignerEditorPanel designer,
                                           DefaultActionGroup actionGroup,
                                           List<? extends RadViewComponent> selection) {
    if (!selection.isEmpty()) {
      addFillActions(designer, actionGroup, selection);
    }
  }

  static void addFillActions(CordovaDesignerEditorPanel designer,
                                     DefaultActionGroup actionGroup,
                                     List<? extends RadViewComponent> selection) {
    actionGroup.add(new ToggleSizeAction(designer, selection, "Toggle Width", "layout_width", CordovaIcons.Designer.FillWidth, CordovaIcons.Designer.WrapWidth));
    actionGroup.add(new ToggleSizeAction(designer, selection, "Toggle Height", "layout_height", CordovaIcons.Designer.FillHeight, CordovaIcons.Designer.WrapHeight));
  }

  public void wrapIn(RadViewComponent newParent, List<RadViewComponent> components) throws Exception {
  }
}
