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
package org.cordovastudio.modules;

import com.intellij.openapi.module.Module;
import org.cordovastudio.CordovaVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Information about a module.
 */
public class CordovaModuleInfo {
    private final
    @NotNull
    CordovaFacet myFacet;

    private CordovaModuleInfo(@NotNull CordovaFacet facet) {
        myFacet = facet;
    }

    @NotNull
    public static CordovaModuleInfo create(@NotNull CordovaFacet facet) {
        return new CordovaModuleInfo(facet);
    }

    @NotNull
    public static CordovaModuleInfo get(@NotNull CordovaFacet facet) {
        return facet.getModuleInfo();
    }

    @Nullable
    public static CordovaModuleInfo get(@NotNull Module module) {
        CordovaFacet facet = CordovaFacet.getInstance(module);
        return facet != null ? facet.getModuleInfo() : null;
    }

    /**
     * Obtains the package name for the current variant, or if not specified, from the primary manifest.
     */
    @Nullable
    public String getPackage() {
        return CordovaManifestInfo.get(myFacet.getModule()).getPackage();
    }
}
