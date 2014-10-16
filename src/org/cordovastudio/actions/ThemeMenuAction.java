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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.dialogs.ThemeSelectionDialog;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThemeMenuAction extends FlatAction {
  private final RenderContext myRenderContext;

  public ThemeMenuAction(@NotNull RenderContext renderContext) {
    myRenderContext = renderContext;
    Presentation presentation = getTemplatePresentation();
    presentation.setDescription("Theme to render layout with");
    presentation.setIcon(CordovaIcons.Toolbar.ChooseTheme);
    updatePresentation(presentation);
  }

  @Override
  public void update(AnActionEvent e) {
    super.update(e);
    updatePresentation(e.getPresentation());
  }

  @Override
  public boolean displayTextInToolbar() {
    return true;
  }

  private void updatePresentation(Presentation presentation) {
    RenderConfiguration configuration = myRenderContext.getConfiguration();
    boolean visible = configuration != null;
    if (visible) {
      String brief = getThemeLabel(configuration.getTheme(), true);
      presentation.setText(brief, false);
      presentation.setDescription(getThemeLabel(configuration.getTheme(), false));
    }
    if (visible != presentation.isVisible()) {
      presentation.setVisible(visible);
    }
  }

  /**
   * Returns a suitable label to use to display the given theme
   *
   * @param theme the theme to produce a label for
   * @param brief if true, generate a brief label (suitable for a toolbar
   *              button), otherwise a fuller name (suitable for a menu item)
   * @deprecated Function seems unnecessary and may be deleted
   * @return the label
   */
  @NotNull
  @Deprecated
  public static String getThemeLabel(@Nullable String theme, boolean brief) {
    if (theme == null) {
      return "";
    }

    if (brief) {
      int index = theme.lastIndexOf('.');
      if (index < theme.length() - 1) {
        return theme.substring(index + 1);
      }
    }
    return theme;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    RenderConfiguration configuration = myRenderContext.getConfiguration();
    if (configuration != null) {
      ThemeSelectionDialog dialog = new ThemeSelectionDialog(configuration);
      dialog.show();
      if (dialog.isOK()) {
        String theme = dialog.getTheme();
        if (theme != null) {
          configuration.setTheme(theme);
          myRenderContext.requestRender();
        }
      }
    }
  }
}
