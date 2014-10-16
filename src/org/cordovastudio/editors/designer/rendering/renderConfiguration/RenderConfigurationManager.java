/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.ConfigurationManager)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 * //TODO document changes made
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
package org.cordovastudio.editors.designer.rendering.renderConfiguration;

import com.google.common.collect.Maps;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.SoftValueHashMap;
import org.cordovastudio.configuration.ConfigurationStateManager;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.DeviceLogger;
import org.cordovastudio.devices.DeviceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@linkplain RenderConfigurationManager} is responsible for managing {@link RenderConfiguration}
 * objects for a given project.
 * <p/>
 * Whereas a {@link RenderConfiguration} is tied to a specific render target or theme,
 * the {@linkplain RenderConfigurationManager} knows the set of available targets, themes,
 * locales etc. for the current project.
 * <p/>
 * The {@linkplain RenderConfigurationManager} is also responsible for storing and retrieving
 * the saved configuration state for a given file.
 */
public class RenderConfigurationManager implements Disposable {
    @NotNull
    private final Module myModule;
    private List<Device> myDevices;
    private Map<String, Device> myDeviceMap;
    private final SoftValueHashMap<VirtualFile, RenderConfiguration> myCache = new SoftValueHashMap<VirtualFile, RenderConfiguration>();
    private Device myDefaultDevice;
    private Locale myLocale;
    private int myStateVersion;
    private long myLocaleCacheStamp;

    private RenderConfigurationManager(@NotNull Module module) {
        myModule = module;
    }

    /**
     * Gets the {@link RenderConfiguration} associated with the given file
     *
     * @return the {@link RenderConfiguration} for the given file
     */
    @NotNull
    public RenderConfiguration getConfiguration(@NotNull VirtualFile file) {
        RenderConfiguration configuration = myCache.get(file);
        if (configuration == null) {
            configuration = create(file);
            myCache.put(file, configuration);
        }

        return configuration;
    }

    boolean hasCachedConfiguration(@NotNull VirtualFile file) {
        return myCache.get(file) != null;
    }

    /**
     * Creates a new {@link RenderConfiguration} associated with this manager
     *
     * @return a new {@link RenderConfiguration}
     */
    @NotNull
    private RenderConfiguration create(@NotNull VirtualFile file) {
        ConfigurationStateManager stateManager = getStateManager();
        RenderConfigurationFileState fileState = stateManager.getConfigurationState(file);

        return RenderConfiguration.create(this, file);
    }

    /**
     * Similar to {@link #getConfiguration(com.intellij.openapi.vfs.VirtualFile)}, but creates a configuration
     * for a file known to be new, and crucially, bases the configuration on the existing configuration
     * for a known file. This is intended for when you fork a layout, and you expect the forked layout
     * to have a configuration that is (as much as possible) similar to the configuration of the
     * forked file. For example, if you create a landscape version of a layout, it will preserve the
     * screen size, locale, theme and render target of the existing layout.
     *
     * @param file     the file to create a configuration for
     * @param baseFile the other file to base the configuration on
     * @return the new configuration
     */
    @NotNull
    public RenderConfiguration createSimilar(@NotNull VirtualFile file, @NotNull VirtualFile baseFile) {
        ConfigurationStateManager stateManager = getStateManager();
        RenderConfigurationFileState fileState = stateManager.getConfigurationState(baseFile);

        RenderConfiguration configuration = RenderConfiguration.create(this, file);

        RenderConfiguration baseConfig = myCache.get(file);

        if (baseConfig != null) {
            configuration.setEffectiveDevice(baseConfig.getDevice(), baseConfig.getDeviceState());
        }

        myCache.put(file, configuration);

        return configuration;
    }

    /**
     * Returns the associated persistence manager
     */
    public ConfigurationStateManager getStateManager() {
        return ConfigurationStateManager.get(myModule.getProject());
    }

    /**
     * Creates a new {@link RenderConfigurationManager} for the given module
     *
     * @param module the associated module
     * @return a new {@link RenderConfigurationManager}
     */
    @NotNull
    public static RenderConfigurationManager create(@NotNull Module module) {
        return new RenderConfigurationManager(module);
    }

    /**
     * Returns the list of available devices for the current platform, if any
     */
    @NotNull
    public List<Device> getDevices() {
        if (myDevices == null || myDevices.isEmpty()) {
            List<Device> devices = null;

            devices = new ArrayList<Device>();
            DeviceManager deviceManager = DeviceManager.createInstance(new DeviceLogger());
            devices.addAll(deviceManager.getDevices());

            if (devices == null) {
                myDevices = Collections.emptyList();
            } else {
                myDevices = devices;
            }
        }

        return myDevices;
    }

    @NotNull
    private Map<String, Device> getDeviceMap() {
        if (myDeviceMap == null) {
            List<Device> devices = getDevices();
            myDeviceMap = Maps.newHashMapWithExpectedSize(devices.size());
            for (Device device : devices) {
                myDeviceMap.put(device.getName(), device);
            }
        }

        return myDeviceMap;
    }

    @Nullable
    public Device getDeviceByName(@NotNull String name) {
        return getDeviceMap().get(name);
    }

    @NotNull
    public Module getModule() {
        return myModule;
    }

    @NotNull
    public Project getProject() {
        return myModule.getProject();
    }

    @Override
    public void dispose() {
    }


    public int getStateVersion() {
        return myStateVersion;
    }
}
