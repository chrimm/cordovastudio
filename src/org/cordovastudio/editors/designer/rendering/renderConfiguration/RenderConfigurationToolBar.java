/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.ConfigurationToolbar)
 *
 * Cooyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova Studio: removed TargetMenuAction and LocaleMenuAction,
 *      changed some classnames to Cordova Studio ones, removed support for
 *      the android platforms differentiation
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
package org.cordovastudio.editors.designer.rendering.renderConfiguration;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.psi.PsiFile;
import org.cordovastudio.actions.*;
import org.cordovastudio.editors.designer.designSurface.preview.PreviewToolWindowForm;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * A widget for configuring a configuration
 */
public class RenderConfigurationToolBar extends JPanel {
  private final PreviewToolWindowForm myPreviewWindow;

  public RenderConfigurationToolBar(PreviewToolWindowForm previewWindow) {
    myPreviewWindow = previewWindow;
    DefaultActionGroup group = createActions(myPreviewWindow);
    ActionToolbar toolbar = createToolBar(group);

    setLayout(new BorderLayout());
    add(toolbar.getComponent(), BorderLayout.CENTER);
  }

  private static ActionToolbar createToolBar(ActionGroup group) {
    ActionManager actionManager = ActionManager.getInstance();
    ActionToolbar toolbar = actionManager.createActionToolbar("LayoutConfiguration", group, true);
    toolbar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);

    // The default toolbar layout adds too much spacing between the buttons. Switch to mini mode,
    // but also set a minimum size which will add *some* padding for our 16x16 icons.
    // Disabled because mini mode does not seem to change the visual appearance anymore, and
    // more importantly, it introduces some subtle layout bugs (additional insets when the
    // toolbar does not fully fit etc)
    //toolbar.setMiniMode(true);

    toolbar.setMinimumButtonSize(new Dimension(22, 24));
    return toolbar;
  }

  public static DefaultActionGroup createActions(RenderContext configurationHolder) {
    DefaultActionGroup group = new DefaultActionGroup();

    ConfigurationMenuAction configAction = new ConfigurationMenuAction(configurationHolder);
    group.add(configAction);
    group.addSeparator();

    DeviceMenuAction deviceAction = new DeviceMenuAction(configurationHolder);
    group.add(deviceAction);
    group.addSeparator();

    OrientationMenuAction orientationAction = new OrientationMenuAction(configurationHolder);
    group.add(orientationAction);
    group.addSeparator();

    ThemeMenuAction themeAction = new ThemeMenuAction(configurationHolder);
    group.add(themeAction);
    group.addSeparator();

    return group;
  }

  public PsiFile getFile() {
    return myPreviewWindow.getFile();
  }

  @Nullable
  public RenderConfiguration getConfiguration() {
    return myPreviewWindow.getConfiguration();
  }
}
