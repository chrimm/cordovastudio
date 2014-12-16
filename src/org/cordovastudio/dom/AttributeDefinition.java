/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * (Original as of org.jetbrains.android.dom.attrs.AttributeDefinition)
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
package org.cordovastudio.dom;

import com.intellij.openapi.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author yole
 */
public class AttributeDefinition {
    private final String myName;
    private final Set<AttributeFormat> myFormats = EnumSet.noneOf(AttributeFormat.class);
    private final Vector<KeyValue<String, String>> myValues = new Vector<>();
    private final Map<String, String> myStyleable2DocValue = new HashMap<>();
    private String myGlobalDocValue;

    public AttributeDefinition(@NotNull String name) {
        myName = name;
    }

    public AttributeDefinition(@NotNull String name, @NotNull Collection<AttributeFormat> formats) {
        myName = name;
        myFormats.addAll(formats);
    }

    public void addValue(@NotNull String name, @NotNull String displayName) {
        myValues.add(new KeyValue<>(name, displayName));
    }

    @NotNull
    public String getName() {
        return myName;
    }

    @NotNull
    public Set<AttributeFormat> getFormats() {
        return Collections.unmodifiableSet(myFormats);
    }

    public void addFormats(@NotNull Collection<AttributeFormat> format) {
        myFormats.addAll(format);
    }

    @NotNull
    public Vector<KeyValue<String, String>> getValues() {
        return myValues;
    }

    @Nullable
    public String getDocValue(@Nullable String parentStyleable) {
        if (parentStyleable == null || !myStyleable2DocValue.containsKey(parentStyleable)) {
            return myGlobalDocValue;
        }
        return myStyleable2DocValue.get(parentStyleable);
    }

    public void addDocValue(@NotNull String docValue, @Nullable String parentStyleable) {
        if (parentStyleable == null || myGlobalDocValue == null) {
            myGlobalDocValue = docValue;
        }
        if (parentStyleable != null) {
            myStyleable2DocValue.put(parentStyleable, docValue);
        }
    }

    @Override
    public String toString() {
        return myName + " [" + myFormats + ']';
    }
}
