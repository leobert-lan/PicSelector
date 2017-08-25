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
