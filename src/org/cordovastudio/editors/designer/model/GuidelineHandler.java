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
package org.cordovastudio.editors.designer.model;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.designer.CordovaDesignerUtils;
import org.cordovastudio.editors.designer.Segment;
import org.cordovastudio.editors.designer.SegmentType;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.modules.CordovaFacet;
import org.jetbrains.annotations.Nullable;
import org.cordovastudio.editors.designer.Insets;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;
import static org.cordovastudio.editors.designer.MarginType.*;
import static org.cordovastudio.editors.designer.designSurface.DrawingStyle.*;
import static org.cordovastudio.GlobalConstants.*;
/**
 * The {@link GuidelineHandler} class keeps track of state related to a guideline operation
 * like move and resize, and performs various constraint computations.
 */
public class GuidelineHandler {
  /**
   * A dependency graph for the relative layout recording constraint relationships
   */
  protected final DependencyGraph myDependencyGraph;

  /**
   * The RelativeLayout we are moving/resizing within
   */
  public final RadViewComponent layout;

  /**
   * The set of nodes being dragged (may be null)
   */
  public Collection<RadViewComponent> myDraggedNodes;

  /**
   * The bounds of the primary child node being dragged
   */
  public Rectangle myBounds;

  /**
   * Whether the left edge is being moved/resized
   */
  protected boolean myMoveLeft;

  /**
   * Whether the right edge is being moved/resized
   */
  protected boolean myMoveRight;

  /**
   * Whether the top edge is being moved/resized
   */
  protected boolean myMoveTop;

  /**
   * Whether the bottom edge is being moved/resized
   */
  protected boolean myMoveBottom;

  /**
   * Whether the drop/move/resize position should be snapped (which can be turned off
   * with a modifier key during the operation)
   */
  protected boolean mySnap = true;

  /**
   * The set of nodes which depend on the currently selected nodes, including
   * transitively, through horizontal constraints (a "horizontal constraint"
   * is a constraint between two horizontal edges)
   */
  public Set<RadViewComponent> myHorizontalDeps;

  /**
   * The set of nodes which depend on the currently selected nodes, including
   * transitively, through vertical constraints (a "vertical constraint"
   * is a constraint between two vertical edges)
   */
  public Set<RadViewComponent> myVerticalDeps;

  /**
   * The current list of constraints which result in a horizontal cycle (if applicable)
   */
  public List<DependencyGraph.Constraint> myHorizontalCycle;

  /**
   * The current list of constraints which result in a vertical cycle (if applicable)
   */
  public List<DependencyGraph.Constraint> myVerticalCycle;

  /**
   * All horizontal segments in the relative layout - top and bottom edges, baseline
   * edges, and top and bottom edges offset by the applicable margins in each direction
   */
  protected final List<Segment> myHorizontalEdges;

  /**
   * All vertical segments in the relative layout - left and right edges, and left and
   * right edges offset by the applicable margins in each direction
   */
  protected final List<Segment> myVerticalEdges;

  /**
   * All center vertical segments in the relative layout. These are kept separate since
   * they only match other center edges.
   */
  protected final List<Segment> myCenterVertEdges;

  /**
   * All center horizontal segments in the relative layout. These are kept separate
   * since they only match other center edges.
   */
  protected final List<Segment> myCenterHorizEdges;

  /**
   * Suggestions for horizontal matches. There could be more than one, but all matches
   * will be equidistant from the current position (as well as in the same direction,
   * which means that you can't have one match 5 pixels to the left and one match 5
   * pixels to the right since it would be impossible to snap to fit with both; you can
   * however have multiple matches all 5 pixels to the left.)
   * <p
   * The best vertical match will be found in {@link #myCurrentTopMatch} or
   * {@link #myCurrentBottomMatch}.
   */
  protected List<Match> myHorizontalSuggestions;

  /**
   * Suggestions for vertical matches.
   * <p
   * The best vertical match will be found in {@link #myCurrentLeftMatch} or
   * {@link #myCurrentRightMatch}.
   */
  protected List<Match> myVerticalSuggestions;

