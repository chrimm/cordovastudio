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

package org.cordovastudio.dialogs.wizards;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by cti on 23.08.14.
 */
public class CordovaStudioNewProjectDialog extends DialogWrapper implements ActionListener{
    private JTextField projectNameTextField;
    private JTextField projectIdTextField;
    private JTextField projectPathTextField;
    private JPanel centerPanel;
    private JButton choosePathButton;
    private JFileChooser fileChooser = new JFileChooser("");

    /**
     * Creates modal <code>DialogWrapper</code> that can be parent for other windows.
     * The currently active window will be the dialog's parent.
     *
     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
     * @see com.intellij.openapi.ui.DialogWrapper#DialogWrapper(com.intellij.openapi.project.Project, boolean)
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public CordovaStudioNewProjectDialog() {
        super(false);
        init();
        initialize();
    }

    /**
     * Initalize Dialog
     *
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private void initialize() {
        /*
         * Hook button events
         */
        choosePathButton.addActionListener(this);
    }

    /**
     * ActionListener for choosePathButton
     *
     * @param e The event
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == choosePathButton) {
            if (showFileChooser()) {
                projectPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    /**
     * Show FileChooser Dialog.
     * Returns true, if OK was clicked.
     *
     * @return True, if path selection was successful.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private boolean showFileChooser() {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose project directory");
        fileChooser.setApproveButtonText("Select");

        int retval = fileChooser.showDialog(null, "Select");
        return (retval == JFileChooser.APPROVE_OPTION);
    }

    /**
     * Return the project name as specified in dialog.
     *
     * @return The project name
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public String getProjectName() {
        return projectNameTextField.getText().trim();
    }

    /**
     * Return the path to the project root as specified in dialog.
     *
     * @return Path to project root
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public String getProjectPath() {
        return projectPathTextField.getText().trim();
    }

    /**
     * Return the project ID as specified in dialog.
     *
     * @return Project ID
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public String getProjectId() {
        return projectIdTextField.getText().trim();
    }

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return <code>null</code>
     * value. In this case there will be no options panel.
     *
     * @return center panel
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return (JComponent) centerPanel;
    }
}
