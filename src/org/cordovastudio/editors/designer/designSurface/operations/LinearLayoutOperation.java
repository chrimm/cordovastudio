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
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.IdeBorderFactory;
import org.cordovastudio.editors.designer.CordovaDesignerUtils;
import org.cordovastudio.editors.designer.ResizeContext;
import org.cordovastudio.editors.designer.designSurface.DesignerGraphics;
import org.cordovastudio.editors.designer.designSurface.DrawingStyle;
import org.cordovastudio.editors.designer.designSurface.layers.FeedbackLayer;
import org.cordovastudio.editors.designer.designSurface.OperationContext;
import org.cordovastudio.editors.designer.designSurface.feedbacks.TextFeedback;
import org.cordovastudio.editors.designer.model.Gravity;
import org.cordovastudio.editors.designer.model.RadComponent;
import org.cordovastudio.editors.designer.model.RadComponentOperations;
import org.cordovastudio.editors.designer.model.RadViewComponent;
import org.cordovastudio.editors.designer.palette.PaletteItem;
import org.cordovastudio.editors.designer.policies.FillPolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.cordovastudio.editors.designer.designSurface.DrawingStyle.*;
import static org.cordovastudio.GlobalConstants.*;

/**
 * @author Alexander Lobas
 */
public class LinearLayoutOperation extends FlowBaseOperation {
  private GravityFeedback myFeedback;
  private TextFeedback myTextFeedback;
  private FlowPositionFeedback myFlowFeedback;
  private Gravity myExclude;
  private Gravity myGravity;
  private boolean mySetGravity;

  public LinearLayoutOperation(RadComponent container, OperationContext context, boolean horizontal) {
    super(container, context, horizontal);

    if (context.isMove() && context.getComponents().size() == 1) {
      myExclude = getGravity(myHorizontal, context.getComponents().get(0));
    }
  }

  private final static int MIN_MARGIN_DIMENSIONS = 100;

  @Override
  protected void createFeedback() {
    super.createFeedback();

    if (myTextFeedback == null) {
      FeedbackLayer layer = myContext.getArea().getFeedbackLayer();

      if (myHorizontal && myBounds.height > MIN_MARGIN_DIMENSIONS
            || !myHorizontal && myBounds.width > MIN_MARGIN_DIMENSIONS) {
        mySetGravity = true;

        myFeedback = new GravityFeedback();
        if (myContainer.getChildren().isEmpty()) {
          myFeedback.setBounds(myBounds);
        }
        layer.add(myFeedback, 0);
      }

      //noinspection ConstantConditions
      if (!SHOW_STATIC_GRID) {
        myFlowFeedback = new FlowPositionFeedback();
        myFlowFeedback.setBounds(myBounds);
        layer.add(myFlowFeedback, 0);
      }

      myTextFeedback = new TextFeedback();
      myTextFeedback.setBorder(IdeBorderFactory.createEmptyBorder(0, 3, 2, 0));
      layer.add(myTextFeedback);

      layer.repaint();
    }
  }

  @Override
  public void showFeedback() {
    super.showFeedback();

    if (mySetGravity) {
      Point location = myContext.getLocation();
      Gravity gravity = myHorizontal ? calculateVertical(myBounds, location) : calculateHorizontal(myBounds, location);
      if (gravity == null) {
        gravity = myHorizontal ? Gravity.left : Gravity.top;
      }

      if (!myContainer.getChildren().isEmpty()) {
        myFeedback.setBounds(myInsertFeedback.getBounds());
      }

      myFeedback.setGravity(gravity);

      myTextFeedback.clear();
      if (gravity == Gravity.left || gravity == Gravity.top) {
        myTextFeedback.setVisible(false);
      } else {
        myTextFeedback.bold(gravity.name());
        myTextFeedback.setVisible(true);
      }
      myTextFeedback.centerTop(myBounds);

      myGravity = gravity;
    }

    //noinspection ConstantConditions
    if (!SHOW_STATIC_GRID) {
      myFlowFeedback.repaint();
    }
  }

