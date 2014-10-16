/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.intellij.designer.designSurface.selection.ResizeSelectionDecorator and com.intellij.android.designer.designSurface.graphics.ResizeSelectionDecorator)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.designer.designSurface.selection.ResizeSelectionDecorator and com.intellij.android.designer.designSurface.graphics.ResizeSelectionDecorator
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

import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.DesignerGraphics;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.selection.NonResizeSelectionDecorator;
import org.cordovastudio.editors.designer.designSurface.selection.ResizePoint;
import org.cordovastudio.editors.designer.designSurface.tools.InputTool;
import org.cordovastudio.editors.designer.model.RadComponent;

import java.awt.*;
import java.util.*;

public class ResizeSelectionDecorator extends NonResizeSelectionDecorator {
  private final DrawingStyle myStyle;

  public ResizeSelectionDecorator(DrawingStyle style) {
    super(style);
    myStyle = style;
  }

  @Override
  protected void paint(DecorationLayer layer, Graphics2D g, RadComponent component) {
    Rectangle bounds = getBounds(layer, component);
    DesignerGraphics.drawRect(myStyle, g, bounds.x, bounds.y, bounds.width, bounds.height);
  }

    private final java.util.List<ResizePoint> myPoints = new ArrayList<ResizePoint>();

    public void clear() {
        myPoints.clear();
    }

    public void addPoint(ResizePoint point) {
        myPoints.add(point);
    }

    @Override
    public InputTool findTargetTool(DecorationLayer layer, RadComponent component, int x, int y) {
        for (ResizePoint point : myPoints) {
            if (visible(component, point)) {
                InputTool tracker = point.findTargetTool(layer, component, x, y);
                if (tracker != null) {
                    return tracker;
                }
            }
        }

        return super.findTargetTool(layer, component, x, y);
    }

    @Override
    public void decorate(DecorationLayer layer, Graphics2D g, RadComponent component) {
        super.decorate(layer, g, component);

        for (ResizePoint point : myPoints) {
            if (visible(component, point)) {
                point.decorate(layer, g, component);
            }
        }
    }

    protected boolean visible(RadComponent component, ResizePoint point) {
        return true;
    }
}
