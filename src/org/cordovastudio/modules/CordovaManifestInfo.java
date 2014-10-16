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
package org.cordovastudio.modules;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.cordovastudio.CordovaVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cordovastudio.GlobalConstants.*;
import static org.cordovastudio.project.CordovaManifest.*;

/**
 * Retrieves and caches manifest information such as the themes to be used for
 * a given activity.
 *
 * @see org.cordovastudio.project.CordovaManifest;
 */
public class CordovaManifestInfo {
    private static final Logger LOG = Logger.getInstance(CordovaManifestInfo.class);

    public static class ActivityAttributes {
        @Nullable
        private final String myIcon;
        @Nullable
        private final String myLabel;
        @NotNull
        private final String myName;
        @Nullable
        private final String myParentActivity;
        @Nullable
        private final String myTheme;
        @Nullable
        private final String myUiOptions;

        public ActivityAttributes(@NotNull XmlTag activity, @Nullable String packageName) {
            // Get activity name.
            String name = activity.getAttributeValue(ATTRIBUTE_NAME, CORDOVASTUDIO_URI);
            if (name == null || name.length() == 0) {
                throw new RuntimeException("Activity name cannot be empty.");
            }
            int index = name.indexOf('.');
            if (index <= 0 && packageName != null && !packageName.isEmpty()) {
                name = packageName + (index == -1 ? "." : "") + name;
            }
            myName = name;

            // Get activity icon.
            String value = activity.getAttributeValue(ATTRIBUTE_ICON, CORDOVASTUDIO_URI);
            if (value != null && value.length() > 0) {
                myIcon = value;
            } else {
                myIcon = null;
            }

            // Get activity label.
            value = activity.getAttributeValue(ATTRIBUTE_LABEL, CORDOVASTUDIO_URI);
            if (value != null && value.length() > 0) {
                myLabel = value;
            } else {
                myLabel = null;
            }

            // Get activity parent. Also search the meta-data for parent info.
            value = activity.getAttributeValue(ATTRIBUTE_PARENT_ACTIVITY_NAME, CORDOVASTUDIO_URI);
            if (value == null || value.length() == 0) {
                // TODO: Not sure if meta data can be used for API Level > 16
                XmlTag[] metaData = activity.findSubTags(NODE_METADATA);
                for (XmlTag data : metaData) {
                    String metaDataName = data.getAttributeValue(ATTRIBUTE_NAME, CORDOVASTUDIO_URI);
                    if (VALUE_PARENT_ACTIVITY.equals(metaDataName)) {
                        value = data.getAttributeValue(ATTRIBUTE_VALUE, CORDOVASTUDIO_URI);
                        if (value != null) {
                            index = value.indexOf('.');
                            if (index <= 0 && packageName != null && !packageName.isEmpty()) {
                                value = packageName + (index == -1 ? "." : "") + value;
                                break;
                            }
                        }
                    }
                }
            }
            if (value != null && value.length() > 0) {
                myParentActivity = value;
            } else {
                myParentActivity = null;
            }

            // Get activity theme.
            value = activity.getAttributeValue(ATTRIBUTE_THEME, CORDOVASTUDIO_URI);
            if (value != null && value.length() > 0) {
                myTheme = value;
            } else {
                myTheme = null;
            }

            // Get UI options.
            value = activity.getAttributeValue(ATTRIBUTE_UI_OPTIONS, CORDOVASTUDIO_URI);
            if (value != null && value.length() > 0) {
                myUiOptions = value;
            } else {
                myUiOptions = null;
            }
        }

        @Nullable
        public String getIcon() {
            return myIcon;
        }

        @Nullable
        public String getLabel() {
            return myLabel;
        }

        @NotNull
        public String getName() {
            return myName;
        }

        @Nullable
        public String getParentActivity() {
            return myParentActivity;
        }

        @Nullable
        public String getTheme() {
            return myTheme;
        }

        @Nullable
        public String getUiOptions() {
            return myUiOptions;
        }
    }

    private final Module myModule;
    private String myPackage;
    private String myManifestTheme;
    private ManifestFile myManifestFile;
    private long myLastChecked;
    private CordovaVersion myCordovaVersion;
    private String myApplicationIcon;
    private String myApplicationLabel;
    private IManifest myManifest;
    private Boolean myApplicationDebuggable;

