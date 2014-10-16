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

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.compiler.impl.javaCompiler.javac.JavacConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JavaSdkVersion;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ClasspathEditor;
import com.intellij.openapi.roots.ui.configuration.ModulesConfigurator;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.problems.WolfTheProblemSolver;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.containers.HashSet;
import com.intellij.util.ui.UIUtil;
import org.cordovastudio.utils.HtmlBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.compiler.JpsJavaCompilerOptions;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.cordovastudio.GlobalConstants.*;
import static org.cordovastudio.editors.designer.rendering.HtmlLinkManager.*;
import static org.cordovastudio.editors.designer.rendering.RenderLogger.TAG_RESOURCES_PREFIX;
import static org.cordovastudio.editors.designer.rendering.RenderLogger.TAG_RESOURCES_RESOLVE_THEME_ATTR;
import static com.intellij.openapi.util.SystemInfo.JAVA_VERSION;

/**
 * Panel which can show render errors, along with embedded hyperlinks to perform actions such as
 * showing relevant source errors, or adding missing attributes, etc.
 * <p>
 * Partially based on {@link com.intellij.codeInspection.ui.Browser}, the inspections result HTML pane, modified
 * to show render errors instead
 */
public class RenderErrorPanel extends JPanel {
  public static final boolean SIZE_ERROR_PANEL_DYNAMICALLY = true;
  private static final int ERROR_PANEL_OPACITY = UIUtil.isUnderDarcula() ? 224 : 208; // out of 255
  /** Class of the render session implementation class; for render errors, we cut off stack dumps at this frame */
  private static final String RENDER_SESSION_IMPL_FQCN = "com.android.layoutlib.bridge.impl.RenderSessionImpl";

  private JEditorPane myHTMLViewer;
  private final HyperlinkListener myHyperLinkListener;
  private RenderResult myResult;
  private HtmlLinkManager myLinkManager;
  private final JScrollPane myScrollPane;

  public void dispose(){
    removeAll();
    if (myHTMLViewer != null) {
      myHTMLViewer.removeHyperlinkListener(myHyperLinkListener);
      myHTMLViewer = null;
    }
  }

