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

public class State {
    private boolean mDefaultState;
    private String mName;
    private String mDescription;
    private ScreenOrientation mOrientation;
    private Boolean mShowStatusbar;

    public boolean isDefaultState() {
        return mDefaultState;
    }

    public void setDefaultState(boolean defaultState) {
        mDefaultState = defaultState;
    }

    @NotNull
    public String getName() {
        return mName;
    }

    public void setName(@NotNull String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public ScreenOrientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(ScreenOrientation orientation) {
        mOrientation = orientation;
    }

    public boolean getStatusbar() {
        return mShowStatusbar;
    }

    public void setStatusbar(boolean showStatusbar) {
        mShowStatusbar = showStatusbar;
    }

    /**
     * Returns a copy of the object that shares no state with it,
     * but is initialized to equivalent values.
     *
     * @return A copy of the object.
     */
    public State deepCopy() {
        State s = new State();
        s.setDefaultState(isDefaultState());
        s.setName(getName());
        s.setDescription(getDescription());
        s.setOrientation(getOrientation());
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        State s = (State) o;
        return mDefaultState == s.isDefaultState()
                && mName.equals(s.getName())
                && mDescription.equals(s.getDescription())
                && mOrientation.equals(s.getOrientation());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (mDefaultState ? 1 : 0);
        if (mName != null) {
            hash = 31 * hash + mName.hashCode();
        }
        if (mDescription != null) {
            hash = 31 * hash + mDescription.hashCode();
        }
        hash = 31 * hash + mOrientation.ordinal();
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State <mDefaultState=");
        sb.append(mDefaultState);
        sb.append(", mName=");
        sb.append(mName);
        sb.append(", mDescription=");
        sb.append(mDescription);
        sb.append(", mOrientation=");
        sb.append(mOrientation);
        sb.append(", mKeyState=");
        sb.append(">");
        return sb.toString();
    }
}
