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
        return super.get(new TagDescriptor(tag, htmlClass, type));
    }
}
