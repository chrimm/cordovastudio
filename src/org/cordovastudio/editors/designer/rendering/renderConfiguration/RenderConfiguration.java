/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.Configuration)
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

import com.google.common.base.Objects;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.cordovastudio.configuration.ConfigurationStateManager;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationListener.*;

/**
 * A {@linkplain RenderConfiguration} is a selection of device, orientation, theme,
 * etc for use when rendering a layout.
 */
public class RenderConfiguration implements Disposable {
    /**
     * The associated file
     */
    @Nullable
    VirtualFile myFile;

    /**
     * The associated {@link RenderConfigurationManager}
     */
    @NotNull
    protected final RenderConfigurationManager myManager;

    /**
     * The theme style to render with
     */
    @Nullable
    private String myTheme;

    /**
     * A specific device to render with
     */
    @Nullable
    private Device mySpecificDevice;

    /**
     * The specific device state
     */
    @Nullable
    private State myState;

    /**
     * The computed effective device; if this configuration does not have a hardcoded specific device,
     * it will be computed based on the current device list; this field caches the value.
     */
    @Nullable
    private Device myDevice;

    /**
     * The device state to use. Used to update {@link #getDeviceState()} such that it returns a state
     * suitable with whatever {@link #getDevice()} returns, since {@link #getDevice()} updates dynamically,
     * and the specific {@link State} instances are tied to actual devices.
     */
    @Nullable
    private String myStateName;

    /**
     * The activity associated with the layout. This is just a cached value of
     * the true value stored on the layout.
     */
    @Nullable
    private String myActivity;

    /**
     * The display name
     */
    private String myDisplayName;

    /**
     * Optional set of listeners to notify via {@link #updated(int)}
     */
    @Nullable
    private List<RenderConfigurationListener> myListeners;

    /**
     * Dirty flags since last notify: corresponds to constants in {@link RenderConfigurationListener}
     */
    protected int myNotifyDirty;

    /**
     * Dirty flags since last folder config sync: corresponds to constants in {@link RenderConfigurationListener}
     */
    protected int myFolderConfigDirty = MASK_FOLDERCONFIG;

    protected int myProjectStateVersion;

    /**
     * Creates a new {@linkplain RenderConfiguration}
     */
    protected RenderConfiguration(@NotNull RenderConfigurationManager manager, @Nullable VirtualFile file) {
        myManager = manager;
        myFile = file;
    }

    /**
     * Creates a new {@linkplain RenderConfiguration}
     *
     * @return a new configuration
     */
    @NotNull
    static RenderConfiguration create(@NotNull RenderConfigurationManager manager,
                                      @Nullable VirtualFile file) {
        return new RenderConfiguration(manager, file);
    }

    /**
     * Creates a configuration suitable for the given file
     *
     * @param base the base configuration to base the file configuration off of
     * @param file the file to look up a configuration for
     * @return a suitable configuration
     */
    @NotNull
    public static RenderConfiguration create(@NotNull RenderConfiguration base,
                                             @NotNull VirtualFile file) {
        // TODO: Figure out whether we need this, or if it should be replaced by
        // a call to RenderConfigurationManager#createSimilar()
        RenderConfiguration configuration = base.clone();

        return configuration;
    }

    /**
     * Creates a new {@linkplain RenderConfiguration} that is a copy from a different configuration
     *
     * @param original the original to copy from
     * @return a new configuration copied from the original
     */
    @NotNull
    public static RenderConfiguration copy(@NotNull RenderConfiguration original) {
        RenderConfiguration copy = new RenderConfiguration(original.myManager, original.myFile);
        copy.myFolderConfigDirty = original.myFolderConfigDirty;
        copy.myProjectStateVersion = original.myProjectStateVersion;
        copy.myTheme = original.getTheme();
        copy.mySpecificDevice = original.mySpecificDevice;
        copy.myDevice = original.myDevice; // avoid getDevice() since it fetches project state
        copy.myStateName = original.myStateName;
        copy.myState = original.myState;
        copy.myDisplayName = original.getDisplayName();

        return copy;
    }

    @Override
    public RenderConfiguration clone() {
        return copy(this);
    }

    public void save() {
        ConfigurationStateManager stateManager = ConfigurationStateManager.get(myManager.getModule().getProject());

        if (myFile != null) {
            RenderConfigurationFileState fileState = new RenderConfigurationFileState();
            fileState.saveState(this);
            stateManager.setConfigurationState(myFile, fileState);
        }
    }

    /**
     * Returns the associated {@link RenderConfigurationManager}
     *
     * @return the manager
     */
    @NotNull
    public RenderConfigurationManager getConfigurationManager() {
        return myManager;
    }

    /**
     * Returns the file associated with this configuration, if any
     *
     * @return the file, or null
     */
    @Nullable
    public VirtualFile getFile() {
        return myFile;
    }

    /**
     * Special marker value which indicates that this activity has been checked and has no activity
     * (whereas a null {@link #myActivity} field means that it has not yet been initialized
     */
    private static final String NO_ACTIVITY = new String();

