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

package org.cordovastudio.editors.designer.model;

import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.designer.propertyTable.PropertyRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.Transferable;

/**
 * Created by cti on 28.08.14.
 */
public class Property extends com.intellij.designer.model.Property {

    public Property(@Nullable Property parent, @NotNull String name) {
        super(parent, name);
    }

    @NotNull
    @Override
    public PropertyRenderer getRenderer() {
        return null;
    }

    @Nullable
    @Override
    public PropertyEditor getEditor() {
        return null;
    }
}
