/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.android.tools.idea.configurations.ThemeSelectionPanel)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Removed support for theme categories
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
package org.cordovastudio.dialogs;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Theme selection dialog.
 * <p/>
 * TODO: In the future, make it easy to create new themes here, as well as assigning a theme
 * to an activity.
 */
public class ThemeSelectionPanel implements ListSelectionListener, Disposable {
  private static final String DIALOG_SUFFIX = ".Dialog";
  private static final String DIALOG_PART = ".Dialog.";
  private static final SimpleTextAttributes SEARCH_HIGHLIGHT_ATTRIBUTES =
    new SimpleTextAttributes(null, JBColor.MAGENTA, null, SimpleTextAttributes.STYLE_BOLD);

  @NotNull
  private final RenderConfiguration myConfiguration;
  @NotNull
  private final ThemeSelectionDialog myDialog;
  @NotNull
  private JBList myThemeList;
  @NotNull
  private JPanel myContentPanel;
  @NotNull
  private ThemeFilterComponent myFilter;
  @Nullable
  private List<String> myThemes;
  @Nullable
  private static Deque<String> ourRecent;

  public ThemeSelectionPanel(@NotNull ThemeSelectionDialog dialog, @NotNull RenderConfiguration configuration) {
    myDialog = dialog;
    myConfiguration = configuration;
    String currentTheme = configuration.getTheme();
    touchTheme(currentTheme);

    setInitialSelection(currentTheme);
    myThemeList.addListSelectionListener(this);
    myThemeList.setCellRenderer(new ColoredListCellRenderer() {
      @Override
      protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
        setIcon(CordovaIcons.Toolbar.ChooseTheme);

        String style = (String)value;
        String filter = myFilter.getFilter();

        if (!filter.isEmpty()) {
          int matchIndex = StringUtil.indexOfIgnoreCase(style, filter, index + 1);
          if (matchIndex != -1) {
            if (matchIndex > 0) {
              append(style.substring(0, matchIndex), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
            int matchEnd = matchIndex + filter.length();
            append(style.substring(matchIndex, matchEnd), SEARCH_HIGHLIGHT_ATTRIBUTES);
            if (matchEnd < style.length()) {

              append(style.substring(matchEnd), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
            return;
          }
        }

        int lastDot = style.lastIndexOf('.');
        if (lastDot > 0) {
          append(style.substring(0, lastDot + 1), SimpleTextAttributes.GRAY_ATTRIBUTES);
          append(style.substring(lastDot + 1), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
        else {
          append(style, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
      }
    });
  }

  private void setInitialSelection(@Nullable String currentTheme) {
    if (currentTheme == null) {
      return;
    }

    updateThemeList();
    myThemeList.setSelectedValue(currentTheme, true);
  }

  @NotNull
  public JPanel getContentPanel() {
    return myContentPanel;
  }

  @NotNull
  private List<String> getThemes() {

      if(myThemes == null) {
          List<String> themes = new ArrayList<String>();

          //TODO: iterate trough themes resource folder and add each folder containing a theme to the list

          myThemes = themes;
      }

    return myThemes;
  }

  private void updateThemeList() {
    String selected = (String)myThemeList.getSelectedValue();

    SortedListModel<String> model = new SortedListModel<String>(String.CASE_INSENSITIVE_ORDER);
    String filter = myFilter.getFilter();

    List<String> themes = getThemes();
    for (String theme : themes) {
      if (matchesFilter(theme, filter)) {
        model.add(theme);
      }
    }

    myThemeList.setModel(model);
    if (selected != null) {
      myThemeList.setSelectedValue(selected, true /*shouldScroll*/);
    }
    else if (model.getSize() > 0) {
      myThemeList.setSelectedIndex(0);
    }
  }

  private static boolean matchesFilter(String theme, String filter) {
    int index = theme.lastIndexOf('/');
    return filter.isEmpty() || StringUtil.indexOfIgnoreCase(theme, filter, index + 1) != -1;
  }

  // ---- Implements ListSelectionListener ----
  @Override
  public void valueChanged(ListSelectionEvent listSelectionEvent) {
    myDialog.checkValidation();
  }

  @Nullable
  public String getTheme() {
    String selected = (String)myThemeList.getSelectedValue();
    touchTheme(selected);
    return selected;
  }

  private static void touchTheme(@Nullable String selected) {
    if (selected != null) {
      if (ourRecent == null || !ourRecent.contains(selected)) {
        if (ourRecent == null) {
          ourRecent = new LinkedList<String>();
        }
        ourRecent.addFirst(selected);
      }
    }
  }

  public JComponent getPreferredFocusedComponent() {
    return myThemeList;
  }

  @Override
  public void dispose() {
    myFilter.dispose();
  }

  public void focus() {
    final Project project = myConfiguration.getModule().getProject();
    final IdeFocusManager focusManager = project.isDefault() ? IdeFocusManager.getGlobalInstance() : IdeFocusManager.getInstance(project);
    focusManager.doWhenFocusSettlesDown(new Runnable() {
      @Override
      public void run() {
        focusManager.requestFocus(myThemeList, true);
      }
    });
  }

  private static boolean haveMatches(String filter, List<String> themes) {
    for (String theme : themes) {
      if (matchesFilter(theme, filter)) {
        return true;
      }
    }
    return false;
  }

  private boolean haveAnyMatches(String filter) {
    return haveMatches(filter, getThemes());
  }

  private void createUIComponents() {
    myFilter = new ThemeFilterComponent("CORDOVASTUDIO_THEME_HISTORY", 10, true);
    // Allow arrow up/down to navigate the filtered matches
    myFilter.getTextEditor().addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
          myThemeList.dispatchEvent(e);
          e.consume();
        }
      }
    });
  }

  public class ThemeFilterComponent extends FilterComponent {
    public ThemeFilterComponent(@NonNls String propertyName, int historySize, boolean onTheFlyUpdate) {
      super(propertyName, historySize, onTheFlyUpdate);
    }

    @Override
    public void filter() {
      String filter = getFilter();
      assert filter != null;

      updateThemeList();
    }

    @Override
    protected void onEscape(KeyEvent e) {
      focus();
      e.consume();
    }
  }
}
