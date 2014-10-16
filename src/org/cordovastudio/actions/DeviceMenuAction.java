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
package org.cordovastudio.actions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.IconLoader;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.FormFactor;
import org.cordovastudio.devices.State;
import org.cordovastudio.editors.designer.rendering.RenderContext;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfigurationManager;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

public class DeviceMenuAction extends FlatComboAction {
    private static final boolean LIST_RECENT_DEVICES = false;
    private final RenderContext myRenderContext;

    public DeviceMenuAction(@NotNull RenderContext renderContext) {
        myRenderContext = renderContext;
        Presentation presentation = getTemplatePresentation();
        presentation.setDescription("The virtual device to render the layout with");
        presentation.setIcon(CordovaIcons.Toolbar.ChooseDevice);
        updatePresentation(presentation);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        updatePresentation(e.getPresentation());
    }

    private void updatePresentation(Presentation presentation) {
        RenderConfiguration configuration = myRenderContext.getConfiguration();
        boolean visible = configuration != null;
        if (visible) {
            String label = getDeviceLabel(configuration.getDevice(), true);
            presentation.setText(label);
        }
        if (visible != presentation.isVisible()) {
            presentation.setVisible(visible);
        }
    }

    /**
     * Returns a suitable label to use to display the given device
     *
     * @param device the device to produce a label for
     * @param brief  if true, generate a brief label (suitable for a toolbar
     *               button), otherwise a fuller name (suitable for a menu item)
     * @return the label
     */
    public static String getDeviceLabel(@Nullable Device device, boolean brief) {
        if (device == null) {
            return "";
        }
        String name = device.getDisplayName();

        if (brief) {
            // Produce a really brief summary of the device name, suitable for
            // use in the narrow space available in the toolbar for example
            int nexus = name.indexOf("Nexus"); //$NON-NLS-1$
            if (nexus != -1) {
                int begin = name.indexOf('(');
                if (begin != -1) {
                    begin++;
                    int end = name.indexOf(')', begin);
                    if (end != -1) {
                        if (name.equals("Nexus 7 (2012)")) {
                            return "Nexus 7";
                        } else {
                            return name.substring(begin, end).trim();
                        }
                    }
                }
            }

            String skipPrefix = "Android ";
            if (name.startsWith(skipPrefix)) {
                name = name.substring(skipPrefix.length());
            }
        }

        return name;
    }

    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DefaultActionGroup group = new DefaultActionGroup(null, true);
        RenderConfiguration configuration = myRenderContext.getConfiguration();
        if (configuration == null) {
            return group;
        }
        Device current = configuration.getDevice();
        RenderConfigurationManager configurationManager = configuration.getConfigurationManager();
        List<Device> deviceList = configurationManager.getDevices();

        if (LIST_RECENT_DEVICES) {
            List<Device> recent = configurationManager.getDevices();
            if (recent.size() > 1) {
                boolean separatorNeeded = false;
                for (Device device : recent) {
                    String label = device.getDisplayName();
                    Icon icon = IconLoader.getIcon(device.getArtwork().getIcon().getPath());
                    group.add(new SetDeviceAction(myRenderContext, label, device, icon, device == current));
                    separatorNeeded = true;
                }
                if (separatorNeeded) {
                    group.addSeparator();
                }
            }
        }

        CordovaFacet facet = CordovaFacet.getInstance(configurationManager.getModule());
        if (facet == null) {
            // Unlikely, but has happened - see http://b.android.com/68091
            return group;
        }

        if (!deviceList.isEmpty()) {
            Map<String, List<Device>> manufacturers = new TreeMap<String, List<Device>>();
            for (Device device : deviceList) {
                List<Device> devices;
                if (manufacturers.containsKey(device.getManufacturer())) {
                    devices = manufacturers.get(device.getManufacturer());
                } else {
                    devices = new ArrayList<Device>();
                    manufacturers.put(device.getManufacturer(), devices);
                }
                devices.add(device);
            }


            Map<FormFactor, List<Device>> deviceMap = Maps.newEnumMap(FormFactor.class);
            for (FormFactor factor : FormFactor.values()) {
                deviceMap.put(factor, Lists.<Device>newArrayList());
            }
            for (List<Device> devices : manufacturers.values()) {
                for (Device device : devices) {

                    deviceMap.get(device.getFormFactor()).add(device);

                }
            }

            DefaultActionGroup genericGroup = new DefaultActionGroup("_Generic Phones and Tablets", true);
            addDeviceSection(genericGroup, current, deviceMap, true, FormFactor.MOBILE);
            group.add(genericGroup);
        }

        return group;
    }

    private void addDeviceSection(@NotNull DefaultActionGroup group,
                                  @Nullable Device current,
                                  @NotNull Map<FormFactor, List<Device>> deviceMap,
                                  boolean reverse,
                                  @NotNull FormFactor factor) {
        List<Device> generic = deviceMap.get(factor);
        if (reverse) {
            Collections.reverse(generic);
        }
        for (final Device device : generic) {
            String label = device.getDisplayName();
            Icon icon = device.getFormFactor().getIcon();
            group.add(new SetDeviceAction(myRenderContext, label, device, icon, current == device));
        }
    }

    private class SetDeviceAction extends ConfigurationAction {
        private final Device myDevice;

        public SetDeviceAction(@NotNull RenderContext renderContext,
                               @NotNull final String title,
                               @NotNull final Device device,
                               @Nullable Icon defaultIcon,
                               final boolean select) {
            super(renderContext, title);
            myDevice = device;
            if (select) {
                getTemplatePresentation().setIcon(AllIcons.Actions.Checked);
            } else if (defaultIcon != null) {
                getTemplatePresentation().setIcon(defaultIcon);
            }
        }

        @Override
        protected void updatePresentation() {
            DeviceMenuAction.this.updatePresentation(DeviceMenuAction.this.getTemplatePresentation());
        }

        @Override
        protected void updateConfiguration(@NotNull RenderConfiguration configuration, boolean commit) {
            // Attempt to jump to the default orientation of the new device; for example, if you're viewing a layout in
            // portrait orientation on a Nexus 4 (its default), and you switch to a Nexus 10, we jump to landscape orientation
            // (its default) unless of course there is a different layout that is the best fit for that device.
            Device prevDevice = configuration.getDevice();
            State prevState = configuration.getDeviceState();
            String newState = prevState != null ? prevState.getName() : null;

            if (newState != null) {
                configuration.setDeviceStateName(newState);
            }

            configuration.setDevice(myDevice);
        }
    }
}
