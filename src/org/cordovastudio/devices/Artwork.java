/*
 * Copyright (C) 2012 The Android Open Source Project
 * (original as of com.android.sdklib.devices.Meta)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  - Adopted to device definition for Cordova Studio devices
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


import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;

public class Artwork {
    private File myIcon;
    private File myFramePortrait;
    private File myFrameLandscape;
    private Point myFrameOffsetLandscape;
    private Point myFrameOffsetPortrait;

    public File getIcon() {
        return myIcon;
    }

    public void setIcon(@Nullable File icon) {
        myIcon = icon;
    }

    public boolean hasIcon() {
        return myIcon != null && myIcon.isFile();
    }

    @Nullable
    public File getFramePortrait() {
        return myFramePortrait;
    }
    @Nullable
    public File getFrameLandscape() {
        return myFrameLandscape;
    }

    public void setFramePortrait(@Nullable File frame) {
        myFramePortrait = frame;
    }

    public void setFrameLandscape(@Nullable File frame) {
        myFrameLandscape = frame;
    }

    public boolean hasFramePortait() {
        return myFramePortrait != null && myFramePortrait.isFile();
    }

    public boolean hasFrameLandscape() {
        return myFrameLandscape != null && myFrameLandscape.isFile();
    }

    public boolean hasFrames() {
        return hasFrameLandscape() && hasFramePortait();
    }

    @Nullable
    public Point getFrameOffsetLandscape() {
        return myFrameOffsetLandscape;
    }

    public void setFrameOffsetLandscape(@Nullable Point offset) {
        myFrameOffsetLandscape = offset;
    }

    @Nullable
    public Point getFrameOffsetPortrait() {
        return myFrameOffsetPortrait;
    }

    public void setFrameOffsetPortrait(@Nullable Point offset) {
        myFrameOffsetPortrait = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Artwork)) {
            return false;
        }
        Artwork m = (Artwork) o;

        // Note that any of the fields of either object can be null
        if (myIcon != null && !FileUtil.filesEqual(myIcon, m.getIcon())) {
            return false;
        } else if (m.getIcon() != null && !FileUtil.filesEqual(m.getIcon(), myIcon)) {
            return false;
        }

        if (myFramePortrait != null && !FileUtil.filesEqual(myFramePortrait, m.getFramePortrait())) {
            return false;
        } else if (m.getFramePortrait() != null && !FileUtil.filesEqual(m.getFramePortrait(), myFramePortrait)) {
            return false;
        }

        if (myFrameLandscape != null && !FileUtil.filesEqual(myFrameLandscape, m.getFrameLandscape())) {
            return false;
        } else if (m.getFrameLandscape() != null && !FileUtil.filesEqual(m.getFrameLandscape(), myFrameLandscape)) {
            return false;
        }

        if (myFrameOffsetLandscape != null
                && !myFrameOffsetLandscape.equals(m.getFrameOffsetLandscape())) {
            return false;
        } else if (m.getFrameOffsetLandscape() != null
                && !m.getFrameOffsetLandscape().equals(myFrameOffsetLandscape)) {
            return false;
        }

        if (myFrameOffsetPortrait != null
                && !myFrameOffsetPortrait.equals(m.getFrameOffsetPortrait())) {
            return false;
        } else if (m.getFrameOffsetPortrait() != null
                && !m.getFrameOffsetPortrait().equals(myFrameOffsetPortrait)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (myIcon != null) {
            String path = myIcon.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
        }
        if (myFramePortrait != null) {
            String path = myFramePortrait.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
        }
        if (myFrameLandscape != null) {
            String path = myFrameLandscape.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
        }
        if (myFrameOffsetLandscape != null) {
            hash = 31 * hash + myFrameOffsetLandscape.x;
            hash = 31 * hash + myFrameOffsetLandscape.y;
        }
        if (myFrameOffsetPortrait != null) {
            hash = 31 * hash + myFrameOffsetPortrait.x;
            hash = 31 * hash + myFrameOffsetPortrait.y;
        }
        return hash;
    }
}
