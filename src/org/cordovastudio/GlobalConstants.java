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

package org.cordovastudio;

/**
 * Created by cti on 29.08.14.
 */
public final class GlobalConstants {

    /**
     * Global settings
     */
    public static final boolean DEBUG = true;
    public static final Class RENDERING_ENGINE = org.cordovastudio.editors.designer.rendering.engines.DummyRenderer.class;

    /**
     * Platform data
     */
    public static final int PLATFORM_UNKNOWN = 0;
    public static final int PLATFORM_LINUX = 1;
    public static final int PLATFORM_WINDOWS = 2;
    public static final int PLATFORM_MACOS = 3;

    /**
     * Returns current platform, one of {@link #PLATFORM_WINDOWS}, {@link #PLATFORM_MACOS},
     * {@link #PLATFORM_LINUX} or {@link #PLATFORM_UNKNOWN}.
     */
    public static final int CURRENT_PLATFORM = currentPlatform();

    /*
     * Cordova URIs
     */
    /** Namespace used in XML files */

    public static final String CORDOVASTUDIO_URI = "http://schemas.cordovastudio.org/";
    public static final String CORDOVASTUDIO_NS_PREFIX = "cordovastudio";
    public static final String CORDOVA_NS_PREFIX = "cdv";
    /** The default prefix used for the {@link #CORDOVASTUDIO_URI} name space */
    public static final String CORDOVASTUDIO_NS_NAME = "cordova";

    /** URI of the reserved "xmlns"  prefix */
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    /** The "xmlns" attribute name */
    public static final String XMLNS = "xmlns";
    /** The default prefix used for the {@link #XMLNS_URI} */
    public static final String XMLNS_PREFIX = "xmlns:";

    /*
     * File Names and extensions
     */
    public static final String DOT_XML = ".xml";
    public static final String DOT_GIF = ".gif";
    public static final String DOT_JPG = ".jpg";
    public static final String DOT_JPEG = ".jpeg";
    public static final String DOT_PNG = ".png";
    public static final String DOT_PROPERTIES = ".properties";

    public static final String EXT_PNG = "png";

    public static final String FN_CORDOVA_MANIFEST_XML = "config.xml";

    /** Device definition list */
    public static final String FN_DEVICES_XML = "devices.xml";
    public static final String MANUFACTURER_GENERIC = "Generic";

    /*
     * Tags
     */
    public static final String VIEW_TAG = "view";

    /*
     * HTML Tag Attributes
     */
    public static final String ATTR_ID = "id";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_CLASS = "class";
    public static final String ATTR_STYLE = "style";
    public static final String ATTR_TITLE = "title";

    /*
     * Tag values
     */
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    public static final String VALUE_N_PX = "%dpx";

    /*
     * HTML Entities
     */
    /** The entity for the ampersand character */
    public static final String AMP_ENTITY = "&amp;";
    /** The entity for the quote character */
    public static final String QUOT_ENTITY = "&quot;";
    /** The entity for the apostrophe character */
    public static final String APOS_ENTITY = "&apos;";
    /** The entity for the less than character */
    public static final String LT_ENTITY = "&lt;";
    /** The entity for the greater than character */
    public static final String GT_ENTITY = "&gt;";

    /*
     * Units
     */
    /** The 'px' unit */
    public static final String UNIT_PX = "px";

