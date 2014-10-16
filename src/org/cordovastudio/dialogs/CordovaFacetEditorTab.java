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
package org.cordovastudio.dialogs;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.ComboboxWithBrowseButton;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import com.intellij.util.PathUtil;
import com.intellij.util.ui.UIUtil;
import org.cordovastudio.modules.CordovaFacet;
import org.cordovastudio.modules.CordovaFacetConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * @author yole
 */
public class CordovaFacetEditorTab extends FacetEditorTab {
  private static final Logger LOG = Logger.getInstance(CordovaFacetEditorTab.class);

  private final CordovaFacetConfiguration myConfiguration;
  private final FacetEditorContext myContext;
  private JPanel myContentPanel;
  private TextFieldWithBrowseButton myRGenPathField;
  private TextFieldWithBrowseButton myAidlGenPathField;
  private JButton myResetPathsButton;
  private TextFieldWithBrowseButton myResFolderField;
  private TextFieldWithBrowseButton myAssetsFolderField;
  private TextFieldWithBrowseButton myNativeLibsFolder;
  private TextFieldWithBrowseButton myManifestFileField;
    private JCheckBox myIsLibraryProjectCheckbox;
  private JPanel myAaptCompilerPanel;
    private JRadioButton myRunProcessResourcesRadio;
  private JRadioButton myCompileResourcesByIdeRadio;
  private JLabel myManifestFileLabel;
  private JLabel myResFolderLabel;
  private JLabel myAssetsFolderLabel;
  private JLabel myNativeLibsFolderLabel;
  private JLabel myAidlGenPathLabel;
  private JLabel myRGenPathLabel;
  private TextFieldWithBrowseButton myCustomDebugKeystoreField;
  private JBLabel myCustomKeystoreLabel;
    private ComboBox myUpdateProjectPropertiesCombo;
  private JBTabbedPane myTabbedPane;
    private JBCheckBox myEnableSourcesAutogenerationCheckBox;
  private JPanel myAptAutogenerationOptionsPanel;
  private JPanel myAidlAutogenerationOptionsPanel;


  public CordovaFacetEditorTab(FacetEditorContext context, CordovaFacetConfiguration facetConfiguration) {
    final Project project = context.getProject();
    myConfiguration = facetConfiguration;
    myContext = context;

    myManifestFileLabel.setLabelFor(myManifestFileField);
    myResFolderLabel.setLabelFor(myResFolderField);
    myAssetsFolderLabel.setLabelFor(myAssetsFolderField);
    myNativeLibsFolderLabel.setLabelFor(myNativeLibsFolder);
    myAidlGenPathLabel.setLabelFor(myAidlGenPathField);
    myRGenPathLabel.setLabelFor(myRGenPathField);
    //myCustomKeystoreLabel.setLabelFor(myCustomDebugKeystoreField);

    final CordovaFacet facet = (CordovaFacet)myContext.getFacet();

    Module module = myContext.getModule();
  }

  @Override
  @Nls
  public String getDisplayName() {
    return "Cordova Facet Settings";
  }

  @Override
  public JComponent createComponent() {
    return myContentPanel;
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public String getHelpTopic() {
    return null;
  }

  @Override
  public void apply() throws ConfigurationException {

  }

  @Override
  public void reset() {

  }

  @Override
  public void disposeUIResources() {
  }
}
