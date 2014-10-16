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

import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.cordovastudio.GlobalConstants.*;

/**
 * A custom version of the {@link LayoutPsiPullParser} which
 * can add padding to a dedicated set of layout nodes, which for example can be used to
 * ensure that empty view groups have certain minimum size during a palette drop.
 */
public class PaddingLayoutPsiPullParser extends LayoutPsiPullParser {
  private final static Pattern FLOAT_PATTERN = Pattern.compile("(-?[0-9]+(?:\\.[0-9]+)?)(.*)"); //$NON-NLS-1$
  private final static int PADDING_VALUE = 10;

  private boolean myZeroAttributeIsPadding = false;
  private boolean myIncreaseExistingPadding = false;

  /**
   * Number of pixels to pad views with in exploded-rendering mode.
   */
  private static final String DEFAULT_PADDING_VALUE = PADDING_VALUE + UNIT_PX;

  /**
   * Number of pixels to pad exploded individual views with. (This is HALF the width of the
   * rectangle since padding is repeated on both sides of the empty content.)
   */
  private static final String FIXED_PADDING_VALUE = "20px"; //$NON-NLS-1$

  /**
   * Set of nodes that we want to auto-pad using {@link #FIXED_PADDING_VALUE} as the padding
   * attribute value. Can be null, which is the case when we don't want to perform any
   * <b>individual</b> node exploding.
   */
  private final Set<XmlTag> myExplodeNodes;

  /**
   * Use the {@link LayoutPsiPullParser#create(com.intellij.psi.xml.XmlFile, RenderLogger, java.util.Set)} factory instead
   */
  PaddingLayoutPsiPullParser(@NotNull XmlFile file, @NotNull RenderLogger logger, @NotNull Set<XmlTag> explodeNodes) {
    super(file, logger);
    myExplodeNodes = explodeNodes;
  }

  @Override
  protected void push(@NotNull Element node) {
    super.push(node);

    myZeroAttributeIsPadding = false;
    myIncreaseExistingPadding = false;
  }

  /*
   * This does not seem to be called by the layoutlib, but we keep this (and maintain
   * it) just in case.
   */
  @Override
  public int getAttributeCount() {
    int count = super.getAttributeCount();
    return count + (myZeroAttributeIsPadding ? 1 : 0);
  }

  /*
   * This does not seem to be called by the layoutlib, but we keep this (and maintain
   * it) just in case.
   */
  @Nullable
  @Override
  public String getAttributeName(int i) {
    if (myZeroAttributeIsPadding) {
      if (i == 0) {
        return ATTR_PADDING;
      }
      else {
        i--;
      }
    }

    return super.getAttributeName(i);
  }

  /*
   * This does not seem to be called by the layoutlib, but we keep this (and maintain
   * it) just in case.
   */
  @Override
  public String getAttributeNamespace(int i) {
    if (myZeroAttributeIsPadding) {
      if (i == 0) {
        return CORDOVASTUDIO_URI;
      }
      else {
        i--;
      }
    }

    return super.getAttributeNamespace(i);
  }

  /*
   * This does not seem to be called by the layoutlib, but we keep this (and maintain
   * it) just in case.
   */
  @Nullable
  @Override
  public String getAttributePrefix(int i) {
    if (myZeroAttributeIsPadding) {
      if (i == 0) {
        assert myRoot != null;
        return myCordovaPrefix;
      }
      else {
        i--;
      }
    }

    return super.getAttributePrefix(i);
  }

  /*
   * This does not seem to be called by the layoutlib, but we keep this (and maintain
   * it) just in case.
   */
  @Nullable
  @Override
  public String getAttributeValue(int i) {
    if (myZeroAttributeIsPadding) {
      if (i == 0) {
        return DEFAULT_PADDING_VALUE;
      }
      else {
        i--;
      }
    }

    Attribute attribute = getAttribute(i);
    if (attribute != null) {
      String value = attribute.value;
      if (value != null && myIncreaseExistingPadding && ATTR_PADDING.equals(attribute.name) &&
          CORDOVASTUDIO_URI.equals(attribute.namespace)) {
        // add the padding and return the value
        return addPaddingToValue(value);
      }
      return value;
    }

    return null;
  }

