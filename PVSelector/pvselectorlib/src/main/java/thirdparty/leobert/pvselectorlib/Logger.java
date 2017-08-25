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

import android.util.Log;

import java.util.Collection;

/**
 * <p><b>Package</b>
 * <p><b>Project</b>
 * <p><b>Classname</b>
 * <p><b>Description</b>: TODO
 * <p>
 * Created by leobert on 2017/2/8.
 */
public class Logger {
    private static final String TAG = "pvselectorlib";

    public static void d(String subTag, String msg) {
        Log.d(TAG, subTag + "\n" + msg);
    }

    public static void i(String subTag, String msg) {
        Log.i(TAG, subTag + "\n" + msg);
    }

    public static void e(String subTag, String msg) {
        Log.e(TAG, subTag + "\n" + msg);
    }

    public static <E> String stringify(Collection<E> collection) {
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
