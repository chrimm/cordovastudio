/*
 * Copyright 2000-2012 JetBrains s.r.o.
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

package org.cordovastudio.editors.designer.designSurface.editableArea;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DataKey;
import org.cordovastudio.editors.designer.designSurface.*;
import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackTreeLayer;
import org.cordovastudio.editors.designer.designSurface.operations.EditOperation;
import org.cordovastudio.editors.designer.designSurface.tools.InputTool;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by cti on 27.08.14.
 */
public interface IEditableArea {
    DataKey<IEditableArea> DATA_KEY = DataKey.create("EditableArea");

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Selection
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    void addSelectionListener(ComponentSelectionListener listener);

    void removeSelectionListener(ComponentSelectionListener listener);

    @NotNull
    List<RadComponent> getSelection();

    boolean isSelected(@NotNull RadComponent component);

    void select(@NotNull RadComponent component);

    void deselect(@NotNull RadComponent component);

    void appendSelection(@NotNull RadComponent component);

    void setSelection(@NotNull List<RadComponent> components);

    void deselect(@NotNull Collection<RadComponent> components);

    void deselectAll();

    void scrollToSelection();

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Visual
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    void setCursor(@Nullable Cursor cursor);

    void setDescription(@Nullable String text);

    @NotNull
    JComponent getNativeComponent();

    @Nullable
    RadComponent findTarget(int x, int y, @Nullable ComponentTargetFilter filter);

    @Nullable
    InputTool findTargetTool(int x, int y);

    void showSelection(boolean value);

    ComponentDecorator getRootSelectionDecorator();

    @Nullable
    EditOperation processRootOperation(OperationContext context);

    FeedbackLayer getFeedbackLayer();

    RadComponent getRootComponent();

    boolean isTree();

    @Nullable
    FeedbackTreeLayer getFeedbackTreeLayer();

    ActionGroup getPopupActions();

    String getPopupPlace();
}
