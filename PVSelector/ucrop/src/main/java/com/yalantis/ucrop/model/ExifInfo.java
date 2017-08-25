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

package com.yalantis.ucrop.model;

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class ExifInfo {

    private int mExifOrientation;
    private int mExifDegrees;
    private int mExifTranslation;

    public ExifInfo(int exifOrientation, int exifDegrees, int exifTranslation) {
        mExifOrientation = exifOrientation;
        mExifDegrees = exifDegrees;
        mExifTranslation = exifTranslation;
    }

    public int getExifOrientation() {
        return mExifOrientation;
    }

    public int getExifDegrees() {
        return mExifDegrees;
    }

    public int getExifTranslation() {
        return mExifTranslation;
    }

    public void setExifOrientation(int exifOrientation) {
        mExifOrientation = exifOrientation;
    }

    public void setExifDegrees(int exifDegrees) {
        mExifDegrees = exifDegrees;
    }

    public void setExifTranslation(int exifTranslation) {
        mExifTranslation = exifTranslation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExifInfo exifInfo = (ExifInfo) o;

        if (mExifOrientation != exifInfo.mExifOrientation) return false;
        if (mExifDegrees != exifInfo.mExifDegrees) return false;
        return mExifTranslation == exifInfo.mExifTranslation;

    }

    @Override
    public int hashCode() {
        int result = mExifOrientation;
        result = 31 * result + mExifDegrees;
        result = 31 * result + mExifTranslation;
        return result;
    }

}
