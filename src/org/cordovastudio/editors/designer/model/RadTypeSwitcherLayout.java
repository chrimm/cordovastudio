/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package org.cordovastudio.editors.designer.model;


import org.cordovastudio.editors.designer.designSurface.OperationContext;

/**
 * @author Alexander Lobas
 */
public class RadTypeSwitcherLayout extends RadViewSwitcherLayout {
  private final String myTypeTag;
  private MetaModel myTypeModel;

  public RadTypeSwitcherLayout(String tag) {
    myTypeTag = tag;
  }

  @Override
  protected boolean checkChildOperation(OperationContext context) {
    if (super.checkChildOperation(context)) {
      if (myTypeModel == null) {
        MetaManager manager = ViewsMetaManager.getInstance(((RadViewComponent)myContainer).getTag().getProject());
        myTypeModel = manager.getModelByTag(myTypeTag);
      }

      return true;
    }

    return false;
  }
}