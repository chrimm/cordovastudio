/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.Transform)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted slightly for Cordova projects (i.e. renamed classes, etc.)
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
package org.cordovastudio.editors.storyboard;

import org.cordovastudio.editors.designer.rendering.RenderedView;

import java.awt.*;

public class Transform {
    public final float myScale;

    public Transform(float scale) {
        myScale = scale;
    }

    // Model to View

    private int scale(int d) {
        return ((int) (d * myScale));
    }

    public int modelToViewX(int x) {
        return scale(x);
    }

    public int modelToViewY(int y) {
        return scale(y);
    }

    public int modelToViewW(int d) {
        return scale(d);
    }

    public int modelToViewH(int d) {
        return scale(d);
    }

    public Point modelToView(org.cordovastudio.editors.storyboard.model.Point loc) {
        return new Point(modelToViewX(loc.x), modelToViewY(loc.y));
    }

    public Dimension modelToView(org.cordovastudio.editors.storyboard.model.Dimension size) {
        return new Dimension(modelToViewW(size.width), modelToViewH(size.height));
    }

    public Rectangle getBounds(RenderedView v) {
        return new Rectangle(modelToViewX(v.x), modelToViewY(v.y), modelToViewW(v.w), modelToViewH(v.h));
    }

    // View to Model

    private int unScale(int d) {
        return (int) (d / myScale);
    }

    public int viewToModelX(int d) {
        return unScale(d);
    }

    public int viewToModelY(int d) {
        return unScale(d);
    }

    public int viewToModelW(int d) {
        return unScale(d);
    }

    public int viewToModelH(int d) {
        return unScale(d);
    }

    public org.cordovastudio.editors.storyboard.model.Point viewToModel(Point loc) {
        return new org.cordovastudio.editors.storyboard.model.Point(viewToModelX(loc.x), viewToModelY(loc.y));
    }

    public org.cordovastudio.editors.storyboard.model.Dimension viewToModel(Dimension dim) {
        return new org.cordovastudio.editors.storyboard.model.Dimension(viewToModelW(dim.width), viewToModelH(dim.height));
    }
}
