/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.editors.navigation.macros.Analyser)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Complete rewrite for analyzing HTML file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.storyboard.macros;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.storyboard.model.*;
import org.cordovastudio.utils.CordovaPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.cordovastudio.GlobalConstants.ANCHOR_TAG;

public class Analyser {
    private static final Logger LOG = Logger.getInstance(Analyser.class);

    private static final String[] INDEX_FILES = {"index.html", "index.htm", "default.html", "default.htm"};

    @NotNull
    private final Project myProject;

    @NotNull
    private final Module myModule;

    public Analyser(@NotNull Project project, @NotNull Module module) {
        myProject = project;
        myModule = module;
    }

    /**
     * Creates and returns a new {@link CordovaStoryboardModel}.
     *
     * @return A new {@link CordovaStoryboardModel}.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Nullable
    public CordovaStoryboardModel createModel() {
        CordovaStoryboardModel model = new CordovaStoryboardModel();

        loadModel(model);

        return model;
    }

    public void updateModel(CordovaStoryboardModel model) {
        model.clear();
        loadModel(model);
    }

    private void loadModel(CordovaStoryboardModel model) {
        VirtualFile rootDir = myProject.getBaseDir().findChild("www");

        if (rootDir != null && rootDir.isDirectory()) {
            VirtualFile indexFile = findIndexFile(rootDir);

            if (indexFile == null)
                return;

            State indexState = new ViewState(indexFile);
            Locator indexStateLocator = Locator.of(indexState, StringUtil.capitalize(indexFile.getNameWithoutExtension()));

            model.addState(indexState);

            createTransitions(indexStateLocator, rootDir, model);
        }
    }

    /**
     * Creates all transitions recursively starting from the source locator.
     *
     * @param source The locator to start from
     * @param rootDir The root directory to which all file links are relative to
     * @param model The model to appen the transitions to
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private void createTransitions(Locator source, VirtualFile rootDir, CordovaStoryboardModel model) {
        List<String> outgoingLinks = findOutgoingLinks(((ViewState)source.getState()).getFile(), true /* localOnly */);

        for(String link : outgoingLinks) {
            VirtualFile targetFileOrDirectory = rootDir.findFileByRelativePath(link);
            VirtualFile targetFile;

            if(targetFileOrDirectory != null && targetFileOrDirectory.exists()) {
                targetFile = (targetFileOrDirectory.isDirectory()) ? findIndexFile(targetFileOrDirectory) : targetFileOrDirectory;

                if(targetFile != null && targetFile.exists() &&
                        /* Omit transitions, which source and destination states would be the same (self-referenced loops) */
                        !targetFile.getPath().equals(((ViewState) source.getState()).getFile().getPath())) {
                    Locator destination = Locator.of(new ViewState(targetFile), StringUtil.capitalize(targetFile.getNameWithoutExtension()));

                    /* Omit already existing transitions to avoid looping */
                    if(!model.hasTransition(source.getState(), destination.getState())) {
                        model.add(new Transition("click", source, destination));
                        createTransitions(destination, rootDir, model);
                    }
                }
            }
        }
    }

    /**
     * Finds the index file ("index.html", etc. as specified in field INDEX_FILES) in the specified directory.
     * @param directory
     * @return The index file, if there is one.
     */
    @Nullable
    private VirtualFile findIndexFile(VirtualFile directory) {
        VirtualFile indexFile = null;

        for (String f : INDEX_FILES) {
            indexFile = directory.findChild(f);
            if (indexFile != null)
                break;
        }

        return indexFile;
    }

    /**
     * Retrieves all outgoing links.
     *
     * @param file The file to retrieve links from
     * @return All outgoing links from the specified file
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @NotNull
    private List<String> findOutgoingLinks(@NotNull VirtualFile file, boolean localOnly) {
        ArrayList<String> outgoingLinks = new ArrayList<>();

        if (!file.exists()) {
            LOG.warn("Could not open file " + file.getName() + " for Storyboard analysis.");
            return outgoingLinks;
        }

        XmlFile xmlFile = (XmlFile) CordovaPsiUtils.getPsiFileSafely(myProject, file);

        if (xmlFile == null) {
            LOG.warn("Could not parse file " + file.getName() + " for Storyboard.");
            return outgoingLinks;
        }

        XmlTag rootTag = CordovaPsiUtils.getRootTagSafely(xmlFile);

        if (rootTag == null) {
            LOG.warn("File " + file.getName() + " does not contain a valid root tag.");
            return outgoingLinks;
        }

        List<XmlTag> anchorTags = findTagsRecursive(ANCHOR_TAG, rootTag);

        for(XmlTag anchorTag : anchorTags) {
            String href = anchorTag.getAttributeValue("href");

            if(href != null) {
                if((localOnly && href.contains("://")) || href.startsWith("#"))
                    continue;

                outgoingLinks.add(href);
            }
        }

        return outgoingLinks;
    }

    /**
     * Retrieves all specified tags recursively in the given root tag and returns it a a flat list.
     *
     * @param tagName The tag name to find
     * @param rootTag The tag to look inside
     * @return A flat list of found tags.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    private List<XmlTag> findTagsRecursive(@NotNull String tagName, @NotNull XmlTag rootTag) {

        ArrayList<XmlTag> foundTags = new ArrayList<>();

        for (XmlTag subTag : rootTag.getSubTags()) {
            if (tagName.equalsIgnoreCase(subTag.getName())) {
                foundTags.add(subTag);
            } else {
                foundTags.addAll(findTagsRecursive(tagName, subTag));
            }
        }

        return foundTags;
    }
}

