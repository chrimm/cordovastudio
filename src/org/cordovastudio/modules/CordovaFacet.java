/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * (original as of org.jetbrains.android.facet.AndroidFacet)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  - Adapted for Cordova projects (almost complete overhaul)
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
package org.cordovastudio.modules;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationManager;
import org.cordovastudio.utils.CordovaPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author yole
 */
public final class CordovaFacet extends Facet<CordovaFacetConfiguration> {
    private static final Logger LOG = Logger.getInstance(CordovaFacet.class);

    public static final FacetTypeId<CordovaFacet> ID = new FacetTypeId<CordovaFacet>("cordova");
    public static final String NAME = "Cordova";

    private RenderConfigurationManager myConfigurationManager;

    private final CordovaModuleInfo myCordovaModuleInfo = CordovaModuleInfo.create(this);

    public CordovaFacet(@NotNull Module module, String name, @NotNull CordovaFacetConfiguration configuration) {
        //noinspection ConstantConditions
        super(getFacetType(), module, name, configuration, null);
        configuration.setFacet(this);
    }

    @Override
    public void initFacet() {
        //TODO: implement? Where is the Facet's panel initialized/created?
    }

    @Override
    public void disposeFacet() {
        if (myConfigurationManager != null) {
            myConfigurationManager.dispose();
        }
    }

    @Nullable
    public static CordovaFacet getInstance(@NotNull Module module) {
        return FacetManager.getInstance(module).getFacetByType(ID);
    }

    @Nullable
    public static CordovaFacet getInstance(@NotNull ConvertContext context) {
        Module module = context.getModule();
        return module != null ? getInstance(module) : null;
    }

    @Nullable
    public static CordovaFacet getInstance(@NotNull final PsiElement element) {
        Module module = CordovaPsiUtils.getModuleSafely(element);
        if (module == null) return null;
        return getInstance(module);
    }

    @Nullable
    public static CordovaFacet getInstance(@NotNull DomElement element) {
        Module module = element.getModule();
        if (module == null) return null;
        return getInstance(module);
    }

    public static CordovaFacetType getFacetType() {
        return (CordovaFacetType) FacetTypeRegistry.getInstance().findFacetType(ID);
    }

    @NotNull
    public RenderConfigurationManager getConfigurationManager() {
        //noinspection ConstantConditions
        return getConfigurationManager(true);
    }

    @Nullable
    public RenderConfigurationManager getConfigurationManager(boolean createIfNecessary) {
        if (myConfigurationManager == null && createIfNecessary) {
            myConfigurationManager = RenderConfigurationManager.create(getModule());
            Disposer.register(this, myConfigurationManager);
        }

        return myConfigurationManager;
    }

    @NotNull
    public CordovaModuleInfo getModuleInfo() {
        return myCordovaModuleInfo;
    }

    public IManifest getManifest() {
        //TODO implement!
        return null;
    }

}
