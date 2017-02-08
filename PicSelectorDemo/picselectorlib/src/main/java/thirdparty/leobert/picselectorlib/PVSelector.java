package thirdparty.leobert.picselectorlib;

import android.content.Context;

/**
 * <p><b>Package</b> thirdparty.leobert.picselectorlib
 * <p><b>Project</b> PicSelectorDemo
 * <p><b>Classname</b> PVSelector
 * <p><b>Description</b>: TODO
 * <p>Created by leobert on 2017/2/7.
 */

public class PVSelector {
    public static IPhotoSelector getPhotoSelector(Context context) {
        return new PhotoSelector(context);
    }

    public static IVideoSelector getVideoSelector(Context context) {
        return new VideoSelector(context);
    }

}