  @Override
  public void eraseFeedback() {
    super.eraseFeedback();
    if (myTextFeedback != null) {
      FeedbackLayer layer = myContext.getArea().getFeedbackLayer();
      if (mySetGravity) {
        layer.remove(myFeedback);
      }
      layer.remove(myTextFeedback);

      //noinspection ConstantConditions
      if (!SHOW_STATIC_GRID) {
        layer.remove(myFlowFeedback);
      }

      layer.repaint();
      myFeedback = null;
      myTextFeedback = null;
    }
  }

  @Nullable
  private Gravity calculateHorizontal(Rectangle bounds, Point location) {
    // Only align to the bottom if you're within the final quarter of the width (*and* the dragged bounds are significantly
    // smaller than the available bounds)
    List<RadComponent> dragged = myContext.getComponents();
    assert !dragged.isEmpty();
    if (dragged.size() > 1) {
      return Gravity.left;
    }

    RadComponent component = dragged.get(0);
    if (component.getBounds(myContext.getArea().getFeedbackLayer()).width > bounds.width / 2) {
      return Gravity.left;
    }

    if (isFilled(myHorizontal, (RadViewComponent)component)) {
      return Gravity.left;
    }

    int thirds = bounds.width / 3;
    int right = bounds.x + 2 * thirds;
    if (location.x >= right) {
      return Gravity.right;
    }

    int center = bounds.x + thirds;
    if (location.x >= center) {
      return Gravity.center;
    }

    return Gravity.left;
  }

  @Nullable
  private Gravity calculateVertical(Rectangle bounds, Point location) {
    // Only align to the bottom if you're within the final quarter of the height (*and* the dragged bounds are significantly
    // smaller than the available bounds)
    List<RadComponent> dragged = myContext.getComponents();
    assert !dragged.isEmpty();
    if (dragged.size() > 1) {
      return Gravity.top;
    }
    RadComponent component = dragged.get(0);
    if (component.getBounds(myContext.getArea().getFeedbackLayer()).height > bounds.height / 2) {
      return Gravity.top;
    }

    if (isFilled(myHorizontal, (RadViewComponent)component)) {
      return Gravity.top;
    }

    int thirds = bounds.height / 3;
    int bottom = bounds.y + 2 * thirds;
    if (location.y >= bottom) {
      return Gravity.bottom;
    }

    int center = bounds.y + thirds;
    if (location.y >= center) {
      return Gravity.center;
    }

    return Gravity.top;
  }

  @Override
  public boolean canExecute() {
    return super.canExecute() || (myComponents.size() == 1 && myGravity != myExclude);
  }

