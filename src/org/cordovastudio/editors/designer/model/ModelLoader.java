/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * (Original as of com.intellij.designer.model.ModelLoader)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Changed load() to directly use File instead of String containing a filename and openening file as stream relative to Class resource
 *  – Added check, whether root element of model definition file is "meta-model"
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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;

/**
 * @author Alexander Lobas
 */
public abstract class ModelLoader {
    protected static final Logger LOG = Logger.getInstance("#com.intellij.designer.model.ModelLoader");

    protected final Project myProject;

    protected ModelLoader(Project project) {
        myProject = project;
    }

    protected final void load(File modelDefinitionFile) {
        try {
            Document document = new SAXBuilder().build(modelDefinitionFile);

            Element rootElement = document.getRootElement();

            if ("meta-model".equalsIgnoreCase(rootElement.getName())) {
                loadDocument(document.getRootElement());
            }
        } catch (Throwable e) {
            LOG.error(e);
        }
    }

    protected abstract void loadDocument(Element rootElement) throws Exception;

    public final Project getProject() {
        return myProject;
    }
}