  /**
   * The current match on the left edge, or null if no match or if the left edge is not
   * being moved or resized.
   */
  protected Match myCurrentLeftMatch;

  /**
   * The current match on the top edge, or null if no match or if the top edge is not
   * being moved or resized.
   */
  protected Match myCurrentTopMatch;

  /**
   * The current match on the right edge, or null if no match or if the right edge is
   * not being moved or resized.
   */
  protected Match myCurrentRightMatch;

  /**
   * The current match on the bottom edge, or null if no match or if the bottom edge is
   * not being moved or resized.
   */
  protected Match myCurrentBottomMatch;

  /**
   * The amount of margin to add to the top edge, or 0
   */
  protected int myTopMargin;

  /**
   * The amount of margin to add to the bottom edge, or 0
   */
  protected int myBottomMargin;

  /**
   * The amount of margin to add to the left edge, or 0
   */
  protected int myLeftMargin;

  /**
   * The amount of margin to add to the right edge, or 0
   */
  protected int myRightMargin;

  /**
   * The associated context
   */
  public final OperationContext myContext;

  /**
   * Error message
   */
  protected String myErrorMessage;

  /**
   * Construct a new {@link GuidelineHandler} for the given relative layout.
   *
   * @param layout the RelativeLayout to handle
   */
  GuidelineHandler(RadViewComponent layout, OperationContext context) {
    this.layout = layout;
    myContext = context;

    myHorizontalEdges = new ArrayList<Segment>();
    myVerticalEdges = new ArrayList<Segment>();
    myCenterVertEdges = new ArrayList<Segment>();
    myCenterHorizEdges = new ArrayList<Segment>();
    myDependencyGraph = DependencyGraph.get(layout);
  }

  /**
   * Returns true if the handler has any suggestions to offer
   *
   * @return true if the handler has any suggestions to offer
   */
  public boolean haveSuggestions() {
    return myCurrentLeftMatch != null || myCurrentTopMatch != null || myCurrentRightMatch != null || myCurrentBottomMatch != null;
  }

  /**
   * Returns the closest match.
   *
   * @return the closest match, or null if nothing matched
   */
  @Nullable
  protected Match pickBestMatch(List<Match> matches) {
    int alternatives = matches.size();
    if (alternatives == 0) {
      return null;
    }
    else if (alternatives == 1) {
      return matches.get(0);
    }
    else {
      assert alternatives > 1;
      Collections.sort(matches, new MatchComparator());
      return matches.get(0);
    }
  }

