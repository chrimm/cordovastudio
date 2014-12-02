/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.model.ViewsMetaManager)
 *
 * Copyright (C) Christoffer T. Timm
 * Changes:
 *  – Added support for multiple model definition files, now every file in "metaModelDefinitions/" will be loaded,
 *    thus you can specify any UI Framework you like, just by adding a corresponding model definition xml file to this folder.
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

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.cordovastudio.editors.designer.palette.CordovaVariationPaletteItem;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.cordovastudio.editors.designer.palette.VariationPaletteItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Lobas
 */
public class ViewsMetaManager extends MetaManager {
  public ViewsMetaManager(Project project) {
      super(project);

      File definitionFileFolder = new File(getClass().getResource("metaModelDefinitions/").getFile());

      if(definitionFileFolder.exists() && definitionFileFolder.isDirectory()) {

          File[] definitionFiles = definitionFileFolder.listFiles(new FilenameFilter() {
              @Override
              public boolean accept(File dir, String name) {
                  return name.endsWith(".xml");
              }
          });

          if(definitionFiles != null && definitionFiles.length > 0) {
              load(definitionFiles);
              //for(File file : definitionFiles) {
              //    load(file);
              //}
          }
      }
  }

    private void load(File[] definitionFiles) {
        /*
         * First, we convert all given files which are meta model defintions to documents,
         * than we need to extract all global properties from all existing definition files,
         * after that we can begin to load the models.
         */

        try {
            List<Document> documents = new ArrayList<>();

            /* Fetch documents */
            for(File definitionFile : definitionFiles) {
                Document document = new SAXBuilder().build(definitionFile);

                Element rootElement = document.getRootElement();

                if ("meta-model".equalsIgnoreCase(rootElement.getName())) {
                    documents.add(document);
                }
            }

            /* Load global properties */
            for(Document document : documents) {
                Element globalPropertiesElement = document.getRootElement().getChild(GLOBAL_PROPERTIES);

                if(globalPropertiesElement != null) {
                    loadGlobalProperties(globalPropertiesElement);
                }
            }

            /* Load meta models */
            for(Document document : documents) {
                loadDocument(document.getRootElement());
            }

        } catch (Throwable e) {
            LOG.error(e);
        }
    }

  public static ViewsMetaManager getInstance(Project project) {
    return ServiceManager.getService(project, ViewsMetaManager.class);
  }

  @NotNull
  @Override
  protected CordovaMetaModel createModel(Class<RadComponent> model, String tag, String htmlClass, String htmlType) throws Exception {
    return new CordovaMetaModel(model, tag, htmlClass, htmlType);
  }

  @NotNull
  @Override
  protected MetaModel loadModel(ClassLoader classLoader, Element element, Map<MetaModel, List<String>> modelToMorphing) throws Exception {
      CordovaMetaModel meta = (CordovaMetaModel)super.loadModel(classLoader, element, modelToMorphing);
      meta.initializeFrom(element);
      return meta;
  }

  @NotNull
  @Override
  protected VariationPaletteItem createVariationPaletteItem(PaletteItem paletteItem, MetaModel model, Element itemElement) {
    return new CordovaVariationPaletteItem(paletteItem, model, itemElement);
  }
}