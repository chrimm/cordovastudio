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

package org.cordovastudio.editors.storyboard;

import com.intellij.AppTopics;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBScrollPane;
import org.cordovastudio.actions.ShowStoryboardEditorAction;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.storyboard.io.XMLReader;
import org.cordovastudio.editors.storyboard.io.XMLWriter;
import org.cordovastudio.editors.storyboard.macros.Analyser;
import org.cordovastudio.editors.storyboard.model.*;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static org.cordovastudio.editors.storyboard.StoryboardView.GAP;
import static org.cordovastudio.editors.storyboard.model.CordovaStoryboardModel.Event.Operation;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class CordovaStoryboardEditor implements FileEditor {
    private static final String TOOLBAR = "SoryboardEditorToolbar";
    private static final Logger LOG = Logger.getInstance("#" + CordovaStoryboardEditor.class.getName());
    private static final boolean DEBUG = false;
    private static final String NAME = "Storyboard";
    private static final int INITIAL_FILE_BUFFER_SIZE = 1000;
    private static final int SCROLL_UNIT_INCREMENT = 20;
    private static final CordovaStoryboardModel.Event PROJECT_READ = new CordovaStoryboardModel.Event(Operation.UPDATE, Object.class);

    private final UserDataHolderBase myUserDataHolder = new UserDataHolderBase();
    @Nullable
    private RenderingParameters myRenderingParams;
    private CordovaStoryboardModel myStoryboardModel;
    private final VirtualFile myFile;
    private JComponent myComponent;
    private boolean myModified;
    private boolean myPendingFileSystemChanges;
    private Analyser myAnalyser;
    private final Listener<CordovaStoryboardModel.Event> myNavigationModelListener;
    private VirtualFileAdapter myVirtualFileListener;
    private final ErrorHandler myErrorHandler;

    public CordovaStoryboardEditor(Project project, VirtualFile file) {
        // Listen for 'Save All' events
        FileDocumentManagerListener saveListener = new FileDocumentManagerAdapter() {
            @Override
            public void beforeAllDocumentsSaving() {
                try {
                    saveFile();
                } catch (IOException e) {
                    LOG.error("Unexpected exception while saving navigation file", e);
                }
            }
        };
        project.getMessageBus().connect(this).subscribe(AppTopics.FILE_DOCUMENT_SYNC, saveListener);
        myFile = file;
        myErrorHandler = new ErrorHandler();
        myRenderingParams = StoryboardView.getRenderingParams(project, file, myErrorHandler);
        if (myRenderingParams != null) {
            RenderConfiguration configuration = myRenderingParams.myConfiguration;
            Module module = configuration.getModule();
            myAnalyser = new Analyser(project, module);
            myStoryboardModel = myAnalyser.createModel();

            //myCodeGenerator = new CodeGenerator(myStoryboardModel, module);

            StoryboardView editor = new StoryboardView(myRenderingParams, myStoryboardModel);
            JBScrollPane scrollPane = new JBScrollPane(editor);
            scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
            JPanel p = new JPanel(new BorderLayout());

            JComponent controls = createToolbar(editor);
            p.add(controls, BorderLayout.NORTH);
            p.add(scrollPane);
            myComponent = p;
        }
        myNavigationModelListener = new Listener<CordovaStoryboardModel.Event>() {
            @Override
            public void notify(@NotNull CordovaStoryboardModel.Event event) {
                if (event.operation == Operation.INSERT && event.operandType == Transition.class) {
                    ArrayList<Transition> transitions = myStoryboardModel.getTransitions();
                    Transition transition = transitions.get(transitions.size() - 1); // todo don't rely on this being the last
                }
                if (event != PROJECT_READ) { // exempt the case when we are updating the model ourselves (because of a file read)
                    myModified = true;
                }
            }
        };
        myStoryboardModel.getListeners().add(myNavigationModelListener);
        myVirtualFileListener = new VirtualFileAdapter() {
            private void somethingChanged(String changeType, @NotNull VirtualFileEvent event) {
                if (DEBUG) System.out.println("NavigationEditor: fileListener:: " + changeType + ": " + event);
                postDelayedRefresh();
            }

            @Override
            public void contentsChanged(@NotNull VirtualFileEvent event) {
                somethingChanged("contentsChanged", event);
            }

            @Override
            public void fileCreated(@NotNull VirtualFileEvent event) {
                somethingChanged("fileCreated", event);
            }

            @Override
            public void fileDeleted(@NotNull VirtualFileEvent event) {
                somethingChanged("fileDeleted", event);
            }
        };
    }

    public class ErrorHandler {
        public void handleError(String title, String errorMessage) {
            myStoryboardModel = new CordovaStoryboardModel();
            {
                JPanel panel = new JPanel(new BorderLayout());
                {
                    JLabel label = new JLabel(title);
                    label.setFont(label.getFont().deriveFont(30f));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    panel.add(label, BorderLayout.NORTH);
                }
                {
                    JLabel label = new JLabel(errorMessage);
                    label.setFont(label.getFont().deriveFont(20f));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    panel.add(label, BorderLayout.CENTER);
                }
                myComponent = new JBScrollPane(panel);
            }
        }
    }

    private void postDelayedRefresh() {
        if (DEBUG) System.out.println("NavigationEditor: postDelayedRefresh");
        // Post to the event queue to coalesce events and effect re-parse when they're all in
        if (!myPendingFileSystemChanges) {
            myPendingFileSystemChanges = true;
            final Application app = ApplicationManager.getApplication();
            app.invokeLater(new Runnable() {
                @Override
                public void run() {
                    app.executeOnPooledThread(new Runnable() {
                        @Override
                        public void run() {
                            app.runReadAction(new Runnable() {
                                @Override
                                public void run() {
                                    myPendingFileSystemChanges = false;
                                    long l = System.currentTimeMillis();
                                    updateNavigationModelFromProject();
                                    if (DEBUG)
                                        System.out.println("Navigation Editor: model read took: " + (System.currentTimeMillis() - l) / 1000.0);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    // See  AndroidDesignerActionPanel

    protected JComponent createToolbar(StoryboardView myDesigner) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM));
        // the UI below is a temporary hack to show UX / dev. rel
        {
            final String dirName = myFile.getParent().getName();

            JPanel combos = new JPanel(new FlowLayout());
            //combos.add(new JLabel(dirName));
            {
                final String phone = "phone";
                final String tablet = "tablet";
                final ComboBox deviceSelector = new ComboBox(new Object[]{phone, tablet});
                final String portrait = "portrait";
                final String landscape = "landscape";
                final ComboBox orientationSelector = new ComboBox(new Object[]{portrait, landscape});
                deviceSelector.setSelectedItem(dirName.contains("-sw600dp") ? tablet : phone);
                orientationSelector.setSelectedItem(dirName.contains("-land") ? landscape : portrait);
                ActionListener actionListener = new ActionListener() {
                    boolean disabled = false;

                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (disabled) {
                            return;
                        }
                        Object device = deviceSelector.getSelectedItem();
                        Object deviceQualifier = (device == tablet) ? "-sw600dp" : "";
                        Object orientation = orientationSelector.getSelectedItem();
                        Object orientationQualifier = (orientation == landscape) ? "-land" : "";
                        new ShowStoryboardEditorAction()
                                .showNavigationEditor(myRenderingParams.myProject, "raw" + deviceQualifier + orientationQualifier, "main.nvg.xml");
                        disabled = true;
                        deviceSelector.setSelectedItem(dirName.contains("-sw600dp") ? tablet : phone);
                        orientationSelector.setSelectedItem(dirName.contains("-land") ? landscape : portrait);
                        disabled = false;

                    }
                };
                {
                    deviceSelector.addActionListener(actionListener);
                    combos.add(deviceSelector);
                }
                {
                    orientationSelector.addActionListener(actionListener);
                    combos.add(orientationSelector);
                }
            }
            panel.add(combos, BorderLayout.CENTER);
        }

        {
            ActionManager actionManager = ActionManager.getInstance();
            ActionToolbar zoomToolBar = actionManager.createActionToolbar(TOOLBAR, getActions(myDesigner), true);
            panel.add(zoomToolBar.getComponent(), BorderLayout.WEST);
            {
                HyperlinkLabel label = new HyperlinkLabel();
                label.setHyperlinkTarget("http://tools.android.com/navigation-editor");
                label.setHyperlinkText(" ", "What's this?", " ");
                panel.add(label, BorderLayout.EAST);
            }
        }

        return panel;
    }

    private static class FileReadException extends Exception {
        private FileReadException(Throwable throwable) {
            super(throwable);
        }
    }

    // See AndroidDesignerActionPanel
    private static ActionGroup getActions(final StoryboardView myDesigner) {
        DefaultActionGroup group = new DefaultActionGroup();

        group.add(new AnAction(null, "Zoom Out (-)", CordovaIcons.Toolbar.ZoomOut) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                myDesigner.zoom(false);
            }
        });
        group.add(new AnAction(null, "Reset Zoom to 100% (1)", CordovaIcons.Toolbar.ZoomActual) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                myDesigner.setScale(1);
            }
        });
        group.add(new AnAction(null, "Zoom In (+)", CordovaIcons.Toolbar.ZoomIn) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                myDesigner.zoom(true);
            }
        });

        return group;
    }

    private static CordovaStoryboardModel read(VirtualFile file) throws FileReadException {
        try {
            InputStream inputStream = file.getInputStream();
            if (inputStream.available() == 0) {
                return new CordovaStoryboardModel();
            }
            return (CordovaStoryboardModel) new XMLReader(inputStream).read();
        } catch (Exception e) {
            throw new FileReadException(e);
        }
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
    }

    @Override
    public boolean isModified() {
        return myModified;
    }

    @Override
    public boolean isValid() {
        return myFile.isValid();
    }

    private void layoutStatesWithUnsetLocations(CordovaStoryboardModel navigationModel) {
        Collection<State> states = navigationModel.getStates();
        final Map<State, org.cordovastudio.editors.storyboard.model.Point> stateToLocation = navigationModel.getStateToLocation();
        final Set<State> visited = new HashSet<State>();
        org.cordovastudio.editors.storyboard.model.Dimension size = myRenderingParams.getDeviceScreenSize();
        org.cordovastudio.editors.storyboard.model.Dimension gridSize = new org.cordovastudio.editors.storyboard.model.Dimension(size.width + GAP.width, size.height + GAP.height);
        final Point location = new Point(GAP.width, GAP.height);
        final int gridWidth = gridSize.width;
        final int gridHeight = gridSize.height;
        for (State state : states) {
            if (visited.contains(state)) {
                continue;
            }
            new Object() {
                public void addChildrenFor(State source) {
                    visited.add(source);
                    if (!stateToLocation.containsKey(source)) {
                        stateToLocation.put(source, new org.cordovastudio.editors.storyboard.model.Point(location.x, location.y));
                    }
                    List<State> children = findDestinationsFor(source, visited);
                    location.x += gridWidth;
                    if (children.isEmpty()) {
                        location.y += gridHeight;
                    } else {
                        for (State child : children) {
                            addChildrenFor(child);
                        }
                    }
                    location.x -= gridWidth;
                }
            }.addChildrenFor(state);
        }
    }

    private List<State> findDestinationsFor(State source, Set<State> visited) {
        java.util.List<State> result = new ArrayList<State>();
        for (Transition transition : myStoryboardModel.getTransitions()) {
            if (transition.getSource().getState() == source) {
                State destination = transition.getDestination().getState();
                if (!visited.contains(destination)) {
                    result.add(destination);
                }
            }
        }
        return result;
    }

    private void updateNavigationModelFromProject() {
        if (DEBUG) System.out.println("NavigationEditor: updateNavigationModelFromProject...");
        if (myRenderingParams == null || myRenderingParams.myProject.isDisposed()) {
            return;
        }
        EventDispatcher<CordovaStoryboardModel.Event> listeners = myStoryboardModel.getListeners();
        boolean notificationWasEnabled = listeners.isNotificationEnabled();
        listeners.setNotificationEnabled(false);
        myAnalyser.updateModel(myStoryboardModel);
        layoutStatesWithUnsetLocations(myStoryboardModel);
        listeners.setNotificationEnabled(notificationWasEnabled);

        myModified = false;
        listeners.notify(PROJECT_READ);
    }

    @Override
    public void selectNotify() {
        if (myRenderingParams != null) {
            CordovaFacet facet = myRenderingParams.myFacet;
            updateNavigationModelFromProject();
            VirtualFileManager.getInstance().addVirtualFileListener(myVirtualFileListener);
            //getResourceFolderManager(facet).addListener(myResourceFolderListener);
        }
    }

    @Override
    public void deselectNotify() {
        if (myRenderingParams != null) {
            CordovaFacet facet = myRenderingParams.myFacet;
            VirtualFileManager.getInstance().removeVirtualFileListener(myVirtualFileListener);
            //getResourceFolderManager(facet).removeListener(myResourceFolderListener);
        }
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null;
    }

    private void saveFile() throws IOException {
        if (myModified) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(INITIAL_FILE_BUFFER_SIZE);
            new XMLWriter(stream).write(myStoryboardModel);
            myFile.setBinaryContent(stream.toByteArray());
            myModified = false;
        }
    }

    @Override
    public void dispose() {
        try {
            saveFile();
        } catch (IOException e) {
            LOG.error("Unexpected exception while saving navigation file", e);
        }

        myStoryboardModel.getListeners().remove(myNavigationModelListener);
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return myUserDataHolder.getUserData(key);
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        myUserDataHolder.putUserData(key, value);
    }
}
