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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.cordovastudio.devices.Device;
import org.cordovastudio.devices.State;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Per file state for layouts */
@SuppressWarnings("UnusedDeclaration") // Getters called by XML serialization reflection
@Tag("config")
public class RenderConfigurationFileState {
  @Nullable
  private String myDeviceState;
  @Nullable
  private String myTheme;

  @Tag("state")
  @Nullable
  public String getDeviceState() {
    return myDeviceState;
  }

  public void setDeviceState(@Nullable String deviceState) {
    myDeviceState = deviceState;
  }

  @Tag("theme")
  @Nullable
  public String getTheme() {
    return myTheme;
  }

  public void setTheme(@Nullable String theme) {
    myTheme = theme;
  }

  public void saveState(@NotNull RenderConfiguration configuration) {
    Device device = configuration.getDevice();
    myDeviceState = null;
    if (device != null) {
      State deviceState = configuration.getDeviceState();
      if (deviceState != null && deviceState != device.getDefaultState()) {
        myDeviceState = deviceState.getName();
      }
    }

    myTheme = StringUtil.nullize(configuration.getTheme());
  }

  public void loadState(@NotNull RenderConfiguration configuration) {
    RenderConfigurationManager manager = configuration.getConfigurationManager();

    configuration.setDeviceStateName(myDeviceState);

    if (myTheme != null) {
      configuration.setTheme(myTheme);
    }
  }

  /**
   * Returns the {@link org.cordovastudio.devices.State} by the given name for the given {@link org.cordovastudio.devices.Device}
   *
   * @param device the device
   * @param name   the name of the state
   */
  @Contract("!null, _ -> !null")
  @Nullable
  static State getState(@Nullable Device device, @Nullable String name) {
    if (device == null) {
      return null;
    }
    else if (name != null) {
      State state = device.getState(name);
      if (state != null) {
        return state;
      }
    }

    return device.getDefaultState();
  }
}
