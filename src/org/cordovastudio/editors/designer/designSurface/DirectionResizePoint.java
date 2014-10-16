/*
 * Copyright (C) 2000-2012 JetBrains s.r.o. (partial)
 * Copyright (C) 2013 The Android Open Source Project (partial)
 * Copyright (C) 2014 Christoffer T. Timm
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

/*
 * This file combines
 *
 * com.intellij.designer.designSurface.selection.DirectionResizePoint
 * (by JetBrains s.r.o.)
 *
 * and
 *
 * com.intellij.android.designer.designSurface.graphics.DirectionResizePoint
 * (by The Android Open Source Project)
 *
 * with some alterations for Cordova Studio
 */
package org.cordovastudio.editors.designer.designSurface;

import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.selection.ResizePoint;
import org.cordovastudio.editors.designer.designSurface.tools.InputTool;
import org.cordovastudio.editors.designer.designSurface.tools.ResizeTracker;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.utils.Position;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * A replacement of the ui-core DirectionResizePoint which uses a logical drawing style instead of
 * a hardcoded color.
 */
public class DirectionResizePoint extends ResizePoint {
    protected int myDirection;
    final private Object myType;
    protected double myXSeparator;
    protected double myYSeparator;
    private final String myDescription;
    private final DrawingStyle myStyle;

    public DirectionResizePoint(DrawingStyle style, int direction, Object type, @Nullable String description) {
        //noinspection UseJBColor
        super(Color.RED /* should not be used */, Color.RED /* should not be used */);
        setDirection(direction);
        myStyle = style;
        myType = type;
        myDescription = description;
    }

    private void setDirection(int direction) {
        myDirection = direction;

        int yDirection = myDirection & Position.EAST_WEST;
        if (yDirection == Position.WEST) {
            myXSeparator = 0;
        }
        else if (yDirection == Position.EAST) {
            myXSeparator = 1;
        }
        else {
            myXSeparator = 0.5;
        }

        int xDirection = myDirection & Position.NORTH_SOUTH;
        if (xDirection == Position.NORTH) {
            myYSeparator = 0;
        }
        else if (xDirection == Position.SOUTH) {
            myYSeparator = 1;
        }
        else {
            myYSeparator = 0.5;
        }
    }

    public DirectionResizePoint move(double xSeparator, double ySeparator) {
        myXSeparator = xSeparator;
        myYSeparator = ySeparator;
        return this;
    }

    public int getDirection() {
        return myDirection;
    }

    @Override
    public Object getType() {
        return myType;
    }

    @Override
    protected InputTool createTool(RadComponent component) {
        return new ResizeTracker(myDirection, myType, myDescription);
    }

    @Override
    protected Point getLocation(DecorationLayer layer, RadComponent component) {
        Rectangle bounds = getBounds(layer, component);
        int size = (getSize() + 1) / 2;
        int x = bounds.x + (int)(bounds.width * myXSeparator) - size;
        int y = bounds.y + (int)(bounds.height * myYSeparator) - size;

        if (myXSeparator == 0) {
            x++;
        }
        if (myYSeparator == 0) {
            y++;
        }

        return new Point(x, y);
    }

    protected Rectangle getBounds(DecorationLayer layer, RadComponent component) {
        return component.getBounds(layer);
    }

    @Override
    protected void paint(DecorationLayer layer, Graphics2D g, RadComponent component) {
        Point location = getLocation(layer, component);
        int size = getSize();
        DesignerGraphics.drawStrokeFilledRect(myStyle, g, location.x, location.y, size, size);
    }

    @Override
    protected int getSize() {
        return 7;
    }

    @Override
    protected int getNeighborhoodSize() {
        return 2;
    }
}
