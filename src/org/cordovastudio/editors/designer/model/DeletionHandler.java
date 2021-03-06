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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.cordovastudio.GlobalConstants.*;

/**
 * Handles deletions in a relative layout, transferring constraints across
 * deleted nodes.
 */
public class DeletionHandler {
  private final List<RadComponent> myChildren;
  private final List<RadViewComponent> myDeleted;
  private final Set<String> myDeletedIds;
  private final Map<String, RadViewComponent> myNodeMap;

  /**
   * Creates a new {@link DeletionHandler}
   *
   * @param deleted the deleted nodes
   * @param moved   nodes that were moved (e.g. deleted, but also inserted elsewhere)
   * @param layout  the parent layout of the deleted nodes
   */
  public DeletionHandler(@NotNull List<RadViewComponent> deleted,
                         @NotNull List<RadViewComponent> moved,
                         @NotNull RadViewComponent layout) {
    myDeleted = deleted;
    myChildren = layout.getChildren();
    myNodeMap = Maps.newHashMapWithExpectedSize(myChildren.size());
    for (RadViewComponent view : RadViewComponent.getViewComponents(myChildren)) {
      String id = view.getId();
      if (id != null) {
        myNodeMap.put(id, view);
      }
    }

    myDeletedIds = Sets.newHashSetWithExpectedSize(myDeleted.size());
    for (RadViewComponent node : myDeleted) {
      String id = node.getId();
      if (id != null) {
        myDeletedIds.add(id);
      }
    }

    // Any widgets that remain (e.g. typically because they were moved) should
    // keep their incoming dependencies
    for (RadViewComponent node : moved) {
      String id = node.getId();
      if (id != null) {
        myDeletedIds.remove(id);
      }
    }
  }

  @Nullable
  private static String getId(@NotNull XmlAttribute attribute) {
      String id = attribute.getValue();
      if (id == null) {
        return null;
      }

      return id;
  }

  /**
   * Updates the constraints in the layout to handle deletion of a set of
   * nodes. This ensures that any constraints pointing to one of the deleted
   * nodes are changed properly to point to a non-deleted node with similar
   * constraints.
   */
  public void updateConstraints() {
    if (myChildren.size() == myDeleted.size()) {
      // Deleting everything: Nothing to be done
      return;
    }

    // Now remove incoming edges to any views that were deleted. If possible,
    // don't just delete them but replace them with a transitive constraint, e.g.
    // if we have "A <= B <= C" and "B" is removed, then we end up with "A <= C",

    for (RadViewComponent child : RadViewComponent.getViewComponents(myChildren)) {
      if (myDeleted.contains(child)) {
        continue;
      }

      for (XmlAttribute attribute : child.getTag().getAttributes()) {
        String id = getId(attribute);
        if (id != null) {
          if (myDeletedIds.contains(id)) {
            // Unset this reference to a deleted widget. It might be
            // replaced if the pointed to node points to some other node
            // on the same side, but it may use a different constraint name,
            // or have none at all (e.g. parent).
            String name = attribute.getLocalName();
            attribute.delete();

            RadViewComponent deleted = myNodeMap.get(id);
            if (deleted != null) {
              ConstraintType type = ConstraintType.fromAttribute(name);
              if (type != null) {
                transfer(deleted, child, type, 0);
              }
            }
          }
        }
      }
    }
  }

  private void transfer(RadViewComponent deleted, RadViewComponent target, ConstraintType targetType, int depth) {
    if (depth == 20) {
      // Prevent really deep flow or unbounded recursion in case there is a bug in
      // the cycle detection code
      return;
    }

    assert myDeleted.contains(deleted);

    for (XmlAttribute attribute : deleted.getTag().getAttributes()) {
      String name = attribute.getLocalName();
      ConstraintType type = ConstraintType.fromAttribute(name);
      if (type == null) {
        continue;
      }

      ConstraintType transfer = getCompatibleConstraint(type, targetType);
      if (transfer != null) {
        String id = getId(attribute);
        if (id != null) {
          if (myDeletedIds.contains(id)) {
            RadViewComponent nextDeleted = myNodeMap.get(id);
            if (nextDeleted != null) {
              // Points to another deleted node: recurse
              transfer(nextDeleted, target, targetType, depth + 1);
            }
          }
          else {
            // Found an undeleted node destination: point to it directly.
            // Note that we're using the
            target.getTag().setAttribute(transfer.name, CORDOVASTUDIO_URI, attribute.getValue());
          }
        }
        else {
          // Pointing to parent or center etc (non-id ref): replicate this on the target
          target.getTag().setAttribute(name, CORDOVASTUDIO_URI, attribute.getValue());
        }
      }
    }
  }

  /**
   * Determines if two constraints are in the same direction and if so returns
   * the constraint in the same direction. Rather than returning boolean true
   * or false, this returns the constraint which is sometimes modified. For
   * example, if you have a node which points left to a node which is centered
   * in parent, then the constraint is turned into center horizontal.
   */
  @Nullable
  private static ConstraintType getCompatibleConstraint(@NotNull ConstraintType first, @NotNull ConstraintType second) {
    if (first == second) {
      return first;
    }

    switch (second) {
      case ALIGN_LEFT:
      case LAYOUT_RIGHT_OF:
        switch (first) {
          case LAYOUT_CENTER_HORIZONTAL:
          case LAYOUT_LEFT_OF:
          case ALIGN_LEFT:
            return first;
          case LAYOUT_CENTER_IN_PARENT:
            return ConstraintType.LAYOUT_CENTER_HORIZONTAL;
        }
        return null;

      case ALIGN_RIGHT:
      case LAYOUT_LEFT_OF:
        switch (first) {
          case LAYOUT_CENTER_HORIZONTAL:
          case ALIGN_RIGHT:
          case LAYOUT_LEFT_OF:
            return first;
          case LAYOUT_CENTER_IN_PARENT:
            return ConstraintType.LAYOUT_CENTER_HORIZONTAL;
        }
        return null;

      case ALIGN_TOP:
      case LAYOUT_BELOW:
      case ALIGN_BASELINE:
        switch (first) {
          case LAYOUT_CENTER_VERTICAL:
          case ALIGN_TOP:
          case LAYOUT_BELOW:
          case ALIGN_BASELINE:
            return first;
          case LAYOUT_CENTER_IN_PARENT:
            return ConstraintType.LAYOUT_CENTER_VERTICAL;
        }
        return null;
      case ALIGN_BOTTOM:
      case LAYOUT_ABOVE:
        switch (first) {
          case LAYOUT_CENTER_VERTICAL:
          case ALIGN_BOTTOM:
          case LAYOUT_ABOVE:
          case ALIGN_BASELINE:
            return first;
          case LAYOUT_CENTER_IN_PARENT:
            return ConstraintType.LAYOUT_CENTER_VERTICAL;
        }
        return null;
    }

    return null;
  }
}
