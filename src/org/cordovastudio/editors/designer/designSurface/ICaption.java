/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.designer.designSurface;


import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadLayout;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Alexander Lobas
 */
public interface ICaption {
  @NotNull
  RadLayout getCaptionLayout(IEditableArea mainArea, boolean horizontal);

  @NotNull
  List<RadComponent> getCaptionChildren(IEditableArea mainArea, boolean horizontal);
}