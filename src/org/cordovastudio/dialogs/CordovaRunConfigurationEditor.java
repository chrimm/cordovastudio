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

package org.cordovastudio.dialogs;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

//TODO: Maybe use class and form from Android...!

/**
 * Created by cti on 23.09.14.
 */
public class CordovaRunConfigurationEditor<CordovaRunConfiguration> extends SettingsEditor<CordovaRunConfiguration> {

    private final Project myProject;

    public CordovaRunConfigurationEditor(Project project){
        myProject = project;
    }

    @Override
    protected void resetEditorFrom(CordovaRunConfiguration s) {
        //TODO implement!
    }

    @Override
    protected void applyEditorTo(CordovaRunConfiguration s) throws ConfigurationException {
        //TODO implement!
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return null;
    }
}
