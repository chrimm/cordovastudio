/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Orignial as of com.intellij.designer.designSurface.selection.NonResizeSelectionDecorator and
 *  com.intellij.android.designer.designSurface.graphics.NonResizeSelectionDecorator)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.designer.designSurface.selection.NonResizeSelectionDecorator and
 *    com.intellij.android.designer.designSurface.graphics.NonResizeSelectionDecorator
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
package org.cordovastudio.editors.designer.designSurface.selection;


import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.tools.DragTracker;
import org.cordovastudio.editors.designer.designSurface.tools.InputTool;
import org.cordovastudio.editors.designer.model.RadComponent;

import java.awt.*;

public class NonResizeSelectionDecorator extends ComponentDecorator {
    private final DrawingStyle myStyle;
    protected final Color myColor;
    private final int myLineWidth;
    private final BasicStroke myStroke;

    public NonResizeSelectionDecorator(DrawingStyle style) {
        myColor = Color.RED;
        myLineWidth = 1;
        myStroke = myLineWidth > 1 ? new BasicStroke(myLineWidth) : null;
        myStyle = style;
    }

    @Override
    public InputTool findTargetTool(DecorationLayer layer, RadComponent component, int x, int y) {
        Rectangle bounds = getBounds(layer, component);
        int lineWidth = Math.max(myLineWidth, 2);

        Rectangle top = new Rectangle(bounds.x, bounds.y, bounds.width, lineWidth);
        Rectangle bottom = new Rectangle(bounds.x, bounds.y + bounds.height - lineWidth, bounds.width, lineWidth);
        Rectangle left = new Rectangle(bounds.x, bounds.y, lineWidth, bounds.height);
        Rectangle right = new Rectangle(bounds.x + bounds.width - lineWidth, bounds.y, lineWidth, bounds.height);

        if (top.contains(x, y) || bottom.contains(x, y) || left.contains(x, y) || right.contains(x, y)) {
            return new DragTracker(component);
        }

        return null;
    }

    protected Rectangle getBounds(DecorationLayer layer, RadComponent component) {
        return component.getBounds(layer);
    }

    @Override
    protected void paint(DecorationLayer layer, Graphics2D g, RadComponent component) {
        g.setColor(myColor);
        if (myStroke != null) {
            g.setStroke(myStroke);
        }

        Rectangle bounds = getBounds(layer, component);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
