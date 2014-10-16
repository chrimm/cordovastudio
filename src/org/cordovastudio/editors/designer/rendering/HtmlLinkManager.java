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
package org.cordovastudio.editors.designer.rendering;

import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassKind;
import com.intellij.codeInsight.intention.impl.CreateClassDialog;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.PsiNavigateUtil;
import org.cordovastudio.editors.designer.utils.ReplaceTagFix;
import org.cordovastudio.utils.SparseArray;
import org.cordovastudio.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.cordovastudio.GlobalConstants.*;

public class HtmlLinkManager {
  private static final String URL_EDIT_CLASSPATH = "action:classpath";
  private static final String URL_SHOW_XML = "action:showXml";
  private static final String URL_ACTION_IGNORE_FRAGMENTS = "action:ignoreFragment";
  private static final String URL_RUNNABLE = "runnable:";
  private static final String URL_COMMAND = "command:";
  private static final String URL_REPLACE_TAGS = "replaceTags:";
  private static final String URL_SHOW_TAG = "showTag:";
  private static final String URL_OPEN = "open:";
  private static final String URL_OPEN_CLASS = "openClass:";
  private static final String URL_ASSIGN_LAYOUT_URL = "assignLayoutUrl:";
  private static final String URL_EDIT_ATTRIBUTE = "editAttribute:";
  private static final String URL_REPLACE_ATTRIBUTE_VALUE = "replaceAttributeValue:";
  private static final String URL_DISABLE_SANDBOX = "disableSandbox:";
  static final String URL_ACTION_CLOSE = "action:close";

  private SparseArray<Runnable> myLinkRunnables;
  private SparseArray<WriteCommandAction> myLinkCommands;
  private int myNextLinkId = 0;

  public HtmlLinkManager() {
  }

  public void handleUrl(@NotNull String url, @Nullable Module module, @Nullable PsiFile file, @Nullable DataContext dataContext,
                        @Nullable RenderResult result) {
    if (url.startsWith("http:") || url.startsWith("https:")) {
      BrowserLauncher.getInstance().browse(url, null, module == null ? null : module.getProject());
    }
    else if (url.startsWith("file:")) {
      assert module != null;
      handleFileUrl(url, module);
    }
    else if (url.startsWith(URL_REPLACE_TAGS)) {
      assert module != null;
      assert file != null;
      handleReplaceTagsUrl(url, module, file);
    }
    else if (url.equals(URL_EDIT_CLASSPATH)) {
      assert module != null;
      handleEditClassPathUrl(url, module);
    }
    else if (url.startsWith(URL_OPEN)) {
      assert module != null;
      handleOpenStackUrl(url, module);
    }
    else if (url.startsWith(URL_OPEN_CLASS)) {
      assert module != null;
      handleOpenClassUrl(url, module);
    }
    else if (url.equals(URL_SHOW_XML)) {
      assert module != null && file != null;
      handleShowXmlUrl(url, module, file);
    }
    else if (url.startsWith(URL_SHOW_TAG)) {
      assert module != null && file != null;
      handleShowTagUrl(url, module, file);
    }
    else if (url.equals(URL_ACTION_IGNORE_FRAGMENTS)) {
      assert result != null;
      handleIgnoreFragments(url, result);
    }
    else if (url.startsWith(URL_EDIT_ATTRIBUTE)) {
      assert result != null;
      if (module != null && file != null) {
        handleEditAttribute(url, module, file);
      }
    }
    else if (url.startsWith(URL_REPLACE_ATTRIBUTE_VALUE)) {
      assert result != null;
      if (module != null && file != null) {
        handleReplaceAttributeValue(url, module, file);
      }
    }
    else if (url.startsWith(URL_RUNNABLE)) {
      Runnable linkRunnable = getLinkRunnable(url);
      if (linkRunnable != null) {
        linkRunnable.run();
      }
    }
    else if (url.startsWith(URL_COMMAND)) {
      WriteCommandAction command = getLinkCommand(url);
      if (command != null) {
        command.execute();
      }
    }
    else {
      assert false : "Unexpected URL: " + url;
    }
  }

