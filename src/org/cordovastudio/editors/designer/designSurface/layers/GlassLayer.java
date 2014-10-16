/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package org.cordovastudio.editors.designer.designSurface.layers;

import org.cordovastudio.editors.designer.componentTree.TreeDropListener;
import org.cordovastudio.editors.designer.designSurface.tools.ToolProvider;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import com.intellij.openapi.actionSystem.DataProvider;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Alexander Lobas
 */
public final class GlassLayer extends JComponent implements DataProvider {
  private static final long EVENT_FLAGS = AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK;

  private final ToolProvider myToolProvider;
  private final IEditableArea myArea;

  public GlassLayer(ToolProvider provider, IEditableArea area) {
    myToolProvider = provider;
    myArea = area;
    enableEvents(EVENT_FLAGS);
    new TreeDropListener(this, area, provider, PaletteItem.class);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    if (enabled) {
      enableEvents(EVENT_FLAGS);
    }
    else {
      disableEvents(EVENT_FLAGS);
    }
  }

  @Override
  protected void processKeyEvent(KeyEvent event) {
    myToolProvider.processKeyEvent(event, myArea);

    if (!event.isConsumed()) {
      super.processKeyEvent(event);
    }
  }

  @Override
  protected void processMouseEvent(MouseEvent event) {
    if (event.getID() == MouseEvent.MOUSE_PRESSED) {
      requestFocusInWindow();
    }

    myToolProvider.processMouseEvent(event, myArea);
  }

  @Override
  protected void processMouseMotionEvent(MouseEvent event) {
    myToolProvider.processMouseEvent(event, myArea);
  }

  @Override
  public Object getData(@NonNls String dataId) {
    return IEditableArea.DATA_KEY.is(dataId) ? myArea : null;
  }
}