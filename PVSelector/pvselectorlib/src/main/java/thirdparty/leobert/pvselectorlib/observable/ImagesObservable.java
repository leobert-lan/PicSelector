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

package thirdparty.leobert.pvselectorlib.observable;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;

import java.util.ArrayList;
import java.util.List;

public class ImagesObservable implements SubjectListener {
    //观察者接口集合
    private List<ObserverListener> observers = new ArrayList<>();

    private List<LocalMediaFolder> folders;
    private List<LocalMedia> medias;
    private List<LocalMedia> selectedImages;
    private static ImagesObservable sObserver;

    private ImagesObservable() {
        folders = new ArrayList<>();
        medias = new ArrayList<>();
        selectedImages = new ArrayList<>();
    }

    public static ImagesObservable getInstance() {
        if (sObserver == null) {
            synchronized (ImagesObservable.class) {
                if (sObserver == null) {
                    sObserver = new ImagesObservable();
                }
            }
        }
        return sObserver;
    }


    /**
     * 存储文件夹图片
     *
     * @param list
     */

    public void saveLocalFolders(List<LocalMediaFolder> list) {
        if (list != null) {
            folders = list;
        }
    }


    /**
     * 存储图片
     *
     * @param list
     */
    public void saveLocalMedia(List<LocalMedia> list) {
        medias = list;
    }


    /**
     * 读取图片
     */
    public List<LocalMedia> readLocalMedias() {
        return medias;
    }

    /**
     * 读取所有文件夹图片
     */
    public List<LocalMediaFolder> readLocalFolders() {
        return folders;
    }


    /**
     * 读取选中的图片
     */
    public List<LocalMedia> readSelectLocalMedias() {
        return selectedImages;
    }


    public void clearLocalFolders() {
        if (folders != null)
            folders.clear();
    }

    public void clearLocalMedia() {
        if (medias != null)
            medias.clear();
    }

    public void clearSelectedLocalMedia() {
        if (selectedImages != null)
            selectedImages.clear();
    }

    @Override
    public void add(ObserverListener observerListener) {
        observers.add(observerListener);
    }

    /**
     * 相册所有列表文件夹
     *
     * @param folders
     */
    @Override
    public void notifyFolderObserver(List<LocalMediaFolder> folders) {
        for (ObserverListener observerListener : observers) {
            observerListener.observerUpFoldersData(folders);
        }
    }

    /**
     * 选中图片集合观察者
     *
     * @param selectMedias
     */
    @Override
    public void notifySelectLocalMediaObserver(List<LocalMedia> selectMedias) {
        for (ObserverListener observerListener : observers) {
            observerListener.observerUpSelectsData(selectMedias);
        }
    }

    @Override
    public void remove(ObserverListener observerListener) {
        if (observers.contains(observerListener)) {
            observers.remove(observerListener);
        }
    }
}