  /** Creates a file url for the given file and line position
   *
   * @param file the file
   * @param line the line, or -1 if not known
   * @param column the column, or 0 if not known
   * @return a URL which points to a given position in a file
   */
  @Nullable
  public static String createFilePositionUrl(@NotNull File file, int line, int column) {
    try {
      String fileUrl = StringUtils.fileToUrlString(file);
      if (line != -1) {
        if (column > 0) {
          return fileUrl + ':' + line + ':' + column;
        } else {
          return fileUrl + ':' + line;
        }
      }
      return fileUrl;
    }
    catch (MalformedURLException e) {
      // Ignore
      Logger.getInstance(HtmlLinkManager.class).error(e);
      return null;
    }
  }

  private static void handleFileUrl(@NotNull String url, @NotNull Module module) {
    Project project = module.getProject();
    try {
      // Allow line numbers and column numbers to be tacked on at the end of
      // the file URL:
      //   file:<path>:<line>:<column>
      //   file:<path>:<line>
      int line = -1;
      int column = 0;
      Pattern pattern = Pattern.compile(".*:(\\d+)(:(\\d+))");
      Matcher matcher = pattern.matcher(url);
      if (matcher.matches()) {
        line = Integer.parseInt(matcher.group(1));
        column = Integer.parseInt(matcher.group(3));
        url = url.substring(0, matcher.start(1) - 1);
      } else {
        matcher = Pattern.compile(".*:(\\d+)").matcher(url);
        if (matcher.matches()) {
          line = Integer.parseInt(matcher.group(1));
          url = url.substring(0, matcher.start(1) - 1);
        }
      }
      File ioFile = StringUtils.urlToFile(url);
      VirtualFile file = LocalFileSystem.getInstance().findFileByIoFile(ioFile);
      if (file != null) {
        openEditor(project, file, line, column);
      }
    }
    catch (MalformedURLException e) {
      // Ignore
    }
  }

  @Nullable
  private WriteCommandAction getLinkCommand(String url) {
    if (myLinkCommands != null && url.startsWith(URL_COMMAND)) {
      String idString = url.substring(URL_COMMAND.length());
      int id = Integer.decode(idString);
      return myLinkCommands.get(id);
    }
    return null;
  }

  public String createRunnableLink(@NotNull Runnable runnable) {
    String url = URL_RUNNABLE + myNextLinkId;
    if (myLinkRunnables == null) {
      myLinkRunnables = new SparseArray<Runnable>(5);
    }
    myLinkRunnables.put(myNextLinkId, runnable);
    myNextLinkId++;

    return url;
  }

  @Nullable
  private Runnable getLinkRunnable(String url) {
    if (myLinkRunnables != null && url.startsWith(URL_RUNNABLE)) {
      String idString = url.substring(URL_RUNNABLE.length());
      int id = Integer.decode(idString);
      return myLinkRunnables.get(id);
    }
    return null;
  }

  private static void handleReplaceTagsUrl(@NotNull String url, @NotNull Module module, @NotNull PsiFile file) {
    assert url.startsWith(URL_REPLACE_TAGS) : url;
    int start = URL_REPLACE_TAGS.length();
    int delimiterPos = url.indexOf('/', start);
    if (delimiterPos != -1) {
      String wrongTag = url.substring(start, delimiterPos);
      String rightTag = url.substring(delimiterPos + 1);
      ReplaceTagFix fix = new ReplaceTagFix(module.getProject(), (XmlFile)file, wrongTag, rightTag);
      fix.execute();
    }
  }

  private static void handleEditClassPathUrl(@NotNull String url, @NotNull Module module) {
    assert url.equals(URL_EDIT_CLASSPATH) : url;
    ProjectSettingsService.getInstance(module.getProject()).openModuleSettings(module);
  }

  public String createOpenClassUrl(@NotNull String className) {
    return URL_OPEN_CLASS + className;
  }

  private static void handleOpenClassUrl(@NotNull String url, @NotNull Module module) {
    assert url.startsWith(URL_OPEN_CLASS) : url;
    String className = url.substring(URL_OPEN_CLASS.length());
    Project project = module.getProject();
    PsiClass clz = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
    if (clz != null) {
      PsiFile containingFile = clz.getContainingFile();
      if (containingFile != null) {
        openEditor(project, containingFile, clz.getTextOffset());
      }
    }
  }

