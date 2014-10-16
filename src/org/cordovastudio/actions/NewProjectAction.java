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

package org.cordovastudio.actions;

import com.intellij.facet.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.cordovastudio.dialogs.wizards.CordovaStudioNewProjectDialog;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.modules.CordovaModuleBuilder;
import org.cordovastudio.modules.CordovaModuleType;
import org.cordovastudio.modules.CordovaModuleTypeId;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.cordovastudio.GlobalConstants.DEBUG;

/**
 * Created by cti on 23.08.14.
 */
public class NewProjectAction extends AnAction{

    private static final Logger LOG = Logger.getInstance(NewProjectAction.class);
    private static String ERROR_MESSAGE_TITLE = "Project Creation Error";

    public NewProjectAction() {
        super("New Project...");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        CordovaStudioNewProjectDialog dialog = new CordovaStudioNewProjectDialog();

        dialog.show();

        if(dialog.isOK()) {

            LOG.info("Creating new Cordova project...");

            /*
             * Create project with details from dialog
             */
            final ProjectManager pm = ProjectManager.getInstance();
            final String projectPath = dialog.getProjectPath();
            final String projectName = dialog.getProjectName();
            final String projectId = dialog.getProjectId();

            if ("".equals(projectPath)) {
                Messages.showMessageDialog("Project Path can not be empty!", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                return;
            }

            File projectPathFileHandle = new File(projectPath);

            /*
             * Check if parent path exists, else create it (createParentDirs() does both of it)
             */
            if (!FileUtilRt.createParentDirs(projectPathFileHandle)) {
                Messages.showMessageDialog("Project Path could not be created!", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                return;
            }

            File parentPath = projectPathFileHandle.getParentFile();

            if (projectPathFileHandle.exists()) {
                if(projectPathFileHandle.list().length > 0) {
                    Messages.showMessageDialog("The specified project directory contains files. Cordova project can not be created there. You need to specify a non existent or empty directory.", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                    return;
                } else {
                    /* Delete File */
                    if(!FileUtilRt.delete(projectPathFileHandle)) {
                        Messages.showMessageDialog("The specified project directory could not be altered. Cordova project can not be created there. You need to have write access.", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                        return;
                    }
                }

            }

            /*
                     * Attention! Cordova will try to create the TARGET directory, so it MUST NOT exist already.
                     * The target's PARENT directory, though, MUST exist.
                     */
            String execStatement;

            if(DEBUG) {
                execStatement = PathMacros.getInstance().getValue("CORDOVA_EXECUTABLE") + " -d create " + projectPath + "/ " + projectId + " " + projectName;
            } else {
                execStatement = PathMacros.getInstance().getValue("CORDOVA_EXECUTABLE") + " create " + projectPath + "/ " + projectId + " " + projectName;
            }

            try {
                LOG.info("Trying to execute '" + execStatement + "'...");

                Process proc = Runtime.getRuntime().exec(execStatement);
                proc.waitFor();

                int exitCode = proc.exitValue();

                if (exitCode > 0) {
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(proc.getErrorStream()));

                    StringBuffer output = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                    }

                    LOG.warn("Cordova process returned with ExitCode " + exitCode + ". Message: " + output.toString());

                    if (output.toString().contains("node: No such file or directory")) {
                        Messages.showMessageDialog("NodeJS binary not found. Please install NodeJS (from http://nodejs.org/) and check PATH environment variable.", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                    } else {
                        Messages.showMessageDialog("Cordova could not be executed. Exit code: " + exitCode + ". Message:\n" + output.toString(), ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
                    }

                    return;
                } else {
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(proc.getInputStream()));

                    StringBuffer output = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                    }
                    LOG.info(output.toString());
                    LOG.info("Cordova project created. (ExitCode " + exitCode + ")");
                }

            } catch (IOException e) {
                Messages.showMessageDialog("Cordova binaries could not be executed.\n" + e.getMessage(), ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            /* Create IDEA project files (i.e. .idea directory) */

            final Project project = pm.createProject(projectName, projectPath);

            LOG.assertTrue(project != null, "Project is null");

            /* Create Cordova project */
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    Module module = ModuleManager.getInstance(project).newModule(projectPath + "/" + projectName + ".iml", CordovaModuleTypeId.CORDOVA_MODULE);

                    CordovaModuleBuilder moduleBuilder = CordovaModuleType.getInstance().createModuleBuilder();

                    moduleBuilder.commitModule(project, ModuleManager.getInstance(project).getModifiableModel());

                    FacetType type = FacetTypeRegistry.getInstance().findFacetType("cordova");
                    Facet facet = type.createFacet(module, "Cordova Facet", type.createDefaultConfiguration(), null);
                    ModifiableFacetModel model = FacetManager.getInstance(module).createModifiableModel();
                    model.addFacet(facet);
                    model.commit();

                    project.save();
                }
            });

            /* Load project */
//            try {
//                pm.loadAndOpenProject(project.getBasePath());
//
//                /* Open /www/index.html in Editor */
//                VirtualFile indexHtmlFile  = LocalFileSystem.getInstance().findFileByIoFile(new File(project.getBasePath()+"/www/index.html"));
//
//                if(indexHtmlFile == null) {
//                    Messages.showMessageDialog("File '/www/index.html' could not be opened. Please check if it exists and try to reopen.", ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
//                    return;
//                }
//
//                //FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, indexHtmlFile), true);
//
//            } catch (Exception e) {
//                Messages.showMessageDialog(e.getMessage(), ERROR_MESSAGE_TITLE, Messages.getErrorIcon());
//            }

        }
    }
}
