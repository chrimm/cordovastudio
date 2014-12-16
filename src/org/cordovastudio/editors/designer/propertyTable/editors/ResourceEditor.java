/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.propertyTable.editors.ResourceEditor)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Added support for new introduces AttributeFormats (Enum, Empty, OnOff)
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
package org.cordovastudio.editors.designer.propertyTable.editors;

import com.intellij.designer.model.PropertiesContainer;
import com.intellij.designer.model.PropertyContext;
import com.intellij.designer.propertyTable.InplaceContext;
import com.intellij.designer.propertyTable.PropertyEditor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.ArrayUtil;
import org.cordovastudio.dom.AttributeFormat;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadModelBuilder;
import org.cordovastudio.editors.designer.model.RadPropertyContext;
import org.cordovastudio.editors.designer.model.ResourceType;
import org.cordovastudio.editors.designer.propertyTable.renderers.ResourceRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Alexander Lobas
 */
public class ResourceEditor extends PropertyEditor {
    private final ResourceType[] myTypes;
    protected ComponentWithBrowseButton myEditor;
    protected RadComponent myRootComponent;
    protected RadComponent myComponent;
    private JCheckBox myCheckBox;
    private final Border myCheckBoxBorder = new JTextField().getBorder();
    private boolean myIgnoreCheckBoxValue;
    private String myBooleanResourceValue;
    private final boolean myIsDimension;

    public ResourceEditor(Set<AttributeFormat> formats) {
        this(convertTypes(formats), formats);
    }

