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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by cti on 05.11.14.
 */

/**
 * A Tripel holding a HTML tag, as well as its class and type attribute values.
 */
public class TagDescriptor {
    @NotNull
    private String mTag;

    @Nullable
    private String mHtmlClass;

    @Nullable
    private String mHtmlType;

    public TagDescriptor(@NotNull String tag, @Nullable String htmlClass, @Nullable String type) {
        mTag = tag;
        mHtmlClass = htmlClass;
        mHtmlType = type;
    }

    @NotNull
    public String getTag() {
        return mTag;
    }

    @Nullable
    public String getHtmlClass() {
        return mHtmlClass;
    }

    @Nullable
    public String getHtmlType() {
        return mHtmlType;
    }

    public void setTag(@NotNull String tag) {
        this.mTag = tag;
    }

    public void setHtmlClass(@Nullable String htmlClass) {
        this.mHtmlClass = htmlClass;
    }

    public void setType(@Nullable String type) {
        this.mHtmlType = type;
    }

    public boolean hasType() {
        return mHtmlType != null && !mHtmlType.isEmpty();
    }

    public boolean hasClass() {
        return mHtmlClass != null && !mHtmlClass.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDescriptor that = (TagDescriptor) o;

        if (mHtmlClass != null ? !mHtmlClass.equals(that.mHtmlClass) : that.mHtmlClass != null) return false;
        if (!mTag.equals(that.mTag)) return false;
        if (mHtmlType != null ? !mHtmlType.equals(that.mHtmlType) : that.mHtmlType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTag.hashCode();
        result = 31 * result + (mHtmlClass != null ? mHtmlClass.hashCode() : 0);
        result = 31 * result + (mHtmlType != null ? mHtmlType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TagDescriptor{" +
                "Tag='" + mTag + '\'' +
                ", HtmlClass='" + mHtmlClass + '\'' +
                ", Type='" + mHtmlType + '\'' +
                '}';
    }
}
