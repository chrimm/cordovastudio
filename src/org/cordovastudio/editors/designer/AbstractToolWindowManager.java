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

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.util.ParameterizedRunnable;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import org.cordovastudio.editors.designer.actions.ToggleEditorModeAction;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Alexander Lobas
 */
public abstract class AbstractToolWindowManager implements ProjectComponent {
  public static final String EDITOR_MODE = "UI_DESIGNER_EDITOR_MODE.";

  private final MergingUpdateQueue myWindowQueue = new MergingUpdateQueue(getComponentName(), 200, true, null);
  protected final Project myProject;
  protected final FileEditorManager myFileEditorManager;
  protected volatile ToolWindow myToolWindow;
  private volatile boolean myToolWindowReady;
  private volatile boolean myToolWindowDisposed;

  private final PropertiesComponent myPropertiesComponent;
  public final String myEditorModeKey;
  private ToggleEditorModeAction myLeftEditorModeAction;
  private ToggleEditorModeAction myRightEditorModeAction;

  private MessageBusConnection myConnection;
  private final FileEditorManagerListener myListener = new FileEditorManagerListener() {
    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
      bindToDesigner(getActiveDesigner());
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        @Override
        public void run() {
          bindToDesigner(getActiveDesigner());
        }
      });
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
      bindToDesigner(getDesigner(event.getNewEditor()));
    }
  };

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // ToolWindow
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  protected AbstractToolWindowManager(Project project, FileEditorManager fileEditorManager) {
    myProject = project;
    myFileEditorManager = fileEditorManager;
    myPropertiesComponent = PropertiesComponent.getInstance(myProject);
    myEditorModeKey = EDITOR_MODE + getComponentName() + ".STATE";
  }

  @Override
  public void projectOpened() {
    initToolWindow();

    StartupManager.getInstance(myProject).registerPostStartupActivity(new Runnable() {
      public void run() {
        myToolWindowReady = true;
        if (getEditorMode() == null) {
          initListeners();
          bindToDesigner(getActiveDesigner());
        }
      }
    });
  }

  @Override
  public void projectClosed() {
    if (!myToolWindowDisposed) {
      disposeComponent();
      myToolWindowDisposed = true;
      myToolWindow = null;
    }
  }

  private void initListeners() {
    myConnection = myProject.getMessageBus().connect(myProject);
    myConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, myListener);
  }

  private void removeListeners() {
    myConnection.disconnect();
    myConnection = null;
  }

  @Nullable
  private static CordovaDesignerEditorPanel getDesigner(FileEditor editor) {
    if (editor instanceof CordovaDesignerEditor) {
        CordovaDesignerEditor designerEditor = (CordovaDesignerEditor)editor;
      return designerEditor.getDesignerPanel();
    }
    return null;
  }

  @Nullable
  public CordovaDesignerEditorPanel getActiveDesigner() {
    for (FileEditor editor : myFileEditorManager.getSelectedEditors()) {
        CordovaDesignerEditorPanel designer = getDesigner(editor);
      if (designer != null) {
        return designer;
      }
    }

    return null;
  }

  @Nullable
  protected static DesignerCustomizations getCustomizations() {
    return DesignerCustomizations.EP_NAME.findExtension(DesignerCustomizations.class);
  }

  private void bindToDesigner(final CordovaDesignerEditorPanel designer) {
    myWindowQueue.cancelAllUpdates();
    myWindowQueue.queue(new Update("update") {
      @Override
      public void run() {
        if (!myToolWindowReady || myToolWindowDisposed) {
          return;
        }
        if (myToolWindow == null) {
          if (designer == null) {
            return;
          }
          initToolWindow();
        }
        updateToolWindow(designer);
      }
    });
  }

  protected abstract void initToolWindow();

  protected abstract void updateToolWindow(@Nullable CordovaDesignerEditorPanel designer);

  protected final void initGearActions() {
    ToolWindowEx toolWindow = (ToolWindowEx)myToolWindow;
    toolWindow.setAdditionalGearActions(new DefaultActionGroup(createGearActions()));
  }

  protected abstract ToolWindowAnchor getAnchor();

  @Override
  public void initComponent() {
  }

  @Override
  public void disposeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // LightToolWindow
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  public final ActionGroup createGearActions() {
    DefaultActionGroup group = new DefaultActionGroup("In Editor Mode", true);

    if (myLeftEditorModeAction == null) {
      myLeftEditorModeAction = new ToggleEditorModeAction(this, myProject, ToolWindowAnchor.LEFT);
    }
    group.add(myLeftEditorModeAction);

    if (myRightEditorModeAction == null) {
      myRightEditorModeAction = new ToggleEditorModeAction(this, myProject, ToolWindowAnchor.RIGHT);
    }
    group.add(myRightEditorModeAction);

    return group;
  }

  public final void bind(CordovaDesignerEditorPanel designer) {
    if (isEditorMode()) {
      myCreateAction.run(designer);
    }
  }

  public final void dispose(CordovaDesignerEditorPanel designer) {
    if (isEditorMode()) {
      disposeContent(designer);
    }
  }

  protected final Object getContent(CordovaDesignerEditorPanel designer) {
    LightToolWindow toolWindow = (LightToolWindow)designer.getClientProperty(getComponentName());
    return toolWindow.getContent();
  }

  protected abstract LightToolWindow createContent(CordovaDesignerEditorPanel designer);

  protected final LightToolWindow createContent(CordovaDesignerEditorPanel designer,
                                                LightToolWindowContent content,
                                                String title,
                                                Icon icon,
                                                JComponent component,
                                                JComponent focusedComponent,
                                                int defaultWidth,
                                                AnAction[] actions) {
    return new LightToolWindow(content,
                               title,
                               icon,
                               component,
                               focusedComponent,
                               designer.getContentSplitter(),
                               getEditorMode(),
                               this,
                               myProject,
                               myPropertiesComponent,
                               getComponentName(),
                               defaultWidth,
                               actions);
  }

  protected final void disposeContent(CordovaDesignerEditorPanel designer) {
    String key = getComponentName();
    LightToolWindow toolWindow = (LightToolWindow)designer.getClientProperty(key);
    designer.putClientProperty(key, null);
    toolWindow.dispose();
  }

  private final ParameterizedRunnable<CordovaDesignerEditorPanel> myCreateAction = new ParameterizedRunnable<CordovaDesignerEditorPanel>() {
    @Override
    public void run(CordovaDesignerEditorPanel designer) {
      designer.putClientProperty(getComponentName(), createContent(designer));
    }
  };

  private final ParameterizedRunnable<CordovaDesignerEditorPanel> myUpdateAnchorAction = new ParameterizedRunnable<CordovaDesignerEditorPanel>() {
    @Override
    public void run(CordovaDesignerEditorPanel designer) {
      LightToolWindow toolWindow = (LightToolWindow)designer.getClientProperty(getComponentName());
      toolWindow.updateAnchor(getEditorMode());
    }
  };

  private final ParameterizedRunnable<CordovaDesignerEditorPanel> myDisposeAction = new ParameterizedRunnable<CordovaDesignerEditorPanel>() {
    @Override
    public void run(CordovaDesignerEditorPanel designer) {
      disposeContent(designer);
    }
  };

  private void runUpdateContent(ParameterizedRunnable<CordovaDesignerEditorPanel> action) {
    for (FileEditor editor : myFileEditorManager.getAllEditors()) {
        CordovaDesignerEditorPanel designer = getDesigner(editor);
      if (designer != null) {
        action.run(designer);
      }
    }
  }

  protected final boolean isEditorMode() {
    return getEditorMode() != null;
  }

  @Nullable
  public final ToolWindowAnchor getEditorMode() {
    String value = myPropertiesComponent.getValue(myEditorModeKey);
    if (value == null) {
      return getAnchor();
    }
    return value.equals("ToolWindow") ? null : ToolWindowAnchor.fromText(value);
  }

  public final void setEditorMode(@Nullable ToolWindowAnchor newState) {
    ToolWindowAnchor oldState = getEditorMode();
    myPropertiesComponent.setValue(myEditorModeKey, newState == null ? "ToolWindow" : newState.toString());

    if (oldState != null && newState != null) {
      runUpdateContent(myUpdateAnchorAction);
    }
    else if (newState != null) {
      removeListeners();
      updateToolWindow(null);
      runUpdateContent(myCreateAction);
    }
    else {
      runUpdateContent(myDisposeAction);
      initListeners();
      bindToDesigner(getActiveDesigner());
    }
  }

  final ToolWindow getToolWindow() {
    return myToolWindow;
  }
}