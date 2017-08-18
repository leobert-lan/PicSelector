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

package thirdparty.leobert.pvselectorlib.model;

import android.graphics.Color;

import java.io.Serializable;

import thirdparty.leobert.pvselectorlib.R;

/**
 * <p><b>Package:</b> thirdparty.leobert.pvselectorlib.model </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> UIConfig </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/18.
 */

public class UIConfig implements Serializable {
    private int imageSpanCount = 4; // 列表每行显示个数
    private int themeStyle = Color.parseColor("#393a3e"); // 标题栏背景色;

    private int checkedBoxDrawable = R.drawable.checkbox_selector;// 图片选择默认样式
    private boolean displayCandidateNo = false;// 是否显示QQ风格选择图片

    private int previewTxtColor = Color.parseColor("#FA632D"); // 底部预览字体颜色
    private int completeTxtColor = Color.parseColor("#FA632D"); // 底部完成字体颜色

    private int bottomBgColor = Color.parseColor("#fafafa"); // 底部背景色
    protected int previewBottomBgColor = Color.parseColor("#dd393a3e"); // 预览底部背景色

    public int getImageSpanCount() {
        return imageSpanCount;
    }

    public void setImageSpanCount(int imageSpanCount) {
        this.imageSpanCount = imageSpanCount;
    }

    public int getThemeStyle() {
        return themeStyle;
    }

    public void setThemeStyle(int themeStyle) {
        this.themeStyle = themeStyle;
    }

    public int getCheckedBoxDrawable() {
        return checkedBoxDrawable;
    }

    public void setCheckedBoxDrawable(int checkedBoxDrawable) {
        this.checkedBoxDrawable = checkedBoxDrawable;
    }

    public boolean isDisplayCandidateNo() {
        return displayCandidateNo;
    }

    public void setDisplayCandidateNo(boolean displayCandidateNo) {
        this.displayCandidateNo = displayCandidateNo;
    }

    public int getPreviewTxtColor() {
        return previewTxtColor;
    }

    public void setPreviewTxtColor(int previewTxtColor) {
        this.previewTxtColor = previewTxtColor;
    }

    public int getCompleteTxtColor() {
        return completeTxtColor;
    }

    public void setCompleteTxtColor(int completeTxtColor) {
        this.completeTxtColor = completeTxtColor;
    }

    public int getBottomBgColor() {
        return bottomBgColor;
    }

    public void setBottomBgColor(int bottomBgColor) {
        this.bottomBgColor = bottomBgColor;
    }

    public int getPreviewBottomBgColor() {
        return previewBottomBgColor;
    }

    public void setPreviewBottomBgColor(int previewBottomBgColor) {
        this.previewBottomBgColor = previewBottomBgColor;
    }


    interface UIConfigDelegate {
        int getImageSpanCount();

        void setImageSpanCount(int imageSpanCount);

        int getThemeStyle();

        void setThemeStyle(int themeStyle);

        int getCheckedBoxDrawable();

        void setCheckedBoxDrawable(int checkedBoxDrawable);

        boolean isDisplayCandidateNo();

        void setDisplayCandidateNo(boolean displayCandidateNo);

        int getPreviewTxtColor();

        void setPreviewTxtColor(int previewTxtColor);

        int getCompleteTxtColor();

        void setCompleteTxtColor(int completeTxtColor);

        int getBottomBgColor();

        void setBottomBgColor(int bottomBgColor);

        int getPreviewBottomBgColor();

        void setPreviewBottomBgColor(int previewBottomBgColor);
    }
}
