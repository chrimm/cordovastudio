/*
 * Copyright (C) 2012 The Android Open Source Project
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

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Instances of this class contain the specifications for a device. Use the
 * {@link Builder} class to construct a Device object, or the
 * {@link DeviceParser} if constructing device objects from XML conforming to
 * the {@link DeviceSchema} standards.
 */
public final class Device {
    /**
     * Name of the device
     */
    @NotNull
    private final String myName;

    /**
     * Name of the device to use in UI contexts
     */
    @NotNull
    private final String myDisplayName;

    /**
     * Manufacturer of the device
     */
    @NotNull
    private final String myManufacturer;

    /**
     * FormFactor of the device
     */
    @NotNull
    private final FormFactor myFormFactor;

    /**
     * Is the display round (or rectangle otherwise) is it maybe on Wear FormFactors
     */
    private final boolean myIsRound;

    /**
     * A list of phone states (landscape, portrait with keyboard out, etc.)
     */
    @NotNull
    private final List<State> myStates;

    /**
     * Meta information such as icon files and device frames
     */
    @NotNull
    private final Artwork myArtwork;

    /**
     * Default state of the device
     */
    @NotNull
    private final State myDefaultState;

    @NotNull
    private Map<ScreenOrientation, Dimension> myScreenSizes;

    /**
     * Returns the name of the {@link Device}.
     *
     * @return The name of the {@link Device}.
     */
    @NotNull
    public String getName() {
        return myName;
    }

    /**
     * Returns the user visible name of the {@link Device}. This is intended to be displayed by the
     * user and can vary over time. For a stable internal name of the device, use {@link #getName}
     * instead.
     *
     * @return The name of the {@link Device}.
     */
    @NotNull
    public String getDisplayName() {
        return myDisplayName;
    }

    /**
     * Returns the manufacturer of the {@link Device}.
     *
     * @return The name of the manufacturer of the {@link Device}.
     */
    @NotNull
    public String getManufacturer() {
        return myManufacturer;
    }

    /**
     * Returns the form factor of the {@link Device}.
     *
     * @return The name of the manufacturer of the {@link Device}.
     */
    @NotNull
    public FormFactor getFormFactor() {
        return myFormFactor;
    }

    /**
     * Return whether the Device's display is round (or rectangle otherwise)
     */
    public boolean isRound() {
        return myIsRound;
    }

    /**
     * Returns all of the {@link State}s the {@link Device} can be in.
     *
     * @return A list of all the {@link State}s.
     */
    @NotNull
    public List<State> getAllStates() {
        return myStates;
    }

    /**
     * Returns the {@link Artwork} object for the device, which contains meta
     * information about the device, such as the device artwork and the icon.
     *
     * @return The {@link Artwork} object for the {@link Device}.
     */
    @NotNull
    public Artwork getArtwork() {
        return myArtwork;
    }

    /**
     * Returns the default {@link State} of the {@link Device}.
     *
     * @return The default {@link State} of the {@link Device}.
     */
    @NotNull
    public State getDefaultState() {
        return myDefaultState;
    }

