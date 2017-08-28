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

package thirdparty.leobert.pvselectorlib.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.util.Utils;

import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.ui.AlbumDirectoryListActivity;
import thirdparty.leobert.pvselectorlib.ui.MediaFolderContentDisplayActivity;
import thirdparty.leobert.pvselectorlib.ui.PictureExternalPreviewActivity;

import static com.yalantis.ucrop.entity.LocalMedia.asArrayList;


public class LaunchConfig {

    public static FunctionConfig config;
    public static LaunchConfig launchConfig;


    public static LaunchConfig getLaunchConfig() {
        if (launchConfig == null) {
            launchConfig = new LaunchConfig();
        }

        return launchConfig;
    }

    public LaunchConfig() {
        super();
    }


    public OnSelectResultCallback resultCallback;

    public OnSelectResultCallback getResultCallback() {
        return resultCallback;
    }

    public static void init(FunctionConfig functionConfig) {
        config = functionConfig;
    }

    /**
     * 启动相册
     */
    public void openPhoto(Context mContext, OnSelectResultCallback resultCall) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        if (config == null) {
            config = new FunctionConfig();
        }
        // 这里仿ios微信相册启动模式
        Intent intent1 = new Intent(mContext, AlbumDirectoryListActivity.class);
        Intent intent2 = new Intent(mContext, MediaFolderContentDisplayActivity.class);
        Intent[] intents = new Intent[2];
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intents[0] = intent1;
        intents[1] = intent2;
        intent1.putExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG, config);
        intent2.putExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG, config);
        mContext.startActivities(intents);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_bottom_in, 0);
        // 绑定图片接口回调函数事件
        resultCallback = resultCall;
    }

    /**
     * 外部图片预览,当宿主存在对已选择的图片再次进行预览的需求时，可以用此处快捷实现，且风格近似
     *
     * @param position
     * @param medias
     */
    public void externalPicturePreview(Context mContext,
                                       int position,
                                       List<LocalMedia> medias) {
        if (medias != null && medias.size() > 0) {
            Intent intent = new Intent();
            //change to ParcelableArrayListExtra
//            intent.putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
//                    (Serializable) medias);
            intent.putParcelableArrayListExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                   asArrayList(medias));
            intent.putExtra(Consts.Extra.EXTRA_POSITION, position);
            intent.setClass(mContext, PictureExternalPreviewActivity.class);
            mContext.startActivity(intent);
            if (mContext instanceof Activity)
                ((Activity) mContext).overridePendingTransition(R.anim.toast_enter, 0);
        }
    }


    /**
     * 处理结果
     */
    public interface OnSelectResultCallback {
        /**
         * 处理成功
         *
         * @param resultList
         */
        void onSelectSuccess(List<LocalMedia> resultList);

    }
}
