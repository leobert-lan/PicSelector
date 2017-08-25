package thirdparty.leobert.pvselectorlib.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.yalantis.ucrop.util.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import thirdparty.leobert.pvselectorlib.rotate.RotateUtil;

public class CompressImageUtil {
    private CompressConfig config;
    private Context context;
    private Handler mhHandler = new Handler();

    public CompressImageUtil(Context context, CompressConfig config) {
        this.context = context;
        this.config = config == null ? CompressConfig.ofDefaultConfig() : config;
    }

    public void compress(String imagePath, CompressListener listener) {
        if (config.isEnablePixelCompress())
            pixelCompress(imagePath, listener);
        else
            qualityCompress(imagePath, listener);
    }

    private void pixelCompress(String imagePath, CompressListener listener) {
        try {
            compressImageByPixel(imagePath, listener);
        } catch (FileNotFoundException e) {
            listener.onCompressFailed(imagePath,
                    String.format("图片压缩失败,%s", e.toString()));
            e.printStackTrace();
        }
    }

    private void qualityCompress(String imagePath, CompressListener listener) {
        int rotation = RotateUtil.getBitmapDegree(imagePath);
        Bitmap origin = BitmapFactory.decodeFile(imagePath);
        Bitmap res;
        if (rotation != 0) {
            res = RotateUtil.rotateBitmapByDegree(origin, rotation);
        } else {
            res = origin;
        }
        compressImageByQuality(res, imagePath, listener);
    }

    /**
     * 多线程压缩图片的质量
     *
     * @param bitmap  内存中的图片
     * @param imgPath 图片的保存路径
     * @author JPH
     * @date 2014-12-5下午11:30:43
     */
    private void compressImageByQuality(final Bitmap bitmap, final String imgPath, final CompressListener listener) {
        if (bitmap == null) {
            sendMsg(false, imgPath, "像素压缩失败,bitmap is null", listener);
            return;
        }
        new Thread(new Runnable() {//开启多线程进行压缩处理
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int options = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                while (baos.toByteArray().length > config.getMaxSize()) {//循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                    baos.reset();
                    options -= 5;
                    if (options <= 5)
                        options = 5;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    if (options == 5)
                        break;
                }
//				if(bitmap!=null&&!bitmap.isRecycled()){
//					bitmap.recycle();//回收内存中的图片
//				}
                try {
                    File thumbnailFile = getThumbnailFile(new File(imgPath));
                    FileOutputStream fos = new FileOutputStream(thumbnailFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    sendMsg(true, thumbnailFile.getPath(), null, listener);
                } catch (Exception e) {
                    sendMsg(false, imgPath, "质量压缩失败", listener);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void compressImageByPixel(String imgPath,
                                      CompressListener listener)
            throws FileNotFoundException {
        if (imgPath == null) {
            sendMsg(false, imgPath, "要压缩的文件不存在", listener);
            return;
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float maxSize = config.getMaxPixel();
        int be = 1;
        if (width >= height && width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
            be = (int) (newOpts.outWidth / maxSize);
            be++;
        } else if (width < height && height > maxSize) {
            be = (int) (newOpts.outHeight / maxSize);
            be++;
        }
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        if (config.isEnableQualityCompress()) {
            compressImageByQuality(bitmap, imgPath, listener);//压缩好比例大小后再进行质量压缩
        } else {
            int rotation = RotateUtil.getBitmapDegree(imgPath);
            Bitmap dest;
            if (rotation != 0) {
                dest = RotateUtil.rotateBitmapByDegree(bitmap, rotation);
            } else {
                dest = bitmap;
            }
            File thumbnailFile = getThumbnailFile(new File(imgPath));
            dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(thumbnailFile));

            listener.onCompressSuccess(thumbnailFile.getPath());
        }
    }


    private void sendMsg(final boolean isSuccess,
                         final String imagePath,
                         final String message,
                         final CompressListener listener) {
        mhHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    listener.onCompressSuccess(imagePath);
                } else {
                    listener.onCompressFailed(imagePath, message);
                }
            }
        });
    }

    private File getThumbnailFile(File file) {
        if (file == null || !file.exists()) return file;
        return FileUtils.getPhotoCacheDir(context, file);
    }

    /**
     * 压缩结果监听器
     */
    public interface CompressListener {
        /**
         * 压缩成功
         *
         * @param imgPath 压缩图片的路径
         */
        void onCompressSuccess(String imgPath);

        /**
         * 压缩失败
         *
         * @param imgPath 压缩失败的图片
         * @param msg     失败的原因
         */
        void onCompressFailed(String imgPath, String msg);
    }
}
