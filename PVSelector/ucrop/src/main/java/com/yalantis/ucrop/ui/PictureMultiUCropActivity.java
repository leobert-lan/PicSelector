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

package com.yalantis.ucrop.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yalantis.ucrop.MultiUCrop;
import com.yalantis.ucrop.Options;
import com.yalantis.ucrop.R;
import com.yalantis.ucrop.UcropConsts;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class PictureMultiUCropActivity extends UCropActivity {

    private int cropIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_multi_cutting);
        mContext = this;
        images = (List<LocalMedia>) getIntent()
                .getSerializableExtra(UcropConsts.Extra.EXTRA_MEDIA_LIST);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        cropIndex = MultiUCrop.getCropPosition(intent);
        cropMode = MultiUCrop.getCropMode(intent);

        enableCompress =
                intent.getBooleanExtra(UcropConsts.Extra.EXTRA_COMPRESS_ENABLED, false);
        for (LocalMedia media : images) {
            media.setCropped(false);
        }
        images.get(cropIndex).setCropped(true);// 默认装载第一张图片
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PicturePhotoGalleryAdapter(mContext, images);
        recyclerView.setAdapter(adapter);
        // 预览图 一页5个,裁剪到第6个的时候滚动到最新位置，不然预览图片看不到
        if (cropIndex >= 5) {
            recyclerView.scrollToPosition(cropIndex);
        }
        setupViews(intent);
        setImageData(intent);
    }

    /**
     * 多图裁剪
     */
    protected void startMultiCopy(String path) {
        // 去裁剪
        MultiUCrop uCrop = MultiUCrop.of(Uri.parse(path), Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg")));
        Options options = new Options.OptionsImpl();
        switch (cropMode) {
            case UcropConsts.CROP_MODE_1_1:
                options.withAspectRatio(1, 1);
                break;
            case UcropConsts.CROP_MODE_3_2:
                options.withAspectRatio(3, 2);
                break;
            case UcropConsts.CROP_MODE_3_4:
                options.withAspectRatio(3, 4);
                break;
            case UcropConsts.CROP_MODE_16_9:
                options.withAspectRatio(16, 9);
                break;
            case UcropConsts.CROP_MODE_DEFAULT:
            default:
                options.withAspectRatio(0, 0);
                break;

        }
        options.setLocalMedia(images);
        options.setPosition(cropIndex);
        options.setCompressionQuality(mCompressQuality);
        options.withMaxResultSize(maxSizeX, maxSizeY);
        options.backgroundColor(backgroundColor);
        uCrop.withOptions(options);
        uCrop.start(PictureMultiUCropActivity.this);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }


    protected void setResultUri(Uri uri,
                                float resultAspectRatio,
                                int imageWidth,
                                int imageHeight) {

        images.get(cropIndex).setCroppedPath(uri.getPath());
        images.get(cropIndex).setCropped(true);
        cropIndex++;
        if (cropIndex >= images.size()) {
            // send the multi-crop-complete signal
            Log.d(getClass().getSimpleName(),"sent ACTION_MULTI_IMAGE_CROPPED_COMPLETE");
            sendBroadcast(new Intent()
                    .setAction(UcropConsts.BcActions
                            .ACTION_MULTI_IMAGE_CROPPED_COMPLETE));

            sendBroadcast(new Intent()
                    .setAction(UcropConsts.BcActions.ACTION_IMAGE_CROPPED)
                    .putExtra(UcropConsts.Extra.EXTRA_SERIALIZABLE_RESULT,
                            (Serializable) images));
            // 裁剪完成，看是否压缩
            for (LocalMedia media : images) {
                media.setCropped(true);
            }
            finish();
            overridePendingTransition(0, R.anim.hold);
        } else {
            finish();
            startMultiCopy(images.get(cropIndex).getPath());
        }

        cancelDialog();
    }


}
