/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package org.cordovastudio.editors.designer.palette;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.cordovastudio.editors.designer.AbstractToolWindowManager;
import org.cordovastudio.editors.designer.CordovaDesignerBundle;
import org.cordovastudio.editors.designer.DesignerCustomizations;
import org.cordovastudio.editors.designer.LightToolWindow;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Lobas
 */
public class CordovaPaletteToolWindowManager extends AbstractToolWindowManager {
  private final CordovaPalettePanel myToolWindowPanel = new CordovaPalettePanel();

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Public Access
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  public CordovaPaletteToolWindowManager(Project project, FileEditorManager fileEditorManager) {
    super(project, fileEditorManager);
  }

  public static CordovaPalettePanel getInstance(CordovaDesignerEditorPanel designer) {
    CordovaPaletteToolWindowManager manager = getInstance(designer.getProject());
    if (manager.isEditorMode()) {
      return (CordovaPalettePanel)manager.getContent(designer);
    }
    return manager.myToolWindowPanel;
  }

  public static CordovaPaletteToolWindowManager getInstance(Project project) {
    return project.getComponent(CordovaPaletteToolWindowManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Impl
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void initToolWindow() {
    myToolWindow = ToolWindowManager.getInstance(myProject).registerToolWindow(CordovaDesignerBundle.message("designer.palette.name"), false, getAnchor(), myProject, true);
    myToolWindow.setIcon(AllIcons.Toolwindows.ToolWindowPalette);
    initGearActions();

    ContentManager contentManager = myToolWindow.getContentManager();
    Content content = contentManager.getFactory().createContent(myToolWindowPanel, null, false);
    content.setCloseable(false);
    content.setPreferredFocusableComponent(myToolWindowPanel);
    contentManager.addContent(content);
    contentManager.setSelectedContent(content, true);
    myToolWindow.setAvailable(false, null);
  }

  @Override
  protected ToolWindowAnchor getAnchor() {
    DesignerCustomizations customization = getCustomizations();
    return customization != null ? customization.getPaletteAnchor() : ToolWindowAnchor.RIGHT;
  }

  @Override
  protected void updateToolWindow(@Nullable CordovaDesignerEditorPanel designer) {
    myToolWindowPanel.loadPalette(designer);

    if (myToolWindowPanel.isEmpty()) {
      myToolWindow.setAvailable(false, null);
    }
    else {
      myToolWindow.setAvailable(true, null);
      myToolWindow.show(null);
    }
  }

  @Override
  public void disposeComponent() {
    myToolWindowPanel.dispose();
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "CordovaPaletteToolWindowManager";
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Impl
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected LightToolWindow createContent(CordovaDesignerEditorPanel designer) {
    CordovaPalettePanel palettePanel = new CordovaPalettePanel();
    palettePanel.loadPalette(designer);

    return createContent(designer,
                         palettePanel,
                         "Palette",
                         AllIcons.Toolwindows.ToolWindowPalette,
                         palettePanel,
                         palettePanel,
                         180,
                         null);
  }
}