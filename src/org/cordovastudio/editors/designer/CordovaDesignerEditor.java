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

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Created by cti on 27.08.14.
 */
public class CordovaDesignerEditor extends UserDataHolderBase implements FileEditor {
    private final CordovaDesignerEditorPanel myDesignerPanel;

    public CordovaDesignerEditor(Project project, VirtualFile file) {
        if (file instanceof LightVirtualFile) {
            file = ((LightVirtualFile)file).getOriginalFile();
        }
        Module module = findModule(project, file);
        if (module == null) {
            throw new IllegalArgumentException("No module for file " + file + " in project " + project);
        }
        myDesignerPanel = createDesignerPanel(project, module, file);
    }

    @Nullable
    protected Module findModule(Project project, VirtualFile file) {
        return ModuleUtilCore.findModuleForFile(file, project);
    }

    @NotNull
    protected CordovaDesignerEditorPanel createDesignerPanel(Project project, Module module, VirtualFile file) {
        return new CordovaDesignerEditorPanel(this, project, module, file);
    }

    public final CordovaDesignerEditorPanel getDesignerPanel() {
        return myDesignerPanel;
    }

    @NotNull
    @Override
    public final JComponent getComponent() {
        return myDesignerPanel;
    }

    @Override
    public final JComponent getPreferredFocusedComponent() {
        return myDesignerPanel.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "Design";
    }

    @Override
    public void dispose() {
        myDesignerPanel.dispose();
    }

    @Override
    public void selectNotify() {
        myDesignerPanel.activate();
    }

    @Override
    public void deselectNotify() {
        myDesignerPanel.deactivate();
    }

    @Override
    public boolean isValid() {
        return myDesignerPanel.isEditorValid();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    @NotNull
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return myDesignerPanel.createState();
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    /**
     * @return highlighter object to perform background analysis and highlighting activities.
     * Return <code>null</code> if no background highlighting activity necessary for this file editor.
     */
    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        //TODO: implement
        return null;
    }

    @Override
    public FileEditorLocation getCurrentLocation() {
        //TODO: implement?
        return null;
    }

    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        //TODO: implement?
        return null;
    }
}
