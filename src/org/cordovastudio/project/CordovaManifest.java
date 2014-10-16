/*
 * Copyright (C) 2009 The Android Open Source Project
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

package org.cordovastudio.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

import static org.cordovastudio.GlobalConstants.FN_CORDOVA_MANIFEST_XML;

/**
 * Helper and Constants for the CordovaManifest.xml file.
 */
public final class CordovaManifest {

    public static final String NODE_APPLICATION = "application";
    public static final String NODE_USES_SDK = "uses-sdk";
    public static final String NODE_METADATA = "meta-data";

    public static final String ATTRIBUTE_PACKAGE = "package";
    public static final String ATTRIBUTE_VERSIONCODE = "versionCode";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_DEBUGGABLE = "debuggable";
    public static final String ATTRIBUTE_LABEL = "label";
    public static final String ATTRIBUTE_ICON = "icon";
    public static final String ATTRIBUTE_CORDOVA_VERSION = "cordovaVersion";
    public static final String ATTRIBUTE_THEME = "theme";
    public static final String ATTRIBUTE_PARENT_ACTIVITY_NAME = "parentActivityName";
    public static final String ATTRIBUTE_UI_OPTIONS = "uiOptions";
    public static final String ATTRIBUTE_VALUE = "value";

    public static final String VALUE_PARENT_ACTIVITY = "PARENT_ACTIVITY";

    /**
     * Returns an {@link com.intellij.openapi.vfs.VirtualFile} object representing the manifest for the given project.
     *
     * @param projectFolder The project containing the manifest file.
     * @return An IAbstractFile object pointing to the manifest or null if the manifest
     * is missing.
     */
    @Nullable
    public static VirtualFile getManifest(VirtualFile projectFolder) {
        VirtualFile file = projectFolder.findChild(FN_CORDOVA_MANIFEST_XML);
        if (file != null && file.exists()) {
            return file;
        }

        return null;
    }

    /**
     * Returns the application icon  for a given manifest.
     *
     * @param manifestFile the manifest to parse.
     * @return the icon or null (or empty) if not found.
     */
    @Nullable
    public static String getApplicationIcon(VirtualFile manifestFile, Project project) {
        try {
            XmlFile xml = (XmlFile) PsiManager.getInstance(project).findFile(manifestFile);

            XmlTag iconTag = xml.getRootTag().findSubTags("icon")[0];
            return iconTag.getAttribute("src").getValue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns the application label  for a given manifest.
     *
     * @param manifestFile the manifest to parse.
     * @return the label or null (or empty) if not found.
     */
    @Nullable
    public static String getApplicationLabel(VirtualFile manifestFile, Project project) {
        try {
            XmlFile xml = (XmlFile) PsiManager.getInstance(project).findFile(manifestFile);

            XmlTag widgetTag = xml.getRootTag().findFirstSubTag("widget");
            XmlTag nameTag = widgetTag.findFirstSubTag("name");

            return nameTag.getValue().getText();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
