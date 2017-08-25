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

import thirdparty.leobert.pvselectorlib.model.PictureConfig;

/**
 * <p><b>Package</b> thirdparty.leobert.pvselectorlib
 * <p><b>Project</b> PicSelectorDemo
 * <p><b>Classname</b> IVideoSelector
 * <p><b>Description</b>: TODO
 * <p>Created by leobert on 2017/2/7.
 */

public interface IVideoSelector extends ICustomStyle<IVideoSelector>{


    /**
     * 允许在第一个位置展示录像选项
     *
     * @return
     */
    IVideoSelector enableCamera();

    /**
     * 录视频的秒数，默认不限制
     *
     * @return
     */
    IVideoSelector setRecordVideoSecond(int maxRecordSecond);

    /**
     * 视频清晰度
     *
     * @param videoDefinition FunctionConfig.RECORD_QUALITY_HIGH 清晰  FunctionConfig.RECORD_QUALITY_ORDINARY 低质量
     * @return
     */
    IVideoSelector setRecordVideoDefinition(int videoDefinition);

    /**
     * 允许选择完后进行预览
     *
     * @return
     */
    IVideoSelector enablePreview();

    /**
     * 单选模式
     *
     * @return
     */
    IVideoSelector singleSelect();

    /**
     * 复选模式
     *
     * @param maxCount 最大选取数量
     * @return
     */
    IVideoSelector multiSelect(int maxCount);


    /**
     * 设置已选择的内容
     *
     * @param selectMedia 已选择内容
     * @return
     */
    IVideoSelector setSelectedMedia(List<LocalMedia> selectMedia);

    void launch(PictureConfig.OnSelectResultCallback resultCallback);
}
