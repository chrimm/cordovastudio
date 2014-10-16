/*
 * Copyright (C) 2013 The Android Open Source Project
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

package org.cordovastudio.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.cordovastudio.GlobalConstants.*;

public class CordovaPsiUtils {
  /**
   * Looks up the {@link com.intellij.psi.PsiFile} for a given {@link com.intellij.openapi.vfs.VirtualFile} in a given {@link com.intellij.openapi.project.Project}, in
   * a safe way (meaning it will acquire a read lock first, and will check that the file is valid
   *
   * @param project the project
   * @param file the file
   * @return the corresponding {@link com.intellij.psi.PsiFile}, or null if not found or valid
   */
  @Nullable
  public static PsiFile getPsiFileSafely(@NotNull final Project project, @NotNull final VirtualFile file) {
    return ApplicationManager.getApplication().runReadAction(new Computable<PsiFile>() {
      @Nullable
      @Override
      public PsiFile compute() {
        return file.isValid() ? PsiManager.getInstance(project).findFile(file) : null;
      }
    });
  }

  /**
   * Looks up the {@link com.intellij.openapi.module.Module} for a given {@link com.intellij.psi.PsiElement}, in a safe way (meaning it will
   * acquire a read lock first.
   *
   * @param element the element
   * @return the module containing the element, or null if not found
   */
  @Nullable
  public static Module getModuleSafely(@NotNull final PsiElement element) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Module>() {
      @Nullable
      @Override
      public Module compute() {
        return ModuleUtilCore.findModuleForPsiElement(element);
      }
    });
  }

  /**
   * Looks up the {@link com.intellij.openapi.module.Module} containing a given {@link com.intellij.openapi.vfs.VirtualFile} in a given {@link com.intellij.openapi.project.Project}, in
   * a safe way (meaning it will acquire a read lock first
   *
   * @param project the project
   * @param file the file
   * @return the corresponding {@link com.intellij.openapi.module.Module}, or null if not found
   */
  @Nullable
  public static Module getModuleSafely(@NotNull final Project project, @NotNull final VirtualFile file) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Module>() {
      @Nullable
      @Override
      public Module compute() {
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        return psiFile == null ? null : ModuleUtilCore.findModuleForPsiElement(psiFile);
      }
    });
  }

  /**
   * Returns the root tag for the given {@link com.intellij.psi.xml.XmlFile}, if any, acquiring the read
   * lock to do so if necessary
   *
   * @param file the file to look up the root tag for
   * @return the corresponding root tag, if any
   */
  @Nullable
  public static XmlTag getRootTagSafely(@NotNull final XmlFile file) {
    if (ApplicationManager.getApplication().isReadAccessAllowed()) {
      return file.getRootTag();
    }
    return ApplicationManager.getApplication().runReadAction(new Computable<XmlTag>() {
      @Nullable
      @Override
      public XmlTag compute() {
        return file.getRootTag();
      }
    });
  }

  /**
   * Get the value of an attribute in the {@link com.intellij.psi.xml.XmlFile} safely (meaning it will acquire the read lock first).
   */
  @Nullable
  public static String getRootTagAttributeSafely(@NotNull final XmlFile file,
                                                 @NotNull final String attribute,
                                                 @Nullable final String namespace) {
    Application application = ApplicationManager.getApplication();
    if (!application.isReadAccessAllowed()) {
      return application.runReadAction(new Computable<String>() {
        @Nullable
        @Override
        public String compute() {
          return getRootTagAttributeSafely(file, attribute, namespace);
        }
      });
    } else {
      XmlTag tag = file.getRootTag();
      if (tag != null) {
        XmlAttribute attr = namespace != null ? tag.getAttribute(attribute, namespace) : tag.getAttribute(attribute);
        if (attr != null) {
          return attr.getValue();
        }
      }
      return null;
    }
  }
}
