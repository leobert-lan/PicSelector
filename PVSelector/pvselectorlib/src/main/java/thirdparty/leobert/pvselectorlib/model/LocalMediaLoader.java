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

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import thirdparty.leobert.pvselectorlib.R;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocalMediaLoader {
    // load type
    @LocalMedia.MediaType
    private static final int TYPE_PICTURE = LocalMedia.TYPE_PICTURE;

    @LocalMedia.MediaType
    private static final int TYPE_VIDEO = LocalMedia.TYPE_VIDEO;

    public int index = 0;
    private final static String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID
    };

    private final static String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION,
    };

    @LocalMedia.MediaType
    private int type = TYPE_PICTURE;

    private FragmentActivity activity;


    public LocalMediaLoader(FragmentActivity activity,
                            @LocalMedia.MediaType int type) {
        this.activity = activity;
        this.type = type;
    }

    public void loadAllImage(final OnLocalMediaLoadedListener imageLoadListener) {
        activity.getSupportLoaderManager().initLoader(type, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        CursorLoader cursorLoader = null;
                        if (id == TYPE_PICTURE) {
                            cursorLoader = new CursorLoader(
                                    activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION, MediaStore.Images.Media.MIME_TYPE + "=? or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?" + " or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
                                    new String[]{"image/jpeg", "image/png", "image/gif"}, IMAGE_PROJECTION[2] + " DESC");
                        } else if (id == TYPE_VIDEO) {
                            cursorLoader = new CursorLoader(
                                    activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    VIDEO_PROJECTION, null, null, VIDEO_PROJECTION[2] + " DESC");
                        }
                        return cursorLoader;
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        try {
                            ArrayList<LocalMediaFolder> imageFolders = new ArrayList<LocalMediaFolder>();
                            LocalMediaFolder allImageFolder = new LocalMediaFolder();
                            List<LocalMedia> allImages = new ArrayList<LocalMedia>();
                            if (data != null) {
                                int count = data.getCount();
                                if (count > 0) {
                                    data.moveToFirst();
                                    do {

                                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                                        // 如原图路径不存在或者路径存在但文件不存在,就结束当前循环
                                        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
                                            continue;
                                        }
                                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                                        int duration = (type == TYPE_VIDEO ? data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4])) : 0);
                                        LocalMedia image = new LocalMedia(path, dateTime, duration, type);
                                        LocalMediaFolder folder = getImageFolder(path, imageFolders);
                                        folder.getImages().add(image);
                                        folder.setType(type);
                                        index++;
                                        folder.setImageNum(folder.getImageNum() + 1);
                                        // 最近相册中  只添加最新的100条
                                        if (index <= 100) {
                                            allImages.add(image);
                                            allImageFolder.setType(type);
                                            allImageFolder.setImageNum(allImageFolder.getImageNum() + 1);
                                        }

                                    } while (data.moveToNext());

                                    if (allImages.size() > 0) {
                                        sortFolder(imageFolders);
                                        imageFolders.add(0, allImageFolder);
                                        String title;
                                        switch (type) {
                                            case LocalMedia.TYPE_VIDEO:
                                                title = activity.getString(R.string.lately_video);
                                                break;
                                            case LocalMedia.TYPE_PICTURE:
                                            default:
                                                title = activity.getString(R.string.lately_image);
                                                break;
                                        }
                                        allImageFolder.setFirstImagePath(allImages.get(0).getPath());
                                        allImageFolder.setName(title);
                                        allImageFolder.setType(type);
                                        allImageFolder.setImages(allImages);
                                    }
                                    imageLoadListener.onLocalMediaLoadComplete(imageFolders);
                                    data.close();
                                } else {
                                    // 如果没有相册
                                    imageLoadListener.onLocalMediaLoadComplete(imageFolders);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {
                    }
                });
    }

    private void sortFolder(List<LocalMediaFolder> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<LocalMediaFolder>() {
            @Override
            public int compare(LocalMediaFolder lhs, LocalMediaFolder rhs) {
                if (lhs.getImages() == null || rhs.getImages() == null) {
                    return 0;
                }
                int lSize = lhs.getImageNum();
                int rSize = rhs.getImageNum();
                return lSize == rSize ? 0 : (lSize < rSize ? 1 : -1);
            }
        });
    }

    private LocalMediaFolder getImageFolder(String path,
                                            List<LocalMediaFolder> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (LocalMediaFolder folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }

    public interface OnLocalMediaLoadedListener {
        void onLocalMediaLoadComplete(List<LocalMediaFolder> folders);
    }
}
