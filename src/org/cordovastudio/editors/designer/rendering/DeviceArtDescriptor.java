/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Orignial as of com.android.tools.idea.ddms.screenshot.DeviceArtDescriptor)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Imported getChildren from com.android.tools.lint.detector.api.LintUtils
 *      as we don't want to use the whole LintUtils class
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

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.containers.HashSet;

import org.cordovastudio.branding.artwork.Artwork;
import org.cordovastudio.devices.ScreenOrientation;
import org.cordovastudio.utils.ImageUtils;
import org.cordovastudio.utils.XmlUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.cordovastudio.GlobalConstants.ATTR_ID;
import static org.cordovastudio.GlobalConstants.ATTR_NAME;

/**
 * Descriptor for a device frame picture (background, shadow, reflection) which can be
 * painted around a screenshot or device rendering
 */
public class DeviceArtDescriptor {
    @NotNull
    public static final DeviceArtDescriptor NONE = new DeviceArtDescriptor(null, null);

    private final String myId;
    private final String myName;
    private final File myFolder;
    private OrientationData myPortrait;
    private OrientationData myLandscape;

    @NonNls
    private static final String FN_BASE = "devices";
    @NonNls
    private static final String FN_DESCRIPTOR = "device-art.xml";

    /**
     * Returns the absolute path to {@link #FN_BASE} folder, or null if it couldn't be located.
     */
    @Nullable
    private static File getBundledDescriptorsFolder() {
        try {
            String path = URLDecoder.decode(Artwork.class.getResource(FN_BASE).getFile(), "UTF-8");
            return new File(path);
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        /*
        TODO: implement finding the real device art descriptor file

        // In the IDE distribution, this should be in plugins/android/lib/FN_BASE
        String androidJarPath = PathManager.getJarPathForClass(DeviceArtDescriptor.class);
        if (androidJarPath != null) {
            File androidJar = new File(androidJarPath);
            if (androidJar.isFile()) {
                File base = new File(androidJar.getParentFile(), FN_BASE);
                if (base.exists() && base.isDirectory()) {
                    return base;
                }
            }
        }

        // In development environments, search a few other folders
        String basePath = PathManager.getHomePath();
        String[] paths = new String[]{
                "plugins" + File.separatorChar + "android" + File.separatorChar,
                ".." + File.separator + "adt" + File.separator + "idea" + File.separator + "android" + File.separatorChar,
                "android" + File.separatorChar + "android" + File.separatorChar,
        };

        for (String p : paths) {
            File base = new File(basePath, p);
            if (base.isDirectory()) {
                File files = new File(base, FN_BASE);
                if (files.isDirectory()) {
                    return files;
                }
            }
        }

        return null;

        */
    }

    @Nullable
    private static File getDescriptorFile(@NotNull File folder) {
        File file = new File(folder, FN_DESCRIPTOR);
        return file.isFile() ? file : null;
    }

    private static List<File> getDescriptorFiles(@Nullable File[] additionalRoots) {
        Set<File> roots = new HashSet<File>();

        File base = getBundledDescriptorsFolder();
        if (base != null) {
            roots.add(base);
        }

        if (additionalRoots != null) {
            Collections.addAll(roots, additionalRoots);
        }

        List<File> files = new ArrayList<File>(roots.size());
        for (File root : roots) {
            File file = getDescriptorFile(root);
            if (file != null) {
                files.add(file);
            }
        }

        return files;
    }

    public static List<DeviceArtDescriptor> getDescriptors(@Nullable File[] folders) {
        List<File> files = getDescriptorFiles(folders);
        List<DeviceArtDescriptor> result = Lists.newArrayList();

        for (File file : files)
            try {
                String xml = Files.toString(file, Charsets.UTF_8);
                Document document = XmlUtils.parseDocumentSilently(xml, false);

                if (document != null) {
                    File baseFolder = file.getParentFile();
                    addDescriptors(result, document, baseFolder);
                } else {
                    Logger.getInstance(DeviceArtDescriptor.class).error("Couldn't parse " + file);
                }
            } catch (IOException e) {
                Logger.getInstance(DeviceArtDescriptor.class).error(e);
            }

        return result;
    }

    private DeviceArtDescriptor(@Nullable File baseFolder, @Nullable Element element) {
        if (element == null) {
            myId = myName = "";
            myFolder = null;
        } else {
            myId = element.getAttribute(ATTR_ID);
            myName = element.getAttribute(ATTR_NAME);
            myFolder = new File(baseFolder, myId);

            List<Element> children = getChildren(element);
            for (Element child : children) {
                OrientationData orientation = new OrientationData(this, child);
                if (orientation.isPortrait()) {
                    myPortrait = orientation;
                } else {
                    myLandscape = orientation;
                }
            }
        }
    }

    /**
     * Returns the children elements of the given node
     *
     * @param node the parent node
     * @return a list of element children, never null
     */
    @NotNull
    public static List<Element> getChildren(@NotNull Node node) {
        NodeList childNodes = node.getChildNodes();
        List<Element> children = new ArrayList<Element>(childNodes.getLength());
        for (int i = 0, n = childNodes.getLength(); i < n; i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                children.add((Element) child);
            }
        }

        return children;
    }

