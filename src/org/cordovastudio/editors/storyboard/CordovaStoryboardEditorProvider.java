/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.NavigationEditorProvider)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova Projects (i.e. changed file acceptance to HTML files)
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

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CordovaStoryboardEditorProvider implements FileEditorProvider, DumbAware {
    @NonNls
    public static final String ID = "storyboard";

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return "html".equalsIgnoreCase(file.getExtension());
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new CordovaStoryboardEditor(project, file);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor editor) {
        Disposer.dispose(editor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project,
                                     @NotNull VirtualFile file) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState state, @NotNull Project project,
                           @NotNull Element targetElement) {
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return ID;
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
