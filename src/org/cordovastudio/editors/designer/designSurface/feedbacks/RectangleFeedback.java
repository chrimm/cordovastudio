/*
 * Copyright (C) 2013 The Android Open Source Project
 * (Original as of com.intellij.designer.designSurface.feedbacks.RectangleFeedback and com.intellij.android.designer.designSurface.graphics.RectangleFeedback)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.designer.designSurface.feedbacks.RectangleFeedback and com.intellij.android.designer.designSurface.graphics.RectangleFeedback
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

import javax.swing.*;
import java.awt.*;

public class RectangleFeedback extends JComponent {
    private final Color myColor;
    protected final int myLine;
    private final DrawingStyle myStyle;

    public RectangleFeedback(DrawingStyle style) {
        myColor = Color.RED;
        myLine = 1;
        myStyle = style;
    }

    protected void paintFeedback(Graphics g) {
        Dimension size = getSize();
        DesignerGraphics.drawRect(myStyle, g, 0, 0, size.width, size.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(myColor);

        paintFeedback(g);
    }
}