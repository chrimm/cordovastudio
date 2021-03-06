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
import com.intellij.util.ArrayUtil;
import org.cordovastudio.actions.AllGravityAction;
import org.cordovastudio.editors.designer.designSurface.*;
import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.GridSelectionDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.StaticDecorator;
import org.cordovastudio.editors.designer.designSurface.operations.EditOperation;
import org.cordovastudio.editors.designer.designSurface.operations.LayoutSpanOperation;
import org.cordovastudio.editors.designer.designSurface.operations.TableLayoutSpanOperation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public class RadTableRowLayout extends RadLinearLayout {
    private static final String[] LAYOUT_PARAMS = {"TableRow_Cell", "LinearLayout_Layout", "ViewGroup_MarginLayout"};

    private GridSelectionDecorator mySelectionDecorator;

    @Override
    @NotNull
    public String[] getLayoutParams() {
        return LAYOUT_PARAMS;
    }

    public static boolean is(RadComponent component) {
        return component != null && component.getLayout() instanceof RadTableRowLayout;
    }

    private boolean isTableParent() {
        return myContainer.getParent() instanceof RadTableLayoutComponent;
    }

    @Override
    public boolean isHorizontal() {
        return true;
    }

    @Override
    public EditOperation processChildOperation(OperationContext context) {
        if (!isTableParent() || context.isTree()) {
            return super.processChildOperation(context);
        }
        if (context.is(LayoutSpanOperation.TYPE)) {
            return new TableLayoutSpanOperation(context, mySelectionDecorator);
        }
        return null;
    }

    @Override
    public void addStaticDecorators(List<StaticDecorator> decorators, List<RadComponent> selection) {
        if (!isTableParent()) {
            super.addStaticDecorators(decorators, selection);
        }
    }

    @Override
    protected boolean supportsOrientation() {
        return false;
    }

    @Override
    public ComponentDecorator getChildSelectionDecorator(RadComponent component, List<RadComponent> selection) {
        if (isTableParent()) {
            if (mySelectionDecorator == null) {
                mySelectionDecorator = new GridSelectionDecorator(DrawingStyle.SELECTION) {
                    @Override
                    public Rectangle getCellBounds(Component layer, RadComponent component) {
                        try {
                            RadTableLayoutComponent tableComponent = (RadTableLayoutComponent) component.getParent().getParent();
                            GridInfo gridInfo = tableComponent.getVirtualGridInfo();
                            int row = tableComponent.getChildren().indexOf(component.getParent());
                            RadComponent[] rowComponents = gridInfo.components[row];
                            int column = ArrayUtil.indexOf(rowComponents, component);
                            int columnSpan = 1;

                            for (int i = column + 1; i < rowComponents.length; i++) {
                                if (rowComponents[i] == component) {
                                    columnSpan++;
                                } else {
                                    break;
                                }
                            }

                            return calculateBounds(layer, gridInfo, tableComponent, component, row, column, 1, columnSpan);
                        } catch (Throwable e) {
                            return new Rectangle();
                        }
                    }
                };
            }

            mySelectionDecorator.clear();
            if (selection.size() == 1) {
                TableLayoutSpanOperation.points(mySelectionDecorator);
            }
            return mySelectionDecorator;
        }
        return super.getChildSelectionDecorator(component, selection);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Actions
    //
    //////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void addContainerSelectionActions(CordovaDesignerEditorPanel designer,
                                             DefaultActionGroup actionGroup,
                                             List<? extends RadViewComponent> selectedChildren) {
        super.addContainerSelectionActions(designer, actionGroup, selectedChildren);
        if (isTableParent()) {
            actionGroup.add(new AllGravityAction(designer, selectedChildren));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Caption
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ICaption getCaption(RadComponent component) {
        return isTableParent() ? myContainer.getParent().getLayout().getCaption(component) : null;
    }
}