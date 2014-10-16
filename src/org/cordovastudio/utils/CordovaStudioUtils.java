/*
 * Copyright 2000-2011 JetBrains s.r.o.
 * (Original as of org.jetbrains.android.util.AndroidUtils)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Removed everything not needed for Cordova Studio
 *  – Imported getStackTrace(Throwable t) from org.jetbrains.android.util.AndroidCommonUtils
 *  – Slight adjustments for Cordova Studio (i.e. renaming of classes etc.)
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
package org.cordovastudio.utils;

import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.PsiNavigateUtil;
import com.intellij.util.containers.HashSet;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.cordovastudio.build.CordovaRunConfiguration;
import org.cordovastudio.build.CordovaRunConfigurationType;
import org.cordovastudio.build.TargetSelectionMode;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;

/**
 * @author yole, coyote
 */
public class CordovaStudioUtils {
    private static final Logger LOG = Logger.getInstance("#org.jetbrains.android.util.AndroidUtils");

    @NonNls
    public static final String ACTIVITY_BASE_CLASS_NAME = "android.app.Activity";
    @NonNls
    public static final String LAUNCH_ACTION_NAME = "android.intent.action.MAIN";

    private CordovaStudioUtils() {
    }

    @Nullable
    public static <T extends DomElement> T loadDomElement(@NotNull final Module module,
                                                          @NotNull final VirtualFile file,
                                                          @NotNull final Class<T> aClass) {
        return loadDomElement(module.getProject(), file, aClass);
    }

    @Nullable
    public static <T extends DomElement> T loadDomElement(@NotNull final Project project,
                                                          @NotNull final VirtualFile file,
                                                          @NotNull final Class<T> aClass) {
        return ApplicationManager.getApplication().runReadAction(new Computable<T>() {
            @Override
            @Nullable
            public T compute() {
                if (project.isDisposed()) return null;
                PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                if (psiFile instanceof XmlFile) {
                    return loadDomElementWithReadPermission(project, (XmlFile) psiFile, aClass);
                } else {
                    return null;
                }
            }
        });
    }

    /**
     * This method should be called under a read action.
     */
    @Nullable
    public static <T extends DomElement> T loadDomElementWithReadPermission(@NotNull Project project,
                                                                            @NotNull XmlFile xmlFile,
                                                                            @NotNull Class<T> aClass) {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        DomManager domManager = DomManager.getDomManager(project);
        DomFileElement<T> element = domManager.getFileElement(xmlFile, aClass);
        if (element == null) return null;
        return element.getRootElement();
    }

    @Nullable
    public static VirtualFile findSourceRoot(@NotNull Module module, VirtualFile file) {
        final Set<VirtualFile> sourceRoots = new HashSet<VirtualFile>();
        Collections.addAll(sourceRoots, ModuleRootManager.getInstance(module).getSourceRoots());

        while (file != null) {
            if (sourceRoots.contains(file)) {
                return file;
            }
            file = file.getParent();
        }
        return null;
    }

    public static void addRunConfiguration(@NotNull final CordovaFacet facet, @Nullable final String activityClass, final boolean ask,
                                           @Nullable final TargetSelectionMode targetSelectionMode,
                                           @Nullable final String preferredAvdName) {
        final Module module = facet.getModule();
        final Project project = module.getProject();

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                final RunManagerEx runManager = RunManagerEx.getInstanceEx(project);
                final RunnerAndConfigurationSettings settings = runManager.
                        createRunConfiguration(module.getName(), CordovaRunConfigurationType.getInstance().getFactory());
                final CordovaRunConfiguration configuration = (CordovaRunConfiguration) settings.getConfiguration();
                configuration.setModule(module);

                if (activityClass != null) {
                    configuration.MODE = CordovaRunConfiguration.LAUNCH_SPECIFIC_ACTIVITY;
                    configuration.ACTIVITY_CLASS = activityClass;
                } else {
                    configuration.MODE = CordovaRunConfiguration.LAUNCH_DEFAULT_ACTIVITY;
                }

                if (targetSelectionMode != null) {
                    configuration.setTargetSelectionMode(targetSelectionMode);
                }
                if (preferredAvdName != null) {
                    configuration.PREFERRED_AVD = preferredAvdName;
                }
                runManager.addConfiguration(settings, false);
                runManager.setActiveConfiguration(settings);
            }
        };
        if (!ask) {
            r.run();
        } else {
            UIUtil.invokeLaterIfNeeded(new Runnable() {
                @Override
                public void run() {
                    final String moduleName = facet.getModule().getName();
                    final int result = Messages.showYesNoDialog(project, "Do you want to create run configuration for module " + moduleName + "?",
                            "Create Run Configuration", Messages.getQuestionIcon());
                    if (result == Messages.YES) {
                        r.run();
                    }
                }
            });
        }
    }

    @Nullable
    public static PsiFile getContainingFile(@NotNull PsiElement element) {
        return element instanceof PsiFile ? (PsiFile) element : element.getContainingFile();
    }

    public static void navigateTo(@NotNull PsiElement[] targets, @Nullable RelativePoint pointToShowPopup) {
        if (targets.length == 0) {
            final JComponent renderer = HintUtil.createErrorLabel("Empty text");
            final JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(renderer, renderer).createPopup();
            if (pointToShowPopup != null) {
                popup.show(pointToShowPopup);
            }
            return;
        }
        if (targets.length == 1 || pointToShowPopup == null) {
            PsiNavigateUtil.navigate(targets[0]);
        } else {
            DefaultPsiElementCellRenderer renderer = new DefaultPsiElementCellRenderer() {
                @Override
                public String getElementText(PsiElement element) {
                    final PsiFile file = getContainingFile(element);
                    return file != null ? file.getName() : super.getElementText(element);
                }

                @Override
                public String getContainerText(PsiElement element, String name) {
                    final PsiFile file = getContainingFile(element);
                    final PsiDirectory dir = file != null ? file.getContainingDirectory() : null;
                    return dir == null ? "" : '(' + dir.getName() + ')';
                }
            };
            final JBPopup popup = NavigationUtil.getPsiElementPopup(targets, renderer, null);
            popup.show(pointToShowPopup);
        }
    }

    public static void showStackStace(@NotNull final Project project, @NotNull Throwable[] throwables) {
        final StringBuilder messageBuilder = new StringBuilder();

        for (Throwable t : throwables) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n\n");
            }
            messageBuilder.append(getStackTrace(t));
        }

        final DialogWrapper wrapper = new DialogWrapper(project, false) {

            {
                init();
            }

            @Override
            protected JComponent createCenterPanel() {
                final JPanel panel = new JPanel(new BorderLayout());
                final JTextArea textArea = new JTextArea(messageBuilder.toString());
                textArea.setEditable(false);
                textArea.setRows(40);
                textArea.setColumns(70);
                panel.add(ScrollPaneFactory.createScrollPane(textArea));
                return panel;
            }
        };
        wrapper.setTitle("Stack trace");
        wrapper.show();
    }


    @NotNull
    public static String getStackTrace(@NotNull Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        try {
            t.printStackTrace(writer);
            return stringWriter.toString();
        } finally {
            writer.close();
        }
    }
}
