/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * (Orinigal as of com.intellij.android.designer.model.RadAbsListView)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Adjusted for Cordova projects
 *  TODO: Do we really need this? How are Lists represented in Cordova??
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

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.designer.designSurface.CordovaDesignerEditorPanel;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static org.cordovastudio.GlobalConstants.TOOLS_URI;

public class RadAbsListView extends RadViewComponent {

    @Override
    public boolean addPopupActions(@NotNull CordovaDesignerEditorPanel designer,
                                   @NotNull DefaultActionGroup beforeGroup,
                                   @NotNull DefaultActionGroup afterGroup,
                                   @Nullable JComponent shortcuts,
                                   @NotNull List<RadComponent> selection) {
        super.addPopupActions(designer, beforeGroup, afterGroup, shortcuts, selection);

        RenderConfiguration configuration = designer.getConfiguration();
        Module module = designer.getModule();

        beforeGroup.add(createListTypeAction(designer));
        beforeGroup.addSeparator();

        return true;
    }

    private DefaultActionGroup createListTypeAction(CordovaDesignerEditorPanel designer) {
        XmlTag tag = getTag();
        String tagName = tag.getName();
        boolean isSpinner = tagName.equals("Spinner");
        boolean isGridView = tagName.equals("GridView");
        String previewType = isGridView ? "Preview Grid Content" : isSpinner ? "Preview Spinner Layout" : "Preview List Content";

        String selected = tag.getAttributeValue("listitem", TOOLS_URI);

        DefaultActionGroup previewGroup = new DefaultActionGroup("_" + previewType, true);

        previewGroup.add(new PickLayoutAction(designer, "Choose Item Layout...", "listitem"));
        previewGroup.addSeparator();

        if (isSpinner) {
            previewGroup.add(new SetListTypeAction(designer, "Spinner Item", "simple_spinner_item", selected));
            previewGroup.add(new SetListTypeAction(designer, "Spinner Dropdown Item", "simple_spinner_dropdown_item", selected));
        } else {
            previewGroup.add(new SetListTypeAction(designer, "Simple List Item", "simple_list_item_1", selected));

            previewGroup.add(new SetListTypeAction(designer, "Simple 2-Line List Item", "simple_list_item_2", selected));
            previewGroup.add(new SetListTypeAction(designer, "Checked List Item", "simple_list_item_checked", selected));
            previewGroup.add(new SetListTypeAction(designer, "Single Choice List Item", "simple_list_item_single_choice", selected));
            previewGroup.add(new SetListTypeAction(designer, "Multiple Choice List Item", "simple_list_item_multiple_choice", selected));

            if (!isGridView) {
                previewGroup.addSeparator();
                previewGroup.add(new SetListTypeAction(designer, "Simple Expandable List Item", "simple_expandable_list_item_1", selected));
                previewGroup.add(new SetListTypeAction(designer, "Simple 2-Line Expandable List Item", "simple_expandable_list_item_2", selected));

                previewGroup.addSeparator();
                previewGroup.add(new PickLayoutAction(designer, "Choose Header...", "listheader"));
                previewGroup.add(new PickLayoutAction(designer, "Choose Footer...", "listfooter"));
            }
        }

        return previewGroup;
    }

    private class PickLayoutAction extends AnAction {
        private final CordovaDesignerEditorPanel myPanel;
        private final String myType;

        private PickLayoutAction(CordovaDesignerEditorPanel panel, String title, String type) {
            super(title);
            myPanel = panel;
            myType = type;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
        /*
      ChooseResourceDialog dialog = new ChooseResourceDialog(myPanel.getModule(), new ResourceType[]{ResourceType.LAYOUT}, null, null);
      dialog.setAllowCreateResource(false);
      dialog.show();
      if (dialog.isOK()) {
        String layout = dialog.getResourceName();
        setNewType(myPanel, myType, layout);
      }
      */
        }
    }

    private class SetListTypeAction extends AnAction {
        private final CordovaDesignerEditorPanel myPanel;
        private final String myLayout;

        public SetListTypeAction(@NotNull CordovaDesignerEditorPanel panel,
                                 @NotNull String title,
                                 @NotNull String layout,
                                 @Nullable String selected) {
            super(title);
            myPanel = panel;
            myLayout = layout;

            if (layout.equals(selected)) {
                Presentation templatePresentation = getTemplatePresentation();
                templatePresentation.putClientProperty(Toggleable.SELECTED_PROPERTY, selected);
                templatePresentation.setIcon(AllIcons.Actions.Checked);
            }
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            setNewType(myPanel, "listitem", myLayout);
        }
    }

    private void setNewType(@NotNull CordovaDesignerEditorPanel panel,
                            @NotNull String type,
                            @Nullable String layout) {
        panel.requestRender();
    }
}