  private static void handleShowXmlUrl(@NotNull String url, @NotNull Module module, @NotNull PsiFile file) {
    assert url.equals(URL_SHOW_XML) : url;
    openEditor(module.getProject(), file, 0, -1);
  }

  private static void handleShowTagUrl(@NotNull String url, @NotNull Module module, @NotNull final PsiFile file) {
    assert url.startsWith(URL_SHOW_TAG) : url;
    final String tagName = url.substring(URL_SHOW_TAG.length());

    XmlTag first = ApplicationManager.getApplication().runReadAction(new Computable<XmlTag>() {
      @Override
      @Nullable
      public XmlTag compute() {
        Collection<XmlTag> xmlTags = PsiTreeUtil.findChildrenOfType(file, XmlTag.class);
        for (XmlTag tag : xmlTags) {
          if (tagName.equals(tag.getName())) {
            return tag;
          }
        }

        return null;
      }
    });

    if (first != null) {
      PsiNavigateUtil.navigate(first);
    } else {
      // Fall back to just opening the editor
      openEditor(module.getProject(), file, 0, -1);
    }
  }

  public String createOpenStackUrl(@NotNull String className, @NotNull String methodName, @NotNull String fileName, int lineNumber) {
    return URL_OPEN + className + '#' + methodName + ';' + fileName + ':' + lineNumber;
  }

