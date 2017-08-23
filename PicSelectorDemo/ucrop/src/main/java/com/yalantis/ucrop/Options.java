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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yalantis.ucrop.annotation.UCropMode;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.ui.PictureMultiUCropActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * <p><b>Package:</b> com.yalantis.ucrop </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> Options </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/22.
 */

public interface Options extends UcropConsts.Extra {

    @NonNull
    Bundle getOptionBundle();

    /**
     * Set one of {@link Bitmap.CompressFormat} that will
     * be used to save resulting Bitmap.
     */
    void setCompressionFormat(@NonNull Bitmap.CompressFormat format);

    void backgroundColor(int color);

    void cropMode(@UCropMode int cropMode);

    /**
     * Set compression quality [0-100] that will be used to save resulting Bitmap.
     */
    void setCompressionQuality(@IntRange(from = 0) int compressQuality);

    /**
     * Set images
     */
    void setLocalMedia(List<LocalMedia> medias);

    /**
     * Set images position
     */
    void setPosition(int cropIndex);


    /**
     * Choose what set of gestures will be enabled on each tab - if any.
     */
    void setAllowedGestures(@PictureMultiUCropActivity.GestureTypes int tabScale,
                            @PictureMultiUCropActivity.GestureTypes int tabRotate,
                            @PictureMultiUCropActivity.GestureTypes int tabAspectRatio);

