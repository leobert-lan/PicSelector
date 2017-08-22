/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.yalantis.ucrop;

/**
 * <p><b>Package:</b> com.yalantis.ucrop </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> Consts </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/22.
 */

public interface UcropConsts {
    interface Extra {

        String EXTRA_SERIALIZABLE_RESULT = "DATA_RESULT_SERIALIZABLE";

        /**
         * used to tag the position of the current media(only Picture)
         * in the selected media list, and this tagged media
         * will be cropped in the Multi-Crop-Mode.
         */
        String EXTRA_CROP_POSITION = "DATA_CROP_POSITION";

        String EXTRA_MEDIA_LIST = "DATA_LIST_MEDIA";

        String EXTRA_COMPRESS_ENABLED = "DATA_IS_COMPRESS_ENABLED";

        String EXTRA_CROPMODE = "DATA_CROP_MODE";

        String EXTRA_COLOR_BACKGROUND = "DATA_COLOR_BACKGROUND";

        String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
        String EXTRA_COMPRESSION_FORMAT_NAME = EXTRA_PREFIX + ".CompressionFormatName";
        String EXTRA_COMPRESSION_QUALITY = EXTRA_PREFIX + ".CompressionQuality";

        String EXTRA_ALLOWED_GESTURES = EXTRA_PREFIX + ".AllowedGestures";

        String EXTRA_MAX_BITMAP_SIZE = EXTRA_PREFIX + ".MaxBitmapSize";
        String EXTRA_MAX_SCALE_MULTIPLIER = EXTRA_PREFIX + ".MaxScaleMultiplier";
        String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = EXTRA_PREFIX + ".ImageToCropBoundsAnimDuration";

        String EXTRA_DIMMED_LAYER_COLOR = EXTRA_PREFIX + ".DimmedLayerColor";
        String EXTRA_CIRCLE_DIMMED_LAYER = EXTRA_PREFIX + ".CircleDimmedLayer";

        String EXTRA_SHOW_CROP_FRAME = EXTRA_PREFIX + ".ShowCropFrame";
        String EXTRA_CROP_FRAME_COLOR = EXTRA_PREFIX + ".CropFrameColor";
        String EXTRA_CROP_FRAME_STROKE_WIDTH = EXTRA_PREFIX + ".CropFrameStrokeWidth";

        String EXTRA_SHOW_CROP_GRID = EXTRA_PREFIX + ".ShowCropGrid";
        String EXTRA_CROP_GRID_ROW_COUNT = EXTRA_PREFIX + ".CropGridRowCount";
        String EXTRA_CROP_GRID_COLUMN_COUNT = EXTRA_PREFIX + ".CropGridColumnCount";
        String EXTRA_CROP_GRID_COLOR = EXTRA_PREFIX + ".CropGridColor";
        String EXTRA_CROP_GRID_STROKE_WIDTH = EXTRA_PREFIX + ".CropGridStrokeWidth";

        String EXTRA_TOOL_BAR_COLOR = EXTRA_PREFIX + ".ToolbarColor";
        String EXTRA_STATUS_BAR_COLOR = EXTRA_PREFIX + ".StatusBarColor";
        String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = EXTRA_PREFIX + ".UcropColorWidgetActive";

        String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = EXTRA_PREFIX + ".UcropToolbarWidgetColor";
        String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = EXTRA_PREFIX + ".UcropToolbarTitleText";
        String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = EXTRA_PREFIX + ".UcropToolbarCancelDrawable";
        String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = EXTRA_PREFIX + ".UcropToolbarCropDrawable";

        String EXTRA_UCROP_LOGO_COLOR = EXTRA_PREFIX + ".UcropLogoColor";

        String EXTRA_HIDE_BOTTOM_CONTROLS = EXTRA_PREFIX + ".HideBottomControls";
        String EXTRA_FREE_STYLE_CROP = EXTRA_PREFIX + ".FreeStyleCrop";

        String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = EXTRA_PREFIX + ".AspectRatioSelectedByDefault";
        String EXTRA_ASPECT_RATIO_OPTIONS = EXTRA_PREFIX + ".AspectRatioOptions";

        String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = EXTRA_PREFIX + ".UcropRootViewBackgroundColor";

        String EXTRA_INPUT_URI = EXTRA_PREFIX + ".InputUri";
        String EXTRA_OUTPUT_URI = EXTRA_PREFIX + ".OutputUri";
        String EXTRA_OUTPUT_CROP_ASPECT_RATIO = EXTRA_PREFIX + ".CropAspectRatio";
        String EXTRA_OUTPUT_IMAGE_WIDTH = EXTRA_PREFIX + ".ImageWidth";
        String EXTRA_OUTPUT_IMAGE_HEIGHT = EXTRA_PREFIX + ".ImageHeight";
        String EXTRA_ERROR = EXTRA_PREFIX + ".Error";

        String EXTRA_ASPECT_RATIO_X = EXTRA_PREFIX + ".AspectRatioX";
        String EXTRA_ASPECT_RATIO_Y = EXTRA_PREFIX + ".AspectRatioY";

        String EXTRA_MAX_SIZE_X = EXTRA_PREFIX + ".MaxSizeX";
        String EXTRA_MAX_SIZE_Y = EXTRA_PREFIX + ".MaxSizeY";
    }

    int CROP_MODE_DEFAULT = 0;
    int CROP_MODE_1_1 = 11;
    int CROP_MODE_3_4 = 34;
    int CROP_MODE_3_2 = 32;
    int CROP_MODE_16_9 = 169;




    /**
     * refers to broadcast actions
     */
    interface BcActions {


        /**
         * resolve the cropped image(s) in the bundle of the intent.broadcast-action
         * will be sent when image(s) cropped
         */
        String ACTION_IMAGE_CROPPED = "app.action.image_cropped";
    }
}
