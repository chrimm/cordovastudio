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
package org.cordovastudio.editors.designer.rendering;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.rendering.engines.CssBoxRenderer;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.modules.CordovaFacet;
import org.cordovastudio.test.DummyDeviceFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.intellij.lang.annotation.HighlightSeverity.ERROR;

/**
 * The {@link RenderService} provides rendering and layout information for
 * Android layouts. This is a wrapper around the layout library.
 */
public class RenderService implements IImageFactory {
    @NotNull
    private final Module myModule;

    @NotNull
    private final Device myDevice;

    @NotNull
    private final State myState;

    @NotNull
    private final XmlFile myPsiFile;

    @NotNull
    private final RenderLogger myLogger;

    @NotNull
    private final RenderingEngine myRenderingEngine;

    private boolean myShowDecorations = true;

    @NotNull
    private final RenderConfiguration myConfiguration;

    private long myTimeout;

    @Nullable
    private Set<XmlTag> myExpandNodes;

    @Nullable
    private RenderContext myRenderContext;

    private final Object myCredential = new Object();

    /**
     * Creates a new {@link RenderService} associated with the given editor.
     *
     * @return a {@link RenderService} which can perform rendering services
     */
    @Nullable
    public static RenderService create(@NotNull final CordovaFacet facet,
                                       @NotNull final Module module,
                                       @NotNull final PsiFile psiFile,
                                       @NotNull final RenderConfiguration configuration,
                                       @NotNull final RenderLogger logger,
                                       @Nullable final RenderContext renderContext) {

        final Project project = module.getProject();

        //RenderingEngine engine;

        //TODO: access project config and get selected rendering engine
        //engine = new DummyRenderer();
        CssBoxRenderer engine = new CssBoxRenderer();
        engine.init(logger);

        Device device = configuration.getDevice();
        if (device == null) {
            //TODO: figure out, why here's no Device found and fix it instead of creating a dummy device
            device = DummyDeviceFactory.createDummyDevice();
            configuration.setDevice(device);
            //logger.addMessage(RenderProblem.createPlain(ERROR, "No device selected"));
            //return null;
        }

        State state = configuration.getDeviceState();
        if (state == null) {
            state = device.getDefaultState();
            configuration.setDeviceState(state);
        }

        RenderService service = new RenderService(facet, module, psiFile, configuration, logger, engine, device, state);
        if (renderContext != null) {
            service.setRenderContext(renderContext);
        }

        return service;
    }

    /**
     * Use the {@link #create} factory instead
     */
    private RenderService(@NotNull CordovaFacet facet,
                          @NotNull Module module,
                          @NotNull PsiFile psiFile,
                          @NotNull RenderConfiguration configuration,
                          @NotNull RenderLogger logger,
                          @NotNull RenderingEngine engine,
                          @NotNull Device device,
                          @NotNull State state) {
        myModule = module;
        myLogger = logger;
        myLogger.setCredential(myCredential);

        if (!(psiFile instanceof XmlFile)) {
            throw new IllegalArgumentException("Can only render XML files: " + psiFile.getClass().getName());
        }

        myPsiFile = (XmlFile) psiFile;
        myConfiguration = configuration;
        myRenderingEngine = engine;
        myDevice = device;
        myState = state;
    }

    @NotNull
    public RenderConfiguration getConfiguration() {
        return myConfiguration;
    }

    @NotNull
    public Module getModule() {
        return myModule;
    }

    @NotNull
    public RenderLogger getLogger() {
        return myLogger;
    }

    @Nullable
    public Set<XmlTag> getExpandNodes() {
        return myExpandNodes;
    }

    public boolean getShowDecorations() {
        return myShowDecorations;
    }

    public void dispose() {
    }

    public RenderService setTimeout(long timeout) {
        myTimeout = timeout;
        return this;
    }

    /**
     * Sets whether the rendering should include decorations such as a system bar, an
     * application bar etc depending on the SDK target and theme. The default is true.
     *
     * @param showDecorations true if the rendering should include system bars etc.
     * @return this (such that chains of setters can be stringed together)
     */
    public RenderService setDecorations(boolean showDecorations) {
        myShowDecorations = showDecorations;
        return this;
    }

