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

package org.cordovastudio.devices;

import org.cordovastudio.branding.CordovaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by cti on 15.09.14.
 */
public enum FormFactor {
    MOBILE, WEAR, GLASS, TV, CAR;

    @NotNull
    public Icon getIcon() {
        switch (this) {
            case CAR: return CordovaIcons.FormFactors.Car;
            case WEAR: return CordovaIcons.FormFactors.Wear;
            case TV: return CordovaIcons.FormFactors.Tv;
            case GLASS: return CordovaIcons.FormFactors.Glass;
            case MOBILE:
            default:
                return CordovaIcons.FormFactors.Mobile;
        }
    }
}
