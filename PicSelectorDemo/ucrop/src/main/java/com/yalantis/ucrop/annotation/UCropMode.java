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

package com.yalantis.ucrop.annotation;

/**
 * <p><b>Package:</b> com.yalantis.ucrop.annotation </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> CropMode </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/22.
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.yalantis.ucrop.UcropConsts.CROP_MODE_16_9;
import static com.yalantis.ucrop.UcropConsts.CROP_MODE_1_1;
import static com.yalantis.ucrop.UcropConsts.CROP_MODE_3_2;
import static com.yalantis.ucrop.UcropConsts.CROP_MODE_3_4;
import static com.yalantis.ucrop.UcropConsts.CROP_MODE_DEFAULT;

/**
 * scopes of crop
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CROP_MODE_DEFAULT,
        CROP_MODE_1_1,
        CROP_MODE_3_4,
        CROP_MODE_3_2,
        CROP_MODE_16_9})
@Inherited
public @interface UCropMode {
}
