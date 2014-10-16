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
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.model.*;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Lobas
 */
public class TableVerticalCaptionOperation extends VerticalCaptionFlowBaseOperation<RadTableLayoutComponent> {
    public TableVerticalCaptionOperation(RadTableLayoutComponent mainContainer,
                                         RadComponent container,
                                         OperationContext context,
                                         IEditableArea mainArea) {
        super(mainContainer, container, context, mainArea);
    }

    @Override
    protected void execute(@Nullable RadComponent insertBefore) throws Exception {
        for (RadComponent component : myComponents) {
            component.removeFromParent();
            myContainer.add(component, insertBefore);
        }

        RadViewComponent mainInsertBefore = insertBefore == null ? null : ((RadCaptionTableRow) insertBefore).getComponent();

        for (RadComponent component : myComponents) {
            RadComponentOperations.moveComponent(myMainContainer, ((RadCaptionTableRow) component).getComponent(), mainInsertBefore);
        }
    }
}