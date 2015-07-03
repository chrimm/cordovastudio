/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.NestedConfiguration)
 *
 * Copyright (C) 2015 Christoffer T. Timm
 * Changes:
 *  – Stripped unneeded functions
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
import org.cordovastudio.actions.DeviceMenuAction;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@linkplain NestedRenderConfiguration} is a {@link RenderConfiguration} which inherits
 * all of its values from a different configuration, except for one or more
 * attributes where it overrides a custom value.
 * <p>
 * Unlike a {@link org.cordovastudio.editors.designer.rendering.renderConfiguration.VaryingRenderConfiguration}, a {@linkplain NestedRenderConfiguration}
 * will always return the same overridden value, regardless of the inherited
 * value.
 * <p>
 * For example, an {@linkplain NestedRenderConfiguration} may fix the locale to always
 * be "en", but otherwise inherit everything else.
 */
public class NestedRenderConfiguration extends RenderConfiguration implements RenderConfigurationListener {
    /**
     * The configuration we are inheriting non-overridden values from
     */
    protected RenderConfiguration myParent;

    /**
     * Bitmask of attributes to be overridden in this configuration
     */
    private int myOverride;

    /**
     * Constructs a new {@linkplain NestedRenderConfiguration}.
     * Construct via {@link #create(RenderConfiguration)}.
     *
     * @param configuration the configuration to inherit from
     */
    protected NestedRenderConfiguration(@NotNull RenderConfiguration configuration) {
        super(configuration.getConfigurationManager(), configuration.getFile());
        myParent = configuration;

        myParent.addListener(this);
    }

    /**
     * Returns the override flags for this configuration. Corresponds to
     * the {@code CFG_} flags in {@link RenderConfigurationListener}.
     *
     * @return the bitmask
     */
    public int getOverrideFlags() {
        return myOverride;
    }

    /**
     * Creates a new {@linkplain NestedRenderConfiguration} that has the same overriding
     * attributes as the given other {@linkplain NestedRenderConfiguration}, and gets
     * its values from the given {@linkplain RenderConfiguration}.
     *
     * @param other  the configuration to copy overrides from
     * @param values the configuration to copy values from
     * @param parent the parent to tie the configuration to for inheriting values
     * @return a new configuration
     */
    @NotNull
    public static NestedRenderConfiguration create(@NotNull NestedRenderConfiguration other,
                                                   @NotNull RenderConfiguration values,
                                                   @NotNull RenderConfiguration parent) {
        NestedRenderConfiguration configuration = new NestedRenderConfiguration(parent);
        initFrom(configuration, other, values);
        return configuration;
    }

    /**
     * Initializes a new {@linkplain NestedRenderConfiguration} with the overriding
     * attributes as the given other {@linkplain NestedRenderConfiguration}, and gets
     * its values from the given {@linkplain RenderConfiguration}.
     *
     * @param configuration the configuration to initialize
     * @param other         the configuration to copy overrides from
     * @param values        the configuration to copy values from
     */
    protected static void initFrom(NestedRenderConfiguration configuration, NestedRenderConfiguration other, RenderConfiguration values) {
        // TODO: Rewrite to use the clone method!
        configuration.startBulkEditing();
        configuration.myOverride = other.myOverride;
        configuration.setDisplayName(values.getDisplayName());

        if (configuration.isOverridingDevice()) {
            Device device = values.getDevice();
            if (device != null) {
                configuration.setDevice(device);
            }
        }
        if (configuration.isOverridingDeviceState()) {
            State deviceState = values.getDeviceState();
            if (deviceState != null) {
                configuration.setDeviceState(deviceState);
            }
        }
        configuration.finishBulkEditing();
    }

    /**
     * Sets the parent configuration that this configuration is inheriting from.
     *
     * @param parent the parent configuration
     */
    public void setParent(@NotNull RenderConfiguration parent) {
        myParent = parent;
    }

    /**
     * Creates a new {@linkplain RenderConfiguration} which inherits values from the
     * given parent {@linkplain RenderConfiguration}, possibly overriding some as
     * well.
     *
     * @param parent the configuration to inherit values from
     * @return a new configuration
     */
    @NotNull
    public static NestedRenderConfiguration create(@NotNull RenderConfiguration parent) {
        return new NestedRenderConfiguration(parent);
    }

