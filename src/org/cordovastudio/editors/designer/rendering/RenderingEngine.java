/*
 * Copyright (C) 2010 The Android Open Source Project
 * (original as of com.android.ide.common.rendering.api.Bridge)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adaption for Cordova rendering
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

import com.intellij.psi.xml.XmlFile;

import java.io.File;
import java.util.Map;

import static org.cordovastudio.editors.designer.rendering.Result.Status.NOT_IMPLEMENTED;

/**
 * Entry point of the Layout Library. Extensions of this class provide a method to compute
 * and render a layout.
 */
public abstract class RenderingEngine {

    /**
     * Initializes the Bridge object.
     *
     * @param platformProperties The build properties for the platform.
     * @param fontLocation the location of the fonts.
     * @param enumValueMap map attrName => { map enumFlagName => Integer value }. This is typically
     *          read from attrs.xml in the SDK target.
     * @param log a {@link RenderLogger} object. Can be null.
     * @return true if success.
     */
    public boolean init(Map<String, String> platformProperties,
            File fontLocation,
            Map<String, Map<String, Integer>> enumValueMap,
            RenderLogger log) {
        return false;
    }

    /**
     * Prepares the layoutlib to unloaded.
     */
    public boolean dispose() {
        return false;
    }

    /**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link RenderSession} on which further actions can be taken.
     *
     * @return a new {@link RenderSession} object that contains the result of the scene creation and
     * first rendering.
     */
    public abstract RenderSession createSession(RenderParams params) throws RenderingException;

    /**
     * Renders a Drawable. If the rendering is successful, the result image is accessible through
     * {@link Result#getData()}. It is of type {@link java.awt.image.BufferedImage}
     *
     * @return the result of the action.
     */
    public Result renderDrawable() throws RenderingException {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
     * Clears the resource cache for a specific project.
     * <p/>This cache contains bitmaps and nine patches that are loaded from the disk and reused
     * until this method is called.
     * <p/>The cache is not configuration dependent and should only be cleared when a
     * resource changes (at this time only bitmaps and 9 patches go into the cache).
     * <p/>
     * The project key provided must be similar to the one passed in {@link RenderParams}.
     *
     * @param projectKey the key for the project.
     */
    public void clearCaches(Object projectKey) {

    }

    /**
     * Utility method returning the parent of a given view object.
     *
     * @param viewObject the object for which to return the parent.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the parent
     *      object in {@link Result#getData()}
     */
    public Result getViewParent(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
     * Utility method returning the index of a given view in its parent.
     * @param viewObject the object for which to return the index.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the index in
     *      the parent in {@link Result#getData()}
     */
    public Result getViewIndex(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
     * Returns true if the character orientation of the locale is right to left.
     * @param locale The locale formatted as language-region
     * @return true if the locale is right to left.
     */
    public boolean isRtl(String locale) {
        return false;
    }

    /**
     * Utility method returning the baseline value for a given view object. This basically returns
     * View.getBaseline().
     *
     * @param viewObject the object for which to return the index.
     *
     * @return the baseline value or -1 if not applicable to the view object or if this layout
     *     library does not implement this method.
     *
     * @deprecated use the extended ViewInfo.
     */
    @Deprecated
    public Result getViewBaseline(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }
}
