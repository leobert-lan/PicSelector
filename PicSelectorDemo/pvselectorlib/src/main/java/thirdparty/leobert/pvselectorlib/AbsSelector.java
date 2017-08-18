package thirdparty.leobert.pvselectorlib;

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
    public T setThemeColor(int mainColor) {
        getConfig().setThemeStyle(mainColor);
        return self();
    }

    @Override
    public T setBottomBgColor(int bottomBgColor) {
        getConfig().setBottomBgColor(bottomBgColor);
        return self();
    }

    @Override
    public T setPreviewColor(int previewColor) {
        getConfig().setPreviewTxtColor(previewColor);
        return self();
    }

    @Override
    public T setCompleteColor(int completeColor) {
        getConfig().setCompleteTxtColor(completeColor);
        return self();
    }

    @Override
    public T enableCheckNumMode() {
        getConfig().setDisplayCandidateNo(true);
        return self();
    }

    @Override
    public T setCheckedBoxDrawable(int drawableResId) {
        getConfig().setCheckedBoxDrawable(drawableResId);
        return self();
    }
}
