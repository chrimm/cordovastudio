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

package org.cordovastudio.test;

import org.cordovastudio.devices.*;

import java.awt.*;

/**
 * Created by cti on 07.10.14.
 */
public class DummyDeviceFactory {
    public static Device createDummyDevice() {
        Device.Builder builder = new Device.Builder();
        State state = new State();
        Artwork artwork = new Artwork();

        state.setName("default");
        state.setDefaultState(true);
        state.setDescription("A dummy State");
        state.setOrientation(ScreenOrientation.PORTRAIT);
        state.setStatusbar(true);

        artwork.setFrameLandscape(null);
        artwork.setFrameOffsetLandscape(null);
        artwork.setFramePortrait(null);
        artwork.setFrameOffsetPortrait(null);
        artwork.setIcon(null);

        builder.setManufacturer("Generic");
        builder.setName("galaxy_nexus");
        builder.setDisplayName("Phone");

        builder.isRound(false);
        builder.setFormFactor(FormFactor.MOBILE);

        builder.addScreenSize(ScreenOrientation.PORTRAIT, new Dimension(720, 1280));
        builder.addScreenSize(ScreenOrientation.LANDSCAPE, new Dimension(1280, 720));

        builder.addState(state);
        builder.setArtwork(artwork);

        return builder.build();
    }
}