    /**
     * Returns true if the locale is overridden
     *
     * @return true if the locale is overridden
     */
    public final boolean isOverridingLocale() {
        return (myOverride & CFG_LOCALE) != 0;
    }

    /**
     * Returns true if the device is overridden
     *
     * @return true if the device is overridden
     */
    public final boolean isOverridingDevice() {
        return (myOverride & CFG_DEVICE) != 0;
    }

    @Override
    @Nullable
    public Device getDevice() {
        if (isOverridingDevice()) {
            return super.getDevice();
        } else {
            return myParent.getDevice();
        }
    }

    public void setDevice(Device device) {
        if (isOverridingDevice()) {
            super.setDevice(device);
        } else {
            myParent.setDevice(device);
        }
    }

    /**
     * Returns true if the device state is overridden
     *
     * @return true if the device state is overridden
     */
    public final boolean isOverridingDeviceState() {
        return (myOverride & CFG_DEVICE_STATE) != 0;
    }

    @Override
    @Nullable
    public State getDeviceState() {
        if (isOverridingDeviceState()) {
            return super.getDeviceState();
        } else {
            State state = myParent.getDeviceState();
            if (isOverridingDevice()) {
                // If the device differs, I need to look up a suitable equivalent state
                // on our device
                if (state != null) {
                    Device device = super.getDevice();
                    if (device != null) {
                        String name = state.getName();
                        state = device.getState(name);
                        if (state != null) {
                            return state;
                        }
                        // No such state in this screen
                        // Try to find a *similar* one. For example,
                        // the parent may be "Landscape" and this device
                        // may have "Landscape,Closed" and "Landscape,Open"
                        // as is the case with device "3.2in HGVA slider (ADP1)".
                        int nameLen = name.length();
                        for (State s : device.getAllStates()) {
                            String n = s.getName();
                            if (n.regionMatches(0, name, 0, Math.min(nameLen, n.length()))) {
                                return s;
                            }
                        }

                        return device.getDefaultState();
                    }
                }
            }

            return state;
        }
    }

    @Override
    public void setDeviceState(State state) {
        if (isOverridingDeviceState()) {
            super.setDeviceState(state);
        } else {
            if (isOverridingDevice()) {
                Device device = super.getDevice();
                if (device != null) {
                    State equivalentState = device.getState(state.getName());
                    if (equivalentState != null) {
                        state = equivalentState;
                    }
                }
            }
            myParent.setDeviceState(state);
        }
    }

    /**
     * Returns the configuration this {@linkplain NestedRenderConfiguration} is
     * inheriting from
     *
     * @return the configuration this configuration is inheriting from
     */
    @NotNull
    public RenderConfiguration getParent() {
        return myParent;
    }

    /**
     * Returns a computed display name (ignoring the value stored by
     * {@link #setDisplayName(String)}) by looking at the override flags
     * and picking a suitable name.
     *
     * @return a suitable display name
     */
    @Nullable
    public String computeDisplayName() {
        return computeDisplayName(myOverride, this);
    }

    /**
     * Computes a display name for the given configuration, using the given
     * override flags (which correspond to the {@code CFG_} constants in
     * {@link RenderConfigurationListener}
     *
     * @param flags         the override bitmask
     * @param configuration the configuration to fetch values from
     * @return a suitable display name
     */
    @Nullable
    public static String computeDisplayName(int flags, @NotNull RenderConfiguration configuration) {
        if ((flags & CFG_DEVICE) != 0) {
            return DeviceMenuAction.getDeviceLabel(configuration.getDevice(), true);
        }

        if ((flags & CFG_DEVICE_STATE) != 0) {
            State deviceState = configuration.getDeviceState();
            if (deviceState != null) {
                return deviceState.getName();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass()).add("parent", myParent.getDisplayName())
                .add("display", getDisplayName())
                .add("overrideLocale", isOverridingLocale())
                .add("overrideDevice", isOverridingDevice())
                .add("overrideDeviceState", isOverridingDeviceState())
                .add("inherited", super.toString())
                .toString();
    }


    @Override
    public void dispose() {
        myParent.removeListener(this);
    }

    @Override
    public boolean changed(int flags) {
        // Mask out the flags that we are overriding; those changes do not affect us
        flags &= ~myOverride;
        if (flags != 0) {
            updated(flags);
        }
        return true;
    }
}
