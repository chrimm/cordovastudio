/*
 * Copyright (C) 2015 Christoffer T. Timm
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

package org.cordovastudio.editors.designer.propertyTable.properties;

import com.intellij.designer.model.Property;
import com.intellij.openapi.application.ApplicationManager;
import org.cordovastudio.dom.AttributeDefinition;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by cti on 20.01.15.
 */
public class TextProperty extends AttributeProperty {

    public TextProperty(@NotNull String name, @NotNull AttributeDefinition definition) {
        super(name, definition);
    }

    public TextProperty(@Nullable Property parent, @NotNull String name, @NotNull AttributeDefinition definition) {
        super(parent, name, definition);
    }

    @Override
    public Object getValue(@NotNull RadViewComponent component) throws Exception {
        return component.getTag().getValue().getTrimmedText();
    }

    @Override
    public void setValue(@NotNull final RadViewComponent component, final Object value) throws Exception {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                component.getTag().getValue().setText((String) value);
            }
        });
    }
}
