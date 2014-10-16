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
import org.cordovastudio.actions.AllGravityAction;
import org.cordovastudio.editors.designer.ResizeOperation;
import org.cordovastudio.editors.designer.actions.OrientationAction;
import org.cordovastudio.editors.designer.componentTree.TreeEditOperation;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.ICaption;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.decorators.*;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.designSurface.operations.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public class RadGridLayout extends RadViewLayoutWithData implements ILayoutDecorator, ICaption, ICaptionDecorator {
    private static final String[] LAYOUT_PARAMS = {"GridLayout_Layout", "ViewGroup_MarginLayout"};

    private GridDecorator myGridDecorator;
    private GridSelectionDecorator mySelectionDecorator;

    @NotNull
    @Override
    public String[] getLayoutParams() {
        return LAYOUT_PARAMS;
    }

    @Override
    public EditOperation processChildOperation(OperationContext context) {
        if (context.isCreate() || context.isPaste() || context.isAdd() || context.isMove()) {
            if (context.isTree()) {
                if (TreeEditOperation.isTarget(myContainer, context)) {
                    return new TreeDropToOperation(myContainer, context);
                }
                return null;
            }
            return new GridLayoutOperation(myContainer, context);
        }
        if (context.is(ResizeOperation.TYPE)) {
            return new ResizeOperation(context);
        }
        if (context.is(LayoutSpanOperation.TYPE)) {
            return new GridLayoutSpanOperation(context, mySelectionDecorator);
        }
        return null;
    }

    private StaticDecorator getGridDecorator() {
        if (myGridDecorator == null) {
            myGridDecorator = new GridDecorator(myContainer);
        }
        return myGridDecorator;
    }

    @Override
    public void addStaticDecorators(List<StaticDecorator> decorators, List<RadComponent> selection) {
        if (selection.contains(myContainer)) {
            if (!(myContainer.getParent().getLayout() instanceof ILayoutDecorator)) {
                decorators.add(getGridDecorator());
            }
        } else {
            for (RadComponent component : selection) {
                if (component.getParent() == myContainer) {
                    decorators.add(getGridDecorator());
                    return;
                }
            }
            super.addStaticDecorators(decorators, selection);
        }
    }

    @Override
    public ComponentDecorator getChildSelectionDecorator(RadComponent component, List<RadComponent> selection) {
        if (mySelectionDecorator == null) {
            mySelectionDecorator = new GridSelectionDecorator(DrawingStyle.SELECTION) {
                @Override
                public Rectangle getCellBounds(Component layer, RadComponent component) {
                    try {
                        RadGridLayoutComponent parent = (RadGridLayoutComponent) component.getParent();
                        GridInfo gridInfo = parent.getGridInfo();
                        Rectangle cellInfo = RadGridLayoutComponent.getCellInfo(component);

                        return calculateBounds(layer, gridInfo, parent, component, cellInfo.y, cellInfo.x, cellInfo.height, cellInfo.width);
                    } catch (Throwable e) {
                        return new Rectangle();
                    }
                }
            };
        }

        mySelectionDecorator.clear();
        if (selection.size() == 1) {
            GridLayoutSpanOperation.points(mySelectionDecorator);
            ResizeOperation.addResizePoints(mySelectionDecorator, (RadViewComponent) selection.get(0));
        } else {
            ResizeOperation.addResizePoints(mySelectionDecorator);
        }

        return mySelectionDecorator;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Actions
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void addContainerSelectionActions(CordovaDesignerEditorPanel designer,
                                             DefaultActionGroup actionGroup,
                                             List<? extends RadViewComponent> selection) {
        super.addContainerSelectionActions(designer, actionGroup, selection);

        actionGroup.add(new OrientationAction(designer, (RadViewComponent) myContainer, true));
        actionGroup.add(new AllGravityAction(designer, selection));
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Caption
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ICaption getCaption(RadComponent component) {
        if (myContainer == component && myContainer.getParent().getLayout() instanceof ICaptionDecorator) {
            return null;
        }
        if (myContainer.getChildren().isEmpty()) {
            return null;
        }
        return this;
    }

    @NotNull
    @Override
    public List<RadComponent> getCaptionChildren(IEditableArea mainArea, boolean horizontal) {
        RadGridLayoutComponent container = getGridComponent();
        GridInfo gridInfo = container.getGridInfo();
        List<RadComponent> components = new ArrayList<RadComponent>();

        if (horizontal) {
            int[] lines = gridInfo.vLines;
            boolean[] emptyColumns = gridInfo.emptyColumns;

            for (int i = 0; i < lines.length - 1; i++) {
                components.add(new RadCaptionGridColumn(mainArea,
                        container,
                        i,
                        lines[i],
                        lines[i + 1] - lines[i],
                        emptyColumns[i]));
            }
        } else {
            int[] lines = gridInfo.hLines;
            boolean[] emptyRows = gridInfo.emptyRows;

            for (int i = 0; i < lines.length - 1; i++) {
                components.add(new RadCaptionGridRow(mainArea,
                        container,
                        i,
                        lines[i],
                        lines[i + 1] - lines[i],
                        emptyRows[i]));
            }
        }

        return components;
    }

    private RadGridLayoutComponent getGridComponent() {
        return (RadGridLayoutComponent) myContainer;
    }

    private RadLayout myCaptionColumnLayout;
    private RadLayout myCaptionRowLayout;

    @NotNull
    @Override
    public RadLayout getCaptionLayout(final IEditableArea mainArea, boolean horizontal) {
        if (horizontal) {
            if (myCaptionColumnLayout == null) {
                myCaptionColumnLayout = new RadViewLayout() {
                    @Override
                    public EditOperation processChildOperation(OperationContext context) {
                        if (context.isMove()) {
                            return new GridHorizontalCaptionOperation(getGridComponent(), myContainer, context, mainArea);
                        }
                        return null;
                    }
                };
            }
            return myCaptionColumnLayout;
        }

        if (myCaptionRowLayout == null) {
            myCaptionRowLayout = new RadViewLayout() {
                @Override
                public EditOperation processChildOperation(OperationContext context) {
                    if (context.isMove()) {
                        return new GridVerticalCaptionOperation(getGridComponent(), myContainer, context, mainArea);
                    }
                    return null;
                }
            };
        }
        return myCaptionRowLayout;
    }
}