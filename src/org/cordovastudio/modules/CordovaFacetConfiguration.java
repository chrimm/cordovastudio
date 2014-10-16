/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * (Original as of org.jetbrains.android.facet.AndroidFacetConfiguration)
 *
 * Copyright 2014 (C) Christoffer T. Timm
 * Changes:
 *  – Complete overhaul to adopt to Cordova projects
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
package org.cordovastudio.modules;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import org.cordovastudio.dialogs.CordovaFacetEditorTab;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene.Kudelevsky
 */
public class CordovaFacetConfiguration implements FacetConfiguration {
    private static final FacetEditorTab[] NO_EDITOR_TABS = new FacetEditorTab[0];

    private CordovaFacet myFacet = null;

    public void init(@NotNull Module module, @NotNull VirtualFile contentRoot) {
        init(module, contentRoot.getPath());
    }

    public void init(@NotNull Module module, @NotNull String baseDirectoryPath) {
    }

    public void setFacet(@NotNull CordovaFacet facet) {
        this.myFacet = facet;
    }

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[]{new CordovaFacetEditorTab(editorContext, this)};
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
    }
}
