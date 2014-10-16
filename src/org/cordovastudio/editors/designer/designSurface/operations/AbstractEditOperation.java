/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.designSurface.AbstractEditOperation and com.intellij.designer.designSurface.AbstractEditOperation)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.android.designer.designSurface.AbstractEditOperation and com.intellij.designer.designSurface.AbstractEditOperation
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
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadComponentOperations;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public abstract class AbstractEditOperation implements EditOperation {
    protected final RadComponent myContainer;
    protected final OperationContext myContext;
    protected List<RadComponent> myComponents;

    public AbstractEditOperation(RadComponent container, OperationContext context) {
        myContainer = container;
        myContext = context;
    }

    @Override
    public void setComponent(RadComponent component) {
        myComponents = Collections.singletonList(component);
    }

    @Override
    public void setComponents(List<RadComponent> components) {
        myComponents = components;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public void execute() throws Exception {
        execute(null);
    }

    protected void execute(@Nullable RadViewComponent insertBefore) throws Exception {
        execute(myContext, (RadViewComponent) myContainer, myComponents, insertBefore);
    }

    public static void execute(OperationContext context,
                               RadViewComponent container,
                               List<RadComponent> components,
                               @Nullable RadViewComponent insertBefore) throws Exception {
        if (context.isAdd() || context.isMove()) {
            for (RadComponent component : components) {
                RadComponentOperations.moveComponent(container, (RadViewComponent) component, insertBefore);
            }
        } else if (context.isCreate()) {
            for (RadComponent component : components) {
                RadComponentOperations.addComponent(container, (RadViewComponent) component, insertBefore);
            }
        } else if (context.isPaste()) {
            for (RadComponent component : components) {
                RadComponentOperations.pasteComponent(container, (RadViewComponent) component, insertBefore);
            }
        }
    }
}