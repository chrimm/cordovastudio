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
package org.cordovastudio.editors.designer.designSurface.decorators;


import org.cordovastudio.editors.designer.designSurface.ConstraintPainter;
import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.DesignerGraphics;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;

import java.awt.*;
import java.util.List;

/**
 * Decorator which paints the RelativeLayout's constraints
 */
public class RelativeLayoutDecorator extends StaticDecorator {
  public RelativeLayoutDecorator(RadComponent container) {
    super(container);
  }

  @Override
  protected void paint(DecorationLayer layer, Graphics2D g, RadComponent container) {
    if (!layer.showSelection() ) {
      return;
    }

    List<RadComponent> selection = layer.getArea().getSelection();
    if (selection.isEmpty()) {
      return;
    }

    if (container instanceof RadViewComponent) {
      DesignerGraphics graphics = new DesignerGraphics(g, layer);
      RadViewComponent parent = (RadViewComponent)container;

      List<RadViewComponent> childNodes = RadViewComponent.getViewComponents(selection);
      boolean showDependents = parent.getChildren().size() > childNodes.size();
      ConstraintPainter.paintSelectionFeedback(graphics, parent, childNodes, showDependents);
    }
  }
}
