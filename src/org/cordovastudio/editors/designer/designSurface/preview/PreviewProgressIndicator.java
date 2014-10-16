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

package org.cordovastudio.editors.designer.designSurface.preview;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.util.ProgressIndicatorBase;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author Eugene.Kudelevsky
*/
class PreviewProgressIndicator extends ProgressIndicatorBase {
  private final Object myLock = new Object();
  private final int myDelay;
  private @NotNull
  final PreviewToolWindowForm myForm;

  PreviewProgressIndicator(@NotNull PreviewToolWindowForm form, int delay) {
    myDelay = delay;
    myForm = form;
  }

  @Override
  public void start() {
    super.start();
    UIUtil.invokeLaterIfNeeded(new Runnable() {
        @Override
        public void run() {
            final Timer timer = UIUtil.createNamedTimer("Android rendering progress timer", myDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    synchronized (myLock) {
                        if (isRunning()) {
                            myForm.getPreviewPanel().registerIndicator(PreviewProgressIndicator.this);
                        }
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    });
  }

  @Override
  public void stop() {
    synchronized (myLock) {
      super.stop();
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        @Override
        public void run() {
          myForm.getPreviewPanel().unregisterIndicator(PreviewProgressIndicator.this);
        }
      });
    }
  }
}
