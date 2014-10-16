/*
 * Copyright (C) 2014 Christoffer T. Timm
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

package org.cordovastudio.editors.designer.designSurface.decorators;

import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.model.GridInfo;
import org.cordovastudio.editors.designer.model.RadComponent;

import java.awt.*;

/**
 * @author Alexander Lobas
 */
public abstract class GridSelectionDecorator extends ResizeSelectionDecorator {
    public GridSelectionDecorator(DrawingStyle style) {
        super(style);
    }

    @Override
    protected Rectangle getBounds(DecorationLayer layer, RadComponent component) {
        return getCellBounds(layer, component);
    }

    public abstract Rectangle getCellBounds(Component layer, RadComponent component);

    public static Rectangle calculateBounds(Component layer,
                                            GridInfo gridInfo,
                                            RadComponent parent,
                                            RadComponent component,
                                            int row,
                                            int column,
                                            int rowSpan,
                                            int columnSpan) {
        Rectangle bounds = parent.getBounds(layer);

        Point topLeft = gridInfo.getCellPosition(layer, row, column);
        Point bottomRight = gridInfo.getCellPosition(layer, row + rowSpan, column + columnSpan);
        bounds.x += topLeft.x;
        bounds.width = bottomRight.x - topLeft.x;
        bounds.y += topLeft.y;
        bounds.height = bottomRight.y - topLeft.y;

        Rectangle componentBounds = component.getBounds(layer);
        if (!bounds.contains(componentBounds.x, componentBounds.y)) {
            return componentBounds;
        }

        return bounds;
    }
}
