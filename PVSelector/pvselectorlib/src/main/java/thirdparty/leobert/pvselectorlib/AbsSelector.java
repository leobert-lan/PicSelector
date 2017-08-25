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
