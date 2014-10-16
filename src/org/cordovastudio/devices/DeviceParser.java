/*
 * Copyright (C) 2012 The Android Open Source Project
 * (original as of com.android.sdklib.devices.DeviceParser)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  - Adopted to device definition XSD for Cordova Studio devices
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

package org.cordovastudio.devices;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceParser {

    private static class DeviceHandler extends DefaultHandler {
        private static final String sSpaceRegex = "[\\s]+";
        private final List<Device> mDevices = new ArrayList<Device>();
        private final StringBuilder mStringAccumulator = new StringBuilder();
        private final File mParentFolder;
        private Artwork mArtwork;
        private State mState;
        private Device.Builder mBuilder;
        private String[] mBootProp;

        public DeviceHandler(@Nullable File parentFolder) {
            mParentFolder = parentFolder;
        }

        @NotNull
        public List<Device> getDevices() {
            return mDevices;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes)
                throws SAXException {

            if (DeviceSchema.NODE_DEVICE.equals(localName)) {
                // Reset everything
                mArtwork = null;
                mState = null;
                mBuilder = new Device.Builder();
            } else if (DeviceSchema.NODE_ARTWORK.equals(localName)) {
                mArtwork = new Artwork();
            } else if (DeviceSchema.NODE_STATE.equals(localName)) {
                mState = new State();
                String defaultState = attributes.getValue(DeviceSchema.ATTR_DEFAULT);
                if ("true".equals(defaultState) || "1".equals(defaultState)) {
                    mState.setDefaultState(true);
                }
                mState.setName(attributes.getValue(DeviceSchema.ATTR_NAME).trim());
            } else if (DeviceSchema.NODE_DIMENSIONS.equals(localName)) {
                mArtwork.setFrameOffsetLandscape(new Point());
                mArtwork.setFrameOffsetPortrait(new Point());
            }
            mStringAccumulator.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            mStringAccumulator.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (DeviceSchema.NODE_DEVICE.equals(localName)) {
                mDevices.add(mBuilder.build());
            } else if (DeviceSchema.NODE_NAME.equals(localName)) {
                mBuilder.setName(getString(mStringAccumulator));
            } else if (DeviceSchema.NODE_DISPLAYNAME.equals(localName)) {
                mBuilder.setDisplayName(getString(mStringAccumulator));
            } else if (DeviceSchema.NODE_MANUFACTURER.equals(localName)) {
                mBuilder.setManufacturer(getString(mStringAccumulator));
            } else if (DeviceSchema.NODE_ARTWORK.equals(localName)) {
                mBuilder.setArtwork(mArtwork);
            } else if (DeviceSchema.NODE_STATE.equals(localName)) {
                mBuilder.addState(mState);
            } else if (DeviceSchema.NODE_ICON.equals(localName)) {
                mArtwork.setIcon(new File(mParentFolder, getString(mStringAccumulator)));
            //} else if (DeviceSchema.NODE_FILENAME.equals(localName)) {
            //    mArtwork.setFramePortrait(new File(mParentFolder, mStringAccumulator.toString().trim()));
                //TODO: How to determine whether to create landscape or portrait frame file?
            //} else if (DeviceSchema.NODE_PORTRAIT_X_OFFSET.equals(localName)) {
                //mArtwork.getFrameOffsetPortrait().x = getInteger(mStringAccumulator);
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:left> tag, but do not know which one. The parent tag will be <d:offset> so we need to access the parent's parent tag...
            //} else if (DeviceSchema.NODE_PORTRAIT_Y_OFFSET.equals(localName)) {
                //mArtwork.getFrameOffsetPortrait().y = getInteger(mStringAccumulator);
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:top> tag, but do not know which one. The parent tag will be <d:offset> so we need to access the parent's parent tag...
            //} else if (DeviceSchema.NODE_LANDSCAPE_X_OFFSET.equals(localName)) {
                //mArtwork.getFrameOffsetLandscape().x = getInteger(mStringAccumulator);
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:left> tag, but do not know which one. The parent tag will be <d:offset> so we need to access the parent's parent tag...
            //} else if (DeviceSchema.NODE_LANDSCAPE_Y_OFFSET.equals(localName)) {
                //mArtwork.getFrameOffsetLandscape().y = getInteger(mStringAccumulator);
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:top> tag, but do not know which one. The parent tag will be <d:offset> so we need to access the parent's parent tag...
            //} else if (DeviceSchema.NODE_X_DIMENSION.equals(localName)) {
                //mHardware.getScreen().setXDimension(getInteger(mStringAccumulator));
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:width> tag, but do not know which one. We should access the parent tag...
            //} else if (DeviceSchema.NODE_Y_DIMENSION.equals(localName)) {
                //mHardware.getScreen().setYDimension(getInteger(mStringAccumulator));
                //TODO: How to determine whether we're in landscape or portrait Section of device definition? We just know, that we're closing an <d:height> tag, but do not know which one. We should access the parent tag...
            } else if (DeviceSchema.NODE_DESCRIPTION.equals(localName)) {
                mState.setDescription(getString(mStringAccumulator));
            } else if (DeviceSchema.NODE_ORIENTATION.equals(localName)) {
                mState.setOrientation(ScreenOrientation.getEnum(getString(mStringAccumulator)));
            } else if (DeviceSchema.NODE_STATUSBAR.equals(localName)) {
                mState.setStatusbar(getBool(mStringAccumulator));
            }
        }

        @Override
        public void error(SAXParseException e) throws SAXParseException {
            throw e;
        }

        private List<String> getStringList(StringBuilder stringAccumulator) {
            List<String> filteredStrings = new ArrayList<String>();
            for (String s : getString(mStringAccumulator).split(sSpaceRegex)) {
                if (s != null && !s.isEmpty()) {
                    filteredStrings.add(s.trim());
                }
            }
            return filteredStrings;
        }

        private static Boolean getBool(StringBuilder s) {
            return equals(s, "true") || equals(s, "1");
        }

        private static double getDouble(StringBuilder stringAccumulator) {
            return Double.parseDouble(getString(stringAccumulator));
        }

        private static String getString(StringBuilder s) {
            return s.toString().trim();
        }

        private static boolean equals(StringBuilder s, String t) {
            int start = 0;
            int length = s.length();
            while (start < length && Character.isWhitespace(s.charAt(start))) {
                start++;
            }
            if (start == length) {
                return t.isEmpty();
            }

            int end = length;
            while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
                end--;
            }

            if (t.length() != (end - start)) {
                return false;
            }

            for (int i = 0, n = t.length(), j = start; i < n; i++, j++) {
                if (Character.toLowerCase(s.charAt(j)) != Character.toLowerCase(t.charAt(i))) {
                    return false;
                }
            }

            return true;
        }

        private static int getInteger(StringBuilder stringAccumulator) {
            return Integer.parseInt(getString(stringAccumulator));
        }
    }

    private static final SAXParserFactory sParserFactory;

    static {
        sParserFactory = SAXParserFactory.newInstance();
        sParserFactory.setNamespaceAware(true);
    }

    @NotNull
    public static List<Device> parse(@NotNull File devicesFile)
            throws SAXException, ParserConfigurationException, IOException {
        InputStream stream = null;
        try {
            stream = new FileInputStream(devicesFile);
            return parseImpl(stream, devicesFile.getAbsoluteFile().getParentFile());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignore) {}
            }
        }
    }

    @NotNull
    public static List<Device> parse(@NotNull InputStream devices)
            throws SAXException, IOException, ParserConfigurationException {
        return parseImpl(devices, null);
    }

    @NotNull
    private static List<Device> parseImpl(@NotNull InputStream devices, @Nullable File parentDir)
            throws SAXException, IOException, ParserConfigurationException {
        if (!devices.markSupported()) {
            devices = new BufferedInputStream(devices);
        }
        devices.mark(500000);
        int version = DeviceSchema.getXmlSchemaVersion(devices);
        SAXParser parser = getParser(version);
        DeviceHandler dHandler = new DeviceHandler(parentDir);
        devices.reset();
        parser.parse(devices, dHandler);
        return dHandler.getDevices();
    }

    @NotNull
    private static SAXParser getParser(int version) throws ParserConfigurationException, SAXException {
        Schema schema = DeviceSchema.getSchema(version);
        if (schema != null) {
            sParserFactory.setSchema(schema);
        }
        return sParserFactory.newSAXParser();
    }
}
