/*
 * Copyright (C) 2014 The Android Open Source Project
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

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.CheckboxAction;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.cordovastudio.editors.designer.designSurface.preview.PreviewToolWindowSettings;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.jetbrains.annotations.NotNull;

/**
 * Builder which creates a toolbar with render options, such as the ability to turn off device frame rendering
 * @see PreviewToolWindowSettings
 */
public class RenderOptionsMenuBuilder {
  final DefaultActionGroup myGroup;
  final PreviewToolWindowSettings mySettings;
  private final RenderContext myContext;

  private RenderOptionsMenuBuilder(@NotNull final RenderContext context, @NotNull Project project) {
    myGroup = new DefaultActionGroup();
    mySettings = PreviewToolWindowSettings.getInstance(project);
    myContext = context;
  }

  public static RenderOptionsMenuBuilder create(@NotNull final RenderContext context, @NotNull Project project) {
    return new RenderOptionsMenuBuilder(context, project);
  }

  @NotNull
  public RenderOptionsMenuBuilder addHideOption() {
    myGroup.addAction(new CheckboxAction("Hide for non-layout files") {
      @Override
      public boolean isSelected(AnActionEvent e) {
        return mySettings.getGlobalState().isHideForNonLayoutFiles();
      }

      @Override
      public void setSelected(AnActionEvent e, boolean state) {
        mySettings.getGlobalState().setHideForNonLayoutFiles(state);
      }
    }).setAsSecondary(true);

    return this;
  }

  @NotNull
  public RenderOptionsMenuBuilder addDeviceFrameOption() {
    myGroup.addAction(new CheckboxAction("Include Device Frames (if available)") {
      @Override
      public boolean isSelected(AnActionEvent e) {
        return mySettings.getGlobalState().isShowDeviceFrames();
      }

      @Override
      public void setSelected(AnActionEvent e, boolean state) {
        mySettings.getGlobalState().setShowDeviceFrames(state);
        myContext.requestRender();
      }
    }).setAsSecondary(true);
    // Indented as related option
    myGroup.addAction(new CheckboxAction("    Show Lighting Effects") {
      @Override
      public boolean isSelected(AnActionEvent e) {
        return mySettings.getGlobalState().isShowEffects();
      }

      @Override
      public void setSelected(AnActionEvent e, boolean state) {
        mySettings.getGlobalState().setShowEffects(state);
        myContext.requestRender();
      }
    }).setAsSecondary(true);

    return this;
  }

  // TODO: Remove when Retina support is complete (and has been field tested a bit)
  @NotNull
  public RenderOptionsMenuBuilder addRetinaOption() {
    if (UIUtil.isRetina()) {
      myGroup.addAction(new CheckboxAction("DEBUG: Render in Retina resolution") {
        @Override
        public boolean isSelected(AnActionEvent e) {
          return mySettings.getGlobalState().isRetina();
        }

        @Override
        public void setSelected(AnActionEvent e, boolean state) {
          mySettings.getGlobalState().setRetina(state);
          myContext.requestRender();
        }
      }).setAsSecondary(true);
    }

    return this;
  }

  @NotNull
  public ActionToolbar build() {
    final ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, myGroup, true);
    toolbar.setReservePlaceAutoPopupIcon(false);
    toolbar.setSecondaryActionsTooltip("Options");
    return toolbar;
  }
}
