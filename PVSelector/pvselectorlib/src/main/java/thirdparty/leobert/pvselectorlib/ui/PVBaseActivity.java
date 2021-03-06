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

package thirdparty.leobert.pvselectorlib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.broadcast.ActionConsumer;
import thirdparty.leobert.pvselectorlib.broadcast.ActionListenedBroadCastReceiver;
import thirdparty.leobert.pvselectorlib.compress.CompressConfig;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;

public abstract class PVBaseActivity extends FragmentActivity {
    protected Context mContext;

    @LocalMedia.MediaType
    protected int type = LocalMedia.TYPE_PICTURE;//default picture

    protected int maxSelectNum = 0;
    protected int spanCount = 4;

    @FunctionConfig.CropMode
//    @UcropConsts.CropMode
    protected int cropMode = FunctionConfig.CROP_MODE_DEFAULT;

    protected boolean showCamera = false;
    protected boolean enablePreview = false;
    protected boolean enableCrop = false;
    protected boolean enablePreviewVideo = true;
    protected int selectMode = FunctionConfig.SELECT_MODE_MULTIPLE;

    @ColorInt
    protected int backgroundColor = 0;

    @DrawableRes
    protected int cb_drawable = 0;
    protected int cropW = 0;
    protected int cropH = 0;
    protected int recordVideoSecond = 0;
    protected int definition = 3;
    protected boolean enableCompress;
    protected boolean displayCandidateNo;

    @ColorInt
    protected int previewTxtColor; // 底部预览字体颜色

    @ColorInt
    protected int completeTxtColor; // 底部完成字体颜色

    protected CharSequence completeText; // 底部完成文字

    @ColorInt
    protected int bottomBgColor; // 底部背景色

    @ColorInt
    protected int previewBottomBgColor; // 预览底部背景色

    protected int compressQuality = 0;// 压缩图片质量
    protected List<LocalMedia> selectMedias = new ArrayList<>();
    protected FunctionConfig config = new FunctionConfig();

    @CompressConfig.CompressScheme
    protected int compressScheme = CompressConfig.SCHEME_SYSTEM;
    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;
    protected int compressW;
    protected int compressH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
    }

    private void initBase() {
        mContext = this;
        initScreenWidth();
        config = (FunctionConfig) getIntent()
                .getSerializableExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG);
        type = config.getType();
        showCamera = config.isShowCamera();
        enablePreview = config.isEnablePreview();
        selectMode = config.getSelectMode();
        enableCrop = config.isEnableCrop();
        maxSelectNum = config.getMaxSelectNum();
        cropMode = config.getCropMode();
        enablePreviewVideo = config.isPreviewVideo();
        backgroundColor = config.getThemeColor();
        cb_drawable = config.getCheckedBoxDrawable();
        enableCompress = config.getPictureCompressEnable();
        spanCount = config.getImageSpanCount();
        cropW = config.getCropWidth();
        cropH = config.getCropHeight();
        recordVideoSecond = config.getRecordVideoSecond();
        definition = config.getRecordVideoDefinition();
        displayCandidateNo = config.isDisplayCandidateNo();
        previewTxtColor = config.getPreviewTxtColor();
        completeTxtColor = config.getCompleteTxtColor();
        bottomBgColor = config.getBottomBgColor();
        completeText = config.getCompleteText();
        if (completeText == null || completeText.equals("")) {
            completeText = "完成";
        }
        previewBottomBgColor = config.getPreviewBottomBgColor();
        compressQuality = config.getCompressQuality();
        selectMedias = config.getSelectMedia();
        compressScheme = config.getCompressScheme();
        compressW = config.getCompressW();
        compressH = config.getCompressH();
        // 如果是显示数据风格，则默认为qq选择风格
        if (displayCandidateNo) {
            cb_drawable = R.drawable.checkbox_num_selector;
        }
    }

    protected void initUiInstance() {
    }

    protected void initUiEventListener() {
    }

    /**
     * 针对6.0动态请求权限问题
     * 判断是否允许此权限
     *
     * @param permissions
     * @return
     */
    protected boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 动态请求权限
     *
     * @param code
     * @param permissions
     */
    protected void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Consts.PermissionReqCode.READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readLocalMedia();
                } else {
                    showToast("读取内存卡权限已被拒绝");
                }
                break;
            case Consts.PermissionReqCode.GRANT_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    showToast("拍照权限已被拒绝");
                }
                break;
        }
    }

    /**
     * 启动相机
     */
    protected void startCamera() {

    }

    /**
     * 读取相册信息
     */
    protected void readLocalMedia() {

    }

    protected void startActivity(Class act) {
        Intent intent = new Intent();
        intent.setClass(this, act);
        startActivity(intent);
    }

    protected void startActivity(Class act, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, act);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 广播发送者
     *
     * @param action
     */
    protected void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }


    /**
     * 注册发送者
     *
     * @param action
     */
    protected void registerReceiver(BroadcastReceiver receiver, String... action) {
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < action.length; i++) {
            intentFilter.addAction(action[i]);
        }
        registerReceiver(receiver, intentFilter);
    }

    private ActionListenedBroadCastReceiver broadCastReceiver;

    protected void registerBroadcastConsumer(ActionConsumer... consumers) {
        if (broadCastReceiver != null) {
            final String TAG = getClass().getName();
            Log.d(TAG,TAG+" may have registered bcr before,try to unregister now");
            broadCastReceiver.unregister(this);
        }

        broadCastReceiver
                = new ActionListenedBroadCastReceiver(consumers);
        broadCastReceiver.register(this);
    }


    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 判断某个activity是否存在
     *
     * @return
     */
    protected boolean isActivityExistence(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        if (getPackageManager().resolveActivity(intent, 0) == null) {
            // 说明系统中不存在这个activity
            return false;
        } else {
            return true;
        }
    }

    /**
     * 初始化屏幕宽高
     */
    protected void initScreenWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
        mScreenWidth = dm.widthPixels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadCastReceiver != null) {
            broadCastReceiver.unregister(this);
            broadCastReceiver = null;
        }
    }
}
