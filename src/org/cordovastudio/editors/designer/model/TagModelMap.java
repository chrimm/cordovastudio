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

package org.cordovastudio.editors.designer.model;

import java.util.HashMap;

/**
 * Created by cti on 05.11.14.
 */
public class TagModelMap extends HashMap<TagDescriptor, MetaModel> {

    public MetaModel get(String tag) {
        return get(tag, null, null);
    }

    public MetaModel get(String tag, String htmlClass, String type) {
        return get(new TagDescriptor(tag, htmlClass, type));
    }

    public MetaModel get(TagDescriptor tagDescriptor) {
        /* Tag, Type AND Class */
        if (tagDescriptor.hasType() && tagDescriptor.hasClass()){
            return super.get(tagDescriptor);
        }
        /* Tag only, ignore Type and Class */
        else if (!tagDescriptor.hasType() && !tagDescriptor.hasClass()) {
            for(TagDescriptor currDescr : super.keySet()) {
                if(currDescr.getTag().equalsIgnoreCase(tagDescriptor.getTag())) {
                    return super.get(currDescr);
                }
            }
        }
        /* Tag and Type, ignore Class */
        else if (tagDescriptor.hasType() && !tagDescriptor.hasClass()) {
            for(TagDescriptor currDescr : super.keySet()) {
                if(currDescr.getTag().equalsIgnoreCase(tagDescriptor.getTag())
                        && currDescr.getHtmlType() != null
                        && currDescr.getHtmlType().equalsIgnoreCase(tagDescriptor.getHtmlType())) {
                    return super.get(currDescr);
                }
            }
        }
        /* Tag and Class, ignore Type */
        else if (!tagDescriptor.hasType() && tagDescriptor.hasClass()) {
            for(TagDescriptor currDescr : super.keySet()) {
                if(currDescr.getTag().equalsIgnoreCase(tagDescriptor.getTag())
                        && currDescr.getHtmlClass() != null
                        && currDescr.getHtmlClass().equalsIgnoreCase(tagDescriptor.getHtmlClass())) {
                    return super.get(currDescr);
                }
            }
        }

        /* None found */
        return null;
    }
}
