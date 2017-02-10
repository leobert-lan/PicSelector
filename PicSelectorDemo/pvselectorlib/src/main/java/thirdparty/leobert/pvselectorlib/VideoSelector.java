package thirdparty.leobert.pvselectorlib;

import android.content.Context;

import com.yalantis.ucrop.entity.LocalMedia;

import java.lang.ref.WeakReference;
import java.util.List;

import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.LocalMediaLoader;
import thirdparty.leobert.pvselectorlib.model.PictureConfig;

/**
 * 视频选择器
 * Created by leobert on 2017/2/8.
 */

class VideoSelector extends AbsSelector<IVideoSelector> implements IVideoSelector{

    private final WeakReference<Context> contextRef;

    VideoSelector(Context context) {
        super();
        contextRef = new WeakReference<>(context);
        config.setType(LocalMediaLoader.TYPE_VIDEO);
        config.setPreviewVideo(false);
        config.setShowCamera(false);
    }

    @Override
    public FunctionConfig getConfig() {
        return config;
    }

    @Override
    protected IVideoSelector self() {
        return this;
    }

    @Override
    public IVideoSelector enableCamera() {
        config.setShowCamera(true);
        config.setRecordVideoSecond(0);//默认不限制时长
        config.setRecordVideoDefinition(FunctionConfig.ORDINARY);
        return this;
    }

    @Override
    public IVideoSelector setRecordVideoSecond(int maxRecordSecond) {
        if (!config.isShowCamera()) {
            Logger.e(VideoSelector.class.getSimpleName(),"please call enableCamera at first");
            enableCamera();
        }
        config.setRecordVideoSecond(maxRecordSecond);
        return this;
    }

    @Override
    public IVideoSelector setRecordVideoDefinition(int videoDefinition) {
        if (!config.isShowCamera()) {
            Logger.e(VideoSelector.class.getSimpleName(),"please call enableCamera at first");
            enableCamera();
        }
        config.setRecordVideoDefinition(videoDefinition);
        return this;
    }

    @Override
    public IVideoSelector enablePreview() {
        config.setPreviewVideo(true);
        return this;
    }

    @Override
    public IVideoSelector singleSelect() {
        config.setSelectMode(FunctionConfig.MODE_SINGLE);
        return this;
    }

    @Override
    public IVideoSelector multiSelect(int maxCount) {
        config.setSelectMode(FunctionConfig.MODE_MULTIPLE);
        config.setMaxSelectNum(maxCount);
        return this;
    }

    @Override
    public IVideoSelector setSelectedMedia(List<LocalMedia> selectMedia) {
        config.setSelectMedia(selectMedia);
        return this;
    }

    @Override
    public void launch(PictureConfig.OnSelectResultCallback resultCallback) {
        Context context = contextRef.get();
        if (context == null) {
            Logger.e(VideoSelector.class.getSimpleName(), "context is null,maybe activity finished");
            return;
        }
        PictureConfig.init(config);
        PictureConfig.getPictureConfig().openPhoto(context, resultCallback);
    }
}
