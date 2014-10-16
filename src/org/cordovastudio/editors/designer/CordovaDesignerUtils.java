/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Orignial as of com.intellij.android.designer.AndroidDesignerUtils)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Removed not needed functions
 *  – Adopted to Cordova projects
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
package org.cordovastudio.editors.designer;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.RootView;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.editors.designer.model.RadVisualComponent;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.RenderLogger;
import org.cordovastudio.editors.designer.rendering.RenderService;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;


public class CordovaDesignerUtils {
    private CordovaDesignerUtils() {
    }

    @Nullable
    public static CordovaFacet getFacet(@NotNull IEditableArea area) {
        CordovaDesignerEditorPanel panel = getPanel(area);
        if (panel != null) {
            RenderConfiguration configuration = panel.getConfiguration();
            assert configuration != null;
            Module module = panel.getModule();
            return CordovaFacet.getInstance(module);
        }

        return null;
    }

    @Nullable
    public static RenderService getRenderService(@NotNull IEditableArea area) {
        CordovaDesignerEditorPanel panel = getPanel(area);

        if (panel != null) {
            RenderConfiguration configuration = panel.getConfiguration();
            assert configuration != null;

            XmlFile xmlFile = panel.getXmlFile();

            Module module = panel.getModule();

            CordovaFacet facet = CordovaFacet.getInstance(module);
            assert facet != null;

            RenderLogger logger = new RenderLogger(xmlFile.getName(), module);

            @SuppressWarnings("UnnecessaryLocalVariable")
            RenderContext renderContext = panel;

            RenderService service = RenderService.create(facet, module, xmlFile, configuration, logger, renderContext);
            assert service != null;

            return service;
        }

        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Pixel (px) to Device Independent Pixel (dp) conversion
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    public static CordovaDesignerEditorPanel getPanel(@NotNull IEditableArea area) {
        RadComponent root = area.getRootComponent();
        if (root instanceof RadVisualComponent) {
            Component nativeComponent = ((RadVisualComponent) root).getNativeComponent();
            if (nativeComponent instanceof RootView) {
                return ((RootView) nativeComponent).getPanel();
            }
        }

        return null;
    }

    /**
     * Returns the preferred size of the given component in the given editable area
     *
     * @param area      the associated {@link IEditableArea}
     * @param component the component to measure
     * @return the bounds, if they can be determined
     */
    public static Dimension computePreferredSize(@NotNull IEditableArea area, @NotNull RadViewComponent component,
                                                 @Nullable RadComponent targetParent) {
        FeedbackLayer layer = area.getFeedbackLayer();
        Dimension size = component.getBounds(layer).getSize();
        if (size.width == 0 && size.height == 0) {

            // TODO: If it's a layout, do something smarter. I really only have to worry about this
            // if the creation XML calls for wrap_content!
            RenderService service = getRenderService(area);
            if (service != null) {
                List<ViewInfo> roots = measureComponent(area, component, targetParent);
                if (roots != null && !roots.isEmpty()) {
                    ViewInfo root = roots.get(0);
                    size.width = root.getRight() - root.getLeft();
                    size.height = root.getBottom() - root.getTop();
                    if (size.width != 0 && size.height != 0) {
                        // Apply canvas scale!
                        if (targetParent != null) {
                            return targetParent.fromModel(layer, size);
                        } else {
                            CordovaDesignerEditorPanel panel = getPanel(area);
                            if (panel != null) {
                                RadComponent rootComponent = panel.getRootComponent();
                                if (rootComponent != null) {
                                    return rootComponent.fromModel(layer, size);
                                }
                            }
                            return size;
                        }
                    }
                }
            }

            // Default size when we can't compute it
            size.width = 100;
            size.height = 30;
        }

        return size;
    }

    /**
     * Measures the given component in the given editable area
     *
     * @param area         the associated {@link IEditableArea}
     * @param component    the component to measure
     * @param targetParent if supplied, the target parent intended for the component
     *                     (used to find namespace declarations and size for fill_parent sizes)
     * @return the measured view info objects at the root level
     */
    @Nullable
    public static List<ViewInfo> measureComponent(@NotNull IEditableArea area, @NotNull RadViewComponent component,
                                                  @Nullable RadComponent targetParent) {
        //TODO: implement!

        return null;
    }
}
