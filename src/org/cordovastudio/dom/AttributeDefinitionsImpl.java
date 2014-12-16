/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
package org.cordovastudio.dom;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlComment;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.containers.HashMap;
import com.intellij.xml.util.XmlUtil;
import com.intellij.xml.util.documentation.XmlDocumentationProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.cordovastudio.GlobalConstants.*;
/**
 * @author yole
 */
public class AttributeDefinitionsImpl implements AttributeDefinitions {
  private static final Logger LOG = Logger.getInstance("#org.jetbrains.android.dom.attrs.AttributeDefinitionsImpl");

  private Map<String, AttributeDefinition> myAttrs = new HashMap<String, AttributeDefinition>();
  private Map<String, StyleableDefinitionImpl> myStyleables = new HashMap<String, StyleableDefinitionImpl>();

  private final List<StyleableDefinition> myStateStyleables = new ArrayList<StyleableDefinition>();
  private final Map<String, Map<String, Integer>> myEnumMap = new HashMap<String, Map<String, Integer>>();

  public AttributeDefinitionsImpl(@NotNull XmlFile... files) {
    for (XmlFile file : files) {
      addAttrsFromFile(file);
    }
  }

  private void addAttrsFromFile(XmlFile file) {
    Map<StyleableDefinitionImpl, String[]> parentMap = new HashMap<StyleableDefinitionImpl, String[]>();
    final XmlDocument document = file.getDocument();
    if (document == null) return;
    final XmlTag rootTag = document.getRootTag();
    if (rootTag == null || !TAG_RESOURCES.equals(rootTag.getName())) return;
    for (XmlTag tag : rootTag.getSubTags()) {
      String tagName = tag.getName();
      if (tagName.equals(TAG_ATTR)) {
        parseAttrTag(tag, null);
      }
      else if (tagName.equals(TAG_DECLARE_STYLEABLE)) {
        parseDeclareStyleableTag(tag, parentMap);
      }
    }

    for (Map.Entry<StyleableDefinitionImpl, String[]> entry : parentMap.entrySet()) {
      StyleableDefinitionImpl definition = entry.getKey();
      String[] parentNames = entry.getValue();
      for (String parentName : parentNames) {
        StyleableDefinitionImpl parent = getStyleableByName(parentName);
        if (parent != null) {
          definition.addParent(parent);
          parent.addChild(definition);
        }
        else {
          LOG.info("Found tag with unknown parent: " + parentName);
        }
      }
    }
  }

  @Nullable
  private AttributeDefinition parseAttrTag(XmlTag tag, @Nullable String parentStyleable) {
    String name = tag.getAttributeValue(ATTR_NAME);
    if (name == null) {
      LOG.info("Found attr tag with no name: " + tag.getText());
      return null;
    }
    List<AttributeFormat> parsedFormats;
    List<AttributeFormat> formats = new ArrayList<AttributeFormat>();
    String format = tag.getAttributeValue(ATTR_TYPE);
    if (format != null) {
      parsedFormats = parseAttrFormat(format);
      if (parsedFormats != null) formats.addAll(parsedFormats);
    }
    XmlTag[] values = tag.findSubTags(TAG_ENUM);
    if (values.length > 0) {
      formats.add(AttributeFormat.Enum);
    }
    else {
      values = tag.findSubTags(TAG_FLAG);
      if (values.length > 0) {
        formats.add(AttributeFormat.Flag);
      }
    }
    AttributeDefinition def = myAttrs.get(name);
    if (def == null) {
      def = new AttributeDefinition(name);
      myAttrs.put(def.getName(), def);
    }
    def.addFormats(formats);
    parseDocComment(tag, def, parentStyleable);
    parseAndAddValues(def, values);
    return def;
  }

  private static void parseDocComment(XmlTag tag, AttributeDefinition def, @Nullable String styleable) {
    PsiElement comment = XmlDocumentationProvider.findPreviousComment(tag);
    if (comment != null) {
      String docValue = XmlUtil.getCommentText((XmlComment) comment);
      if (docValue != null && !StringUtil.isEmpty(docValue)) {
        def.addDocValue(docValue, styleable);
      }
    }
  }

  @Nullable
  private static List<AttributeFormat> parseAttrFormat(String formatString) {
    List<AttributeFormat> result = new ArrayList<AttributeFormat>();
    final String[] formats = formatString.split("\\|");
    for (String format : formats) {
      final AttributeFormat attributeFormat;
      try {
        attributeFormat = AttributeFormat.valueOf(StringUtil.capitalize(format));
      }
      catch (IllegalArgumentException e) {
        return null;
      }
      result.add(attributeFormat);
    }
    return result;
  }

  private void parseAndAddValues(AttributeDefinition def, XmlTag[] values) {
    for (XmlTag value : values) {
      final String valueName = value.getAttributeValue(ATTR_NAME);
      if (valueName == null) {
        LOG.info("Unknown value for tag: " + value.getText());
      }
      else {
        def.addValue(valueName);

        final String strIntValue = value.getAttributeValue(ATTR_VALUE);
        if (strIntValue != null) {
          try {
            // Integer.decode cannot handle "ffffffff", see JDK issue 6624867
            int intValue = (int) (long) Long.decode(strIntValue);
            Map<String, Integer> value2Int = myEnumMap.get(def.getName());
            if (value2Int == null) {
              value2Int = new HashMap<String, Integer>();
              myEnumMap.put(def.getName(), value2Int);
            }
            value2Int.put(valueName, intValue);
          }
          catch (NumberFormatException ignored) {
          }
        }
      }
    }
  }

  private void parseDeclareStyleableTag(XmlTag tag, Map<StyleableDefinitionImpl, String[]> parentMap) {
    String name = tag.getAttributeValue(ATTR_NAME);
    if (name == null) {
      LOG.info("Found declare-styleable tag with no name: " + tag.getText());
      return;
    }
    StyleableDefinitionImpl def = new StyleableDefinitionImpl(name);
    String parentNameAttributeValue = tag.getAttributeValue(ATTR_PARENT);
    if (parentNameAttributeValue != null) {
      String[] parentNames = parentNameAttributeValue.split("\\s+");
      parentMap.put(def, parentNames);
    }
    myStyleables.put(name, def);

    if (name.endsWith("State")) {
      myStateStyleables.add(def);
    }

    for (XmlTag subTag : tag.findSubTags(TAG_ATTR)) {
      parseStyleableAttr(def, subTag);
    }
  }

  private void parseStyleableAttr(StyleableDefinitionImpl def, XmlTag tag) {
    String name = tag.getAttributeValue(ATTR_NAME);
    if (name == null) {
      LOG.info("Found attr tag with no name: " + tag.getText());
      return;
    }

    final AttributeDefinition attr = parseAttrTag(tag, def.getName());
    if (attr != null) {
      def.addAttribute(attr);
    }
  }

  @Override
  @Nullable
  public StyleableDefinitionImpl getStyleableByName(@NotNull String name) {
    return myStyleables.get(name);
  }

  @NotNull
  @Override
  public Set<String> getAttributeNames() {
    return myAttrs.keySet();
  }

  @Override
  @Nullable
  public AttributeDefinition getAttrDefByName(@NotNull String name) {
    return myAttrs.get(name);
  }

  @NotNull
  @Override
  public StyleableDefinition[] getStateStyleables() {
    return myStateStyleables.toArray(new StyleableDefinition[myStateStyleables.size()]);
  }

  @NotNull
  public Map<String, Map<String, Integer>> getEnumMap() {
    return myEnumMap;
  }
}
