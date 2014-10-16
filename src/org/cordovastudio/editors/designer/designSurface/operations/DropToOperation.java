/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.designer.designSurface.DropToOperation and com.intellij.android.designer.designSurface.DropToOperation)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged om.intellij.designer.designSurface.DropToOperation and com.intellij.android.designer.designSurface.DropToOperation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.designer.designSurface.operations;

import com.intellij.ui.JBColor;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.feedbacks.AlphaFeedback;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexander Lobas
 */
public class DropToOperation extends AbstractEditOperation {
    private final Color myColor;
    private JComponent myFeedback;

    public DropToOperation(RadComponent container, OperationContext context) {
        super(container, context);
        myColor = JBColor.GREEN;
    }

    @Override
    public void execute() throws Exception {
        execute(myContext, (RadViewComponent) myContainer, myComponents, null);
    }

    @Override
    public void showFeedback() {
        FeedbackLayer layer = myContext.getArea().getFeedbackLayer();

        if (myFeedback == null) {
            myFeedback = new AlphaFeedback(myColor);
            layer.add(myFeedback);
            myFeedback.setBounds(myContainer.getBounds(layer));
            layer.repaint();
        }
    }

    @Override
    public void eraseFeedback() {
        if (myFeedback != null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            layer.remove(myFeedback);
            layer.repaint();
            myFeedback = null;
        }
    }
}