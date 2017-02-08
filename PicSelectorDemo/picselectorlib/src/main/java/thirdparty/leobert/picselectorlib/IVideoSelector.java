package thirdparty.leobert.picselectorlib;

import com.yalantis.ucrop.entity.LocalMedia;

import java.util.List;

import thirdparty.leobert.picselectorlib.model.PictureConfig;

/**
 * <p><b>Package</b> thirdparty.leobert.picselectorlib
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
     * @param videoDefinition FunctionConfig.HIGH 清晰  FunctionConfig.ORDINARY 低质量
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
