package thirdparty.leobert.picselectorlib;

import android.util.Log;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p><b>Package</b>
 * <p><b>Project</b>
 * <p><b>Classname</b>
 * <p><b>Description</b>: TODO
 * <p>
 * Created by leobert on 2017/2/8.
 */
public class Logger {
    private static final String TAG = "picselectorlib";

    public static void d(String subTag, String msg) {
        Log.d(TAG, subTag + "\n" + msg);
    }

    public static void i(String subTag, String msg) {
        Log.i(TAG, subTag + "\n" + msg);
    }

    public static void e(String subTag, String msg) {
        Log.e(TAG, subTag + "\n" + msg);
    }

    public static <E> String stringnify(Collection<E> collection) {
        if (collection == null) {
            return "collection is null";
        } else if (collection.size() == 0) {
            return "collection is empty";
        } else {
            StringBuilder stringBuilder = new StringBuilder("[");
            for (E aCollection : collection) {
                stringBuilder.append(aCollection).append(",");
            }
            int end = stringBuilder.lastIndexOf(",");
            stringBuilder.replace(end, end + 1, "]");
            return stringBuilder.toString();
        }
    }
}
