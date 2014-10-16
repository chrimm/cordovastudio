/*
 * Copyright (C) 2009 The Android Open Source Project
 * (original as of com.android.sdklib.AndroidVersion)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Complete overhaul to represent version info of Cordova projects
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

package org.cordovastudio;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CordovaVersion implements Comparable<CordovaVersion> {

    public static final CordovaVersion DEFAULT = new CordovaVersion(3, 0, 0, null);

    private int myMajorVersion;
    private int myMinorVersion;
    private int myRevision;
    private String myBuild;

    /**
     * Thrown when an {@link CordovaVersion} object could not be created.
     * @see CordovaVersion#CordovaVersion
     */
    public static final class CordovaVersionException extends Exception {
        private static final long serialVersionUID = 1L;

        CordovaVersionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Creates an {@link CordovaVersion} from a string
     *
     * @param versionString
     * @throws CordovaVersionException if the input isn't a valid version string.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public CordovaVersion(@NotNull String versionString) throws CordovaVersionException {
        myMajorVersion = -1;
        myMinorVersion = -1;
        myRevision     = -1;
        myBuild        = "";

        try {
            String[] versionAndBuildArray = versionString.split("-", 1);
            String[] versionArray = versionAndBuildArray[0].split(".");

            myMajorVersion = Integer.parseInt(versionArray[0]);
            myMinorVersion = (versionArray.length > 1) ? Integer.parseInt(versionArray[1]) : 0;
            myRevision = (versionArray.length > 2) ? Integer.parseInt(versionArray[2]) : 0;

            if(versionAndBuildArray.length > 1) {
                myBuild = versionAndBuildArray[1];
            }

        } catch (NumberFormatException e) {
            throw new CordovaVersionException("Invalid Cordova version string '" + versionString + "'. Reason: " + e.getMessage(), null);
        }

        if (myMajorVersion < 0 || myMinorVersion < 0 || myRevision < 0) {
            throw new CordovaVersionException("Invalid Cordova version string " + versionString, null);
        }
    }

    /**
     * Creates an {@link CordovaVersion}
     *
     * @param majorVersion The Major version number
     * @param minorVersion The Minor version number
     * @param revision The Revision number
     * @param buildString An optional string to represent the build version
     * @throws CordovaVersionException if the input isn't a valid version string.
     * @author Christoffer T. Timm <kontakt@christoffertimm.de>
     */
    public CordovaVersion(int majorVersion, int minorVersion, int revision, @Nullable String buildString) {
        myMajorVersion = majorVersion;
        myMinorVersion = minorVersion;
        myRevision     = revision;
        myBuild        = buildString != null ? buildString : "";
    }

    public int getMajorVersion() {
        return myMajorVersion;
    }

    public int getMinorVersion() {
        return myMinorVersion;
    }

    public int getRevision() {
        return myRevision;
    }

    public String getBuild() {
        return myBuild;
    }

    public String toString(boolean appendBuildString) {
        StringBuilder versionString = new StringBuilder();
        versionString.append(myMajorVersion).append(".");
        versionString.append(myMinorVersion).append(".");
        versionString.append(myRevision);

        if(appendBuildString) {
            versionString.append("-").append(myBuild);
        }

        return versionString.toString();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CordovaVersion) {

            CordovaVersion version = (CordovaVersion)o;

            return myMajorVersion       == version.getMajorVersion()
                    && myMinorVersion   == version.getMinorVersion()
                    && myRevision       == version.getRevision()
                    && myBuild          == version.getBuild();
        } else if (o instanceof String) {
            return this.toString().equals(o.toString());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@NotNull CordovaVersion o) {
        if(equals(o))
            return 0;

        if(myMajorVersion != o.getMajorVersion())
            return myMajorVersion - o.getMajorVersion();

        if(myMinorVersion != o.getMinorVersion())
            return myMinorVersion - o.getMinorVersion();

        if(myRevision != o.getRevision())
            return myRevision - o.getRevision();

        /*
         * If we arrive at this position, the only difference is the Build string.
         * We can only compare it as string, as we don't know how it's formatted.
         */

        return myBuild.compareTo(o.getBuild());
    }

    public boolean isGreaterOrEqualThan(String versionString) {
        try {
            CordovaVersion version = new CordovaVersion(versionString);
            return isGreaterOrEqualThan(version);
        } catch (CordovaVersionException e) {
            return false;
        }
    }

    public boolean isGreaterOrEqualThan(CordovaVersion version) {
        return compareTo(version) >= 0;
    }

}
