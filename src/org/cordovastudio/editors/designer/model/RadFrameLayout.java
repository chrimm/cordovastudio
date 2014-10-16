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
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.Pair;
import org.cordovastudio.editors.designer.ResizeOperation;
import org.cordovastudio.editors.designer.actions.AbstractGravityAction;
import org.cordovastudio.editors.designer.componentTree.TreeEditOperation;
import org.cordovastudio.editors.designer.designSurface.*;
import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.ResizeSelectionDecorator;
import org.cordovastudio.editors.designer.designSurface.operations.EditOperation;
import org.cordovastudio.editors.designer.designSurface.operations.FrameLayoutOperation;
import org.cordovastudio.editors.designer.designSurface.operations.LayoutMarginOperation;
import org.cordovastudio.editors.designer.designSurface.operations.TreeDropToOperation;
import org.cordovastudio.editors.designer.designSurface.selection.ResizePoint;
import org.cordovastudio.editors.designer.policies.ResizePolicy;
import org.cordovastudio.editors.designer.utils.Position;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static org.cordovastudio.GlobalConstants.NS_RESOURCES;

/**
 * @author Alexander Lobas
 */
public class RadFrameLayout extends RadViewLayoutWithData {
    private static final String[] LAYOUT_PARAMS = {"FrameLayout_Layout", "ViewGroup_MarginLayout"};

    private ResizeSelectionDecorator mySelectionDecorator;

    @Override
    @NotNull
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
            return new FrameLayoutOperation(myContainer, context);
        }
        if (context.is(ResizeOperation.TYPE)) {
            return new ResizeOperation(context);
        }
        if (context.is(LayoutMarginOperation.TYPE)) {
            return new LayoutMarginOperation(context);
        }
        return null;
    }

    @Override
    public ComponentDecorator getChildSelectionDecorator(RadComponent component, List<RadComponent> selection) {
        if (mySelectionDecorator == null) {
            mySelectionDecorator = new ResizeSelectionDecorator(DrawingStyle.SELECTION) {
                @Override
                protected boolean visible(RadComponent component, ResizePoint point) {
                    if (point.getType() == ResizeOperation.TYPE && component instanceof RadViewComponent) {
                        ResizePolicy policy = ResizePolicy.getResizePolicy((RadViewComponent) component);
                        return policy.applies((DirectionResizePoint) point);
                    }
                    if (point.getType() == LayoutMarginOperation.TYPE) {
                        Pair<Gravity, Gravity> gravity = Gravity.getSides(component);
                        int direction = ((DirectionResizePoint) point).getDirection();

                        if (direction == Position.WEST) { // left
                            return gravity.first == Gravity.left || gravity.first == Gravity.center;
                        }
                        if (direction == Position.EAST) { // right
                            return gravity.first == Gravity.right || gravity.first == Gravity.center;
                        }
                        if (direction == Position.NORTH) { // top
                            return gravity.second == Gravity.top || gravity.second == Gravity.center;
                        }
                        if (direction == Position.SOUTH) { // bottom
                            return gravity.second == Gravity.bottom || gravity.second == Gravity.center;
                        }
                    }
                    return true;
                }
            };
        }

        mySelectionDecorator.clear();
        if (selection.size() == 1) {
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

    private static List<Pair<Boolean, Gravity>> ITEMS = Arrays
            .asList(Pair.create(Boolean.FALSE, Gravity.top), Pair.create(Boolean.FALSE, Gravity.center),
                    Pair.create(Boolean.FALSE, Gravity.bottom), null, Pair.create(Boolean.TRUE, Gravity.left),
                    Pair.create(Boolean.TRUE, Gravity.center), Pair.create(Boolean.TRUE, Gravity.right));

    @Override
    public void addContainerSelectionActions(CordovaDesignerEditorPanel designer,
                                             DefaultActionGroup actionGroup,
                                             List<? extends RadViewComponent> selection) {
        super.addContainerSelectionActions(designer, actionGroup, selection);

        if (selection.size() != 1) {
            return;
        }

        AbstractGravityAction<Pair<Boolean, Gravity>> action =
                new AbstractGravityAction<Pair<Boolean, Gravity>>(designer, selection) {
                    private Pair<Gravity, Gravity> myGravity;

                    @Override
                    protected boolean addSeparator(DefaultActionGroup actionGroup, Pair<Boolean, Gravity> item) {
                        if (item == null) {
                            actionGroup.addSeparator();
                            return true;
                        }
                        return false;
                    }

                    @NotNull
                    @Override
                    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
                        myGravity = Gravity.getSides(myComponents.get(0));
                        return super.createPopupActionGroup(button);
                    }

                    @Override
                    protected void update(Pair<Boolean, Gravity> item, Presentation presentation, boolean popup) {
                        if (popup) {
                            Gravity selection = item.first ? myGravity.first : myGravity.second;
                            presentation.setIcon(selection == item.second ? CHECKED : null);
                            presentation.setText(item.second.name());
                        }
                    }

                    @Override
                    protected boolean selectionChanged(Pair<Boolean, Gravity> item) {
                        Gravity oldSelection = item.first ? myGravity.first : myGravity.second;

                        if (oldSelection != item.second) {
                            final String gravity =
                                    item.first ? Gravity.getValue(item.second, myGravity.second) : Gravity.getValue(myGravity.first, item.second);

                            execute(new Runnable() {
                                @Override
                                public void run() {
                                    myComponents.get(0).getTag().setAttribute("layout_gravity", NS_RESOURCES, gravity);
                                }
                            });
                        }

                        return false;
                    }

                    @Override
                    public void setSelection(Pair<Boolean, Gravity> selection) {
                    }
                };
        action.setItems(ITEMS, null);

        actionGroup.add(action);
    }
}
