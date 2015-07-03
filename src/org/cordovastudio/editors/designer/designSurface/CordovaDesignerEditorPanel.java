/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (as of com.intellij.android.designer.designSurface.AndroidDesignerEditorPanel
 *  and com.intellij.designer.designSurface)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged AndroidDesignerEditorPanel and DesignerEditorPanel
 *  – Adopted Rendering and other parts to use with Cordova HTML Files
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

package org.cordovastudio.editors.designer.designSurface;

import com.intellij.designer.model.Property;
import com.intellij.designer.propertyTable.InplaceContext;
import com.intellij.diagnostic.AttachmentFactory;
import com.intellij.diagnostic.LogMessageEx;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ThreeComponentsSplitter;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBLayeredPane;
import com.intellij.util.*;
import com.intellij.util.containers.IntArrayList;
import com.intellij.util.ui.AsyncProcessIcon;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import org.cordovastudio.editors.designer.*;
import org.cordovastudio.editors.designer.actions.AbstractComboBoxAction;
import org.cordovastudio.editors.designer.actions.CommonEditActionsProvider;
import org.cordovastudio.editors.designer.clipboard.CordovaPasteFactory;
import org.cordovastudio.editors.designer.componentTree.CordovaTreeDecorator;
import org.cordovastudio.editors.designer.componentTree.TreeComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.ComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.EmptyComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.editableArea.ComponentEditableArea;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.designSurface.editableArea.TreeEditableArea;
import org.cordovastudio.editors.designer.designSurface.layers.DecorationLayer;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.layers.GlassLayer;
import org.cordovastudio.editors.designer.designSurface.layers.InplaceEditingLayer;
import org.cordovastudio.editors.designer.designSurface.operations.EditOperation;
import org.cordovastudio.editors.designer.designSurface.preview.RenderPreviewManager;
import org.cordovastudio.editors.designer.designSurface.tools.*;
import org.cordovastudio.editors.designer.model.*;
import org.cordovastudio.editors.designer.palette.CordovaPaletteToolWindowContent;
import org.cordovastudio.editors.designer.palette.CordovaPaletteToolWindowManager;
import org.cordovastudio.editors.designer.palette.PaletteGroup;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.cordovastudio.editors.designer.propertyTable.PropertyTableTab;
import org.cordovastudio.editors.designer.propertyTable.TablePanelActionPolicy;
import org.cordovastudio.editors.designer.rendering.*;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationToolBar;
import org.cordovastudio.modules.CordovaFacet;
import org.cordovastudio.utils.CordovaPsiUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.cordovastudio.editors.designer.rendering.RenderErrorPanel.SIZE_ERROR_PANEL_DYNAMICALLY;
import static org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener.MASK_ALL;

/**
 * Created by cti on 27.08.14.
 */
