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
package org.cordovastudio.actions;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import org.cordovastudio.editors.designer.actions.AbstractGravityAction;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.model.Gravity;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadComponentOperations;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.cordovastudio.GlobalConstants.*;

/**
 * @author Alexander Lobas
 */
public class AllGravityAction extends AbstractGravityAction<Gravity> {
  public AllGravityAction(CordovaDesignerEditorPanel designer, List<? extends RadViewComponent> components) {
    super(designer, components);
    setItems(Arrays.asList(Gravity.values()), null);
  }

  private List<Gravity> mySelection;

  @NotNull
  @Override
  protected DefaultActionGroup createPopupActionGroup(JComponent button) {
    Iterator<? extends RadViewComponent> iterator = myComponents.iterator();
    int flags = 0;
    if (iterator.hasNext()) {
      flags = Gravity.getFlags(iterator.next());
      while (iterator.hasNext()) {
        if (flags != Gravity.getFlags(iterator.next())) {
          flags = 0;
          break;
        }
      }
    }
    mySelection = Gravity.flagToValues(flags);

    return super.createPopupActionGroup(button);
  }

  @Override
  protected void update(Gravity item, Presentation presentation, boolean popup) {
    if (popup) {
      presentation.setIcon(mySelection.contains(item) ? CHECKED : null);
      presentation.setText(item.name());
    }
  }

  @Override
  protected boolean selectionChanged(Gravity item) {
    int index = mySelection.indexOf(item);
    if (index == -1) {
      mySelection.add(item);
    }
    else {
      mySelection.remove(index);
    }
    execute(new Runnable() {
      @Override
      public void run() {
        if (mySelection.isEmpty()) {
          for (RadComponent component : myComponents) {
            RadComponentOperations.deleteAttribute(component, "layout_gravity");
          }
        }
        else {
          String value = StringUtil.join(mySelection, new Function<Gravity, String>() {
              @Override
              public String fun(Gravity gravity) {
                  return gravity.name();
              }
          }, "|");

          for (RadComponent component : myComponents) {
            XmlTag tag = ((RadViewComponent)component).getTag();
            tag.setAttribute("layout_gravity", NS_RESOURCES, value);
          }
        }
      }
    });

    return false;
  }

  @Override
  public void setSelection(Gravity selection) {
  }
}