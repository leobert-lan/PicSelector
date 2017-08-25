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

package com.yalantis.ucrop.entity;


import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * local "Media" file，picture or video
 */
public class LocalMedia implements Serializable {

    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_PICTURE, TYPE_VIDEO})
    public @interface MediaType {
    }

    private String path;
    private String compressPath;
    private String croppedPath;
    private long duration;
    private long lastUpdateAt;
    private boolean isCropped;
//    /**
//    private boolean isSelectedMediaContains;
//     *
//     */
//    private int position;
//    private int num;

    private
    @MediaType
    int type;

    private boolean compressed;

    private final GridItemInfoHolder gridItemInfoHolder
            = new GridItemInfoHolder();

    public LocalMedia(String path, long lastUpdateAt, long duration, @MediaType int type) {
        this.path = path;
        this.duration = duration;
        this.lastUpdateAt = lastUpdateAt;
        this.type = type;
    }

    public LocalMedia(String path, long duration, long lastUpdateAt,
                      boolean isChecked, int position, int num, @MediaType int type) {
        this.path = path;
        this.duration = duration;
        this.lastUpdateAt = lastUpdateAt;
//        this.isSelectedMediaContains = isSelectedMediaContains;
//        this.position = position;
//        this.num = num;
        this.type = type;
    }

    public LocalMedia() {
    }

    public String getCroppedPath() {
        return croppedPath;
    }

    public void setCroppedPath(String croppedPath) {
        this.croppedPath = croppedPath;
    }

    public boolean isCropped() {
        return isCropped;
    }

    public void setCropped(boolean cropped) {
        isCropped = cropped;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

//    public int getNum() {
//        return num;
//    }
//
//    public void setNum(int num) {
//        this.num = num;
//    }

    @MediaType
    public int getType() {
        return type;
    }

    public void setType(@MediaType int type) {
        this.type = type;
    }


    public GridItemInfoHolder getGridItemInfoHolder() {
        return gridItemInfoHolder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

//    public boolean getIsChecked() {
//        return this.isSelectedMediaContains;
//    }
//
//    public void setIsChecked(boolean isSelectedMediaContains) {
//        this.isSelectedMediaContains = isSelectedMediaContains;
//    }

//    public int getPosition() {
//        return this.position;
//    }
//
//    public void setPosition(int position) {
//        this.position = position;
//    }

    @Override
    public String toString() {
        return "original path:" + this.path;
    }
}
