/*
 * Copyright (C) 2014 Christoffer T. Timm
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

package org.cordovastudio.modules;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.Sdk;
import org.cordovastudio.branding.CordovaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by cti on 26.08.14.
 */
public class CordovaModuleType extends ModuleType {

    private static String MODULE_TYPE_NAME = "Cordova Module";
    private static String MODULE_TYPE_DESC = "Modules for Cordova Projects";

    public CordovaModuleType() {
        super(CordovaModuleTypeId.CORDOVA_MODULE);
    }

    @NotNull
    public static CordovaModuleType getInstance() {
        return (CordovaModuleType) ModuleTypeManager.getInstance().findByID(CordovaModuleTypeId.CORDOVA_MODULE);
        //return (CordovaModuleType) ModuleTypeManager.getInstance().findByID(ModuleTypeId.WEB_MODULE);
    }

    @NotNull
    @Override
    public CordovaModuleBuilder createModuleBuilder() {
        return new CordovaModuleBuilder();
    }

    @NotNull
    public String getName() {
        return MODULE_TYPE_NAME;
    }

    @NotNull
    public String getDescription() {
        return MODULE_TYPE_DESC;
    }

    public Icon getBigIcon() {
        return CordovaIcons.Modules.CordovaModule;
    }

    public Icon getNodeIcon(boolean isOpened) {
        return CordovaIcons.Modules.CordovaModule;
    }

    /**
     * As Cordova projects have no need for Java SDKs, accept all SDKs as valid.
     *
     * @param module        A Module
     * @param projectSdk    An SDK
     * @return Whether the specified SDK is valid for this ModuleType
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public boolean isValidSdk(@NotNull final Module module, final Sdk projectSdk) {
        return true;
    }
}