    /**
     * Key for the per-module non-persistent property storing the {@link CordovaManifestInfo} for this module.
     */
    final static Key<CordovaManifestInfo> MANIFEST_FINDER = new Key<CordovaManifestInfo>("adt-manifest-info"); //$NON-NLS-1$

    /**
     * Key for the per-module non-persistent property storing the merged {@link CordovaManifestInfo} for this module.
     */
    final static Key<CordovaManifestInfo> MERGED_MANIFEST_FINDER = new Key<CordovaManifestInfo>("adt-merged-manifest-info");

    CordovaManifestInfo(Module module) {
        myModule = module;
    }

    /**
     * Returns the {@link CordovaManifestInfo} for the given module.
     *
     * @param module the module the finder is associated with
     * @return a {@ManifestInfo} for the given module
     */
    public static CordovaManifestInfo get(Module module) {
        Key<CordovaManifestInfo> key = MANIFEST_FINDER;

        CordovaManifestInfo finder = module.getUserData(key);
        if (finder == null) {
            CordovaFacet facet = CordovaFacet.getInstance(module);
            if (facet == null) {
                throw new IllegalArgumentException("Manifest information can only be obtained on modules with the Cordova facet.");
            }

            finder = new CordovaManifestInfo(module);
            module.putUserData(key, finder);
        }

        return finder;
    }

    /**
     * Clears the cached manifest information. The next get call on one of the
     * properties will cause the information to be refreshed.
     */
    public void clear() {
        myLastChecked = 0;
    }

    /**
     * Returns the default package registered in the Android manifest
     *
     * @return the default package registered in the manifest
     */
    @Nullable
    public String getPackage() {
        sync();
        return myPackage;
    }

    /**
     * Returns the manifest theme registered on the application, if any
     *
     * @return a manifest theme, or null if none was registered
     */
    @Nullable
    public String getManifestTheme() {
        sync();
        return myManifestTheme;
    }

    /**
     * Returns the default theme for this project, by looking at the manifest default
     * theme registration, target SDK, rendering target, etc.
     *
     * @return the theme to use for this project, never null
     */
    @NotNull
    public String getDefaultTheme() {
        sync();

        if (myManifestTheme != null) {
            return myManifestTheme;
        }

        return "default";
    }

    /**
     * Returns the application icon, or null
     *
     * @return the application icon, or null
     */
    @Nullable
    public String getApplicationIcon() {
        sync();
        return myApplicationIcon;
    }

    /**
     * Returns the application label, or null
     *
     * @return the application label, or null
     */
    @Nullable
    public String getApplicationLabel() {
        sync();
        return myApplicationLabel;
    }

    /**
     * Returns the value for the debuggable flag set in the manifest. Returns null if not set.
     */
    @Nullable
    public Boolean getApplicationDebuggable() {
        sync();
        return myApplicationDebuggable;
    }

    /**
     * Returns the target SDK version
     *
     * @return the target SDK version
     */
    @NotNull
    public CordovaVersion getCordovaVersion() {
        sync();
        return myCordovaVersion != null ? myCordovaVersion : CordovaVersion.DEFAULT;
    }


    @NotNull
    protected List<IManifest> getManifests() {
        sync();
        return myManifest != null ? Collections.singletonList(myManifest) : Collections.<IManifest>emptyList();
    }

