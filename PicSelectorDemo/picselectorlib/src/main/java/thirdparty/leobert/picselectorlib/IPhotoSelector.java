package thirdparty.leobert.picselectorlib;

/**
 * <p><b>Package</b> thirdparty.leobert.picselectorlib
 * <p><b>Project</b> PicSelectorDemo
 * <p><b>Classname</b> IPhotoSelector
 * <p><b>Description</b>: TODO
 * <p>Created by leobert on 2017/2/7.
 */

public interface IPhotoSelector {

    IPhotoSelector singleSelect();

    IPhotoSelector multiSelect(int maxCount);

    IPhotoSelector enableCrop();

    IPhotoSelector enableCrop(int mode);

    IPhotoSelector enableCrop(int w,int h);

    IPhotoSelector


//    /**
//    * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
//    * isPreview    --> 是否打开预览选项
//    * isCrop       --> 是否打开剪切选项
//    * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
//    * ThemeStyle -->主题颜色
//    * CheckedBoxDrawable -->图片勾选样式
//    * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
//    * cropH-->裁剪高度 值不能小于100
//    * isCompress -->是否压缩图片
//    * setEnablePixelCompress 是否启用像素压缩
//    * setEnableQualityCompress 是否启用质量压缩
//    * setRecordVideoSecond 录视频的秒数，默认不限制
//    * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
//    * setImageSpanCount -->每行显示个数
//    * setCheckNumMode 是否显示QQ选择风格(带数字效果)
//    * setPreviewColor 预览文字颜色
//    * setCompleteColor 完成文字颜色
//    * setPreviewBottomBgColor 预览界面底部背景色
//    * setBottomBgColor 选择图片页面底部背景色
//    * setCompressQuality 设置裁剪质量，默认无损裁剪
//    * setSelectMedia 已选择的图片
//    * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
//    * 注意-->type为2时 设置isPreview or isCrop 无效
//    * 注意：Options可以为空，默认标准模式
//    */
}
