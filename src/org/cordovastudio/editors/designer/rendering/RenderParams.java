/*
 * Copyright (C) 2010 The Android Open Source Project
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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

/**
 * Base class for rendering parameters. This include the generic parameters but not what needs
 * to be rendered or additional parameters.
 *
 */
public class RenderParams {

    public static final long DEFAULT_TIMEOUT = 250; //ms

    private final Project mProject;
    private final RenderLogger mLog;

    private boolean mCustomBackgroundEnabled;
    private int mCustomBackgroundColor;
    private long mTimeout;

    private IImageFactory mImageFactory;

    private String mAppIcon;
    private String mAppLabel;
    private String mLocale;
    private String mActivityName;
    private boolean mForceNoDecor;
    private ILayoutPullParser mParser;

    public RenderParams(ILayoutPullParser parser, Module module, RenderLogger logger) {
        mParser = parser;
        mLog = logger;
        mProject = module.getProject();
    }


    /**
     *
     * @param project An Object identifying the project. This is used for the cache mechanism.
     * @param log the object responsible for displaying warning/errors to the user.
     */
    public RenderParams(Project project, RenderLogger log) {
        mProject = project;
        mLog = log;
        mCustomBackgroundEnabled = false;
        mTimeout = DEFAULT_TIMEOUT;
    }

    /**
     * Copy constructor.
     */
    public RenderParams(RenderParams params) {
        mProject = params.mProject;
        mLog = params.mLog;
        mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
        mCustomBackgroundColor = params.mCustomBackgroundColor;
        mTimeout = params.mTimeout;
        mImageFactory = params.mImageFactory;
        mAppIcon = params.mAppIcon;
        mAppLabel = params.mAppLabel;
        mLocale = params.mLocale;
        mActivityName = params.mActivityName;
        mForceNoDecor = params.mForceNoDecor;
    }

    public ILayoutPullParser getParser() {
        return mParser;
    }

    public void setOverrideBgColor(int color) {
        mCustomBackgroundEnabled = true;
        mCustomBackgroundColor = color;
    }

    public void setTimeout(long timeout) {
        mTimeout = timeout;
    }

    public void setImageFactory(IImageFactory imageFactory) {
        mImageFactory = imageFactory;
    }

    public void setAppIcon(String appIcon) {
        mAppIcon = appIcon;
    }

    public void setAppLabel(String appLabel) {
        mAppLabel = appLabel;
    }

    public void setLocale(String locale) {
        mLocale = locale;
    }

    public void setActivityName(String activityName) {
        mActivityName = activityName;
    }

    public void setForceNoDecor() {
        mForceNoDecor = true;
    }

    public Object getProjectKey() {
        return mProject;
    }

    public RenderLogger getLog() {
        return mLog;
    }

    public boolean isBgColorOverridden() {
        return mCustomBackgroundEnabled;
    }

    public int getOverrideBgColor() {
        return mCustomBackgroundColor;
    }

    public long getTimeout() {
        return mTimeout;
    }

    public IImageFactory getImageFactory() {
        return mImageFactory;
    }

    public String getAppIcon() {
        return mAppIcon;
    }

    public String getAppLabel() {
        return mAppLabel;
    }

    public String getLocale() {
        return mLocale;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public boolean isForceNoDecor() {
        return mForceNoDecor;
    }
}
