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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;
import org.cordovastudio.editors.designer.designSurface.feedbacks.AlphaFeedback;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;

import java.awt.*;
import java.util.Iterator;

import static org.cordovastudio.GlobalConstants.NS_RESOURCES;

/**
 * @author Alexander Lobas
 */
public class AbsoluteLayoutOperation extends AbstractEditOperation {
    private AlphaFeedback myFeedback;
    private Rectangle myBounds;
    private Point myStartLocation;

    public AbsoluteLayoutOperation(RadComponent container, OperationContext context) {
        super(container, context);
    }

    private void createFeedback() {
        if (myFeedback == null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();

            if (myContext.isCreate() || myContext.isPaste()) {
                myBounds = new Rectangle(0, 0, 64, 32);
            } else {
                Iterator<RadComponent> I = myComponents.iterator();
                myBounds = I.next().getBounds(layer);
                while (I.hasNext()) {
                    myBounds.add(I.next().getBounds(layer));
                }

                if (myBounds.width == 0) {
                    myBounds.width = 64;
                }
                if (myBounds.height == 0) {
                    myBounds.height = 32;
                }

                myStartLocation = myBounds.getLocation();
            }

            myFeedback = new AlphaFeedback(myComponents.size() == 1 ? Color.green : Color.orange);
            myFeedback.setSize(myBounds.width, myBounds.height);

            layer.add(myFeedback);
            layer.repaint();
        }
    }

    @Override
    public void showFeedback() {
        createFeedback();
        Point location = myContext.getLocation();
        Dimension delta = myContext.getSizeDelta();

        if (delta == null || (delta.width == 0 && delta.height == 0) || myComponents.size() > 1) {
            myBounds.x = location.x - myBounds.width / 2;
            myBounds.y = location.y - myBounds.height / 2;
            myFeedback.setLocation(myBounds.x, myBounds.y);
        } else {
            myBounds.width = location.x - myBounds.x;
            myBounds.height = location.y - myBounds.y;
            myFeedback.setSize(myBounds.width, myBounds.height);
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

    @Override
    public void execute() throws Exception {
        if (!myContext.isMove()) {
            super.execute();
        }
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                IEditableArea area = myContext.getArea();
                FeedbackLayer layer = area.getFeedbackLayer();
                Rectangle parentBounds = myContainer.getBounds();

                if (myContext.isCreate() || myContext.isPaste()) {
                    Point location = myContainer.convertPoint(layer, myBounds.x, myBounds.y);
                    Dimension delta = myContext.getSizeDelta();

                    for (RadComponent component : myComponents) {
                        XmlTag tag = ((RadViewComponent) component).getTag();

                        String x = Integer.toString(location.x - parentBounds.x);
                        String y = Integer.toString(location.y - parentBounds.y);
                        tag.setAttribute("layout_x", NS_RESOURCES, x);
                        tag.setAttribute("layout_y", NS_RESOURCES, y);

                        if (delta != null && myComponents.size() == 1) {
                            Rectangle modelBounds = component.toModel(layer, myBounds);
                            if (delta.width > 0) {
                                String width = Integer.toString(modelBounds.width);
                                tag.setAttribute("layout_width", NS_RESOURCES, width);
                            }
                            if (delta.height > 0) {
                                String height = Integer.toString(modelBounds.height);
                                tag.setAttribute("layout_height", NS_RESOURCES, height);
                            }
                        }
                    }
                } else {
                    int moveDeltaX = myBounds.x - myStartLocation.x;
                    int moveDeltaY = myBounds.y - myStartLocation.y;

                    for (RadComponent component : myComponents) {
                        Rectangle bounds = component.getBounds(layer);
                        Point location = component.convertPoint(layer, bounds.x + moveDeltaX, bounds.y + moveDeltaY);
                        XmlTag tag = ((RadViewComponent) component).getTag();

                        String x = Integer.toString(location.x - parentBounds.x);
                        String y = Integer.toString(location.y - parentBounds.y);
                        tag.setAttribute("layout_x", NS_RESOURCES, x);
                        tag.setAttribute("layout_y", NS_RESOURCES, y);
                    }
                }
            }
        });
    }
}