  private boolean checkCycle(Match match, boolean vertical) {
    if (match != null && match.cycle) {
      for (RadViewComponent to : myDraggedNodes) {
        RadViewComponent from = match.edge.node;
        if (from == null) {
          continue;
        }
        assert match.with.node == null || match.with.node == to;
        List<DependencyGraph.Constraint> path = myDependencyGraph.getPathTo(from, to, vertical);
        if (path != null) {
          if (vertical) {
            myVerticalCycle = path;
          }
          else {
            myHorizontalCycle = path;
          }
          String desc = DependencyGraph.Constraint.describePath(path, match.type.name, match.edge.id);

          myErrorMessage = "Constraint creates a cycle: " + desc;
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks for any cycles in the dependencies
   *
   * @return an error message, or null if there are no errors
   */
  protected String checkCycles() {

    myErrorMessage = null;
    myHorizontalCycle = null;
    myVerticalCycle = null;

    // Deliberate short circuit evaluation -- only list the first cycle
    //noinspection StatementWithEmptyBody
    if (checkCycle(myCurrentTopMatch, true /* vertical */) || checkCycle(myCurrentBottomMatch, true)) {
    }

    // Deliberate short circuit evaluation -- only list the first cycle
    //noinspection StatementWithEmptyBody
    if (checkCycle(myCurrentLeftMatch, false) || checkCycle(myCurrentRightMatch, false)) {
    }

    return myErrorMessage;
  }

  /**
   * Records the matchable outside edges for the given node to the potential match list
   */
  protected void addBounds(RadViewComponent node, String id, boolean addHorizontal, boolean addVertical, boolean includePadding) {
    JComponent target = myContext.getArea().getFeedbackLayer();
    Rectangle b = node.getBounds(target);
    Insets m = node.getMargins(target);
    Insets p = includePadding ? node.getPadding(target) : Insets.NONE;

    if (addHorizontal) {
      if (m.top != 0) {
        myHorizontalEdges.add(new Segment(b.y + p.top, b.x + p.left, x2(b) - p.right, node, id, SegmentType.TOP, WITHOUT_MARGIN));
        myHorizontalEdges.add(new Segment(b.y - m.top + p.top, b.x + p.left, x2(b) - p.right, node, id, SegmentType.TOP, WITH_MARGIN));
      }
      else {
        myHorizontalEdges.add(new Segment(b.y + p.top, b.x + p.left, x2(b) - p.right, node, id, SegmentType.TOP, NO_MARGIN));
      }
      if (m.bottom != 0) {
        myHorizontalEdges.add(new Segment(y2(b) - p.bottom, b.x + p.left, x2(b) - p.right, node, id, SegmentType.BOTTOM, WITHOUT_MARGIN));
        myHorizontalEdges.add(
          new Segment(y2(b) + m.bottom - p.bottom, b.x + p.left, x2(b) - p.right, node, id, SegmentType.BOTTOM, WITH_MARGIN));
      }
      else {
        myHorizontalEdges.add(new Segment(y2(b) - p.bottom, b.x + p.left, x2(b) - p.right, node, id, SegmentType.BOTTOM, NO_MARGIN));
      }
    }
    if (addVertical) {
      if (m.left != 0) {
        myVerticalEdges.add(new Segment(b.x + p.left, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.LEFT, WITHOUT_MARGIN));
        myVerticalEdges.add(new Segment(b.x - m.left + p.left, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.LEFT, WITH_MARGIN));
      }
      else {
        myVerticalEdges.add(new Segment(b.x + p.left, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.LEFT, NO_MARGIN));
      }
      if (m.right != 0) {
        myVerticalEdges.add(new Segment(x2(b) - p.right, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.RIGHT, WITHOUT_MARGIN));
        myVerticalEdges.add(new Segment(x2(b) + m.right - p.right, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.RIGHT,
                                        WITH_MARGIN));
      }
      else {
        myVerticalEdges.add(new Segment(x2(b) - p.right, b.y + p.top, y2(b) - p.bottom, node, id, SegmentType.RIGHT, NO_MARGIN));
      }
    }
  }

  /**
   * Records the center edges for the given node to the potential match list
   */
  protected void addCenter(RadViewComponent node, String id) {
    JComponent target = myContext.getArea().getFeedbackLayer();
    Rectangle b = node.getBounds(target);

    myCenterHorizEdges.add(new Segment(centerY(b), b.x, x2(b), node, id, SegmentType.CENTER_HORIZONTAL, NO_MARGIN));
    myCenterVertEdges.add(new Segment(centerX(b), b.y, y2(b), node, id, SegmentType.CENTER_VERTICAL, NO_MARGIN));
  }

  /**
   * Records the baseline edge for the given node to the potential match list
   */
  protected int addBaseLine(RadViewComponent node, String id) {
    int baselineY = node.getBaseline();
    if (baselineY != -1) {
      JComponent target = myContext.getArea().getFeedbackLayer();
      Rectangle b = node.getBounds(target);
      baselineY = node.fromModel(target, new Dimension(0, baselineY)).height;
      myHorizontalEdges.add(new Segment(b.y + baselineY, b.x, x2(b), node, id, SegmentType.BASELINE, NO_MARGIN));
    }

    return baselineY;
  }

  protected void snapVertical(Segment vEdge, int x, Rectangle newBounds) {
    newBounds.x = x;
  }

  protected void snapHorizontal(Segment hEdge, int y, Rectangle newBounds) {
    newBounds.y = y;
  }

  /**
   * Returns whether two edge types are compatible. For example, we only match the
   * center of one object with the center of another.
   *
   * @param edge    the first edge type to compare
   * @param dragged the second edge type to compare the first one with
   * @param delta   the delta between the two edge locations
   * @return true if the two edge types can be compatibly matched
   */
  protected boolean isEdgeTypeCompatible(SegmentType edge, SegmentType dragged, int delta) {

    if (Math.abs(delta) > MAX_MATCH_DISTANCE) {
      if (dragged == SegmentType.LEFT || dragged == SegmentType.TOP) {
        if (delta > 0) {
          return false;
        }
      }
      else {
        if (delta < 0) {
          return false;
        }
      }
    }

    switch (edge) {
      case BOTTOM:
      case TOP:
        return dragged == SegmentType.TOP || dragged == SegmentType.BOTTOM;
      case LEFT:
      case RIGHT:
        return dragged == SegmentType.LEFT || dragged == SegmentType.RIGHT;

      // Center horizontal, center vertical and Baseline only matches the same
      // type, and only within the matching distance -- no margins!
      case BASELINE:
      case CENTER_HORIZONTAL:
      case CENTER_VERTICAL:
        return dragged == edge && Math.abs(delta) < MAX_MATCH_DISTANCE;
      default:
        assert false : edge;
    }
    return false;
  }

  /**
   * Finds the closest matching segments among the given list of edges for the given
   * dragged edge, and returns these as a list of matches
   */
  protected List<Match> findClosest(Segment draggedEdge, List<Segment> edges) {
    List<Match> closest = new ArrayList<Match>();
    addClosest(draggedEdge, edges, closest);
    return closest;
  }

  protected void addClosest(Segment draggedEdge, List<Segment> edges, List<Match> closest) {
    int at = draggedEdge.at;
    int closestDelta = closest.size() > 0 ? closest.get(0).delta : Integer.MAX_VALUE;
    int closestDistance = abs(closestDelta);
    for (Segment edge : edges) {
      assert draggedEdge.edgeType.isHorizontal() == edge.edgeType.isHorizontal();

      int delta = edge.at - at;
      int distance = abs(delta);
      if (distance > closestDistance) {
        continue;
      }

      if (!isEdgeTypeCompatible(edge.edgeType, draggedEdge.edgeType, delta)) {
        continue;
      }

      boolean withParent = edge.node == layout;
      ConstraintType type = ConstraintType.forMatch(withParent, draggedEdge.edgeType, edge.edgeType);
      if (type == null) {
        continue;
      }

      // Ensure that the edge match is compatible; for example, a "below"
      // constraint can only apply to the margin bounds and a "bottom"
      // constraint can only apply to the non-margin bounds.
      if (type.relativeToMargin && edge.marginType == WITHOUT_MARGIN) {
        continue;
      }
      else if (!type.relativeToMargin && edge.marginType == WITH_MARGIN) {
        continue;
      }

      Match match = new Match(edge, draggedEdge, type, delta);

      if (distance < closestDistance) {
        closest.clear();
        closestDistance = distance;
        closestDelta = delta;
      }
      else if (delta * closestDelta < 0) {
        // They have different signs, e.g. the matches are equal but
        // on opposite sides; can't accept them both
        continue;
      }
      closest.add(match);
    }
  }

  protected void clearSuggestions() {
    myErrorMessage = null;
    myHorizontalSuggestions = myVerticalSuggestions = null;
    myCurrentLeftMatch = myCurrentRightMatch = null;
    myCurrentTopMatch = myCurrentBottomMatch = null;
  }

  /**
   * Given a node, apply the suggestions by expressing them as relative layout param
   * values
   *
   * @param n the node to apply constraints to
   */
  public void applyConstraints(RadViewComponent n) {
    // Process each edge separately
    XmlAttribute centerBoth = n.getTag().getAttribute(ATTR_LAYOUT_CENTER_IN_PARENT, CORDOVASTUDIO_URI);
    if (centerBoth != null && VALUE_TRUE.equals(centerBoth.getValue())) {
      centerBoth.delete();

      // If you had a center-in-both-directions attribute, and you're
      // only resizing in one dimension, then leave the other dimension
      // centered, e.g. if you have centerInParent and apply alignLeft,
      // then you should end up with alignLeft and centerVertically
      if (myCurrentTopMatch == null && myCurrentBottomMatch == null) {
        n.getTag().setAttribute(ATTR_LAYOUT_CENTER_VERTICAL, CORDOVASTUDIO_URI, VALUE_TRUE);
      }
      if (myCurrentLeftMatch == null && myCurrentRightMatch == null) {
        n.getTag().setAttribute(ATTR_LAYOUT_CENTER_HORIZONTAL, CORDOVASTUDIO_URI, VALUE_TRUE);
      }
    }

    if (myMoveTop || myMoveBottom || myMoveLeft || myMoveRight) {
      // If you've set an overall margin attribute, we need to decompose it into
      // individual left/right/top/bottom values.
      XmlTag tag = n.getTag();
      XmlAttribute attribute = tag.getAttribute(ATTR_LAYOUT_MARGIN, CORDOVASTUDIO_URI);
      if (attribute != null) {
        String value = attribute.getValue();
        if (value != null) {
          attribute.delete();
          // These may be individually replaced later as we're applying constraints
          tag.setAttribute(ATTR_LAYOUT_MARGIN_LEFT, CORDOVASTUDIO_URI, value);
          tag.setAttribute(ATTR_LAYOUT_MARGIN_RIGHT, CORDOVASTUDIO_URI, value);
          tag.setAttribute(ATTR_LAYOUT_MARGIN_TOP, CORDOVASTUDIO_URI, value);
          tag.setAttribute(ATTR_LAYOUT_MARGIN_BOTTOM, CORDOVASTUDIO_URI, value);
        }
      }
    }

    if (myMoveTop) {
      // Remove top attachments
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_TOP);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_TOP);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_BELOW);

      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_CENTER_VERTICAL);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_BASELINE);

    }

