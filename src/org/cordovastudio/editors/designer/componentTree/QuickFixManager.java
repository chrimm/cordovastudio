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
package org.cordovastudio.editors.designer.componentTree;

import com.intellij.designer.model.ErrorInfo;
import com.intellij.icons.AllIcons;
import org.cordovastudio.editors.designer.designSurface.ComponentSelectionListener;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.inspection.AbstractQuickFixManager;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public final class QuickFixManager extends AbstractQuickFixManager implements ComponentSelectionListener {
  private IEditableArea myArea;

  public QuickFixManager(JComponent component, JViewport viewPort) {
    super(null, component, viewPort);
  }

  public void setEditableArea(IEditableArea area) {
    myArea = area;
    area.addSelectionListener(this);
  }

  @Override
  public void selectionChanged(IEditableArea area) {
    hideHint();
    updateHintVisibility();
  }

  @NotNull
  @Override
  protected List<ErrorInfo> getErrorInfos() {
    List<RadComponent> selection = myArea.getSelection();

    if (selection.size() == 1) {
      return RadComponent.getError(selection.get(0));
    }

    return Collections.emptyList();
  }

  @Override
  protected Rectangle getErrorBounds() {
    ComponentTree component = (ComponentTree)myComponent;
    Rectangle bounds = component.getPathBounds(component.getSelectionPath());
    if (bounds != null) {
      bounds.x += AllIcons.Actions.IntentionBulb.getIconWidth();
    }
    return bounds;
  }
}
