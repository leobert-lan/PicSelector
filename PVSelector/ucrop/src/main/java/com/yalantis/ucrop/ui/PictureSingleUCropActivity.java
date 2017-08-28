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
import android.support.annotation.NonNull;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UcropConsts;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import static com.yalantis.ucrop.entity.LocalMedia.asArrayList;


public class PictureSingleUCropActivity extends UCropActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_ucrop_activity_photobox);
        final Intent intent = getIntent();
        setupViews(intent);
        setImageData(intent);
    }

    private Uri inputUri;

    @Override
    protected void setImageData(@NonNull Intent intent) {
        super.setImageData(intent);
        inputUri = UCrop.getInput(intent);
    }

    @Override
    protected void setResultUri(Uri uri,
                                float resultAspectRatio,
                                int imageWidth,
                                int imageHeight) {
        cancelDialog();
        List<LocalMedia> result = new ArrayList<>();
        LocalMedia media = new LocalMedia();
        media.setCropped(true);
        media.setPath(inputUri.getPath());
        media.setCroppedPath(uri.getPath());
        media.setType(LocalMedia.TYPE_PICTURE); // only picture
        result.add(media);
        sendBroadcast(new Intent()
                .setAction(UcropConsts.BcActions.ACTION_IMAGE_CROPPED)
                .putParcelableArrayListExtra(UcropConsts.Extra.EXTRA_ARRAYLIST_LOCALMEDIA,
                        asArrayList(result)));
        finish();
        overridePendingTransition(0, R.anim.hold);
    }

}
