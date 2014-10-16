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

package org.cordovastudio.branding;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by cti on 26.08.14.
 */
public class CordovaIcons {

    private static String RESOURCE_DIRECTORY    = "artwork/";
    private static String ICON_DIRECTORY        = RESOURCE_DIRECTORY + "icons/";

    private static Icon loadIcon(String iconFileName) {
        return IconLoader.getIcon(ICON_DIRECTORY + iconFileName);
    }

    public static class Files {
        public  static final Icon CordovaFile = loadIcon("filesCordovaFile_16.png"); //16x16
    }

    public static class Designer {
        public static final Icon Gravity = loadIcon("designerGravity_16.png"); // 16x16
        public static final Icon ToolWindow = loadIcon("toolWindow_13.png"); // 13x13
        public static final Icon WrapWidth = loadIcon("designerWrapWidth_16.png"); // 16x16
        public static final Icon WrapHeight = loadIcon("designerWrapHeight_16.png"); // 16x16
        public static final Icon FillWidth = loadIcon("designerFillWidth_16.png"); // 16x16
        public static final Icon FillHeight = loadIcon("designerFillHeight_16.png"); // 16x16
        public static final Icon SwitchHorizontalLinear = loadIcon("designerSwitchHorizontalLinear_16.png"); // 16x16
        public static final Icon SwitchVerticalLinear = loadIcon("designerSwitchVerticalLinear_16.png"); // 16x16
        public static final Icon Baseline = loadIcon("designerBaseline_16.png"); // 16x16
        public static final Icon NoBaseline = loadIcon("designerNoBaseline_16.png"); // 16x16
        public static final Icon DistributeWeights = loadIcon("designerDistributeWeights_16.png"); // 16x16
        public static final Icon ClearWeights = loadIcon("designerClearWeights_16.png"); // 16x16
        public static final Icon DominateWeight = loadIcon("designerDominateWeight_16.png"); // 16x16
        public static final Icon Weights = loadIcon("designerWeights_16.png"); // 16x16
    }

    public static class Modules {
        public static final Icon CordovaModule = loadIcon("cordovaModuleIcon_16.png"); // 16x16
    }

    public static class FormFactors {
        public static final Icon Mobile = loadIcon("formFactorMobile_16.png"); // 16x16
        public static final Icon Wear = loadIcon("formFactorWear_16.png"); // 16x16
        public static final Icon Glass = loadIcon("formFactorGlass_16.png"); // 16x16
        public static final Icon Car = loadIcon("formFactorCar_16.png"); // 16x16
        public static final Icon Tv = loadIcon("formFactorTv_16.png"); // 16x16
    }

    public static class Toolbar {
        public static final Icon DropDownArrow = loadIcon("toolbarDropDownArrow_16.png"); // 16x16

        public static final Icon ChooseDevice = loadIcon("toolbarChooseDevice_16.png"); // 16x16
        public static final Icon PortraitOrientation = loadIcon("toolbarPortraitOrientation_16.png"); // 16x16
        public static final Icon LandscapeOrientation = loadIcon("toolbarLandscapeOrientation_16.png"); // 16x16
        public static final Icon FlipToPortraitOrientation = loadIcon("toolbarFlipToPortraitOrientation_16.png"); // 16x16
        public static final Icon FlipToLandscapeOrientation = loadIcon("toolbarFlipToLandscapeOrientation_16.png"); // 16x16
        public static final Icon SquareOrientation = loadIcon("toolbarSquareOrientation_16.png"); // 16x16

        public static final Icon ChooseTheme = loadIcon("toolbarChooseTheme_16.png"); // 16x16

        public static final Icon CaptureScreenShot = loadIcon("toolbarCaptureScreenShot_16.png"); // 16x16

        public static final Icon NormalRender = loadIcon("toolbarNormalRender_16.png"); // 16x16
        public static final Icon ViewportRender = loadIcon("toolbarViewportRender_16.png"); // 16x16
    }

