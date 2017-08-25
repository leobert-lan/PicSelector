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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.yalantis.ucrop.ui.PictureSingleUCropActivity;

/**
 * Builder class to ease Intent setup.
 */
public class SingleUCrop extends UCrop<SingleUCrop>{

    /**
     * This method creates new Intent builder and sets both source and destination image URIs.
     *
     * @param source      Uri for image to crop
     * @param destination Uri for saving the cropped image
     */
    public static SingleUCrop of(@NonNull Uri source, @NonNull Uri destination) {
        return new SingleUCrop(source, destination);
    }

    private SingleUCrop(@NonNull Uri source, @NonNull Uri destination) {
        super(source, destination);
        instance = this;
    }

    /**
     * Get Intent to start {@link PictureSingleUCropActivity}
     *
     * @return Intent for {@link PictureSingleUCropActivity}
     */
    public Intent getIntent(@NonNull Context context) {
        mCropIntent.setClass(context, PictureSingleUCropActivity.class);
        mCropIntent.putExtras(mCropOptionsBundle);
        return mCropIntent;
    }

}
