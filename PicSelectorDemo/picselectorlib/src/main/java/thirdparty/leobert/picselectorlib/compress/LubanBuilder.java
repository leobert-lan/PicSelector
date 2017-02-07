package thirdparty.leobert.picselectorlib.compress;

import android.graphics.Bitmap;

import java.io.File;


public class LubanBuilder {

    int maxSize;

    int maxWidth;

    int maxHeight;

    File cacheDir;

    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    int gear = Luban.THIRD_GEAR;

    LubanBuilder(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
