/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of instance software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and instance permission notice shall be included in all
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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yalantis.ucrop.annotation.UCropMode;
import com.yalantis.ucrop.ui.PictureMultiUCropActivity;

/**
 * <p><b>Package:</b> com.yalantis.ucrop </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> UCrop </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/22.
 */

public abstract class UCrop<T extends UCrop> {
    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;

    protected Intent mCropIntent;
    protected Bundle mCropOptionsBundle;

    protected T instance;

    UCrop(@NonNull Uri source, @NonNull Uri destination) {
        mCropIntent = new Intent();
        mCropOptionsBundle = new Bundle();
        mCropOptionsBundle.putParcelable(UcropConsts.Extra.EXTRA_INPUT_URI,
                source);
        mCropOptionsBundle.putParcelable(UcropConsts.Extra.EXTRA_OUTPUT_URI,
                destination);

    }

    /**
     * Set an aspect ratio for crop bounds.
     * User won't see the menu with other ratios options.
     *
     * @param x aspect ratio X
     * @param y aspect ratio Y
     */
    public T withAspectRatio(float x, float y) {
        mCropOptionsBundle.putFloat(UcropConsts.Extra.EXTRA_ASPECT_RATIO_X, x);
        mCropOptionsBundle.putFloat(UcropConsts.Extra.EXTRA_ASPECT_RATIO_Y, y);
        return instance;
    }

    /**
     * Set an aspect ratio for crop bounds that is evaluated from source image width and height.
     * User won't see the menu with other ratios options.
     */
    public T useSourceImageAspectRatio() {
        mCropOptionsBundle.putFloat(UcropConsts.Extra.EXTRA_ASPECT_RATIO_X, 0);
        mCropOptionsBundle.putFloat(UcropConsts.Extra.EXTRA_ASPECT_RATIO_Y, 0);
        return instance;
    }

    /**
     * Set maximum size for result cropped image.
     *
     * @param width  max cropped image width
     * @param height max cropped image height
     */
    public T withMaxResultSize(@IntRange(from = 100) int width,
                               @IntRange(from = 100) int height) {
        mCropOptionsBundle.putInt(UcropConsts.Extra.EXTRA_MAX_SIZE_X, width);
        mCropOptionsBundle.putInt(UcropConsts.Extra.EXTRA_MAX_SIZE_Y, height);
        return instance;
    }

    public T withOptions(@NonNull Options options) {
        mCropOptionsBundle.putAll(options.getOptionBundle());
        return instance;
    }

    /**
     * Send the crop Intent from an Activity
     *
     * @param activity Activity to receive result
     */
    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_CROP);
    }

    /**
     * Send the crop Intent from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    /**
     * Send the crop Intent from a Fragment
     *
     * @param fragment Fragment to receive result
     */
    public void start(@NonNull Context context, @NonNull Fragment fragment) {
        start(context, fragment, REQUEST_CROP);
    }

    /**
     * Send the crop Intent from a support library Fragment
     *
     * @param fragment Fragment to receive result
     */
    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment) {
        start(context, fragment, REQUEST_CROP);
    }

    /**
     * Send the crop Intent with a custom request code
     *
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Send the crop Intent with a custom request code
     *
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Get Intent to start
     */
    public abstract Intent getIntent(@NonNull Context context);

    /**
     * Retrieve cropped image Uri from the result Intent
     *
     * @param intent crop result intent
     */
    @Nullable
    public static Uri getOutput(@NonNull Intent intent) {
        return intent.getParcelableExtra(UcropConsts.Extra.EXTRA_OUTPUT_URI);
    }

    public static Uri getInput(@NonNull Intent intent) {
        return intent.getParcelableExtra(UcropConsts.Extra.EXTRA_INPUT_URI);
    }

    /**
     * Retrieve the width of the cropped image
     *
     * @param intent crop result intent
     */
    public static int getOutputImageWidth(@NonNull Intent intent) {
        return intent.getIntExtra(UcropConsts.Extra.EXTRA_OUTPUT_IMAGE_WIDTH, -1);
    }

    /**
     * Retrieve the height of the cropped image
     *
     * @param intent crop result intent
     */
    public static int getOutputImageHeight(@NonNull Intent intent) {
        return intent.getIntExtra(UcropConsts.Extra.EXTRA_OUTPUT_IMAGE_HEIGHT, -1);
    }

    /**
     * Retrieve cropped image aspect ratio from the result Intent
     *
     * @param intent crop result intent
     * @return aspect ratio as a floating point value (x:y)
     * - so it will be 1 for 1:1 or 4/3 for 4:3
     */
    public static float getOutputCropAspectRatio(@NonNull Intent intent) {
        return intent.getParcelableExtra(UcropConsts.Extra
                .EXTRA_OUTPUT_CROP_ASPECT_RATIO);
    }

    /**
     * Method retrieves error from the result intent.
     *
     * @param result crop result Intent
     * @return Throwable that could happen while image processing
     */
    @Nullable
    public static Throwable getError(@NonNull Intent result) {
        return (Throwable) result.getSerializableExtra(UcropConsts.Extra.EXTRA_ERROR);
    }

    public static int getCropPosition(Intent intent) {
        return intent.getIntExtra(UcropConsts.Extra.EXTRA_CROP_POSITION, 0);
    }

    @UCropMode
    @SuppressWarnings("ResourceType")
    public static int getCropMode(Intent intent) {
        return intent.getIntExtra(UcropConsts.Extra.EXTRA_CROPMODE,
                UcropConsts.CROP_MODE_DEFAULT);
    }

    public static float getAspectRatioX(@NonNull Intent intent) {
        return intent.getFloatExtra(UcropConsts.Extra.EXTRA_ASPECT_RATIO_X, 0);
    }

    public static float getAspectRatioY(@NonNull Intent intent) {
        return intent.getFloatExtra(UcropConsts.Extra.EXTRA_ASPECT_RATIO_Y, 0);
    }

}
