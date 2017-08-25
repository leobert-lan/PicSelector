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

import com.yalantis.ucrop.entity.LocalMedia;

import java.util.List;

import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.LaunchConfig;

/**
 * <p><b>Package</b> thirdparty.leobert.pvselectorlib
 * <p><b>Project</b> PicSelectorDemo
 * <p><b>Classname</b> IPhotoSelector
 * <p><b>Description</b>: API for Picture Selector
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

    IPhotoSelector enableCrop(@FunctionConfig.CropMode int mode);

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
    IPhotoSelector useSystemCompress(boolean enablePixelCompress, boolean enableQualityCompress);


    /**
     * max size is a const see at {@link  thirdparty.leobert.pvselectorlib.model.FunctionConfig#MAX_COMPRESS_SIZE}
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return
     */
    IPhotoSelector enableLuBanCompress(int maxWidth, int maxHeight);

    /**
     * 设置已选择的内容
     * @param selectMedia 已选择内容
     * @return
     */
    IPhotoSelector setSelectedMedia(List<LocalMedia> selectMedia);

    void launch(LaunchConfig.OnSelectResultCallback resultCallback);

}