    if (myMoveBottom) {
      // Remove bottom attachments
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_BOTTOM);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_BOTTOM);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ABOVE);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_CENTER_VERTICAL);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_BASELINE);
    }

    if (myMoveLeft) {
      // Remove left attachments
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_LEFT);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_LEFT);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_START);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_START);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_TO_RIGHT_OF);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_CENTER_HORIZONTAL);
    }

    if (myMoveRight) {
      // Remove right attachments
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_RIGHT);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_RIGHT);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_PARENT_END);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_ALIGN_END);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_TO_LEFT_OF);
      clearAttribute(n, CORDOVASTUDIO_URI, ATTR_LAYOUT_CENTER_HORIZONTAL);
    }

    if (myMoveTop && myCurrentTopMatch != null) {
      applyConstraint(n, myCurrentTopMatch.getConstraint(true /* generateId */));
      if (myCurrentTopMatch.type == ConstraintType.ALIGN_BASELINE) {
        // HACK! WORKAROUND! Baseline doesn't provide a new bottom edge for attachments
        String c = myCurrentTopMatch.getConstraint(true);
        c = c.replace(ATTR_LAYOUT_ALIGN_BASELINE, ATTR_LAYOUT_ALIGN_BOTTOM);
        applyConstraint(n, c);
      }
    }

    if (myMoveBottom && myCurrentBottomMatch != null) {
      applyConstraint(n, myCurrentBottomMatch.getConstraint(true));
    }

    if (myMoveLeft && myCurrentLeftMatch != null) {
      String constraint = myCurrentLeftMatch.getConstraint(true);
        applyConstraint(n, constraint);
    }

    if (myMoveRight && myCurrentRightMatch != null) {
      String constraint = myCurrentRightMatch.getConstraint(true);
        applyConstraint(n, constraint);
    }

    if (myMoveLeft) {
      applyMargin(n, ATTR_LAYOUT_MARGIN_LEFT, getLeftMargin());
    }
    if (myMoveRight) {
      applyMargin(n, ATTR_LAYOUT_MARGIN_RIGHT, getRightMargin());
    }
    if (myMoveTop) {
      applyMargin(n, ATTR_LAYOUT_MARGIN_TOP, getTopMargin());
    }
    if (myMoveBottom) {
      applyMargin(n, ATTR_LAYOUT_MARGIN_BOTTOM, getBottomMargin());
    }
  }

  private static void clearAttribute(RadViewComponent view, String uri, String attributeName) {
    XmlAttribute attribute = view.getTag().getAttribute(attributeName, uri);
    if (attribute != null) {
      attribute.delete();
    }
  }

  private static void applyConstraint(RadViewComponent n, String constraint) {
    assert constraint.contains("=") : constraint;
    String name = constraint.substring(0, constraint.indexOf('='));
    String value = constraint.substring(constraint.indexOf('=') + 1);
    n.getTag().setAttribute(name, CORDOVASTUDIO_URI, value);
  }

  private static void applyMargin(RadViewComponent n, String marginAttribute, int margin) {
    if (margin > 0) {
      n.getTag().setAttribute(marginAttribute, CORDOVASTUDIO_URI, String.format(VALUE_N_PX, margin));
    }
    else {
      // Clear out existing margin
      XmlAttribute attribute = n.getTag().getAttribute(marginAttribute, CORDOVASTUDIO_URI);
      if (attribute != null) {
        attribute.delete();
      }
    }
  }

  private static void removeRelativeParams(RadViewComponent node) {
    for (ConstraintType type : ConstraintType.values()) {
      clearAttribute(node, CORDOVASTUDIO_URI, type.name);
    }
    clearAttribute(node, CORDOVASTUDIO_URI, ATTR_LAYOUT_MARGIN_LEFT);
    clearAttribute(node, CORDOVASTUDIO_URI, ATTR_LAYOUT_MARGIN_RIGHT);
    clearAttribute(node, CORDOVASTUDIO_URI, ATTR_LAYOUT_MARGIN_TOP);
    clearAttribute(node, CORDOVASTUDIO_URI, ATTR_LAYOUT_MARGIN_BOTTOM);
  }

  /**
   * Attach the new child to the previous node
   *
   * @param previous the previous child
   * @param node     the new child to attach it to
   */
  public void attachPrevious(RadViewComponent previous, RadViewComponent node) {
    removeRelativeParams(node);

    String id = previous.getId();
    if (StringUtil.isEmpty(id)) {
      return;
    }

    if (myCurrentTopMatch != null || myCurrentBottomMatch != null) {
      // Attaching the top: arrange below, and for bottom arrange above
      node.getTag().setAttribute(myCurrentTopMatch != null ? ATTR_LAYOUT_BELOW : ATTR_LAYOUT_ABOVE, CORDOVASTUDIO_URI, id);
      // Apply same left/right constraints as the parent
      if (myCurrentLeftMatch != null) {
        applyConstraint(node, myCurrentLeftMatch.getConstraint(true));
        applyMargin(node, ATTR_LAYOUT_MARGIN_LEFT, myLeftMargin);
      }
      else if (myCurrentRightMatch != null) {
        applyConstraint(node, myCurrentRightMatch.getConstraint(true));
        applyMargin(node, ATTR_LAYOUT_MARGIN_RIGHT, myRightMargin);
      }
    }
    else if (myCurrentLeftMatch != null || myCurrentRightMatch != null) {
      node.getTag().setAttribute(myCurrentLeftMatch != null ? ATTR_LAYOUT_TO_RIGHT_OF : ATTR_LAYOUT_TO_LEFT_OF, CORDOVASTUDIO_URI, id);
      // Apply same top/bottom constraints as the parent
      if (myCurrentTopMatch != null) {
        applyConstraint(node, myCurrentTopMatch.getConstraint(true));
        applyMargin(node, ATTR_LAYOUT_MARGIN_TOP, getTopMargin());
      }
      else if (myCurrentBottomMatch != null) {
        applyConstraint(node, myCurrentBottomMatch.getConstraint(true));
        applyMargin(node, ATTR_LAYOUT_MARGIN_BOTTOM, getBottomMargin());
      }
    }
  }

  /**
   * Breaks any cycles detected by the handler
   */
  public void removeCycles() {
    if (myHorizontalCycle != null) {
      removeCycles(myHorizontalDeps);
    }
    if (myVerticalCycle != null) {
      removeCycles(myVerticalDeps);
    }
  }

  private void removeCycles(Set<RadViewComponent> deps) {
    for (RadViewComponent node : myDraggedNodes) {
      DependencyGraph.ViewData view = myDependencyGraph.getView(node);
      if (view != null) {
        for (DependencyGraph.Constraint constraint : view.dependedOnBy) {
          // For now, remove ALL constraints pointing to this node in this orientation.
          // Later refine this to be smarter. (We can't JUST remove the constraints
          // identified in the cycle since there could be multiple.)
          clearAttribute(constraint.from.node, CORDOVASTUDIO_URI, constraint.type.name);
        }
      }
    }
  }

  /**
   * The current match on the left edge, or null if no match or if the left edge is not
   * being moved or resized.
   */
  public Match getCurrentLeftMatch() {
    return myCurrentLeftMatch;
  }

  /**
   * The current match on the top edge, or null if no match or if the top edge is not
   * being moved or resized.
   */
  public Match getCurrentTopMatch() {
    return myCurrentTopMatch;
  }

  /**
   * The current match on the right edge, or null if no match or if the right edge is
   * not being moved or resized.
   */
  public Match getCurrentRightMatch() {
    return myCurrentRightMatch;
  }

  /**
   * The current match on the bottom edge, or null if no match or if the bottom edge is
   * not being moved or resized.
   */
  public Match getCurrentBottomMatch() {
    return myCurrentBottomMatch;
  }

  /**
   * The amount of margin to add to the left edge, or 0
   */
  public int getLeftMargin() {
    return myLeftMargin;
  }

  /**
   * The amount of margin to add to the right edge, or 0
   */
  public int getRightMargin() {
    return myRightMargin;
  }

  /**
   * The amount of margin to add to the top edge, or 0
   */
  public int getTopMargin() {
    return myTopMargin;
  }

  /**
   * The amount of margin to add to the bottom edge, or 0
   */
  public int getBottomMargin() {
    return myBottomMargin;
  }

  /**
   * The associated context
   */
  public OperationContext getContext() {
    return myContext;
  }

  /**
   * Comparator used to sort matches such that the first match is the most desirable
   * match (where we prefer attaching to parent bounds, we avoid matches that lead to a
   * cycle, we prefer constraints on closer widgets rather than ones further away, and
   * so on.)
   * <p/>
   * There are a number of sorting criteria. One of them is the distance between the
   * matched edges. We may end up with multiple matches that are the same distance. In
   * that case we look at the orientation; on the left side, prefer left-oriented
   * attachments, and on the right-side prefer right-oriented attachments. For example,
   * consider the following scenario:
   * <p/>
   * <pre>
   *    +--------------------+-------------------------+
   *    | Attached on left   |                         |
   *    +--------------------+                         |
   *    |                                              |
   *    |                    +-----+                   |
   *    |                    |  A  |                   |
   *    |                    +-----+                   |
   *    |                                              |
   *    |                    +-------------------------+
   *    |                    |       Attached on right |
   *    +--------------------+-------------------------+
   * </pre>
   * <p/>
   * Here, dragging the left edge should attach to the top left attached view, whereas
   * in the following layout dragging the right edge would attach to the bottom view:
   * <p/>
   * <pre>
   *    +--------------------------+-------------------+
   *    | Attached on left         |                   |
   *    +--------------------------+                   |
   *    |                                              |
   *    |                    +-----+                   |
   *    |                    |  A  |                   |
   *    |                    +-----+                   |
   *    |                                              |
   *    |                          +-------------------+
   *    |                          | Attached on right |
   *    +--------------------------+-------------------+
   *
   * </pre>
   * <p/>
   * </ul>
   */
  private final class MatchComparator implements Comparator<Match> {
    @Override
    public int compare(Match m1, Match m2) {
      // Always prefer matching parent bounds
      int parent1 = m1.edge.node == layout ? -1 : 1;
      int parent2 = m2.edge.node == layout ? -1 : 1;
      // unless it's a center bound -- those should always get lowest priority since
      // they overlap with other usually more interesting edges near the center of
      // the layout.
      if (m1.edge.edgeType == SegmentType.CENTER_HORIZONTAL || m1.edge.edgeType == SegmentType.CENTER_VERTICAL) {
        parent1 = 2;
      }
      if (m2.edge.edgeType == SegmentType.CENTER_HORIZONTAL || m2.edge.edgeType == SegmentType.CENTER_VERTICAL) {
        parent2 = 2;
      }
      if (parent1 != parent2) {
        return parent1 - parent2;
      }

      // Avoid matching edges that would lead to a cycle
      if (m1.edge.edgeType.isHorizontal()) {
        int cycle1 = myHorizontalDeps.contains(m1.edge.node) ? 1 : -1;
        int cycle2 = myHorizontalDeps.contains(m2.edge.node) ? 1 : -1;
        if (cycle1 != cycle2) {
          return cycle1 - cycle2;
        }
      }
      else {
        int cycle1 = myVerticalDeps.contains(m1.edge.node) ? 1 : -1;
        int cycle2 = myVerticalDeps.contains(m2.edge.node) ? 1 : -1;
        if (cycle1 != cycle2) {
          return cycle1 - cycle2;
        }
      }

      // TODO: Sort by minimum depth -- do we have the depth anywhere?

      // Prefer nodes that are closer
      int distance1, distance2;
      if (m1.edge.to <= m1.with.from) {
        distance1 = m1.with.from - m1.edge.to;
      }
      else if (m1.edge.from >= m1.with.to) {
        distance1 = m1.edge.from - m1.with.to;
      }
      else {
        // Some kind of overlap - not sure how to prioritize these yet...
        distance1 = 0;
      }
      if (m2.edge.to <= m2.with.from) {
        distance2 = m2.with.from - m2.edge.to;
      }
      else if (m2.edge.from >= m2.with.to) {
        distance2 = m2.edge.from - m2.with.to;
      }
      else {
        // Some kind of overlap - not sure how to prioritize these yet...
        distance2 = 0;
      }

      if (distance1 != distance2) {
        return distance1 - distance2;
      }

      // Prefer matching on baseline
      int baseline1 = (m1.edge.edgeType == SegmentType.BASELINE) ? -1 : 1;
      int baseline2 = (m2.edge.edgeType == SegmentType.BASELINE) ? -1 : 1;
      if (baseline1 != baseline2) {
        return baseline1 - baseline2;
      }

      // Prefer matching top/left edges before matching bottom/right edges
      int orientation1 = (m1.with.edgeType == SegmentType.LEFT || m1.with.edgeType == SegmentType.TOP) ? -1 : 1;
      int orientation2 = (m2.with.edgeType == SegmentType.LEFT || m2.with.edgeType == SegmentType.TOP) ? -1 : 1;
      if (orientation1 != orientation2) {
        return orientation1 - orientation2;
      }

      // Prefer opposite-matching over same-matching.
      // In other words, if we have the choice of matching
      // our left edge with another element's left edge,
      // or matching our left edge with another element's right
      // edge, prefer the right edge since that
      // The two matches have identical distance; try to sort by
      // orientation
      int edgeType1 = (m1.edge.edgeType != m1.with.edgeType) ? -1 : 1;
      int edgeType2 = (m2.edge.edgeType != m2.with.edgeType) ? -1 : 1;
      if (edgeType1 != edgeType2) {
        return edgeType1 - edgeType2;
      }

      return 0;
    }
  }

  public static int centerX(Rectangle rectangle) {
    return rectangle.x + rectangle.width / 2;
  }

  public static int centerY(Rectangle rectangle) {
    return rectangle.y + rectangle.height / 2;
  }

  public static int x2(Rectangle rectangle) {
    return rectangle.x + rectangle.width;
  }

  public static int y2(Rectangle rectangle) {
    return rectangle.y + rectangle.height;
  }
}
