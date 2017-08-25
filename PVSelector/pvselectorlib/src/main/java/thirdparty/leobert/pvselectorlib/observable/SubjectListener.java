package thirdparty.leobert.pvselectorlib.observable;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;

import java.util.List;

public interface SubjectListener {
    void add(ObserverListener observerListener);

    void notifyFolderObserver(List<LocalMediaFolder> folders);

    void notifySelectLocalMediaObserver(List<LocalMedia> medias);

    void remove(ObserverListener observerListener);
}