    /**
     * Gets the context for the usage of this {@link RenderService}.
     */
    @Nullable
    public RenderContext getRenderContext() {
        return myRenderContext;
    }

    /**
     * Sets the context for the usage of this {@link RenderService}.
     *
     * @param renderContext the render context
     * @return this, for constructor chaining
     */
    @Nullable
    public RenderService setRenderContext(@Nullable RenderContext renderContext) {
        myRenderContext = renderContext;
        return this;
    }

    /**
     * Sets the nodes to expand during rendering. These will be padded with approximately
     * 20 pixels. The default is null.
     *
     * @param nodesToExpand the nodes to be expanded
     * @return this (such that chains of setters can be stringed together)
     */
    @NotNull
    public RenderService setNodesToExpand(@Nullable Set<XmlTag> nodesToExpand) {
        myExpandNodes = nodesToExpand;
        return this;
    }

    /**
     * Renders the model and returns the result as a {@link org.cordovastudio.editors.designer.rendering.RenderSession}.
     *
     * @return the {@link RenderResult resulting from rendering the current model
     */
    @Nullable
    private RenderResult createRenderSession() {

        final RenderParams params = new RenderParams(LayoutPsiPullParser.create(myPsiFile, myLogger), myModule, myLogger);

        try {
            RenderResult result = ApplicationManager.getApplication().runReadAction(new Computable<RenderResult>() {
                @Nullable
                @Override
                public RenderResult compute() {

                    try {
                        int retries = 0;
                        RenderSession session = null;
                        while (retries < 10) {
                            session = myRenderingEngine.createSession(params, myDevice);
                            Result result = session.getResult();
                            if (result.getStatus() != Result.Status.ERROR_TIMEOUT) {
                                // Sometimes happens at startup; treat it as a timeout; typically a retry fixes it
                                if (!result.isSuccess()) {
                                    retries++;
                                    continue;
                                }
                                break;
                            }
                            retries++;
                        }

                        return new RenderResult(RenderService.this, session, myPsiFile, myLogger);
                    } catch (Exception e) {
                        myLogger.error(null, e.getLocalizedMessage(), e, null);
                        e.printStackTrace(System.err);
                        return null;
                    }
                }
            });
            addDiagnostics(result.getSession());
            return result;
        } catch (RuntimeException t) {
            myLogger.error(null, t.getLocalizedMessage(), t, null);
            throw t;
        }
    }

    /**
     * Returns true if the given file can be rendered
     */
    public static boolean canRender(@Nullable PsiFile file) {
        //TODO: determine whether rendering engine can render this file
        return true;
    }

    private static final Object RENDERING_LOCK = new Object();

