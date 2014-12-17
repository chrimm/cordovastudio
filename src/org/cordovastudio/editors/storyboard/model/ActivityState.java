/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.storyboard.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActivityState extends State {
    private final String className;
    private String xmlResourceName;

    public ActivityState(String className) {
        this.className = className;
    }

    @NotNull
    @Override
    public String getClassName() {
        return className;
    }

    @Nullable
    @Override
    public String getXmlResourceName() {
        return xmlResourceName;
    }

    public void setXmlResourceName(String xmlResourceName) {
        this.xmlResourceName = xmlResourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityState that = (ActivityState) o;

        if (!className.equals(that.className)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return "ActivityState{" + className + '}';
    }
}
