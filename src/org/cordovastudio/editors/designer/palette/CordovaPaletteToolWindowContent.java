/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.designer.palette;

import org.cordovastudio.editors.designer.LightToolWindowContent;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.jetbrains.annotations.Nullable;

public interface CordovaPaletteToolWindowContent extends LightToolWindowContent {
  @Nullable
  PaletteItem getActiveItem();

  void clearActiveItem();

  void refresh();

  boolean isEmpty();

  void loadPalette(@Nullable CordovaDesignerEditorPanel designer);
}