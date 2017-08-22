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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.Options;
import com.yalantis.ucrop.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UcropConsts;
import com.yalantis.ucrop.annotation.UCropMode;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.dialog.SweetAlertDialog;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.ToolbarUtil;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Package:</b> com.yalantis.ucrop.ui </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> UCropActivity </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/22.
 */

abstract class UCropActivity extends FragmentActivity {

    ///////////////////////////////////////////////////////////////////////////
    // GestureTypes
    ///////////////////////////////////////////////////////////////////////////
    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;

    @IntDef({NONE, SCALE, ROTATE, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {
    }


    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT
            = Bitmap.CompressFormat.JPEG;


    protected int mLogoColor;

    protected UCropView mUCropView;
    protected GestureCropImageView mGestureCropImageView;
    protected OverlayView mOverlayView;
    protected RelativeLayout rlToolBar;
    protected Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    protected int mCompressQuality = DEFAULT_COMPRESS_QUALITY;
    @UCropMode
    protected int cropMode = UcropConsts.CROP_MODE_DEFAULT;// 裁剪模式

    protected RecyclerView recyclerView;
    protected PicturePhotoGalleryAdapter adapter;
    protected List<LocalMedia> images = new ArrayList<>();

    protected ImageButton navBack;

    /**
     * located at the right in the toolbar,click to submit crop
     */
    protected TextView tvOpComplete;


    protected SweetAlertDialog dialog;
    protected Context mContext;
    protected int maxSizeX, maxSizeY;
    protected int backgroundColor = 0;
    protected boolean enableCompress;

    protected void initiateRootViews() {
        mUCropView = (UCropView) findViewById(R.id.ucrop);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();

        mGestureCropImageView.setTransformImageListener(mImageListener);

        ((ImageView) findViewById(R.id.image_view_logo))
                .setColorFilter(mLogoColor, PorterDuff.Mode.SRC_ATOP);
    }

    protected TransformImageView.TransformImageListener mImageListener
            = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
        }

        @Override
        public void onScale(float currentScale) {
        }

        @Override
        public void onLoadComplete() {
            mUCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            setResultError(e);
            finish();
        }
    };

    protected void setResultError(Throwable throwable) {
        setResult(UCrop.RESULT_ERROR,
                new Intent().putExtra(UcropConsts.Extra.EXTRA_ERROR, throwable));
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
        cancelDialog();
    }

