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

package org.cordovastudio.startup;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.EmptyAction;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.PathMacros;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.ui.Messages;
import org.cordovastudio.actions.ActionRemover;
import org.cordovastudio.actions.NewProjectAction;
import org.cordovastudio.modules.CordovaModuleType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Intializer for CordovaStudio startup
 *
 * @author Christoffer T. Timm <kontakt@christoffertimm.de>
 */
public class CordovaStudioInitializer implements ApplicationComponent {

    private static final String DEFAULT_CORDOVA_EXECUTABLE_PATH = "/usr/local/bin/cordova";

    public CordovaStudioInitializer() {
    }

    /**
     * Initialize this ApplicationComponent
     *
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public void initComponent() {

        /*
         * Hide unneeded IDEA Actions
         */
        hideIdeaActions();

        /*
         * Replace IntelliJ Actions with CordovaStudio Actions
         */
        replaceNewProjectActions();

        /*
         * Initialize Configuration
         */
        initializePathVariables();

        /*
         * Initialize Modules
         */
        unregisterIdeaModuleTypes();
        registerCordovaModuleType();
    }

    private void hideIdeaActions() {
        hideAction("NewPackageInfo", "package-info.java");

        hideAction("NewForm", "GUI Form");
        hideAction("NewDialog", "Dialog");
        hideAction("NewFormSnapshot", "Form Snapshot");
        replaceAction("Groovy.NewClass", new EmptyAction());
        replaceAction("Groovy.NewScript", new EmptyAction());
        hideAction("NewModule", "New Module...");
        hideAction("NewModuleInGroup", "Module");
        hideAction("CreateLibraryFromFile", "Add As Library...");
        hideAction("ImportModule", "Import Module...");
        //hideAction(IdeActions.GROUP_MOVE_MODULE_TO_GROUP, "Move Module to Group");
        hideAction(IdeActions.MODULE_SETTINGS, "Module Settings");

        //hideAction(IdeActions.GROUP_WELCOME_SCREEN_DOC, "Docs and How-Tos");
        //hideAction(IdeActions.GROUP_WELCOME_SCREEN_QUICKSTART, "WelcomeScreen.QuickStart"); //TODO: find name
        hideAction(IdeActions.ACTION_EXTERNAL_JAVADOC, "External Documentation");
        hideAction(IdeActions.ACTION_QUICK_JAVADOC, "Quick Documentation");

        hideAction("AddFrameworkSupport", "Add Framework Support...");

        hideAction(IdeActions.ACTION_GENERATE_ANT_BUILD, "Generate Ant Build...");
        hideAction("BuildArtifact", "Build Artifacts...");
        hideAction("RunTargetAction", "Run Ant Target");
        hideAction(IdeActions.ACTION_MAKE_MODULE, "Make Module");
        hideAction(IdeActions.ACTION_GENERATE_ANT_BUILD, "Generate Ant Build...");
        hideAction(IdeActions.ACTION_INSPECT_CODE, "Inspect Code...");
        //hideAction(IdeActions.GROUP_DEBUGGER, "DebuggerActions"); //TODO: find name
        hideAction(IdeActions.ACTION_DEFAULT_DEBUGGER, "Debug");
        hideAction(IdeActions.ACTION_TOGGLE_LINE_BREAKPOINT, "Toggle Line Breakpoint");
        //hideAction(IdeActions.GROUP_USAGE_VIEW_POPUP, "UsageView.Popup"); //TODO: find name
        hideAction(IdeActions.ACTION_GOTO_DECLARATION, "Declaration");
        hideAction(IdeActions.ACTION_GOTO_TYPE_DECLARATION, "Type Declaration");
        hideAction(IdeActions.ACTION_GOTO_IMPLEMENTATION, "Implementation(s)");

        hideAction(IdeActions.ACTION_ANALYZE_DEPENDENCIES, "Analyze Dependencies...");
        hideAction(IdeActions.ACTION_ANALYZE_BACK_DEPENDENCIES, "Analyze Backward Dependencies...");
        hideAction(IdeActions.ACTION_ANALYZE_CYCLIC_DEPENDENCIES, "Analyze Cyclic Dependencies...");

        //hideAction(IdeActions.GROUP_REFACTOR, "Refactor");

        hideAction(IdeActions.ACTION_TYPE_HIERARCHY, "Class Hierarchy");
        hideAction(IdeActions.ACTION_METHOD_HIERARCHY, "Method Hierarchy");
        hideAction(IdeActions.ACTION_CALL_HIERARCHY, "Call Hierarchy");
        //hideAction(IdeActions.GROUP_TYPE_HIERARCHY_POPUP, "TypeH ierarchy");
        //hideAction(IdeActions.GROUP_METHOD_HIERARCHY_POPUP, "Method Hierarchy");
        //hideAction(IdeActions.GROUP_CALL_HIERARCHY_POPUP, "Call Hierarchy");

        //hideAction(IdeActions.GROUP_COMMANDER_POPUP, "Commander");

        //hideAction(IdeActions.GROUP_TESTTREE_POPUP, "TestTreePopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_TESTSTATISTICS_POPUP, "TestStatisticsTablePopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_J2EE_VIEW_POPUP, "J2EEViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_EJB_TRANSACTION_ATTRIBUTES_VIEW_POPUP, "EjbTransactionAttributesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_EJB_ENVIRONMENT_ENTRIES_VIEW_POPUP, "EjbEnvironmentEntriesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_EJB_REFERENCES_VIEW_POPUP, "EjbReferencesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_SECURITY_ROLES_VIEW_POPUP, "SecurityRolesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_PARAMETERS_VIEW_POPUP, "ParametersViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_SERVLET_MAPPING_VIEW_POPUP, "ServletMappingViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_EJB_RESOURCE_REFERENCES_VIEW_POPUP, "EjbResourceReferencesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_EJB_RESOURCE_ENVIRONMENT_REFERENCES_VIEW_POPUP, "EjbResourceEnvironmentReferencesViewPopupMenu"); //TODO: find name
        //hideAction(IdeActions.GROUP_ADD_SUPPORT, "AddSupportGroup");

        hideAction(IdeActions.ACTION_QUICK_IMPLEMENTATIONS, "Quick Definition");
    }

    /**
     * Ungerister the standard IDEA actions for project creation
     * and replace it with our own.
     *
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private static void replaceNewProjectActions() {
        replaceAction("NewProject", new NewProjectAction());
        replaceAction("WelcomeScreen.CreateNewProject", new NewProjectAction());

//        replaceAction("ImportProject", new ImportProjectAction());
//        replaceAction("WelcomeScreen.ImportProject", new ImportProjectAction());

    }

    /**
     * Check for existence of Cordova executable and set (if applicable) path variables.
     *
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private static void initializePathVariables() {
        PathMacros pathMacros = PathMacros.getInstance();

        String cordovaPath = pathMacros.getValue("CORDOVA_EXECUTABLE");

        if(cordovaPath == null) {
            if(new File(DEFAULT_CORDOVA_EXECUTABLE_PATH).exists()) {
                pathMacros.setMacro("CORDOVA_EXECUTABLE", "/usr/local/bin/cordova");
            } else {
                pathMacros.setMacro("CORDOVA_EXECUTABLE", "");
                Messages.showMessageDialog("Path to Cordova binary can not be found in default location (/usr/local/bin/). Please set 'CORDOVA_EXECUTABLE' in Path Variables in preferences!", "Cordova not found!", Messages.getErrorIcon());
            }
        }
    }

    /**
     * Hides a specific action.
     *
     * @param actionId  ID of the action to be replaced
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private static void hideAction(@NotNull String actionId, @NotNull String backupText) {
        AnAction oldAction = ActionManager.getInstance().getAction(actionId);
        if (oldAction != null) {
            AnAction newAction = new ActionRemover(oldAction, backupText);
            replaceAction(actionId, newAction);
        }
    }

    /**
     * Replace a specific action with another one.
     * (The action icon will stay the same.)
     *
     * @param actionId  ID of the action to be replaced
     * @param newAction New action
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private static void replaceAction(String actionId, AnAction newAction) {
        ActionManager am = ActionManager.getInstance();
        AnAction oldAction = am.getAction(actionId);
        if (oldAction != null) {
            newAction.getTemplatePresentation().setIcon(oldAction.getTemplatePresentation().getIcon());
            am.unregisterAction(actionId);
        }
        am.registerAction(actionId, newAction);
    }

    private static void unregisterIdeaModuleTypes() {
        //TODO: derive com.intellij.openapi.module.impl.ModuleTypeManagerImpl and add function for unregistering ModuleTypes
    }

    private static void registerCordovaModuleType() {
        ModuleTypeManager.getInstance().registerModuleType(new CordovaModuleType());
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    /**
     * Return the name of this Component
     *
     * @return Component name
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @NotNull
    public String getComponentName() {
        return "CordovaStudioInitializer";
    }
}