  private static void handleOpenStackUrl(@NotNull String url, @NotNull Module module) {
    assert url.startsWith(URL_OPEN) : url;
    // Syntax: URL_OPEN + className + '#' + methodName + ';' + fileName + ':' + lineNumber;
    int start = URL_OPEN.length();
    int semi = url.indexOf(';', start);
    String className;
    String fileName;
    int line;
    if (semi != -1) {
      className = url.substring(start, semi);
      int colon = url.indexOf(':', semi + 1);
      if (colon != -1) {
        fileName = url.substring(semi + 1, colon);
        line = Integer.decode(url.substring(colon + 1));
      } else {
        fileName = url.substring(semi + 1);
        line = -1;
      }
      // Attempt to open file
    } else {
      className = url.substring(start);
      fileName = null;
      line = -1;
    }
    String method = null;
    int hash = className.indexOf('#');
    if (hash != -1) {
      method = className.substring(hash + 1);
      className = className.substring(0, hash);
    }

    Project project = module.getProject();
    PsiClass clz = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
    if (clz != null) {
      PsiFile containingFile = clz.getContainingFile();
      if (fileName != null && containingFile != null && line != -1) {
        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile != null) {
          String name = virtualFile.getName();
          if (fileName.equals(name)) {
            // Use the line number rather than the methodName
            openEditor(project, containingFile, line - 1, -1);
            return;
          }
        }
      }

      if (method != null) {
        PsiMethod[] methodsByName = clz.findMethodsByName(method, true);
        for (PsiMethod m : methodsByName) {
          PsiFile psiFile = m.getContainingFile();
          if (psiFile != null) {
            VirtualFile virtualFile = psiFile.getVirtualFile();
            if (virtualFile != null) {
              OpenFileDescriptor descriptor = new OpenFileDescriptor(project, virtualFile, m.getTextOffset());
              FileEditorManager.getInstance(project).openEditor(descriptor, true);
              return;
            }
          }
        }
      }

      if (fileName != null) {
        PsiFile[] files = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
        for (PsiFile psiFile : files) {
          if (openEditor(project, psiFile, line != -1 ? line - 1 : -1, -1)) {
            break;
          }
        }
      }
    }
  }

  private static boolean openEditor(@NotNull Project project, @NotNull PsiFile psiFile, int line, int column) {
    VirtualFile file = psiFile.getVirtualFile();
    if (file != null) {
      return openEditor(project, file, line, column);
    }

    return false;
  }

  private static boolean openEditor(@NotNull Project project, @NotNull VirtualFile file, int line, int column) {
    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, line, column);
    return !FileEditorManager.getInstance(project).openEditor(descriptor, true).isEmpty();
  }

  private static boolean openEditor(@NotNull Project project, @NotNull PsiFile psiFile, int offset) {
    VirtualFile file = psiFile.getVirtualFile();
    if (file != null) {
      return openEditor(project, file, offset);
    }

    return false;
  }

  private static boolean openEditor(@NotNull Project project, @NotNull VirtualFile file, int offset) {
    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, offset);
    return !FileEditorManager.getInstance(project).openEditor(descriptor, true).isEmpty();
  }

  private static void handleIgnoreFragments(@NotNull String url, @NotNull RenderResult result) {
    assert url.equals(URL_ACTION_IGNORE_FRAGMENTS);
    RenderLogger.ignoreFragments();
    RenderService renderService = result.getRenderService();
    if (renderService != null) {
      RenderContext renderContext = renderService.getRenderContext();
      if (renderContext != null) {
        renderContext.requestRender();
      }
    }
  }

  public String createEditAttributeUrl(String attribute, String value) {
    return URL_EDIT_ATTRIBUTE + attribute + '/' + value;
  }

  private static void handleEditAttribute(@NotNull String url, @NotNull Module module, @NotNull final PsiFile file) {
    assert url.startsWith(URL_EDIT_ATTRIBUTE);
    int attributeStart = URL_EDIT_ATTRIBUTE.length();
    int valueStart = url.indexOf('/');
    final String attributeName = url.substring(attributeStart, valueStart);
    final String value = url.substring(valueStart + 1);

    XmlAttribute first = ApplicationManager.getApplication().runReadAction(new Computable<XmlAttribute>() {
      @Override
      @Nullable
      public XmlAttribute compute() {
        Collection<XmlAttribute> attributes = PsiTreeUtil.findChildrenOfType(file, XmlAttribute.class);
        for (XmlAttribute attribute : attributes) {
          if (attributeName.equals(attribute.getLocalName()) && value.equals(attribute.getValue())) {
            return attribute;
          }
        }

        return null;
      }
    });

    if (first != null) {
      PsiNavigateUtil.navigate(first.getValueElement());
    } else {
      // Fall back to just opening the editor
      openEditor(module.getProject(), file, 0, -1);
    }
  }

  private static void handleReplaceAttributeValue(@NotNull String url, @NotNull Module module, @NotNull final PsiFile file) {
    assert url.startsWith(URL_REPLACE_ATTRIBUTE_VALUE);
    int attributeStart = URL_REPLACE_ATTRIBUTE_VALUE.length();
    int valueStart = url.indexOf('/');
    int newValueStart = url.indexOf('/', valueStart + 1);
    final String attributeName = url.substring(attributeStart, valueStart);
    final String oldValue = url.substring(valueStart + 1, newValueStart);
    final String newValue = url.substring(newValueStart + 1);

    WriteCommandAction<Void> action = new WriteCommandAction<Void>(module.getProject(), "Set Attribute Value", file) {
      @Override
      protected void run(@NotNull Result<Void> result) throws Throwable {
        Collection<XmlAttribute> attributes = PsiTreeUtil.findChildrenOfType(file, XmlAttribute.class);
        int oldValueLen = oldValue.length();
        for (XmlAttribute attribute : attributes) {
          if (attributeName.equals(attribute.getLocalName())) {
            String attributeValue = attribute.getValue();
            if (attributeValue == null) {
              continue;
            }
            if (oldValue.equals(attributeValue)) {
              attribute.setValue(newValue);
            } else {
              int index = attributeValue.indexOf(oldValue);
              if (index != -1) {
                if ((index == 0 || attributeValue.charAt(index - 1) == '|') &&
                    (index + oldValueLen == attributeValue.length() || attributeValue.charAt(index + oldValueLen) == '|')) {
                  attributeValue = attributeValue.substring(0, index) + newValue + attributeValue.substring(index + oldValueLen);
                  attribute.setValue(attributeValue);
                }
              }
            }
          }
        }
      }
    };
    action.execute();
  }

  public String createDisableSandboxUrl() {
    return URL_DISABLE_SANDBOX;
  }
}
