/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.AndroidRootComponent)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted slightly for Cordova projects (i.e. renamed classes, etc.)
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
package org.cordovastudio.editors.storyboard.model;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import org.cordovastudio.editors.designer.CordovaDesignerEditorProvider;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.model.ViewType;
import org.cordovastudio.editors.designer.rendering.*;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.storyboard.RenderingParameters;
import org.cordovastudio.editors.storyboard.Transform;
import org.cordovastudio.modules.CordovaFacet;
import org.cordovastudio.utils.ImageUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class CordovaRootComponent extends JComponent {
    public static final boolean DEBUG = true;

    private final RenderingParameters myRenderingParameters;
    private final PsiFile myLayoutFile;
    private final boolean myIsMenu;

    @NotNull
    public
    Transform transform = createTransform(1);
    private Image myScaledImage;
    private RenderResult myRenderResult = null;
    private boolean myRenderPending = false;

    public CordovaRootComponent(@NotNull final RenderingParameters renderingParameters, @Nullable final PsiFile psiFile, boolean isMenu) {
        myRenderingParameters = renderingParameters;
        myLayoutFile = psiFile;
        myIsMenu = isMenu;
    }

    public void launchLayoutEditor() {
        if (myLayoutFile != null) {
            Project project = myRenderingParameters.getProject();
            VirtualFile virtualFile = myLayoutFile.getVirtualFile();
            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, virtualFile, 0);
            FileEditorManager manager = FileEditorManager.getInstance(project);
            manager.openEditor(descriptor, true);
            manager.setSelectedEditor(virtualFile, CordovaDesignerEditorProvider.CORDOVA_DESIGNER_ID);
        }
    }

    @Nullable
    private RenderResult getRenderResult() {
        return myRenderResult;
    }

    private void setRenderResult(@Nullable RenderResult renderResult) {
        Container parent = getParent();
        if (parent == null) { // this is coming in of a different thread - we may have been detached form the view hierarchy in the meantime
            return;
        }
        myRenderResult = renderResult;
        if (myIsMenu) {
            revalidate();
        }
        // once we have finished rendering we know where our internal views are and our parent needs to repaint (arrows etc.)
        //repaint();
        parent.repaint();
    }

    public float getScale() {
        return transform.myScale;
    }

    private void invalidate2() {
        myScaledImage = null;
    }

    public void setScale(float scale) {
        transform = createTransform(scale);
        invalidate2();
    }

    private Transform createTransform(float scale) {
        if (myIsMenu) {
            return new Transform(scale) {
                private boolean myCachedRenderedViewValid = false;
                private RenderedView myCachedRenderedView;

                private RenderedView getRenderedView() {
                    if (!myCachedRenderedViewValid) {
                        myCachedRenderedView = getMenu(myRenderResult);
                        myCachedRenderedViewValid = true;
                    }
                    return myCachedRenderedView;
                }

                private int getDx() {
                    RenderedView menu = getRenderedView();
                    return (menu == null) ? 0 : menu.x;
                }

                private int getDy() {
                    RenderedView menu = getRenderedView();
                    return (menu == null) ? 0 : menu.y;
                }

                @Override
                public int modelToViewX(int x) {
                    return super.modelToViewX(x - getDx());
                }

                @Override
                public int modelToViewY(int y) {
                    return super.modelToViewY(y - getDy());
                }

                @Override
                public int viewToModelX(int x) {
                    return super.viewToModelX(x) + getDx();
                }

                @Override
                public int viewToModelY(int y) {
                    return super.viewToModelY(y) + getDy();
                }
            };
        }
        return new Transform(scale);
    }

    @Nullable
    private static RenderedView findMenu(List<RenderedView> children) {
        for (RenderedView child : children) {
            ViewInfo view = child.view;
            if (view != null && view.getViewType() == ViewType.ACTION_BAR_OVERFLOW_MENU) {
                return child.getParent();
            }
            RenderedView menu = findMenu(child.getChildren());
            if (menu != null) {
                return menu;
            }
        }
        return null;
    }

    @Nullable
    private static RenderedView getMenu(RenderResult renderResult) {
        if (renderResult == null) {
            return null;
        }
        RenderedViewHierarchy hierarchy = renderResult.getHierarchy();
        if (hierarchy == null) {
            return null;
        }
        return findMenu(hierarchy.getRoots());
    }

    private static org.cordovastudio.editors.storyboard.model.Dimension size(@Nullable RenderedView view) {
        if (view == null) {
            //return com.android.navigation.Dimension.ZERO;
            return new org.cordovastudio.editors.storyboard.model.Dimension(100, 100); // width/height 0 and 1 is too small to cause an invalidate, for some reason
        }
        return new org.cordovastudio.editors.storyboard.model.Dimension(view.w, view.h);
    }

    @Override
    public Dimension getPreferredSize() {
        return transform.modelToView(myIsMenu ? size(getMenu(myRenderResult)) : myRenderingParameters.getDeviceScreenSize());
    }

    //ScalableImage image = myRenderResult.getImage();
    //if (image != null) {
    //  image.paint(g);
    //}
      /*
      if (false) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
        //g2.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
      }
      if (false) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
      }
      */
    @Nullable
    private Image getScaledImage() {
        if (myScaledImage == null || myScaledImage.getWidth(null) != getWidth() || myScaledImage.getHeight(null) != getHeight()) {
            RenderedImage renderedImage = (myRenderResult == null) ? null : myRenderResult.getImage();
            BufferedImage image = (renderedImage == null) ? null : renderedImage.getOriginalImage();
            myScaledImage = (image == null) ? null : ImageUtils.scale(image, transform.myScale, transform.myScale, 0, 0);
        }
        return myScaledImage;
    }

    private void center(Graphics g, String message, Font font, int height) {
        int messageWidth = getFontMetrics(font).stringWidth(message);
        g.drawString(message, (getWidth() - messageWidth) / 2, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        Image scaledImage = getScaledImage();
        if (scaledImage != null) {
            if (myIsMenu) {
                Point point = transform.modelToView(org.cordovastudio.editors.storyboard.model.Point.ORIGIN);
                g.drawImage(scaledImage, point.x, point.y, null);
            } else {
                g.drawImage(scaledImage, 0, 0, null);
            }
        } else {
            g.setColor(JBColor.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(JBColor.GRAY);
            Font font = g.getFont();
            int vCenter = getHeight() / 2;
            //center(g, "Initialising...", font, vCenter);
            String message = "[" + (myLayoutFile == null ? "no xml resource" : myLayoutFile.getName()) + "]";
            center(g, message, font, vCenter);
            //center(g, message, font, vCenter + font.getSize() * 2);
            render();
        }
    }

    private void render() {
        if (myLayoutFile == null) {
            return;
        }
        Project project = myRenderingParameters.getProject();
        final CordovaFacet facet = myRenderingParameters.getFacet();
        final RenderConfiguration configuration = myRenderingParameters.getConfiguration();

        if (project.isDisposed()) {
            return;
        }
        if (myRenderPending) { // already rendering
            return;
        }
        myRenderPending = true;

        // The rendering service takes long enough to initialise that we don't want to do this from the EDT.
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                Module module = facet.getModule();
                RenderLogger logger = new RenderLogger(myLayoutFile.getName(), module);
                final RenderService service = RenderService.create(facet, module, myLayoutFile, configuration, logger, null);
                if (service != null) {
                    RenderResult renderedResult = service.render();
                    if (renderedResult != null) {
                        RenderSession session = renderedResult.getSession();
                        if (session != null) {
                            Result result = session.getResult();
                            if (result.isSuccess()) {
                                setRenderResult(renderedResult);
                                service.dispose();
                                return;
                            }
                        }
                    }
                    if (DEBUG) System.out.println("AndroidRootComponent: rendering failed ");
                }
            }
        });
    }

    @Nullable
    public RenderedView getRootView() {
        RenderResult renderResult = getRenderResult();
        if (renderResult == null) {
            return null;
        }
        return getRoot(renderResult);
    }

    @Nullable
    private static RenderedView getRoot(@Nullable RenderResult renderResult) {
        if (renderResult == null) {
            return null;
        }
        RenderedViewHierarchy hierarchy = renderResult.getHierarchy();
        if (hierarchy == null) {
            return null;
        }
        List<RenderedView> roots = hierarchy.getRoots();
        if (roots.isEmpty()) {
            return null;
        }
        return roots.get(0);
    }

    @Nullable
    public RenderedView getRenderedView(Point p) {
        RenderResult renderResult = getRenderResult();
        if (renderResult == null) {
            return null;
        }
        RenderedViewHierarchy hierarchy = renderResult.getHierarchy();
        if (hierarchy == null) {
            return null;
        }
        return hierarchy.findLeafAt(transform.viewToModelX(p.x), transform.viewToModelY(p.y));
    }
}
