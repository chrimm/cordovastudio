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
package org.cordovastudio.editors.storyboard;

import com.intellij.ide.dnd.DnDEvent;
import com.intellij.ide.dnd.DnDManager;
import com.intellij.ide.dnd.DnDTarget;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import org.cordovastudio.editors.designer.rendering.RenderedView;
import org.cordovastudio.editors.designer.rendering.ShadowPainter;
import org.cordovastudio.editors.designer.rendering.renderConfiguration.RenderConfiguration;
import org.cordovastudio.editors.storyboard.model.*;
import org.cordovastudio.modules.CordovaFacet;
import org.cordovastudio.utils.CordovaPsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static org.cordovastudio.branding.Colors.CordovaDarkGray;
import static org.cordovastudio.editors.storyboard.Utilities.*;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class StoryboardView extends JComponent {
    private static final Logger LOG = Logger.getInstance(StoryboardView.class);
    public static final org.cordovastudio.editors.storyboard.model.Dimension GAP = new org.cordovastudio.editors.storyboard.model.Dimension(500, 100);
    private static final Color BACKGROUND_COLOR = new JBColor(Gray.get(192), Gray.get(70));
    private static final Color SNAP_GRID_LINE_COLOR_MINOR = new JBColor(Gray.get(180), Gray.get(60));
    private static final Color SNAP_GRID_LINE_COLOR_MIDDLE = new JBColor(Gray.get(170), Gray.get(50));
    private static final Color SNAP_GRID_LINE_COLOR_MAJOR = new JBColor(Gray.get(160), Gray.get(40));

    private static final float ZOOM_FACTOR = 1.1f;

    // Snap grid
    private static final int MINOR_SNAP = 32;
    private static final int MIDDLE_COUNT = 5;
    private static final int MAJOR_COUNT = 10;

    public static final Dimension MINOR_SNAP_GRID = new Dimension(MINOR_SNAP, MINOR_SNAP);
    public static final Dimension MIDDLE_SNAP_GRID = scale(MINOR_SNAP_GRID, MIDDLE_COUNT);
    public static final Dimension MAJOR_SNAP_GRID = scale(MINOR_SNAP_GRID, MAJOR_COUNT);
    public static final int MIN_GRID_LINE_SEPARATION = 8;

    public static final int LINE_WIDTH = 12;
    private static final Point MULTIPLE_DROP_STRIDE = point(MAJOR_SNAP_GRID);
    private static final String ID_PREFIX = "@+id/";
    private static final Color TRANSITION_LINE_COLOR = CordovaDarkGray;
    private static final Condition<Component> SCREENS = instanceOf(CordovaRootComponent.class);
    private static final Condition<Component> EDITORS = not(SCREENS);
    private static final boolean DRAW_DESTINATION_RECTANGLES = false;
    private static final boolean DEBUG = false;

    private final RenderingParameters myRenderingParams;
    private final CordovaStoryboardModel myCordovaStoryboardModel;

    private final Assoc<State, CordovaRootComponent> myStateComponentAssociation = new Assoc<State, CordovaRootComponent>();
    private final Assoc<Transition, Component> myTransitionEditorAssociation = new Assoc<Transition, Component>();

    private boolean myStateCacheIsValid;
    private boolean myTransitionEditorCacheIsValid;
    @NotNull
    private Selections.Selection mySelection = Selections.NULL;
    private Map<State, Map<String, RenderedView>> myLocationToRenderedView = new IdentityHashMap<State, Map<String, RenderedView>>();
    private Image myBackgroundImage;
    private Point myMouseLocation;
    private Transform myTransform = new Transform(1 / 4f);

    // Configuration

    private boolean showRollover = true;
    private boolean mDrawGrid = true;

    /* In projects with one module with an CordovaFacet, return that CordovaFacet. */
    @Nullable
    private static CordovaFacet getCordovaFacet(@NotNull Project project, @NotNull CordovaStoryboardEditor.ErrorHandler handler) {
        CordovaFacet result = null;
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            CordovaFacet facet = CordovaFacet.getInstance(module);
            if (facet == null) {
                continue;
            }
            if (result == null) {
                result = facet;
            } else {
                handler.handleError("", "Sorry, Storyboard does not yet support multiple module projects. ");
                return null;
            }
        }
        return result;
    }

    @Nullable
    public static RenderingParameters getRenderingParams(@NotNull Project project,
                                                         @NotNull VirtualFile file,
                                                         @NotNull CordovaStoryboardEditor.ErrorHandler handler) {
        CordovaFacet facet = getCordovaFacet(project, handler);
        if (facet == null) {
            return null;
        }
        RenderConfiguration configuration = facet.getConfigurationManager().getConfiguration(file);
        return new RenderingParameters(project, configuration, facet);
    }

    public StoryboardView(RenderingParameters renderingParams, CordovaStoryboardModel model) {
        myRenderingParams = renderingParams;
        myCordovaStoryboardModel = model;

        setFocusable(true);
        setLayout(null);

        // Mouse listener
        {
            MouseAdapter mouseListener = new MyMouseListener();
            addMouseListener(mouseListener);
            addMouseMotionListener(mouseListener);
        }

        // Focus listener
        {
            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent focusEvent) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent focusEvent) {
                    repaint();
                }
            });
        }

        // Drag and Drop listener
        {
            final DnDManager dndManager = DnDManager.getInstance();
            dndManager.registerTarget(new MyDnDTarget(), this);
        }

        // Key listeners
        /*
        {
            Action remove = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mySelection.remove();
                    setSelection(Selections.NULL);
                }
            };
            registerKeyBinding(KeyEvent.VK_DELETE, "delete", remove);
            registerKeyBinding(KeyEvent.VK_BACK_SPACE, "backspace", remove);
        }*/

        // Model listener
        {
            myCordovaStoryboardModel.getListeners().add(new Listener<CordovaStoryboardModel.Event>() {
                @Override
                public void notify(@NotNull CordovaStoryboardModel.Event event) {
                    if (DEBUG)
                        System.out.println("NavigationView:: <listener> " + myStateCacheIsValid + " " + myTransitionEditorCacheIsValid);
                    if (event.operandType.isAssignableFrom(State.class)) {
                        myStateCacheIsValid = false;
                    }
                    if (event.operandType.isAssignableFrom(Transition.class)) {
                        myTransitionEditorCacheIsValid = false;
                    }
                    revalidate();
                    repaint();
                }
            });
        }
    }

    @Nullable
    private static RenderedView getRenderedView(CordovaRootComponent c, Point location) {
        return c.getRenderedView(diff(location, c.getLocation()));
    }

    @Nullable
    public Transition getTransition(CordovaRootComponent sourceComponent, @Nullable RenderedView namedSourceLeaf, Point mouseUpLocation) {
        Component destComponent = getComponentAt(mouseUpLocation);
        if (sourceComponent != destComponent) {
            if (destComponent instanceof CordovaRootComponent) {
                CordovaRootComponent destinationRoot = (CordovaRootComponent) destComponent;
                RenderedView endLeaf = getRenderedView(destinationRoot, mouseUpLocation);
                RenderedView namedEndLeaf = getNamedParent(endLeaf);

                Map<CordovaRootComponent, State> rootComponentToState = getStateComponentAssociation().valueToKey;
                Locator sourceLocator = Locator.of(rootComponentToState.get(sourceComponent), getViewId(namedSourceLeaf));
                Locator destinationLocator = Locator.of(rootComponentToState.get(destComponent), getViewId(namedEndLeaf));
                return new Transition("", sourceLocator, destinationLocator);
            }
        }
        return null;
    }

    public static Rectangle getBounds(CordovaRootComponent c, @Nullable RenderedView leaf) {
        if (leaf == null) {
            return c.getBounds();
        }
        Rectangle r = c.transform.getBounds(leaf);
        return new Rectangle(c.getX() + r.x, c.getY() + r.y, r.width, r.height);
    }

    public Rectangle getNamedLeafBoundsAt(Component sourceComponent, Point location) {
        Component destComponent = getComponentAt(location);
        if (sourceComponent != destComponent) {
            if (destComponent instanceof CordovaRootComponent) {
                CordovaRootComponent destinationRoot = (CordovaRootComponent) destComponent;
                RenderedView endLeaf = getRenderedView(destinationRoot, location);
                RenderedView namedEndLeaf = getNamedParent(endLeaf);
                return getBounds(destinationRoot, namedEndLeaf);
            }
        }
        return new Rectangle(location);
    }

    public float getScale() {
        return myTransform.myScale;
    }

    public void setScale(float scale) {
        myTransform = new Transform(scale);
        myBackgroundImage = null;
        for (CordovaRootComponent root : getStateComponentAssociation().keyToValue.values()) {
            root.setScale(scale);
        }
        setPreferredSize();

        revalidate();
        repaint();
    }

    public void zoom(boolean in) {
        setScale(myTransform.myScale * (in ? ZOOM_FACTOR : 1 / ZOOM_FACTOR));
    }

    private Assoc<State, CordovaRootComponent> getStateComponentAssociation() {
        if (!myStateCacheIsValid) {
            syncStateCache(myStateComponentAssociation);
            myStateCacheIsValid = true;
        }
        return myStateComponentAssociation;
    }

    private Assoc<Transition, Component> getTransitionEditorAssociation() {
        if (!myTransitionEditorCacheIsValid) {
            syncTransitionCache(myTransitionEditorAssociation);
            myTransitionEditorCacheIsValid = true;
        }
        return myTransitionEditorAssociation;
    }

    @Nullable
    static String getViewId(@Nullable RenderedView leaf) {
        if (leaf != null) {
            XmlTag tag = leaf.tag;
            if (tag != null) {
                String attributeValue = tag.getAttributeValue("android:id");
                if (attributeValue != null && attributeValue.startsWith(ID_PREFIX)) {
                    return attributeValue.substring(ID_PREFIX.length());
                }
            }
        }
        return null;
    }

    @Nullable
    static RenderedView getNamedParent(@Nullable RenderedView view) {
        while (view != null && getViewId(view) == null) {
            view = view.getParent();
        }
        return view;
    }

    private Map<String, RenderedView> getNameToRenderedView(State state) {
        Map<String, RenderedView> result = myLocationToRenderedView.get(state);
        if (result == null) {
            RenderedView root = getStateComponentAssociation().keyToValue.get(state).getRootView();
            if (root != null) {
                myLocationToRenderedView.put(state, result = createViewNameToRenderedView(root));
            } else {
                return Collections.emptyMap(); // rendering library hasn't loaded, temporarily return an empty map
            }
        }
        return result;
    }

    private static Map<String, RenderedView> createViewNameToRenderedView(@NotNull RenderedView root) {
        final Map<String, RenderedView> result = new HashMap<String, RenderedView>();
        new Object() {
            void walk(RenderedView parent) {
                for (RenderedView child : parent.getChildren()) {
                    String id = getViewId(child);
                    if (id != null) {
                        result.put(id, child);
                    }
                    walk(child);
                }
            }
        }.walk(root);
        return result;
    }

    static void paintLeaf(Graphics g, @Nullable RenderedView leaf, Color color, CordovaRootComponent component) {
        if (leaf != null) {
            Color oldColor = g.getColor();
            g.setColor(color);
            drawRectangle(g, getBounds(component, leaf));
            g.setColor(oldColor);
        }
    }

    private void registerKeyBinding(int keyCode, String name, Action action) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), name);
        getActionMap().put(name, action);
    }

    private void setSelection(@NotNull Selections.Selection selection) {
        mySelection = selection;
        // the re-validate() call shouldn't be necessary but removing it causes orphaned
        // combo-boxes to remain visible (and click-able) after a 'remove' operation
        revalidate();
        repaint();
    }

    private void moveSelection(Point location) {
        mySelection.moveTo(location);
        revalidate();
        repaint();
    }

    private void setMouseLocation(Point mouseLocation) {
        myMouseLocation = mouseLocation;
        if (showRollover) {
            repaint();
        }
    }

    private void finaliseSelectionLocation(Point location) {
        mySelection = mySelection.finaliseSelectionLocation(location);
        revalidate();
        repaint();
    }

  /*
  private List<State> findDestinationsFor(State state, Set<State> exclude) {
    List<State> result = new ArrayList<State>();
    for (Transition transition : myCordovaStoryboardModel) {
      State source = transition.getSource();
      if (source.equals(state)) {
        State destination = transition.getDestination();
        if (!exclude.contains(destination)) {
          result.add(destination);
        }
      }
    }
    return result;
  }
  */

    private void drawGrid(Graphics g, Color c, Dimension modelSize, int width, int height) {
        g.setColor(c);
        Dimension viewSize = myTransform.modelToView(org.cordovastudio.editors.storyboard.model.Dimension.create(modelSize));
        if (viewSize.width < MIN_GRID_LINE_SEPARATION || viewSize.height < MIN_GRID_LINE_SEPARATION) {
            return;
        }
        for (int x = 0; x < myTransform.viewToModelW(width); x += modelSize.width) {
            int vx = myTransform.modelToViewX(x);
            g.drawLine(vx, 0, vx, getHeight());
        }
        for (int y = 0; y < myTransform.viewToModelH(height); y += modelSize.height) {
            int vy = myTransform.modelToViewY(y);
            g.drawLine(0, vy, getWidth(), vy);
        }
    }

    private void drawBackground(Graphics g, int width, int height) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);

        drawGrid(g, SNAP_GRID_LINE_COLOR_MINOR, MINOR_SNAP_GRID, width, height);
        drawGrid(g, SNAP_GRID_LINE_COLOR_MIDDLE, MIDDLE_SNAP_GRID, width, height);
        drawGrid(g, SNAP_GRID_LINE_COLOR_MAJOR, MAJOR_SNAP_GRID, width, height);
    }

    private Image getBackGroundImage() {
        if (myBackgroundImage == null ||
                myBackgroundImage.getWidth(null) != getWidth() ||
                myBackgroundImage.getHeight(null) != getHeight()) {
            myBackgroundImage = UIUtil.createImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            drawBackground(myBackgroundImage.getGraphics(), getWidth(), getHeight());
        }
        return myBackgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw background
        if (mDrawGrid) {
            g.drawImage(getBackGroundImage(), 0, 0, null);
        } else {
            Color tmp = getBackground();
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(tmp);
        }

        // draw component shadows
        for (Component c : getStateComponentAssociation().keyToValue.values()) {
            Rectangle r = c.getBounds();
            ShadowPainter.drawRectangleShadow(g, r.x, r.y, r.width, r.height);
        }
    }

    public static Graphics2D createLineGraphics(Graphics g, int lineWidth) {
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setColor(TRANSITION_LINE_COLOR);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setStroke(new BasicStroke(lineWidth));
        return g2D;
    }

    private static Rectangle getCorner(Point a, int cornerDiameter) {
        int cornerRadius = cornerDiameter / 2;
        return new Rectangle(a.x - cornerRadius, a.y - cornerRadius, cornerDiameter, cornerDiameter);
    }

    private static void drawLine(Graphics g, Point a, Point b) {
        g.drawLine(a.x, a.y, b.x, b.y);
    }

    private static void drawArrow(Graphics g, Point a, Point b, int lineWidth) {
        Utilities.drawArrow(g, a.x, a.y, b.x, b.y, lineWidth);
    }

    private static void drawRectangle(Graphics g, Rectangle r) {
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    private static int x1(Rectangle src) {
        return src.x;
    }

    private static int x2(Rectangle dst) {
        return dst.x + dst.width;
    }

    private static int y1(Rectangle src) {
        return src.y;
    }

    private static int y2(Rectangle dst) {
        return dst.y + dst.height;
    }

    public static class Line {
        public final Point a;
        public final Point b;

        Line(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        Point project(Point p) {
            boolean horizontal = a.x == b.x;
            boolean vertical = a.y == b.y;
            if (!horizontal && !vertical) {
                throw new UnsupportedOperationException();
            }
            return horizontal ? new Point(a.x, p.y) : new Point(p.x, a.y);
        }
    }

    public static Line getMidLine(Rectangle src, Rectangle dst) {
        Point midSrc = centre(src);
        Point midDst = centre(dst);

        int dx = Math.abs(midSrc.x - midDst.x);
        int dy = Math.abs(midSrc.y - midDst.y);
        boolean horizontal = dx >= dy;

        int middle;
        if (horizontal) {
            middle = x1(src) - x2(dst) > 0 ? (x2(dst) + x1(src)) / 2 : (x2(src) + x1(dst)) / 2;
        } else {
            middle = y1(src) - y2(dst) > 0 ? (y2(dst) + y1(src)) / 2 : (y2(src) + y1(dst)) / 2;
        }

        Point a = horizontal ? new Point(middle, midSrc.y) : new Point(midSrc.x, middle);
        Point b = horizontal ? new Point(middle, midDst.y) : new Point(midDst.x, middle);

        return new Line(a, b);
    }

    private Line getMidLine(Transition t) {
        Map<State, CordovaRootComponent> m = getStateComponentAssociation().keyToValue;
        State src = t.getSource().getState();
        State dst = t.getDestination().getState();
        return getMidLine(m.get(src).getBounds(), m.get(dst).getBounds());
    }

    public static Point[] getControlPoints(Rectangle src, Rectangle dst, Line midLine) {
        Point a = midLine.project(centre(src));
        Point b = midLine.project(centre(dst));
        return new Point[]{project(a, src), a, b, project(b, dst)};
    }

    private Point[] getControlPoints(Transition t) {
        return getControlPoints(getBounds(t.getSource()), getBounds(t.getDestination()), getMidLine(t));
    }

    private static int getTurnLength(Point[] points, float scale) {
        int N = points.length;
        int cornerDiameter = (int) (Math.min(MAJOR_SNAP_GRID.width, MAJOR_SNAP_GRID.height) * scale);

        for (int i = 0; i < N - 1; i++) {
            Point a = points[i];
            Point b = points[i + 1];

            int length = (int) length(diff(b, a));
            if (i != 0 && i != N - 2) {
                length /= 2;
            }
            cornerDiameter = Math.min(cornerDiameter, length);
        }
        return cornerDiameter;
    }

    private static void drawCurve(Graphics g, Point[] points, float scale) {
        final int N = points.length;
        final int cornerDiameter = getTurnLength(points, scale);

        boolean horizontal = points[0].x != points[1].x;
        Point previous = points[0];
        for (int i = 1; i < N - 1; i++) {
            Rectangle turn = getCorner(points[i], cornerDiameter);
            Point startTurn = project(previous, turn);
            drawLine(g, previous, startTurn);
            Point endTurn = project(points[i + 1], turn);
            drawCorner(g, startTurn, endTurn, horizontal);
            previous = endTurn;
            horizontal = !horizontal;
        }

        Point endPoint = points[N - 1];
        if (length(diff(previous, endPoint)) > 1) { //
            drawArrow(g, previous, endPoint, (int) (LINE_WIDTH * scale));
        }
    }

    public void drawTransition(Graphics g, Rectangle src, Rectangle dst, Point[] controlPoints) {
        // draw source rect
        drawRectangle(g, src);

        // draw curved 'Manhattan route' from source to destination
        drawCurve(g, controlPoints, myTransform.myScale);

        // draw destination rect
        if (DRAW_DESTINATION_RECTANGLES) {
            Color oldColor = g.getColor();
            g.setColor(JBColor.CYAN);
            drawRectangle(g, dst);
            g.setColor(oldColor);
        }
    }

    private void drawTransition(Graphics g, Transition t) {
        drawTransition(g, getBounds(t.getSource()), getBounds(t.getDestination()), getControlPoints(t));
    }

    public void paintTransitions(Graphics g) {
        for (Transition transition : myCordovaStoryboardModel.getTransitions()) {
            drawTransition(g, transition);
        }
    }

    private static int angle(Point p) {
        //if ((p.x == 0) == (p.y == 0)) {
        //  throw new IllegalArgumentException();
        //}
        return p.x > 0 ? 0 : p.y < 0 ? 90 : p.x < 0 ? 180 : 270;
    }

    private static void drawCorner(Graphics g, Point a, Point b, boolean horizontal) {
        int radiusX = Math.abs(a.x - b.x);
        int radiusY = Math.abs(a.y - b.y);
        Point centre = horizontal ? new Point(a.x, b.y) : new Point(b.x, a.y);
        int startAngle = angle(diff(a, centre));
        int endAngle = angle(diff(b, centre));
        int dangle = endAngle - startAngle;
        int angle = dangle - (Math.abs(dangle) <= 180 ? 0 : 360 * sign(dangle));
        g.drawArc(centre.x - radiusX, centre.y - radiusY, radiusX * 2, radiusY * 2, startAngle, angle);
    }

    private RenderedView getRenderedView(Locator locator) {
        return getNameToRenderedView(locator.getState()).get(locator.getViewName());
    }

    private void paintRollover(Graphics2D lineGraphics) {
        if (myMouseLocation == null || !showRollover) {
            return;
        }
        Component component = getComponentAt(myMouseLocation);
        if (component instanceof CordovaRootComponent) {
            Stroke oldStroke = lineGraphics.getStroke();
            lineGraphics.setStroke(new BasicStroke(1));
            CordovaRootComponent CordovaRootComponent = (CordovaRootComponent) component;
            RenderedView leaf = getRenderedView(CordovaRootComponent, myMouseLocation);
            RenderedView namedLeaf = getNamedParent(leaf);
            paintLeaf(lineGraphics, leaf, JBColor.RED, CordovaRootComponent);
            paintLeaf(lineGraphics, namedLeaf, JBColor.BLUE, CordovaRootComponent);
            lineGraphics.setStroke(oldStroke);
        }
    }

    private void paintSelection(Graphics g) {
        mySelection.paint(g, hasFocus());
        mySelection.paintOver(g);
    }

    private void paintChildren(Graphics g, Condition<Component> condition) {
        Rectangle bounds = new Rectangle();
        for (int i = getComponentCount() - 1; i >= 0; i--) {
            Component child = getComponent(i);
            if (condition.value(child)) {
                child.getBounds(bounds);
                Graphics cg = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
                child.paint(cg);
            }
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        paintChildren(g, SCREENS);
        Graphics2D lineGraphics = createLineGraphics(g, myTransform.modelToViewW(LINE_WIDTH));
        paintTransitions(lineGraphics);
        paintRollover(lineGraphics);
        paintSelection(g);
        paintChildren(g, EDITORS);
    }

    private Rectangle getBounds(Locator source) {
        Map<State, CordovaRootComponent> stateToComponent = getStateComponentAssociation().keyToValue;
        CordovaRootComponent component = stateToComponent.get(source.getState());
        return getBounds(component, getRenderedView(source));
    }

    @Override
    public void doLayout() {
        if (DEBUG) System.out.println("NavigationView: doLayout");
        Map<Transition, Component> transitionToEditor = getTransitionEditorAssociation().keyToValue;

        Map<State, CordovaRootComponent> stateToComponent = getStateComponentAssociation().keyToValue;
        for (State state : stateToComponent.keySet()) {
            CordovaRootComponent root = stateToComponent.get(state);
            root.setLocation(myTransform.modelToView(myCordovaStoryboardModel.getStateToLocation().get(state))/*new Point(50,50)*/);
            root.setSize(root.getPreferredSize());
        }

        for (Transition transition : myCordovaStoryboardModel.getTransitions()) {
            String gesture = transition.getType();
            if (gesture != null) {
                Component editor = transitionToEditor.get(transition);
                if (editor == null) { // if model is changed on another thread we may see null here (with new notification system)
                    continue;
                }
                Dimension preferredSize = editor.getPreferredSize();
                Point[] points = getControlPoints(transition);
                Point location = diff(midPoint(points[1], points[2]), midPoint(preferredSize));
                editor.setLocation(location);
                editor.setSize(preferredSize);
            }
        }
    }

    private <K, V extends Component> void removeLeftovers(Assoc<K, V> assoc, Collection<K> a) {
        for (Map.Entry<K, V> e : new ArrayList<Map.Entry<K, V>>(assoc.keyToValue.entrySet())) {
            K k = e.getKey();
            V v = e.getValue();
            if (!a.contains(k)) {
                assoc.remove(k, v);
                remove(v);
                repaint();
            }
        }
    }

    private JComboBox createEditorFor(final Transition transition) {
        String gesture = transition.getType();
        JComboBox c = new ComboBox(new DefaultComboBoxModel(new Object[]{"press", "swipe"}));
        c.setSelectedItem(gesture);
        c.setForeground(getForeground());
        //c.setBorder(LABEL_BORDER);
        //c.setOpaque(true);
        c.setBackground(BACKGROUND_COLOR);
        c.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                transition.setType((String) itemEvent.getItem());
                myCordovaStoryboardModel.getListeners().notify(CordovaStoryboardModel.Event.update(Transition.class));
            }
        });
        return c;
    }

    private void syncTransitionCache(Assoc<Transition, Component> assoc) {
        if (DEBUG) System.out.println("NavigationView: syncTransitionCache");
        // add anything that is in the model but not in our cache
        for (Transition transition : myCordovaStoryboardModel.getTransitions()) {
            if (!assoc.keyToValue.containsKey(transition)) {
                Component editor = createEditorFor(transition);
                add(editor);
                assoc.add(transition, editor);
            }
        }
        // remove anything that is in our cache but not in the model
        removeLeftovers(assoc, myCordovaStoryboardModel.getTransitions());
    }

    private CordovaRootComponent createRootComponentFor(State state) {
        Module module = myRenderingParams.myFacet.getModule();
        //String resourceName = isMenu ? state.getXmlResourceName() : Analyser.getXMLFileName(module, state.getClassName(), true);
        //PsiFile psiFile = getLayoutXmlFile(isMenu, resourceName, myRenderingParams.myConfiguration, myRenderingParams.myProject);

        PsiFile psiFile = CordovaPsiUtils.getPsiFileSafely(myRenderingParams.getProject(), ((ViewState)state).getFile());

        CordovaRootComponent result = new CordovaRootComponent(myRenderingParams, psiFile, false);
        result.setScale(myTransform.myScale);
        return result;
    }

    private void syncStateCache(Assoc<State, CordovaRootComponent> assoc) {
        if (DEBUG) System.out.println("NavigationView: syncStateCache");
        assoc.clear();
        removeAll();
        //repaint();

        // add anything that is in the model but not in our cache
        for (State state : myCordovaStoryboardModel.getStates()) {
            if (!assoc.keyToValue.containsKey(state)) {
                CordovaRootComponent root = createRootComponentFor(state);
                assoc.add(state, root);
                add(root);
            }
        }

        setPreferredSize();
    }

    private static org.cordovastudio.editors.storyboard.model.Point getMaxLoc(Collection<org.cordovastudio.editors.storyboard.model.Point> locations) {
        int maxX = 0;
        int maxY = 0;
        for (org.cordovastudio.editors.storyboard.model.Point location : locations) {
            maxX = Math.max(maxX, location.x);
            maxY = Math.max(maxY, location.y);
        }
        return new org.cordovastudio.editors.storyboard.model.Point(maxX, maxY);
    }

    private void setPreferredSize() {
        org.cordovastudio.editors.storyboard.model.Dimension size = myRenderingParams.getDeviceScreenSize();
        org.cordovastudio.editors.storyboard.model.Dimension gridSize = new org.cordovastudio.editors.storyboard.model.Dimension(size.width + GAP.width, size.height + GAP.height);
        org.cordovastudio.editors.storyboard.model.Point maxLoc = getMaxLoc(myCordovaStoryboardModel.getStateToLocation().values());
        Dimension max = myTransform.modelToView(new org.cordovastudio.editors.storyboard.model.Dimension(maxLoc.x + gridSize.width, maxLoc.y + gridSize.height));
        setPreferredSize(max);
    }

    private Selections.Selection createSelection(Point mouseDownLocation, boolean shiftDown) {
        Component component = getComponentAt(mouseDownLocation);
        if (component instanceof StoryboardView) {
            return Selections.NULL;
        }
        Transition transition = getTransitionEditorAssociation().valueToKey.get(component);
        if (component instanceof CordovaRootComponent) {
            CordovaRootComponent CordovaRootComponent = (CordovaRootComponent) component;
            //if (!shiftDown) {
                return new Selections.CordovaRootComponentSelection(myCordovaStoryboardModel, CordovaRootComponent, mouseDownLocation, transition,
                        getStateComponentAssociation().valueToKey.get(CordovaRootComponent),
                        myTransform);
            //} else {
                /*
                RenderedView leaf = getRenderedView(CordovaRootComponent, mouseDownLocation);
                return new Selections.RelationSelection(myCordovaStoryboardModel, CordovaRootComponent, mouseDownLocation, getNamedParent(leaf), this);
                */
            //}
        } else {
            return new Selections.ComponentSelection<Component>(myCordovaStoryboardModel, component, transition);
        }
    }

    private class MyMouseListener extends MouseAdapter {

        private void showPopup(MouseEvent e) {
            JPopupMenu menu = new JBPopupMenu();
            JMenuItem editItem = new JBMenuItem("Edit");
            editItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Component child = getComponentAt(e.getPoint());
                    if (child instanceof CordovaRootComponent) {
                        CordovaRootComponent CordovaRootComponent = (CordovaRootComponent) child;
                        CordovaRootComponent.launchLayoutEditor();
                    }
                }
            });
            menu.add(editItem);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
                return;
            }
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            Point location = e.getPoint();
            boolean modified = (e.isShiftDown() || e.isControlDown() || e.isMetaDown());
            setSelection(createSelection(location, modified));
            requestFocus();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            setMouseLocation(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            moveSelection(e.getPoint());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Component child = getComponentAt(e.getPoint());
                if (child instanceof CordovaRootComponent) {
                    CordovaRootComponent CordovaRootComponent = (CordovaRootComponent) child;
                    CordovaRootComponent.launchLayoutEditor();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e);
                return;
            }
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            finaliseSelectionLocation(e.getPoint());
        }
    }

    private class MyDnDTarget implements DnDTarget {
        private int applicableDropCount = 0;

        private void execute(State state, boolean execute) {
            if (!getStateComponentAssociation().keyToValue.containsKey(state)) {
                if (execute) {
                    myCordovaStoryboardModel.addState(state);
                } else {
                    applicableDropCount++;
                }
            }
        }

        private void dropOrPrepareToDrop(DnDEvent anEvent, boolean execute) {
            //TODO: reimplement?
        }

        @Override
        public boolean update(DnDEvent anEvent) {
            applicableDropCount = 0;
            dropOrPrepareToDrop(anEvent, false);
            anEvent.setDropPossible(applicableDropCount > 0);
            return false;
        }

        @Override
        public void drop(DnDEvent anEvent) {
            dropOrPrepareToDrop(anEvent, true);
        }


        @Override
        public void cleanUpOnLeave() {
        }

        @Override
        public void updateDraggedImage(Image image, Point dropPoint, Point imageOffset) {
        }
    }
}
