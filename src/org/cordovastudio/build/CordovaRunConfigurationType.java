/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * (Original as of org.jetbrains.android.run.AndroidRunConfigurationType)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adopted for Cordova Studio (i.e. adjusted Strings and class names)
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
package org.cordovastudio.build;

import com.intellij.compiler.options.CompileStepBeforeRun;
import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.facet.ProjectFacetManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yole
 */
public class CordovaRunConfigurationType implements ConfigurationType {
    private final ConfigurationFactory myFactory = new CordovaRunConfigurationFactory(this);

    public static class CordovaRunConfigurationFactory extends ConfigurationFactory {
        protected CordovaRunConfigurationFactory(@NotNull ConfigurationType type) {
            super(type);
        }

        @Override
        public RunConfiguration createTemplateConfiguration(Project project) {
            return new CordovaRunConfiguration(project, this);
        }

        @Override
        public boolean canConfigurationBeSingleton() {
            return false;
        }

        @Override
        public boolean isApplicable(@NotNull Project project) {
            return ProjectFacetManager.getInstance(project).hasFacets(CordovaFacet.ID);
        }

        @Override
        public void configureBeforeRunTaskDefaults(Key<? extends BeforeRunTask> providerID, BeforeRunTask task) {
            // Disable the default Make compile step for this run configuration type
            if (CompileStepBeforeRun.ID.equals(providerID)) {
                task.setEnabled(false);
            }
        }
    }

    public static CordovaRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(CordovaRunConfigurationType.class);
    }

    @Override
    public String getDisplayName() {
        return "Cordova App";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Cordova run/debug configuration";
    }

    @Override
    public Icon getIcon() {
        return CordovaIcons.Build.CordovaBuildConfiguration;
    }

    @Override
    @NotNull
    public String getId() {
        return "CordovaRunConfigurationType";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{myFactory};
    }

    public ConfigurationFactory getFactory() {
        return myFactory;
    }
}