    /**
     * Returns the state of the device with the given name.
     *
     * @param name The name of the state requested.
     * @return The State object requested or null if there's no state with the
     * given name.
     */
    @Nullable
    public State getState(String name) {
        for (State s : getAllStates()) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns the size of the screen for the specified device orientation.
     *
     * @param orientation The device orientation for which the screen size shall be returned.
     * @return The size of the screen for the specified device orientation.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @SuppressWarnings("SuspiciousNameCombination") // Deliberately swapping orientations
    @Nullable
    public Dimension getScreenSize(@NotNull ScreenOrientation orientation) {
        return myScreenSizes.get(orientation);
    }

    /**
     * Returns the size of the screen for the default orientation.
     *
     * @return The size of the screen for the default orientation.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Nullable
    public Dimension getScreenSize() {
        return getScreenSize(myDefaultState.getOrientation());
    }

    /**
     * Returns screen sizes.
     *
     * @return A {@linkplain Map} of ScreenSizes and their according {@linkplain ScreenOrientation}s.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    @Nullable
    public Map<ScreenOrientation, Dimension> getAllScreenSizes() {
        return myScreenSizes;
    }

    /**
     * Returns whether this Device is a tablet.
     *
     * @return {@code true}, iff this Device is a tablet, {@code false} otherwise.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public boolean isTablet() {
        boolean isTablet = false;
        if (myName.toLowerCase().contains("tab") || myName.toLowerCase().contains("pad")) { // For example "10.1in WXGA (Tablet)"
            isTablet = true;
        } else {
            if (getScreenDiagonalLength() >= 6.95) { // Arbitrary
                isTablet = true;
            }
        }
        return isTablet;
    }

    /**
     * Return the diagonal length of the Device's screen
     *
     * @return The diagonal length of the Device's screen
     */
    @NotNull
    public Double getScreenDiagonalLength() {
        ScreenOrientation orientation = myDefaultState.getOrientation();

        if (orientation != null) {
            Dimension screen = myScreenSizes.get(orientation);

            if (screen != null) {
                return Math.sqrt(screen.width ^ 2 + screen.height ^ 2);
            }
        } else {
            /* If no default orientation found, try all possible orientations
             * and use the first one for which a screen size is found
             */
            for (ScreenOrientation so : ScreenOrientation.values()) {
                Dimension screen = myScreenSizes.get(so);
                if(screen != null) {
                    return Math.sqrt(screen.width ^ 2 + screen.height ^ 2);
                }
            }
        }

        return 0.00;
    }

    public static class Builder {
        private String mName;
        private String mDisplayName;
        private String mManufacturer;
        private FormFactor mFormFactor;
        private Map<ScreenOrientation, Dimension> mScreenSizes;
        private boolean mIsRound;
        private final List<State> mStates = new ArrayList<State>();
        private Artwork mArtwork;
        private State mDefaultState;

        public Builder() {
        }

        public Builder(Device d) {
            mName = d.getName();
            mDisplayName = d.getDisplayName();
            mManufacturer = d.getManufacturer();
            mFormFactor = d.getFormFactor();
            mScreenSizes = d.getAllScreenSizes();
            mIsRound = d.isRound();
            for (State s : d.getAllStates()) {
                mStates.add(s.deepCopy());
            }
            mStates.addAll(d.getAllStates());
            mArtwork = d.getArtwork();
            mDefaultState = d.getDefaultState();
        }

        public void setName(@NotNull String name) {
            mName = name;
        }

        public void setDisplayName(@NotNull String name) {
            mDisplayName = name;
        }

        public void setManufacturer(@NotNull String manufacturer) {
            mManufacturer = manufacturer;
        }

        public void setFormFactor(@NotNull FormFactor formFactor) {
            mFormFactor = formFactor;
        }

        public void addScreenSize(@NotNull ScreenOrientation screenOrientation, @NotNull Dimension screenSize) {
            if(mScreenSizes == null) {
                mScreenSizes = new HashMap<ScreenOrientation, Dimension>();
            }

            if(mScreenSizes.containsKey(screenOrientation)) {
                if(mScreenSizes.get(screenOrientation).equals(screenSize)) {
                    /* ScreenSize already exists in Map, just return */
                    return;
                } else {
                    /* ScreenSize clashes with existing one --> error */
                    throw generateBuildException("New ScreenSize clashes with existing ScreenSize for this ScreenOrientation (" + screenOrientation.toString() + ").");
                }
            } else {
                mScreenSizes.put(screenOrientation, screenSize);
            }
        }

        public void isRound(boolean isRound) {
            mIsRound = isRound;
        }

        public void addState(State state) {
            mStates.add(state);
        }

        public void addAllState(@NotNull Collection<? extends State> states) {
            mStates.addAll(states);
        }

        /**
         * Removes the first {@link State} with the given name
         *
         * @param stateName The name of the {@link State} to remove.
         * @return Whether a {@link State} was removed or not.
         */
        public boolean removeState(@NotNull String stateName) {
            for (int i = 0; i < mStates.size(); i++) {
                if (stateName != null && stateName.equals(mStates.get(i).getName())) {
                    mStates.remove(i);
                    return true;
                }
            }
            return false;
        }

        public void setArtwork(@NotNull Artwork artwork) {
            mArtwork = artwork;
        }

        public Device build() {
            if (mName == null) {
                throw generateBuildException("Device missing name");
            } else if (mDisplayName == null) {
                throw generateBuildException("Device missing display name");
            } else if (mManufacturer == null) {
                throw generateBuildException("Device missing manufacturer");
            } else if (mStates.size() <= 0) {
                throw generateBuildException("Device states not configured");
            }

            if (mArtwork == null) {
                mArtwork = new Artwork();
            }
            for (State s : mStates) {
                if (s.isDefaultState()) {
                    mDefaultState = s;
                    break;
                }
            }
            if (mDefaultState == null) {
                throw generateBuildException("Device missing default state");
            }
            return new Device(this);
        }

        private IllegalStateException generateBuildException(String err) {
            String device = "";
            if (mManufacturer != null) {
                device = mManufacturer + ' ';
            }
            if (mName != null) {
                device += mName;
            } else {
                device = "Unknown " + device + "Device";
            }

            return new IllegalStateException("Error building " + device + ": " + err);
        }
    }

    private Device(Builder b) {
        myName = b.mName;
        myDisplayName = b.mDisplayName;
        myManufacturer = b.mManufacturer;
        myFormFactor = b.mFormFactor;
        myScreenSizes = b.mScreenSizes;
        myIsRound = b.mIsRound;
        myStates = Collections.unmodifiableList(b.mStates);
        myArtwork = b.mArtwork;
        myDefaultState = b.mDefaultState;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        Device d = (Device) o;
        return myName.equals(d.getDisplayName())
                && myManufacturer.equals(d.getManufacturer())
                && myFormFactor.equals(d.getFormFactor())
                && myScreenSizes.equals(d.getAllScreenSizes())
                && myIsRound == d.isRound()
                && myStates.equals(d.getAllStates())
                && myArtwork.equals(d.getArtwork())
                && myDefaultState.equals(d.getDefaultState());
    }

    /**
     * For *internal* usage only. Must not be serialized to disk.
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + myName.hashCode();
        hash = 31 * hash + myManufacturer.hashCode();
        hash = 31 * hash + myFormFactor.hashCode();
        hash = 31 * hash + myScreenSizes.hashCode();
        hash = 31 * hash + (myIsRound ? "true" : "false").hashCode();
        hash = 31 * hash + myStates.hashCode();
        hash = 31 * hash + myArtwork.hashCode();
        hash = 31 * hash + myDefaultState.hashCode();

        return hash;
    }

    /**
     * toString value suitable for debugging only.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Device [myName=");
        sb.append(myName);
        sb.append(", myDisplayName=");
        sb.append(myDisplayName);
        sb.append(", myManufacturer=");
        sb.append(myManufacturer);
        sb.append(", myFormFactor=");
        sb.append(myFormFactor);
        sb.append(", myScreenSizes=");
        sb.append(myScreenSizes);
        sb.append(", myIsRound=");
        sb.append(myIsRound);
        sb.append(", myStates=");
        sb.append(myStates);
        sb.append(", myMeta=");
        sb.append(myArtwork);
        sb.append(", myDefaultState=");
        sb.append(myDefaultState);
        sb.append("]");
        return sb.toString();
    }

    private static Pattern PATTERN = Pattern.compile(
            "(\\d+\\.?\\d*)(?:in|\") (.+?)( \\(.*Nexus.*\\))?"); //$NON-NLS-1$

    /**
     * Returns a "sortable" name for the device -- if a device list is sorted
     * using this sort-aware display name, it will be displayed in an order that
     * is user friendly with devices using names first sorted alphabetically
     * followed by all devices that use a numeric screen size sorted by actual
     * size.
     * <p/>
     * Note that although the name results in a proper sort, it is not a name
     * that you actually want to display to the user.
     * <p/>
     * Extracted from DeviceMenuListener. Modified to remove the leading space
     * insertion as it doesn't render neatly in the avd manager. Instead added
     * the option to add leading zeroes to make the string names sort properly.
     * <p/>
     * Replace "'in'" with '"' (e.g. 2.7" QVGA instead of 2.7in QVGA).
     * Use the same precision for all devices (all but one specify decimals).
     */
    private String getSortableName() {
        String sortableName = myName;
        Matcher matcher = PATTERN.matcher(sortableName);
        if (matcher.matches()) {
            String size = matcher.group(1);
            String n = matcher.group(2);
            int dot = size.indexOf('.');
            if (dot == -1) {
                size = size + ".0";
                dot = size.length() - 2;
            }
            if (dot < 3) {
                // Pad to have at least 3 digits before the dot, for sorting
                // purposes.
                // We can revisit this once we get devices that are more than
                // 999 inches wide.
                size = "000".substring(dot) + size;
            }
            sortableName = size + "\" " + n;
        }

        return sortableName;
    }

    /**
     * Returns a comparator suitable to sort a device list using a sort-aware display name.
     * The list is displayed in an order that is user friendly with devices using names
     * first sorted alphabetically followed by all devices that use a numeric screen size
     * sorted by actual size.
     */
    public static Comparator<Device> getDisplayComparator() {
        return new Comparator<Device>() {
            @Override
            public int compare(Device d1, Device d2) {
                String s1 = d1.getSortableName();
                String s2 = d2.getSortableName();
                if (s1.length() > 1 && s2.length() > 1) {
                    int i1 = Character.isDigit(s1.charAt(0)) ? 1 : 0;
                    int i2 = Character.isDigit(s2.charAt(0)) ? 1 : 0;
                    if (i1 != i2) {
                        return i1 - i2;
                    }
                }
                return s1.compareTo(s2);
            }
        };
    }

}