    /**
     * This method sets multiplier that is used to calculate max image scale from min image scale.
     *
     * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
     */
    void setMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false)
                                       float maxScaleMultiplier);

    /**
     * This method sets animation duration for image to wrap the crop bounds
     *
     * @param durationMillis - duration in milliseconds
     */
    void setImageToCropBoundsAnimDuration(@IntRange(from = 100) int durationMillis);


    /**
     * Setter for max size for both width and height of bitmap that will be decoded from an input Uri and used in the view.
     *
     * @param maxBitmapSize - size in pixels
     */
    void setMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize);

    /**
     * @param color - desired color of dimmed area around the crop bounds
     */
    void setDimmedLayerColor(@ColorInt int color);

    /**
     * @param isCircle - set it to true if you want dimmed layer to have an circle inside
     */
    void setCircleDimmedLayer(boolean isCircle);

    /**
     * @param show - set to true if you want to see a crop frame rectangle on top of an image
     */
    void setShowCropFrame(boolean show);

    /**
     * @param color - desired color of crop frame
     */
    void setCropFrameColor(@ColorInt int color);

    /**
     * @param width - desired width of crop frame line in pixels
     */
    void setCropFrameStrokeWidth(@IntRange(from = 0) int width);

    /**
     * @param show - set to true if you want to see a crop grid/guidelines on top of an image
     */
    void setShowCropGrid(boolean show);

    /**
     * @param count - crop grid rows count.
     */
    void setCropGridRowCount(@IntRange(from = 0) int count);

    /**
     * @param count - crop grid columns count.
     */
    void setCropGridColumnCount(@IntRange(from = 0) int count);

    /**
     * @param color - desired color of crop grid/guidelines
     */
    void setCropGridColor(@ColorInt int color);

    /**
     * @param width - desired width of crop grid lines in pixels
     */
    void setCropGridStrokeWidth(@IntRange(from = 0) int width);

    /**
     * @param color - desired resolved color of the toolbar
     */
    void setToolbarColor(@ColorInt int color);

    /**
     * @param color - desired resolved color of the statusbar
     */
    void setStatusBarColor(@ColorInt int color);

    /**
     * @param color - desired resolved color of the active
     *              and selected widget (default is orange)
     *              and progress wheel middle line
     */
    void setActiveWidgetColor(@ColorInt int color);

    /**
     * @param color - desired resolved color of Toolbar text and buttons (default is darker orange)
     */
    void setToolbarWidgetColor(@ColorInt int color);

    /**
     * @param text - desired text for Toolbar title
     */
    void setToolbarTitle(@Nullable String text);

    /**
     * @param drawable - desired drawable for the Toolbar left cancel icon
     */
    void setToolbarCancelDrawable(@DrawableRes int drawable);

    /**
     * @param drawable - desired drawable for the Toolbar right crop icon
     */
    void setToolbarCropDrawable(@DrawableRes int drawable);

    /**
     * @param color - desired resolved color of logo fill (default is darker grey)
     */
    void setLogoColor(@ColorInt int color);

    /**
     * @param hide - set to true to hide the bottom controls (shown by default)
     */
    void setHideBottomControls(boolean hide);

    /**
     * @param enabled - set to true to let user resize crop bounds (disabled by default)
     */
    void setFreeStyleCropEnabled(boolean enabled);

    /**
     * Pass an ordered list of desired aspect ratios that should be available for a user.
     *
     * @param selectedByDefault - index of aspect ratio option that is selected by default (starts with 0).
     * @param aspectRatio       - list of aspect ratio options that are available to user
     */
    void setAspectRatioOptions(int selectedByDefault,
                               AspectRatio... aspectRatio);

    /**
     * @param color - desired background color that should be applied to the root view
     */
    void setRootViewBackgroundColor(@ColorInt int color);

    /**
     * Set an aspect ratio for crop bounds.
     * User won't see the menu with other ratios options.
     *
     * @param x aspect ratio X
     * @param y aspect ratio Y
     */
    void withAspectRatio(float x, float y);


    /**
     * Set an aspect ratio for crop bounds that is evaluated from source image width and height.
     * User won't see the menu with other ratios options.
     */
    void useSourceImageAspectRatio();

    /**
     * Set maximum size for result cropped image.
     *
     * @param width  max cropped image width
     * @param height max cropped image height
     */
    void withMaxResultSize(@IntRange(from = 0) int width,
                           @IntRange(from = 0) int height);

    void setCompressEnabled(boolean enableCompress);


    class OptionsImpl implements Options {
        private final Bundle mOptionBundle;

        public OptionsImpl() {
            this.mOptionBundle = new Bundle();
        }

        @NonNull
        @Override
        public Bundle getOptionBundle() {
            return mOptionBundle;
        }

        /**
         * Set one of {@link Bitmap.CompressFormat} that will
         * be used to save resulting Bitmap.
         */
        @Override
        public void setCompressionFormat(@NonNull Bitmap.CompressFormat format) {
            mOptionBundle.putString(EXTRA_COMPRESSION_FORMAT_NAME, format.name());
        }

        @Override
        public void backgroundColor(int color) {
            mOptionBundle.putInt(EXTRA_COLOR_BACKGROUND, color);
        }

        @Override
        public void cropMode(@UCropMode int cropMode) {
            mOptionBundle.putInt(EXTRA_CROPMODE, cropMode);
        }

        /**
         * Set compression quality [0-100] that will be used to save resulting Bitmap.
         */
        @Override
        public void setCompressionQuality(@IntRange(from = 0) int compressQuality) {
            mOptionBundle.putInt(EXTRA_COMPRESSION_QUALITY, compressQuality);
        }

        /**
         * Set images
         */
        @Override
        public void setLocalMedia(List<LocalMedia> medias) {
            mOptionBundle.putSerializable(UcropConsts.Extra.EXTRA_MEDIA_LIST,
                    (Serializable) medias);
        }

        /**
         * Set images position
         */
        @Override
        public void setPosition(int cropIndex) {
            mOptionBundle.putInt(UcropConsts.Extra.EXTRA_CROP_POSITION, cropIndex);
        }


        /**
         * Choose what set of gestures will be enabled on each tab - if any.
         */
        @Override
        public void setAllowedGestures(@PictureMultiUCropActivity.GestureTypes int tabScale,
                                       @PictureMultiUCropActivity.GestureTypes int tabRotate,
                                       @PictureMultiUCropActivity.GestureTypes int tabAspectRatio) {
            mOptionBundle.putIntArray(EXTRA_ALLOWED_GESTURES, new int[]{tabScale, tabRotate, tabAspectRatio});
        }

        /**
         * This method sets multiplier that is used to calculate max image scale from min image scale.
         *
         * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
         */
        @Override
        public void setMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false) float maxScaleMultiplier) {
            mOptionBundle.putFloat(EXTRA_MAX_SCALE_MULTIPLIER, maxScaleMultiplier);
        }

        /**
         * This method sets animation duration for image to wrap the crop bounds
         *
         * @param durationMillis - duration in milliseconds
         */
        @Override
        public void setImageToCropBoundsAnimDuration(@IntRange(from = 100) int durationMillis) {
            mOptionBundle.putInt(EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, durationMillis);
        }

        /**
         * Setter for max size for both width and height of bitmap that will be decoded from an input Uri and used in the view.
         *
         * @param maxBitmapSize - size in pixels
         */
        @Override
        public void setMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize) {
            mOptionBundle.putInt(EXTRA_MAX_BITMAP_SIZE, maxBitmapSize);
        }

        /**
         * @param color - desired color of dimmed area around the crop bounds
         */
        @Override
        public void setDimmedLayerColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_DIMMED_LAYER_COLOR, color);
        }

        /**
         * @param isCircle - set it to true if you want dimmed layer to have an circle inside
         */
        @Override
        public void setCircleDimmedLayer(boolean isCircle) {
            mOptionBundle.putBoolean(EXTRA_CIRCLE_DIMMED_LAYER, isCircle);
        }

        /**
         * @param show - set to true if you want to see a crop frame rectangle on top of an image
         */
        @Override
        public void setShowCropFrame(boolean show) {
            mOptionBundle.putBoolean(EXTRA_SHOW_CROP_FRAME, show);
        }

        /**
         * @param color - desired color of crop frame
         */
        @Override
        public void setCropFrameColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_CROP_FRAME_COLOR, color);
        }

        /**
         * @param width - desired width of crop frame line in pixels
         */
        @Override
        public void setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
            mOptionBundle.putInt(EXTRA_CROP_FRAME_STROKE_WIDTH, width);
        }

        /**
         * @param show - set to true if you want to see a crop grid/guidelines on top of an image
         */
        @Override
        public void setShowCropGrid(boolean show) {
            mOptionBundle.putBoolean(EXTRA_SHOW_CROP_GRID, show);
        }

        /**
         * @param count - crop grid rows count.
         */
        @Override
        public void setCropGridRowCount(@IntRange(from = 0) int count) {
            mOptionBundle.putInt(EXTRA_CROP_GRID_ROW_COUNT, count);
        }

        /**
         * @param count - crop grid columns count.
         */
        @Override
        public void setCropGridColumnCount(@IntRange(from = 0) int count) {
            mOptionBundle.putInt(EXTRA_CROP_GRID_COLUMN_COUNT, count);
        }

        /**
         * @param color - desired color of crop grid/guidelines
         */
        @Override
        public void setCropGridColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_CROP_GRID_COLOR, color);
        }

        /**
         * @param width - desired width of crop grid lines in pixels
         */
        @Override
        public void setCropGridStrokeWidth(@IntRange(from = 0) int width) {
            mOptionBundle.putInt(EXTRA_CROP_GRID_STROKE_WIDTH, width);
        }

        /**
         * @param color - desired resolved color of the toolbar
         */
        @Override
        public void setToolbarColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_TOOL_BAR_COLOR, color);
        }

        /**
         * @param color - desired resolved color of the statusbar
         */
        @Override
        public void setStatusBarColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_STATUS_BAR_COLOR, color);
        }

        /**
         * @param color - desired resolved color of the active and selected widget (default is orange) and progress wheel middle line
         */
        @Override
        public void setActiveWidgetColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_UCROP_COLOR_WIDGET_ACTIVE, color);
        }

        /**
         * @param color - desired resolved color of Toolbar text and buttons (default is darker orange)
         */
        @Override
        public void setToolbarWidgetColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, color);
        }

        /**
         * @param text - desired text for Toolbar title
         */
        @Override
        public void setToolbarTitle(@Nullable String text) {
            mOptionBundle.putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, text);
        }

        /**
         * @param drawable - desired drawable for the Toolbar left cancel icon
         */
        @Override
        public void setToolbarCancelDrawable(@DrawableRes int drawable) {
            mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, drawable);
        }

        /**
         * @param drawable - desired drawable for the Toolbar right crop icon
         */
        @Override
        public void setToolbarCropDrawable(@DrawableRes int drawable) {
            mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CROP_DRAWABLE, drawable);
        }

        /**
         * @param color - desired resolved color of logo fill (default is darker grey)
         */
        @Override
        public void setLogoColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_UCROP_LOGO_COLOR, color);
        }

        /**
         * @param hide - set to true to hide the bottom controls (shown by default)
         */
        @Override
        public void setHideBottomControls(boolean hide) {
            mOptionBundle.putBoolean(EXTRA_HIDE_BOTTOM_CONTROLS, hide);
        }

        /**
         * @param enabled - set to true to let user resize crop bounds (disabled by default)
         */
        @Override
        public void setFreeStyleCropEnabled(boolean enabled) {
            mOptionBundle.putBoolean(EXTRA_FREE_STYLE_CROP, enabled);
        }

        /**
         * Pass an ordered list of desired aspect ratios that should be available for a user.
         *
         * @param selectedByDefault - index of aspect ratio option that is selected by default (starts with 0).
         * @param aspectRatio       - list of aspect ratio options that are available to user
         */
        @Override
        public void setAspectRatioOptions(int selectedByDefault,
                                          AspectRatio... aspectRatio) {
            if (selectedByDefault > aspectRatio.length) {
                throw new IllegalArgumentException(String.format(Locale.US,
                        "Index [selectedByDefault = %d] cannot be higher than aspect ratio options count [count = %d].",
                        selectedByDefault, aspectRatio.length));
            }
            mOptionBundle.putInt(EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT,
                    selectedByDefault);
            mOptionBundle.putParcelableArrayList(EXTRA_ASPECT_RATIO_OPTIONS,
                    new ArrayList<Parcelable>(Arrays.asList(aspectRatio)));
        }

        /**
         * @param color - desired background color that
         *              should be applied to the root view
         */
        @Override
        public void setRootViewBackgroundColor(@ColorInt int color) {
            mOptionBundle.putInt(EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, color);
        }

        /**
         * Set an aspect ratio for crop bounds.
         * User won't see the menu with other ratios options.
         *
         * @param x aspect ratio X
         * @param y aspect ratio Y
         */
        @Override
        public void withAspectRatio(float x, float y) {
            mOptionBundle.putFloat(EXTRA_ASPECT_RATIO_X, x);
            mOptionBundle.putFloat(EXTRA_ASPECT_RATIO_Y, y);
        }


        /**
         * Set an aspect ratio for crop bounds that is evaluated
         * from source image width and height.
         * User won't see the menu with other ratios options.
         */
        @Override
        public void useSourceImageAspectRatio() {
            mOptionBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0);
            mOptionBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0);
        }

        /**
         * Set maximum size for result cropped image.
         *
         * @param width  max cropped image width
         * @param height max cropped image height
         */
        @Override
        public void withMaxResultSize(@IntRange(from = 0) int width,
                                      @IntRange(from = 0) int height) {
            mOptionBundle.putInt(EXTRA_MAX_SIZE_X, width);
            mOptionBundle.putInt(EXTRA_MAX_SIZE_Y, height);
        }

        @Override
        public void setCompressEnabled(boolean enableCompress) {
            mOptionBundle.putBoolean(UcropConsts.Extra.EXTRA_COMPRESS_ENABLED,
                    enableCompress);
        }
    }
}
