/*
 * Copyright (C) 2014 The Android Open Source Project
 * (Original as of com.intellij.android.designer.model.RadModelBuilder)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova designer model
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
package org.cordovastudio.editors.designer.model;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.designer.ModuleProvider;
import org.cordovastudio.editors.designer.componentTree.TreeComponentDecorator;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.designSurface.RootView;
import org.cordovastudio.editors.designer.rendering.MergeCookie;
import org.cordovastudio.editors.designer.rendering.RenderResult;
import org.cordovastudio.editors.designer.rendering.RenderSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import static org.cordovastudio.GlobalConstants.ATTR_CLASS;
import static org.cordovastudio.GlobalConstants.ATTR_TYPE;

/**
 * Builder responsible for building up (and synchronizing) a hierarchy of {@link org.cordovastudio.editors.designer.model.ViewInfo}
 * objects from layoutlib with a corresponding hierarchy of {@link org.cordovastudio.editors.designer.model.RadViewComponent}
 */
public class RadModelBuilder {
    private static final String DESIGNER_KEY = "DESIGNER";

    // Special tag defined in the meta model file (html5-meta-model.xml) defining the root node, shown as "Device Screen"
    public static final String ROOT_NODE_TAG = "html";

    private final IdManager myIdManager;
    private final MetaManager myMetaManager;
    private final Map<XmlTag, RadViewComponent> myTagToComponentMap = Maps.newIdentityHashMap();
    private final Map<XmlTag, RadViewComponent> myMergeComponentMap = Maps.newHashMap();
    private RootView myNativeComponent;
    private CordovaDesignerEditorPanel myDesigner;

    public RadModelBuilder(@NotNull CordovaDesignerEditorPanel designer) {
        myDesigner = designer;
        myMetaManager = ViewsMetaManager.getInstance(designer.getProject());
        myIdManager = designer.getIdManager();
    }

    @Nullable
    public static RadViewComponent update(@NotNull CordovaDesignerEditorPanel designer,
                                          @NotNull RenderResult result,
                                          @Nullable RadViewComponent prevRoot,
                                          @NotNull RootView nativeComponent) {
        RadModelBuilder builder = new RadModelBuilder(designer);
        return builder.build(prevRoot, result, nativeComponent);
    }

    @NotNull
    public static CordovaDesignerEditorPanel getDesigner(@NotNull RadComponent component) {
        return component.getRoot().getClientProperty(DESIGNER_KEY);
    }

    @Nullable
    public static ModuleProvider getModuleProvider(@NotNull RadComponent component) {
        return getDesigner(component);
    }

    @Nullable
    public static Module getModule(@NotNull RadComponent component) {
        ModuleProvider provider = getModuleProvider(component);
        return provider != null ? provider.getModule() : null;
    }

    @Nullable
    public static Project getProject(@NotNull RadComponent component) {
        ModuleProvider provider = getModuleProvider(component);
        return provider != null ? provider.getProject() : null;
    }

    @Nullable
    public static IdManager getIdManager(@NotNull RadComponent component) {
        return getDesigner(component).getIdManager();
    }

    @Nullable
    public static XmlFile getXmlFile(@NotNull RadComponent component) {
        return getDesigner(component).getXmlFile();
    }

    @Nullable
    public static TreeComponentDecorator getTreeDecorator(@NotNull RadComponent component) {
        return getDesigner(component).getTreeDecorator();
    }