public class CordovaDesignerEditorPanel extends JPanel
        implements DataProvider, ModuleProvider, RadPropertyContext, RenderContext, OverlayContainer {

    private static final Logger LOG = Logger.getInstance(CordovaDesignerEditorPanel.class);

    protected static final Integer LAYER_COMPONENT = JLayeredPane.DEFAULT_LAYER;
    protected static final Integer LAYER_DECORATION = JLayeredPane.POPUP_LAYER;
    protected static final Integer LAYER_FEEDBACK = JLayeredPane.DRAG_LAYER;
    protected static final Integer LAYER_GLASS = LAYER_FEEDBACK + 100;
    protected static final Integer LAYER_INPLACE_EDITING = LAYER_GLASS + 100;
    private static final Integer LAYER_PROGRESS = LAYER_INPLACE_EDITING + 100;
    private static final Integer LAYER_ERRORS = LAYER_INPLACE_EDITING + 150; // Must be an Integer, not an int; see JLayeredPane.addImpl
    private static final Integer LAYER_PREVIEW = LAYER_INPLACE_EDITING + 170; // Must be an Integer, not an int; see JLayeredPane.addImpl

    private static final int DEFAULT_HORIZONTAL_MARGIN = 30;
    private static final int DEFAULT_VERTICAL_MARGIN = 20;

    private final static String DESIGNER_CARD = "designer";
    private final static String ERROR_CARD = "error";
    private final static String ERROR_STACK_CARD = "stack";
    private final static String ERROR_NO_STACK_CARD = "no_stack";

    /**
     * General
     */
    private boolean myActive;
    private final IdManager myIdManager = new IdManager();

    /**
     * Editor Panels
     */
    private final CordovaDesignerEditor myEditor;
    private final Project myProject;
    private Module myModule;
    protected final VirtualFile myFile;

    private final CardLayout myLayout = new CardLayout();
    private final ThreeComponentsSplitter myContentSplitter = new ThreeComponentsSplitter();
    private final JPanel myPanel = new JPanel(myLayout);
    private JPanel myDesignerCard;

    protected CordovaDesignerActionPanel myActionPanel;

    protected CaptionPanel myHorizontalCaption;
    protected CaptionPanel myVerticalCaption;

    protected JScrollPane myScrollPane;
    protected JLayeredPane myLayeredPane;
    protected GlassLayer myGlassLayer;
    private DecorationLayer myDecorationLayer;
    private FeedbackLayer myFeedbackLayer;
    private InplaceEditingLayer myInplaceEditingLayer;

    private final HoverOverlay myHover = new HoverOverlay(this);
    private final List<Overlay> myOverlays = Arrays.asList((Overlay)myHover);

    protected ToolProvider myToolProvider;
    protected IEditableArea mySurfaceArea;

    protected RadComponent myRootComponent;
    private RootView myRootView;
    private boolean myShowingRoot;

    private final TreeComponentDecorator myTreeDecorator;

    protected QuickFixManager myQuickFixManager;

    private PaletteItem myActivePaletteItem;
    private java.util.List<?> myExpandedComponents;
    private final Map<String, Property> mySelectionPropertyMap = new HashMap<String, Property>();
    private int[][] myExpandedState;
    private int[][] mySelectionState;
    private final Map<String, int[][]> mySourceSelectionState = new FixedHashMap<String, int[][]>(16);

    /**
     * Errors
     */
    private FixableMessageAction myWarnAction;

    private JPanel myErrorPanel;
    protected JPanel myErrorMessages;
    private JPanel myErrorStackPanel;
    private CardLayout myErrorStackLayout;
    private JTextArea myErrorStack;

    private MyRenderPanelWrapper myErrorPanelWrapper;

    private int myConfigurationDirty;

    /**
     * Progress
     */
    private JPanel myProgressPanel;
    private AsyncProcessIcon myProgressIcon;
    private JLabel myProgressMessage;

    /**
     * Associated source file
     */
    private final XmlFile myXmlFile;
    private final ExternalPSIChangeListener myPsiChangeListener;

    /**
     * Rendering
     */
    private final Lock myRendererLock = new ReentrantLock();
    private RenderResult myRenderResult;
    private volatile RenderSession myRenderSession;
    private volatile long myRenderSessionId;
    private final Alarm myRenderSessionAlarm = new Alarm();
    private final MergingUpdateQueue mySessionQueue;
    @Nullable
    private RenderConfiguration myRenderConfiguration;
    private final CordovaDesignerEditorPanel.LayoutConfigurationListener myConfigListener;
    private final CordovaFacet myFacet;

    private boolean myShowDeviceFrames = true;
    private int myMaxWidth;
    private int myMaxHeight;

    private boolean myNeedsRerendering;

    private RenderPreviewManager myPreviewManager;
    private RenderPreviewTool myPreviewTool;

    /** Zoom level (1 = 100%). TODO: Persist this setting across IDE sessions (on a per file basis) */
    private double myZoom = 1;

    public CordovaDesignerEditorPanel(@NotNull CordovaDesignerEditor editor, @NotNull Project project, @NotNull Module module, @NotNull VirtualFile file) {
        LOG.info("Instantiating EditorPanel...");

        myEditor = editor;
        myProject = project;
        myModule = module;
        myFile = file;

        initUI();

        myToolProvider.loadDefaultTool();
        myTreeDecorator = new CordovaTreeDecorator(project);

        showProgress("Loading configuration...");

        CordovaFacet facet = CordovaFacet.getInstance(getModule());
        assert facet != null;
        myFacet = facet;

        myConfigListener = new LayoutConfigurationListener();
        initializeConfiguration();

        mySessionQueue = new MergingUpdateQueue("cordovastudio.designer", 10, true, null, editor, null, Alarm.ThreadToUse.OWN_THREAD);

        myXmlFile = (XmlFile) CordovaPsiUtils.getPsiFileSafely(getProject(), myFile);
        assert myXmlFile != null : myFile;
        myPsiChangeListener = new ExternalPSIChangeListener(this, myXmlFile, 100, new Runnable() {
            @Override
            public void run() {
                reparseFile();
            }
        });

        addActions();

        myActive = true;
        myPsiChangeListener.setInitialize();
        myPsiChangeListener.activate();
        myPsiChangeListener.addRequest();

        LOG.info("EditorPanel created.");
    }

    private void initUI() {
        LOG.info("Initializing UI...");

        setLayout(new BorderLayout());

        myContentSplitter.setDividerWidth(0);
        myContentSplitter.setDividerMouseZoneSize(Registry.intValue("ide.splitter.mouseZone"));
        add(myContentSplitter, BorderLayout.CENTER);

        createDesignerCard();
        createErrorCard();
        createProgressPanel();

        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                CordovaDesignerEditorPanel designer = CordovaDesignerEditorPanel.this;
                getDesignerWindowManager().bind(designer);
                getPaletteWindowManager().bind(designer);
            }
        });

        LOG.info("UI initialized.");
    }

    private void addActions() {
        addConfigurationActions();
        addGotoDeclarationAction();
    }

    private void addConfigurationActions() {
        DefaultActionGroup designerActionGroup = getActionPanel().getActionGroup();
        ActionGroup group = RenderConfigurationToolBar.createActions(this);
        designerActionGroup.add(group);
    }

    private void addGotoDeclarationAction() {
        AnAction gotoDeclaration = new AnAction("Go To Declaration") {
            @Override
            public void update(AnActionEvent e) {
                IEditableArea area = e.getData(IEditableArea.DATA_KEY);
                e.getPresentation().setEnabled(area != null && area.getSelection().size() == 1);
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                IEditableArea area = e.getData(IEditableArea.DATA_KEY);
                if (area != null) {
                    RadViewComponent component = (RadViewComponent) area.getSelection().get(0);
                    PsiNavigateUtil.navigate(component.getTag());
                }
            }
        };
        myActionPanel.registerAction(gotoDeclaration, IdeActions.ACTION_GOTO_DECLARATION);
        myActionPanel.getPopupGroup().add(gotoDeclaration);
    }

    private void createDesignerCard() {
        LOG.info("Creating designer card...");

        JPanel panel = new JPanel(new FillLayout());
        myContentSplitter.setInnerComponent(panel);

        myLayeredPane = new MyLayeredPane();

        mySurfaceArea = createEditableArea();

        myToolProvider = createToolProvider();

        myGlassLayer = new GlassLayer(myToolProvider, mySurfaceArea);
        myLayeredPane.add(myGlassLayer, LAYER_GLASS);

        myDecorationLayer = createDecorationLayer();
        myLayeredPane.add(myDecorationLayer, LAYER_DECORATION);

        myFeedbackLayer = createFeedbackLayer();
        myLayeredPane.add(myFeedbackLayer, LAYER_FEEDBACK);

        myInplaceEditingLayer = createInplaceEditingLayer();
        myLayeredPane.add(myInplaceEditingLayer, LAYER_INPLACE_EDITING);

        JPanel content = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        myVerticalCaption = createCaptionPanel(false);
        content.add(myVerticalCaption, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        myHorizontalCaption = createCaptionPanel(true);
        content.add(myHorizontalCaption, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        myScrollPane = createScrollPane(myLayeredPane);
        content.add(myScrollPane, gbc);

        myHorizontalCaption.attachToScrollPane(myScrollPane);
        myVerticalCaption.attachToScrollPane(myScrollPane);

        myQuickFixManager = new QuickFixManager(this, myGlassLayer, myScrollPane.getViewport());

        myActionPanel = createActionPanel();
        myWarnAction = new FixableMessageAction();

        panel.add(myActionPanel.getToolbarComponent());
        panel.add(myPanel);

        myDesignerCard = content;
        myPanel.add(myDesignerCard, DESIGNER_CARD);

        mySurfaceArea.addSelectionListener(new ComponentSelectionListener() {
            @Override
            public void selectionChanged(IEditableArea area) {
                storeSourceSelectionState();
            }
        });

        LOG.info("Designer card created.");
    }

    public final ThreeComponentsSplitter getContentSplitter() {
        return myContentSplitter;
    }

    protected IEditableArea createEditableArea() {
        return new DesignerEditableArea();
    }

    protected ToolProvider createToolProvider() {
        return new DesignerToolProvider();
    }

    protected DecorationLayer createDecorationLayer() {
        return new DecorationLayer(this, mySurfaceArea);
    }

    protected FeedbackLayer createFeedbackLayer() {
        return new FeedbackLayer();
    }

    protected InplaceEditingLayer createInplaceEditingLayer() {
        return new InplaceEditingLayer(this);
    }

    protected CaptionPanel createCaptionPanel(boolean horizontal) {
        return new CaptionPanel(this, horizontal, true);
    }

    protected JScrollPane createScrollPane(@NotNull JLayeredPane content) {
        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(content);
        scrollPane.setBackground(new JBColor(Color.WHITE, UIUtil.getListBackground()));
        return scrollPane;
    }

    protected CordovaDesignerActionPanel createActionPanel() {
        return new CordovaDesignerActionPanel(this, myGlassLayer);
    }

    @Nullable
    public final PaletteItem getActivePaletteItem() {
        return myActivePaletteItem;
    }

    public final void activatePaletteItem(@Nullable PaletteItem paletteItem) {
        myActivePaletteItem = paletteItem;
        if (paletteItem != null) {
            myToolProvider.setActiveTool(new CreationTool(true, createCreationFactory(paletteItem)));
        } else if (myToolProvider.getActiveTool() instanceof CreationTool) {
            myToolProvider.loadDefaultTool();
        }
    }

    protected final void showDesignerCard() {
        myErrorMessages.removeAll();
        myErrorStack.setText(null);
        myLayeredPane.revalidate();
        myHorizontalCaption.update();
        myVerticalCaption.update();
        myLayout.show(myPanel, DESIGNER_CARD);
    }

    private void createErrorCard() {
        myErrorPanel = new JPanel(new BorderLayout());
        myErrorMessages = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 10, 5, true, false));
        myErrorPanel.add(myErrorMessages, BorderLayout.PAGE_START);

        myErrorStack = new JTextArea(50, 20);
        myErrorStack.setEditable(false);

        myErrorStackLayout = new CardLayout();
        myErrorStackPanel = new JPanel(myErrorStackLayout);
        myErrorStackPanel.add(new JLabel(), ERROR_NO_STACK_CARD);
        myErrorStackPanel.add(ScrollPaneFactory.createScrollPane(myErrorStack), ERROR_STACK_CARD);

        myErrorPanel.add(myErrorStackPanel, BorderLayout.CENTER);

        myPanel.add(myErrorPanel, ERROR_CARD);
    }

    public final void showError(@NotNull String message, @NotNull Throwable e) {
        if (isProjectClosed()) {
            return;
        }

        while (e instanceof InvocationTargetException) {
            if (e.getCause() == null) {
                break;
            }
            e = e.getCause();
        }

        ErrorInfo info = new ErrorInfo();
        info.myMessage = info.myDisplayMessage = message;
        info.myThrowable = e;
        configureError(info);

        if (info.myShowMessage) {
            showErrorPage(info);
        }
        if (info.myShowLog) {
            LOG.error(LogMessageEx.createEvent(info.myDisplayMessage,
                    info.myMessage + "\n" + ExceptionUtil.getThrowableText(info.myThrowable),
                    getErrorAttachments(info)));
        }
    }

    protected Attachment[] getErrorAttachments(ErrorInfo info) {
        return new Attachment[]{AttachmentFactory.createAttachment(myFile)};
    }

    protected void configureError(final @NotNull ErrorInfo info) {
        // Error messages for the user (broken custom views, missing resources, etc) are already
        // trapped during rendering and shown in the error panel. These errors are internal errors
        // in the layout editor and should instead be redirected to the log.
        info.myShowMessage = false;
        info.myShowLog = true;

        StringBuilder builder = new StringBuilder();

        builder.append("ActiveTool: ").append(myToolProvider.getActiveTool());
        builder.append("\nSDK: ");

        if (info.myThrowable instanceof IndexOutOfBoundsException && myRootComponent != null && myRenderSession != null) {
            builder.append("\n-------- RadTree --------\n");
            RadComponentOperations.printTree(builder, myRootComponent, 0);
            builder.append("\n-------- ViewTree(").append(myRenderSession.getRootViews().size()).append(") --------\n");
            for (ViewInfo viewInfo : myRenderSession.getRootViews()) {
                RadComponentOperations.printTree(builder, viewInfo, 0);
            }
        }

        info.myMessage = builder.toString();
    }

    protected void showErrorPage(final ErrorInfo info) {
        storeState();
        hideProgress();
        myRootComponent = null;

        myErrorMessages.removeAll();

        if (info.myShowStack) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            info.myThrowable.printStackTrace(new PrintStream(stream));
            myErrorStack.setText(stream.toString());
            myErrorStackLayout.show(myErrorStackPanel, ERROR_STACK_CARD);
        } else {
            myErrorStack.setText(null);
            myErrorStackLayout.show(myErrorStackPanel, ERROR_NO_STACK_CARD);
        }

        addErrorMessage(new FixableMessageInfo(true, info.myDisplayMessage, "", "", null, null), Messages.getErrorIcon());
        for (FixableMessageInfo message : info.myMessages) {
            addErrorMessage(message, message.myErrorIcon ? Messages.getErrorIcon() : Messages.getWarningIcon());
        }

        myErrorPanel.revalidate();
        myLayout.show(myPanel, ERROR_CARD);

        getDesignerToolWindow().refresh(true);
        repaint();
    }

    protected void addErrorMessage(final FixableMessageInfo message, Icon icon) {
        if (message.myLinkText.length() > 0 || message.myAfterLinkText.length() > 0) {
            HyperlinkLabel warnLabel = new HyperlinkLabel();
            warnLabel.setOpaque(false);
            warnLabel.setHyperlinkText(message.myBeforeLinkText, message.myLinkText, message.myAfterLinkText);
            warnLabel.setIcon(icon);

            if (message.myQuickFix != null) {
                warnLabel.addHyperlinkListener(new HyperlinkListener() {
                    public void hyperlinkUpdate(final HyperlinkEvent e) {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            message.myQuickFix.run();
                        }
                    }
                });
            }
            myErrorMessages.add(warnLabel);
        } else {
            JBLabel warnLabel = new JBLabel();
            warnLabel.setOpaque(false);
            warnLabel.setText("<html><body>" + message.myBeforeLinkText.replace("\n", "<br>") + "</body></html>");
            warnLabel.setIcon(icon);
            myErrorMessages.add(warnLabel);
        }
        if (message.myAdditionalFixes != null && message.myAdditionalFixes.size() > 0) {
            JPanel fixesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            fixesPanel.setBorder(IdeBorderFactory.createEmptyBorder(3, 0, 10, 0));
            fixesPanel.setOpaque(false);
            fixesPanel.add(Box.createHorizontalStrut(icon.getIconWidth()));

            for (Pair<String, Runnable> pair : message.myAdditionalFixes) {
                HyperlinkLabel fixLabel = new HyperlinkLabel();
                fixLabel.setOpaque(false);
                fixLabel.setHyperlinkText(pair.getFirst());
                final Runnable fix = pair.getSecond();

                fixLabel.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            fix.run();
                        }
                    }
                });
                fixesPanel.add(fixLabel);
            }
            myErrorMessages.add(fixesPanel);
        }
    }

    protected final void showWarnMessages(@Nullable java.util.List<FixableMessageInfo> messages) {
        if (messages == null) {
            myWarnAction.hide();
        } else {
            myWarnAction.show(messages);
        }
    }

    private void createProgressPanel() {
        myProgressIcon = new AsyncProcessIcon("Designer progress");
        myProgressMessage = new JLabel();

        JPanel progressBlock = new JPanel();
        progressBlock.add(myProgressIcon);
        progressBlock.add(myProgressMessage);
        progressBlock.setBorder(IdeBorderFactory.createRoundedBorder());

        myProgressPanel = new JPanel(new GridBagLayout());
        myProgressPanel.add(progressBlock,
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
                        0, 0));
        myProgressPanel.setOpaque(false);
    }

    protected final void showProgress(String message) {
        myProgressMessage.setText(message);
        if (myProgressPanel.getParent() == null) {
            myGlassLayer.setEnabled(false);
            myProgressIcon.resume();
            myLayeredPane.add(myProgressPanel, LAYER_PROGRESS);
            myLayeredPane.repaint();
        }
    }

    protected final void hideProgress() {
        myGlassLayer.setEnabled(true);
        myProgressIcon.suspend();
        myLayeredPane.remove(myProgressPanel);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    private void initializeConfiguration() {
        myRenderConfiguration = myFacet.getConfigurationManager().getConfiguration(myFile);
        myRenderConfiguration.addListener(myConfigListener);
    }

    /**
     * Returns the current configuration, if any (should never return null when
     * a file is being rendered; can only be null if no file is showing)
     */
    @Nullable
    @Override
    public RenderConfiguration getConfiguration() {
        return myRenderConfiguration;
    }

    /**
     * Sets the given configuration to be used for rendering
     *
     * @param configuration the configuration to use
     */
    @Override
    public void setConfiguration(@NotNull RenderConfiguration configuration) {
        if (configuration != myRenderConfiguration) {
            if (myRenderConfiguration != null) {
                myRenderConfiguration.removeListener(myConfigListener);
            }
            myRenderConfiguration = configuration;
            myRenderConfiguration.addListener(myConfigListener);
            myConfigListener.changed(MASK_ALL);
            // TODO: Cause immediate toolbar updates?
        }
    }

    private void saveState() {
        if (myRenderConfiguration != null) {
            myRenderConfiguration.save();
        }
    }

    /**
     * Update the rendering
     */
    @Override
    public void requestRender() {
        updateRenderer(false);
        mySessionQueue.sendFlush();
    }

    /**
     * The type of rendering context
     */
    @NotNull
    @Override
    public UsageType getType() {
        return UsageType.LAYOUT_EDITOR;
    }

    /**
     * Returns the current XML file, if any
     */
    @Nullable
    @Override
    public XmlFile getXmlFile() {
        return myXmlFile;
    }

    private void reparseFile() {
        LOG.info("Parsing file...");

        try {
            storeState();
            showDesignerCard();

            parseFile(new Runnable() {
                @Override
                public void run() {
                    showDesignerCard();
                    myLayeredPane.revalidate();
                    restoreState();
                }
            });
        } catch (RuntimeException e) {
            myPsiChangeListener.clear();
            showError("Parsing error", e.getCause() == null ? e : e.getCause());
        }

        LOG.info("Parsing done.");
    }

    private void parseFile(final Runnable runnable) {
        if (myRenderConfiguration == null) {
            return;
        }

        createRenderer(new ThrowableConsumer<RenderResult, Throwable>() {
            @Override
            public void consume(RenderResult result) throws Throwable {
                RenderSession session = result.getSession();
                if (session == null) {
                    return;
                }
                updateDeviceFrameVisibility(result);

                if (!session.getResult().isSuccess()) {
                    // This image may not have been fully rendered before some error caused
                    // the render to abort, but a partial render is better. However, if the render
                    // was due to some configuration change, we don't want to replace the image
                    // since all the mouse regions and model setup will no longer match the pixels.
                    if (myRootView != null && myRootView.getImage() != null && session.getImage() != null &&
                            session.getImage().getWidth() == myRootView.getImage().getWidth() &&
                            session.getImage().getHeight() == myRootView.getImage().getHeight()) {
                        myRootView.setRenderedImage(result.getImage());
                        myRootView.repaint();
                    }
                    return;
                }

                boolean insertPanel = !myShowingRoot;
                if (myRootView == null) {
                    myRootView = new RootView(CordovaDesignerEditorPanel.this, 0, 0, result);
                    insertPanel = true;
                } else {
                    myRootView.setRenderedImage(result.getImage());

                    myRootView.updateBounds(true);
                }
                boolean firstRender = myRootComponent == null;
                try {
                    myRootComponent = RadModelBuilder.update(CordovaDesignerEditorPanel.this, result, (RadViewComponent) myRootComponent, myRootView);
                } catch (Throwable e) {
                    myRootComponent = null;
                    throw e;
                }

                // Start out selecting the root layout rather than the device item; this will
                // show relevant layout actions immediately, will cause the component tree to
                // be properly expanded, etc
                if (firstRender) {
                    RadViewComponent rootComponent = getLayoutRoot();
                    if (rootComponent != null) {
                        mySurfaceArea.setSelection(Collections.<RadComponent>singletonList(rootComponent));
                    }
                }

                if (insertPanel) {
                    // Use a custom layout manager which adjusts the margins/padding around the designer canvas
                    // dynamically; it will try to use DEFAULT_HORIZONTAL_MARGIN * DEFAULT_VERTICAL_MARGIN, but
                    // if there is not enough room, it will split the margins evenly in each dimension until
                    // there is no room available without scrollbars.
                    JPanel rootPanel = new JPanel(new LayoutManager() {
                        @Override
                        public void addLayoutComponent(String s, Component component) {
                        }

                        @Override
                        public void removeLayoutComponent(Component component) {
                        }

                        @Override
                        public Dimension preferredLayoutSize(Container container) {
                            return new Dimension(0, 0);
                        }

                        @Override
                        public Dimension minimumLayoutSize(Container container) {
                            return new Dimension(0, 0);
                        }

                        @Override
                        public void layoutContainer(Container container) {
                            myRootView.updateBounds(false);
                            int x = Math.max(2, Math.min(DEFAULT_HORIZONTAL_MARGIN, (container.getWidth() - myRootView.getWidth()) / 2));
                            int y = Math.max(2, Math.min(DEFAULT_VERTICAL_MARGIN, (container.getHeight() - myRootView.getHeight()) / 2));

                            if (myMaxWidth > 0) {
                                myRootView.setLocation(Math.max(0, (myMaxWidth - myRootView.getScaledWidth()) / 2),
                                        2 + Math.max(0, (myMaxHeight - myRootView.getScaledHeight()) / 2));
                            } else {
                                myRootView.setLocation(x, y);
                            }
                        }
                    });

                    rootPanel.setBackground(DrawingStyle.DESIGNER_BACKGROUND_COLOR);
                    rootPanel.setOpaque(true);
                    rootPanel.add(myRootView);

                    //Debug
                    /*
                    BufferedImage image = myRootView.getImage();
                    try {
                        FileOutputStream os = new FileOutputStream("/Users/cti/bsc/testbench/Test41/www/test.png");
                        ImageIO.write(image, "png", os);
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }*/

                    myLayeredPane.add(rootPanel, LAYER_COMPONENT);
                    myShowingRoot = true;
                }
                zoomToFitIfNecessary();

                loadInspections(new EmptyProgressIndicator());
                updateInspections();

                runnable.run();
            }
        });
    }

    private void createRenderer(final ThrowableConsumer<RenderResult, Throwable> runnable) {
        disposeRenderer();
        if (myRenderConfiguration == null) {
            return;
        }

        myRenderSessionAlarm.addRequest(new Runnable() {
            @Override
            public void run() {
                if (myRenderSession == null) {
                    showProgress(myRenderSessionId <= 1 ? "Initializing Rendering Library..." : "Rendering... ");
                }
            }
        }, 500);

        final long sessionId = ++myRenderSessionId;

        mySessionQueue.queue(new Update("render") {
            private void cancel() {
                myRenderSessionAlarm.cancelAllRequests();
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!isProjectClosed()) {
                            hideProgress();
                        }
                    }
                });
            }

            @Override
            public void run() {
                try {
                    if (sessionId != myRenderSessionId) {
                        cancel();
                        return;
                    }

                    final Module module = getModule();
                    final RenderLogger logger = new RenderLogger(myFile.getName(), module);

                    if (myRenderConfiguration.getTheme() == null) {
                        //logger.error(null, "No theme selected", null);
                        myRenderConfiguration.setTheme("default");
                        myRenderConfiguration.save();
                    }

                    if(myRenderConfiguration.getDevice() == null) {

                    }

                    if (logger.hasProblems()) {
                        cancel();
                        RenderResult renderResult = RenderResult.createBlank(myXmlFile, logger);
                        runnable.consume(renderResult);
                        updateErrors(renderResult);
                        return;
                    }

                    final RenderResult renderResult;
                    RenderContext renderContext = CordovaDesignerEditorPanel.this;
                    if (myRendererLock.tryLock()) {
                        try {
                            final RenderService service = RenderService.create(myFacet, module, myXmlFile, myRenderConfiguration, logger, renderContext);
                            if (service != null) {
                                renderResult = service.render();
                                service.dispose();
                            } else {
                                renderResult = RenderResult.createBlank(myXmlFile, logger);
                            }
                            myRenderResult = renderResult;
                        } finally {
                            myRendererLock.unlock();
                        }
                    } else {
                        cancel();
                        return;
                    }

                    if (sessionId != myRenderSessionId) {
                        cancel();
                        return;
                    }

                    if (renderResult == null) {
                        throw new RenderingException();
                    }

                    myRenderSessionAlarm.cancelAllRequests();

                    Runnable uiRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!isProjectClosed()) {
                                    hideProgress();
                                    if (sessionId == myRenderSessionId) {
                                        runnable.consume(renderResult);
                                        updateErrors(renderResult);
                                    }
                                }
                            } catch (Throwable e) {
                                myPsiChangeListener.clear();
                                showError("Parsing error", e);
                            }
                        }
                    };
                    if (ApplicationManager.getApplication().isUnitTestMode()) {
                        ApplicationManager.getApplication().invokeAndWait(uiRunnable, ModalityState.defaultModalityState());
                    } else {
                        ApplicationManager.getApplication().invokeLater(uiRunnable);
                    }
                } catch (final Throwable e) {
                    myPsiChangeListener.clear();
                    myRenderSessionAlarm.cancelAllRequests();

                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            myPsiChangeListener.clear();
                            showError("Render error", e);
                        }
                    });
                }
            }
        });
    }

    private void updateRenderer(final boolean updateProperties) {
        if (myRenderConfiguration == null) {
            return;
        }
        if (myRootComponent == null) {
            reparseFile();
            return;
        }
        createRenderer(new ThrowableConsumer<RenderResult, Throwable>() {
            @Override
            public void consume(RenderResult result) throws Throwable {
                RenderSession session = result.getSession();
                if (session == null || session.getImage() == null) {
                    return;
                }
                updateDeviceFrameVisibility(result);
                myRootComponent = RadModelBuilder.update(CordovaDesignerEditorPanel.this, result, (RadViewComponent) myRootComponent, myRootView);
                myRootView.setRenderedImage(result.getImage());

                zoomToFitIfNecessary();

                myLayeredPane.revalidate();
                myHorizontalCaption.update();
                myVerticalCaption.update();

                CordovaDesignerToolWindow toolWindow = getToolWindow();
                if (toolWindow != null) {
                    toolWindow.refresh(updateProperties);
                }
            }
        });
    }

    private void updateErrors(@NotNull final RenderResult result) {
        Application application = ApplicationManager.getApplication();
        if (!application.isDispatchThread()) {
            application.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateErrors(result);
                }
            });
            return;
        }
        RenderLogger logger = result.getLogger();
        if (!logger.hasProblems()) {
            if (myErrorPanelWrapper == null) {
                return;
            }
            myLayeredPane.remove(myErrorPanelWrapper);
            myErrorPanelWrapper = null;
            myLayeredPane.repaint();
        } else {
            if (myErrorPanelWrapper == null) {
                myErrorPanelWrapper = new MyRenderPanelWrapper(new RenderErrorPanel());
            }
            myErrorPanelWrapper.getErrorPanel().showErrors(result);
            myLayeredPane.add(myErrorPanelWrapper, LAYER_ERRORS);
            myLayeredPane.repaint();
        }
    }

    private void disposeRenderer() {
        if (myRenderSession != null) {
            myRenderSession.dispose();
            myRenderSession = null;
        }
    }

    @Nullable
    private CordovaDesignerToolWindow getToolWindow() {
        try {
            // This method sometimes returns null. We don't want to bother the user with that; the worst that
            // can happen is that the property view is not updated.
            return CordovaDesignerToolWindowManager.getInstance(this);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Returns the current virtual file, if any
     * <p/>
     * TODO: Get rid of this now that configurations carry file info!
     */
    @Nullable
    @Override
    public VirtualFile getVirtualFile() {
        return myFile;
    }

    @NotNull
    @Override
    public final Module getModule() {
        if (myModule.isDisposed()) {
            myModule = findModule(myProject, myFile);
            if (myModule == null) {
                throw new IllegalArgumentException("No module for file " + myFile + " in project " + myProject);
            }
        }
        return myModule;
    }

    /**
     * Returns whether the current scene has an alpha channel
     *
     * @return true if the scene has an alpha channel, false if not or if unknown
     */
    @Override
    public boolean hasAlphaChannel() {
        return false;
    }

    /**
     * Returns the Swing component the rendering is painted into
     *
     * @return the component
     */
    @NotNull
    @Override
    public Component getComponent() {
        return myLayeredPane;
    }

    /**
     * Returns the size of the full/original rendered image, without scaling applied
     *
     * @return the image size, or {@link #NO_SIZE}
     */
    @NotNull
    @Override
    public Dimension getFullImageSize() {
        if (myRootView != null) {
            BufferedImage image = myRootView.getImage();
            if (image != null) {
                return new Dimension(image.getWidth(), image.getHeight());
            }
        }

        return NO_SIZE;
    }

    /**
     * Returns the size of the (possibly zoomed) image
     *
     * @return the scaled image, or {@link #NO_SIZE}
     */
    @NotNull
    @Override
    public Dimension getScaledImageSize() {
        if (myRootView != null) {
            BufferedImage image = myRootView.getImage();
            if (image != null) {
                return new Dimension((int)(myZoom * image.getWidth()), (int)(myZoom * image.getHeight()));
            }
        }

        return NO_SIZE;
    }

    /**
     * Returns the rectangle which defines the client area (the space available for
     * the rendering)
     *
     * @return the client area
     */
    @NotNull
    @Override
    public Rectangle getClientArea() {
        return myScrollPane.getViewport().getViewRect();
    }

    /**
     * Returns true if this render context supports render previews
     *
     * @return true if render previews are supported
     */
    @Override
    public boolean supportsPreviews() {
        return true;
    }

    @Nullable
    @Override
    public RenderPreviewManager getPreviewManager(boolean createIfNecessary) {
        if (myPreviewManager == null && createIfNecessary) {
            myPreviewManager = new RenderPreviewManager(this);
            RenderPreviewPanel panel = new RenderPreviewPanel();
            myLayeredPane.add(panel, LAYER_PREVIEW);
            myLayeredPane.revalidate();
            myLayeredPane.repaint();
        }

        return myPreviewManager;
    }

    /**
     * Sets the rendering size to be at most the given width and the given height.
     *
     * @param width  the maximum width, or 0 to use any size
     * @param height the maximum height, or 0 to use any height
     */
    @Override
    public void setMaxSize(int width, int height) {
        myMaxWidth = width;
        myMaxHeight = height;
        layoutParent();
    }

    @Override
    public void zoomFit(boolean onlyZoomOut, boolean allowZoomIn) {
        zoom(allowZoomIn ? ZoomType.FIT : ZoomType.FIT_INTO);
    }


    protected void layoutParent() {
        if (myRootView != null) {
            ((JComponent) myRootView.getParent()).revalidate();
        }
    }

    /**
     * Called when the content of the rendering has changed, so the view
     * should update the layout (to for example recompute zoom fit, if applicable,
     * and to revalidate the components
     */
    @Override
    public void updateLayout() {

    }

    /**
     * Sets whether device frames should be shown in the main window. Note that this
     * is a flag which is and'ed with the user's own preference. If device frames are
     * turned off, this flag will have no effect. But if they are on, they can be
     * temporarily turned off with this method. This is used in render preview mode
     * for example to ensure that the previews and the main rendering both agree on
     * whether to show device frames.
     *
     * @param on whether device frames should be enabled or not
     */
    @Override
    public void setDeviceFramesEnabled(boolean on) {
        myShowDeviceFrames = on;
        if (myRootView != null) {
            RenderedImage image = myRootView.getRenderedImage();
            if (image != null) {
                image.setDeviceFrameEnabled(on);
            }
        }
    }

    private void updateDeviceFrameVisibility(@Nullable RenderResult result) {
        if (result != null) {
            RenderedImage image = result.getImage();
            if (image != null) {
                RenderService renderService = result.getRenderService();
                image.setDeviceFrameEnabled(myShowDeviceFrames && renderService != null &&
                        renderService.getShowDecorations());
            }
        }
    }

    /**
     * Returns the most recent rendered image, if any
     */
    @Nullable
    @Override
    public BufferedImage getRenderedImage() {
        return myRootView != null ? myRootView.getImage() : null;
    }

    @Nullable
    @Override
    public RenderedViewHierarchy getViewHierarchy() {
        return myRenderResult != null ? myRenderResult.getHierarchy() : null;
    }

    @Nullable
    protected Module findModule(Project project, VirtualFile file) {
        return ModuleUtilCore.findModuleForFile(file, project);
    }

    public final CordovaDesignerEditor getEditor() {
        return myEditor;
    }

    public VirtualFile getFile() {
        return myFile;
    }

    @Override
    public final Project getProject() {
        return myProject;
    }

    public final boolean isProjectClosed() {
        return myProject.isDisposed() || !myProject.isOpen();
    }

    public IEditableArea getSurfaceArea() {
        return mySurfaceArea;
    }

    public ToolProvider getToolProvider() {
        return myToolProvider;
    }

    public CordovaDesignerActionPanel getActionPanel() {
        return myActionPanel;
    }

    public InplaceEditingLayer getInplaceEditingLayer() {
        return myInplaceEditingLayer;
    }

    public JComponent getPreferredFocusedComponent() {
        return myDesignerCard.isVisible() ? myGlassLayer : myErrorPanel;
    }

    public IdManager getIdManager() {
        return myIdManager;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // State
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    public java.util.List<?> getExpandedComponents() {
        return myExpandedComponents;
    }

    public void setExpandedComponents(@Nullable java.util.List<?> expandedComponents) {
        myExpandedComponents = expandedComponents;
    }

    public Property getSelectionProperty(@Nullable String key) {
        return mySelectionPropertyMap.get(key);
    }

    public void setSelectionProperty(@Nullable String key, Property selectionProperty) {
        mySelectionPropertyMap.put(key, selectionProperty);
    }

    protected void storeState() {
        if (myRootComponent != null && myExpandedState == null && mySelectionState == null) {
            myExpandedState = new int[myExpandedComponents == null ? 0 : myExpandedComponents.size()][];
            for (int i = 0; i < myExpandedState.length; i++) {
                IntArrayList path = new IntArrayList();
                componentToPath((RadComponent) myExpandedComponents.get(i), path);
                myExpandedState[i] = path.toArray();
            }

            mySelectionState = getSelectionState();

            myExpandedComponents = null;

            InputTool tool = myToolProvider.getActiveTool();
            if (!(tool instanceof MarqueeTracker) &&
                    !(tool instanceof CreationTool) &&
                    !(tool instanceof PasteTool)) {
                myToolProvider.loadDefaultTool();
            }
        }
    }

    private void storeSourceSelectionState() {
        if (!CommonEditActionsProvider.isDeleting) {
            mySourceSelectionState.put(getEditorText(), getSelectionState());
        }
    }

    private int[][] getSelectionState() {
        return getSelectionState(mySurfaceArea.getSelection());
    }

    protected static int[][] getSelectionState(java.util.List<RadComponent> selection) {
        int[][] selectionState = new int[selection.size()][];

        for (int i = 0; i < selectionState.length; i++) {
            IntArrayList path = new IntArrayList();
            componentToPath(selection.get(i), path);
            selectionState[i] = path.toArray();
        }

        return selectionState;
    }

    private static void componentToPath(RadComponent component, IntArrayList path) {
        RadComponent parent = component.getParent();

        if (parent != null) {
            path.add(0, parent.getChildren().indexOf(component));
            componentToPath(parent, path);
        }
    }

    protected void restoreState() {
        CordovaDesignerToolWindowContent toolManager = getDesignerToolWindow();

        if (myExpandedState != null) {
            java.util.List<RadComponent> expanded = new ArrayList<RadComponent>();
            for (int[] path : myExpandedState) {
                pathToComponent(expanded, myRootComponent, path, 0);
            }
            myExpandedComponents = expanded;
            toolManager.expandFromState();
            myExpandedState = null;
        }

        java.util.List<RadComponent> selection = new ArrayList<RadComponent>();

        int[][] selectionState = mySourceSelectionState.get(getEditorText());
        if (selectionState != null) {
            for (int[] path : selectionState) {
                pathToComponent(selection, myRootComponent, path, 0);
            }
        }

        if (selection.isEmpty()) {
            if (mySelectionState != null) {
                for (int[] path : mySelectionState) {
                    pathToComponent(selection, myRootComponent, path, 0);
                }
            }
        }

        if (selection.isEmpty()) {
            toolManager.refresh(true);
        } else {
            mySurfaceArea.setSelection(selection);
        }

        mySelectionState = null;
    }

    protected static void pathToComponent(java.util.List<RadComponent> components, RadComponent component, int[] path, int index) {
        if (index == path.length) {
            components.add(component);
        } else {
            java.util.List<RadComponent> children = component.getChildren();
            int componentIndex = path[index];
            if (0 <= componentIndex && componentIndex < children.size()) {
                pathToComponent(components, children.get(componentIndex), path, index + 1);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @deprecated Artifact of Android Studio. Will be removed eventually.
     * May live on, if we eventually implement different targets in Cordova Studio...
     */
    public String getPlatformTarget() {
        return "cordova";
    }

    @Nullable
    private RadViewComponent getLayoutRoot() {
        if (myRootComponent != null && myRootComponent.getChildren().size() == 1) {
            RadComponent component = myRootComponent.getChildren().get(0);
            if (component.isBackground() && component instanceof RadViewComponent) {
                return (RadViewComponent) component;
            }
        }

        return null;
    }

    protected RadComponent findTarget(int x, int y, @Nullable ComponentTargetFilter filter) {
        if (myRootComponent != null) {
            FindComponentVisitor visitor = new FindComponentVisitor(myLayeredPane, filter, x, y);
            myRootComponent.accept(visitor, false);
            return visitor.getResult();
        }
        return null;
    }

    protected ComponentDecorator getRootSelectionDecorator() {
        return EmptyComponentDecorator.INSTANCE;
    }

    @Nullable
    protected EditOperation processRootOperation(OperationContext context) {
        return null;
    }

    protected boolean execute(ThrowableRunnable<Exception> operation, final boolean updateProperties) {
        if (!ReadonlyStatusHandler.ensureFilesWritable(getProject(), myFile)) {
            return false;
        }
        try {
            myPsiChangeListener.stop();
            operation.run();
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    boolean active = myPsiChangeListener.isActive();
                    if (active) {
                        myPsiChangeListener.stop();
                    }
                    updateRenderer(updateProperties);
                    if (active) {
                        myPsiChangeListener.start();
                    }
                }
            });
            return true;
        }
        catch (Throwable e) {
            showError("Execute command", e);
            return false;
        }
        finally {
            myPsiChangeListener.start();
        }
    }

    protected void executeWithReparse(ThrowableRunnable<Exception> operation) {
        if (!ReadonlyStatusHandler.ensureFilesWritable(getProject(), myFile)) {
            return;
        }
        try {
            myPsiChangeListener.stop();
            operation.run();
            myPsiChangeListener.start();
            reparseFile();
        }
        catch (Throwable e) {
            showError("Execute command", e);
            myPsiChangeListener.start();
        }
    }

    protected void execute(java.util.List<EditOperation> operations) {
        if (!ReadonlyStatusHandler.ensureFilesWritable(getProject(), myFile)) {
            return;
        }
        try {
            myPsiChangeListener.stop();
            for (EditOperation operation : operations) {
                operation.execute();
            }
            updateRenderer(true);
        }
        catch (Throwable e) {
            showError("Execute command", e);
        }
        finally {
            myPsiChangeListener.start();
        }
    }

    public java.util.List<PaletteGroup> getPaletteGroups() {
        Project project = getProject();
        ViewsMetaManager vmm = ViewsMetaManager.getInstance(project);
        return vmm.getPaletteGroups();
    }

    /**
     * Returns a suitable version label from the version attribute from a {@link PaletteItem} version
     */
    @NotNull
    public String getVersionLabel(@Nullable String version) {
        return StringUtil.notNullize(version);
    }

    public boolean isDeprecated(@Nullable String deprecatedIn) {
        return !StringUtil.isEmpty(deprecatedIn);
    }

    protected InputTool createDefaultTool() {
        return new SelectionTool();
    }

    @NotNull
    protected ComponentCreationFactory createCreationFactory(final PaletteItem paletteItem) {
        return new ComponentCreationFactory() {
            @NotNull
            @Override
            public RadComponent create() throws Exception {
                RadViewComponent component = RadComponentOperations.createComponent(null, paletteItem.getMetaModel());
                component.setInitialPaletteItem(paletteItem);
                if (component instanceof IConfigurableComponent) {
                    ((IConfigurableComponent) component).configure(myRootComponent);
                }
                return component;
            }
        };
    }

    @Nullable
    public ComponentPasteFactory createPasteFactory(String xmlComponents) {
        return new CordovaPasteFactory(getModule(), xmlComponents);
    }

    public String getEditorText() {
        return ApplicationManager.getApplication().runReadAction(new Computable<String>() {
            @Override
            public String compute() {
            return myXmlFile.getText();
            }
        });
    }

    public void activate() {
        myActive = true;
        myPsiChangeListener.activate();

        if (myNeedsRerendering) {
            updateRenderer(true);
            myNeedsRerendering = false;
        }
    }

    public void deactivate() {
        myActive = false;
        myPsiChangeListener.deactivate();
    }

    @NotNull
    public CordovaDesignerEditorState createState() {
        return new CordovaDesignerEditorState(myFile);
    }

    public boolean isEditorValid() {
        return myFile.isValid();
    }

    @Override
    public Object getData(@NonNls String dataId) {
        return myActionPanel.getData(dataId);
    }

    public void dispose() {
        myPsiChangeListener.dispose();
        if (myRenderConfiguration != null) {
            myRenderConfiguration.removeListener(myConfigListener);
        }

        Disposer.dispose(myProgressIcon);
        getDesignerWindowManager().dispose(this);
        getPaletteWindowManager().dispose(this);
        Disposer.dispose(myContentSplitter);

        disposeRenderer();
    }

    protected AbstractToolWindowManager getDesignerWindowManager() {
        return CordovaDesignerToolWindowManager.getInstance(myProject);
    }

    protected AbstractToolWindowManager getPaletteWindowManager() {
        return CordovaPaletteToolWindowManager.getInstance(myProject);
    }

    public CordovaDesignerToolWindowContent getDesignerToolWindow() {
        return CordovaDesignerToolWindowManager.getInstance(this);
    }

    protected CordovaPaletteToolWindowContent getPaletteToolWindow() {
        return CordovaPaletteToolWindowManager.getInstance(this);
    }

    @Nullable
    public WrapInProvider getWrapInProvider() {
        return null;
    }

    @Nullable
    public RadComponent getRootComponent() {
        return myRootComponent;
    }

    public Object[] getTreeRoots() {
        return myRootComponent == null ? ArrayUtil.EMPTY_OBJECT_ARRAY : new Object[]{myRootComponent};
    }

    public TreeComponentDecorator getTreeDecorator() {
        return myTreeDecorator;
    }

    public void handleTreeArea(TreeEditableArea treeArea) {
    }

    @NotNull
    public TablePanelActionPolicy getTablePanelActionPolicy() {
        return TablePanelActionPolicy.ALL;
    }

    @Nullable
    public PropertyTableTab[] getPropertyTableTabs() {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Zooming
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    public boolean isZoomSupported() {
        return false;
    }

    /**
     * Auto fits the scene, if requested. This will be the case the first time
     * the layout is opened, and after orientation or device changes.
     */
    synchronized void zoomToFitIfNecessary() {
    }

    public void zoom(@NotNull ZoomType type) {
    }

    public void setZoom(double zoom) {
    }

    public double getZoom() {
        return 1;
    }

    protected void viewZoomed() {
        // Hide quickfix light bulbs; position can be obsolete after the zoom level has changed
        myQuickFixManager.hideHint();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Inspection
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    public void loadInspections(ProgressIndicator progress) {
    }

    public void updateInspections() {
        myQuickFixManager.update();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // OverlayContainer
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    private static final int MINIMIZE_WIDTH = 25;

    /**
     * Returns a list of overlays to be shown in this container
     */
    @Nullable
    @Override
    public List<Overlay> getOverlays() {
        return myOverlays;
    }

    /**
     * Returns true if the given tag should be shown as selected
     *
     * @param tag
     */
    @Override
    public boolean isSelected(@NotNull XmlTag tag) {
        for (RadComponent component : getSurfaceArea().getSelection()) {
            if (component instanceof RadViewComponent) {
                RadViewComponent rv = (RadViewComponent)component;
                if (tag == rv.getTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts the given rectangle (in model coordinates) to coordinates in the given
     * target component's coordinate system.
     * <p/>
     * Returns a new {@link java.awt.Rectangle}, so callers are free to modify the result.
     *
     * @param target    the component whose coordinate system the rectangle should be
     *                  translated into
     * @param rectangle the model rectangle to convert
     * @return the rectangle converted to the coordinate system of the target
     */
    @NotNull
    @Override
    public Rectangle fromModel(@NotNull Component target, @NotNull Rectangle rectangle) {
        Rectangle r = myRootComponent.fromModel(target, rectangle);
        if (target == myRootView) {
            double scale = myRootView.getScale();
            r.x *= scale;
            r.y *= scale;
            r.width *= scale;
            r.height *= scale;
        }
        RenderedImage renderedImage = myRootView.getRenderedImage();
        if (renderedImage != null) {
            Rectangle imageBounds = renderedImage.getImageBounds();
            if (imageBounds != null) {
                r.x += imageBounds.x;
                r.y += imageBounds.y;
            }
        }
        return r;
    }

    /**
     * Converts the given rectangle (in coordinates relative to the given component)
     * into the equivalent rectangle in model coordinates.
     * <p/>
     * Returns a new {@link java.awt.Rectangle}, so callers are free to modify the result.
     *
     * @param source    the component which defines the coordinate system of the rectangle
     * @param rectangle the rectangle to be converted into model coordinates
     * @return the rectangle converted to the model coordinate system
     * @see org.cordovastudio.editors.designer.model.RadComponent#toModel(java.awt.Component, java.awt.Rectangle)
     */
    @NotNull
    @Override
    public Rectangle toModel(@NotNull Component source, @NotNull Rectangle rectangle) {
        RenderedImage renderedImage = myRootView.getRenderedImage();
        if (renderedImage != null) {
            Rectangle imageBounds = renderedImage.getImageBounds();
            if (imageBounds != null && imageBounds.x != 0 && imageBounds.y != 0) {
                rectangle = new Rectangle(rectangle);
                rectangle.x -= imageBounds.x;
                rectangle.y -= imageBounds.y;
            }
        }
        Rectangle r = myRootComponent.toModel(source, rectangle);
        if (source == myRootView) {
            double scale = myRootView.getScale();
            r.x /= scale;
            r.y /= scale;
            r.width /= scale;
            r.height /= scale;
        }
        return r;
    }

    private static final class FillLayout implements LayoutManager2 {
        @Override
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        @Override
        public float getLayoutAlignmentX(Container target) {
            return 0.5f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0.5f;
        }

        @Override
        public void invalidateLayout(Container target) {
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Component toolbar = parent.getComponent(0);
            Dimension toolbarSize = toolbar.isVisible() ? toolbar.getPreferredSize() : new Dimension();
            Dimension contentSize = parent.getComponent(1).getPreferredSize();
            int extraWidth = 0;
            JComponent jParent = (JComponent) parent;
            if (jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY) != null) {
                extraWidth += MINIMIZE_WIDTH;
            }
            if (jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY) != null) {
                extraWidth += MINIMIZE_WIDTH;
            }
            return new Dimension(Math.max(toolbarSize.width, contentSize.width + extraWidth), toolbarSize.height + contentSize.height);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Component toolbar = parent.getComponent(0);
            Dimension toolbarSize = toolbar.isVisible() ? toolbar.getMinimumSize() : new Dimension();
            Dimension contentSize = parent.getComponent(1).getMinimumSize();
            int extraWidth = 0;
            JComponent jParent = (JComponent) parent;
            if (jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY) != null) {
                extraWidth += MINIMIZE_WIDTH;
            }
            if (jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY) != null) {
                extraWidth += MINIMIZE_WIDTH;
            }
            return new Dimension(Math.max(toolbarSize.width, contentSize.width + extraWidth), toolbarSize.height + contentSize.height);
        }

        @Override
        public void layoutContainer(Container parent) {
            int leftWidth = 0;
            int rightWidth = 0;
            JComponent jParent = (JComponent) parent;
            JComponent left = (JComponent) jParent.getClientProperty(LightToolWindow.LEFT_MIN_KEY);
            if (left != null) {
                leftWidth = MINIMIZE_WIDTH;
            }
            JComponent right = (JComponent) jParent.getClientProperty(LightToolWindow.RIGHT_MIN_KEY);
            if (right != null) {
                rightWidth = MINIMIZE_WIDTH;
            }
            int extraWidth = leftWidth + rightWidth;

            int width = parent.getWidth() - extraWidth;
            int height = parent.getHeight();
            Component toolbar = parent.getComponent(0);
            Dimension toolbarSize = toolbar.isVisible() ? toolbar.getPreferredSize() : new Dimension();
            toolbar.setBounds(leftWidth, 0, width, toolbarSize.height);
            parent.getComponent(1).setBounds(leftWidth, toolbarSize.height, width, height - toolbarSize.height);

            if (left != null) {
                left.setBounds(0, 0, leftWidth, height);
            }
            if (right != null) {
                right.setBounds(width + leftWidth, 0, rightWidth, height);
            }
        }
    }

    /**
     * Size of the scene, in scroll pane view port pixels.
     */
    @NotNull
    protected Dimension getSceneSize(Component target) {
        int width = 0;
        int height = 0;

        if (myRootComponent != null) {
            Rectangle bounds = myRootComponent.getBounds(target);
            width = Math.max(width, (int) bounds.getMaxX());
            height = Math.max(height, (int) bounds.getMaxY());

            for (RadComponent component : myRootComponent.getChildren()) {
                Rectangle childBounds = component.getBounds(target);
                width = Math.max(width, (int) childBounds.getMaxX());
                height = Math.max(height, (int) childBounds.getMaxY());
            }
        }

        width += 50;
        height += 40;

        return new Dimension(width, height);
    }

    protected class DesignerEditableArea extends ComponentEditableArea {
        public DesignerEditableArea() {
            super(myLayeredPane);
        }

        @Override
        protected void fireSelectionChanged() {
            super.fireSelectionChanged();
            myLayeredPane.revalidate();
            myLayeredPane.repaint();
        }

        @Override
        public void scrollToSelection() {
            java.util.List<RadComponent> selection = getSelection();
            if (selection.size() == 1) {
                Rectangle bounds = selection.get(0).getBounds(myLayeredPane);
                if (bounds != null) {
                    myLayeredPane.scrollRectToVisible(bounds);
                }
            }
        }

        @Override
        public RadComponent findTarget(int x, int y, @Nullable ComponentTargetFilter filter) {
            return CordovaDesignerEditorPanel.this.findTarget(x, y, filter);
        }

        @Override
        public InputTool findTargetTool(int x, int y) {
            if (myPreviewManager != null && myRootView != null) {
                if (myPreviewTool == null) {
                    myPreviewTool = new RenderPreviewTool();
                }

                if (x > (myRootView.getX() + myRootView.getWidth()) ||
                        y > (myRootView.getY() + myRootView.getHeight())) {
                    return myPreviewTool;
                }
            }

            if (myRootComponent != null && myRenderResult != null) {
                RadComponent target = findTarget(x, y, null);
                RenderedView leaf = null;
                if (target instanceof RadViewComponent) {
                    RadViewComponent rv = (RadViewComponent)target;
                    RenderedViewHierarchy hierarchy = myRenderResult.getHierarchy();
                    if (hierarchy != null) {
                        leaf = hierarchy.findViewByTag(rv.getTag());
                    }
                }
                if (myHover.setHoveredView(leaf)) {
                    repaint();
                }
            }
            return myDecorationLayer.findTargetTool(x, y);
        }

        @Override
        public void showSelection(boolean value) {
            myDecorationLayer.showSelection(value);
        }

        @Override
        public ComponentDecorator getRootSelectionDecorator() {
            return CordovaDesignerEditorPanel.this.getRootSelectionDecorator();
        }

        @Nullable
        public EditOperation processRootOperation(OperationContext context) {
            return CordovaDesignerEditorPanel.this.processRootOperation(context);
        }

        @Override
        public FeedbackLayer getFeedbackLayer() {
            return myFeedbackLayer;
        }

        @Override
        public RadComponent getRootComponent() {
            return myRootComponent;
        }

        @Override
        public ActionGroup getPopupActions() {
            return myActionPanel.getPopupActions(this);
        }

        @Override
        public String getPopupPlace() {
            return ActionPlaces.GUI_DESIGNER_EDITOR_POPUP;
        }
    }

    private class RenderPreviewPanel extends JComponent {
        RenderPreviewPanel() {
            //super(new BorderLayout());
            setBackground(null);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (myPreviewManager != null) {
                myPreviewManager.paint((Graphics2D)g);
            }
        }
    }

    private class RenderPreviewTool extends InputTool {
        @Override
        public void mouseMove(MouseEvent event, IEditableArea area) throws Exception {
            if (myPreviewManager != null) {
                myPreviewManager.moved(event);
            }
        }

        @Override
        public void mouseUp(MouseEvent event, IEditableArea area) throws Exception {
            super.mouseUp(event, area);
            if (myPreviewManager != null && event.getClickCount() > 0) {
                myPreviewManager.click(event);
            }
        }

        @Override
        public void mouseEntered(MouseEvent event, IEditableArea area) throws Exception {
            super.mouseEntered(event, area);
            if (myPreviewManager != null) {
                myPreviewManager.enter(event);
            }
        }

        @Override
        public void mouseExited(MouseEvent event, IEditableArea area) throws Exception {
            super.mouseExited(event, area);
            if (myPreviewManager != null) {
                myPreviewManager.exit(event);
            }
        }
    }

    protected class DesignerToolProvider extends ToolProvider {
        @Override
        public void loadDefaultTool() {
            setActiveTool(createDefaultTool());
        }

        @Override
        public void setActiveTool(InputTool tool) {
            if (getActiveTool() instanceof CreationTool && !(tool instanceof CreationTool)) {
                getPaletteToolWindow().clearActiveItem();
            }
            if (!(tool instanceof SelectionTool)) {
                hideInspections();
            }
            super.setActiveTool(tool);
        }

        @Override
        public boolean execute(final ThrowableRunnable<Exception> operation, String command, final boolean updateProperties) {
            final boolean[] is = {true};
            CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
                public void run() {
                    is[0] = CordovaDesignerEditorPanel.this.execute(operation, updateProperties);
                }
            }, command, null);
            return is[0];
        }

        @Override
        public void executeWithReparse(final ThrowableRunnable<Exception> operation, String command) {
            CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
                public void run() {
                    CordovaDesignerEditorPanel.this.executeWithReparse(operation);
                }
            }, command, null);
        }

        @Override
        public void execute(final java.util.List<EditOperation> operations, String command) {
            CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
                public void run() {
                    CordovaDesignerEditorPanel.this.execute(operations);
                }
            }, command, null);
        }

        @Override
        public void startInplaceEditing(@Nullable InplaceContext inplaceContext) {
            myInplaceEditingLayer.startEditing(inplaceContext);
        }

        @Override
        public void hideInspections() {
            myQuickFixManager.hideHint();
        }

        @Override
        public void showError(@NonNls String message, Throwable e) {
            CordovaDesignerEditorPanel.this.showError(message, e);
        }

        @Override
        public boolean isZoomSupported() {
            return CordovaDesignerEditorPanel.this.isZoomSupported();
        }

        @Override
        public void zoom(@NotNull ZoomType type) {
            CordovaDesignerEditorPanel.this.zoom(type);
        }

        @Override
        public void setZoom(double zoom) {
            CordovaDesignerEditorPanel.this.setZoom(zoom);
        }

        @Override
        public double getZoom() {
            return CordovaDesignerEditorPanel.this.getZoom();
        }
    }

    private final class MyLayeredPane extends JBLayeredPane implements Scrollable {
        public void doLayout() {
            for (int i = getComponentCount() - 1; i >= 0; i--) {
                Component component = getComponent(i);
                component.setBounds(0, 0, getWidth(), getHeight());
            }
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            Rectangle bounds = myScrollPane.getViewport().getBounds();
            Dimension size = getSceneSize(this);

            size.width = Math.max(size.width, bounds.width);
            size.height = Math.max(size.height, bounds.height);

            return size;
        }

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (orientation == SwingConstants.HORIZONTAL) {
                return visibleRect.width - 10;
            }
            return visibleRect.height - 10;
        }

        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private class FixableMessageAction extends AbstractComboBoxAction<FixableMessageInfo> {
        private final DefaultActionGroup myActionGroup = new DefaultActionGroup();
        private String myTitle;
        private boolean myIsAdded;

        public FixableMessageAction() {
            myActionPanel.getActionGroup().add(myActionGroup);

            Presentation presentation = getTemplatePresentation();
            presentation.setDescription("Warnings");
            presentation.setIcon(AllIcons.Ide.Warning_notifications);
        }

        public void show(java.util.List<FixableMessageInfo> messages) {
            if (!myIsAdded) {
                myTitle = Integer.toString(messages.size());
                setItems(messages, null);
                myActionGroup.add(this);
                myActionPanel.update();
                myIsAdded = true;
            }
        }

        public void hide() {
            if (myIsAdded) {
                myActionGroup.remove(this);
                myActionPanel.update();
                myIsAdded = false;
            }
        }

        @NotNull
        @Override
        protected DefaultActionGroup createPopupActionGroup(JComponent button) {
            DefaultActionGroup actionGroup = new DefaultActionGroup();
            for (final FixableMessageInfo message : myItems) {
                AnAction action;
                if ((message.myQuickFix != null && (message.myLinkText.length() > 0 || message.myAfterLinkText.length() > 0)) ||
                        (message.myAdditionalFixes != null && message.myAdditionalFixes.size() > 0)) {
                    final AnAction[] defaultAction = new AnAction[1];
                    DefaultActionGroup popupGroup = new DefaultActionGroup() {
                        @Override
                        public boolean canBePerformed(DataContext context) {
                            return true;
                        }

                        @Override
                        public void actionPerformed(AnActionEvent e) {
                            defaultAction[0].actionPerformed(e);
                        }
                    };
                    popupGroup.setPopup(true);
                    action = popupGroup;

                    if (message.myQuickFix != null && (message.myLinkText.length() > 0 || message.myAfterLinkText.length() > 0)) {
                        AnAction popupAction = new AnAction() {
                            @Override
                            public void actionPerformed(AnActionEvent e) {
                                message.myQuickFix.run();
                            }
                        };
                        popupAction.getTemplatePresentation().setText(cleanText(message.myLinkText + message.myAfterLinkText));
                        popupGroup.add(popupAction);
                        defaultAction[0] = popupAction;
                    }
                    if (message.myAdditionalFixes != null && message.myAdditionalFixes.size() > 0) {
                        for (final Pair<String, Runnable> pair : message.myAdditionalFixes) {
                            AnAction popupAction = new AnAction() {
                                @Override
                                public void actionPerformed(AnActionEvent e) {
                                    pair.second.run();
                                }
                            };
                            popupAction.getTemplatePresentation().setText(cleanText(pair.first));
                            popupGroup.add(popupAction);
                            if (defaultAction[0] == null) {
                                defaultAction[0] = popupAction;
                            }
                        }
                    }
                } else {
                    action = new EmptyAction(true);
                }
                actionGroup.add(action);
                update(message, action.getTemplatePresentation(), true);
            }
            return actionGroup;
        }

        @Override
        protected void update(FixableMessageInfo item, Presentation presentation, boolean popup) {
            if (popup) {
                presentation.setText(cleanText(item.myBeforeLinkText));
            } else {
                presentation.setText(myTitle);
            }
        }

        private String cleanText(String text) {
            if (text != null) {
                text = text.trim();
                text = StringUtil.replace(text, "&nbsp;", " ");
                text = StringUtil.replace(text, "\n", " ");

                StringBuilder builder = new StringBuilder();
                int length = text.length();
                boolean whitespace = false;

                for (int i = 0; i < length; i++) {
                    char ch = text.charAt(i);
                    if (ch == ' ') {
                        if (!whitespace) {
                            whitespace = true;
                            builder.append(ch);
                        }
                    } else {
                        whitespace = false;
                        builder.append(ch);
                    }
                }

                text = builder.toString();
            }
            return text;
        }

        @Override
        protected boolean selectionChanged(FixableMessageInfo item) {
            return false;
        }
    }

    public static final class ErrorInfo {
        public String myMessage;
        public String myDisplayMessage;

        public final java.util.List<FixableMessageInfo> myMessages = new ArrayList<FixableMessageInfo>();

        public Throwable myThrowable;

        public boolean myShowMessage = true;
        public boolean myShowStack = true;
        public boolean myShowLog;
    }

    public static final class FixableMessageInfo {
        public final boolean myErrorIcon;
        public final String myBeforeLinkText;
        public final String myLinkText;
        public final String myAfterLinkText;
        public final Runnable myQuickFix;
        public final java.util.List<Pair<String, Runnable>> myAdditionalFixes;

        public FixableMessageInfo(boolean errorIcon,
                                  String beforeLinkText,
                                  String linkText,
                                  String afterLinkText,
                                  Runnable quickFix,
                                  java.util.List<Pair<String, Runnable>> additionalFixes) {
            myErrorIcon = errorIcon;
            myBeforeLinkText = beforeLinkText;
            myLinkText = linkText;
            myAfterLinkText = afterLinkText;
            myQuickFix = quickFix;
            myAdditionalFixes = additionalFixes;
        }
    }

    private static class FixedHashMap<K, V> extends HashMap<K, V> {
        private final int mySize;
        private final java.util.List<K> myKeys = new LinkedList<K>();

        public FixedHashMap(int size) {
            mySize = size;
        }

        @Override
        public V put(K key, V value) {
            if (!myKeys.contains(key)) {
                if (myKeys.size() >= mySize) {
                    remove(myKeys.remove(0));
                }
                myKeys.add(key);
            }
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            if (myKeys.contains(key)) {
                int index = myKeys.indexOf(key);
                int last = myKeys.size() - 1;
                myKeys.set(index, myKeys.get(last));
                myKeys.set(last, (K) key);
            }
            return super.get(key);
        }
    }

    private class LayoutConfigurationListener implements RenderConfigurationListener {
        @Override
        public boolean changed(int flags) {
            if (isProjectClosed()) {
                return true;
            }

            if (myActive) {
                updateRenderer(false);
                updatePalette();

                saveState();
            } else {
                myNeedsRerendering = true;
            }

            return true;
        }
    }

    private void updatePalette() {
        try {
            CordovaPaletteToolWindowManager.getInstance(this).refresh();
        } catch (Throwable e) {
            // Pass
        }
    }

    /**
     * Layered pane which shows the rendered image, as well as (if applicable) an error message panel on top of the rendering
     * near the bottom
     */
    private static class MyRenderPanelWrapper extends JPanel {
        private final RenderErrorPanel myErrorPanel;
        private int myErrorPanelHeight = -1;

        public MyRenderPanelWrapper(@NotNull RenderErrorPanel errorPanel) {
            super(new BorderLayout());
            myErrorPanel = errorPanel;
            setBackground(null);
            setOpaque(false);
            add(errorPanel);
        }

        private RenderErrorPanel getErrorPanel() {
            return myErrorPanel;
        }

        @Override
        public void doLayout() {
            super.doLayout();
            positionErrorPanel();
        }

        private void positionErrorPanel() {
            int height = getHeight();
            int width = getWidth();
            int size;
            if (SIZE_ERROR_PANEL_DYNAMICALLY) {
                if (myErrorPanelHeight == -1) {
                    // Make the layout take up to 3/4ths of the height, and at least 1/4th, but
                    // anywhere in between based on what the actual text requires
                    size = height * 3 / 4;
                    int preferredHeight = myErrorPanel.getPreferredHeight(width) + 8;
                    if (preferredHeight < size) {
                        size = Math.max(preferredHeight, Math.min(height / 4, size));
                        myErrorPanelHeight = size;
                    }
                } else {
                    size = myErrorPanelHeight;
                }
            } else {
                size = height / 2;
            }

            myErrorPanel.setSize(width, size);
            myErrorPanel.setLocation(0, height - size);
        }
    }
}
