/*
 * Copyright 2000-2012 JetBrains s.r.o.
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

import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.designSurface.feedbacks.LineInsertFeedback;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Alexander Lobas
 */
public abstract class VerticalCaptionFlowBaseOperation<T extends RadViewComponent> extends AbstractFlowBaseOperation {
    protected final T myMainContainer;
    private final IEditableArea myMainArea;
    private LineInsertFeedback myMainInsertFeedback;
    private int myMainXLocation;

    public VerticalCaptionFlowBaseOperation(T mainContainer,
                                            RadComponent container,
                                            OperationContext context,
                                            IEditableArea mainArea) {
        super(container, context, false);
        myMainContainer = mainContainer;
        myMainArea = mainArea;
    }

    @Override
    protected void createFeedback() {
        super.createFeedback();

        if (myMainInsertFeedback == null) {
            FeedbackLayer layer = myMainArea.getFeedbackLayer();

            Rectangle bounds = myMainContainer.getBounds(layer);
            myMainXLocation = bounds.x;

            myMainInsertFeedback = new LineInsertFeedback(new DrawingStyle(Color.GREEN, new BasicStroke(1.0f)), true);
            myMainInsertFeedback.size(getMainFeedbackWidth(layer, myMainXLocation), 0);

            layer.add(myMainInsertFeedback);
            layer.repaint();
        }
    }

    protected int getMainFeedbackWidth(FeedbackLayer layer, int mainXLocation) {
        List<RadComponent> children = myMainContainer.getChildren();
        Rectangle lastChildBounds = children.get(children.size() - 1).getBounds(layer);
        return lastChildBounds.x + lastChildBounds.width - mainXLocation;
    }

    @Override
    public void showFeedback() {
        super.showFeedback();
        Point location = SwingUtilities.convertPoint(myInsertFeedback.getParent(),
                myInsertFeedback.getLocation(),
                myMainArea.getFeedbackLayer());
        myMainInsertFeedback.setLocation(myMainXLocation, location.y);
    }

    @Override
    public void eraseFeedback() {
        super.eraseFeedback();
        if (myMainInsertFeedback != null) {
            FeedbackLayer layer = myMainArea.getFeedbackLayer();
            layer.remove(myMainInsertFeedback);
            layer.repaint();
            myMainInsertFeedback = null;
        }
    }
}