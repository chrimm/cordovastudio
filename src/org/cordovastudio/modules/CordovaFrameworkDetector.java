/*
 * Copyright 2000-2011 JetBrains s.r.o.
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
package org.cordovastudio.modules;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.DetectedFrameworkDescription;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.framework.detection.FrameworkDetectionContext;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.cordovastudio.GlobalConstants.FN_CORDOVA_MANIFEST_XML;

/**
 * @author nik
 */
public class CordovaFrameworkDetector extends FacetBasedFrameworkDetector<CordovaFacet, CordovaFacetConfiguration> {
    private static final NotificationGroup ANDROID_MODULE_IMPORTING_NOTIFICATION =
            NotificationGroup.balloonGroup("Cordova Module Importing");

    public CordovaFrameworkDetector() {
        super("cordova");
    }

    @Override
    public List<? extends DetectedFrameworkDescription> detect(@NotNull Collection<VirtualFile> newFiles,
                                                               @NotNull FrameworkDetectionContext context) {
        Project project = context.getProject();
        if (project != null) {
            return Collections.emptyList();
        }
        return super.detect(newFiles, context);
    }

    @Override
    public void setupFacet(@NotNull final CordovaFacet facet, final ModifiableRootModel model) {
        final Module module = facet.getModule();
        final Project project = module.getProject();

        final VirtualFile[] contentRoots = model.getContentRoots();

        if (contentRoots.length == 1) {
            facet.getConfiguration().init(module, contentRoots[0]);
        }
        //ImportDependenciesUtil.importDependencies(module, true);

        StartupManager.getInstance(project).runWhenProjectIsInitialized(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().saveAll();
            }
        });
    }

    @Override
    public FacetType<CordovaFacet, CordovaFacetConfiguration> getFacetType() {
        return CordovaFacet.getFacetType();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return StdFileTypes.XML;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent().withName(FN_CORDOVA_MANIFEST_XML);
    }
}
