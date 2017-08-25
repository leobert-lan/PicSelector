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
import android.support.annotation.DrawableRes;

import thirdparty.leobert.pvselectorlib.model.FunctionConfig;

/**
 * 抽象选择器，实现样式定义
 * Created by leobert on 2017/2/8.
 */

abstract class AbsSelector<T> implements ICustomStyle<T> {

    protected final FunctionConfig config;

    public abstract FunctionConfig getConfig();

    protected abstract T self();

    AbsSelector() {
        config = new FunctionConfig();
        getConfig().setDisplayCandidateNo(false);
    }

    @Override
    public T setImageSpanCount(int spanCount) {
        getConfig().setImageSpanCount(spanCount);
        return self();
    }

    @Override
    public T setThemeColor(@ColorInt int mainColor) {
        getConfig().setThemeColor(mainColor);
        return self();
    }


    @Override
    public T setBottomBarBgColor(@ColorInt int color) {
        getConfig().setBottomBgColor(color);
        return self();
    }

    @Override
    public T setPreviewTxtColor(@ColorInt int color) {
        getConfig().setPreviewTxtColor(color);
        return self();
    }

    @Override
    public T setCompleteTxtColor(@ColorInt int color) {
        getConfig().setCompleteTxtColor(color);
        return self();
    }

    @Override
    public T enableDisplayCandidateNo() {
        getConfig().setDisplayCandidateNo(true);
        return self();
    }

    @Override
    public T setCheckedBoxDrawable(@DrawableRes int drawableResId) {
        getConfig().setCheckedBoxDrawable(drawableResId);
        return self();
    }
}
