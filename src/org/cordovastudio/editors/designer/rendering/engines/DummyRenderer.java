/*
 * Copyright (C) 2014 Christoffer T. Timm
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

package org.cordovastudio.editors.designer.rendering.engines;

import com.intellij.util.ui.UIUtil;
import org.cordovastudio.devices.Device;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.rendering.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.cordovastudio.editors.designer.rendering.Result.Status.SUCCESS;

/**
 * Created by cti on 09.09.14.
 */
public class DummyRenderer extends RenderingEngine {
    /**
     * Initializes the Bridge object.
     *
     * @param log a {@link org.cordovastudio.editors.designer.rendering.RenderLogger} object. Can be null.
     * @return true if success.
     */
    public boolean init(RenderLogger log) {
        return super.init(null, null, null, log);
    }


    /**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link org.cordovastudio.editors.designer.rendering.RenderSession} on which further actions can be taken.
     *
     * @param params
     * @return a new {@link org.cordovastudio.editors.designer.rendering.RenderSession} object that contains the result of the scene creation and
     * first rendering.
     */
    @Override
    public RenderSession createSession(RenderParams params, Device device) throws RenderingException {
        ViewInfo rootViewInfo = new ViewInfo("DummyRootViewInfo", null, 0, 0, 640, 960);
        BufferedImage image = UIUtil.createImage(640, 960, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setBackground(Color.BLUE);

        return new StaticRenderSession(SUCCESS.createResult(), rootViewInfo, image);
    }
}
