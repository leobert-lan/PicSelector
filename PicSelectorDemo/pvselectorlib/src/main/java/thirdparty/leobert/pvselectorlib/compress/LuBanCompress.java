package thirdparty.leobert.pvselectorlib.compress;

import android.content.Context;

import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LuBanCompress implements CompressInterface {
    private List<LocalMedia> images;
    private CompressInterface.CompressListener listener;
    private Context context;
    private LuBanOptions options;
    private ArrayList<File> files = new ArrayList<>();

    public LuBanCompress(Context context,
                         CompressConfig config,
                         List<LocalMedia> images,
                         CompressListener listener) {
        options = config.getLuBanOptions();
        this.images = images;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressError(images, " images is null");
            return;
        }
        for (LocalMedia image : images) {
            if (image == null) {
                listener.onCompressError(images, " There are pictures of fetchCompressInterface  is null.");
                return;
            }
            if (image.isCropped())
                files.add(new File(image.getCroppedPath()));
            else
                files.add(new File(image.getPath()));
        }

        if (images.size() == 1)
            compressOne();
        else
            compressMulti();
    }

    private void compressOne() {
        LuBan.compress(context, files.get(0))
                .putGear(LuBan.CUSTOM_GEAR)
                .setMaxHeight(options.getMaxHeight())
                .setMaxWidth(options.getMaxWidth())
                .setMaxSize(options.getMaxSize() / 1000)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        LocalMedia image = images.get(0);
                        image.setCompressPath(file.getPath());
                        image.setCompressed(true);
                        listener.onCompressSuccess(images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressError(images, e.getMessage() + " is fetchCompressInterface failures");
                    }
                });
    }

    private void compressMulti() {
        LuBan.compress(context, files)
                .putGear(LuBan.CUSTOM_GEAR)
                .setMaxSize(
                        options.getMaxSize() / 1000)                // limit the final image size（unit：Kb）
                .setMaxHeight(options.getMaxHeight())             // limit image height
                .setMaxWidth(options.getMaxWidth())
                .launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        handleCompressCallBack(fileList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressError(images, "[fetchCompressInterface failure] message:\r" +
                                e.getMessage());
                    }
                });
    }

    private void handleCompressCallBack(List<File> files) {
        for (int i = 0, j = images.size(); i < j; i++) {
            LocalMedia image = images.get(i);
            image.setCompressed(true);
            image.setCompressPath(files.get(i).getPath());
        }
        listener.onCompressSuccess(images);
    }
}
