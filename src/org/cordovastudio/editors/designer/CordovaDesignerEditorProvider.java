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

package org.cordovastudio.editors.designer;

import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Created by cti on 28.08.14.
 */
public class CordovaDesignerEditorProvider implements FileEditorProvider  {
    private static final Logger LOG = Logger.getInstance(CordovaDesignerEditorProvider.class);

    /** FileEditorProvider ID for the layout editor */
    public static final String CORDOVA_DESIGNER_ID = "cordova-designer";

    public boolean accept(@NotNull final Project project, @NotNull final VirtualFile file){
        return file.getFileType() == StdFileTypes.HTML &&
               (ModuleUtil.findModuleForFile(file, project) != null || file instanceof LightVirtualFile);
    }

    @NotNull public FileEditor createEditor(@NotNull final Project project, @NotNull final VirtualFile file){
        LOG.assertTrue(accept(project, file));
        return new CordovaDesignerEditor(project, file);
    }

    public void disposeEditor(@NotNull final FileEditor editor){
        Disposer.dispose(editor);
    }

    @NotNull
    public FileEditorState readState(@NotNull final Element element, @NotNull final Project project, @NotNull final VirtualFile file){
        return new CordovaDesignerEditorState(file);
    }

    public void writeState(@NotNull final FileEditorState state, @NotNull final Project project, @NotNull final Element element){
        //TODO[anton,vova] implement
    }

    @NotNull public String getEditorTypeId(){
        return CORDOVA_DESIGNER_ID;
    }

    @NotNull public FileEditorPolicy getPolicy() {
        /* Always use the Cordova Designer instead of standard HTML editor */
        //return ApplicationManagerEx.getApplicationEx().isInternal() ? FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR : FileEditorPolicy.HIDE_DEFAULT_EDITOR;
        //return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }

}

