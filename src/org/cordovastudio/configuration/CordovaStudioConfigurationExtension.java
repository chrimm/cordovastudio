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

package org.cordovastudio.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by cti on 24.08.14.
 */
public class CordovaStudioConfigurationExtension implements Configurable {

    private final String CONFIGURATION_SECTION_TITLE = "Cordova";

    /**
     * Returns the user-visible name of the settings component.
     *
     * @return the visible name of the component.
     */
    @Nls
    @Override
    public String getDisplayName() {
        return CONFIGURATION_SECTION_TITLE;
    }

    /**
     * Returns the topic in the help file which is shown when help for the configurable
     * is requested.
     *
     * @return the help topic, or null if no help is available.
     */
    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * Returns the user interface component for editing the configuration.
     *
     * @return the component instance.
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        return null;
    }

    /**
     * Checks if the settings in the user interface component were modified by the user and
     * need to be saved.
     *
     * @return true if the settings were modified, false otherwise.
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /**
     * Store the settings from configurable to other components.
     */
    @Override
    public void apply() throws ConfigurationException {

    }

    /**
     * Load settings from other components to configurable.
     */
    @Override
    public void reset() {

    }

    /**
     * Disposes the Swing components used for displaying the configuration.
     */
    @Override
    public void disposeUIResources() {

    }
}
