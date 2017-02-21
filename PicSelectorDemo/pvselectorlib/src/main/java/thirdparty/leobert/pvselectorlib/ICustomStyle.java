package thirdparty.leobert.pvselectorlib;

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
    T setThemeColor(int mainColor);

    /**
     * 设置选择图片页面底部背景色
     *
     * @param bottomBgColor
     */
    T setBottomBgColor(int bottomBgColor);


    /**
     * "预览"文字颜色
     *
     * @param previewColor
     */
    T setPreviewColor(int previewColor);

    /**
     * “已完成”文字颜色
     *
     * @param completeColor
     */
    T setCompleteColor(int completeColor);

    /**
     * 设置完成选取的文字
     * @param completeText
     */
    T setCompleteText(CharSequence completeText);

    /**
     * 启用计数checkbox
     */
    T enableCheckNumMode();

    T setCheckedBoxDrawable(int drawableResId);

}
