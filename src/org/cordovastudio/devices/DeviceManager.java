/*
 * Copyright (C) 2012 The Android Open Source Project
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

package org.cordovastudio.devices;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.Closeables;
import org.cordovastudio.utils.ILogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.*;
import java.util.*;

import static org.cordovastudio.GlobalConstants.*;

/**
 * Manager class for interacting with {@link Device}s
 */
public class DeviceManager {

    private ILogger myLog;
    private List<Device> myDevices;
    private final Object myLock = new Object();
    private final List<DevicesChangedListener> sListeners = new ArrayList<DevicesChangedListener>();

    public enum DeviceStatus {
        /**
         * The device exists unchanged from the given configuration
         */
        EXISTS,
        /**
         * A device exists with the given name and manufacturer, but has a different configuration
         */
        CHANGED,
        /**
         * There is no device with the given name and manufacturer
         */
        MISSING
    }

    /**
     * Creates a new instance of DeviceManager.
     *
     * @param log SDK logger instance. Should be non-null.
     */
    public static DeviceManager createInstance(@NotNull ILogger log) {
        // TODO consider using a cache and reusing the same instance of the device manager
        // for the same manager/log combo.
        return new DeviceManager(log);
    }

    /**
     * Creates a new instance of DeviceManager.
     *
     * @param log SDK logger instance. Should be non-null.
     */
    private DeviceManager(@NotNull ILogger log) {
        myLog = log;
    }

    /**
     * Interface implemented by objects which want to know when changes occur to the {@link Device}
     * lists.
     */
    public interface DevicesChangedListener {
        /**
         * Called after one of the {@link Device} lists has been updated.
         */
        void onDevicesChanged();
    }

    /**
     * Register a listener to be notified when the device lists are modified.
     *
     * @param listener The listener to add. Ignored if already registered.
     */
    public void registerListener(@NotNull DevicesChangedListener listener) {
        synchronized (sListeners) {
            if (!sListeners.contains(listener)) {
                sListeners.add(listener);
            }
        }
    }

    /**
     * Removes a listener from the notification list such that it will no longer receive
     * notifications when modifications to the {@link Device} list occur.
     *
     * @param listener The listener to remove.
     */
    public boolean unregisterListener(@NotNull DevicesChangedListener listener) {
        synchronized (sListeners) {
            return sListeners.remove(listener);
        }
    }

    @NotNull
    public DeviceStatus getDeviceStatus(@NotNull String name, @NotNull String manufacturer) {
        Device d = getDevice(name, manufacturer);
        if (d == null) {
            return DeviceStatus.MISSING;
        }

        return DeviceStatus.EXISTS;
    }

    @Nullable
    public Device getDevice(@NotNull String name, @NotNull String manufacturer) {
        initDevicesLists();

        return getDeviceImpl(myDevices, name, manufacturer);
    }

    @Nullable
    private Device getDeviceImpl(@NotNull List<Device> devicesList,
                                 @NotNull String name,
                                 @NotNull String manufacturer) {
        for (Device d : devicesList) {
            if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Returns the known {@link Device} list.
     *
     * @return A copy of the list of {@link Device}s. Can be empty but not null.
     */
    @NotNull
    public List<Device> getDevices() {
        initDevicesLists();

        List<Device> devices = new ArrayList<Device>();

        if (myDevices != null) {
            devices.addAll(myDevices);
        }

        return Collections.unmodifiableList(devices);
    }

    private void initDevicesLists() {
        boolean changed = initDevices();

        if (changed) {
            notifyListeners();
        }
    }

    /**
     * Initializes all {@link Device}s
     * @return True if the list has changed.
     */
    private boolean initDevices() {
        synchronized (myLock) {
            if (myDevices != null) {
                return false;
            }
            myDevices = new ArrayList<Device>();
            File devicesFile = null;
            try {
                devicesFile = new File(FN_DEVICES_XML);
                if (devicesFile.exists()) {
                    myDevices.addAll(DeviceParser.parse(devicesFile));
                    return true;
                }
            } catch (SAXException e) {
                // Probably an old config file which we don't want to overwrite.
                if (devicesFile != null) {
                    String base = devicesFile.getAbsoluteFile() + ".old";
                    File renamedConfig = new File(base);
                    int i = 0;
                    while (renamedConfig.exists()) {
                        renamedConfig = new File(base + '.' + (i++));
                    }
                    myLog.error(e, "Error parsing %1$s, backing up to %2$s",
                            devicesFile.getAbsolutePath(),
                            renamedConfig.getAbsolutePath());
                    devicesFile.renameTo(renamedConfig);
                }
            } catch (ParserConfigurationException e) {
                myLog.error(e, "Error parsing %1$s",
                        devicesFile == null ? "(null)" : devicesFile.getAbsolutePath());
            } catch (IOException e) {
                myLog.error(e, "Error parsing %1$s",
                        devicesFile == null ? "(null)" : devicesFile.getAbsolutePath());
            }
        }
        return false;
    }

    private void notifyListeners() {
        synchronized (sListeners) {
            for (DevicesChangedListener listener : sListeners) {
                listener.onDevicesChanged();
            }
        }
    }
}
