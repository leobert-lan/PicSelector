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

import android.content.Context;

import com.yalantis.ucrop.entity.LocalMedia;

import java.lang.ref.WeakReference;
import java.util.List;

import thirdparty.leobert.pvselectorlib.compress.CompressConfig;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.LaunchConfig;

/**
 * 相片选择实现
 * Created by leobert on 2017/2/8.
 */

/*public*/ class PhotoSelector extends AbsSelector<IPhotoSelector>
        implements IPhotoSelector {

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
    public IPhotoSelector useSystemCompress(boolean enablePixelCompress,
                                            boolean enableQualityCompress) {
        config.setPictureCompressEnable(true);
        config.setCompressScheme(CompressConfig.SCHEME_SYSTEM);
        config.setEnablePixelCompress(enablePixelCompress);
        config.setEnableQualityCompress(enableQualityCompress);
        // TODO: 2017/2/8 quality
        config.setCompressQuality(100);
        return this;
    }

    @Override
    public IPhotoSelector enableLuBanCompress(int maxWidth, int maxHeight) {
        config.setPictureCompressEnable(true);
        config.setCompressScheme(CompressConfig.SCHEME_LUBAN);
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
    public void launch(LaunchConfig.OnSelectResultCallback resultCallback) {
        Context context = contextRef.get();
        if (context == null) {
            Logger.e(PhotoSelector.class.getSimpleName(), "context is null,maybe activity finished");
            return;
        }
        LaunchConfig.init(config);
        LaunchConfig.getLaunchConfig().openPhoto(context, resultCallback);
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
