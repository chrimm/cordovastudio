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
package org.cordovastudio.editors.designer.designSurface.preview;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.components.JBScrollPane;
import org.cordovastudio.actions.RefreshRenderAction;
import org.cordovastudio.actions.RenderOptionsMenuBuilder;
import org.cordovastudio.editors.designer.rendering.*;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationManager;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationToolBar;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eugene.Kudelevsky
 */
public class PreviewToolWindowForm implements Disposable, RenderConfigurationListener, RenderContext,
        OverlayContainer {
    private JPanel myContentPanel;
    private PreviewPanel myPreviewPanel;
    private JBScrollPane myScrollPane;
    private JPanel mySecondToolBarPanel;
    private JPanel myFirstToolbarPanel;
    private PsiFile myFile;
    private RenderConfiguration myConfiguration;
    private CordovaFacet myFacet;
    private final PreviewToolWindowManager myToolWindowManager;
    private final ActionToolbar myActionToolBar;
    private final HoverOverlay myHover = new HoverOverlay(this);
    private final List<Overlay> myOverlays = Arrays.asList((Overlay) myHover);

    public PreviewToolWindowForm(final Project project, PreviewToolWindowManager toolWindowManager) {
        Disposer.register(this, myPreviewPanel);

        myToolWindowManager = toolWindowManager;

        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.addSeparator();
        actionGroup.add(new RefreshRenderAction(this));
        myActionToolBar = ActionManager.getInstance().createActionToolbar("LayoutPreview", actionGroup, true);
        myActionToolBar.setReservePlaceAutoPopupIcon(false);

        ActionToolbar optionsToolBar = RenderOptionsMenuBuilder.create(this, project).addHideOption().addDeviceFrameOption()
                .addRetinaOption().build();
        JComponent toolbar = myActionToolBar.getComponent();
        RenderConfigurationToolBar configToolBar = new RenderConfigurationToolBar(this);
        myFirstToolbarPanel.add(configToolBar, BorderLayout.CENTER);
        mySecondToolBarPanel.add(optionsToolBar.getComponent(), BorderLayout.EAST);
        mySecondToolBarPanel.add(toolbar, BorderLayout.CENTER);

        myContentPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                myPreviewPanel.updateImageSize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        myScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
        myScrollPane.getVerticalScrollBar().setUnitIncrement(5);

        myPreviewPanel.setOverlayContainer(this);
        myPreviewPanel.installHover(myHover);
        myPreviewPanel.setRenderContext(this);
    }

    public JPanel getContentPanel() {
        return myContentPanel;
    }

    public PsiFile getFile() {
        return myFile;
    }

    public boolean setFile(@Nullable PsiFile file) {
        final boolean fileChanged = !Comparing.equal(myFile, file);
        myFile = file;

        if (fileChanged) {
            if (myConfiguration != null) {
                myConfiguration.removeListener(this);
                myConfiguration = null;
            }


            if (file != null) {
                final VirtualFile virtualFile = file.getVirtualFile();
                if (virtualFile != null) {
                    myFacet = CordovaFacet.getInstance(file);
                    if (myFacet != null) {
                        RenderConfigurationManager manager = myFacet.getConfigurationManager();
                        myConfiguration = manager.getConfiguration(virtualFile);
                        myConfiguration.removeListener(this);
                        myConfiguration.addListener(this);
                    }
                }
            }
        }

        return true;
    }

    private void saveState() {
        if (myConfiguration != null) {
            myConfiguration.save();
        }
    }

    @Override
    public void dispose() {
    }

    @Nullable
    public RenderResult getRenderResult() {
        return myPreviewPanel.getRenderResult();
    }

    public void setRenderResult(@NotNull final RenderResult renderResult, @Nullable final TextEditor editor) {
        myPreviewPanel.setRenderResult(renderResult, editor);
    }

    @NotNull
    public PreviewPanel getPreviewPanel() {
        return myPreviewPanel;
    }

    public void updatePreviewPanel() {
        myPreviewPanel.update();
    }

    // ---- Implements RenderContext ----

    @Override
    @Nullable
    public RenderConfiguration getConfiguration() {
        return myConfiguration;
    }

    @Override
    public void setConfiguration(@NotNull RenderConfiguration configuration) {
        if (configuration != myConfiguration) {
            if (myConfiguration != null) {
                myConfiguration.removeListener(this);
            }
            myConfiguration = configuration;
            myConfiguration.addListener(this);
            changed(MASK_ALL);
            // TODO: Cause immediate toolbar updates?
        }
    }

    @Override
    public void requestRender() {
        if (myFile != null) {
            myToolWindowManager.render();
            myToolWindowManager.flush();
            myPreviewPanel.update();
        }
    }

    @Override
    @NotNull
    public UsageType getType() {
        return UsageType.XML_PREVIEW;
    }

    @Nullable
    @Override
    public XmlFile getXmlFile() {
        return (XmlFile) myFile;
    }

    @Nullable
    @Override
    public VirtualFile getVirtualFile() {
        return myFile != null ? myFile.getVirtualFile() : null;
    }

    @Nullable
    @Override
    public Module getModule() {
        if (myFile != null) {
            final CordovaFacet facet = CordovaFacet.getInstance(myFile);
            if (facet != null) {
                return facet.getModule();
            }
        }

        return null;
    }

    @Override
    public boolean hasAlphaChannel() {
        return myPreviewPanel.hasAlphaChannel();
    }

    @Override
    @NotNull
    public Component getComponent() {
        return myPreviewPanel.getRenderComponent();
    }

    @Override
    public void updateLayout() {
        myPreviewPanel.update();
        myPreviewPanel.getRenderComponent().repaint();
    }

    @Override
    public void setDeviceFramesEnabled(boolean on) {
        myPreviewPanel.setDeviceFramesEnabled(on);
    }

    @Nullable
    @Override
    public BufferedImage getRenderedImage() {
        RenderResult result = myPreviewPanel.getRenderResult();
        if (result != null) {
            RenderedImage renderedImage = result.getImage();
            if (renderedImage != null) {
                return renderedImage.getOriginalImage();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public RenderedViewHierarchy getViewHierarchy() {
        RenderResult result = myPreviewPanel.getRenderResult();
        if (result != null) {
            return result.getHierarchy();
        }
        return null;
    }

    @Override
    @NotNull
    public Dimension getFullImageSize() {
        return myPreviewPanel.getFullImageSize();
    }

    @Override
    @NotNull
    public Dimension getScaledImageSize() {
        return myPreviewPanel.getScaledImageSize();
    }

    @Override
    @NotNull
    public Rectangle getClientArea() {
        return myScrollPane.getViewport().getViewRect();
    }

    @Override
    public boolean supportsPreviews() {
        return true;
    }

    /**
     * Returns the preview manager for this render context, if any. Will only
     * be called if this context returns true from {@link #supportsPreviews()}.
     *
     * @param createIfNecessary if true, create the preview manager if it does not exist, otherwise
     *                          only return it if it has already been created
     * @return the preview manager, or null if it doesn't exist and {@code createIfNecessary} was false
     */
    @Nullable
    @Override
    public RenderPreviewManager getPreviewManager(boolean createIfNecessary) {
        return null;
    }

    @Override
    public void setMaxSize(int width, int height) {
        myPreviewPanel.setMaxSize(width, height);
    }

    /**
     * Perform a zoom to fit operation
     *
     * @param onlyZoomOut if true, only adjust the zoom if it would be zooming out
     * @param allowZoomIn if true, allow the zoom factor to be greater than 1 (e.g. bigger than real size)
     */
    @Override
    public void zoomFit(boolean onlyZoomOut, boolean allowZoomIn) {

    }

    // ---- Implements OverlayContainer ----

    @Override
    @NotNull
    public Rectangle fromModel(@NotNull Component target, @NotNull Rectangle rectangle) {
        assert myPreviewPanel != null;
        assert target == myPreviewPanel.getPaintComponent().getParent(); // Currently point conversion only supports this configuration
        Rectangle converted = myPreviewPanel.fromModelToScreen(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        return converted != null ? converted : rectangle;
    }

    @Override
    @NotNull
    public Rectangle toModel(@NotNull Component source, @NotNull Rectangle rectangle) {
        assert myPreviewPanel != null;
        assert source == myPreviewPanel.getPaintComponent().getParent(); // Currently point conversion only supports this configuration
        Rectangle converted = myPreviewPanel.fromScreenToModel(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        return converted != null ? converted : rectangle;
    }

    @Override
    @Nullable
    public List<Overlay> getOverlays() {
        return myOverlays;
    }

    @Override
    public boolean isSelected(@NotNull XmlTag tag) {
        return myPreviewPanel.isSelected(tag);
    }

    // ---- Implements ConfigurationListener ----

    @Override
    public boolean changed(int flags) {
        saveState();
        myToolWindowManager.render();

        return true;
    }

}