    /**
     * Ensure that the package, theme and activity maps are initialized and up to date
     * with respect to the manifest file
     */
    private void sync() {
        // Since each of the accessors call sync(), allow a bunch of immediate
        // accessors to all bypass the file stat() below
        long now = System.currentTimeMillis();
        if (now - myLastChecked < 50 && myManifestFile != null) {
            return;
        }
        myLastChecked = now;

        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                syncWithReadPermission();
            }
        });
    }

    private void syncWithReadPermission() {
        if (myManifestFile == null) {
            myManifestFile = ManifestFile.create(myModule);
            if (myManifestFile == null) {
                return;
            }
        }

        // Check to see if our data is up to date
        boolean refresh = myManifestFile.refresh();
        if (!refresh) {
            // Already have up to date data
            return;
        }

        myManifestTheme = null;
        myCordovaVersion = CordovaVersion.DEFAULT;
        myPackage = ""; //$NON-NLS-1$
        myApplicationIcon = null;
        myApplicationLabel = null;

        try {
            XmlFile xmlFile = myManifestFile.getXmlFile();
            if (xmlFile == null) {
                return;
            }

            XmlTag root = xmlFile.getRootTag();
            if (root == null) {
                return;
            }

            myPackage = root.getAttributeValue(ATTRIBUTE_PACKAGE);

            XmlTag[] applications = root.findSubTags(NODE_APPLICATION);
            if (applications.length > 0) {
                assert applications.length == 1;
                XmlTag application = applications[0];
                myApplicationIcon = application.getAttributeValue(ATTRIBUTE_ICON, CORDOVASTUDIO_URI);
                myApplicationLabel = application.getAttributeValue(ATTRIBUTE_LABEL, CORDOVASTUDIO_URI);
                myManifestTheme = application.getAttributeValue(ATTRIBUTE_THEME, CORDOVASTUDIO_URI);

                String debuggable = application.getAttributeValue(ATTRIBUTE_DEBUGGABLE, CORDOVASTUDIO_URI);
                myApplicationDebuggable = debuggable == null ? null : VALUE_TRUE.equals(debuggable);

            }

            // Look up target SDK
            XmlTag[] usesSdks = root.findSubTags(NODE_USES_SDK);
            if (usesSdks.length > 0) {
                XmlTag usesSdk = usesSdks[0];
                myCordovaVersion = getCordovaVersion(usesSdk, ATTRIBUTE_CORDOVA_VERSION, CordovaVersion.DEFAULT);
            }

            myManifest = loadDomElementWithReadPermission(myModule.getProject(), xmlFile, IManifest.class);
        } catch (Exception e) {
            LOG.error("Could not read Manifest data", e);
        }
    }

    private CordovaVersion getCordovaVersion(XmlTag tag, String attribute, CordovaVersion defaultVersion) {
        String valueString = tag.getAttributeValue(attribute, CORDOVASTUDIO_URI);

        if (valueString != null) {
            try {
                return new CordovaVersion(valueString);
            } catch(CordovaVersion.CordovaVersionException e) {
                LOG.error("Could not read Cordova version from Manifest", e);
            }
        }

        return defaultVersion;
    }

    @Nullable
    private IManifest loadDomElementWithReadPermission(@NotNull Project project,
                                                                            @NotNull XmlFile xmlFile,
                                                                            @NotNull Class<IManifest> aClass) {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        DomManager domManager = DomManager.getDomManager(project);
        DomFileElement<IManifest> element = domManager.getFileElement(xmlFile, aClass);

        if (element == null) return null;

        return element.getRootElement();
    }

    private static class ManifestFile {
        private final Module myModule;
        private VirtualFile myVFile;
        private XmlFile myXmlFile;
        private long myLastModified = 0;

        private ManifestFile(@NotNull Module module, @NotNull VirtualFile file) {
            myModule = module;
            myVFile = file;
        }

        @Nullable
        public static synchronized ManifestFile create(@NotNull Module module) {
            ApplicationManager.getApplication().assertReadAccessAllowed();

            CordovaFacet facet = CordovaFacet.getInstance(module);
            if (facet == null) {
                return null;
            }

            VirtualFile manifestFile = null;//facet.getManifestFile();
            if (manifestFile == null) {
                return null;
            }

            return new ManifestFile(module, manifestFile);
        }

        @Nullable
        private XmlFile parseManifest() {
            if (myVFile == null || !myVFile.exists()) {
                return null;
            }
            Project project = myModule.getProject();
            if (project.isDisposed()) {
                return null;
            }
            PsiFile psiFile = PsiManager.getInstance(project).findFile(myVFile);
            return (psiFile instanceof XmlFile) ? (XmlFile)psiFile : null;
        }

        public synchronized boolean refresh() {
            long lastModified = getLastModified();
            if (myXmlFile == null || myLastModified < lastModified) {
                myXmlFile = parseManifest();
                if (myXmlFile == null) {
                    return false;
                }
                myLastModified = lastModified;
                return true;
            } else {
                return false;
            }
        }

        private long getLastModified() {
            if (myXmlFile != null) {
                return myXmlFile.getModificationStamp();
            } else {
                return 0;
            }
        }

        @Nullable
        public synchronized XmlFile getXmlFile() {
            return myXmlFile;
        }
    }
}
