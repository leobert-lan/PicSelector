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

import thirdparty.leobert.pvselectorlib.ui.PictureImageGridActivity;
import thirdparty.leobert.pvselectorlib.ui.PicturePreviewActivity;

/**
 * <p><b>Package:</b> thirdparty.leobert.pvselectorlib </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> PVSelectorConsts </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/18.
 */

public interface PVSelectorConsts {

    /**
     * use to tag the intent when request to grant permission
     */
    interface PermissionReqCode {
        int READ_EXTERNAL_STORAGE = 0x01;
        int GRANT_CAMERA = 0x02;
    }

    interface AcResultReqCode {
        /**
         * used to start {@link PictureImageGridActivity}, to select
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
}
