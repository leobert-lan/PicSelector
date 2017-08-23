package thirdparty.leobert.pvselectorlib;

import android.content.Context;

import com.yalantis.ucrop.entity.LocalMedia;

import java.lang.ref.WeakReference;
import java.util.List;

import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.PictureConfig;

/**
 * 相片选择实现
 * Created by leobert on 2017/2/8.
 */

/*public*/ class PhotoSelector extends AbsSelector<IPhotoSelector> implements IPhotoSelector {

    private final WeakReference<Context> contextRef;

    public PhotoSelector(Context context) {
        super();
        contextRef = new WeakReference<>(context);
        config.setType(LocalMedia.TYPE_PICTURE);
        config.setEnablePreview(false);
        config.setShowCamera(false);
        config.setPictureCompressEnable(false);
        config.setEnableCrop(false);
    }

    @Override
    public IPhotoSelector enableCamera() {
        config.setShowCamera(true);
        return this;
    }

    @Override
    public IPhotoSelector enablePreview() {
        config.setEnablePreview(true);
        return this;
    }

    @Override
    public IPhotoSelector singleSelect() {
        config.setSelectMode(FunctionConfig.SELECT_MODE_SINGLE);
        return this;
    }

    @Override
    public IPhotoSelector multiSelect(int maxCount) {
        config.setSelectMode(FunctionConfig.SELECT_MODE_MULTIPLE);
        config.setMaxSelectNum(maxCount);
        return this;
    }

    @Override
    public IPhotoSelector enableCrop() {
        enableCrop(FunctionConfig.CROP_MODE_DEFAULT);
        return this;
    }

    @Override
    public IPhotoSelector enableCrop(int mode) {
        config.setEnableCrop(true);
        config.setCropMode(mode);
        return this;
    }

    @Override
    public IPhotoSelector enableCrop(int w, int h) {
        config.setEnableCrop(true);
        config.setCropHeight(h);
        config.setCropWidth(w);
        return this;
    }

    @Override
    public IPhotoSelector useSystemCompressOnCrop(boolean enablePixelCompress, boolean enableQualityCompress) {
        config.setPictureCompressEnable(true);
        config.setCompressScheme(1);
        config.setEnablePixelCompress(enablePixelCompress);
        config.setEnableQualityCompress(enableQualityCompress);
        // TODO: 2017/2/8 quality
        config.setCompressQuality(100);
        return this;
    }

    @Override
    public IPhotoSelector enableLubanCompressOnCrop(int maxWidth, int maxHeight) {
        config.setPictureCompressEnable(true);
        config.setCompressScheme(2);
        config.setCompressH(maxHeight);
        config.setCompressW(maxWidth);
        return this;
    }

    @Override
    public IPhotoSelector setSelectedMedia(List<LocalMedia> selectMedia) {
        config.setSelectMedia(selectMedia);
        return this;
    }

    @Override
    public void launch(PictureConfig.OnSelectResultCallback resultCallback) {
        Context context = contextRef.get();
        if (context == null) {
            Logger.e(PhotoSelector.class.getSimpleName(), "context is null,maybe activity finished");
            return;
        }
        PictureConfig.init(config);
        PictureConfig.getPictureConfig().openPhoto(context, resultCallback);
    }


    @Override
    public FunctionConfig getConfig() {
        return config;
    }

    @Override
    protected IPhotoSelector self() {
        return this;
    }

    @Override
    public IPhotoSelector setCompleteText(CharSequence completeText) {
        config.setCompleteText(completeText);
        return this;
    }
}
