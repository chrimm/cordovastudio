/*
 * Copyright (C) 2014 Christoffer T. Timm
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

package org.cordovastudio.editors.designer.propertyTable.editors;

import com.intellij.designer.model.PropertiesContainer;
import com.intellij.designer.model.PropertyContext;
import com.intellij.designer.propertyTable.InplaceContext;
import com.intellij.designer.propertyTable.editors.ComboEditor;
import com.intellij.openapi.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * Created by cti on 16.12.14.
 */
public class EnumComboEditor extends ComboEditor {
    public static final String UNSET = "<unset>";

    public EnumComboEditor(Vector<KeyValue<String, String>> values) {

        DefaultComboBoxModel model = new DefaultComboBoxModel<>(values);
        model.insertElementAt(UNSET, 0);
        myCombo.setModel(model);
        myCombo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myCombo.getSelectedItem() == UNSET) {
                    myCombo.setSelectedItem(null);
                }
            }
        });

        myCombo.setSelectedIndex(0);
    }

    @Override
    public Object getValue() throws Exception {
        Object itemObj = myCombo.getSelectedItem();
        if(itemObj instanceof KeyValue) {
            KeyValue<String, String> item = (KeyValue<String, String>)itemObj;
            return item.getValue();
        }
        //return item == UNSET ? null : item;
        return null;
    }

    @NotNull
    @Override
    public JComponent getComponent(@Nullable PropertiesContainer container,
                                   @Nullable PropertyContext context, Object value,
                                   @Nullable InplaceContext inplaceContext) {
        myCombo.setSelectedItem(value);
        myCombo.setBorder(inplaceContext == null ? null : myComboBorder);
        return myCombo;
    }
}
