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

package thirdparty.leobert.pvselectorlib;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * 自定义样式
 * Created by leobert on 2017/2/8.
 */

public interface ICustomStyle<T> {

    /**
     * @param spanCount 单行数量上限
     */
    T setImageSpanCount(int spanCount);

    /**
     * @param mainColor 主色 Context.getColor(resId)
     */
    T setThemeColor(@ColorInt int mainColor);

    /**
     * 设置选择图片页面底部背景色
     *
     * @param color
     */
    T setBottomBarBgColor(@ColorInt int color);


    /**
     * "预览"文字颜色
     *
     * @param color
     */
    T setPreviewTxtColor(@ColorInt int color);

    /**
     * “已完成”文字颜色
     *
     * @param color
     */
    T setCompleteTxtColor(@ColorInt int color);

    /**
     * 设置完成选取的文字
     * @param completeText
     */
    T setCompleteText(CharSequence completeText);

    /**
     * 启用计数checkbox
     */
    T enableDisplayCandidateNo();

    T setCheckedBoxDrawable(@DrawableRes int drawableResId);

}
