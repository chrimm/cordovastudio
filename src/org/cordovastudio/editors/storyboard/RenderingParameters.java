/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.RenderingParameters)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted slightly for Cordova projects (i.e. renamed classes, etc.)
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
package org.cordovastudio.editors.storyboard;

import com.intellij.openapi.project.Project;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static org.cordovastudio.editors.storyboard.Utilities.ZERO_SIZE;
import static org.cordovastudio.editors.storyboard.Utilities.notNull;

public class RenderingParameters {

    @NotNull
    final Project myProject;
    @NotNull
    final RenderConfiguration myConfiguration;
    @NotNull
    final CordovaFacet myFacet;

    public RenderingParameters(@NotNull Project project, @NotNull RenderConfiguration configuration, @NotNull CordovaFacet facet) {
        this.myProject = project;
        this.myConfiguration = configuration;
        this.myFacet = facet;
    }

    @NotNull
    public Project getProject() {
        return myProject;
    }

    @NotNull
    public RenderConfiguration getConfiguration() {
        return myConfiguration;
    }

    @NotNull
    public CordovaFacet getFacet() {
        return myFacet;
    }

    public org.cordovastudio.editors.storyboard.model.Dimension getDeviceScreenSize() {
        return org.cordovastudio.editors.storyboard.model.Dimension.create(getDeviceScreenSize1());
    }

    private Dimension getDeviceScreenSize1() {
        RenderConfiguration configuration = myConfiguration;
        Device device = configuration.getDevice();
        if (device == null) {
            return ZERO_SIZE;
        }
        State deviceState = configuration.getDeviceState();
        if (deviceState == null) {
            deviceState = device.getDefaultState();
        }
        return notNull(device.getScreenSize(deviceState.getOrientation()));
    }

    Dimension getDeviceScreenSizeFor(Transform transform) {
        return transform.modelToView(getDeviceScreenSize());
    }
}