    @Nullable
    public RenderResult render() {
        // During development only:
        //assert !ApplicationManager.getApplication().isReadAccessAllowed() : "Do not hold read lock during render!";

        synchronized (RENDERING_LOCK) {
            RenderResult renderResult;
            try {
                renderResult = createRenderSession();
            } catch (final Exception e) {
                String message = e.getMessage();
                if (message == null) {
                    message = e.toString();
                }
                myLogger.addMessage(RenderProblem.createPlain(ERROR, message, myModule.getProject(), myLogger.getLinkManager(), e));
                renderResult = new RenderResult(this, null, myPsiFile, myLogger);
            }

            return renderResult;
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void addDiagnostics(@Nullable RenderSession session) {
        if (session == null) {
            return;
        }
        Result r = session.getResult();
        if (!myLogger.hasProblems() && !r.isSuccess()) {
            if (r.getException() != null || r.getErrorMessage() != null) {
                myLogger.error(null, r.getErrorMessage(), r.getException(), null);
            } else if (r.getStatus() == Result.Status.ERROR_TIMEOUT) {
                myLogger.error(null, "Rendering timed out.", null);
            } else {
                myLogger.error(null, "Unknown render problem: " + r.getStatus(), null);
            }
        }
    }

    @NotNull
    public RenderingEngine getRenderingEngine() {
        return myRenderingEngine;
    }

    @NotNull
    public XmlFile getPsiFile() {
        return myPsiFile;
    }

    @Nullable
    public static RenderingEngine getRenderingEngine(@Nullable final Module module) {
        if (module == null) {
            return null;
        }

        Project project = module.getProject();

        //TODO: determine the right Rendering Engine from settings and return the real one
        return new CssBoxRenderer();
    }

    /**
     * Measure the children of the given parent tag, applying the given filter to the
     * pull parser's attribute values.
     *
     * @param parent the parent tag to measure children for
     * @param filter the filter to apply to the attribute values
     * @return a map from the children of the parent to new bounds of the children
     */
    @Nullable
    public Map<XmlTag, ViewInfo> measureChildren(XmlTag parent, final AttributeFilter filter) {
        ILayoutPullParser modelParser = LayoutPsiPullParser.create(filter, parent, myLogger);
        Map<XmlTag, ViewInfo> map = Maps.newHashMap();
        RenderSession session = measure(modelParser);
        if (session != null) {
            Result result = session.getResult();
            if (result != null && result.isSuccess()) {
                assert session.getRootViews().size() == 1;
                ViewInfo root = session.getRootViews().get(0);
                List<ViewInfo> children = root.getChildren();
                for (ViewInfo info : children) {
                    Object cookie = info.getCookie();
                    if (cookie instanceof XmlTag) {
                        map.put((XmlTag)cookie, info);
                    }
                }
            }

            return map;
        }

        return null;
    }

    /**
     * Measure the given child in context, applying the given filter to the
     * pull parser's attribute values.
     *
     * @param tag the child to measure
     * @param filter the filter to apply to the attribute values
     * @return a view info, if found
     */
    @Nullable
    public ViewInfo measureChild(XmlTag tag, final AttributeFilter filter) {
        XmlTag parent = tag.getParentTag();
        if (parent != null) {
            Map<XmlTag, ViewInfo> map = measureChildren(parent, filter);
            if (map != null) {
                for (Map.Entry<XmlTag, ViewInfo> entry : map.entrySet()) {
                    if (entry.getKey() == tag) {
                        return entry.getValue();
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    private RenderSession measure(ILayoutPullParser parser) {
        final RenderParams params = new RenderParams(
                parser,
                myModule /* projectKey */,
                myLogger);

        params.setForceNoDecor();

        try {
            return ApplicationManager.getApplication().runReadAction(new Computable<RenderSession>() {
                @Nullable
                @Override
                public RenderSession compute() {
                    int retries = 0;
                    while (retries < 10) {
                        try {
                            RenderSession session = myRenderingEngine.createSession(params, myDevice);
                            Result result = session.getResult();
                            if (result.getStatus() != Result.Status.ERROR_TIMEOUT) {
                                // Sometimes happens at startup; treat it as a timeout; typically a retry fixes it
                                if (!result.isSuccess() && "The main Looper has already been prepared.".equals(result.getErrorMessage())) {
                                    retries++;
                                    continue;
                                }
                                return session;
                            }
                        } catch(RenderingException e) {
                            //TODO: log?
                        } finally {
                            retries++;
                        }

                    }

                    return null;
                }
            });
        }
        catch (RuntimeException t) {
            // Exceptions from the bridge
            myLogger.error(null, t.getLocalizedMessage(), t, null);
            throw t;
        }
    }

    // ---- Implements IImageFactory ----

    /**
     * TODO: reuse image across subsequent render operations if the size is the same
     */
    @SuppressWarnings("UndesirableClassUsage") // Don't need Retina for rendering; will scale down anyway
    @Override
    public BufferedImage getImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public boolean requiresTransparency() {
        //TODO check if this function is need and implement
        return false;
    }

    /**
     * The {@link AttributeFilter} allows a client of {@link #measureChildren} to modify the actual
     * XML values of the nodes being rendered, for example to force width and height values to
     * wrap_content when measuring preferred size.
     */
    public interface AttributeFilter {
        /**
         * Returns the attribute value for the given node and attribute name. This filter
         * allows a client to adjust the attribute values that a node presents to the
         * layout library.
         * <p/>
         * Returns "" to unset an attribute. Returns null to return the unfiltered value.
         *
         * @param node      the node for which the attribute value should be returned
         * @param namespace the attribute namespace
         * @param localName the attribute local name
         * @return an override value, or null to return the unfiltered value
         */
        @Nullable
        String getAttribute(@NotNull XmlTag node, @Nullable String namespace, @NotNull String localName);
    }
}
