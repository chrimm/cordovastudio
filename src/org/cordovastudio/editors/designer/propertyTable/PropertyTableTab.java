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
package org.cordovastudio.editors.designer.propertyTable;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Alexander Lobas
 */
public final class PropertyTableTab {
  private final String myKey;
  private final String myDescription;
  private final Icon myIcon;

  public PropertyTableTab(@NotNull String key, @NotNull String description, @NotNull Icon icon) {
    myKey = key;
    myDescription = description;
    myIcon = icon;
  }

  @NotNull
  public String getKey() {
    return myKey;
  }

  @NotNull
  public String getDescription() {
    return myDescription;
  }

  @NotNull
  public Icon getIcon() {
    return myIcon;
  }
}