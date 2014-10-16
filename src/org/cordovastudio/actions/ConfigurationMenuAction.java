/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.ConfigurationMenuAction)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Complete overhaul for Cordova projects
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
package org.cordovastudio.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.RenderPreviewMode;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConfigurationMenuAction extends FlatComboAction {
    private final RenderContext myRenderContext;

    public ConfigurationMenuAction(RenderContext renderContext) {
        myRenderContext = renderContext;
        Presentation presentation = getTemplatePresentation();
        presentation.setDescription("Configuration to render this layout with in the IDE");
        presentation.setIcon(CordovaIcons.Files.CordovaFile);
    }

    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DefaultActionGroup group = new DefaultActionGroup("Configuration", true);

        VirtualFile virtualFile = myRenderContext.getVirtualFile();
        if (virtualFile != null) {
            Module module = myRenderContext.getModule();
            if (module == null) {
                return group;
            }
            Project project = module.getProject();

            if (myRenderContext.supportsPreviews()) {
                addMultiConfigActions(group);
            }
        }

        return group;
    }

    private void addMultiConfigActions(DefaultActionGroup group) {
        VirtualFile file = myRenderContext.getVirtualFile();
        if (file == null) {
            return;
        }
        RenderConfiguration configuration = myRenderContext.getConfiguration();
        if (configuration == null) {
            return;
        }

        group.addSeparator();

        group.add(new PreviewAction(myRenderContext, "Preview Representative Sample", ACTION_PREVIEW_MODE, RenderPreviewMode.DEFAULT, true));

        group.add(new PreviewAction(myRenderContext, "None", ACTION_PREVIEW_MODE, RenderPreviewMode.NONE, true));
    }

    private static final int ACTION_ADD = 1;
    private static final int ACTION_DELETE_ALL = 2;
    private static final int ACTION_PREVIEW_MODE = 3;

    private static class PreviewAction extends AnAction {
        private final int myAction;
        private final RenderPreviewMode myMode;
        private final RenderContext myRenderContext;

        public PreviewAction(@NotNull RenderContext renderContext, @NotNull String title, int action,
                             @Nullable RenderPreviewMode mode, boolean enabled) {
            super(title, null, null);
            myRenderContext = renderContext;
            myAction = action;
            myMode = mode;

            if (mode != null && mode == RenderPreviewMode.getCurrent()) {
                // Select
                Presentation templatePresentation = getTemplatePresentation();
                templatePresentation.setIcon(AllIcons.Actions.Checked);
                templatePresentation.setEnabled(false);
            }

            if (!enabled) {
                getTemplatePresentation().setEnabled(false);
            }
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            myRenderContext.updateLayout();
            //myRenderContext.zoomFit(true /*onlyZoomOut*/, false /*allowZoomIn*/);
        }
    }
}
