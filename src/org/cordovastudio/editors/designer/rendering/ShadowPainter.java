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
package org.cordovastudio.editors.designer.rendering;

import com.intellij.util.ui.UIUtil;
import org.cordovastudio.utils.ImageUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static org.cordovastudio.branding.artwork.Artwork.Shadows;

public class ShadowPainter {

  /**
   * Adds a drop shadow to a semi-transparent image (of an arbitrary shape) and returns it as a new image.
   * This method attempts to mimic the same visual characteristics as the rectangular shadow painting
   * methods in this class, {@link #createRectangularDropShadow(java.awt.image.BufferedImage)} and
   * {@link #createSmallRectangularDropShadow(java.awt.image.BufferedImage)}.
   *
   * @param source the source image
   * @param shadowSize the size of the shadow, normally {@link #SHADOW_SIZE or {@link #SMALL_SHADOW_SIZE}}
   * @return a new image with the shadow painted in
   */
  @NotNull
  public static BufferedImage createDropShadow(BufferedImage source, int shadowSize) {
    shadowSize /= 2; // make shadow size have the same meaning as in the other shadow paint methods in this class

    return createDropShadow(source, shadowSize, 0.7f, 0);
  }

  /**
   * Creates a drop shadow of a given image and returns a new image which shows the
   * input image on top of its drop shadow.
   * <p>
   * <b>NOTE: If the shape is rectangular and opaque, consider using
   * {@link #drawRectangleShadow(java.awt.Graphics, int, int, int, int)} instead.</b>
   *
   * @param source the source image to be shadowed
   * @param shadowSize the size of the shadow in pixels
   * @param shadowOpacity the opacity of the shadow, with 0=transparent and 1=opaque
   * @param shadowRgb the RGB int to use for the shadow color
   * @return a new image with the source image on top of its shadow
   */
  @SuppressWarnings({"AssignmentToForLoopParameter", "UnnecessaryLocalVariable",   // Imported code
                      "SuspiciousNameCombination", "UnusedAssignment"})
  public static BufferedImage createDropShadow(BufferedImage source, int shadowSize,
                                               float shadowOpacity, int shadowRgb) {

    // This code is based on
    //      http://www.jroller.com/gfx/entry/non_rectangular_shadow

    BufferedImage image;
    int width = source.getWidth();
    int height = source.getHeight();
    boolean isRetina = ImageUtils.isRetinaImage(source);
    if (isRetina && UIUtil.isAppleRetina()) {
      // This shadow painting code doesn't work right with the Apple JDK 6 Retina support.
      // Since this code isn't used very frequently, just skip the drop shadows for now
      return source;
    }
    if (isRetina) {
      image = ImageUtils.createDipImage(width + SHADOW_SIZE, height + SHADOW_SIZE, BufferedImage.TYPE_INT_ARGB);
    } else {
      //noinspection UndesirableClassUsage
      image = new BufferedImage(width + SHADOW_SIZE, height + SHADOW_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    Graphics2D g2 = image.createGraphics();
    //noinspection ConstantConditions
    UIUtil.drawImage(g2, source, shadowSize, shadowSize, null);

    int dstWidth = image.getWidth();
    int dstHeight = image.getHeight();

    int left = (shadowSize - 1) >> 1;
    int right = shadowSize - left;
    int xStart = left;
    int xStop = dstWidth - right;
    int yStart = left;
    int yStop = dstHeight - right;

    shadowRgb &= 0x00FFFFFF;

    int[] aHistory = new int[shadowSize];
    int historyIdx = 0;

    int aSum;

    int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    int lastPixelOffset = right * dstWidth;
    float sumDivider = shadowOpacity / shadowSize;

    // horizontal pass
    for (int y = 0, bufferOffset = 0; y < dstHeight; y++, bufferOffset = y * dstWidth) {
      aSum = 0;
      historyIdx = 0;
      for (int x = 0; x < shadowSize; x++, bufferOffset++) {
        int a = dataBuffer[bufferOffset] >>> 24;
        aHistory[x] = a;
        aSum += a;
      }

      bufferOffset -= right;

      for (int x = xStart; x < xStop; x++, bufferOffset++) {
        int a = (int) (aSum * sumDivider);
        dataBuffer[bufferOffset] = a << 24 | shadowRgb;

        // subtract the oldest pixel from the sum
        aSum -= aHistory[historyIdx];

        // get the latest pixel
        a = dataBuffer[bufferOffset + right] >>> 24;
        aHistory[historyIdx] = a;
        aSum += a;

        if (++historyIdx >= shadowSize) {
          historyIdx -= shadowSize;
        }
      }
    }
    // vertical pass
    for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {
      aSum = 0;
      historyIdx = 0;
      for (int y = 0; y < shadowSize; y++, bufferOffset += dstWidth) {
        int a = dataBuffer[bufferOffset] >>> 24;
        aHistory[y] = a;
        aSum += a;
      }

      bufferOffset -= lastPixelOffset;

      for (int y = yStart; y < yStop; y++, bufferOffset += dstWidth) {
        int a = (int) (aSum * sumDivider);
        dataBuffer[bufferOffset] = a << 24 | shadowRgb;

        // subtract the oldest pixel from the sum
        aSum -= aHistory[historyIdx];

        // get the latest pixel
        a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
        aHistory[historyIdx] = a;
        aSum += a;

        if (++historyIdx >= shadowSize) {
          historyIdx -= shadowSize;
        }
      }
    }

    //noinspection ConstantConditions
    UIUtil.drawImage(g2, source, null, 0, 0);
    g2.dispose();

    return image;
  }

  /**
   * Draws a rectangular drop shadow (of size {@link #SHADOW_SIZE} by
   * {@link #SHADOW_SIZE} around the given source and returns a new image with
   * both combined
   *
   * @param source the source image
   * @return the source image with a drop shadow on the bottom and right
   */
  public static BufferedImage createRectangularDropShadow(BufferedImage source) {
    int type = source.getType();
    if (type == BufferedImage.TYPE_CUSTOM) {
      type = BufferedImage.TYPE_INT_ARGB;
    }

    int width = source.getWidth();
    int height = source.getHeight();
    BufferedImage image;
    if (ImageUtils.isRetinaImage(source)) {
      image = ImageUtils.createDipImage(width + SHADOW_SIZE, height + SHADOW_SIZE, type);
    } else {
      //noinspection UndesirableClassUsage
      image = new BufferedImage(width + SHADOW_SIZE, height + SHADOW_SIZE, type);
    }
    Graphics g = image.getGraphics();
    //noinspection ConstantConditions
    UIUtil.drawImage(g, source, 0, 0, null);
    drawRectangleShadow(image, 0, 0, width, height);
    g.dispose();

    return image;
  }

  /**
   * Draws a small rectangular drop shadow (of size {@link #SMALL_SHADOW_SIZE} by
   * {@link #SMALL_SHADOW_SIZE} around the given source and returns a new image with
   * both combined
   *
   * @param source the source image
   * @return the source image with a drop shadow on the bottom and right
   */
  public static BufferedImage createSmallRectangularDropShadow(BufferedImage source) {
    int type = source.getType();
    if (type == BufferedImage.TYPE_CUSTOM) {
      type = BufferedImage.TYPE_INT_ARGB;
    }

    int width = source.getWidth();
    int height = source.getHeight();

    BufferedImage image;
    if (ImageUtils.isRetinaImage(source)) {
      image = ImageUtils.createDipImage(width + SMALL_SHADOW_SIZE, height + SMALL_SHADOW_SIZE, type);
    } else {
      //noinspection UndesirableClassUsage
      image = new BufferedImage(width + SMALL_SHADOW_SIZE, height + SMALL_SHADOW_SIZE, type);
    }

    Graphics g = image.getGraphics();
    //noinspection ConstantConditions
    UIUtil.drawImage(g, source, 0, 0, null);
    drawSmallRectangleShadow(image, 0, 0, width, height);
    g.dispose();

    return image;
  }

  /**
   * Draws a drop shadow for the given rectangle into the given context. It
   * will not draw anything if the rectangle is smaller than a minimum
   * determined by the assets used to draw the shadow graphics.
   * The size of the shadow is {@link #SHADOW_SIZE}.
   *
   * @param image the image to draw the shadow into
   * @param x the left coordinate of the left hand side of the rectangle
   * @param y the top coordinate of the top of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public static void drawRectangleShadow(BufferedImage image,
                                               int x, int y, int width, int height) {
    Graphics gc = image.getGraphics();
    try {
      drawRectangleShadow(gc, x, y, width, height);
    } finally {
      gc.dispose();
    }
  }

  /**
   * Draws a small drop shadow for the given rectangle into the given context. It
   * will not draw anything if the rectangle is smaller than a minimum
   * determined by the assets used to draw the shadow graphics.
   * The size of the shadow is {@link #SMALL_SHADOW_SIZE}.
   *
   * @param image the image to draw the shadow into
   * @param x the left coordinate of the left hand side of the rectangle
   * @param y the top coordinate of the top of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public static void drawSmallRectangleShadow(BufferedImage image,
                                                    int x, int y, int width, int height) {
    Graphics gc = image.getGraphics();
    try {
      drawSmallRectangleShadow(gc, x, y, width, height);
    } finally {
      gc.dispose();
    }
  }

  /**
   * The width and height of the drop shadow painted by
   * {@link #drawRectangleShadow(java.awt.Graphics, int, int, int, int)}
   */
  public static final int SHADOW_SIZE = 20; // DO NOT EDIT. This corresponds to bitmap graphics

  /**
   * The width and height of the drop shadow painted by
   * {@link #drawSmallRectangleShadow(java.awt.Graphics, int, int, int, int)}
   */
  public static final int SMALL_SHADOW_SIZE = 10; // DO NOT EDIT. Corresponds to bitmap graphics

  /**
   * Draws a drop shadow for the given rectangle into the given context. It
   * will not draw anything if the rectangle is smaller than a minimum
   * determined by the assets used to draw the shadow graphics.
   *
   * @param gc the graphics context to draw into
   * @param x the left coordinate of the left hand side of the rectangle
   * @param y the top coordinate of the top of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  @SuppressWarnings("ConstantConditions")
  public static void drawRectangleShadow(Graphics gc, int x, int y, int width, int height) {
    assert Shadows.ShadowBottomLeft != null;
    assert Shadows.ShadowBottomRight.getWidth(null) == SHADOW_SIZE;
    assert Shadows.ShadowBottomRight.getHeight(null) == SHADOW_SIZE;

    int blWidth = Shadows.ShadowBottomLeft.getWidth(null);
    int trHeight = Shadows.ShadowTopRight.getHeight(null);
    if (width < blWidth) {
      return;
    }
    if (height < trHeight) {
      return;
    }

    UIUtil.drawImage(gc, Shadows.ShadowBottomLeft, x, y + height, null);
    UIUtil.drawImage(gc, Shadows.ShadowBottomRight, x + width, y + height, null);
    UIUtil.drawImage(gc, Shadows.ShadowTopRight, x + width, y, null);
    ImageUtils.drawDipImage(gc, Shadows.ShadowBottom, x + Shadows.ShadowBottomLeft.getWidth(null), y + height, x + width,
                            y + height + Shadows.ShadowBottom.getHeight(null), 0, 0, Shadows.ShadowBottom.getWidth(null), Shadows.ShadowBottom.getHeight(null),
                            null);
    ImageUtils.drawDipImage(gc, Shadows.ShadowRight, x + width, y + Shadows.ShadowTopRight.getHeight(null), x + width + Shadows.ShadowRight.getWidth(null),
                            y + height, 0, 0, Shadows.ShadowRight.getWidth(null), Shadows.ShadowRight.getHeight(null), null);
  }

  /**
   * Draws a small drop shadow for the given rectangle into the given context. It
   * will not draw anything if the rectangle is smaller than a minimum
   * determined by the assets used to draw the shadow graphics.
   * <p>
   *
   * @param gc the graphics context to draw into
   * @param x the left coordinate of the left hand side of the rectangle
   * @param y the top coordinate of the top of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  @SuppressWarnings("ConstantConditions")
  public static void drawSmallRectangleShadow(Graphics gc, int x, int y, int width, int height) {
    assert Shadows.Shadow2BottomLeft != null;
    assert Shadows.Shadow2TopRight != null;
    assert Shadows.Shadow2BottomRight.getWidth(null) == SMALL_SHADOW_SIZE;
    assert Shadows.Shadow2BottomRight.getHeight(null) == SMALL_SHADOW_SIZE;

    int blWidth = Shadows.Shadow2BottomLeft.getWidth(null);
    int trHeight = Shadows.Shadow2TopRight.getHeight(null);
    if (width < blWidth) {
      return;
    }
    if (height < trHeight) {
      return;
    }

    UIUtil.drawImage(gc, Shadows.Shadow2BottomLeft, x, y + height, null);
    UIUtil.drawImage(gc, Shadows.Shadow2BottomRight, x + width, y + height, null);
    UIUtil.drawImage(gc, Shadows.Shadow2TopRight, x + width, y, null);
    ImageUtils.drawDipImage(gc, Shadows.Shadow2Bottom, x + Shadows.Shadow2BottomLeft.getWidth(null), y + height, x + width,
                            y + height + Shadows.Shadow2Bottom.getHeight(null), 0, 0, Shadows.Shadow2Bottom.getWidth(null), Shadows.Shadow2Bottom.getHeight(null),
                            null);
    ImageUtils.drawDipImage(gc, Shadows.Shadow2Right, x + width, y + Shadows.Shadow2TopRight.getHeight(null), x + width + Shadows.Shadow2Right.getWidth(null),
                            y + height, 0, 0, Shadows.Shadow2Right.getWidth(null), Shadows.Shadow2Right.getHeight(null), null);
  }
}
