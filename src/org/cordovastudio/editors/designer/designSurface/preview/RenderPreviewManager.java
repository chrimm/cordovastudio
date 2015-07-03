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
package org.cordovastudio.editors.designer.designSurface.preview;

import com.google.common.collect.Lists;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Alarm;
import com.intellij.util.ui.Animator;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.NestedRenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationManager;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.VaryingRenderConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static com.intellij.util.Alarm.ThreadToUse.POOLED_THREAD;
import static com.intellij.util.Alarm.ThreadToUse.SWING_THREAD;
import static org.cordovastudio.editors.designer.designSurface.preview.RenderPreviewMode.*;
import static org.cordovastudio.editors.designer.rendering.ShadowPainter.SHADOW_SIZE;
import static org.cordovastudio.editors.designer.rendering.ShadowPainter.SMALL_SHADOW_SIZE;

/**
 * Manager for the configuration previews, which handles layout computations,
 * managing the image buffer cache, etc
 * <p>
 * Missing:
 * <ol>
 * <li> Support for manual thumbnails </li>
 * <li> Support for included layouts </li>
 * </ol>
 */
public class RenderPreviewManager implements Disposable {
    public static final boolean SUPPORTS_MANUAL_PREVIEWS = false;
    static final int VERTICAL_GAP = 18;
    static final int HORIZONTAL_GAP = 12;
    static final int TITLE_HEIGHT = 14;

    private static double ourScale = 1.0;
    private static final int RENDER_DELAY = 150;
    private static final int MAX_WIDTH = 200;
    @SuppressWarnings("SuspiciousNameCombination")
    private static final int MAX_HEIGHT = MAX_WIDTH;
    private static boolean ZOOM_ENABLED = false;
    private static final int ZOOM_ICON_WIDTH = 16;
    private static final int ZOOM_ICON_HEIGHT = 16;
    private
    @Nullable
    List<RenderPreview> myPreviews;
    private
    @NotNull
    final RenderContext myRenderContext;
    private
    @NotNull
    RenderPreviewMode myMode = NONE;
    private
    @Nullable
    RenderPreview myActivePreview;
    private
    @Nullable
    VirtualFile myCurrentFile;
    private
    @Nullable
    SwapAnimation myAnimator;
    private int myLayoutHeight;
    private int myPrevCanvasWidth;
    private int myPrevCanvasHeight;
    private int myPrevImageWidth;
    private int myPrevImageHeight;
    private int myPendingRenderCount;

    /**
     * Last seen state revision in this {@link RenderPreviewManager}. If less
     * than {@link #ourRevision}, the previews need to be updated on next exposure
     */
    private int myRevision;
    /**
     * Current global revision count
     */
    private static int ourRevision;
    private boolean myNeedLayout;
    private boolean myNeedRender;

    /**
     * Whether we should render previews in a background thread or in the Swing thread.
     * There have been issues with this in the past, for example running into this
     * exception:
     * java.lang.IllegalStateException: After scene creation, #init() must be called
     * at com.android.layoutlib.bridge.impl.RenderAction.acquire(RenderAction.java:151)
     * <p>
     * However, it seems to work for now so enabled.
     */
    private static final boolean RENDER_ASYNC = true;

    @SuppressWarnings("ConstantConditions")
    private final
    @NotNull
    Alarm myAlarm = RENDER_ASYNC ? new Alarm(POOLED_THREAD, this) : new Alarm(SWING_THREAD, this);

    /**
     * Creates a {@link RenderPreviewManager} associated with the given canvas
     *
     * @param canvas the canvas to manage previews for
     */
    public RenderPreviewManager(@NotNull RenderContext canvas) {
        myRenderContext = canvas;
    }

    /**
     * Remove mouse listeners for this preview manager for the given component
     */
    public void unregisterMouseListener(@NotNull Component source) {
        source.removeMouseListener(myPreviewMouseListener);
        source.removeMouseMotionListener(myPreviewMouseListener);
    }

