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

/** Interface implemented by clients getting notified about configuration attribute changes */
public interface RenderConfigurationListener {
  /** The {@link org.cordovastudio.devices.Device} in change flags or override flags */
  int CFG_DEVICE = 1 << 1;

  /** The {@link org.cordovastudio.devices.State} in change flags or override flags */
  int CFG_DEVICE_STATE = 1 << 2;

  /** The theme in change flags or override flags */
  int CFG_THEME = 1 << 3;

  /** The locale in change flags or override flags */
  int CFG_LOCALE = 1 << 4;

  /** The display name has changed */
  int CFG_NAME = 1 << 9;

  /** References all attributes */
  int MASK_ALL = 0xFFFF;

  /** Attributes which affect the full folder configuration (e.g. setting a target can add -vNN etc) */
  int MASK_FOLDERCONFIG = CFG_LOCALE | CFG_DEVICE | CFG_DEVICE_STATE;

  /** Attributes which affect which best-layout-file selection */
  int MASK_FILE_ATTRS = CFG_DEVICE | CFG_DEVICE_STATE | CFG_LOCALE;

  /** Attributes which affect resource resolution */
  int MASK_RESOLVE_RESOURCES = MASK_FOLDERCONFIG | CFG_THEME;

  /** Attributes which affect rendering appearance */
  int MASK_RENDERING = MASK_FILE_ATTRS | CFG_THEME;

  /** Attributes which are edited project-wide */
  int MASK_PROJECT_STATE = CFG_LOCALE | CFG_DEVICE;

  /**
   * The configuration has changed. If the client returns false, it means that
   * the change was rejected. This typically means that changing the
   * configuration in this particular way makes a configuration which has a
   * better file match than the current client's file, so it will open that
   * file to edit the new configuration -- and the current configuration
   * should go back to editing the state prior to this change.
   *
   * @param flags details about what changed; consult the {@code CFG_} flags
   *              such as {@link #CFG_DEVICE}, {@link #CFG_LOCALE}, etc.
   * @return true if the change was accepted, false if it was rejected.
   */
  boolean changed(int flags);
}