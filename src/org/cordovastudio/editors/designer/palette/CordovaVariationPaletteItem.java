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
package org.cordovastudio.editors.designer.palette;

import org.cordovastudio.editors.designer.model.CordovaMetaModel;
import org.cordovastudio.editors.designer.model.MetaModel;
import org.cordovastudio.editors.designer.policies.FillPolicy;
import org.cordovastudio.editors.designer.policies.ResizePolicy;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

public class CordovaVariationPaletteItem extends VariationPaletteItem {
  private ResizePolicy myResizePolicy;
  private FillPolicy myFillPolicy;

  public CordovaVariationPaletteItem(PaletteItem defaultItem, MetaModel model, Element element) {
    super(defaultItem, model, element);

    String resize = element.getAttributeValue(CordovaMetaModel.ATTR_RESIZE);
    if (resize != null) {
      myResizePolicy = ResizePolicy.get(resize);
    }

    String fill = element.getAttributeValue(CordovaMetaModel.ATTR_FILL);
    if (fill != null) {
      myFillPolicy = FillPolicy.get(fill);
    }
  }

  @Nullable
  public ResizePolicy getResizePolicy() {
    return myResizePolicy;
  }

  public FillPolicy getFillPolicy() {
    return myFillPolicy;
  }
}