    /**
     * Add mouse listeners for this preview manager for the given component
     */
    public void registerMouseListener(@NotNull Component source) {
        if (myPreviewMouseListener == null) {
            myPreviewMouseListener = new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent mouseEvent) {
                    super.mouseMoved(mouseEvent);
                    RenderPreviewManager.this.moved(mouseEvent);
                }

                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    super.mouseClicked(mouseEvent);
                    RenderPreviewManager.this.click(mouseEvent);
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    super.mouseEntered(mouseEvent);
                    RenderPreviewManager.this.enter(mouseEvent);
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    super.mouseExited(mouseEvent);
                    RenderPreviewManager.this.exit(mouseEvent);
                }
            };
        }
        source.addMouseListener(myPreviewMouseListener);
        source.addMouseMotionListener(myPreviewMouseListener);
    }

    private MouseAdapter myPreviewMouseListener;

    /**
     * Revise the global state revision counter. This will cause all layout
     * preview managers to refresh themselves to the latest revision when they
     * are next exposed.
     */
    public static void bumpRevision() {
        ourRevision++;
    }

    /**
     * Returns the associated render context
     *
     * @return the render context
     */
    @NotNull
    public RenderContext getRenderContext() {
        return myRenderContext;
    }

    /**
     * Zooms in (grows all previews)
     */
    public void zoomIn() {
        if (ZOOM_ENABLED) {
            setScale(ourScale * (1 / 0.9));
            updatedZoom();
        }
    }

    /**
     * Zooms out (shrinks all previews)
     */
    public void zoomOut() {
        if (ZOOM_ENABLED) {
            setScale(ourScale * (0.9 / 1));
            updatedZoom();
        }
    }

    /**
     * Zooms to 100 (resets zoom)
     */
    public void zoomReset() {
        if (ZOOM_ENABLED) {
            setScale(1.0);
            updatedZoom();
            myNeedLayout = true;
            redraw();
        }
    }

    private static void setScale(double newScale) {
        assert ZOOM_ENABLED;
        ourScale = newScale;
        if (Math.abs(ourScale - 1.0) < 0.0001) {
            ourScale = 1.0;
        }
    }

    private void updatedZoom() {
        if (ZOOM_ENABLED) {
            if (hasPreviews()) {
                assert myPreviews != null;
                for (RenderPreview preview : myPreviews) {
                    preview.disposeThumbnail();
                }
                RenderPreview preview = getStashedPreview();
                if (preview != null) {
                    preview.disposeThumbnail();
                }
            }

            myNeedLayout = myNeedRender = true;
            redraw();
        }
    }

    static int getMaxWidth() {
        return (int) (ourScale * MAX_WIDTH);
    }

    static int getMaxHeight() {
        return (int) (ourScale * MAX_HEIGHT);
    }

    static double getScale() {
        return ourScale;
    }

    /**
     * Returns whether there are any manual preview items (provided the current
     * mode is manual previews
     *
     * @return true if there are items in the manual preview list
     */
    @SuppressWarnings("ConstantConditions")
    public boolean hasManualPreviews() {
        assert myMode == CUSTOM;
        // Not yet implemented; this shouldn't be called
        assert !SUPPORTS_MANUAL_PREVIEWS;
        return false;
    }

    /**
     * Delete all the previews
     */
    @SuppressWarnings("ConstantConditions")
    public void deleteManualPreviews() {
        disposePreviews();
        selectMode(NONE);
        myRenderContext.zoomFit(true /* onlyZoomOut */, false /*allowZoomIn*/);

        // Not yet implemented; this shouldn't be called
        assert !SUPPORTS_MANUAL_PREVIEWS;
    }

    /**
     * Dispose all the previews
     */
    public void disposePreviews() {
        if (myPreviews != null) {
            List<RenderPreview> old = myPreviews;
            myPreviews = null;
            for (RenderPreview preview : old) {
                preview.dispose();
            }
        }
    }

    /**
     * Deletes the given preview
     *
     * @param preview the preview to be deleted
     */
    @SuppressWarnings("ConstantConditions")
    public void deletePreview(@NotNull RenderPreview preview) {
        assert myPreviews != null;
        RenderPreviewMode.deleteId(preview.getId());
        myPreviews.remove(preview);
        preview.dispose();
        layout(true);
        redraw();

        if (SUPPORTS_MANUAL_PREVIEWS) {
            assert false; // Need to update persistent list here when manual previews is supported
        }
    }

    /**
     * Compute the total width required for the previews, including internal padding
     *
     * @return total width in pixels
     */
    public int computePreviewWidth() {
        if (hasPreviews()) {
            assert myPreviews != null;
            int minPreviewWidth = myPreviews.get(0).getLayoutWidth();
            for (RenderPreview preview : myPreviews) {
                minPreviewWidth = Math.min(minPreviewWidth, preview.getLayoutWidth());
            }

            if (minPreviewWidth > 0) {
                minPreviewWidth += HORIZONTAL_GAP;
                minPreviewWidth += SMALL_SHADOW_SIZE;
            }

            return minPreviewWidth;
        }

        return 0;
    }

    /**
     * Layout Algorithm. This sets the {@link RenderPreview#getX()} and
     * {@link RenderPreview#getY()} coordinates of all the previews. It also
     * marks previews as visible or invisible via
     * {@link RenderPreview#setVisible(boolean)} according to their position and
     * the current visible view port in the layout canvas. Finally, it also sets
     * the {@code myLayoutHeight} field, such that the scrollbars can compute the
     * right scrolled area, and that scrolling can cause render refreshes on
     * views that are made visible.
     * <p>
     * This is not a traditional bin packing problem, because the objects to be
     * packaged do not have a fixed size; we can scale them up and down in order
     * to provide an "optimal" size.
     * <p>
     * See http://en.wikipedia.org/wiki/Packing_problem See
     * http://en.wikipedia.org/wiki/Bin_packing_problem
     */
    void layout(boolean refresh) {
        myNeedLayout = false;

        if (myPreviews == null || myPreviews.isEmpty()) {
            return;
        }

        Rectangle clientArea = myRenderContext.getClientArea();
        Dimension scaledImageSize = myRenderContext.getScaledImageSize();
        int scaledImageWidth = scaledImageSize.width;
        int scaledImageHeight = scaledImageSize.height;

        if (!refresh &&
                (scaledImageWidth == myPrevImageWidth &&
                        scaledImageHeight == myPrevImageHeight &&
                        clientArea.width == myPrevCanvasWidth &&
                        clientArea.height == myPrevCanvasHeight)) {
            // No change
            return;
        }

        myPrevImageWidth = scaledImageWidth;
        myPrevImageHeight = scaledImageHeight;
        myPrevCanvasWidth = clientArea.width;
        myPrevCanvasHeight = clientArea.height;

        beginRenderScheduling();

        myLayoutHeight = 0;

        RenderContext.UsageType usageType = myRenderContext.getType();
        if (!ourClassicLayout && (usageType == RenderContext.UsageType.XML_PREVIEW || usageType == RenderContext.UsageType.LAYOUT_EDITOR)) {
            // Quadrant rendering. In XML preview, the "main" rendering isn't
            // directly edited, so doesn't need to be nearly as visually prominent;
            // instead we subdivide the space equally and size all the thumbnails
            // equally
            tiledLayout();
        } else if (previewsHaveIdenticalSize() || fixedOrder()) {
            // If all the preview boxes are of identical sizes, or if the order is predetermined,
            // just lay them out in rows.
            rowLayout();
        } else if (previewsFit()) {
            layoutFullFit();
        } else {
            rowLayout();
        }
    }

    private void tiledLayout() {
        assert myPreviews != null;
        PreviewTileLayout tileLayout = new PreviewTileLayout(myPreviews, myRenderContext, fixedOrder());
        tileLayout.performLayout();
        myFixedRenderSize = tileLayout.getFixedRenderSize();
        myLayoutHeight = tileLayout.getLayoutHeight();
    }

    private Dimension myFixedRenderSize;

    @Nullable
    public Dimension getFixedRenderSize() {
        return myFixedRenderSize;
    }

    void redraw() {
        myRenderContext.getComponent().repaint();
    }

    /**
     * Performs a simple layout where the views are laid out in a row, wrapping
     * around the top left canvas image.
     */
    private void rowLayout() {
        assert myPreviews != null;
        PreviewRowLayout tileLayout = new PreviewRowLayout(myPreviews, myRenderContext, fixedOrder());
        tileLayout.performLayout();
        myLayoutHeight = tileLayout.getLayoutHeight();
    }

    private boolean fixedOrder() {
        return myMode == SCREENS;
    }

    /**
     * Returns true if all the previews have the same identical size
     */
    private boolean previewsHaveIdenticalSize() {
        if (!hasPreviews()) {
            return true;
        }

        assert myPreviews != null;
        Iterator<RenderPreview> iterator = myPreviews.iterator();
        RenderPreview first = iterator.next();
        int width = first.getLayoutWidth();
        int height = first.getLayoutHeight();

        while (iterator.hasNext()) {
            RenderPreview preview = iterator.next();
            if (width != preview.getLayoutWidth() || height != preview.getLayoutHeight()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if all the previews can fully fit in the available space
     */
    private boolean previewsFit() {
        assert myPreviews != null;
        Rectangle clientArea = myRenderContext.getClientArea();
        Dimension scaledImageSize = myRenderContext.getScaledImageSize();
        int scaledImageWidth = scaledImageSize.width;
        int scaledImageHeight = scaledImageSize.height;

        int availableWidth = clientArea.x + clientArea.width - getX();
        int availableHeight = clientArea.y + clientArea.height - getY();
        int bottomBorder = scaledImageHeight + SHADOW_SIZE;
        int rightHandSide = scaledImageWidth + HORIZONTAL_GAP;

        // First see if we can fit everything; if so, we can try to make the layouts
        // larger such that they fill up all the available space
        long availableArea = rightHandSide * bottomBorder + availableWidth * (Math.max(0, availableHeight - bottomBorder));

        long requiredArea = 0;
        for (RenderPreview preview : myPreviews) {
            // Note: This does not include individual preview scale; the layout
            // algorithm itself may be tweaking the scales to fit elements within
            // the layout
            requiredArea += preview.getArea();
        }

        return requiredArea * ourScale < availableArea;
    }

    private void layoutFullFit() {
        assert myPreviews != null;
        PreviewBinPackingLayout tileLayout = new PreviewBinPackingLayout(myPreviews, myRenderContext, getX(), getY());
        if (tileLayout.performLayout()) {
            myLayoutHeight = tileLayout.getLayoutHeight();
        } else {
            rowLayout();
        }
    }

    /**
     * Paints the configuration previews
     *
     * @param gc the graphics context to paint into
     */
    public void paint(Graphics2D gc) {
        if (hasPreviews()) {
            assert myPreviews != null;
            // Ensure up to date at all times; consider moving if it's too expensive
            layout(myNeedLayout);
            if (myNeedRender) {
                renderPreviews();
            }
            int rootX = getX();
            int rootY = getY();

            RenderConfiguration canvasConfiguration = myRenderContext.getConfiguration();
            if (canvasConfiguration == null) {
                return;
            }

            Rectangle clientArea = myRenderContext.getClientArea();
            for (RenderPreview preview : myPreviews) {
                if (preview.isVisible()) {
                    int x = rootX + preview.getX();
                    int y = rootY + preview.getY();
                    gc.setClip(0, 0, clientArea.width, clientArea.height);
                    preview.paint(gc, x, y);
                }
            }

      /* TODO: Render a render-preview like label over the main rendering
      RenderPreview preview = getStashedPreview();
      if (preview != null) {
        String displayName = null;
        Configuration configuration = preview.getConfiguration();
        if (configuration instanceof VaryingConfiguration) {
          // Use override flags from stashed preview, but configuration
          // data from live (not varying) configured configuration
          VaryingConfiguration cfg = (VaryingConfiguration)configuration;
          int flags = cfg.getAlternateFlags() | cfg.getOverrideFlags();
          displayName = NestedConfiguration.computeDisplayName(flags, canvasConfiguration);
        }
        else if (configuration instanceof NestedConfiguration) {
          int flags = ((NestedConfiguration)configuration).getOverrideFlags();
          displayName = NestedConfiguration.computeDisplayName(flags, canvasConfiguration);
        }
        else {
          displayName = configuration.getDisplayName();
        }
        if (displayName != null) {
          int x = destX + destWidth / 2 - preview.getWidth() / 2;
          int y = destY + destHeight;
          preview.paintTitle(gc, x, y, false, displayName);
        }
      }
      */
        }

        if (myAnimator != null) {
            myAnimator.paint(gc);
        }
    }

    private void addPreview(@NotNull RenderPreview preview) {
        String id = preview.getId();
        if (id == null) {
            id = preview.getDisplayName();
            preview.setId(id);
        }

        if (RenderPreviewMode.isDeletedId(id)) {
            return;
        }

        if (myPreviews == null) {
            myPreviews = Lists.newArrayList();
        }
        myPreviews.add(preview);
    }

    /**
     * Adds the current configuration as a new configuration preview
     */
    @SuppressWarnings("ConstantConditions")
    public void addAsThumbnail() {
        assert !SUPPORTS_MANUAL_PREVIEWS; // Not yet implemented
    }

    /**
     * Computes a unique new name for a configuration preview that represents
     * the current, default configuration
     *
     * @return a unique name
     */
    @SuppressWarnings("UnusedDeclaration")
    private String getUniqueName() {
        if (SUPPORTS_MANUAL_PREVIEWS) {
            if (myPreviews != null && !myPreviews.isEmpty()) {
                Set<String> names = new HashSet<String>(myPreviews.size());
                for (RenderPreview preview : myPreviews) {
                    names.add(preview.getDisplayName());
                }

                int index = 2;
                while (true) {
                    String name = String.format("Config%1$d", index);
                    if (!names.contains(name)) {
                        return name;
                    }
                    index++;
                }
            }
        }
        return "Config1";
    }

    /**
     * Generates a bunch of default configuration preview thumbnails
     */
    public void addDefaultPreviews() {
        RenderConfiguration parent = myRenderContext.getConfiguration();
        if (parent instanceof NestedRenderConfiguration) {
            parent = ((NestedRenderConfiguration) parent).getParent();
        }

        if (parent == null) {
            return;
        }

        // Vary screen size
        // TODO: Be smarter here: Pick a screen that is both as differently as possible
        // from the current screen as well as also supported. So consider
        // things like supported screens, targetSdk etc.
        createScreenVariations(parent);

        // Vary orientation
        createStateVariation(parent);

        // Vary render target
        createRenderTargetVariation(parent);

        // Also add in include-context previews, if any
        // TODO: Implement this
        //addIncludedInPreviews();

        // Make a placeholder preview for the current screen, in case we switch from it
        RenderPreview preview = RenderPreview.create(this, parent, false);
        setStashedPreview(preview);

        sortPreviewsByOrientation();
    }

    private void createRenderTargetVariation(@SuppressWarnings("UnusedParameters") @NotNull RenderConfiguration parent) {
        /* This is disabled for now: need to load multiple versions of layoutlib.
        When I did this, there seemed to be some drug interactions between
        them, and I would end up with NPEs in layoutlib code which normally works.
        VaryingConfiguration configuration =
                VaryingConfiguration.create(chooser, parent);
        configuration.setAlternatingTarget(true);
        configuration.syncFolderConfig();
        addPreview(RenderPreview.create(this, configuration));
        */
    }

    private void createStateVariation(@NotNull RenderConfiguration parent) {
        State currentState = parent.getDeviceState();
        State nextState = parent.getNextDeviceState(currentState);
        if (nextState != currentState) {
            VaryingRenderConfiguration configuration = VaryingRenderConfiguration.create(parent);
            configuration.setAlternateDeviceState(true);
            addPreview(RenderPreview.create(this, configuration, false));
        }
    }

    private void createScreenVariations(@NotNull RenderConfiguration parent) {
        VaryingRenderConfiguration configuration;

        configuration = VaryingRenderConfiguration.create(parent);
        configuration.setVariation(0);
        configuration.setAlternateDevice(true);
        addPreview(RenderPreview.create(this, configuration, false));

        configuration = VaryingRenderConfiguration.create(parent);
        configuration.setVariation(1);
        configuration.setAlternateDevice(true);
        addPreview(RenderPreview.create(this, configuration, false));
    }

    /**
     * Returns the current mode as seen by this {@link RenderPreviewManager}.
     * Note that it may not yet have been synced with the global mode kept in
     * {@link RenderPreviewMode#getCurrent()}.
     *
     * @return the current preview mode
     */
    @NotNull
    public RenderPreviewMode getMode() {
        return myMode;
    }

    /**
     * Update the set of previews for the current mode
     *
     * @param force force a refresh even if the preview type has not changed
     * @return true if the views were recomputed, false if the previews were
     * already showing and the mode not changed
     */
    public boolean recomputePreviews(boolean force) {
        RenderPreviewMode newMode = getCurrent();
        myCurrentFile = myRenderContext.getVirtualFile();

        if (newMode == myMode && !force && (myRevision == ourRevision || myMode == NONE || myMode == CUSTOM)) {
            return false;
        }

        myMode = newMode;
        myRevision = ourRevision;

        if (ZOOM_ENABLED) {
            setScale(1.0);
        }
        disposePreviews();

        // Only show device frames when showing screen sizes
        myRenderContext.setDeviceFramesEnabled(myMode == NONE || myMode == SCREENS);

        switch (myMode) {
            case DEFAULT:
                addDefaultPreviews();
                break;
            case SCREENS:
                addScreenSizePreviews();
                break;
            case VARIATIONS:
                addVariationPreviews();
                break;
            case CUSTOM:
                addManualPreviews();
                break;
            case NONE:
                // Can't just set myNeedZoom because with no previews, the paint
                // method does nothing
                myRenderContext.zoomFit(false /* onlyZoomOut */, false /*allowZoomIn*/);
                myFixedRenderSize = null;
                myRenderContext.setMaxSize(0, 0);
                break;
            default:
                assert false : myMode;
        }

        // We schedule layout for the next redraw rather than process it here immediately;
        // not only does this let us avoid doing work for windows where the tab is in the
        // background, but when a file is opened we may not know the size of the canvas
        // yet, and the layout methods need it in order to do a good job. By the time
        // the canvas is painted, we have accurate bounds.
        myNeedLayout = myNeedRender = true;
        myRenderContext.updateLayout();
        layout(true);
        redraw();

        return true;
    }

    /**
     * Sets the new render preview mode to use
     *
     * @param mode the new mode
     */
    public void selectMode(@NotNull RenderPreviewMode mode) {
        if (mode != myMode) {
            setCurrent(mode);

            recomputePreviews(false);
        }
    }

    /**
     * Similar to {@link #addDefaultPreviews()} but for screen sizes
     */
    public void addScreenSizePreviews() {
        RenderConfiguration configuration = myRenderContext.getConfiguration();
        if (configuration == null) {
            return;
        }

        if (myPreviews != null && myPreviews.size() == 1) {
            // If there is only one other device, don't show a label on it; it will be obvious from
            // the context and just looks inconsistent (since there is no label on the main device)
            myPreviews.get(0).setDisplayName("");
        }

        RenderPreview preview = RenderPreview.create(this, configuration, true);
        setStashedPreview(preview);

        // Sorted by screen size, in decreasing order
        sortPreviewsByScreenSize();
    }

    /**
     * Previews this layout as included in other layouts
     */
    public void addVariationPreviews() {
        RenderConfiguration configuration = myRenderContext.getConfiguration();
        if (configuration == null) {
            return;
        }

        /*

        Commented out for now, as we not yet support multiple variations
        //TODO Figure out how to work with variantions if not with multiple resources/files

        VirtualFile file = configuration.getFile();
        List<VirtualFile> variations = ResourceHelper.getResourceVariations(file, false);

        // Sort by parent folder
        Collections.sort(variations, new Comparator<VirtualFile>() {
            @Override
            public int compare(VirtualFile file1, VirtualFile file2) {
                return file1.getParent().getName().compareTo(file2.getParent().getName());
            }
        });

        for (VirtualFile variation : variations) {
            String title = variation.getParent().getName();
            RenderConfiguration variationConfiguration = RenderConfiguration.create(configuration, variation);
            variationConfiguration.setTheme(configuration.getTheme());
            RenderPreview preview = RenderPreview.create(this, variationConfiguration, false);
            preview.setDisplayName(title);
            preview.setAlternateInput(variation);

            addPreview(preview);
        }

        */

        sortPreviewsByOrientation();
    }

    /**
     * Previews this layout using a custom configured set of layouts
     */
    @SuppressWarnings("ConstantConditions")
    public void addManualPreviews() {
        // Not yet implemented; this shouldn't be called
        assert !SUPPORTS_MANUAL_PREVIEWS;
    }

    /**
     * Notifies that the main configuration has changed.
     *
     * @param flags the change flags, a bitmask corresponding to the
     *              {@code CHANGE_} constants in {@link org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener}
     */
    public void configurationChanged(int flags) {
        // Similar to renderPreviews, but only acts on incomplete previews
        if (hasPreviews()) {
            assert myPreviews != null;
            // Do zoomed images first
            beginRenderScheduling();
            for (RenderPreview preview : myPreviews) {
                if (preview.getScale() > 1.2) {
                    preview.configurationChanged(flags);
                }
            }
            for (RenderPreview preview : myPreviews) {
                if (preview.getScale() <= 1.2) {
                    preview.configurationChanged(flags);
                }
            }
            RenderPreview preview = getStashedPreview();
            if (preview != null) {
                preview.configurationChanged(flags);
                preview.dispose();
            }
            myNeedLayout = true;
            myNeedRender = true;
            redraw();
        }
    }

    /**
     * Updates the configuration preview thumbnails
     */
    public void renderPreviews() {
        if (myRenderContext.getVirtualFile() != myCurrentFile) {
            recomputePreviews(true);
            return;
        }

        if (hasPreviews()) {
            assert myPreviews != null;
            beginRenderScheduling();

            myAlarm.cancelAllRequests();

            // Process in visual order
            ArrayList<RenderPreview> visualOrder = new ArrayList<RenderPreview>(myPreviews);
            Collections.sort(visualOrder, RenderPreview.VISUAL_ORDER);

            // Do zoomed images first
            for (RenderPreview preview : visualOrder) {
                if (preview.getScale() > 1.2 && preview.isVisible()) {
                    scheduleRender(preview);
                }
            }
            // Non-zoomed images
            for (RenderPreview preview : visualOrder) {
                if (preview.getScale() <= 1.2 && preview.isVisible()) {
                    scheduleRender(preview);
                }
            }
        }

        myNeedRender = false;

        if (myClassicLayout != ourClassicLayout) {
            setClassicLayout(ourClassicLayout);
        }
    }

    /**
     * Reset rendering scheduling. The next render request will be scheduled
     * after a single delay unit.
     */
    public void beginRenderScheduling() {
        myPendingRenderCount = 0;
    }

    /**
     * Schedule rendering the given preview. Each successive call will add an additional
     * delay unit to the schedule from the previous {@link #scheduleRender(RenderPreview)}
     * call, until {@link #beginRenderScheduling()} is called again.
     *
     * @param preview the preview to render
     */
    public void scheduleRender(@NotNull RenderPreview preview) {
        myPendingRenderCount++;
        scheduleRender(preview, myPendingRenderCount * RENDER_DELAY);
    }

    /**
     * Schedule rendering the given preview.
     *
     * @param preview the preview to render
     * @param delay   the delay to wait before rendering
     */
    public void scheduleRender(@NotNull final RenderPreview preview, long delay) {
        Runnable pending = preview.getPendingRendering();
        if (pending != null) {
            myAlarm.cancelRequest(pending);
        }
        Runnable request = new Runnable() {
            @Override
            public void run() {
                preview.setPendingRendering(null);
                preview.updateSize();
                preview.renderSync();
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        redraw();
                    }
                });
            }
        };
        preview.setPendingRendering(request);
        myAlarm.addRequest(request, delay);
    }

    /**
     * Switch to the given configuration preview
     *
     * @param preview the preview to switch to
     */
    public void switchTo(@NotNull RenderPreview preview) {
        assert myPreviews != null;
        RenderConfiguration originalConfiguration = myRenderContext.getConfiguration();
        if (originalConfiguration == null) {
            return;
        }

        VirtualFile input = preview.getAlternateInput();
        if (input != null) {
            // This switches to the given file, but the file might not have
            // an identical configuration to what was shown in the preview.
            // For example, while viewing a 10" layout-xlarge file, it might
            // show a preview for a 5" version tied to the default layout. If
            // you click on it, it will open the default layout file, but it might
            // be using a different screen size; any of those that match the
            // default layout, say a 3.8".
            //
            // Thus, we need to also perform a screen size sync first
            RenderConfiguration configuration = preview.getConfiguration();
            if (configuration instanceof NestedRenderConfiguration) {
                NestedRenderConfiguration nestedConfig = (NestedRenderConfiguration) configuration;
                boolean setSize = nestedConfig.isOverridingDevice();
                if (configuration instanceof VaryingRenderConfiguration) {
                    VaryingRenderConfiguration c = (VaryingRenderConfiguration) configuration;
                    setSize |= c.isAlternatingDevice();
                }

                if (setSize) {
                    RenderConfigurationManager configurationManager = originalConfiguration.getConfigurationManager();
                    VirtualFile editedFile = originalConfiguration.getFile();
                    assert editedFile != null;

                    /* Commented out for now, as we do not make use of variantions
                        TODO: re-comment-in if we use variantions

                    configurationManager.syncToVariations(CFG_DEVICE | CFG_DEVICE_STATE, editedFile, configuration, false, false);
                    */
                }
            }

            Project project = configuration.getModule().getProject();
            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, input, -1);
            FileEditorManager.getInstance(project).openEditor(descriptor, true);
            // TODO: Ensure that the layout editor is focused?

            return;
        }

        // The new configuration is the configuration which will become the configuration
        // in the layout editor's chooser
        RenderConfiguration previewConfiguration = preview.getConfiguration();
        RenderConfiguration newConfiguration = previewConfiguration;
        if (newConfiguration instanceof NestedRenderConfiguration) {
            // Should never use a complementing configuration for the main
            // rendering's configuration; instead, create a new configuration
            // with a snapshot of the configuration's current values
            newConfiguration = previewConfiguration.clone();

            // Remap all the previews to be parented to this new copy instead
            // of the old one (which is no longer controlled by the chooser)
            for (RenderPreview p : myPreviews) {
                RenderConfiguration configuration = p.getConfiguration();
                if (configuration instanceof NestedRenderConfiguration) {
                    NestedRenderConfiguration nested = (NestedRenderConfiguration) configuration;
                    nested.setParent(newConfiguration);
                }
            }
        }

        // Make a preview for the configuration which *was* showing in the
        // chooser up until this point:
        RenderPreview newPreview = getStashedPreview();
        if (newPreview == null) {
            newPreview = RenderPreview.create(this, originalConfiguration, false);
        }

        // Update its configuration such that it is complementing or inheriting
        // from the new chosen configuration
        if (previewConfiguration instanceof VaryingRenderConfiguration) {
            VaryingRenderConfiguration varying = VaryingRenderConfiguration.create((VaryingRenderConfiguration) previewConfiguration, newConfiguration);
            varying.updateDisplayName();
            originalConfiguration = varying;
            newPreview.setConfiguration(originalConfiguration);
        } else if (previewConfiguration instanceof NestedRenderConfiguration) {
            NestedRenderConfiguration nested =
                    NestedRenderConfiguration.create((NestedRenderConfiguration) previewConfiguration, originalConfiguration, newConfiguration);
            nested.setDisplayName(nested.computeDisplayName());
            originalConfiguration = nested;
            newPreview.setConfiguration(originalConfiguration);
        }

        // Replace clicked preview with preview of the formerly edited main configuration
        // This doesn't work yet because the image overlay has had its image
        // replaced by the configuration previews! I should make a list of them
        for (int i = 0, n = myPreviews.size(); i < n; i++) {
            if (preview == myPreviews.get(i)) {
                myPreviews.set(i, newPreview);
                break;
            }
        }

        // Stash the corresponding preview (not active) on the canvas so we can
        // retrieve it if clicking to some other preview later
        setStashedPreview(preview);
        preview.setVisible(false);

        // Switch to the configuration from the clicked preview (though it's
        // most likely a copy, see above)
        myRenderContext.setConfiguration(newConfiguration);

        myNeedLayout = true;
        redraw();

        animateTransition(preview, newPreview);
    }

    private void animateTransition(RenderPreview preview, RenderPreview newPreview) {
        myAnimator = new SwapAnimation(preview, newPreview);
        myAnimator.resume();
    }

    /**
     * Gets the preview at the given location, or null if none. This is
     * currently deeply tied to where things are painted in onPaint().
     */
    @Nullable
    RenderPreview getPreview(MouseEvent mouseEvent) {
        if (hasPreviews()) {
            assert myPreviews != null;
            int rootX = getX();
            int mouseX = mouseEvent.getX();
            if (mouseX < rootX) {
                return null;
            }
            int rootY = getY();
            int mouseY = mouseEvent.getY();

            for (RenderPreview preview : myPreviews) {
                int x = rootX + preview.getX();
                int y = rootY + preview.getY();
                if (mouseX >= x && mouseX <= x + preview.getWidth()) {
                    if (mouseY >= y && mouseY <= y + preview.getHeight()) {
                        return preview;
                    }
                }
            }
        }

        return null;
    }

    private static int getX() {
        return 0;
    }

    private static int getY() {
        return 0;
    }

    private int getZoomX() {
        if (ZOOM_ENABLED) {
            Rectangle clientArea = myRenderContext.getClientArea();
            int x = clientArea.x + clientArea.width - ZOOM_ICON_WIDTH;
            return x - 6;
        }
        return 0;
    }

    private int getZoomY() {
        if (ZOOM_ENABLED) {
            Rectangle clientArea = myRenderContext.getClientArea();
            return clientArea.y + 5;
        }
        return 0;
    }

    /**
     * Returns the height of the layout
     *
     * @return the height
     */
    public int getHeight() {
        return myLayoutHeight;
    }

    /**
     * Notifies that preview manager that the mouse cursor has moved to the
     * given control position within the layout canvas
     *
     * @param mouseEvent the mouse event
     */
    public void moved(MouseEvent mouseEvent) {
        RenderPreview hovered = getPreview(mouseEvent);
        if (hovered != myActivePreview) {
            if (myActivePreview != null) {
                myActivePreview.setActive(false);
            }
            myActivePreview = hovered;
            if (myActivePreview != null) {
                myActivePreview.setActive(true);
            }
            redraw();
        }
    }

    /**
     * Notifies that preview manager that the mouse cursor has entered the layout canvas
     *
     * @param mouseEvent the mouse event
     */
    public void enter(MouseEvent mouseEvent) {
        moved(mouseEvent);
    }

    /**
     * Notifies that preview manager that the mouse cursor has exited the layout canvas
     *
     * @param mouseEvent the mouse event
     */
    @SuppressWarnings("UnusedParameters")
    public void exit(MouseEvent mouseEvent) {
        if (myActivePreview != null) {
            myActivePreview.setActive(false);
        }
        myActivePreview = null;
        redraw();
    }

    /**
     * Process a mouse click, and return true if it was handled by this manager
     * (e.g. the click was on a preview)
     *
     * @param mouseEvent the mouse event
     * @return true if the click occurred over a preview and was handled, false otherwise
     */
    public boolean click(MouseEvent mouseEvent) {
        // Clicked zoom?
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();

        if (ZOOM_ENABLED) {
            int x = getZoomX();
            if (x > 0) {
                if (mouseX >= x && mouseX <= x + ZOOM_ICON_WIDTH) {
                    int y = getZoomY();
                    if (mouseY >= y && mouseY <= y + 4 * ZOOM_ICON_HEIGHT) {
                        if (mouseY < y + ZOOM_ICON_HEIGHT) {
                            zoomIn();
                            mouseEvent.consume();
                        } else if (mouseY < y + 2 * ZOOM_ICON_HEIGHT) {
                            zoomOut();
                            mouseEvent.consume();
                        } else if (mouseY < y + 3 * ZOOM_ICON_HEIGHT) {
                            zoomReset();
                            mouseEvent.consume();
                        } else {
                            selectMode(NONE);
                            mouseEvent.consume();
                        }
                        return true;
                    }
                }
            }
        }

        RenderPreview preview = getPreview(mouseEvent);
        if (preview != null) {
            boolean handled = preview.click(mouseX - getX() - preview.getX(), mouseY - getY() - preview.getY());
            if (handled) {
                // In case layout was performed, there could be a new preview
                // under this coordinate now, so make sure it's hover etc
                // shows up
                moved(mouseEvent);
                mouseEvent.consume();
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if there are thumbnail previews
     *
     * @return true if thumbnails are being shown
     */
    public boolean hasPreviews() {
        return myPreviews != null && !myPreviews.isEmpty();
    }


    private void sortPreviewsByScreenSize() {
        if (myPreviews != null) {
            Collections.sort(myPreviews, new Comparator<RenderPreview>() {
                @Override
                public int compare(RenderPreview preview1, RenderPreview preview2) {
                    RenderConfiguration config1 = preview1.getConfiguration();
                    RenderConfiguration config2 = preview2.getConfiguration();

                    Device device1 = config1.getDevice();
                    Device device2 = config2.getDevice();
                    if (device1 != null && device2 != null) {
                        double delta = device1.getScreenDiagonalLength() - device2.getScreenDiagonalLength();
                        return (int) Math.signum(delta);
                    }

                    State state1 = config1.getDeviceState();
                    State state2 = config2.getDeviceState();
                    if (state1 != state2 && state1 != null && state2 != null) {
                        return state1.getName().compareTo(state2.getName());
                    }

                    return preview1.getDisplayName().compareTo(preview2.getDisplayName());
                }
            });
        }
    }

    private void sortPreviewsByOrientation() {
        if (myPreviews != null) {
            Collections.sort(myPreviews, new Comparator<RenderPreview>() {
                @Override
                public int compare(RenderPreview preview1, RenderPreview preview2) {
                    RenderConfiguration config1 = preview1.getConfiguration();
                    RenderConfiguration config2 = preview2.getConfiguration();
                    State state1 = config1.getDeviceState();
                    State state2 = config2.getDeviceState();
                    if (state1 != state2 && state1 != null && state2 != null) {
                        return state1.getName().compareTo(state2.getName());
                    }

                    return preview1.getDisplayName().compareTo(preview2.getDisplayName());
                }
            });
        }
    }

    /**
     * Notifies the {@linkplain RenderPreviewManager} that the configuration used
     * in the main chooser has been changed. This may require updating parent references
     * in the preview configurations inheriting from it.
     *
     * @param oldConfiguration the previous configuration
     * @param newConfiguration the new configuration in the chooser
     */
    @SuppressWarnings("UnusedDeclaration")
    public void updateMasterConfiguration(@NotNull RenderConfiguration oldConfiguration, @NotNull RenderConfiguration newConfiguration) {
        if (hasPreviews()) {
            assert myPreviews != null;
            for (RenderPreview preview : myPreviews) {
                RenderConfiguration configuration = preview.getConfiguration();
                if (configuration instanceof NestedRenderConfiguration) {
                    NestedRenderConfiguration nestedConfig = (NestedRenderConfiguration) configuration;
                    if (nestedConfig.getParent() == oldConfiguration) {
                        nestedConfig.setParent(newConfiguration);
                    }
                }
            }
        }
    }

    private RenderPreview myStashedPreview;

    private void setStashedPreview(RenderPreview preview) {
        myStashedPreview = preview;
    }

    RenderPreview getStashedPreview() {
        return myStashedPreview;
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
        disposePreviews();
        myAlarm.cancelAllRequests();
        myAlarm.dispose();
        if (myAnimator != null) {
            myAnimator.dispose();
            myAnimator = null;
        }
    }

    // Debugging only
    private static boolean ourClassicLayout = false;
    private boolean myClassicLayout = false;

    public static void toggleLayoutMode(RenderContext context) {
        ourClassicLayout = !ourClassicLayout;
        RenderPreviewManager previewManager = context.getPreviewManager(false);
        if (previewManager != null && previewManager.myClassicLayout != ourClassicLayout) {
            previewManager.setClassicLayout(ourClassicLayout);
        }
    }

    private void setClassicLayout(boolean classicLayout) {
        myClassicLayout = classicLayout;
        if (classicLayout) {
            myFixedRenderSize = null;
            myRenderContext.setMaxSize(0, 0);
            myRenderContext.zoomFit(false, false);
            if (myPreviews != null) {
                for (RenderPreview preview : myPreviews) {
                    preview.setMaxSize(getMaxWidth(), getMaxHeight());
                }
            }
        }

        myRenderContext.updateLayout();
        layout(true);
        redraw();
    }

    /**
     * Animation overlay shown briefly after swapping two previews
     */
    private class SwapAnimation extends Animator {
        private static final int DURATION = 400; // ms
        private Rectangle initialRect1;
        private Rectangle targetRect1;
        private Rectangle initialRect2;
        private Rectangle targetRect2;
        private RenderPreview preview;

        private Rectangle currentRectangle1;
        private Rectangle currentRectangle2;


        SwapAnimation(RenderPreview preview1, RenderPreview preview2) {
            super("Switch Configurations", 16, DURATION, false);
            initialRect1 = new Rectangle(preview1.getX(), preview1.getY(), preview1.getWidth(), preview1.getHeight());
            // TODO: Also look at vertical alignment of the left hand side image!
            Dimension scaledImageSize = myRenderContext.getScaledImageSize();
            initialRect2 = new Rectangle(0, 0, scaledImageSize.width, scaledImageSize.height);
            preview = preview2;
        }

        @Override
        public void paintNow(final int frame, final int totalFrames, final int cycle) {
            if (targetRect1 == null) {
                Dimension scaledImageSize = myRenderContext.getScaledImageSize();
                targetRect1 = new Rectangle(0, 0, scaledImageSize.width, scaledImageSize.height);
            }
            double portion = frame / (double) totalFrames;
            Rectangle rect1 = new Rectangle((int) (portion * (targetRect1.x - initialRect1.x) + initialRect1.x),
                    (int) (portion * (targetRect1.y - initialRect1.y) + initialRect1.y),
                    (int) (portion * (targetRect1.width - initialRect1.width) + initialRect1.width),
                    (int) (portion * (targetRect1.height - initialRect1.height) + initialRect1.height));

            if (targetRect2 == null) {
                targetRect2 = new Rectangle(preview.getX(), preview.getY(), preview.getWidth(), preview.getHeight());
            }
            Rectangle rect2 = new Rectangle((int) (portion * (targetRect2.x - initialRect2.x) + initialRect2.x),
                    (int) (portion * (targetRect2.y - initialRect2.y) + initialRect2.y),
                    (int) (portion * (targetRect2.width - initialRect2.width) + initialRect2.width),
                    (int) (portion * (targetRect2.height - initialRect2.height) + initialRect2.height));

            currentRectangle1 = rect1;
            currentRectangle2 = rect2;

            redraw();
        }

        private void paint(Graphics gc) {
            //noinspection UseJBColor
            gc.setColor(Color.DARK_GRAY);
            Rectangle rect1 = currentRectangle1;
            if (rect1 != null) {
                gc.drawRect(rect1.x, rect1.y, rect1.width, rect1.height);
            }
            Rectangle rect2 = currentRectangle2;
            if (rect2 != null) {
                gc.drawRect(rect2.x, rect2.y, rect2.width, rect2.height);
            }
        }

        @Override
        protected void paintCycleEnd() {
            Disposer.dispose(this);
            redraw();
        }

        @Override
        public void dispose() {
            super.dispose();
            myAnimator = null;
        }
    }

}
