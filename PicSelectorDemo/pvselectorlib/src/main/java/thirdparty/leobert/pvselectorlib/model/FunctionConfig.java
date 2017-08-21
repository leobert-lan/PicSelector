package thirdparty.leobert.pvselectorlib.model;

import android.support.annotation.IntDef;

import com.yalantis.ucrop.entity.LocalMedia;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class FunctionConfig implements Serializable, UIConfig.UIConfigDelegate {


    ///////////////////////////////////////////////////////////////////////////
    //  crop mode const&annotation define
    ///////////////////////////////////////////////////////////////////////////

    public static final int CROP_MODE_DEFAULT = 0;
    public static final int CROP_MODE_1_1 = 11;
    public static final int CROP_MODE_3_4 = 34;
    public static final int CROP_MODE_3_2 = 32;
    public static final int CROP_MODE_16_9 = 169;

    /**
     * scopes of crop
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CROP_MODE_DEFAULT,
            CROP_MODE_1_1,
            CROP_MODE_3_4,
            CROP_MODE_3_2,
            CROP_MODE_16_9})
    public @interface CropMode {
    }

    ///////////////////////////////////////////////////////////////////////////
    // select mode const&annotation define
    ///////////////////////////////////////////////////////////////////////////

    public final static int SELECT_MODE_MULTIPLE = 1;// 多选
    public final static int SELECT_MODE_SINGLE = 2;// 单选

    /**
     * mode for select
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SELECT_MODE_MULTIPLE,
            SELECT_MODE_SINGLE})
    public @interface SelectMode {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Video record quality const&annotation define
    ///////////////////////////////////////////////////////////////////////////

    public static final int RECORD_QUALITY_ORDINARY = 0;// 普通 低质量
    public static final int RECORD_QUALITY_HIGH = 1;// 清晰

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RECORD_QUALITY_ORDINARY,
            RECORD_QUALITY_HIGH
    })
    public @interface VideoRecordQuality {
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * zero mean will crop the real width
     */
    public static final int CROP_WIDTH_DEFAULT = 0;

    /**
     * zero mean will crop the real width
     */
    public static final int CROP_HEIGHT_DEFAULT = 0;


    public static final int SELECT_MAX_NUM = 9;
    public static final int MAX_COMPRESS_SIZE = 102400;


    @LocalMedia.MediaType
    private int type = LocalMedia.TYPE_PICTURE;

    @CropMode
    private int cropMode = CROP_MODE_DEFAULT; // 裁剪模式; 默认、1:1、3:4、3:2、16:9

    private int maxSelectNum = SELECT_MAX_NUM; // 多选最大可选数量

    @SelectMode
    private int selectMode = SELECT_MODE_MULTIPLE; // 单选 or 多选

    private boolean isShowCamera = true; // 是否显示相机
    private boolean enablePreview = true; // 是否预览图片
    private boolean enableCrop; // 是否裁剪图片，只针对单选图片有效
    private boolean isPreviewVideo; // 是否可预览视频(播放)


    private UIConfig uiConfig = new UIConfig();

    private int cropWidth = CROP_WIDTH_DEFAULT; // 裁剪宽度  如果值大于图片原始宽高 将返回原图大小
    private int cropHeight = CROP_HEIGHT_DEFAULT;// 裁剪高度  如果值大于图片原始宽高 将返回原图大小
    private int recordVideoSecond = 0;// 录视频秒数

    @VideoRecordQuality
    private int recordVideoDefinition = RECORD_QUALITY_ORDINARY;// 视频清晰度


    private boolean enablePictureCompress = false;// 是否压缩图片，默认不压缩

    protected int compressQuality = 100;// 图片压缩质量,默认无损
    protected List<LocalMedia> selectMedia = new ArrayList<>();// 已选择的图片
    protected int compressFlag = 1; // 1 系统自带压缩 2 luban压缩
    protected int compressW;
    protected int compressH;

    protected CharSequence completeText;

    public CharSequence getCompleteText() {
        return completeText;
    }

    public void setCompleteText(CharSequence completeText) {
        this.completeText = completeText;
    }

    public int getCompressW() {
        return compressW;
    }

    public void setCompressW(int compressW) {
        this.compressW = compressW;
    }

    public int getCompressH() {
        return compressH;
    }

    public void setCompressH(int compressH) {
        this.compressH = compressH;
    }

    public int getCompressFlag() {
        return compressFlag;
    }

    public void setCompressFlag(int compressFlag) {
        this.compressFlag = compressFlag;
    }

    /**
     * 是否启用像素压缩
     */
    protected boolean isEnablePixelCompress = true;
    /**
     * 是否启用质量压缩
     */
    protected boolean isEnableQualityCompress = true;

    public boolean isEnableQualityCompress() {
        return isEnableQualityCompress;
    }

    public void setEnableQualityCompress(boolean enableQualityCompress) {
        isEnableQualityCompress = enableQualityCompress;
    }

    public boolean isEnablePixelCompress() {
        return isEnablePixelCompress;
    }

    public void setEnablePixelCompress(boolean enablePixelCompress) {
        isEnablePixelCompress = enablePixelCompress;
    }

    public List<LocalMedia> getSelectMedia() {
        return selectMedia;
    }

    public void setSelectMedia(List<LocalMedia> selectMedia) {
        this.selectMedia = selectMedia;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }


    @VideoRecordQuality
    public int getRecordVideoDefinition() {
        return recordVideoDefinition;
    }

    public void setRecordVideoDefinition(@VideoRecordQuality int recordVideoDefinition) {
        this.recordVideoDefinition = recordVideoDefinition;
    }

    public int getRecordVideoSecond() {
        return recordVideoSecond;
    }

    public void setRecordVideoSecond(int recordVideoSecond) {
        this.recordVideoSecond = recordVideoSecond;
    }

    public boolean getPictureCompressEnable() {
        return enablePictureCompress;
    }

    public void setPictureCompressEnable(boolean enablePictureCompress) {
        this.enablePictureCompress = enablePictureCompress;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    ///////////////////////////////////////////////////////////////////////////
    // UIConfigDelegate
    ///////////////////////////////////////////////////////////////////////////
    public int getImageSpanCount() {
        return uiConfig.getImageSpanCount();
    }

    public void setImageSpanCount(int imageSpanCount) {
        this.uiConfig.setImageSpanCount(imageSpanCount);
    }

    public int getPreviewBottomBgColor() {
        return uiConfig.getPreviewBottomBgColor();
    }

    public void setPreviewBottomBgColor(int previewBottomBgColor) {
        this.uiConfig.setPreviewBottomBgColor(previewBottomBgColor);
    }

    public int getBottomBgColor() {
        return uiConfig.getBottomBgColor();
    }

    public void setBottomBgColor(int bottomBgColor) {
        this.uiConfig.setBottomBgColor(bottomBgColor);
    }


    @Override
    public boolean isDisplayCandidateNo() {
        return uiConfig.isDisplayCandidateNo();
    }

    @Override
    public void setDisplayCandidateNo(boolean displayCandidateNo) {
        this.uiConfig.setDisplayCandidateNo(displayCandidateNo);
    }

    @Override
    public int getPreviewTxtColor() {
        return uiConfig.getPreviewTxtColor();
    }

    @Override
    public void setPreviewTxtColor(int previewTxtColor) {
        this.uiConfig.setPreviewTxtColor(previewTxtColor);
    }

    @Override
    public int getCompleteTxtColor() {
        return uiConfig.getCompleteTxtColor();
    }

    @Override
    public void setCompleteTxtColor(int completeTxtColor) {
        this.uiConfig.setCompleteTxtColor(completeTxtColor);
    }

    public int getThemeStyle() {
        return uiConfig.getThemeStyle();
    }

    public void setThemeStyle(int themeStyle) {
        this.uiConfig.setThemeStyle(themeStyle);
    }

    @Override
    public int getCheckedBoxDrawable() {
        return uiConfig.getCheckedBoxDrawable();
    }

    @Override
    public void setCheckedBoxDrawable(int checkedBoxDrawable) {
        this.uiConfig.setCheckedBoxDrawable(checkedBoxDrawable);
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    @LocalMedia.MediaType
    public int getType() {
        return type;
    }

    public void setType(@LocalMedia.MediaType int type) {
        this.type = type;
    }

    @CropMode
    public int getCropMode() {
        return cropMode;
    }

    public void setCropMode(@CropMode int cropMode) {
        this.cropMode = cropMode;
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    @SelectMode
    public int getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(@SelectMode int selectMode) {
        this.selectMode = selectMode;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    public boolean isEnablePreview() {
        return enablePreview;
    }

    public void setEnablePreview(boolean enablePreview) {
        this.enablePreview = enablePreview;
    }

    public boolean isEnableCrop() {
        return enableCrop;
    }

    public void setEnableCrop(boolean enableCrop) {
        this.enableCrop = enableCrop;
    }

    public boolean isPreviewVideo() {
        return isPreviewVideo;
    }

    public void setPreviewVideo(boolean previewVideo) {
        isPreviewVideo = previewVideo;
    }

}
