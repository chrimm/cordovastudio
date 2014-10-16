/*
 * Copyright (C) 2013 The Android Open Source Project
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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

abstract class ConfigurationAction extends AnAction implements RenderConfigurationListener {
  private static final String FILE_ARROW = " \u2192 ";
  protected final RenderContext myRenderContext;
  private int myFlags;

  public ConfigurationAction(@NotNull RenderContext renderContext, @NotNull String title) {
    this(renderContext, title, null);
  }

  public ConfigurationAction(@NotNull RenderContext renderContext, @NotNull String title, @Nullable Icon icon) {
    super(title, null, icon);
    myRenderContext = renderContext;
  }

  protected void updatePresentation() {
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    tryUpdateConfiguration();
    updatePresentation();
    myRenderContext.requestRender();
  }

  protected void tryUpdateConfiguration() {
    RenderConfiguration configuration = myRenderContext.getConfiguration();
    if (configuration != null) {
      updateConfiguration(configuration, true /*commit*/);
    }
  }

  @Override
  public boolean changed(int flags) {
    myFlags |= flags;
    return true;
  }

  protected abstract void updateConfiguration(@NotNull RenderConfiguration configuration, boolean commit);

  public static boolean isBetterMatchLabel(@NotNull String label) {
    return label.contains(FILE_ARROW);
  }
}
