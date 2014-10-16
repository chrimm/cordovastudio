/*
 * Copyright (C) 2014 Christoffer T. Timm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cordovastudio.editors.designer.palette;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.text.StringUtil;
import org.cordovastudio.editors.designer.model.MetaModel;
import org.cordovastudio.utils.ImageUtils;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Implementation of a {@link PaletteItem} which delegates to another {@linkplain PaletteItem}
 * but which possibly overrides the title, icon and or creation properties.
 */
public class VariationPaletteItem implements PaletteItem {
    private final PaletteItem myDefaultItem;
    private final String myTitle;
    private final String myIconPath;
    private final String myTooltip;
    private final String myCreation;
    private Icon myIcon;
    private MetaModel myModel;

    public VariationPaletteItem(PaletteItem defaultItem, MetaModel model, Element element) {
        myDefaultItem = defaultItem;
        myModel = model;

        String title = element.getAttributeValue("title");
        if (StringUtil.isEmpty(title)) {
            title = myDefaultItem.getTitle();
        }
        myTitle = title;

        myIconPath = element.getAttributeValue("icon");

        if (StringUtil.isEmpty(myIconPath)) {
            myIcon = myDefaultItem.getIcon();
        } else {
            myIcon = ImageUtils.getReflectiveIcon(myIconPath);
        }

        String tooltip = element.getAttributeValue("tooltip");
        if (StringUtil.isEmpty(tooltip)) {
            tooltip = myDefaultItem.getTooltip();
        }
        myTooltip = tooltip;

        Element creation = element.getChild("creation");
        if (creation != null) {
            myCreation = creation.getTextTrim();
        } else {
            myCreation = myModel.getCreation();
        }
    }

    @Override
    public String getTitle() {
        return myTitle;
    }

    @Override
    public Icon getIcon() {
        if (myIcon == null) {
            assert myIconPath != null;
            myIcon = ImageUtils.getReflectiveIcon(myIconPath);
        }
        return myIcon;
    }

    @Override
    public String getTooltip() {
        return myTooltip;
    }

    @Override
    public String getVersion() {
        return myDefaultItem.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return myDefaultItem.isEnabled();
    }

    @Override
    public String getCreation() {
        return myCreation;
    }

    @Override
    public MetaModel getMetaModel() {
        return myModel;
    }

    @Override
    public void setMetaModel(MetaModel metaModel) {
        myModel = metaModel;
    }

    @Nullable
    @Override
    public String getDeprecatedIn() {
        return myDefaultItem.getDeprecatedIn();
    }

    @Nullable
    @Override
    public String getDeprecatedHint() {
        return myDefaultItem.getDeprecatedHint();
    }
}