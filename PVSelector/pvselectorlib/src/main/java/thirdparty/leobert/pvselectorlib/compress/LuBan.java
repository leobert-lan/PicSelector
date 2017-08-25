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
package thirdparty.leobert.pvselectorlib.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LuBan {

    public static final int FIRST_GEAR = 1;
    public static final int THIRD_GEAR = 3;
    public static final int CUSTOM_GEAR = 4;

    @IntDef({FIRST_GEAR, THIRD_GEAR, CUSTOM_GEAR})
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    @Documented
    @Inherited
    @interface GEAR {
    }

    private static final String TAG = "Luban";
    private static String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

    private File mFile;

    private List<File> mFileList;

    private LuBanBuilder mBuilder;

    private LuBan(File cacheDir) {
        mBuilder = new LuBanBuilder(cacheDir);
    }

    public static LuBan compress(Context context, File file) {
        LuBan luBan = new LuBan(LuBan.getPhotoCacheDir(context));
        luBan.mFile = file;
        luBan.mFileList = Collections.singletonList(file);
        return luBan;
    }

    public static LuBan compress(Context context, List<File> files) {
        LuBan luBan = new LuBan(LuBan.getPhotoCacheDir(context));
        luBan.mFileList = files;
        luBan.mFile = files.get(0);
        return luBan;
    }

    /**
     * 自定义压缩模式 FIRST_GEAR、THIRD_GEAR、CUSTOM_GEAR
     *
     * @param gear
     * @return
     */
    public LuBan putGear(@GEAR int gear) {
        mBuilder.gear = gear;
        return this;
    }

    /**
     * 自定义图片压缩格式
     *
     * @param compressFormat
     * @return
     */
    public LuBan setCompressFormat(Bitmap.CompressFormat compressFormat) {
        mBuilder.compressFormat = compressFormat;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大体积
     *
     * @param size
     * @return
     */
    public LuBan setMaxSize(int size) {
        mBuilder.maxSize = size;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大宽度
     *
     * @param width 最大宽度
     * @return
     */
    public LuBan setMaxWidth(int width) {
        mBuilder.maxWidth = width;
        return this;
    }

    /**
     * CUSTOM_GEAR 指定目标图片的最大高度
     *
     * @param height 最大高度
     * @return
     */
    public LuBan setMaxHeight(int height) {
        mBuilder.maxHeight = height;
        return this;
    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnCompressListener listener) {
        asObservable().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.onStart();
                    }
                }).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                listener.onSuccess(file);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                listener.onError(throwable);
            }
        });
    }

    /**
     * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
     *
     * @param listener 接收回调结果
     */
    public void launch(final OnMultiCompressListener listener) {
        asListObservable().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        listener.onStart();
                    }
                })
                .subscribe(new Action1<List<File>>() {
                    @Override
                    public void call(List<File> files) {
                        listener.onSuccess(files);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError(throwable);
                    }
                });
    }

    /**
     * 返回File Observable
     *
     * @return
     */
    public Observable<File> asObservable() {
        LuBanCompressor compresser = new LuBanCompressor(mBuilder);
        return compresser.singleAction(mFile);
    }

    /**
     * 返回fileList Observable
     *
     * @return
     */
    public Observable<List<File>> asListObservable() {
        LuBanCompressor compressor = new LuBanCompressor(mBuilder);
        return compressor.multiAction(mFileList);
    }

    // Utils

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store
     * retrieved media and thumbnails.
     *
     * @param context A context.
     * @see #getPhotoCacheDir(Context, String)
     */
    private static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, LuBan.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store
     * retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getPhotoCacheDir(Context)
     */
    private static File getPhotoCacheDir(Context context, String cacheName) {
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /**
     * 清空Luban所产生的缓存
     * Clears the cache generated by Luban
     *
     * @return
     */
    public LuBan clearCache() {
        if (mBuilder.cacheDir.exists()) {
            deleteFile(mBuilder.cacheDir);
        }
        return this;
    }

    /**
     * 清空目标文件或文件夹
     * Empty the target file or folder
     */
    private void deleteFile(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File file : fileOrDirectory.listFiles()) {
                deleteFile(file);
            }
        }
        fileOrDirectory.delete();
    }


}