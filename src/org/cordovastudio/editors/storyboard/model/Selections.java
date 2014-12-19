/*
 * Copyright (C) 2013 The Android Open Source Project
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
package org.cordovastudio.editors.storyboard.model;

import org.cordovastudio.editors.designer.rendering.RenderedView;
import org.cordovastudio.editors.storyboard.StoryboardView;
import org.cordovastudio.editors.storyboard.Transform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.Point;
import java.util.Map;

import static org.cordovastudio.branding.Colors.CordovaIceBlue;
import static org.cordovastudio.editors.storyboard.Utilities.*;

public class Selections {
    private static final Color SELECTION_COLOR = CordovaIceBlue;
    private static final int SELECTION_RECTANGLE_LINE_WIDTH = 4;

    public static Selection NULL = new EmptySelection();

    public abstract static class Selection {

        public abstract void moveTo(Point location);

        public abstract Selection finaliseSelectionLocation(Point location);

        public abstract void paint(Graphics g, boolean hasFocus);

        public abstract void paintOver(Graphics g);

        public abstract void remove();
    }

    private static class EmptySelection extends Selection {
        @Override
        public void moveTo(Point location) {
        }

        @Override
        public void paint(Graphics g, boolean hasFocus) {
        }

        @Override
        public void paintOver(Graphics g) {
        }

        @Override
        public Selection finaliseSelectionLocation(Point location) {
            return this;
        }

        @Override
        public void remove() {
        }
    }

    public static class ComponentSelection<T extends Component> extends Selection {
        protected final T myComponent;
        protected final Transition myTransition;
        protected final CordovaStoryboardModel myNavigationModel;

        public ComponentSelection(CordovaStoryboardModel navigationModel, T component, Transition transition) {
            myNavigationModel = navigationModel;
            myComponent = component;
            myTransition = transition;
        }

        @Override
        public void moveTo(Point location) {
        }

        @Override
        public void paint(Graphics g, boolean hasFocus) {
            if (hasFocus) {
                Graphics2D g2D = (Graphics2D) g.create();
                g2D.setStroke(new BasicStroke(SELECTION_RECTANGLE_LINE_WIDTH));
                g2D.setColor(SELECTION_COLOR);
                Rectangle selection = myComponent.getBounds();
                int l = SELECTION_RECTANGLE_LINE_WIDTH / 2;
                selection.grow(l, l);
                g2D.drawRect(selection.x, selection.y, selection.width, selection.height);
            }
        }

        @Override
        public void paintOver(Graphics g) {
        }

        @Override
        public Selection finaliseSelectionLocation(Point location) {
            return this;
        }

        @Override
        public void remove() {
            myNavigationModel.remove(myTransition);
        }
    }

    public static class CordovaRootComponentSelection extends ComponentSelection<CordovaRootComponent> {
        protected final Point myMouseDownLocation;
        protected final Point myOrigComponentLocation;
        private final State myState;
        private final Transform myTransform;

        public CordovaRootComponentSelection(CordovaStoryboardModel navigationModel,
                                             CordovaRootComponent component,
                                             Point mouseDownLocation,
                                             Transition transition,
                                             State state,
                                             Transform transform) {
            super(navigationModel, component, transition);
            myMouseDownLocation = mouseDownLocation;
            myOrigComponentLocation = myComponent.getLocation();
            myState = state;
            myTransform = transform;
        }

        private void moveTo(Point location, boolean snap) {
            Point newLocation = add(diff(location, myMouseDownLocation), myOrigComponentLocation);
            if (snap) {
                newLocation = snap(newLocation, myTransform.modelToView(org.cordovastudio.editors.storyboard.model.Dimension.create(StoryboardView.MIDDLE_SNAP_GRID)));
            }
            myComponent.setLocation(newLocation);
            myNavigationModel.getStateToLocation().put(myState, myTransform.viewToModel(newLocation));
            myNavigationModel.getListeners().notify(CordovaStoryboardModel.Event.update(Map.class)); // just avoid State.class, which would trigger reload
        }

        @Override
        public void moveTo(Point location) {
            moveTo(location, false);
        }

        @Override
        public void remove() {
            myNavigationModel.removeState(myState);
        }

        @Override
        public Selection finaliseSelectionLocation(Point location) {
            moveTo(location, true);
            return this;
        }
    }

    public static class RelationSelection extends Selection {
        private final CordovaRootComponent mySourceComponent;
        private final StoryboardView myNavigationEditor;
        private final CordovaStoryboardModel myNavigationModel;
        private final RenderedView myNamedLeaf;
        @NotNull
        private Point myMouseLocation;

        public RelationSelection(CordovaStoryboardModel navigationModel,
                                 @NotNull CordovaRootComponent sourceComponent,
                                 @NotNull Point mouseDownLocation,
                                 @Nullable RenderedView namedLeaf,
                                 @NotNull StoryboardView navigationEditor) {
            myNavigationModel = navigationModel;
            mySourceComponent = sourceComponent;
            myMouseLocation = mouseDownLocation;
            myNamedLeaf = namedLeaf;
            myNavigationEditor = navigationEditor;
        }

        @Override
        public void moveTo(Point location) {
            myMouseLocation = location;
        }

        @Override
        public void paint(Graphics g, boolean hasFocus) {
        }

        @Override
        public void paintOver(Graphics g) {
            int lineWidth = mySourceComponent.transform.modelToViewW(StoryboardView.LINE_WIDTH);
            Graphics2D lineGraphics = StoryboardView.createLineGraphics(g, lineWidth);
            Rectangle sourceBounds = StoryboardView.getBounds(mySourceComponent, myNamedLeaf);
            Rectangle destBounds = myNavigationEditor.getNamedLeafBoundsAt(mySourceComponent, myMouseLocation);
            Rectangle sourceComponentBounds = mySourceComponent.getBounds();
            // if the mouse hasn't left the bounds of the originating component yet, use leaf bounds instead for the midLine calculation
            Rectangle startBounds = sourceComponentBounds.contains(myMouseLocation) ? sourceBounds : sourceComponentBounds;
            StoryboardView.Line midLine = StoryboardView.getMidLine(startBounds, new Rectangle(myMouseLocation));
            Point[] controlPoints = StoryboardView.getControlPoints(sourceBounds, destBounds, midLine);
            myNavigationEditor.drawTransition(lineGraphics, sourceBounds, destBounds, controlPoints);
        }

        @Override
        public Selection finaliseSelectionLocation(Point mouseUpLocation) {
            Transition transition = myNavigationEditor.getTransition(mySourceComponent, myNamedLeaf, mouseUpLocation);
            if (transition != null) {
                myNavigationModel.add(transition);
            }
            return NULL;
        }

        @Override
        public void remove() {
        }
    }
}