    public static class Palette {
        public static final Icon AbsoluteLayout = loadIcon("palette/AbsoluteLayout.png"); // 16x16
        public static final Icon AdapterViewFlipper = loadIcon("palette/AdapterViewFlipper.png"); // 16x16
        public static final Icon AnalogClock = loadIcon("palette/AnalogClock.png"); // 16x16
        public static final Icon AutoCompleteTextView = loadIcon("palette/AutoCompleteTextView.png"); // 16x16
        public static final Icon Button = loadIcon("palette/Button.png"); // 16x16
        public static final Icon CalendarView = loadIcon("palette/CalendarView.png"); // 16x16
        public static final Icon CheckBox = loadIcon("palette/CheckBox.png"); // 16x16
        public static final Icon CheckedTextView = loadIcon("palette/CheckedTextView.png"); // 16x16
        public static final Icon Chronometer = loadIcon("palette/Chronometer.png"); // 16x16
        public static final Icon DatePicker = loadIcon("palette/DatePicker.png"); // 16x16
        public static final Icon DeviceScreen = loadIcon("palette/DeviceScreen.png"); // 16x16
        public static final Icon DialerFilter = loadIcon("palette/DialerFilter.png"); // 16x16
        public static final Icon DigitalClock = loadIcon("palette/DigitalClock.png"); // 16x16
        public static final Icon EditText = loadIcon("palette/EditText.png"); // 16x16
        public static final Icon ExpandableListView = loadIcon("palette/ExpandableListView.png"); // 16x16
        public static final Icon Fragment = loadIcon("palette/fragment.png"); // 16x16
        public static final Icon FrameLayout = loadIcon("palette/FrameLayout.png"); // 16x16
        public static final Icon Gallery = loadIcon("palette/Gallery.png"); // 16x16
        public static final Icon GestureOverlayView = loadIcon("palette/GestureOverlayView.png"); // 16x16
        public static final Icon GridLayout = loadIcon("palette/GridLayout.png"); // 16x16
        public static final Icon GridView = loadIcon("palette/GridView.png"); // 16x16
        public static final Icon HorizontalScrollView = loadIcon("palette/HorizontalScrollView.png"); // 16x16
        public static final Icon ImageButton = loadIcon("palette/ImageButton.png"); // 16x16
        public static final Icon ImageSwitcher = loadIcon("palette/ImageSwitcher.png"); // 16x16
        public static final Icon ImageView = loadIcon("palette/ImageView.png"); // 16x16
        public static final Icon Include = loadIcon("palette/include.png"); // 16x16
        public static final Icon LinearLayout = loadIcon("palette/LinearLayout.png"); // 16x16
        public static final Icon VerticalLinearLayout = loadIcon("palette/VerticalLinearLayout.png"); // 16x16
        public static final Icon LinearLayout3 = loadIcon("palette/LinearLayout3.png"); // 16x16
        public static final Icon ListView = loadIcon("palette/ListView.png"); // 16x16
        public static final Icon MediaController = loadIcon("palette/MediaController.png"); // 16x16
        public static final Icon MultiAutoCompleteTextView = loadIcon("palette/MultiAutoCompleteTextView.png"); // 16x16
        public static final Icon Merge = loadIcon("palette/merge.png"); // 16x16
        public static final Icon NumberPicker = loadIcon("palette/NumberPicker.png"); // 16x16
        public static final Icon ProgressBar = loadIcon("palette/ProgressBar.png"); // 16x16
        public static final Icon QuickContactBadge = loadIcon("palette/QuickContactBadge.png"); // 16x16
        public static final Icon RadioButton = loadIcon("palette/RadioButton.png"); // 16x16
        public static final Icon RadioGroup = loadIcon("palette/RadioGroup.png"); // 16x16
        public static final Icon RatingBar = loadIcon("palette/RatingBar.png"); // 16x16
        public static final Icon RelativeLayout = loadIcon("palette/RelativeLayout.png"); // 16x16
        public static final Icon RequestFocus = loadIcon("palette/requestFocus.png"); // 16x16
        public static final Icon ScrollView = loadIcon("palette/ScrollView.png"); // 16x16
        public static final Icon SearchView = loadIcon("palette/SearchView.png"); // 16x16
        public static final Icon SeekBar = loadIcon("palette/SeekBar.png"); // 16x16
        public static final Icon SlidingDrawer = loadIcon("palette/SlidingDrawer.png"); // 16x16
        public static final Icon Space = loadIcon("palette/Space.png"); // 16x16
        public static final Icon Spinner = loadIcon("palette/Spinner.png"); // 16x16
        public static final Icon StackView = loadIcon("palette/StackView.png"); // 16x16
        public static final Icon SurfaceView = loadIcon("palette/SurfaceView.png"); // 16x16
        public static final Icon Switch = loadIcon("palette/Switch.png"); // 16x16
        public static final Icon TabHost = loadIcon("palette/TabHost.png"); // 16x16
        public static final Icon TableLayout = loadIcon("palette/TableLayout.png"); // 16x16
        public static final Icon TableRow = loadIcon("palette/TableRow.png"); // 16x16
        public static final Icon TabWidget = loadIcon("palette/TabWidget.png"); // 16x16
        public static final Icon TextClock = loadIcon("palette/TextClock.png"); // 16x16
        public static final Icon TextSwitcher = loadIcon("palette/TextSwitcher.png"); // 16x16
        public static final Icon TextureView = loadIcon("palette/TextureView.png"); // 16x16
        public static final Icon TextView = loadIcon("palette/TextView.png"); // 16x16
        public static final Icon TimePicker = loadIcon("palette/TimePicker.png"); // 16x16
        public static final Icon ToggleButton = loadIcon("palette/ToggleButton.png"); // 16x16
        public static final Icon TwoLineListItem = loadIcon("palette/TwoLineListItem.png"); // 16x16
        public static final Icon Unknown = loadIcon("palette/customView.png"); // 16x16
        public static final Icon VideoView = loadIcon("palette/VideoView.png"); // 16x13
        public static final Icon View = loadIcon("palette/View.png"); // 16x16
        public static final Icon ViewAnimator = loadIcon("palette/ViewAnimator.png"); // 16x16
        public static final Icon ViewFlipper = loadIcon("palette/ViewFlipper.png"); // 16x16
        public static final Icon ViewStub = loadIcon("palette/ViewStub.png"); // 16x16
        public static final Icon ViewSwitcher = loadIcon("palette/ViewSwitcher.png"); // 16x16
        public static final Icon WebView = loadIcon("palette/WebView.png"); // 16x16
        public static final Icon ZoomButton = loadIcon("palette/ZoomButton.png"); // 16x16
        public static final Icon ZoomControls = loadIcon("palette/ZoomControls.png"); // 16x16
    }

    public static class Windows {
        public static final Icon Preview = loadIcon("windowPreview_13.png");
    }

    public static class Build {
        public static final Icon CordovaBuildConfiguration = loadIcon("cordovaBuildConfiguration_16.png"); //16x16
    }

    public static final Icon ErrorBadge = loadIcon("errorBadge_16.png"); // 16x16
    public static final Icon WarningBadge = loadIcon("warningBadge_16.png"); // 16x16
}
