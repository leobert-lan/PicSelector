package thirdparty.leobert.picselectorlib;

import com.yalantis.ucrop.entity.LocalMedia;

import java.util.List;

import thirdparty.leobert.picselectorlib.model.PictureConfig;

/**
 * <p><b>Package</b> thirdparty.leobert.picselectorlib
 * <p><b>Project</b> PicSelectorDemo
 * <p><b>Classname</b> IPhotoSelector
 * <p><b>Description</b>: TODO
 * <p>Created by leobert on 2017/2/7.
 */

public interface IPhotoSelector extends ICustomStyle<IPhotoSelector>{

    /**
     * 允许在第一个位置展示拍照选项
     * @return
     */
    IPhotoSelector enableCamera();

    /**
     * 允许选择时、选择完后进行预览
     * @return
     */
    IPhotoSelector enablePreview();

    /**
     * 单选模式
     * @return
     */
    IPhotoSelector singleSelect();

    /**
     * 复选模式
     * @param maxCount 最大选取数量
     * @return
     */
    IPhotoSelector multiSelect(int maxCount);

    IPhotoSelector enableCrop();

    IPhotoSelector enableCrop(int mode);

    /**
     * 裁剪，如果值大于图片原始宽高 将返回原图大小
     *
     * @param w 裁剪宽度 值不能小于100
     * @param h 裁剪高度 值不能小于100
     * @return this
     */
    IPhotoSelector enableCrop(int w, int h);

    /**
     * 截取时使用系统API的压缩
     * @param enablePixelCompress 是否启用像素压缩
     * @param enableQualityCompress 是否启用质量压缩
     * @return this
     */
    IPhotoSelector useSystemCompressOnCrop(boolean enablePixelCompress,boolean enableQualityCompress);


    /**
     * max size is a const see at {@link  thirdparty.leobert.picselectorlib.model.FunctionConfig#MAX_COMPRESS_SIZE}
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return
     */
    IPhotoSelector enableLubanCompressOnCrop(int maxWidth,int maxHeight);

    /**
     * 设置已选择的内容
     * @param selectMedia 已选择内容
     * @return
     */
    IPhotoSelector setSelectedMedia(List<LocalMedia> selectMedia);

    void launch(PictureConfig.OnSelectResultCallback resultCallback);

}
