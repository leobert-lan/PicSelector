package thirdparty.leobert.pvselectorlib.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.MultiUCrop;
import com.yalantis.ucrop.Options;
import com.yalantis.ucrop.SingleUCrop;
import com.yalantis.ucrop.dialog.SweetAlertDialog;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;
import com.yalantis.ucrop.util.FileUtils;
import com.yalantis.ucrop.util.ScreenUtils;
import com.yalantis.ucrop.util.ToolbarUtil;
import com.yalantis.ucrop.util.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.Logger;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.adapter.PictureImageGridAdapter;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.FinishActionConsumer;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.ImageCroppedActionConsumer;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.RefreshDataActionConsumer;
import thirdparty.leobert.pvselectorlib.compress.CompressConfig;
import thirdparty.leobert.pvselectorlib.compress.CompressImageOptions;
import thirdparty.leobert.pvselectorlib.compress.CompressInterface;
import thirdparty.leobert.pvselectorlib.compress.LuBanOptions;
import thirdparty.leobert.pvselectorlib.decoration.GridSpacingItemDecoration;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.model.LocalMediaLoader;
import thirdparty.leobert.pvselectorlib.model.PictureConfig;
import thirdparty.leobert.pvselectorlib.observable.ImagesObservable;

/**
 * display the snapshot of each media in the folder, in grid.
 */
