/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.android.designer.componentTree.AndroidTreeDecorator)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Added support for deprecated components in tree (i.e. striked out decoration)
 *  – Added support for hidden components in tree
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
package org.cordovastudio.editors.designer.componentTree;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.daemon.impl.SeverityRegistrar;
import com.intellij.designer.model.ErrorInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.LayeredIcon;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import org.cordovastudio.branding.CordovaIcons;
import org.cordovastudio.editors.designer.model.IComponentDecorator;
import org.cordovastudio.editors.designer.model.MetaModel;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static org.cordovastudio.GlobalConstants.ATTR_ID;

/**
 * Tree decorator for the component tree.
 * <p>
 * It displays the id (if any) in bold, the tag name (unless implied by the id),
 * and the key attributes of the view in gray. It also inlines {@code <view>} tags.
 * Finally, it uses the palette icons, and overlays warning and error icons
 * if the corresponding tag has lint warnings.
 */
public final class CordovaTreeDecorator implements TreeComponentDecorator {
    @Nullable
    private final Project myProject;

    public CordovaTreeDecorator(@Nullable Project project) {
        myProject = project;
    }

    @Override
    public void decorate(RadComponent component, SimpleColoredComponent renderer, AttributeWrapper wrapper, boolean full) {
        MetaModel metaModel = component.getMetaModel();

        decorate(component, metaModel, renderer, wrapper, full);
    }

    private void decorate(RadComponent component,
                          MetaModel metaModel,
                          SimpleColoredComponent renderer,
                          AttributeWrapper wrapper,
                          boolean full) {
        String id = component.getPropertyValue(ATTR_ID);
        id = StringUtil.nullize(id);

        PaletteItem item = metaModel.getPaletteItem();
        String type = null;
        String tagName = metaModel.getTag();
        if (item != null) {
            type = item.getTitle();
        }

        if (id != null) {
            SimpleTextAttributes idStyle;
            if (metaModel.isDeprecated()) {
                idStyle = wrapper.getAttribute(SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            } else {
                idStyle = wrapper.getAttribute(new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD | SimpleTextAttributes.STYLE_STRIKEOUT, null));
            }
            renderer.append(id, idStyle);
        }

        if (id == null && type == null) {
            type = tagName;
        }

        // Don't display the type if it's obvious from the id (e.g.
        // if the id is button1, don't display (Button) as the type)
        if (type != null && (id == null || !StringUtil.startsWithIgnoreCase(id, type))) {
            SimpleTextAttributes typeStyle;
            if (metaModel.isDeprecated()) {
                typeStyle = wrapper.getAttribute(new SimpleTextAttributes(SimpleTextAttributes.STYLE_STRIKEOUT, null));
            } else {
                typeStyle = wrapper.getAttribute(SimpleTextAttributes.REGULAR_ATTRIBUTES);

            }
            renderer.append(id != null ? String.format(" (%1$s)", type) : type, typeStyle);
        }

        // Display typical arguments
        StringBuilder fullTitle = new StringBuilder();
        String title = metaModel.getTitle();
        if (title != null) {
            int start = title.indexOf('%');
            if (start != -1) {
                int end = title.indexOf('%', start + 1);
                if (end != -1) {
                    String variable = title.substring(start + 1, end);

                    String value = component.getPropertyValue(variable);
                    if (!StringUtil.isEmpty(value)) {
                        value = StringUtil.shortenTextWithEllipsis(value, 30, 5);
                    }

                    if (!StringUtil.isEmpty(value)) {
                        String prefix = title.substring(0, start);
                        String suffix = title.substring(end + 1);

                        fullTitle.append(prefix).append(value).append(suffix);
                    }
                }
            }
        }

        if (fullTitle.length() > 0) {
            SimpleTextAttributes valueStyle = wrapper.getAttribute(SimpleTextAttributes.GRAY_ATTRIBUTES);
            renderer.append(fullTitle.toString(), valueStyle);
        }

        if (full) {
            Icon icon = metaModel.getIcon();

            // Annotate icons with lint warnings or errors, if applicable
            HighlightDisplayLevel displayLevel = null;
            if (myProject != null) {
                SeverityRegistrar severityRegistrar = SeverityRegistrar.getSeverityRegistrar(myProject);
                for (ErrorInfo errorInfo : RadComponent.getError(component)) {
                    if (displayLevel == null || severityRegistrar.compare(errorInfo.getLevel().getSeverity(), displayLevel.getSeverity()) > 0) {
                        displayLevel = errorInfo.getLevel();
                    }
                }
                if (displayLevel == HighlightDisplayLevel.ERROR) {
                    LayeredIcon layeredIcon = new LayeredIcon(2);
                    layeredIcon.setIcon(icon, 0);
                    layeredIcon.setIcon(CordovaIcons.ErrorBadge, 1, 10, 10);
                    icon = layeredIcon;
                } else if (displayLevel == HighlightDisplayLevel.WARNING || displayLevel == HighlightDisplayLevel.WEAK_WARNING) {
                    LayeredIcon layeredIcon = new LayeredIcon(2);
                    layeredIcon.setIcon(icon, 0);
                    layeredIcon.setIcon(CordovaIcons.WarningBadge, 1, 10, 10);
                    icon = layeredIcon;
                }
            }

            renderer.setIcon(icon);

            if (component instanceof IComponentDecorator) {
                ((IComponentDecorator) component).decorateTree(renderer, wrapper);
            }
        }
    }
}
