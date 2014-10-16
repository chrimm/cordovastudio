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
import com.intellij.openapi.util.Pair;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.containers.hash.HashSet;
import org.cordovastudio.editors.designer.designSurface.DesignerGraphics;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.feedbacks.TextFeedback;
import org.cordovastudio.editors.designer.model.Gravity;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Set;

import static org.cordovastudio.GlobalConstants.NS_RESOURCES;

/**
 * @author Alexander Lobas
 */
public class FrameLayoutOperation extends AbstractEditOperation {
    private Set<Pair<Gravity, Gravity>> myExcludes = Collections.emptySet();
    private GravityFeedback myFeedback;
    private TextFeedback myTextFeedback;
    private Rectangle myBounds;
    private String myGravity;

    public FrameLayoutOperation(RadComponent container, OperationContext context) {
        super(container, context);

        if (context.isMove()) {
            myExcludes = new HashSet<Pair<Gravity, Gravity>>();

            for (RadComponent component : context.getComponents()) {
                myExcludes.add(Gravity.getSides(component));
            }
        }
    }

    private void createFeedback() {
        if (myFeedback == null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            myBounds = ((RadViewComponent) myContainer).getPaddedBounds(layer);

            myFeedback = new GravityFeedback();
            myFeedback.setBounds(myBounds);
            layer.add(myFeedback);

            myTextFeedback = new TextFeedback();
            myTextFeedback.setBorder(IdeBorderFactory.createEmptyBorder(0, 3, 2, 0));
            layer.add(myTextFeedback);

            layer.repaint();
        }
    }

    @Override
    public void showFeedback() {
        createFeedback();

        Point location = myContext.getLocation();
        Gravity horizontal = calculateHorizontal(myBounds, location);
        Gravity vertical = calculateVertical(myBounds, location);

        if (myContext.isMove() && exclude(horizontal, vertical)) {
            horizontal = vertical = null;
        }

        myFeedback.setGravity(horizontal, vertical);
        configureTextFeedback(myBounds, horizontal, vertical);

        myGravity = Gravity.getValue(horizontal, vertical);
    }

    private void configureTextFeedback(Rectangle bounds, @Nullable Gravity horizontal, @Nullable Gravity vertical) {
        myTextFeedback.clear();

        if (horizontal == null || vertical == null) {
            myTextFeedback.bold("None");
        } else {
            myTextFeedback.bold("[" + vertical.name() + ", " + horizontal.name() + "]");
        }

        myTextFeedback.centerTop(bounds);
    }

    @Override
    public void eraseFeedback() {
        if (myFeedback != null) {
            FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
            layer.remove(myFeedback);
            layer.remove(myTextFeedback);
            layer.repaint();
            myFeedback = null;
            myTextFeedback = null;
        }
    }

    @Nullable
    private static Gravity calculateHorizontal(Rectangle bounds, Point location) {
        if (bounds.width < 10) {
            return Gravity.left;
        }

        Gravity horizontal = null;
        double left = bounds.x + bounds.width / 3.0;
        double right = bounds.x + 2 * bounds.width / 3.0;

        if (location.x < left) {
            horizontal = Gravity.left;
        } else if (left < location.x && location.x < right) {
            horizontal = Gravity.center;
        } else if (location.x > right) {
            horizontal = Gravity.right;
        }

        return horizontal;
    }

    @Nullable
    private static Gravity calculateVertical(Rectangle bounds, Point location) {
        if (bounds.height < 10) {
            return Gravity.top;
        }

        Gravity vertical = null;
        double top = bounds.y + bounds.height / 3.0;
        double bottom = bounds.y + 2 * bounds.height / 3.0;

        if (location.y < top) {
            vertical = Gravity.top;
        } else if (top < location.y && location.y < bottom) {
            vertical = Gravity.center;
        } else if (location.y > bottom) {
            vertical = Gravity.bottom;
        }

        return vertical;
    }

    private boolean exclude(@Nullable Gravity horizontal, @Nullable Gravity vertical) {
        for (Pair<Gravity, Gravity> p : myExcludes) {
            if (p.first == horizontal && p.second == vertical) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canExecute() {
        return myGravity != null;
    }

    @Override
    public void execute() throws Exception {
        super.execute();

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                for (RadComponent component : myComponents) {
                    ((RadViewComponent) component).getTag().setAttribute("layout_gravity", NS_RESOURCES, myGravity);
                }
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // Feedback
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    private static final Gravity[] HORIZONTALS = {Gravity.left, Gravity.center, Gravity.right};
    private static final Gravity[] VERTICALS = {Gravity.top, Gravity.center, Gravity.bottom};

    private class GravityFeedback extends JComponent {
        @Nullable
        private Gravity myHorizontal;
        @Nullable
        private Gravity myVertical;

        public GravityFeedback() {
        }

        public void setGravity(@Nullable Gravity horizontal, @Nullable Gravity vertical) {
            myHorizontal = horizontal;
            myVertical = vertical;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Rectangle bounds = ((RadViewComponent) myContainer).getPaddedBounds(this);
            DesignerGraphics.drawRect(DrawingStyle.DROP_PREVIEW, g, bounds.x, bounds.y, bounds.width, bounds.height);

            for (Gravity h : HORIZONTALS) {
                for (Gravity v : VERTICALS) {
                    boolean selection = (myHorizontal == h && myVertical == v);
                    if (selection || !exclude(h, v)) {
                        paintCell(g, h, v, selection);
                    }
                }
            }
        }

        private boolean paintCell(Graphics g, Gravity horizontal, Gravity vertical, boolean selection) {
            int x = 0;
            int width = (getWidth() - 2) / 3;
            if (horizontal == Gravity.center) {
                x = width + 1;
            } else if (horizontal == Gravity.right) {
                x = getWidth() - width;
            }

            int y = 0;
            int height = (getHeight() - 2) / 3;
            if (vertical == Gravity.center) {
                y = height + 1;
            } else if (vertical == Gravity.bottom) {
                y = getHeight() - height;
            }

            int hSpace = Math.min(5, Math.max(1, getWidth() / 30));
            if (hSpace > 1) {
                x += hSpace;
                width -= 2 * hSpace;
            }

            int vSpace = Math.min(5, Math.max(1, getHeight() / 30));
            if (vSpace > 1) {
                y += vSpace;
                height -= 2 * vSpace;
            }

            if (selection) {
                if (myHorizontal == horizontal && myVertical == vertical) {
                    DesignerGraphics.drawFilledRect(DrawingStyle.DROP_ZONE_ACTIVE, g, x, y, width, height);

                    return true;
                }
            } else {
                DesignerGraphics.drawFilledRect(DrawingStyle.DROP_ZONE, g, x, y, width, height);
            }

            return false;
        }
    }
}