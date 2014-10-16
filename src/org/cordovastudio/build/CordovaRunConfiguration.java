/*
 * Copyright 2000-2010 JetBrains s.r.o.
 * (Orinigal as of org.jetbrains.android.run.AndroidRunConfiguration)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged org.jetbrains.android.run.AndroidRunConfiguration and org.jetbrains.android.run.AndroidRunConfigurationBase
 *  – Adopted for running of Cordova projects
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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.cordovastudio.dialogs.CordovaRunConfigurationEditor;
import org.cordovastudio.modules.CordovaFacet;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene.Kudelevsky
 */
public class CordovaRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements RefactoringListenerProvider {
    private static final Logger LOG = Logger.getInstance(CordovaRunConfiguration.class);

    public String TARGET_SELECTION_MODE = TargetSelectionMode.EMULATOR.name();
    public String PREFERRED_AVD = "";

    @NonNls
    public static final String LAUNCH_DEFAULT_ACTIVITY = "default_activity";
    @NonNls
    public static final String LAUNCH_SPECIFIC_ACTIVITY = "specific_activity";
    @NonNls
    public static final String DO_NOTHING = "do_nothing";

    public String ACTIVITY_CLASS = "";
    public String MODE = LAUNCH_DEFAULT_ACTIVITY;

    public CordovaRunConfiguration(Project project, ConfigurationFactory factory) {
        super(new RunConfigurationModule(project), factory);
    }

    @Override
    public final void checkConfiguration() throws RuntimeConfigurationException {
        RunConfigurationModule configurationModule = getConfigurationModule();
        configurationModule.checkForWarning();
        Module module = configurationModule.getModule();

        if (module == null) {
            return;
        }

        Project project = module.getProject();

        CordovaFacet facet = CordovaFacet.getInstance(module);
        if (facet == null) {
            throw new RuntimeConfigurationError("No Cordova facet found in the module");
        }
        if (facet.getManifest() == null) {
            throw new RuntimeConfigurationError("config.xml doesn't exist or has incorrect root tag");
        }
    }

    @Override
    public Collection<Module> getValidModules() {
        final List<Module> result = new ArrayList<Module>();
        Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        for (Module module : modules) {
            if (CordovaFacet.getInstance(module) != null) {
                result.add(module);
            }
        }
        return result;
    }


    public void setTargetSelectionMode(@NotNull TargetSelectionMode mode) {
        TARGET_SELECTION_MODE = mode.name();
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        readModule(element);
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        writeModule(element);
        DefaultJDOMExternalizer.writeExternal(this, element);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        Project project = getProject();

        CordovaRunConfigurationEditor<CordovaRunConfiguration> editor = new CordovaRunConfigurationEditor<CordovaRunConfiguration>(project);

        return editor;
    }

    @Override
    @Nullable
    public RefactoringElementListener getRefactoringElementListener(PsiElement element) {
        //TODO implement? Do we need a refactoring listener? Do we use refactoring in general?
        return null;
    }

    /**
     * Prepares for executing a specific instance of the run configuration.
     *
     * @param executor    the execution mode selected by the user (run, debug, profile etc.)
     * @param environment the environment object containing additional settings for executing the configuration.
     * @return the RunProfileState describing the process which is about to be started, or null if it's impossible to start the process.
     */
    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        //TODO Implement and create class CordovaRunningState from org.jetbrains.android.run.AndroidRunningState
        return null;
    }
}