public class MediaFolderContentDisplayActivity extends PVBaseActivity
        implements View.OnClickListener,
        PictureImageGridAdapter.OnPhotoSelectChangedListener {
    public final String TAG = "FolderContentDisplay";
//            MediaFolderContentDisplayActivity.class.getSimpleName(); too long

    private List<LocalMedia> localMedias = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView tvSelectedCount;

    /**
     * located at the right in the bottom bar, click to complete select
     * start crop or fetchCompressInterface or finish if none next flow exist
     */
    private TextView tvOpComplete;

    private RelativeLayout rlBottomBar;

    private RelativeLayout rlToolBar;
    private TextView tvTitle;
    private ImageButton navBack;

    /**
     * located at the right in the toolbar,cancel select when clicked
     */
    private TextView tvOpCancel;

    /**
     * located at the bottom bar,click to preview all the selected media(Only Picture
     * has this operate,so it will be hidden when select video)
     */
    private Button btnPreview;

    private PictureImageGridAdapter adapter;
    private String cameraPath;
    private SweetAlertDialog dialog;
    private List<LocalMediaFolder> folders = new ArrayList<>();

    private boolean isFirstStarted;

    private FinishActionConsumer finishActionConsumer
            = new FinishActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        }
    };

    private RefreshDataActionConsumer refreshDataActionConsumer
            = new RefreshDataActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            List<LocalMedia> selectImages = (List<LocalMedia>) intent
                    .getSerializableExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST);
            if (selectImages != null && adapter != null)
                adapter.bindSelectedMedias(selectImages);
        }
    };

    private ImageCroppedActionConsumer imageCroppedActionConsumer
            = new ImageCroppedActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            List<LocalMedia> result = (List<LocalMedia>) intent
                    .getSerializableExtra(Consts.Extra.EXTRA_SERIALIZABLE_RESULT);
            if (result == null)
                result = new ArrayList<>();
            handleCropResult(result);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_media_folder_content_display);

        registerBroadcastConsumer(finishActionConsumer,
                refreshDataActionConsumer,
                imageCroppedActionConsumer);

        initUiInstance();

        initUiEventListener();

        isFirstStarted = getIntent()
                .getBooleanExtra(Consts.Extra.EXTRA_IS_FIRST_STARTED, false);

        if (!isFirstStarted) {
            // 第一次启动ImageActivity，没有获取过相册列表
            // 先判断手机是否有读取权限，主要是针对6.0已上系统
            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                readLocalMedia();
            } else {
                requestPermission(Consts.PermissionReqCode.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            selectMedias = (List<LocalMedia>) getIntent()
                    .getSerializableExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST);
        }

        String folderName = getIntent()
                .getStringExtra(Consts.Extra.EXTRA_FOLDER_NAME);
        folders = ImagesObservable.getInstance().readLocalFolders();
        if (folders == null) {
            folders = new ArrayList<>();
        }

        if (savedInstanceState != null) {
            cameraPath =
                    savedInstanceState.getString(Consts.BundleKey.CAMERA_SAVED_PATH);
        }

        localMedias = ImagesObservable.getInstance().readLocalMedias();
        if (localMedias == null) {
            localMedias = new ArrayList<>();
        }

        if (selectMedias == null) {
            selectMedias = new ArrayList<>();
        }
        if (enablePreview && selectMode == FunctionConfig.SELECT_MODE_MULTIPLE) {
            if (type == LocalMedia.TYPE_VIDEO) {
                // 如果是视频不能预览
                btnPreview.setVisibility(View.GONE);
            } else {
                btnPreview.setVisibility(View.VISIBLE);
            }
        } else if (selectMode == FunctionConfig.SELECT_MODE_SINGLE) {
            rlBottomBar.setVisibility(View.GONE);
        } else {
            btnPreview.setVisibility(View.GONE);
        }
        if (folderName != null && !folderName.equals("")) {
            tvTitle.setText(folderName);
        } else {
            switch (type) {
                case LocalMedia.TYPE_PICTURE:
                    tvTitle.setText(getString(R.string.lately_image));
                    break;
                case LocalMedia.TYPE_VIDEO:
                    tvTitle.setText(getString(R.string.lately_video));
                    break;
            }
        }
        // 解决调用 notifyItemChanged 闪烁问题,取消默认动画
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        // 如果是显示数据风格，则默认为qq选择风格
        if (displayCandidateNo) {
            tvSelectedCount.setBackgroundResource(R.drawable.message_oval_blue);
        }
        String titleText = tvTitle.getText().toString().trim();
        if (showCamera) {
            if (!Utils.isNull(titleText) && titleText.startsWith("最近") || titleText.startsWith("Recent")) {
                // 只有最近相册 才显示拍摄按钮，不然相片混乱
                showCamera = true;
            } else {
                showCamera = false;
            }
        }
        adapter = new PictureImageGridAdapter(this, showCamera, maxSelectNum, selectMode, enablePreview, enablePreviewVideo, cb_drawable, displayCandidateNo);
        recyclerView.setAdapter(adapter);
        if (selectMedias.size() > 0) {
            ChangeImageNumber(selectMedias);
            adapter.bindSelectedMedias(selectMedias);
        }
        adapter.bindMediaData(localMedias);
        adapter.setOnPhotoSelectChangedListener(MediaFolderContentDisplayActivity.this);
    }

    @Override
    protected void initUiInstance() {
        //instantiation
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rlBottomBar = (RelativeLayout) findViewById(R.id.rl_bar_bottom);
        navBack = (ImageButton) findViewById(R.id.picture_left_back);
        rlToolBar = (RelativeLayout) findViewById(R.id.album_rl_toolbar);
        tvTitle = (TextView) findViewById(R.id.album_tv_title);
        tvOpCancel = (TextView) findViewById(R.id.album_toolbar_rightop);
        tvOpComplete = (TextView) findViewById(R.id.bottombar_tv_select_complete);
        btnPreview = (Button) findViewById(R.id.bottombar_btn_preview);
        tvSelectedCount = (TextView) findViewById(R.id.bottombar_tv_select_count);

        //UI style
        rlToolBar.setBackgroundColor(backgroundColor);
        ToolbarUtil.setColorNoTranslucent(this, backgroundColor);
        rlBottomBar.setBackgroundColor(bottomBgColor);
        btnPreview.setTextColor(previewTxtColor);
        tvOpComplete.setTextColor(completeTxtColor);
        tvOpCancel.setText(getString(R.string.txt_cancel));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        //UI config
    }

    @Override
    protected void initUiEventListener() {
        btnPreview.setOnClickListener(this);
        tvOpComplete.setOnClickListener(this);
        navBack.setOnClickListener(this);
        tvOpCancel.setOnClickListener(this);
    }

    @Override
    protected void readLocalMedia() {
        /**
         * 根据type决定，查询本地图片或视频。
         */
        showDialog("请稍候...");
        new LocalMediaLoader(this, type).loadAllImage(new LocalMediaLoader.OnLocalMediaLoadedListener() {

            @Override
            public void onLocalMediaLoadComplete(List<LocalMediaFolder> folders) {
                dismissDialog();
                if (folders.size() > 0) {
                    // 取最近相册或视频数据
                    LocalMediaFolder folder = folders.get(0);
                    localMedias = folder.getImages();
                    adapter.bindMediaData(localMedias);
                    MediaFolderContentDisplayActivity.this.folders = folders;
                    ImagesObservable.getInstance().saveLocalFolders(folders);
                    ImagesObservable.getInstance().notifyFolderObserver(folders);
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            activityFinish(1);
        } else if (id == R.id.album_toolbar_rightop) {
            activityFinish(2);
        } else if (id == R.id.bottombar_btn_preview) {
            onBtnPreviewClicked();
        } else if (id == R.id.bottombar_tv_select_complete) {
            List<LocalMedia> images = adapter.getSelectedImages();
            if (enableCrop && type == LocalMedia.TYPE_PICTURE
                    && selectMode == FunctionConfig.SELECT_MODE_MULTIPLE) {
                // 是图片和选择压缩并且是多张，调用批量压缩
                startMultiCrop(images);
            } else {
                // 图片才压缩，视频不管
                if (enableCompress && type == LocalMedia.TYPE_PICTURE) {
                    compressImage(images);
                } else {
                    resultBack(images);
                }
            }
        }
    }

    /**
     * called when the "preview" button on clicked, located at the bottom bar
     * <p>
     * will preview the whole selected medias
     */
    private void onBtnPreviewClicked() {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();

        //build preview data
        List<LocalMedia> selectedImages = adapter.getSelectedImages();
        List<LocalMedia> medias = new ArrayList<>();
        for (LocalMedia media : selectedImages)
            medias.add(media);

        intent.putExtra(Consts.Extra.EXTRA_PREVIEW_LIST,
                (Serializable) medias);
        intent.putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                (Serializable) selectedImages);

        intent.putExtra(Consts.Extra.EXTRA_POSITION, 0);
        intent.putExtra(Consts.Extra.EXTRA_FROM_BOTTOMBAR_PREVIEW, true);
        intent.putExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG, config);
        intent.setClass(this, PicturePreviewActivity.class);
        startActivityForResult(intent,
                Consts.AcResultReqCode.REQUEST_MEDIA_PREVIEW);
    }

    private void resultBack(List<LocalMedia> images) {
        onResult(images);
    }

    @Override
    public void onTakePhoto() {
        // 启动相机拍照,先判断手机是否有拍照权限
        if (hasPermission(Manifest.permission.CAMERA)) {
            startCamera();
        } else {
            requestPermission(Consts.PermissionReqCode.GRANT_CAMERA,
                    Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onChange(List<LocalMedia> selectImages) {
        ChangeImageNumber(selectImages);
    }

    /**
     * 图片选中数量
     *
     * @param selectImages
     */
    public void ChangeImageNumber(List<LocalMedia> selectImages) {
        Animation animation;
        boolean enable = selectImages.size() != 0;
        if (enable) {
            btnPreview.setAlpha(1.0f);
            tvOpComplete.setEnabled(true);
            tvOpComplete.setAlpha(1.0f);
            btnPreview.setEnabled(true);
            animation = AnimationUtils.loadAnimation(mContext, R.anim.modal_in);
            tvSelectedCount.startAnimation(animation);
            tvSelectedCount.setVisibility(View.VISIBLE);
            tvSelectedCount.setText(selectImages.size() + "");
            tvOpComplete.setText(completeText);
        } else {
            tvOpComplete.setEnabled(false);
            btnPreview.setAlpha(0.5f);
            btnPreview.setEnabled(false);
            tvOpComplete.setAlpha(0.5f);
            if (selectImages.size() > 0) {
                animation = AnimationUtils.loadAnimation(mContext,
                        R.anim.modal_out);
                tvSelectedCount.startAnimation(animation);
            }
            tvSelectedCount.setVisibility(View.INVISIBLE);
            tvOpComplete.setText("请选择");
        }
    }

    @Override
    public void startCamera() {
        switch (type) {
            case LocalMedia.TYPE_VIDEO:
                // 录视频
                startCaptureVideo();
                break;
            case LocalMedia.TYPE_PICTURE:
            default:
                // 拍照
                startCapturePicture();
                break;
        }

    }

    @Override
    public void onMediaClick(LocalMedia media, int position) {
        if (selectMode == FunctionConfig.SELECT_MODE_SINGLE)
            onSingleCaseMediaClicked(adapter.getDatas(), position);
        else
            onMultiCaseMediaClicked(adapter.getDatas(), position);
    }

    private void onSingleCaseMediaClicked(List<LocalMedia> previewMedias,
                                          int position) {
        switch (type) {
            case LocalMedia.TYPE_VIDEO:
                handleSingleMediaSelectComplete(previewMedias.get(position));
                break;
            case LocalMedia.TYPE_PICTURE:
            default:
                if (enableCrop)
                    startSingleCasePictureCrop(previewMedias.get(position));
                else if (enableCompress)
                    startSingleCasePictureCompress(previewMedias.get(position));
                else
                    handleSingleMediaSelectComplete(previewMedias.get(position));
                break;
        }
    }

    private void onMultiCaseMediaClicked(List<LocalMedia> previewMedias, int position) {
        switch (type) {
            case LocalMedia.TYPE_VIDEO:
                startVideoPreview(previewMedias.get(position));
                break;
            case LocalMedia.TYPE_PICTURE:
            default:
                startPicturesPreview(previewMedias, position);
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // single case onclick callback handles
    ///////////////////////////////////////////////////////////////////////////

    private void startSingleCasePictureCrop(LocalMedia media) {
        startSingleCrop(media.getPath());
    }

    private void startSingleCasePictureCompress(LocalMedia media) {
        ArrayList<LocalMedia> result = new ArrayList<>();
        LocalMedia m = new LocalMedia();
        m.setPath(media.getPath());
        m.setType(type);
        result.add(m);
        compressImage(result);
    }

    private void handleSingleMediaSelectComplete(LocalMedia media) {
        ArrayList<LocalMedia> result = new ArrayList<>();
        LocalMedia m = new LocalMedia();
        m.setPath(media.getPath());
        m.setType(type);
        result.add(m);
        onSelectDone(result);
    }

    ///////////////////////////////////////////////////////////////////////////
    // multi case onclick callback handles
    ///////////////////////////////////////////////////////////////////////////

    private void startPicturesPreview(List<LocalMedia> previewMedias, int position) {
        if (Utils.isFastDoubleClick())
            return;
        Intent intent = new Intent();
        ImagesObservable.getInstance().saveLocalMedia(previewMedias);
        List<LocalMedia> selectedImages = adapter.getSelectedImages();
        intent.putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                (Serializable) selectedImages);
        intent.putExtra(Consts.Extra.EXTRA_POSITION, position);
        intent.putExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG, config);
        intent.setClass(mContext, PicturePreviewActivity.class);
        startActivityForResult(intent,
                Consts.AcResultReqCode.REQUEST_MEDIA_PREVIEW);
    }

    private void startVideoPreview(LocalMedia media) {
        if (Utils.isFastDoubleClick())
            return;
        Bundle bundle = new Bundle();
        bundle.putString(Consts.Extra.EXTRA_PREVIEW_VIDEO_PATH,
                media.getPath());
        bundle.putSerializable(Consts.Extra.EXTRA_FUNCTION_CONFIG,
                config);
        startActivity(VideoDisplayActivity.class, bundle);
    }

    /**
     * 裁剪
     *
     * @param path
     */
    protected void startSingleCrop(String path) {
        SingleUCrop singleUCrop = SingleUCrop.of(Uri.parse(path),
                Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg")));
        Options options = new Options.OptionsImpl();
        switch (cropMode) {
            case FunctionConfig.CROP_MODE_DEFAULT:
                options.withAspectRatio(0, 0);
                break;
            case FunctionConfig.CROP_MODE_1_1:
                options.withAspectRatio(1, 1);
                break;
            case FunctionConfig.CROP_MODE_3_2:
                options.withAspectRatio(3, 2);
                break;
            case FunctionConfig.CROP_MODE_3_4:
                options.withAspectRatio(3, 4);
                break;
            case FunctionConfig.CROP_MODE_16_9:
                options.withAspectRatio(16, 9);
                break;
        }

        options.setCompressionQuality(compressQuality);
        options.withMaxResultSize(cropW, cropH);
        options.backgroundColor(backgroundColor);
//        options.cropMode(cropMode); just as options.withAspectRatio()
        options.setCompressEnabled(enableCompress);
        singleUCrop.withOptions(options);
        singleUCrop.start(MediaFolderContentDisplayActivity.this);
    }

    /**
     * 多图裁剪
     *
     * @param medias
     */
    protected void startMultiCrop(List<LocalMedia> medias) {
        if (medias != null && medias.size() > 0) {
            LocalMedia media = medias.get(0);
            String path = media.getPath();
            // 去裁剪
            MultiUCrop multiUCrop = MultiUCrop.of(Uri.parse(path),
                    Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + ".jpg")));
            Options options = new Options.OptionsImpl();
            switch (cropMode) {
                case FunctionConfig.CROP_MODE_DEFAULT:
                    options.withAspectRatio(0, 0);
                    break;
                case FunctionConfig.CROP_MODE_1_1:
                    options.withAspectRatio(1, 1);
                    break;
                case FunctionConfig.CROP_MODE_3_2:
                    options.withAspectRatio(3, 2);
                    break;
                case FunctionConfig.CROP_MODE_3_4:
                    options.withAspectRatio(3, 4);
                    break;
                case FunctionConfig.CROP_MODE_16_9:
                    options.withAspectRatio(16, 9);
                    break;
            }
            options.setLocalMedia(medias);
            options.setCompressionQuality(compressQuality);
            options.withMaxResultSize(cropW, cropH);
            options.backgroundColor(backgroundColor);
            options.setCompressEnabled(enableCompress);
            multiUCrop.withOptions(options);
            multiUCrop.start(MediaFolderContentDisplayActivity.this);
        }

    }

    /**
     * start to camera、preview、crop
     */
    public void startCapturePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this, type);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri;
            String authority = getPackageName() + ".provider";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageUri = FileProvider.getUriForFile(mContext, authority, cameraFile);//通过FileProvider创建一个content类型的Uri
            } else {
                imageUri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent,
                    Consts.AcResultReqCode.REQUEST_START_CAMERA);
        }
    }

    /**
     * start to camera、video
     */
    public void startCaptureVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this, type);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri;
            String authority = getPackageName() + ".provider";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageUri = FileProvider.getUriForFile(mContext, authority, cameraFile);//通过FileProvider创建一个content类型的Uri
            } else {
                imageUri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, definition);
            startActivityForResult(cameraIntent,
                    Consts.AcResultReqCode.REQUEST_START_CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (resultCode == RESULT_OK) {
            // on take photo success
            if (requestCode == Consts.AcResultReqCode.REQUEST_START_CAMERA) {
                // 拍照返回
                File file = new File(cameraPath);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                if (selectMode == FunctionConfig.SELECT_MODE_SINGLE) {
                    // 如果是单选 拍照后直接返回
                    if (enableCrop && type == LocalMedia.TYPE_PICTURE) {
                        // 如果允许裁剪，并且是图片
                        startSingleCrop(cameraPath);
                    } else {
                        if (enableCompress && type == LocalMedia.TYPE_PICTURE) {
                            // 压缩图片
                            ArrayList<LocalMedia> compresses = new ArrayList<>();
                            LocalMedia compress = new LocalMedia();
                            compress.setPath(cameraPath);
                            compress.setType(type);
                            compresses.add(compress);
                            compressImage(compresses);
                        } else {
                            ArrayList<LocalMedia> result = new ArrayList<>();
                            LocalMedia m = new LocalMedia();
                            m.setPath(cameraPath);
                            m.setType(type);
                            result.add(m);
                            onSelectDone(result);
                        }
                    }
                } else {
                    // 多选 返回列表并选中当前拍照的
                    int duration = 0;
                    if (type == LocalMedia.TYPE_VIDEO) {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(file.getPath());
                        duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    } else {
                        duration = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
                    }
                    createNewFolder(folders);
                    // 生成拍照图片对象
                    LocalMedia media = new LocalMedia(file.getPath(), duration, duration, type);
                    // 根据新拍照生成的图片，插入到对应的相册当中，避免重新查询一遍数据库
                    LocalMediaFolder folder = getImageFolder(media.getPath(), folders);
                    // 更新当前图片所属文件夹
                    folder.getImages().add(0, media);// 插入到第一个位置
                    folder.setImageNum(folder.getImageNum() + 1);
                    folder.setFirstImagePath(media.getPath());
                    folder.setType(type);

                    // 取出最近文件夹 插入刚拍的照片或视频，并且如果大于100张，先移除最后一条在保存，因为最近文件夹中只显示100条数据
                    LocalMediaFolder mediaFolder = folders.get(0);
                    mediaFolder.setFirstImagePath(media.getPath());
                    mediaFolder.setType(type);
                    List<LocalMedia> localMedias = mediaFolder.getImages();
                    if (localMedias.size() >= 100) {
                        localMedias.remove(localMedias.size() - 1);
                    }
                    List<LocalMedia> images = adapter.getDatas();
                    images.add(0, media);
                    mediaFolder.setImages(images);
                    mediaFolder.setImageNum(mediaFolder.getImages().size());
                    // 没有到最大选择量 才做默认选中刚拍好的
                    if (adapter.getSelectedImages().size() < maxSelectNum) {
                        List<LocalMedia> selectedImages = adapter.getSelectedImages();
                        selectedImages.add(media);
                        adapter.bindSelectedMedias(selectedImages);
                        ChangeImageNumber(adapter.getSelectedImages());
                    }
                    adapter.bindMediaData(images);
                }

            }
        }
    }

    /**
     * 如果没有任何相册，先创建一个最近相册出来
     *
     * @param folders
     */
    private void createNewFolder(List<LocalMediaFolder> folders) {
        if (folders.size() == 0) {
            // 没有相册 先创建一个最近相册出来
            LocalMediaFolder newFolder = new LocalMediaFolder();
            String folderName;
            switch (type) {
                case LocalMedia.TYPE_VIDEO:
                    folderName = getString(R.string.lately_video);
                    break;
                case LocalMedia.TYPE_PICTURE:
                default:
                    folderName = getString(R.string.lately_image);
                    break;
            }
            newFolder.setName(folderName);
            newFolder.setPath("");
            newFolder.setFirstImagePath("");
            newFolder.setType(type);
            folders.add(newFolder);
        }
    }

    private void handleCropResult(List<LocalMedia> result) {
        if (result != null) {
            if (enableCompress) // && type == LocalMedia.TYPE_PICTURE) { always be picture
                compressImage(result);
            else
                onSelectDone(result);
            return;
        }
        Logger.d(TAG, "[empty data],on handle picture cropped result");
    }

    public void onSelectDone(List<LocalMedia> result) {
        onResult(result);
    }

    public void onResult(List<LocalMedia> images) {
        // 因为这里是单一实例的结果集，重新用变量接收一下在返回，
        // 不然会产生结果集被单一实例清空的问题
        List<LocalMedia> result = new ArrayList<>();
        for (LocalMedia media : images) {
            result.add(media);
        }
        PictureConfig.OnSelectResultCallback resultCallback =
                PictureConfig.getPictureConfig().getResultCallback();
        if (resultCallback != null) {
            resultCallback.onSelectSuccess(result);
        }
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
        sendBroadcast(Consts.BcActions.ACTION_FINISH_ACTIVITY);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Consts.BundleKey.CAMERA_SAVED_PATH, cameraPath);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                activityFinish(1);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void activityFinish(int type) {
        switch (type) {
            case 1:
                // 返回
                List<LocalMedia> selectedImages = adapter.getSelectedImages();
                ImagesObservable.getInstance().notifySelectLocalMediaObserver(selectedImages);
                finish();
                break;
            case 2:
                // 取消
                sendBroadcast(Consts.BcActions.ACTION_FINISH_ACTIVITY);
                finish();
                overridePendingTransition(0, R.anim.slide_bottom_out);
                break;
        }

    }

    /**
     * 处理图片压缩
     */
    private void compressImage(List<LocalMedia> result) {
        showDialog("处理中...");
        CompressConfig compressConfig = CompressConfig.ofDefaultConfig();
        switch (compressScheme) {
            case CompressConfig.SCHEME_LUBAN:
                // luban压缩
                LuBanOptions option = new LuBanOptions();
                option.setMaxHeight(compressH);
                option.setMaxWidth(compressW);
                option.setMaxSize(FunctionConfig.MAX_COMPRESS_SIZE);
                Logger.d(TAG, "check point");
                compressConfig = CompressConfig.ofLuBan(option);
                break;
            case CompressConfig.SCHEME_SYSTEM:
            default:
                // 系统自带压缩
                compressConfig.enablePixelCompress(config.isEnablePixelCompress());
                compressConfig.enableQualityCompress(config.isEnableQualityCompress());
                break;
        }

        CompressImageOptions.fetchCompressInterface(this, compressConfig, result, new CompressInterface.CompressListener() {
            @Override
            public void onCompressSuccess(List<LocalMedia> images) {
                // 压缩成功回调
                Logger.d(TAG, "compressSuccess:" + Logger.stringify(images));
                onResult(images);
                dismissDialog();
            }

            @Override
            public void onCompressError(List<LocalMedia> images, String msg) {
                // 压缩失败回调 返回原图
                Logger.i(TAG, "compressFailure" + Logger.stringify(images) + "\n" + msg);
                List<LocalMedia> selectedImages = adapter.getSelectedImages();
                onResult(selectedImages);
                dismissDialog();
            }
        }).compress();
    }

    private void showDialog(String msg) {
        dialog = new SweetAlertDialog(MediaFolderContentDisplayActivity.this);
        dialog.setTitleText(msg);
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//        }
//    }
}
