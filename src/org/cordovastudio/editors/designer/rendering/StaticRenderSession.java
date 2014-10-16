/*
 * Copyright (C) 2010 The Android Open Source Project
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

package org.cordovastudio.editors.designer.rendering;


import org.cordovastudio.editors.designer.model.ViewInfo;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
 * Static {@link RenderSession} returning a given {@link Result}, {@link ViewInfo} and
 * {@link java.awt.image.BufferedImage}.
 * <p/>
 * All other methods are untouched from the base implementation provided by the API.
 * <p/>
 * This is meant to be used as a wrapper around the static results. No further operations are
 * possible.
 *
 */
public class StaticRenderSession extends RenderSession {

    private final Result mResult;
    private final List<ViewInfo> mRootViewInfo;
    private final BufferedImage mImage;

    public StaticRenderSession(Result result, ViewInfo rootViewInfo, BufferedImage image) {
        mResult = result;
        mRootViewInfo = Collections.singletonList(rootViewInfo);
        mImage = image;
    }

    @Override
    public Result getResult() {
        return mResult;
    }

    @Override
    public List<ViewInfo> getRootViews() {
        return mRootViewInfo;
    }

    @Override
    public BufferedImage getImage() {
        return mImage;
    }
}