    /**
     * This method extracts all data from the incoming intent and setups views properly.
     */
    protected void setImageData(@NonNull Intent intent) {
        Uri inputUri = UCrop.getInput(intent);
        Uri outputUri = UCrop.getOutput(intent);
        processOptions(intent);

        if (inputUri != null && outputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri, outputUri);
            } catch (Exception e) {
                setResultError(e);
                finish();
            }
        } else {
            setResultError(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
            finish();
        }
    }

    /**
     * This method extracts {@link Options #optionsBundle} from incoming intent
     * and setups Activity, {@link OverlayView} and {@link CropImageView} properly.
     */
    private void processOptions(@NonNull Intent intent) {
        // Bitmap compression options
        String compressionFormatName = intent.getStringExtra(Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        mCompressFormat = (compressFormat == null) ?
                DEFAULT_COMPRESS_FORMAT : compressFormat;

        mCompressQuality = intent.getIntExtra(Options.EXTRA_COMPRESSION_QUALITY, DEFAULT_COMPRESS_QUALITY);


        // Crop image view options
        mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(Options.EXTRA_MAX_BITMAP_SIZE, CropImageView.DEFAULT_MAX_BITMAP_SIZE));
        mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(Options.EXTRA_MAX_SCALE_MULTIPLIER, CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER));
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));

        // Overlay view options
        boolean enableFreestyleCrop =
                intent.getBooleanExtra(Options.EXTRA_FREE_STYLE_CROP,
                        OverlayView.DEFAULT_FREESTYLE_CROP_MODE != OverlayView.FREESTYLE_CROP_MODE_DISABLE);
        mOverlayView.setFreestyleCropMode(enableFreestyleCrop ?
                OverlayView.FREESTYLE_CROP_MODE_ENABLE :
                OverlayView.FREESTYLE_CROP_MODE_DISABLE);

        mOverlayView.setDimmedColor(intent.getIntExtra(Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(R.color.ucrop_color_default_dimmed)));
        mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(Options.EXTRA_CIRCLE_DIMMED_LAYER, OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER));

        mOverlayView.setShowCropFrame(intent.getBooleanExtra(Options.EXTRA_SHOW_CROP_FRAME, OverlayView.DEFAULT_SHOW_CROP_FRAME));
        mOverlayView.setCropFrameColor(intent.getIntExtra(Options.EXTRA_CROP_FRAME_COLOR,
                ContextCompat.getColor(this, R.color.ucrop_color_default_crop_frame)));
        mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));

        mOverlayView.setShowCropGrid(intent.getBooleanExtra(Options.EXTRA_SHOW_CROP_GRID, OverlayView.DEFAULT_SHOW_CROP_GRID));
        mOverlayView.setCropGridRowCount(intent.getIntExtra(Options.EXTRA_CROP_GRID_ROW_COUNT, OverlayView.DEFAULT_CROP_GRID_ROW_COUNT));
        mOverlayView.setCropGridColumnCount(intent.getIntExtra(Options.EXTRA_CROP_GRID_COLUMN_COUNT, OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT));
        mOverlayView.setCropGridColor(intent.getIntExtra(Options.EXTRA_CROP_GRID_COLOR,
                ContextCompat.getColor(this, R.color.ucrop_color_default_crop_grid)));
        mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(Options.EXTRA_CROP_GRID_STROKE_WIDTH,
                getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));

        // Aspect ratio options
        float aspectRatioX = UCrop.getAspectRatioX(intent);
        float aspectRatioY = UCrop.getAspectRatioY(intent);

        int aspectRationSelectedByDefault = intent.getIntExtra(Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioX > 0 && aspectRatioY > 0) {
            mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
        } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size()) {
            mGestureCropImageView.setTargetAspectRatio(aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioX() /
                    aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioY());
        } else {
            mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
        }

        // Result bitmap max size options
        maxSizeX = intent.getIntExtra(UcropConsts.Extra.EXTRA_MAX_SIZE_X, 0);
        maxSizeY = intent.getIntExtra(UcropConsts.Extra.EXTRA_MAX_SIZE_Y, 0);

        if (maxSizeX > 0 && maxSizeY > 0) {
            mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }


    protected void setupViews(@NonNull Intent intent) {
        tvOpComplete = (TextView) findViewById(R.id.tv_right);
        rlToolBar = (RelativeLayout) findViewById(R.id.rl_title);
        tvOpComplete.setText("确定");
        navBack = (ImageButton) findViewById(R.id.left_back);
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvOpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDialog("处理中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cropAndSaveImage();
            }
        });
        mLogoColor = intent.getIntExtra(Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_logo));
        backgroundColor = intent.getIntExtra(Options.EXTRA_COLOR_BACKGROUND, 0);
        rlToolBar.setBackgroundColor(backgroundColor);
        enableCompress = intent.getBooleanExtra(Options.EXTRA_COMPRESS_ENABLED, false);
//        type = intent.getIntExtra("type", LocalMedia.TYPE_PICTURE);
        ToolbarUtil.setColorNoTranslucent(this, backgroundColor);
        initiateRootViews();
    }


    protected void cropAndSaveImage() {
        supportInvalidateOptionsMenu();

        mGestureCropImageView.cropAndSaveImage(mCompressFormat, mCompressQuality, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int imageWidth, int imageHeight) {
                setResultUri(resultUri, mGestureCropImageView.getTargetAspectRatio(), imageWidth, imageHeight);
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
            }
        });
    }

    protected abstract void setResultUri(Uri uri,
                                         float resultAspectRatio,
                                         int imageWidth,
                                         int imageHeight);

    protected void showDialog(String msg) {
        if (dialog != null)
            dialog.dismiss();
        dialog = new SweetAlertDialog(this);
        dialog.setTitleText(msg);
        dialog.show();
    }

    protected void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGestureCropImageView != null) {
            mGestureCropImageView.cancelAllAnimations();
        }
    }

}
