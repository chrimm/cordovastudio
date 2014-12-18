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

package org.cordovastudio.editors.storyboard.model;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by cti on 18.12.14.
 */
public class ViewState extends State {

    @NotNull
    private VirtualFile myFile;

    public ViewState(@NotNull VirtualFile file) {
        myFile = file;
    }

    @NotNull
    public VirtualFile getFile() {
        return myFile;
    }

    public void setFile(@NotNull VirtualFile myFile) {
        this.myFile = myFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewState that = (ViewState) o;

        return myFile.equals(that.getFile());
    }

    @Override
    public int hashCode() {
        return myFile.hashCode();
    }

    @Override
    public String toString() {
        return "ViewState{" + myFile.getName() + '}';
    }
}
