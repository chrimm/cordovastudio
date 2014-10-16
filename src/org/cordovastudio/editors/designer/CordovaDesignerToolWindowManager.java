/*
 * Copyright 2000-2012 JetBrains s.r.o.
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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Alexander Lobas
 */
public final class CordovaDesignerToolWindowManager extends AbstractToolWindowManager {
  private final CordovaDesignerToolWindow myToolWindowContent;

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Public Access
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  public CordovaDesignerToolWindowManager(Project project, FileEditorManager fileEditorManager) {
    super(project, fileEditorManager);
    myToolWindowContent = new CordovaDesignerToolWindow(project, true);
  }

  public static CordovaDesignerToolWindow getInstance(CordovaDesignerEditorPanel designer) {
    CordovaDesignerToolWindowManager manager = getInstance(designer.getProject());
    if (manager.isEditorMode()) {
      return (CordovaDesignerToolWindow)manager.getContent(designer);
    }
    return manager.myToolWindowContent;
  }


  public static CordovaDesignerToolWindowManager getInstance(Project project) {
    return project.getComponent(CordovaDesignerToolWindowManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Impl
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void initToolWindow() {
    myToolWindow = ToolWindowManager.getInstance(myProject).registerToolWindow(CordovaDesignerBundle.message("designer.toolwindow.name"),
                                                                               false, getAnchor(), myProject, true);
    myToolWindow.setIcon(CordovaIcons.Designer.ToolWindow);

    if (!ApplicationManager.getApplication().isHeadlessEnvironment()) {
      myToolWindow.getComponent().putClientProperty(ToolWindowContentUi.HIDE_ID_LABEL, "true");
    }

    ((ToolWindowEx)myToolWindow).setTitleActions(myToolWindowContent.createActions());
    initGearActions();

    ContentManager contentManager = myToolWindow.getContentManager();
    Content content =
      contentManager.getFactory()
        .createContent(myToolWindowContent.getToolWindowPanel(), CordovaDesignerBundle.message("designer.toolwindow.title"), false);
    content.setCloseable(false);
    content.setPreferredFocusableComponent(myToolWindowContent.getComponentTree());
    contentManager.addContent(content);
    contentManager.setSelectedContent(content, true);
    myToolWindow.setAvailable(false, null);
  }

  @Override
  protected ToolWindowAnchor getAnchor() {
    DesignerCustomizations customization = getCustomizations();
    return customization != null ? customization.getStructureAnchor() : ToolWindowAnchor.LEFT;
  }

  @Override
  protected void updateToolWindow(@Nullable CordovaDesignerEditorPanel designer) {
    myToolWindowContent.update(designer);

    if (designer == null) {
      myToolWindow.setAvailable(false, null);
    }
    else {
      myToolWindow.setAvailable(true, null);
      myToolWindow.show(null);
    }
  }

  @Override
  public void disposeComponent() {
    myToolWindowContent.dispose();
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "CordovaDesignerToolWindowManager";
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Impl
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected LightToolWindow createContent(CordovaDesignerEditorPanel designer) {
    CordovaDesignerToolWindow toolWindowContent = new CordovaDesignerToolWindow(myProject, false);
    toolWindowContent.update(designer);

    return createContent(designer,
                         toolWindowContent,
                         CordovaDesignerBundle.message("designer.toolwindow.title"),
                         CordovaIcons.Designer.ToolWindow,
                         toolWindowContent.getToolWindowPanel(),
                         toolWindowContent.getComponentTree(),
                         320,
                         toolWindowContent.createActions());
  }
}