    static void addDescriptors(List<DeviceArtDescriptor> result, Document document, File baseFolder) {
        NodeList deviceList = document.getElementsByTagName("device");
        for (int i = 0; i < deviceList.getLength(); i++) {
            Element element = (Element) deviceList.item(i);
            DeviceArtDescriptor descriptor = new DeviceArtDescriptor(baseFolder, element);
            result.add(descriptor);
        }
    }

    @NotNull
    public String getId() {
        return myId;
    }

    @NotNull
    public String getName() {
        return myName;
    }

    @NotNull
    public OrientationData getArtDescriptor(@NotNull ScreenOrientation orientation) {
        return orientation == ScreenOrientation.PORTRAIT ? myPortrait : myLandscape;
    }

    public File getBaseFolder() {
        return myFolder;
    }

    public Dimension getScreenSize(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getScreenSize();
    }

    public Point getScreenPos(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getScreenPos();
    }

    public Dimension getFrameSize(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getFrameSize();
    }

    public Rectangle getCrop(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getCrop();
    }

    @Nullable
    public File getFrame(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getBackgroundFile();
    }

    @Nullable
    public File getDropShadow(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getShadowFile();
    }

    @Nullable
    public File getReflectionOverlay(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getReflectionFile();
    }

    @Nullable
    public File getMask(@NotNull ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getMaskFile();
    }

    public double getAspectRatio(ScreenOrientation orientation) {
        return getArtDescriptor(orientation).getAspectRatio();
    }

    public boolean isStretchable() {
        /* We don't want to stretch any device frame in Cordova Studio as there are too many cases,
         * where stretching is not applicable
         */
        return false;
    }

    /**
     * Returns whether this descriptor can frame the given image.
     */
    public boolean canFrameImage(BufferedImage image, ScreenOrientation orientation) {
        if (isStretchable()) {
            return true;
        }

        // Not all devices are available in all orientations
        if (orientation == ScreenOrientation.PORTRAIT && myPortrait == null) {
            return false;
        } else if (orientation == ScreenOrientation.LANDSCAPE && myLandscape == null) {
            return false;
        }

        // Don't support framing images smaller than our screen size (we don't want to stretch the image)
        Dimension screenSize = getArtDescriptor(orientation).getScreenSize();
        if (image.getWidth() < screenSize.getWidth() || image.getHeight() < screenSize.getHeight()) {
            return false;
        }

        // Make sure that the aspect ratio is nearly identical to the image aspect ratio
        double imgAspectRatio = image.getWidth() / (double) image.getHeight();
        double descriptorAspectRatio = getAspectRatio(orientation);
        return Math.abs(imgAspectRatio - descriptorAspectRatio) < ImageUtils.EPSILON;
    }