    /**
     * Returns current platform
     *
     * @return one of {@link #PLATFORM_WINDOWS}, {@link #PLATFORM_MACOS},
     * {@link #PLATFORM_LINUX} or {@link #PLATFORM_UNKNOWN}.
     */
    public static int currentPlatform() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            return PLATFORM_MACOS;
        } else if (os.startsWith("Windows")) {
            return PLATFORM_WINDOWS;
        } else if (os.startsWith("Linux")) {
            return PLATFORM_LINUX;
        }

        return PLATFORM_UNKNOWN;
    }

    /**
     * Returns current platform's UI name
     *
     * @return one of "Windows", "Mac OS X", "Linux" or "other".
     */
    public static String currentPlatformName() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            return "Mac OS X";
        } else if (os.startsWith("Windows")) {
            return "Windows";
        } else if (os.startsWith("Linux")) {
            return "Linux";
        }

        return "Other";
    }

    /***
     * DEPRECATED STUFF
     * Here only for backwards compatibility during development
     * TODO: remove!
     */
    /** @deprecated Old Android artifact, won't be used in Cordova Studio, just here for compatibility during development */
    public static final String VIEW_INCLUDE = "include";
    /** @deprecated Old Android artifact, won't be used in Cordova Studio, just here for compatibility during development */
    public static final String TOOLS_URI = "http://schemas.android.com/tools";
    /** @deprecated Old Android artifact, won't be used in Cordova Studio, just here for compatibility during development */
    public static final String NS_RESOURCES = "http://schemas.android.com/apk/res/android";
    /** Namespace used for auto-adjusting namespaces
     * @deprecated Old Android artifact, won't be used in Cordova Studio, just here for compatibility during development */
    public static final String AUTO_URI = "http://schemas.android.com/apk/res-auto";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String VIEW_FRAGMENT = "fragment";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String VALUE_WRAP_CONTENT = "wrap_content";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String VALUE_FILL_PARENT = "fill_parent";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String VALUE_VERTICAL = "vertical";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String VALUE_HORIZONTAL = "horizontal";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT = "layout";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_GRAVITY = "layout_gravity";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_WIDTH = "layout_width";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_HEIGHT = "layout_height";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_WEIGHT = "layout_weight";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN = "layout_margin";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_ORIENTATION = "orientation";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_WEIGHT_SUM = "weightSum";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_BASELINE_ALIGNED = "baselineAligned";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_PADDING = "padding";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_VISIBILITY = "visibility";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_BELOW = "layout_below";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ABOVE = "layout_above";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_LEFT = "layout_alignLeft";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_RIGHT = "layout_alignRight";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_TOP = "layout_alignTop";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_BOTTOM = "layout_alignBottom";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_TO_RIGHT_OF = "layout_toRightOf";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_TO_LEFT_OF = "layout_toLeftOf";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_LEFT = "layout_alignParentLeft";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_RIGHT = "layout_alignParentRight";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_TOP = "layout_alignParentTop";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_BOTTOM = "layout_alignParentBottom";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_BASELINE = "layout_alignBaseline";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_START = "layout_alignStart";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_END = "layout_alignEnd";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_TO_START_OF = "layout_toStartOf";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_TO_END_OF = "layout_toEndOf";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_START = "layout_alignParentStart";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ALIGN_PARENT_END = "layout_alignParentEnd";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_CENTER_IN_PARENT = "layout_centerInParent";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_CENTER_VERTICAL = "layout_centerVertical";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_CENTER_HORIZONTAL = "layout_centerHorizontal";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_LEFT = "layout_marginLeft";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_RIGHT = "layout_marginRight";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_START = "layout_marginStart";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_END = "layout_marginEnd";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_TOP = "layout_marginTop";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_MARGIN_BOTTOM = "layout_marginBottom";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String FRAME_LAYOUT = "FrameLayout";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String LIST_VIEW = "ListView";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String EXPANDABLE_LIST_VIEW = "ExpandableListView";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String GRID_VIEW = "GridView";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String SPINNER = "Spinner";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String TAG_RESOURCES = "resources";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String TAG_ATTR = "attr";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String TAG_DECLARE_STYLEABLE = "declare-styleable";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_FORMAT = "format";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String TAG_ENUM = "enum";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String TAG_FLAG = "flag";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_VALUE = "value";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_PARENT = "parent";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LABEL = "label";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_ICON = "icon";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_THEME = "theme";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_ROW_COUNT = "rowCount";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_COLUMN_COUNT = "columnCount";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ROW = "layout_row";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_ROW_SPAN = "layout_rowSpan";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_COLUMN = "layout_column";
    /** @deprecated Old Android artifact. Will be removed. */
    public static final String ATTR_LAYOUT_COLUMN_SPAN = "layout_columnSpan";
}