  @Override
  public void execute() throws Exception {
    if (super.canExecute()) {
      super.execute();
    }

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        Gravity gravity = myGravity == Gravity.top || myGravity == Gravity.left ? null : myGravity;
        execute(myHorizontal, gravity, RadViewComponent.getViewComponents(myComponents));
      }
    });
  }

  public static void applyGravity(boolean horizontal, @Nullable Gravity gravity, @NotNull List<? extends RadViewComponent> components) {
    if (gravity == null) {
      for (RadViewComponent component : components) {
        XmlTag tag = component.getTag();
        RadComponentOperations.deleteAttribute(tag, ATTR_LAYOUT_GRAVITY);
      }
    }
    else {
      String gravityValue = horizontal ? Gravity.getValue(null, gravity) : Gravity.getValue(gravity, null);

      for (RadViewComponent component : components) {
        XmlTag tag = component.getTag();

        if (isFilled(horizontal, component)) {
          tag.setAttribute(horizontal ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH, CORDOVASTUDIO_URI, VALUE_WRAP_CONTENT);
        }

        if (gravityValue != null) {
          tag.setAttribute(ATTR_LAYOUT_GRAVITY, CORDOVASTUDIO_URI, gravityValue);
        } else {
          XmlAttribute a = tag.getAttribute(ATTR_LAYOUT_GRAVITY, CORDOVASTUDIO_URI);
          if (a != null) {
            a.delete();
          }
        }
      }
    }
  }

  public void execute(boolean horizontal, @Nullable Gravity gravity, @NotNull List<? extends RadViewComponent> components) {
    applyGravity(horizontal, gravity, components);

    if (myContext.isMove()) {
      // Don't adjust widths/heights/weights when just moving within a single
      // LinearLayout
      // TODO: If it's a move from one widget to another, track that differently!
      return;
    }

    // Attempt to set fill-properties on newly added views such that for example,
    // in a vertical layout, a text field defaults to filling horizontally, but not
    // vertically.
    if (components.size() == 1) {
      RadViewComponent node = components.get(0);
      boolean vertical = !horizontal;

      FillPolicy fill = FillPolicy.getFillPreference(node);
      String fillParent = VALUE_FILL_PARENT;
      XmlTag tag = node.getTag();
      if (fill.fillHorizontally(vertical)) {
        tag.setAttribute(ATTR_LAYOUT_WIDTH, CORDOVASTUDIO_URI, fillParent);
      }
      else {
        tag.setAttribute(ATTR_LAYOUT_WIDTH, CORDOVASTUDIO_URI, VALUE_WRAP_CONTENT);
        if (!vertical && fill == FillPolicy.WIDTH_IN_VERTICAL) {
          // In a horizontal layout, make views that would fill horizontally in a
          // vertical layout have a non-zero weight instead. This will make the item
          // fill but only enough to allow other views to be shown as well.
          // (However, for drags within the same layout we do not touch
          // the weight, since it might already have been tweaked to a particular
          // value)
          tag.setAttribute(ATTR_LAYOUT_WEIGHT, CORDOVASTUDIO_URI, "1");
        }
      }
      if (fill.fillVertically(vertical)) {
        tag.setAttribute(ATTR_LAYOUT_HEIGHT, CORDOVASTUDIO_URI, fillParent);
      } else {
        tag.setAttribute(ATTR_LAYOUT_HEIGHT, CORDOVASTUDIO_URI, VALUE_WRAP_CONTENT);
      }

      // TODO: How does my measure-render handle drop customizations? I need to put it into the
      // metadata!
    }

    // If you insert into a layout that already is using layout weights,
    // and all the layout weights are the same (nonzero) value, then use
    // the same weight for this new layout as well. Also duplicate the 0dip/0px/0dp
    // sizes, if used.
    boolean duplicateWeight = true;
    boolean duplicate0dip = true;
    String sameWeight = null;
    String sizeAttribute = horizontal ? ATTR_LAYOUT_WIDTH : ATTR_LAYOUT_HEIGHT;
    List<RadViewComponent> siblings = RadViewComponent.getViewComponents(myContainer.getChildren());
    for (RadViewComponent target : siblings) {
      if (components.contains(target)) {
        continue;
      }
      XmlTag tag = target.getTag();
      String weight = tag.getAttributeValue(ATTR_LAYOUT_WEIGHT, CORDOVASTUDIO_URI);
      if (weight == null || weight.length() == 0) {
        duplicateWeight = false;
        break;
      } else if (sameWeight != null && !sameWeight.equals(weight)) {
        duplicateWeight = false;
      } else {
        sameWeight = weight;
      }
      String size = tag.getAttributeValue(sizeAttribute, CORDOVASTUDIO_URI);
      if (size != null && !size.startsWith("0")) {
        duplicate0dip = false;
        break;
      }
    }
    if (duplicateWeight && sameWeight != null) {
      for (RadViewComponent component : components) {
        XmlTag tag = component.getTag();
        tag.setAttribute(ATTR_LAYOUT_WEIGHT, CORDOVASTUDIO_URI, sameWeight);
        if (duplicate0dip) {
          tag.setAttribute(sizeAttribute, CORDOVASTUDIO_URI, "0dp");
        }
      }
    }
  }

  @Nullable
  public static Gravity getGravity(boolean horizontal, RadComponent component) {
    XmlTag tag = ((RadViewComponent)component).getTag();
    String length = tag.getAttributeValue(horizontal ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH, CORDOVASTUDIO_URI);

    if (length != null && !ResizeContext.isFill(length)) {
      Pair<Gravity, Gravity> gravity = Gravity.getSides(component);
      return horizontal ? gravity.second : gravity.first;
    }

    return null;
  }

  private static boolean isFilled(boolean horizontal, RadViewComponent component) {
    XmlTag tag = component.getTag();
    if (tag != null) {
      // Dragging within canvas
      XmlAttribute attribute = tag.getAttribute(horizontal ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH, CORDOVASTUDIO_URI);
      if (attribute == null) {
        return false;
      }
      String value = attribute.getValue();
      return VALUE_FILL_PARENT.equals(value);
    } else {
      // Dragging from palette: no PSI element exists yet
      // Look at the creation XML to see whether it's a filling view
      PaletteItem paletteItem = component.getInitialPaletteItem();
      if (paletteItem != null) {
        String creation = paletteItem.getCreation();
        if (creation != null) {
          int index = creation.indexOf(horizontal ? "layout_width=\"wrap_content\"" : "layout_height=\"wrap_content\"");
          return index == -1;
        }
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  //
  // Feedback
  //
  //////////////////////////////////////////////////////////////////////////////////////////

  private class FlowPositionFeedback extends JComponent {
    @Override
    public void paint(Graphics graphics) {
      super.paint(graphics);
    }


    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      Rectangle bounds = ((RadViewComponent)myContainer).getPaddedBounds(this);
      RadComponent component = myContainer;
      DesignerGraphics.drawRect(DrawingStyle.DROP_RECIPIENT, g, bounds.x, bounds.y, bounds.width, bounds.height);

      if (myHorizontal) {
        for (RadComponent child : component.getChildren()) {
          Rectangle childBounds = child.getBounds(this);
          int marginRight = ((RadViewComponent)child).getMargins(this).right;
          int x = childBounds.x + childBounds.width + marginRight;
          DesignerGraphics.drawLine(DrawingStyle.DROP_ZONE, g, x, bounds.y, x, bounds.y + bounds.height);
        }
      }
      else {
        for (RadComponent child : component.getChildren()) {
          Rectangle childBounds = child.getBounds(this);
          int marginBottom = ((RadViewComponent)child).getMargins(this).bottom;
          int y = childBounds.y + childBounds.height + marginBottom;
          DesignerGraphics.drawLine(DrawingStyle.DROP_ZONE, g, bounds.x, y, bounds.x + bounds.width, y);
        }
      }
    }
  }

  private class GravityFeedback extends JComponent {
    @Nullable
    private Gravity myGravity;

    public void setGravity(@Nullable Gravity gravity) {
      myGravity = gravity;
      repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      // Paint outline of inserted component, if there's just one:
      if (myComponents.size() == 1) {
        RadViewComponent first = (RadViewComponent)myComponents.get(0);
        Dimension size = CordovaDesignerUtils.computePreferredSize(myContext.getArea(), first, myContainer);
        Rectangle bounds;
        if (myHorizontal) {
          bounds = computeHorizontalPreviewBounds(size);
        }
        else {
          bounds = computeVerticalPreviewBounds(size);
        }

        Shape clip = g.getClip();
        g.setClip(bounds);
        DesignerGraphics.drawRect(DrawingStyle.DROP_PREVIEW, g, bounds.x, bounds.y, bounds.width, bounds.height);
        g.setClip(clip);
      } else {
        // Multiple components: Just show insert line
        if (myHorizontal) {
          paintHorizontalCell(g);
        }
        else {
          paintVerticalCell(g);
        }
      }
    }
    private Rectangle computeVerticalPreviewBounds(Dimension b) {
      int x = 0;
      int y = 0;
      if (!myContainer.getChildren().isEmpty()) {
        y -= b.height / 2;
      }

      if (myGravity == Gravity.center) {
        x = getWidth() / 2 - b.width / 2;
      }
      else if (myGravity == null) {
        x = 0;
        b.width = getWidth();
      }
      else if (myGravity == Gravity.right) {
        x = getWidth() - b.width;
      }

      int width = b.width;
      int hSpace = Math.min(5, Math.max(1, getWidth() / 30));
      if (hSpace > 1) {
        x += hSpace;
        width -= 2 * hSpace;
        if (width < 5) {
          width = 5;
        }
      }
      return new Rectangle(x, y, width, b.height);
    }

    private Rectangle computeHorizontalPreviewBounds(Dimension b) {
      int x = 0;
      int y = 0;
      if (!myContainer.getChildren().isEmpty()) {
        x -= b.width / 2;
      }

      if (myGravity == Gravity.center) {
        y = getHeight() / 2 - b.height / 2;
      }
      else if (myGravity == null) {
        y = 0;
        b.height = getHeight();
      }
      else if (myGravity == Gravity.bottom) {
        y = getHeight() - b.height;
      }

      // Make the box slightly smaller such that it doesn't overlap exactly
      // with the layout bounding box
      int vSpace = Math.min(5, Math.max(1, getHeight() / 30));
      int height = b.height;
      if (vSpace > 1) {
        y += vSpace;
        height -= 2 * vSpace;
        if (height < 5) {
          height = 5;
        }
      }
      return new Rectangle(x, y, b.width, height);
    }

    private void paintHorizontalCell(Graphics g) {
      int y = 0;
      int height = (getHeight() - 3) / 4;
      if (myGravity == Gravity.center) {
        y = height + 1;
      }
      else if (myGravity == null) {
        y = 2 * height + 2;
      }
      else if (myGravity == Gravity.bottom) {
        y = getHeight() - height;
      }

      int vSpace = Math.min(5, Math.max(1, getHeight() / 30));
      if (vSpace > 1) {
        y += vSpace;
        height -= 2 * vSpace;
      }

      int thickness = DrawingStyle.GRAVITY.getLineWidth();
      if (myContainer.getChildren().isEmpty()) {
        DesignerGraphics.drawFilledRect(DrawingStyle.GRAVITY, g, 0, y, thickness, height);
        DesignerGraphics.drawFilledRect(DrawingStyle.GRAVITY, g, myBounds.width - thickness, y, thickness, height);
      }
      else {
        DesignerGraphics.drawLine(DrawingStyle.GRAVITY, g, thickness / 2, y, thickness / 2, y + height);
      }
    }

    private void paintVerticalCell(Graphics g) {
      int x = 0;
      int width = (getWidth() - 3) / 4;
      if (myGravity == Gravity.center) {
        x = width + 1;
      }
      else if (myGravity == null) {
        x = 2 * width + 2;
      }
      else if (myGravity == Gravity.right) {
        x = getWidth() - width;
      }

      int hSpace = Math.min(5, Math.max(1, getWidth() / 30));
      if (hSpace > 1) {
        x += hSpace;
        width -= 2 * hSpace;
      }

      int thickness = DrawingStyle.GRAVITY.getLineWidth();
      if (myContainer.getChildren().isEmpty()) {
        DesignerGraphics.drawFilledRect(DrawingStyle.GRAVITY, g, x, 0, width, thickness);
        DesignerGraphics.drawFilledRect(DrawingStyle.GRAVITY, g, x, myBounds.height - thickness, width, thickness);
      }
      else {
        DesignerGraphics.drawLine(DrawingStyle.GRAVITY, g, x, thickness / 2, x + width, thickness / 2);
      }
    }
  }
}
