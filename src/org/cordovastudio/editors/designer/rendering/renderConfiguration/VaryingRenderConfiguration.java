/*
 * Copyright (C) 2013 The Android Open Source Project
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

import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * An {@linkplain VaryingRenderConfiguration} is a {@link RenderConfiguration} which
 * inherits all of its values from a different configuration, except for one or
 * more attributes where it overrides a custom value, and the overridden value
 * will always <b>differ</b> from the inherited value!
 * <p>
 * For example, a {@linkplain VaryingRenderConfiguration} may state that it
 * overrides the locale, and if the inherited locale is "en", then the returned
 * locale from the {@linkplain VaryingRenderConfiguration} may be for example "nb",
 * but never "en".
 * <p>
 * The configuration will attempt to make its changed inherited value to be as
 * different as possible from the inherited value. Thus, a configuration which
 * overrides the device will probably return a phone-sized screen if the
 * inherited device is a tablet, or vice versa.
 */
public class VaryingRenderConfiguration extends NestedRenderConfiguration {
    /**
     * Variation version; see {@link #setVariation(int)}
     */
    private int myVariation;

    /**
     * Variation version count; see {@link #setVariationCount(int)}
     */
    private int myVariationCount;

    /**
     * Bitmask of attributes to be varied/alternated from the parent
     */
    private int myAlternate;

    /**
     * Constructs a new {@linkplain VaryingRenderConfiguration}.
     * Construct via {@link #create(RenderConfiguration)} or
     * {@link #create(VaryingRenderConfiguration, RenderConfiguration)}
     *
     * @param configuration the configuration to inherit from
     */
    private VaryingRenderConfiguration(@NotNull RenderConfiguration configuration) {
        super(configuration);
    }

    /**
     * Creates a new {@linkplain RenderConfiguration} which inherits values from the
     * given parent {@linkplain RenderConfiguration}, possibly overriding some as
     * well.
     *
     * @param parent the configuration to inherit values from
     * @return a new configuration
     */
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    @NotNull
    public static VaryingRenderConfiguration create(@NotNull RenderConfiguration parent) {
        return new VaryingRenderConfiguration(parent);
    }

    /**
     * Creates a new {@linkplain VaryingRenderConfiguration} that has the same overriding
     * attributes as the given other {@linkplain VaryingRenderConfiguration}.
     *
     * @param other  the configuration to copy overrides from
     * @param parent the parent to tie the configuration to for inheriting values
     * @return a new configuration
     */
    @NotNull
    public static VaryingRenderConfiguration create(@NotNull VaryingRenderConfiguration other, @NotNull RenderConfiguration parent) {
        VaryingRenderConfiguration configuration = new VaryingRenderConfiguration(parent);
        configuration.startBulkEditing();
        initFrom(configuration, other, other);
        configuration.myAlternate = other.myAlternate;
        configuration.myVariation = other.myVariation;
        configuration.myVariationCount = other.myVariationCount;
        configuration.finishBulkEditing();

        return configuration;
    }

    /**
     * Returns the alternate flags for this configuration. Corresponds to
     * the {@code CFG_} flags in {@link RenderConfigurationListener}.
     *
     * @return the bitmask
     */
    public int getAlternateFlags() {
        return myAlternate;
    }

    /**
     * Sets the variation version for this
     * {@linkplain VaryingRenderConfiguration}. There might be multiple
     * {@linkplain VaryingRenderConfiguration} instances inheriting from a
     * {@link RenderConfiguration}. The variation version allows them to choose
     * different complementing values, so they don't all flip to the same other
     * (out of multiple choices) value. The {@link #setVariationCount(int)}
     * value can be used to determine how to partition the buckets of values.
     * Also updates the variation count if necessary.
     *
     * @param variation variation version
     */
    public void setVariation(int variation) {
        myVariation = variation;
        myVariationCount = Math.max(myVariationCount, variation + 1);
    }

    /**
     * Sets the number of {@link VaryingRenderConfiguration} variations mapped
     * to the same parent configuration as this one. See
     * {@link #setVariation(int)} for details.
     *
     * @param count the total number of variation versions
     */
    public void setVariationCount(int count) {
        myVariationCount = count;
    }