    /**
     * Descriptor for a particular device frame (e.g. a set of images for a particular device in a particular orientation)
     */
    private static class OrientationData {
        private final DeviceArtDescriptor myDevice;
        private final String myShadowName;
        private final String myBackgroundName;
        private final String myReflectionName;
        private final String myMaskName;
        private final Dimension myScreenSize;
        private final Point myScreenPos;
        private final Dimension myFrameSize;
        private final Rectangle myCrop;
        private final ScreenOrientation myOrientation;

        OrientationData(DeviceArtDescriptor device, Element element) {
            myDevice = device;
            String orientation = element.getAttribute(ATTR_NAME);
            if ("port".equals(orientation)) {
                myOrientation = ScreenOrientation.PORTRAIT;
            } else {
                assert "land".equals(orientation) : orientation;
                myOrientation = ScreenOrientation.LANDSCAPE;
            }

            myFrameSize = getDimension(element.getAttribute("size"));
            myScreenSize = getDimension(element.getAttribute("screenSize"));
            myScreenPos = getPoint(element.getAttribute("screenPos"));
            myCrop = getRectangle(element.getAttribute("crop"));

            myBackgroundName = getFileName(element, "back");
            myShadowName = getFileName(element, "shadow");
            myReflectionName = getFileName(element, "lights");
            myMaskName = getFileName(element, "mask");
        }

        @Nullable
        private static String getFileName(Element element, String name) {
            return name != null && !name.isEmpty() ? element.getAttribute(name) : null;
        }

        @Nullable
        private static Dimension getDimension(String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }

            int comma = value.indexOf(',');
            if (comma == -1) {
                return null;
            }
            return new Dimension(getInteger(value.substring(0, comma)), getInteger(value.substring(comma + 1)));
        }

        @Nullable
        private static Point getPoint(String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }

            int comma = value.indexOf(',');
            if (comma == -1) {
                return null;
            }
            return new Point(getInteger(value.substring(0, comma)), getInteger(value.substring(comma + 1)));
        }

        @Nullable
        private static Rectangle getRectangle(String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }

            int comma1 = value.indexOf(',');
            if (comma1 == -1) {
                return null;
            }
            int comma2 = value.indexOf(',', comma1 + 1);
            if (comma2 == -1) {
                return null;
            }
            int comma3 = value.indexOf(',', comma2 + 1);
            if (comma3 == -1) {
                return null;
            }
            String x = value.substring(0, comma1);
            String y = value.substring(comma1 + 1, comma2);
            String w = value.substring(comma2 + 1, comma3);
            String h = value.substring(comma3 + 1);
            return new Rectangle(getInteger(x), getInteger(y), getInteger(w), getInteger(h));
        }

        private static int getInteger(String value) {
            return Integer.parseInt(value);
        }

        public boolean isPortrait() {
            return myOrientation == ScreenOrientation.PORTRAIT;
        }

        public Dimension getScreenSize() {
            return myScreenSize;
        }

        public Point getScreenPos() {
            return myScreenPos;
        }

        public Dimension getFrameSize() {
            return myFrameSize;
        }

        public Rectangle getCrop() {
            return myCrop;
        }

        @NotNull
        public ScreenOrientation getOrientation() {
            return myOrientation;
        }

        @Nullable
        public File getBackgroundFile() {
            return myBackgroundName != null ? new File(myDevice.getBaseFolder(), myBackgroundName) : null;
        }

        @Nullable
        public File getShadowFile() {
            return myShadowName != null ? new File(myDevice.getBaseFolder(), myShadowName) : null;
        }

        @Nullable
        public File getReflectionFile() {
            return myReflectionName != null ? new File(myDevice.getBaseFolder(), myReflectionName) : null;
        }

        @Nullable
        public File getMaskFile() {
            return myMaskName != null ? new File(myDevice.getBaseFolder(), myMaskName) : null;
        }

        public double getAspectRatio() {
            return myScreenSize.width / (double) myScreenSize.height;
        }
    }
}
