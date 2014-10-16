/*
 * Copyright (C) 2014 The Android Open Source Project
 * (Original as of com.android.tools.idea.rendering.RefreshRenderAction)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova Studio (i.e. removed support for targets)
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
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener;
import org.cordovastudio.modules.CordovaFacet;

public class RefreshRenderAction extends AnAction {
    private final RenderContext myContext;

    public RefreshRenderAction(RenderContext context) {
        super("Refresh", null, AllIcons.Actions.Refresh);
        myContext = context;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        RenderConfiguration configuration = myContext.getConfiguration();

        if (configuration != null) {
            configuration.updated(RenderConfigurationListener.MASK_RENDERING);
        }

        myContext.requestRender();
    }
}
