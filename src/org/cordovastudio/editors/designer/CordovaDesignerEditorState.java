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

package org.cordovastudio.editors.designer;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by cti on 27.08.14.
 */
public class CordovaDesignerEditorState implements FileEditorState{
    private final long myModificationStamp;

    public CordovaDesignerEditorState(VirtualFile file) {
        Document document = FileDocumentManager.getInstance().getCachedDocument(file);
        myModificationStamp = document != null ? document.getModificationStamp() : file.getModificationStamp();
    }

    @Override
    public int hashCode() {
        return (int)(myModificationStamp ^ (myModificationStamp >>> 32));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof CordovaDesignerEditorState) {
            return myModificationStamp == ((CordovaDesignerEditorState)object).myModificationStamp;
        }
        return false;
    }

    @Override
    public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
        return otherState instanceof CordovaDesignerEditorState;
    }
}
