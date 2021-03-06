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

package thirdparty.leobert.pvselectorlib.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;
import com.yalantis.ucrop.util.ToolbarUtil;
import com.yalantis.ucrop.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.adapter.PictureAlbumDirectoryAdapter;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.FinishActionConsumer;
import thirdparty.leobert.pvselectorlib.decoration.RecycleViewDivider;
import thirdparty.leobert.pvselectorlib.model.PictureConfig;
import thirdparty.leobert.pvselectorlib.observable.ImagesObservable;
import thirdparty.leobert.pvselectorlib.observable.ObserverListener;

/**
 * display the list of media folder.
 */
public class AlbumDirectoryListActivity
        extends PVBaseActivity
        implements View.OnClickListener,
        PictureAlbumDirectoryAdapter.OnItemClickListener,
        ObserverListener {

    private List<LocalMediaFolder> folders = new ArrayList<>();
    private PictureAlbumDirectoryAdapter adapter;
    private RecyclerView recyclerView;

    private TextView tvHintEmptyFolder;

    private RelativeLayout rlToolBar;

    /** tv widget, display activity title/label */
    private TextView tvTitle;

    /** tv widget, located at the right in the toolbar, refers to cancel select in
     * this case */
    private TextView tvOpCancel;

    private List<LocalMedia> selectMedias = new ArrayList<>();


    private FinishActionConsumer finishActionConsumer = new FinishActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_album);

        registerBroadcastConsumer(finishActionConsumer);

        if (selectMedias == null)
            selectMedias = new ArrayList<>();

        initUiInstance();
        initUiEventListener();
        initData();
    }

    @Override
    protected void initUiInstance() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvHintEmptyFolder = (TextView) findViewById(R.id.tv_empty);
        rlToolBar = (RelativeLayout) findViewById(R.id.album_rl_toolbar);
        tvTitle = (TextView) findViewById(R.id.album_tv_title);
        tvOpCancel = (TextView) findViewById(R.id.album_toolbar_rightop);

        tvTitle.setText(initTitleName());

        ToolbarUtil.setColorNoTranslucent(this, backgroundColor);
        rlToolBar.setBackgroundColor(backgroundColor);
        tvOpCancel.setText(getString(R.string.txt_cancel));

        adapter = new PictureAlbumDirectoryAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, Utils.dip2px(this, 0.5f), ContextCompat.getColor(this, R.color.ucrop_line_color)));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initUiEventListener() {
        tvHintEmptyFolder.setOnClickListener(this);
        ImagesObservable.getInstance().add(this);
        tvOpCancel.setOnClickListener(this);
        adapter.setOnItemClickListener(this);
    }

    private String initTitleName() {
        String ret;
        switch (type) {
            case LocalMedia.TYPE_VIDEO:
                ret = getString(R.string.select_video);
                break;
            case LocalMedia.TYPE_PICTURE:
            default:
                ret = getString(R.string.select_photo);
                break;
        }
        return ret;
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        if (folders.size() > 0) { // show list
            tvHintEmptyFolder.setVisibility(View.GONE);
            adapter.bindFolderData(folders);
            notifyDataCheckedStatus(selectMedias);
        } else {
            tvHintEmptyFolder.setVisibility(View.VISIBLE);
            switch (type) { //show empty view
                case LocalMedia.TYPE_VIDEO:
                    tvHintEmptyFolder.setText(getString(R.string.no_video));
                    break;
                case LocalMedia.TYPE_PICTURE:
                default:
                    tvHintEmptyFolder.setText(getString(R.string.no_photo));
                    break;
            }
        }
    }


    /**
     * 设置选中状态
     */
    private void notifyDataCheckedStatus(List<LocalMedia> medias) {
        try {
            // 获取选中图片
            if (medias == null) {
                medias = new ArrayList<>();
            }

            List<LocalMediaFolder> folders = adapter.getFolderData();
            for (LocalMediaFolder folder : folders) {
                // 只重置之前有选中过的文件夹，因为有可能也取消选中的
                if (folder.isChecked()) {
                    folder.setCheckedNum(0);
                    folder.setChecked(false);
                }
            }

            if (medias.size() > 0) {
                for (LocalMediaFolder folder : folders) {
                    int num = 0;// 记录当前相册下有多少张是选中的
                    List<LocalMedia> images = folder.getImages();
                    for (LocalMedia media : images) {
                        String path = media.getPath();
                        for (LocalMedia m : medias) {
                            if (path.equals(m.getPath())) {
                                num++;
                                folder.setChecked(true);
                                folder.setCheckedNum(num);
                            }
                        }
                    }
                }
            }
            adapter.bindFolderData(folders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_empty) {
            startEmptyImageActivity();
        } else if (id == R.id.album_toolbar_rightop) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        }
    }

    /**
     *
     */
    private void startEmptyImageActivity() {
        List<LocalMedia> medias = new ArrayList<>();
        String title;
        switch (type) {
            case LocalMedia.TYPE_VIDEO:
                title = getString(R.string.lately_video);
                break;
            case LocalMedia.TYPE_PICTURE:
            default:
                title = getString(R.string.lately_image);
                break;
        }
        startImageGridActivity(title, medias);
    }

    @Override
    public void onItemClick(String folderName, List<LocalMedia> images) {
        if (images != null && images.size() > 0) {
            startImageGridActivity(folderName, images);
        }
    }


    private void startImageGridActivity(String folderName,
                                        final List<LocalMedia> medias) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent();
        // TODO: 2017/8/18 use java observe
        List<LocalMediaFolder> folders = adapter.getFolderData();
        ImagesObservable.getInstance().saveLocalMedia(medias);
        ImagesObservable.getInstance().saveLocalFolders(folders);

        intent.putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                (Serializable) selectMedias);
        intent.putExtra(Consts.Extra.EXTRA_FUNCTION_CONFIG, config);
        intent.putExtra(Consts.Extra.EXTRA_FOLDER_NAME, folderName);
        intent.putExtra(Consts.Extra.EXTRA_IS_FIRST_STARTED, true);
        intent.setClass(mContext, MediaFolderContentDisplayActivity.class);
        startActivityForResult(intent,
                Consts.AcResultReqCode.REQUEST_SELECT_MEDIA);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//        }
        clearReference();
    }

    protected void clearReference() {
        // 释放静态变量
        PictureConfig.getPictureConfig().resultCallback = null;
        PictureConfig.pictureConfig = null;
        ImagesObservable.getInstance().remove(this);
        ImagesObservable.getInstance().clearLocalFolders();
        ImagesObservable.getInstance().clearLocalMedia();
        ImagesObservable.getInstance().clearSelectedLocalMedia();
    }

    @Override
    public void observerUpFoldersData(List<LocalMediaFolder> folders) {
        this.folders = folders;
        adapter.bindFolderData(folders);
        initData();
    }

    @Override
    public void observerUpSelectsData(List<LocalMedia> selectMedias) {
        folders = ImagesObservable.getInstance().readLocalFolders();
        this.selectMedias = selectMedias;
        if (folders != null && folders.size() > 0)
            adapter.bindFolderData(folders);
        if (selectMedias == null)
            selectMedias = new ArrayList<>();
        notifyDataCheckedStatus(selectMedias);
        if (tvHintEmptyFolder.getVisibility() == View.VISIBLE && adapter.getFolderData().size() > 0)
            tvHintEmptyFolder.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.AcResultReqCode.REQUEST_SELECT_MEDIA) {
            if (resultCode == RESULT_OK) {
                List<LocalMedia> result =
                        (List<LocalMedia>) data.getSerializableExtra(Consts.Extra.EXTRA_SERIALIZABLE_RESULT);
                setResult(RESULT_OK, new Intent().putExtra(Consts.Extra.EXTRA_SERIALIZABLE_RESULT, (Serializable) result));
                finish();
            }
        }
    }


}
