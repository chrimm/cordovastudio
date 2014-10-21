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

package cz.vutbr.web.csskit;

import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.xml.XmlAttribute;
import cz.vutbr.web.css.Selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TagUtil {

    public static final String CLASS_DELIM = " ";
    public static final String CLASS_ATTR = "class";
    public static final String ID_ATTR = "id";

    public static Collection<String> elementClasses(HtmlTag tag) {
        XmlAttribute attr = tag.getAttribute(CLASS_ATTR);

        if (attr != null) {
            String classNames = attr.getValue();

            if (classNames != null) {
                Collection<String> list = new ArrayList<String>();
                for (String cname : classNames.toLowerCase().split(CLASS_DELIM)) {
                    cname = cname.trim();
                    if (cname.length() > 0)
                        list.add(cname);
                }
                return list;
            }

        }

        return Collections.emptyList();
    }

    public static boolean matchesClassOld(HtmlTag tag, String className) {
        XmlAttribute attr = tag.getAttribute(CLASS_ATTR);

        if (attr != null) {
            String attrVal = attr.getValue();

            if (attrVal != null) {
                String classNames = attrVal.toLowerCase();
                int len = className.length();
                int start = classNames.indexOf(className.toLowerCase());
                if (start == -1)
                    return false;
                else
                    return ((start == 0 || Character.isWhitespace(classNames.charAt(start - 1))) &&
                            (start + len == classNames.length() || Character.isWhitespace(classNames.charAt(start + len))));
            }
        }

        return false;
    }

    public static boolean matchesClass(HtmlTag tag, String className) {
        XmlAttribute attr = tag.getAttribute(CLASS_ATTR);

        if (attr != null) {
            String attrVal = attr.getValue();

            if (attrVal != null) {
                String classNames = attrVal.toLowerCase();
                String search = className.toLowerCase();
                int len = className.length();
                int lastIndex = 0;

                while ((lastIndex = classNames.indexOf(search, lastIndex)) != -1) {
                    if ((lastIndex == 0 || Character.isWhitespace(classNames.charAt(lastIndex - 1))) &&
                            (lastIndex + len == classNames.length() || Character.isWhitespace(classNames.charAt(lastIndex + len)))) {
                        return true;
                    }
                    lastIndex += len;
                }
                return false;
            }
        }

        return false;
    }


    public static String elementID(HtmlTag tag) {
        XmlAttribute attr = tag.getAttribute(ID_ATTR);

        if (attr != null) {
            return attr.getValue();
        }

        return "";
    }

    public static boolean matchesID(HtmlTag tag, String id) {
        return id.equalsIgnoreCase(elementID(tag));
    }

    public static String elementName(HtmlTag tag) {
        return tag.getName();
    }

    public static boolean matchesName(HtmlTag tag, String name) {
        return !(name == null || tag == null) && name.equalsIgnoreCase(elementName(tag));
    }

    public static boolean matchesAttribute(HtmlTag tag, String name, String value, Selector.Operator o) {
        XmlAttribute attr = tag.getAttribute(name);

        if (attr != null && o != null) {
            String attributeValue = attr.getValue();

            if (attributeValue == null) {
                attributeValue = "";
            }

            switch (o) {
                case EQUALS:
                    return attributeValue.equals(value);
                case INCLUDES:
                    attributeValue = " " + attributeValue + " ";
                    return attributeValue.matches(".* " + value + " .*");
                case DASHMATCH:
                    return attributeValue.matches("^" + value + "(-.*|$)");
                case CONTAINS:
                    return attributeValue.matches(".*" + value + ".*");
                case STARTSWITH:
                    return attributeValue.matches("^" + value + ".*");
                case ENDSWITH:
                    return attributeValue.matches(".*" + value + "$");
                default:
                    return true;
            }
        }

        return false;
    }

}
