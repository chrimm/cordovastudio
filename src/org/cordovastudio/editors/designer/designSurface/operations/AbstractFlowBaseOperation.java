/*
 * Copyright 2000-2012 JetBrains s.r.o.
 * (Original as of com.intellij.designer.designSurface.AbstractFlowBaseOperation and com.intellij.android.designer.designSurface.layout.AbstractFlowBaseOperation)
 *
 * Copyright (C) 2014 Christoffer T. Timm
 * Changes:
 *  – Merged com.intellij.designer.designSurface.AbstractFlowBaseOperation and
 *    com.intellij.android.designer.designSurface.layout.AbstractFlowBaseOperation
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

package org.cordovastudio.editors.designer.designSurface.operations;

import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.feedbacks.LineInsertFeedback;
import org.cordovastudio.editors.designer.designSurface.feedbacks.RectangleFeedback;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Alexander Lobas
 */
public abstract class AbstractFlowBaseOperation extends AbstractEditOperation {
    protected final boolean myHorizontal;
    protected RectangleFeedback myFirstInsertFeedback;
    protected LineInsertFeedback myInsertFeedback;
    protected Rectangle myBounds;
    protected RadComponent myChildTarget;
    protected boolean myInsertBefore;

    public AbstractFlowBaseOperation(RadComponent container, OperationContext context, boolean horizontal) {
        super(container, context);
        myHorizontal = horizontal;
    }

    protected void createFeedback() {
        if (myFirstInsertFeedback == null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            myBounds = myContainer.getBounds(layer);

            createFirstInsertFeedback();
            createInsertFeedback();

            if (getChildren().isEmpty()) {
                layer.add(myFirstInsertFeedback);
            } else {
                layer.add(myInsertFeedback);
            }
            layer.repaint();
        }
    }

    protected java.util.List<RadComponent> getChildren() {
        return myContainer.getChildren();
    }

    @Override
    public void showFeedback() {
        createFeedback();

        java.util.List<RadComponent> children = getChildren();
        if (!children.isEmpty()) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            Point location = myContext.getLocation();
            myChildTarget = null;

            if (myHorizontal) {
                handleHorizontal(children, layer, location);
            } else {
                handleVertical(children, layer, location);
            }
            if (myChildTarget == null) {
                myChildTarget = getSideChildTarget();
            }

            Rectangle targetBounds = getBounds(myChildTarget, layer);
            myInsertBefore = myHorizontal ? location.x < targetBounds.getCenterX() : location.y < targetBounds.getCenterY();
            setInsertFeedbackBounds(targetBounds);

            layer.repaint();
        }
    }

    protected void handleHorizontal(java.util.List<RadComponent> children, FeedbackLayer layer, Point location) {
        for (RadComponent child : children) {
            Rectangle childBounds = getBounds(child, layer);
            if (childBounds.x <= location.x && location.x <= childBounds.getMaxX()) {
                myChildTarget = child;
                break;
            }
        }
    }

    protected void handleVertical(java.util.List<RadComponent> children, FeedbackLayer layer, Point location) {
        for (RadComponent child : children) {
            Rectangle childBounds = getBounds(child, layer);
            if (childBounds.y <= location.y && location.y <= childBounds.getMaxY()) {
                myChildTarget = child;
                break;
            }
        }
    }

    protected void setInsertFeedbackBounds(Rectangle targetBounds) {
        if (myHorizontal) {
            if (myInsertBefore) {
                myInsertFeedback.setLocation(targetBounds.x, myBounds.y);
            } else {
                myInsertFeedback.setLocation(targetBounds.x + targetBounds.width, myBounds.y);
            }
        } else {
            if (myInsertBefore) {
                myInsertFeedback.setLocation(myBounds.x, targetBounds.y);
            } else {
                myInsertFeedback.setLocation(myBounds.x, targetBounds.y + targetBounds.height);
            }
        }
    }

    protected Rectangle getBounds(RadComponent component, FeedbackLayer layer) {
        return component.getBounds(layer);
    }

    private RadComponent getSideChildTarget() {
        Point location = myContext.getLocation();
        java.util.List<RadComponent> children = getChildren();
        RadComponent lastChild = children.get(children.size() - 1);
        Rectangle childBounds = lastChild.getBounds(myContext.getArea().getFeedbackLayer());

        if (myHorizontal) {
            if (location.x >= childBounds.getMaxX()) {
                return lastChild;
            }
            return children.get(0);
        }
        if (location.y >= childBounds.getMaxY()) {
            return lastChild;
        }
        return children.get(0);
    }

    @Override
    public void eraseFeedback() {
        if (myFirstInsertFeedback != null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            layer.remove(myFirstInsertFeedback);
            layer.remove(myInsertFeedback);
            layer.repaint();
            myFirstInsertFeedback = null;
            myInsertFeedback = null;
        }
    }

    @Override
    public boolean canExecute() {
        if (myContext.isMove()) {
            if (myComponents.contains(myChildTarget)) {
                return false;
            }

            java.util.List<RadComponent> children = getChildren();
            int index = children.indexOf(myChildTarget) + (myInsertBefore ? -1 : 1);
            if (0 <= index && index < children.size()) {
                return !myComponents.contains(children.get(index));
            }
        }
        return true;
    }

    @Override
    public void execute() throws Exception {
        if (myChildTarget == null || myInsertBefore) {
            execute(myChildTarget);
        } else {
            java.util.List<RadComponent> children = getChildren();
            int index = children.indexOf(myChildTarget) + 1;
            if (index < children.size()) {
                execute(children.get(index));
            } else {
                execute(null);
            }
        }
    }

    protected abstract void execute(@Nullable RadComponent insertBefore) throws Exception;


    protected void createInsertFeedback() {
        // TODO: Replace this with a proper DrawingStyle-based feedback
        myInsertFeedback = new LineInsertFeedback(new DrawingStyle(Color.GREEN, new BasicStroke(1.0f)), !myHorizontal);
        myInsertFeedback.size(myBounds.width, myBounds.height);
    }

    protected void createFirstInsertFeedback() {
        // TODO: Replace this with a proper DrawingStyle feedback
        myFirstInsertFeedback = new RectangleFeedback(new DrawingStyle(Color.GREEN, new BasicStroke(2.0f)));
        myFirstInsertFeedback.setBounds(myBounds);
    }
}