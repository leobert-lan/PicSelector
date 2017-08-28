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

import android.support.annotation.StringDef;

import com.yalantis.ucrop.UcropConsts;
import com.yalantis.ucrop.annotation.UCropResultExtraKey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import thirdparty.leobert.pvselectorlib.ui.MediaFolderContentDisplayActivity;
import thirdparty.leobert.pvselectorlib.ui.PicturePreviewActivity;

/**
 * <p><b>Package:</b> thirdparty.leobert.pvselectorlib </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> Consts </p>
 * <p><b>Description:</b> define and classify constants </p>
 * Created by leobert on 2017/8/18.
 */

public interface Consts {

    /**
     * use to tag the intent when request to grant permission
     */
    interface PermissionReqCode {
        int READ_EXTERNAL_STORAGE = 0x01;
        int GRANT_CAMERA = 0x02;
    }

    interface AcResultReqCode {
        /**
         * used to start {@link MediaFolderContentDisplayActivity}, to select
         * media in the given folder.
         */
        int REQUEST_SELECT_MEDIA = 88;

        /**
         * used to start Camera to capture picture or record video.
         */
        int REQUEST_START_CAMERA = 99;

        /**
         * used to start {@link PicturePreviewActivity},listen the result
         * to get selected medias.
         */
        int REQUEST_MEDIA_PREVIEW = 100;
    }

    /**
     * used to define place-hold keys in savedInstanceState's bundle
     */
    interface BundleKey {
        /**
         * keep the path that used to save the picture you will capture
         * by camera
         */
        String CAMERA_SAVED_PATH = "CameraSavedPath";
    }

    interface Extra {
        String EXTRA_FUNCTION_CONFIG = "FUNCTION_CONFIG";

        String EXTRA_IS_FIRST_STARTED = "IS_FIRST_STARTED";

        String EXTRA_FROM_BOTTOMBAR_PREVIEW = "FROM_BOTTOMBAR_PREVIEW";

        /**
         * used to tag the position of the current media
         * in the selected media list, and this tagged media
         * will be previewed at first.
         */
        String EXTRA_POSITION = "POSITION";

        String EXTRA_PREVIEW_LIST = "DATA_LIST_PREVIEW";

        @ExtraKey
        String EXTRA_PREVIEW_SELECT_LIST = "DATA_LIST_PREVIEW_SELECTED";

        @ResultExtraKey
        @ExtraKey
        String EXTRA_ARRAYLIST_LOCALMEDIA = UcropConsts.Extra.EXTRA_ARRAYLIST_LOCALMEDIA;

        /**
         * used to put/get the name of the folder that contains media,
         * when you choose one to have a quick look, all medias'(picture or video)
         * snapshot will be displayed in the grid. And the folder name will be
         * displayed as the title in the toolbar
         */
        String EXTRA_FOLDER_NAME = "FOLDER_NAME";

        String PV_TYPE = "PV_TYPE";

        String EXTRA_PREVIEW_VIDEO_PATH = "DATA_VIDEO_PREVIEW_PATH";
    }

    /**
     * refers to broadcast actions
     */
    interface BcActions {

        /**
         * finish the backend activities which bind with this action
         */
        String ACTION_FINISH_ACTIVITY = "app.activity.finish";

        /**
         * refresh the media content data in the folder. broadcast-action
         * will be sent when data has been changed, e.g. image captured
         */
        String ACTION_REFRESH_DATA = "app.action.refresh.data";

        /**
         * resolve the cropped image(s) in the bundle of the intent.broadcast-action
         * will be sent when image(s) cropped
         */
        String ACTION_IMAGE_CROPPED = UcropConsts.BcActions.ACTION_IMAGE_CROPPED;
        //"app.action.image_cropped";

        /**
         * as the signal of multi-crop complete.
         */
        String ACTION_MULTI_IMAGE_CROPPED_COMPLETE =
                UcropConsts.BcActions.ACTION_MULTI_IMAGE_CROPPED_COMPLETE;
    }

    /**
     * used to tag the key in result-intent-data
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Extra.EXTRA_ARRAYLIST_LOCALMEDIA})
    @UCropResultExtraKey
    @interface ResultExtraKey {
    }

    /**
     * used to tag the key in intent-data,prepared for intent-parser
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Extra.EXTRA_ARRAYLIST_LOCALMEDIA})
    @interface ExtraKey {
    }
}
