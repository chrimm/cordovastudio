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

import com.intellij.util.ArrayUtil;
import org.cordovastudio.editors.designer.designSurface.decorators.GridSelectionDecorator;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.model.GridInfo;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadTableLayoutComponent;
import org.cordovastudio.editors.designer.utils.Position;

import java.awt.*;

/**
 * @author Alexander Lobas
 */
public class TableLayoutSpanOperation extends LayoutSpanOperation {
    public TableLayoutSpanOperation(OperationContext context, GridSelectionDecorator decorator) {
        super(context, decorator);
    }

    @Override
    public void setComponent(RadComponent component) {
        super.setComponent(component);
        mySpan = RadTableLayoutComponent.getCellSpan(myComponent);
    }

    @Override
    protected String getColumnAttribute(boolean asName) {
        return asName ? "layout:column" : "layout_column";
    }

    @Override
    protected String getColumnSpanAttribute(boolean asName) {
        return asName ? "layout:span" : "layout_span";
    }

    @Override
    protected String getRowAttribute(boolean asName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getRowSpanAttribute(boolean asName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected RadComponent getContainer() {
        return myComponent.getParent().getParent();
    }

    @Override
    protected Point getCellInfo() {
        RadTableLayoutComponent tableComponent = (RadTableLayoutComponent) myComponent.getParent().getParent();
        GridInfo gridInfo = tableComponent.getVirtualGridInfo();
        int row = tableComponent.getChildren().indexOf(myComponent.getParent());
        int column = ArrayUtil.indexOf(gridInfo.components[row], myComponent);

        return new Point(column, row);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // ResizePoint
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    public static void points(GridSelectionDecorator decorator) {
        decorator.addPoint(new SpanPoint(Position.WEST,
                TYPE,
                "Change layout:column x layout:span",
                decorator)); // left

        decorator.addPoint(new SpanPoint(Position.EAST,
                TYPE,
                "Change layout:span",
                decorator)); // right
    }
}