  /*
   * This is the main method used by the LayoutInflater to query for attributes.
   */
  @Nullable
  @Override
  public String getAttributeValue(String namespace, String localName) {
    boolean isPaddingAttribute = ATTR_PADDING.equals(localName);
    if (isPaddingAttribute && CORDOVASTUDIO_URI.equals(namespace)) {
      Element node = getCurrentNode();
      if (node != null && myExplodeNodes.contains(node.cookie)) {
        return FIXED_PADDING_VALUE;
      }
    }

    if (myZeroAttributeIsPadding && isPaddingAttribute && CORDOVASTUDIO_URI.equals(namespace)) {
      return DEFAULT_PADDING_VALUE;
    }

    String value = super.getAttributeValue(namespace, localName);
    if (value != null) {
      if (myIncreaseExistingPadding && isPaddingAttribute && CORDOVASTUDIO_URI.equals(namespace)) {
        // add the padding and return the value
        return addPaddingToValue(value);
      }

    }

    return value;
  }

  // ------- TypedValue stuff
  // This is adapted from com.android.layoutlib.bridge.ResourceHelper
  // (but modified to directly take the parsed value and convert it into pixel instead of
  // storing it into a TypedValue)
  // this was originally taken from platform/frameworks/base/libs/utils/ResourceTypes.cpp

  private static final class DimensionEntry {
    final String name;
    final int type;

    DimensionEntry(String name, int unit) {
      this.name = name;
      this.type = unit;
    }
  }

  /**
   * {@link DimensionEntry} complex unit: Value is raw pixels.
   */
  private static final int COMPLEX_UNIT_PX = 0;
  /**
   * {@link DimensionEntry} complex unit: Value is Device Independent
   * Pixels.
   */
  private static final int COMPLEX_UNIT_DIP = 1;
  /**
   * {@link DimensionEntry} complex unit: Value is a scaled pixel.
   */
  private static final int COMPLEX_UNIT_SP = 2;
  /**
   * {@link DimensionEntry} complex unit: Value is in points.
   */
  private static final int COMPLEX_UNIT_PT = 3;
  /**
   * {@link DimensionEntry} complex unit: Value is in inches.
   */
  private static final int COMPLEX_UNIT_IN = 4;
  /**
   * {@link DimensionEntry} complex unit: Value is in millimeters.
   */
  private static final int COMPLEX_UNIT_MM = 5;

  private final static DimensionEntry[] DIMENSIONS =
    new DimensionEntry[]{
      new DimensionEntry(UNIT_PX, COMPLEX_UNIT_PX)
    };

  /**
   * Adds padding to an existing dimension.
   * <p/>This will resolve the attribute value (which can be px, dip, dp, sp, pt, in, mm) to
   * a pixel value, add the padding value ({@link #PADDING_VALUE}),
   * and then return a string with the new value as a px string ("42px");
   * If the conversion fails, only the special padding is returned.
   */
  private String addPaddingToValue(@Nullable String s) {
    if (s == null) {
      return DEFAULT_PADDING_VALUE;
    }
    int padding = PADDING_VALUE;
    if (stringToPixel(s)) {
      padding += myLastPixel;
    }

    return padding + UNIT_PX;
  }

  /** Out value from {@link #stringToPixel(String)}: the integer pixel value */
  private int myLastPixel;

  /**
   * Convert the string into a pixel value, and puts it in {@link #myLastPixel}
   *
   * @param s the dimension value from an XML attribute
   * @return true if success.
   */
  private boolean stringToPixel(String s) {
    // remove the space before and after
    s = s.trim();
    int len = s.length();

    if (len <= 0) {
      return false;
    }

    // check that there's no non ASCII characters.
    char[] buf = s.toCharArray();
    for (int i = 0; i < len; i++) {
      if (buf[i] > 255) {
        return false;
      }
    }

    // check the first character
    if (buf[0] < '0' && buf[0] > '9' && buf[0] != '.' && buf[0] != '-') {
      return false;
    }

    // now look for the string that is after the float...
    Matcher m = FLOAT_PATTERN.matcher(s);
    if (m.matches()) {
      String f_str = m.group(1);
      String end = m.group(2);

      float f;
      try {
        f = Float.parseFloat(f_str);
      }
      catch (NumberFormatException e) {
        // this shouldn't happen with the regexp above.
        return false;
      }

      if (end.length() > 0 && end.charAt(0) != ' ') {
        // We only support dimension-type values, so try to parse the unit for dimension
        DimensionEntry dimension = parseDimension(end);
        if (dimension != null) {
            if(dimension.type != COMPLEX_UNIT_PX) {
                return false;
            }

          // store result (converted to int)
          myLastPixel = (int)(f + 0.5); //TODO: why +0.5?? is it to round it? but we can use fractions of pixel...

          return true;
        }
      }
    }

    return false;
  }

  @Nullable
  private static DimensionEntry parseDimension(String str) {
    str = str.trim();

    for (DimensionEntry d : DIMENSIONS) {
      if (d.name.equals(str)) {
        return d;
      }
    }

    return null;
  }
}