  @Nullable
  public String showErrors(@NotNull final RenderResult result) {
    RenderLogger logger = result.getLogger();
    if (!logger.hasProblems()) {
      showEmpty();
      myResult = null;
      myLinkManager = null;
      return null;
    }
    myResult = result;
    myLinkManager = result.getLogger().getLinkManager();

    try {
      // Generate HTML under a read lock, since many errors require peeking into the PSI
      // to for example find class names to suggest as typo replacements
      String html = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
        @Override
        public String compute() {
          return generateHtml(result);
        }
      });
      myHTMLViewer.read(new StringReader(html), null);
      setupStyle();
      myHTMLViewer.setCaretPosition(0);
      return html;
    }
    catch (Exception e) {
      showEmpty();
      return null;
    }
  }

  public RenderErrorPanel() {
    super(new BorderLayout());
    setOpaque(false);
    myHTMLViewer = new JEditorPane(UIUtil.HTML_MIME, "<HTML><BODY>Render Problems</BODY></HTML>");
    myHTMLViewer.setEditable(false);
    myHyperLinkListener = new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          JEditorPane pane = (JEditorPane)e.getSource();
          if (e instanceof HTMLFrameHyperlinkEvent) {
            HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent)e;
            HTMLDocument doc = (HTMLDocument)pane.getDocument();
            doc.processHTMLFrameHyperlinkEvent(evt);
            return;
          }

          String url = e.getDescription();
          if (url.equals(URL_ACTION_CLOSE)) {
            close();
            return;
          }
          Module module = myResult.getModule();
          PsiFile file = myResult.getFile();
          DataContext dataContext = DataManager.getInstance().getDataContext(RenderErrorPanel.this);
          assert dataContext != null;

          myLinkManager.handleUrl(url, module, file, dataContext, myResult);
        }
      }
    };
    myHTMLViewer.addHyperlinkListener(myHyperLinkListener);
    myHTMLViewer.setMargin(new Insets(3, 3, 3, 3));

    myScrollPane = ScrollPaneFactory.createScrollPane(myHTMLViewer);
    setupStyle();

    add(myScrollPane, BorderLayout.CENTER);
  }

  private void close() {
    this.setVisible(false);
  }

  private void setupStyle() {
    // Make the scrollPane transparent
    if (myScrollPane != null) {
      JViewport viewPort = myScrollPane.getViewport();
      viewPort.setOpaque(false);
      viewPort.setBackground(null);
      myScrollPane.setOpaque(false);
      myScrollPane.setBackground(null);
    }

    Document document = myHTMLViewer.getDocument();
    if (!(document instanceof StyledDocument)) {
      return;
    }

    StyledDocument styledDocument = (StyledDocument)document;

    EditorColorsManager colorsManager = EditorColorsManager.getInstance();
    EditorColorsScheme scheme = colorsManager.getGlobalScheme();

    Style style = styledDocument.addStyle("active", null);
    StyleConstants.setFontFamily(style, scheme.getEditorFontName());
    StyleConstants.setFontSize(style, scheme.getEditorFontSize());
    styledDocument.setCharacterAttributes(0, document.getLength(), style, false);

    // Make background semitransparent
    Color background = myHTMLViewer.getBackground();
    if (background != null) {
      background = new Color(background.getRed(), background.getGreen(), background.getBlue(), ERROR_PANEL_OPACITY);
      myHTMLViewer.setBackground(background);
    }
  }

  public int getPreferredHeight(@SuppressWarnings("UnusedParameters") int width) {
    return myHTMLViewer.getPreferredSize().height;
  }

  private String generateHtml(@NotNull RenderResult result) {
    RenderLogger logger = result.getLogger();
    RenderService renderService = result.getRenderService();
    assert logger.hasProblems();

    HtmlBuilder builder = new HtmlBuilder(new StringBuilder(300));
    builder.openHtmlBody();

    // Construct close button. Sadly <img align="right"> doesn't work in JEditorPanes; would
    // have looked a lot nicer with the image flushed to the right!
    builder.addHtml("<A HREF=\"");
    builder.addHtml(URL_ACTION_CLOSE);
    builder.addHtml("\">");
    builder.addIcon(HtmlBuilderHelper.getCloseIconPath());
    builder.addHtml("</A>");
    builder.addHeading("Rendering Problems", HtmlBuilderHelper.getHeaderFontColor()).newline();

    reportMissingStyles(logger, builder);
    if (renderService != null) {
      reportRelevantCompilationErrors(logger, builder, renderService);
    }
    reportOtherProblems(logger, builder);
    if (renderService != null) {
      reportRenderingFidelityProblems(logger, builder, renderService);
    }

    builder.closeHtmlBody();

    return builder.getHtml();
  }


  @NotNull
  private static Collection<String> getAllViews(@NotNull Module module) {
    Set<String> names = new java.util.HashSet<String>();

      //TODO: implement

    return names;
  }

  @NotNull
  private static Collection<PsiClass> findInheritors(@NotNull Module module, @NotNull String name) {
    Project project = module.getProject();
    PsiClass base = JavaPsiFacade.getInstance(project).findClass(name, GlobalSearchScope.allScope(project));
    if (base != null) {
      GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, false);
      return ClassInheritorsSearch.search(base, scope, true).findAll();
    }
    return Collections.emptyList();
  }

  private void reportSandboxError(@NotNull HtmlBuilder builder, Throwable throwable, boolean newlineBefore, boolean newlineAfter) {
    if (throwable instanceof SecurityException) {
      if (newlineBefore) {
        builder.newline();
      }
      builder.addLink("Turn off custom view rendering sandbox", myLinkManager.createDisableSandboxUrl());

      if (newlineAfter) {
        builder.newline().newline();
      }
    }
  }

  private void reportRenderingFidelityProblems(@NotNull RenderLogger logger, @NotNull HtmlBuilder builder,
                                               @NotNull final RenderService renderService) {
    List<RenderProblem> fidelityWarnings = logger.getFidelityWarnings();
    if (fidelityWarnings != null && !fidelityWarnings.isEmpty()) {
      builder.add("The graphics preview in the layout editor may not be accurate:").newline();
      builder.beginList();
      int count = 0;
      for (final RenderProblem warning : fidelityWarnings) {
        builder.listItem();
        warning.appendHtml(builder.getStringBuilder());
        final Object clientData = warning.getClientData();
        if (clientData != null) {
          builder.addLink(" (Ignore for this session)", myLinkManager.createRunnableLink(new Runnable() {
            @Override
            public void run() {
              RenderLogger.ignoreFidelityWarning(clientData);
              RenderContext renderContext = renderService.getRenderContext();
              if (renderContext != null) {
                renderContext.requestRender();
              }
            }
          }));
        }
        builder.newline();
        count++;
        // Only display the first 3 render fidelity issues
        if (count == 3) {
          @SuppressWarnings("ConstantConditions")
          int remaining = fidelityWarnings.size() - count;
          if (remaining > 0) {
            builder.add("(").addHtml(Integer.toString(remaining)).add(" additional render fidelity issues hidden)");
            break;
          }
        }
      }
      builder.endList();
      builder.addLink("Ignore all fidelity warnings for this session", myLinkManager.createRunnableLink(new Runnable() {
        @Override
        public void run() {
          RenderLogger.ignoreAllFidelityWarnings();
          RenderContext renderContext = renderService.getRenderContext();
          if (renderContext != null) {
            renderContext.requestRender();
          }
        }
      }));
      builder.newline();
    }
  }

  private static void reportMissingStyles(RenderLogger logger, HtmlBuilder builder) {
    if (logger.seenTagPrefix(TAG_RESOURCES_RESOLVE_THEME_ATTR)) {
      builder.addBold("Missing styles. Is the correct theme chosen for this layout?").newline();
      builder.addIcon(HtmlBuilderHelper.getTipIconPath());
      builder.add("Use the Theme combo box above the layout to choose a different layout, or fix the theme style references.");
      builder.newline().newline();
    }
  }

  private static void reportRelevantCompilationErrors(RenderLogger logger, HtmlBuilder builder, RenderService renderService) {
    Module module = logger.getModule();
    Project project = module.getProject();
    WolfTheProblemSolver wolfgang = WolfTheProblemSolver.getInstance(project);
    if (wolfgang.hasProblemFilesBeneath(module)) {
      if (logger.seenTagPrefix(TAG_RESOURCES_PREFIX)) {
        // Do we have errors in the res/ files?
        // See if it looks like we have aapt problems
        boolean haveResourceErrors = wolfgang.hasProblemFilesBeneath(new Condition<VirtualFile>() {
          @Override
          public boolean value(VirtualFile virtualFile) {
            return virtualFile.getFileType() == StdFileTypes.XML;
          }
        });
        if (haveResourceErrors) {
          builder.addBold("NOTE: This project contains resource errors, so aapt did not succeed, " +
                          "which can cause rendering failures. Fix resource problems first.");
          builder.newline().newline();
        }
      }
    }
  }

  private void reportOtherProblems(RenderLogger logger, HtmlBuilder builder) {
    List<RenderProblem> messages = logger.getMessages();
    if (messages != null && !messages.isEmpty()) {
      Set<String> seenTags = Sets.newHashSet();
      for (RenderProblem message : messages) {
        String tag = message.getTag();
        if (tag != null && seenTags.contains(tag)) {
          continue;
        }
        seenTags.add(tag);

        HighlightSeverity severity = message.getSeverity();
        if (severity == HighlightSeverity.ERROR) {
          builder.addIcon(HtmlBuilderHelper.getErrorIconPath());
        } else if (severity == HighlightSeverity.WARNING) {
          builder.addIcon(HtmlBuilderHelper.getWarningIconPath());
        }

        String html = message.getHtml();
        builder.getStringBuilder().append(html);
        builder.newlineIfNecessary();

        Throwable throwable = message.getThrowable();
        if (throwable != null) {
          reportSandboxError(builder, throwable, false, true);
          reportThrowable(builder, throwable, !html.isEmpty());
        }

        if (tag != null) {
          int count = logger.getTagCount(tag);
          if (count > 1) {
            builder.add(" (").addHtml(Integer.toString(count)).add(" similar errors not shown)");
          }
        }

        builder.newline();
      }
    }
  }

  /** Display the problem list encountered during a render */
  private void reportThrowable(@NotNull HtmlBuilder builder, @NotNull final Throwable throwable, boolean hideIfIrrelevant) {
    StackTraceElement[] frames = throwable.getStackTrace();
    int end = -1;
    boolean haveInterestingFrame = false;
    for (int i = 0; i < frames.length; i++) {
      StackTraceElement frame = frames[i];
      if (isInterestingFrame(frame)) {
        haveInterestingFrame = true;
      }
      String className = frame.getClassName();
      if (className.equals(RENDER_SESSION_IMPL_FQCN)) {
        end = i;
        break;
      }
    }

    if (end == -1 || !haveInterestingFrame) {
      // Not a recognized stack trace range: just skip it
      if (hideIfIrrelevant) {
        return;
      } else {
        // List just the top frames
        for (int i = 0; i < frames.length; i++) {
          StackTraceElement frame = frames[i];
          if (!isVisible(frame)) {
            end = i;
            if (end == 0) {
              // Find end instead
              for (int j = 0; j < frames.length; j++) {
                frame = frames[j];
                String className = frame.getClassName();
                if (className.equals(RENDER_SESSION_IMPL_FQCN)) {
                  end = j;
                  break;
                }
              }
            }
            break;
          }
        }
      }
    }

    builder.add(throwable.toString()).newline();

    boolean wasHidden = false;
    int indent = 2;
    File platformSource = null;
    boolean platformSourceExists = true;
    for (int i = 0; i < end; i++) {
      StackTraceElement frame = frames[i];
      if (isHiddenFrame(frame)) {
        wasHidden = true;
        continue;
      }

      String className = frame.getClassName();
      String methodName = frame.getMethodName();
      builder.addNbsps(indent);
      builder.add("at ").add(className).add(".").add(methodName);
      String fileName = frame.getFileName();
      if (fileName != null && !fileName.isEmpty()) {
        int lineNumber = frame.getLineNumber();
        String location = fileName + ':' + lineNumber;
        if (isInterestingFrame(frame)) {
          if (wasHidden) {
            builder.addNbsps(indent);
            builder.add("    ...").newline();
            wasHidden = false;
          }
          String url = myLinkManager.createOpenStackUrl(className, methodName, fileName, lineNumber);
          builder.add("(").addLink(location, url).add(")");
        }
        builder.newline();
      }
    }

    builder.addLink("Copy stack to clipboard", myLinkManager.createRunnableLink(new Runnable() {
      @Override
      public void run() {
        String text = Throwables.getStackTraceAsString(throwable);
        try {
          CopyPasteManager.getInstance().setContents(new StringSelection(text));
        }
        catch (Exception ignore) {
        }
      }
    }));
  }

  private static boolean isHiddenFrame(StackTraceElement frame) {
    String className = frame.getClassName();
    return
      className.startsWith("sun.reflect.") ||
      className.equals("android.view.BridgeInflater") ||
      className.startsWith("com.android.tools.") ||
      className.startsWith("org.jetbrains.");
  }

  private static boolean isInterestingFrame(StackTraceElement frame) {
    String className = frame.getClassName();
    return !(className.startsWith("android.")          //$NON-NLS-1$
             || className.startsWith("org.jetbrains.") //$NON-NLS-1$
             || className.startsWith("com.android.")   //$NON-NLS-1$
             || className.startsWith("java.")          //$NON-NLS-1$
             || className.startsWith("javax.")         //$NON-NLS-1$
             || className.startsWith("sun."));         //$NON-NLS-1$
  }

  private static boolean isFramework(StackTraceElement frame) {
    String className = frame.getClassName();
    return (className.startsWith("android.")          //$NON-NLS-1$
             || className.startsWith("java.")          //$NON-NLS-1$
             || className.startsWith("javax.")         //$NON-NLS-1$
             || className.startsWith("sun."));         //$NON-NLS-1$
  }

  private static boolean isVisible(StackTraceElement frame) {
    String className = frame.getClassName();
    return !(isFramework(frame) || className.startsWith("sun.")); //$NON-NLS-1$
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  private void showEmpty() {
    try {
      myHTMLViewer.read(new StringReader("<html><body></body></html>"), null);
    }
    catch (IOException e) {
      // can't be
    }
  }

  private static void askAndRebuild(Project project) {
    final int r = Messages.showYesNoDialog(project, "You have to rebuild project to see the fixed preview. Would you like to do it?",
            "Rebuild Project", Messages.getQuestionIcon());
    if (r == Messages.YES) {
      CompilerManager.getInstance(project).rebuild(null);
    }
  }

  @NotNull
  private static Set<String> getSdkNamesFromModules(@NotNull Collection<Module> modules) {
    final Set<String> result = new HashSet<String>();
    for (Module module : modules) {
      final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();

      if (sdk != null) {
        result.add(sdk.getName());
      }
    }
    return result;
  }

  @NotNull
  private static List<Module> getProblemModules(@NotNull Module root) {
    final List<Module> result = new ArrayList<Module>();
    collectProblemModules(root, new HashSet<Module>(), result);
    return result;
  }

  private static void collectProblemModules(@NotNull Module module, @NotNull Set<Module> visited, @NotNull Collection<Module> result) {
    if (!visited.add(module)) {
      return;
    }
    for (Module depModule : ModuleRootManager.getInstance(module).getDependencies(false)) {
      collectProblemModules(depModule, visited, result);
    }
  }

  private static class RebuildWith16Fix implements Runnable {
    private final Project myProject;

    private RebuildWith16Fix(Project project) {
      myProject = project;
    }

    @Override
    public void run() {
      final JpsJavaCompilerOptions settings = JavacConfiguration.getOptions(myProject, JavacConfiguration.class);
      if (settings.ADDITIONAL_OPTIONS_STRING.length() > 0) {
        settings.ADDITIONAL_OPTIONS_STRING += ' ';
      }
      settings.ADDITIONAL_OPTIONS_STRING += "-target 1.6";
      CompilerManager.getInstance(myProject).rebuild(null);
    }
  }

  private static class HtmlBuilderHelper {
    @Nullable
    private static String getIconPath(String relative) {
      // TODO: Find a way to do this more efficiently; not referencing assets but the corresponding
      // AllIcons constants, and loading them into HTML class loader contexts?
      URL resource = AllIcons.class.getClassLoader().getResource(relative);
      try {
        return (resource != null) ? resource.toURI().toURL().toExternalForm() : null;
      }
      catch (MalformedURLException e) {
        return null;
      }
      catch (URISyntaxException e) {
        return null;
      }
    }

    @Nullable
    public static String getCloseIconPath() {
      return getIconPath("/actions/closeNew.png");
    }

    @Nullable
    public static String getTipIconPath() {
      return getIconPath("/actions/createFromUsage.png");
    }

    @Nullable
    public static String getWarningIconPath() {
      return getIconPath("/actions/warning.png");
    }

    @Nullable
    public static String getErrorIconPath() {
      return getIconPath("/actions/error.png");
    }

    public static String getHeaderFontColor() {
      // See om.intellij.codeInspection.HtmlComposer.appendHeading
      // (which operates on StringBuffers)
      return UIUtil.isUnderDarcula() ? "#A5C25C" : "#005555";
    }
  }
}

