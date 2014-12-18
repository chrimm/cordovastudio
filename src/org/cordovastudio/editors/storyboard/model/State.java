/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.navigation.State)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Removed fields for Class name and XmlResource
 *  – Added field for a rendered Image
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

import org.cordovastudio.editors.designer.rendering.RenderedImage;
import org.jetbrains.annotations.NotNull;

public abstract class State {
    protected RenderedImage myRenderedImage;

    public RenderedImage getRenderedImage() {
        return myRenderedImage;
    }

    public void setRenderedImage(RenderedImage myRenderedImage) {
        this.myRenderedImage = myRenderedImage;
    }

    @Deprecated
    @NotNull
    public Point getLocation() {
        return Point.ORIGIN;
    }

    @Deprecated
    public void setLocation(@NotNull Point location) {
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
