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
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.cordovastudio.branding.CordovaIcons;

import javax.swing.*;

/**
 * Created by cti on 26.08.14.
 */
public class CordovaModuleBuilder extends ModuleBuilder {

    public static final String GROUP_NAME = "Cordova";
    public static final Icon ICON = CordovaIcons.Modules.CordovaModule;

    /**
     *
     * @param modifiableRootModel A modifiableRootModel to setup
     * @throws ConfigurationException
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        doAddContentEntry(modifiableRootModel);
    }

    /**
     * Returns the current instance of CordovaModulBuilder.
     *
     * @return The current instance of CordovaModulBuilder
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public ModuleType getModuleType() {
        return CordovaModuleType.getInstance();
    }

    /**
     * Returns the presentable name.
     *
     * @return The presentable name
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public String getPresentableName() {
        return getGroupName();
    }

    /**
     * Returns the group name.
     *
     * @return The group name
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public String getGroupName() {
        return GROUP_NAME;
    }

    /**
     * Returns the icon for this module node.
     *
     * @return The icon for this module node
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public Icon getNodeIcon() {
        return ICON;
    }
}