    /**
     * Returns the chosen device.
     *
     * @return the chosen device
     */
    @Nullable
    public Device getDevice() {
        return myDevice;
    }

    /**
     * Returns the chosen device state
     *
     * @return the device state
     */
    @Nullable
    public State getDeviceState() {
        if (myState == null) {
            Device device = getDevice();
            myState = RenderConfigurationFileState.getState(device, myStateName);
        }

        return myState;
    }

    /**
     * Returns the current theme style
     *
     * @return the theme style
     */
    @Nullable
    public String getTheme() {
        return myTheme;
    }

    /**
     * Returns the display name to show for this configuration
     *
     * @return the display name, or null if none has been assigned
     */
    @Nullable
    public String getDisplayName() {
        return myDisplayName;
    }

    /**
     * Sets the device
     *
     * @param device the device
     */
    public void setDevice(Device device) {
        if (mySpecificDevice != device) {

            myDevice = mySpecificDevice = device;

            int updateFlags = CFG_DEVICE;

            if (device != null) {
                State state = device.getDefaultState();

                if (myState != state) {
                    setDeviceStateName(state.getName());
                    myState = state;
                    updateFlags |= CFG_DEVICE_STATE;
                }
            }

            updated(updateFlags);
        }
    }

    /**
     * Sets the device state
     *
     * @param state the device state
     */
    public void setDeviceState(State state) {
        if (myState != state) {
            if (state != null) {
                setDeviceStateName(state.getName());
            } else {
                myStateName = null;
            }
            myState = state;

            updated(CFG_DEVICE_STATE);
        }
    }

    /**
     * Sets the device state name
     *
     * @param stateName the device state name
     */
    public void setDeviceStateName(@Nullable String stateName) {
        if (!Objects.equal(stateName, myStateName)) {
            myStateName = stateName;
            myState = null;

            updated(CFG_DEVICE_STATE);
        }
    }

    /**
     * Sets the display name to be shown for this configuration.
     *
     * @param displayName the new display name
     */
    public void setDisplayName(@Nullable String displayName) {
        if (!StringUtil.equals(myDisplayName, displayName)) {
            myDisplayName = displayName;
            updated(CFG_NAME);
        }
    }

    /**
     * Sets the theme style
     *
     * @param theme the theme
     */
    public void setTheme(@Nullable String theme) {
        if (!StringUtil.equals(myTheme, theme)) {
            myTheme = theme;
            updated(CFG_THEME);
        }
    }


    /**
     * Returns the screen size required for this configuration
     */
    @Nullable
    public Dimension getScreenSize() {
        if (myDevice != null) {
            return myDevice.getScreenSize();
        }

        return null;
    }

    /**
     * Get the next cyclical state after the given state
     *
     * @param from the state to start with
     * @return the following state following
     */
    @Nullable
    public State getNextDeviceState(@Nullable State from) {
        Device device = getDevice();
        if (device == null) {
            return null;
        }
        List<State> states = device.getAllStates();
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i) == from) {
                return states.get((i + 1) % states.size());
            }
        }

        // Search by name instead
        if (from != null) {
            String name = from.getName();
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).getName().equals(name)) {
                    return states.get((i + 1) % states.size());
                }
            }
        }

        return null;
    }

    /**
     * Called when one or more attributes of the configuration has changed
     */
    public void updated(int flags) {
        myNotifyDirty |= flags;
        myFolderConfigDirty |= flags;

        if (myManager.getStateVersion() != myProjectStateVersion) {
            myNotifyDirty |= MASK_PROJECT_STATE;
            myFolderConfigDirty |= MASK_PROJECT_STATE;
            myDevice = null;
            myState = null;
        }
    }

    /**
     * Adds a listener to be notified when the configuration changes
     *
     * @param listener the listener to add
     */
    public void addListener(@NotNull RenderConfigurationListener listener) {
        if (myListeners == null) {
            myListeners = new ArrayList<RenderConfigurationListener>();
        }
        myListeners.add(listener);
    }

    /**
     * Removes a listener such that it is no longer notified of changes
     *
     * @param listener the listener to remove
     */
    public void removeListener(@NotNull RenderConfigurationListener listener) {
        if (myListeners != null) {
            myListeners.remove(listener);
            if (myListeners.isEmpty()) {
                myListeners = null;
            }
        }
    }

    // For debugging only
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("display", getDisplayName())
                .add("theme", getTheme())
                .add("device", getDevice())
                .add("state", getDeviceState())
                .toString();
    }

    public Module getModule() {
        return myManager.getModule();
    }

    @Override
    public void dispose() {
    }

    public void setEffectiveDevice(@Nullable Device device, @Nullable State state) {
        int updateFlags = 0;
        if (myDevice != device) {
            updateFlags = CFG_DEVICE;
            myDevice = device;
        }

        if (myState != state) {
            myState = state;
            myStateName = state != null ? state.getName() : null;
            updateFlags |= CFG_DEVICE_STATE;
        }

        if (updateFlags != 0) {
            updated(updateFlags);
        }
    }
}