    public ResourceEditor(@Nullable ResourceType[] types, Set<AttributeFormat> formats) {
        myTypes = types;
        myIsDimension = formats.contains(AttributeFormat.Dimension);

        if (formats.contains(AttributeFormat.Boolean)) {
            myCheckBox = new JCheckBox();
            myEditor = new ComponentWithBrowseButton<JCheckBox>(myCheckBox, null) {
                @Override
                public Dimension getPreferredSize() {
                    return getComponentPreferredSize();
                }
            };
            myCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (!myIgnoreCheckBoxValue) {
                        myBooleanResourceValue = null;
                        fireValueCommitted(true, true);
                    }
                }
            });
        } else {
            myEditor = new TextFieldWithBrowseButton() {
                @Override
                protected void installPathCompletion(FileChooserDescriptor fileChooserDescriptor,
                                                     @Nullable Disposable parent) {
                }

                @Override
                public Dimension getPreferredSize() {
                    return getComponentPreferredSize();
                }
            };
            myEditor.registerKeyboardAction(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

            JTextField textField = getComboText();
            textField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireValueCommitted(true, true);
                }
            });
            textField.getDocument().addDocumentListener(
                    new DocumentAdapter() {
                        @Override
                        protected void textChanged(final DocumentEvent e) {
                            preferredSizeChanged();
                        }
                    }
            );
        }

        if (myCheckBox == null) {
            myEditor.registerKeyboardAction(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        }

        myEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
        myEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                myEditor.getChildComponent().requestFocus();
            }
        });
    }

    private Dimension getComponentPreferredSize() {
        Dimension size1 = myEditor.getChildComponent().getPreferredSize();
        Dimension size2 = myEditor.getButton().getPreferredSize();
        return new Dimension(Math.max(size1.width, 25) + 5 + size2.width, size1.height);
    }

    private static ResourceType[] convertTypes(Set<AttributeFormat> formats) {
        Set<ResourceType> types = EnumSet.noneOf(ResourceType.class);
        for (AttributeFormat format : formats) {
            switch (format) {
                case Empty:
                case OnOff:
                case Boolean:
                    types.add(ResourceType.BOOL);
                    break;
                case Color:
                    return new ResourceType[]{ResourceType.COLOR, ResourceType.DRAWABLE};//ChooseResourceDialog.COLOR_TYPES;
                case Dimension:
                    types.add(ResourceType.DIMEN);
                    break;
                case Integer:
                    types.add(ResourceType.INTEGER);
                    break;
                case Fraction:
                    types.add(ResourceType.FRACTION);
                    break;
                case String:
                    types.add(ResourceType.STRING);
                    break;
                case Reference:
                    types.add(ResourceType.COLOR);
                    types.add(ResourceType.DRAWABLE);
                    types.add(ResourceType.STRING);
                    types.add(ResourceType.ID);
                    types.add(ResourceType.STYLE);
                    break;
            }
        }

        return types.toArray(new ResourceType[types.size()]);
    }

    @NotNull
    @Override
    public JComponent getComponent(@Nullable PropertiesContainer container,
                                   @Nullable PropertyContext context,
                                   Object object,
                                   @Nullable InplaceContext inplaceContext) {
        myComponent = (RadComponent) container;
        myRootComponent = context instanceof RadPropertyContext ? ((RadPropertyContext) context).getRootComponent() : null;

        String value = (String) object;
        JTextField text = getComboText();

        if (text == null) {
            if (StringUtil.isEmpty(value) || value.equals("true") || value.equals("false")) {
                myBooleanResourceValue = null;
            } else {
                myBooleanResourceValue = value;
            }

            try {
                myIgnoreCheckBoxValue = true;
                myCheckBox.setSelected(Boolean.parseBoolean(value));
            } finally {
                myIgnoreCheckBoxValue = false;
            }

            if (inplaceContext == null) {
                myEditor.setBorder(null);
                myCheckBox.setText(null);
            } else {
                myEditor.setBorder(myCheckBoxBorder);
                myCheckBox.setText(myBooleanResourceValue);
            }
        } else {
            text.setText(value);
            if (inplaceContext != null) {
                text.setColumns(0);
                if (inplaceContext.isStartChar()) {
                    text.setText(inplaceContext.getText(text.getText()));
                }
            }
        }
        return myEditor;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        JTextField text = getComboText();
        return text == null ? myCheckBox : text;
    }

    @Override
    public Object getValue() {
        JTextField text = getComboText();
        if (text == null) {
            return myBooleanResourceValue == null ? Boolean.toString(myCheckBox.isSelected()) : myBooleanResourceValue;
        }
        String value = text.getText();
        if (value == StringsComboEditor.UNSET || StringUtil.isEmpty(value)) {
            return null;
        }
        if (myIsDimension &&
                !value.startsWith("@") &&
                !value.endsWith("dip") &&
                !value.equalsIgnoreCase("wrap_content") &&
                !value.equalsIgnoreCase("fill_parent") &&
                !value.equalsIgnoreCase("match_parent")) {
            if (value.length() <= 2) {
                return value + "dp";
            }
            int index = value.length() - 2;
            String dimension = value.substring(index);
            if (ArrayUtil.indexOf(ResourceRenderer.DIMENSIONS, dimension) == -1) {
                return value + "dp";
            }
        }
        return value;
    }

    @Override
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(myEditor);
    }

    protected void showDialog() {
        Module module = RadModelBuilder.getModule(myRootComponent);
        if (module == null) {
            if (myBooleanResourceValue != null) {
                fireEditingCancelled();
            }
            return;
        }
        //ResourceDialog dialog = new ResourceDialog(module, myTypes, (String)getValue(), (RadViewComponent)myComponent);
        //dialog.show();

        //if (dialog.isOK()) {
        //  setValue(dialog.getResourceName());
        //}
        //else if (myBooleanResourceValue != null) {
        fireEditingCancelled();
        //}
    }

    protected final void setValue(String value) {
        JTextField text = getComboText();
        if (text == null) {
            myBooleanResourceValue = value;
            fireValueCommitted(false, true);
        } else {
            text.setText(value);
            fireValueCommitted(true, true);
        }
    }

    private JTextField getComboText() {
        JComponent component = myEditor.getChildComponent();
        if (component instanceof JTextField) {
            return (JTextField) component;
        }
        if (component instanceof JComboBox) {
            JComboBox combo = (JComboBox) component;
            return (JTextField) combo.getEditor().getEditorComponent();
        }
        return null;
    }

    private static final class MyComboBox extends ComboBox {
        public MyComboBox() {
            ((JTextField) getEditor().getEditorComponent()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isPopupVisible()) {
                        ComboPopup popup = getPopup();
                        if (popup != null) {
                            setSelectedItem(popup.getList().getSelectedValue());
                        }
                    }
                }
            });
        }
    }
}