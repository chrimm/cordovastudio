/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.intellij.designer.designSurface.feedbacks.InsertFeedback and com.intellij.android.designer.designSurface.graphics.InsertFeedback
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
package org.cordovastudio.editors.designer.designSurface.feedbacks;

import org.cordovastudio.editors.designer.designSurface.DesignerGraphics;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;

import java.awt.*;

public class InsertFeedback extends LineInsertFeedback {
    protected boolean myCross;
    private final DrawingStyle myStyle;

    public InsertFeedback(DrawingStyle style) {
        super(style, false);

        myStyle = style;
    }

    public void cross(int xCenter, int yCenter, int size) {
        myCross = true;
        setBounds(xCenter - size - 1, yCenter - size - 1, 2 * size + 3, 2 * size + 3);
        setVisible(true);
    }

    @Override
    public void horizontal(int x, int y, int width) {
        myCross = false;
        super.horizontal(x, y, width);
    }

    @Override
    public void vertical(int x, int y, int height) {
        myCross = false;
        super.vertical(x, y, height);
    }

  @Override
  protected void paintLines(Graphics g) {
    if (myCross) {
      int size = getWidth();
      DesignerGraphics.drawCross(myStyle, g, size);
    }
    else {
      Dimension size = getSize();
      if (myHorizontal) {
        paintHorizontal(g, size);
      }
      else {
        paintVertical(g, size);
      }
    }
  }

  @Override
  protected void paintHorizontal(Graphics g, Dimension size) {
    int lineWidth = myStyle.getLineWidth();
    int middle = lineWidth / 2;
    DesignerGraphics.drawLine(myStyle, g, 0, middle, size.width, middle);
  }

  @Override
  protected void paintVertical(Graphics g, Dimension size) {
    int lineWidth = myStyle.getLineWidth();
    int middle = lineWidth / 2;
    DesignerGraphics.drawLine(myStyle, g, middle, 0, middle, size.height);
  }}