    @Nullable
    public RadViewComponent build(@Nullable RadViewComponent prevRoot,
                                  @NotNull RenderResult result,
                                  @NotNull RootView nativeComponent) {
        myNativeComponent = nativeComponent;
        RadViewComponent root = prevRoot;

        //TODO: add compatibility for SPAs (e.g. for jQuery Mobile with multiple <div data-role="page"> elements)
        /* Find first page Tag */
        //XmlTag[] pages = XmlUtils.findSubNodeByAttribute(myDesigner.getXmlFile().getRootTag().findFirstSubTag("body"), "data-role", "page");

        //if(pages.length == 0) {
        //    return null;
        //}

        //XmlTag rootTag = pages[0];

        //as long as we don't support SPAs, just use thhe <body> tag as root tag
        XmlTag rootTag = myDesigner.getXmlFile().getRootTag().findFirstSubTag("html");

        boolean isMerge = rootTag != null && "merge".equals(rootTag.getName());
        if (root == null || isMerge != (root.getMetaModel() == myMetaManager.getModelByTag("merge"))) {
            try {
                root = createRoot(isMerge, rootTag);
                if (root == null) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        RenderSession session = result.getSession();
        assert session != null;

        updateClientProperties(result, nativeComponent, root);
        initTagMap(root);
        root.getChildren().clear();
        updateHierarchy(root, session);

        // I've removed any tags that are still in the map. I could call removeComponent on these, but I'm worried
        //for (RadViewComponent removed : map.values()) {
        //  myIdManager.removeComponent(removed, false);
        //}

        updateRootBounds(root, session);

        return root;
    }

    protected void updateClientProperties(RenderResult result, RootView nativeComponent, RadViewComponent root) {
        root.setNativeComponent(nativeComponent);
    }

    protected void updateRootBounds(RadViewComponent root, RenderSession session) {
        // Ensure bounds for the root matches actual top level children
        BufferedImage image = session.getImage();
        Rectangle bounds = new Rectangle(0, 0, image != null ? image.getWidth() : 0, image != null ? image.getHeight() : 0);
        for (RadComponent radComponent : root.getChildren()) {
            bounds = bounds.union(radComponent.getBounds());
        }
        root.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    protected void updateHierarchy(RadViewComponent root, RenderSession session) {
        myNativeComponent.clearEmptyRegions();
        List<ViewInfo> rootViews = session.getRootViews();
        if (rootViews != null) {
            for (ViewInfo info : rootViews) {
                updateHierarchy(root, info, 0, 0);
            }
        }
    }

    protected void initTagMap(@NotNull RadViewComponent root) {
        myTagToComponentMap.clear();
        for (RadViewComponent component : RadViewComponent.getViewComponents(root.getChildren())) {
            gatherTags(myTagToComponentMap, component);
        }
    }

    @Nullable
    protected RadViewComponent createRoot(boolean isMerge, @Nullable XmlTag rootTag) throws Exception {
        RadViewComponent root;
        MetaModel rootModel = myMetaManager.getModelByTag(isMerge ? "merge" : ROOT_NODE_TAG);
        assert rootModel != null;
        root = RadComponentOperations.createComponent(rootTag, rootModel);
        root.setClientProperty(DESIGNER_KEY, myDesigner);
        return root;
    }

    private static void gatherTags(Map<XmlTag, RadViewComponent> map, RadViewComponent component) {
        XmlTag tag = component.getTag();
        if (tag != EmptyXmlTag.INSTANCE) {
            map.put(tag, component);
        }

        for (RadComponent child : component.getChildren()) {
            if (child instanceof RadViewComponent) {
                gatherTags(map, (RadViewComponent) child);
            }
        }
    }

    @Nullable
    public RadViewComponent updateHierarchy(@Nullable RadViewComponent parent,
                                            ViewInfo view,
                                            int parentX,
                                            int parentY) {
        Object cookie = view.getCookie();
        RadViewComponent component = null;

        XmlTag tag = null;
        boolean isMerge = false;
        if (cookie instanceof XmlTag) {
            tag = (XmlTag) cookie;
        } else if (cookie instanceof MergeCookie) {
            isMerge = true;
            cookie = ((MergeCookie) cookie).getCookie();
            if (cookie instanceof XmlTag) {
                tag = (XmlTag) cookie;
                if (myMergeComponentMap.containsKey(tag)) {
                    // Just expand the bounds

                    /* With the CssBoxRenderer we already have absolute coordinates, so we don't need to compute them */
                    //int top = parentY + view.getTop();
                    //int left = parentX + view.getLeft();

                    int top = view.getTop();
                    int left = view.getLeft();
                    int width = view.getRight() - view.getLeft();
                    int height = view.getBottom() - view.getTop();
                    RadViewComponent radViewComponent = myMergeComponentMap.get(tag);
                    radViewComponent.getBounds().add(new Rectangle(left, top, width, height));
                    return null;
                }
            }
        }
        if (tag != null) {
            boolean loadProperties;
            component = myTagToComponentMap.get(tag);
            if (component != null) {
                if (!tag.isValid()) {
                    component = null;
                } else {
                    ApplicationManager.getApplication().assertReadAccessAllowed();

                    String name = tag.getName();

                    if (myMetaManager.getModelByTag(name) != component.getMetaModel()) {
                        component = null;
                    }
                }
            }
            if (component == null) {
                // TODO: Construct tag name from ViewInfo's class name so we don't have to touch the PSI data structures at all
                // (so we don't need a read lock)
                String tagName = tag.isValid() ? tag.getName() : "illegal";

                String attrClassStr = tag.getAttributeValue(ATTR_CLASS);
                if(attrClassStr == null) {
                    attrClassStr = "";
                }
                String[] htmlClasses = attrClassStr.split(" ");

                String htmlType = tag.getAttributeValue(ATTR_TYPE);

                try {
                    MetaModel metaModel = null;


                    /* First try to get Model by Class and Type */
                    /* ex: <input type="submit" class="button"> */
                    if(htmlClasses.length > 0 && htmlType != null && !"".equals(htmlType)) {
                        for(String htmlClass : htmlClasses) {
                            metaModel = myMetaManager.getModel(tagName, htmlClass, htmlType);
                            if(metaModel != null)
                                break;
                        }
                    }

                    /* If none found, try only by Type */
                    /* ex: <input type="submit" class="someStylingOnlyClasses"> */
                    if(metaModel == null && htmlType != null && !"".equals(htmlType)) {
                        metaModel = myMetaManager.getModel(tagName, null, htmlType);
                    }

                    /* If still none found, try each Class, but no Type */
                    /* ex: <command class="button"> */
                    if(metaModel == null && htmlClasses.length > 0) {
                        for(String htmlClass : htmlClasses) {
                            metaModel = myMetaManager.getModel(tagName, htmlClass, null);
                            if(metaModel != null)
                                break;
                        }
                    }

                    /* If still none found, try by Tag only */
                    /* ex: <button> */
                    if(metaModel == null) {
                        metaModel = myMetaManager.getModelByTag(tagName);
                    }

                    /* If yet still nothing is found, we simply don't recognise this tag --> error handling */
                    if (metaModel == null) {
                        return null;
                        //TODO: for now, we just don't display/use unknown tags,
                        //but in the future, we should consider displaying "(illegal tag)".
                        //What about legal tag, that we just don't want to see in the ComponentTree, like <script>?

                        //metaModel = myMetaManager.getModelByTag("unknown");
                        //assert metaModel != null;
                    }

                    component = RadComponentOperations.createComponent(tag, metaModel);
                    myIdManager.addComponent(component);
                    loadProperties = true;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }

            } else {
                component.getChildren().clear();
                myTagToComponentMap.remove(tag);
                loadProperties = component.getParent() != parent;
            }

            component.setViewInfo(view);
            component.setNativeComponent(myNativeComponent);

            /* With the CssBoxRenderer we already have absolute coordinates, so we don't need to compute them */
            //int top = parentY + view.getTop();
            //int left = parentX + view.getLeft();

            int top = view.getTop();
            int left = view.getLeft();
            int width = view.getRight() - view.getLeft();
            int height = view.getBottom() - view.getTop();

            if (width < RootView.EMPTY_COMPONENT_SIZE && height < RootView.EMPTY_COMPONENT_SIZE) {
                myNativeComponent.addEmptyRegion(left, top, RootView.VISUAL_EMPTY_COMPONENT_SIZE, RootView.VISUAL_EMPTY_COMPONENT_SIZE);
            }

            component.setBounds(left, top, Math.max(width, RootView.VISUAL_EMPTY_COMPONENT_SIZE), Math.max(height, RootView.VISUAL_EMPTY_COMPONENT_SIZE));

            if (parent != null && parent != component) {
                parent.add(component, null);

            }
        }

        if (component != null) {
            parent = component;
        }

        parentX += view.getLeft();
        parentY += view.getTop();

        for (ViewInfo child : view.getChildren()) {
            updateHierarchy(parent, child, parentX, parentY);
        }

        return component;
    }
}
