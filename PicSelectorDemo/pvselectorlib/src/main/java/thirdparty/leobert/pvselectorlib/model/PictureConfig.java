package thirdparty.leobert.pvselectorlib.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.ui.AlbumDirectoryListActivity;
import thirdparty.leobert.pvselectorlib.ui.MediaFolderContentDisplayActivity;
import thirdparty.leobert.pvselectorlib.ui.PictureExternalPreviewActivity;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.util.Utils;

import java.io.Serializable;
import java.util.List;


public class PictureConfig {

    public static FunctionConfig config;
    public static PictureConfig pictureConfig;


    public static PictureConfig getPictureConfig() {
        if (pictureConfig == null) {
            pictureConfig = new PictureConfig();
        }

        return pictureConfig;
    }

    public PictureConfig() {
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
            intent.putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                    (Serializable) medias);
            intent.putExtra(Consts.Extra.EXTRA_POSITION, position);
            intent.setClass(mContext, PictureExternalPreviewActivity.class);
            mContext.startActivity(intent);
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