    /**
     * Updates the display name in this configuration based on the values and override settings
     */
    public void updateDisplayName() {
        setDisplayName(computeDisplayName());
    }


    // Cached values, key=parent's device, cached value=device
    private Device myPrevParentDevice;
    private Device myPrevDevice;

    @Override
    @Nullable
    public Device getDevice() {
        if (isOverridingDevice()) {
            return super.getDevice();
        }
        Device device = myParent.getDevice();
        if (isAlternatingDevice() && device != null) {
            if (device == myPrevParentDevice) {
                return myPrevDevice;
            }

            myPrevParentDevice = device;

            // Pick a different device
            List<Device> devices = getConfigurationManager().getDevices();

            // Divide up the available devices into {@link #myVariationCount} + 1 buckets
            // (the + 1 is for the bucket now taken up by the inherited value).
            // Then assign buckets to each {@link #myVariation} version, and pick one
            // from the bucket assigned to this current configuration's variation version.

            // I could just divide up the device list count, but that would treat a lot of
            // very similar phones as having the same kind of variety as the 7" and 10"
            // tablets which are sitting right next to each other in the device list.
            // Instead, do this by screen size.

            double smallest = 100;
            double biggest = 1;
            for (Device d : devices) {
                double size = d.getScreenDiagonalLength();
                if (size == 0.00) {
                    continue; // no data
                }
                if (size >= biggest) {
                    biggest = size;
                }
                if (size <= smallest) {
                    smallest = size;
                }
            }

            int bucketCount = myVariationCount + 1;
            double inchesPerBucket = (biggest - smallest) / bucketCount;

            double overriddenSize = device.getScreenDiagonalLength();
            int overriddenBucket = (int) ((overriddenSize - smallest) / inchesPerBucket);
            int bucket = (myVariation < overriddenBucket) ? myVariation : myVariation + 1;
            double from = inchesPerBucket * bucket + smallest;
            double to = from + inchesPerBucket;
            if (biggest - to < 0.1) {
                to = biggest + 0.1;
            }

            for (Device d : devices) {
                double size = d.getScreenDiagonalLength();
                if (size >= from && size < to) {
                    device = d;
                    break;
                }
            }

            myPrevDevice = device;
        }

        return device;
    }

    @Override
    @Nullable
    public State getDeviceState() {
        if (isOverridingDeviceState()) {
            return super.getDeviceState();
        }
        State state = myParent.getDeviceState();
        if (isAlternatingDeviceState() && state != null) {
            State next = getNextDeviceState(state);
            if (next != null) {
                return next;
            } else {
                return state;
            }
        } else {
            if ((isAlternatingDevice() || isOverridingDevice()) && state != null) {
                // If the device differs, I need to look up a suitable equivalent state
                // on our device
                Device device = getDevice();
                if (device != null) {
                    return device.getState(state.getName());
                }
            }

            return state;
        }
    }

    @Override
    @Nullable
    public String computeDisplayName() {
        return computeDisplayName(getOverrideFlags() | myAlternate, this);
    }

    /**
     * Sets whether the device should be alternated by this configuration
     *
     * @param alternate if true, alternate the inherited value
     */
    public void setAlternateDevice(boolean alternate) {
        if (alternate) {
            myAlternate |= CFG_DEVICE;
        } else {
            myAlternate &= ~CFG_DEVICE;
        }
    }

    /**
     * Returns true if the device is alternated
     *
     * @return true if the device is alternated
     */
    public final boolean isAlternatingDevice() {
        return (myAlternate & CFG_DEVICE) != 0;
    }

    /**
     * Sets whether the device state should be alternated by this configuration
     *
     * @param alternate if true, alternate the inherited value
     */
    public void setAlternateDeviceState(boolean alternate) {
        if (alternate) {
            myAlternate |= CFG_DEVICE_STATE;
        } else {
            myAlternate &= ~CFG_DEVICE_STATE;
        }
    }

    /**
     * Returns true if the device state is alternated
     *
     * @return true if the device state is alternated
     */
    public final boolean isAlternatingDeviceState() {
        return (myAlternate & CFG_DEVICE_STATE) != 0;
    }
}