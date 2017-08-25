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

import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.LaunchConfig;

/**
 * 视频选择器
 * Created by leobert on 2017/2/8.
 */

class VideoSelector extends AbsSelector<IVideoSelector>
        implements IVideoSelector{

    private final WeakReference<Context> contextRef;

    VideoSelector(Context context) {
        super();
        contextRef = new WeakReference<>(context);
        config.setType(LocalMedia.TYPE_VIDEO);
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
        config.setRecordVideoDefinition(FunctionConfig.RECORD_QUALITY_ORDINARY);
        return this;
    }

    @Override
    public IVideoSelector setRecordVideoSecond(int maxRecordSecond) {
        if (!config.isShowCamera()) {
            Logger.e(VideoSelector.class.getSimpleName(),
                    "please call enableCamera at first");
            enableCamera();
        }
        config.setRecordVideoSecond(maxRecordSecond);
        return this;
    }

    @Override
    public IVideoSelector setRecordVideoDefinition(int videoDefinition) {
        if (!config.isShowCamera()) {
            Logger.e(VideoSelector.class.getSimpleName(),
                    "please call enableCamera at first");
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
        config.setSelectMode(FunctionConfig.SELECT_MODE_SINGLE);
        return this;
    }

    @Override
    public IVideoSelector multiSelect(int maxCount) {
        config.setSelectMode(FunctionConfig.SELECT_MODE_MULTIPLE);
        config.setMaxSelectNum(maxCount);
        return this;
    }

    @Override
    public IVideoSelector setSelectedMedia(List<LocalMedia> selectMedia) {
        config.setSelectMedia(selectMedia);
        return this;
    }

    @Override
    public void launch(LaunchConfig.OnSelectResultCallback resultCallback) {
        Context context = contextRef.get();
        if (context == null) {
            Logger.e(VideoSelector.class.getSimpleName(),
                    "context is null,maybe activity finished");
            return;
        }
        LaunchConfig.init(config);
        LaunchConfig.getLaunchConfig().openPhoto(context, resultCallback);
    }

    @Override
    public IVideoSelector setCompleteText(CharSequence completeText) {
        config.setCompleteText(completeText);
        return this;
    }
}
