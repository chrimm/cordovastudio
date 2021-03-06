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
package org.cordovastudio.editors.designer.designSurface.operations;

import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.decorators.GridSelectionDecorator;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadGridLayoutComponent;
import org.cordovastudio.editors.designer.utils.Position;

import java.awt.*;

/**
 * @author Alexander Lobas
 */
public class GridLayoutSpanOperation extends LayoutSpanOperation {
    public GridLayoutSpanOperation(OperationContext context, GridSelectionDecorator decorator) {
        super(context, decorator);
    }

    @Override
    public void setComponent(RadComponent component) {
        super.setComponent(component);
        int direction = myContext.getResizeDirection();
        mySpan = RadGridLayoutComponent.getSpan(myComponent, direction == Position.NORTH || direction == Position.SOUTH);
    }

    @Override
    protected String getColumnAttribute(boolean asName) {
        return asName ? "layout:column" : "layout_column";
    }

    @Override
    protected String getColumnSpanAttribute(boolean asName) {
        return asName ? "layout:columnSpan" : "layout_columnSpan";
    }

    @Override
    protected String getRowAttribute(boolean asName) {
        return asName ? "layout:row" : "layout_row";
    }

    @Override
    protected String getRowSpanAttribute(boolean asName) {
        return asName ? "layout:rowSpan" : "layout_rowSpan";
    }

    @Override
    protected Point getCellInfo() {
        return RadGridLayoutComponent.getCellInfo(myComponent).getLocation();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // ResizePoint
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    public static void points(GridSelectionDecorator decorator) {
        decorator.addPoint(new SpanPoint(Position.WEST,
                TYPE,
                "Change layout:column x layout:columnSpan",
                decorator).move(0, 0.25)); // left

        decorator.addPoint(new SpanPoint(Position.EAST,
                TYPE,
                "Change layout:columnSpan",
                decorator).move(1, 0.75)); // right

        decorator.addPoint(new SpanPoint(Position.NORTH,
                TYPE,
                "Change layout:row x layout:rowSpan",
                decorator).move(0.25, 0)); // top

        decorator.addPoint(new SpanPoint(Position.SOUTH,
                TYPE,
                "Change layout:rowSpan",
                decorator).move(0.75, 1)); // bottom
    }
}