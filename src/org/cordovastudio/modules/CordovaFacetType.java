/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.cordovastudio.branding.CordovaIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author yole
 */
public class CordovaFacetType extends FacetType<CordovaFacet, CordovaFacetConfiguration> {

  public static final String TYPE_ID = "cordova";

  public CordovaFacetType() {
    super(CordovaFacet.ID, TYPE_ID, "Cordova");
  }


  @Override
  public CordovaFacetConfiguration createDefaultConfiguration() {
    return new CordovaFacetConfiguration();
  }

  @Override
  public CordovaFacet createFacet(@NotNull Module module,
                                  String name,
                                  @NotNull CordovaFacetConfiguration configuration,
                                  @Nullable Facet underlyingFacet) {
    // DO NOT COMMIT MODULE-ROOT MODELS HERE!
    // modules are not initialized yet, so some data may be lost

    return new CordovaFacet(module, name, configuration);
  }

  @Override
  public boolean isSuitableModuleType(ModuleType moduleType) {
    return moduleType instanceof CordovaModuleType;
  }

  @Override
  public Icon getIcon() {
    return CordovaIcons.Modules.CordovaModule;
  }
}
