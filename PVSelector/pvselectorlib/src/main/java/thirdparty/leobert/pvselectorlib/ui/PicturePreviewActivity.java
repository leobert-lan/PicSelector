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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.MultiUCrop;
import com.yalantis.ucrop.Options;
import com.yalantis.ucrop.dialog.OptAnimationLoader;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.util.ToolbarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.FinishActionConsumer;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.MultiCropCompleteActionConsumer;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.observable.ImagesObservable;
import thirdparty.leobert.pvselectorlib.widget.PreviewViewPager;

import static com.yalantis.ucrop.entity.LocalMedia.asArrayList;

public class PicturePreviewActivity extends PVBaseActivity
        implements View.OnClickListener {
    private ImageButton navBack;
    private TextView tvSelectedCount;
    private TextView tvTitle;
    private TextView tvOpComplete;
    private PreviewViewPager viewPager;
    private int position;

    private View selectorArea;

    private List<LocalMedia> datas = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private TextView check;
    private SimpleFragmentAdapter adapter;
    private Handler mHandler = new Handler();

    private FinishActionConsumer finishActionConsumer
            = new FinishActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        }
    };

    private MultiCropCompleteActionConsumer multiCropCompleteActionConsumer
            = new MultiCropCompleteActionConsumer() {
        @Override
        public void consume(Context context, Intent intent) {
            finish();
            overridePendingTransition(0, R.anim.slide_bottom_out);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);

        registerBroadcastConsumer(finishActionConsumer,
                multiCropCompleteActionConsumer);

        adapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        position = getIntent().getIntExtra(Consts.Extra.EXTRA_POSITION, 0);
        boolean from_bottom_preview = getIntent()
                .getBooleanExtra(Consts.Extra.EXTRA_FROM_BOTTOMBAR_PREVIEW, false);

        if (from_bottom_preview)
            // 底部预览按钮过来
            datas = getIntent()
                    .getParcelableArrayListExtra(Consts.Extra.EXTRA_PREVIEW_LIST);
        else
            datas = ImagesObservable.getInstance().readLocalMedias();

        selectImages = getIntent()
                .getParcelableArrayListExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST);

        initUiInstance();
        initViewPageAdapterData();
        initUiEventListener();
    }

    @Override
    protected void initUiInstance() {
        RelativeLayout rlToolBar = (RelativeLayout) findViewById(R.id.rl_title);
        navBack = (ImageButton) findViewById(R.id.left_back);
        viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        selectorArea = findViewById(R.id.toolbar_area_selector);
        RelativeLayout bottomBar =
                (RelativeLayout) findViewById(R.id.preview_bottom_bar);
        check = (TextView) findViewById(R.id.check);
        tvOpComplete = (TextView) findViewById(R.id.bottombar_tv_select_complete);
        tvSelectedCount = (TextView) findViewById(R.id.item_tv_selected_count);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        rlToolBar.setBackgroundColor(backgroundColor);
        ToolbarUtil.setColorNoTranslucent(this, backgroundColor);
        tvOpComplete.setTextColor(completeTxtColor);
        bottomBar.setBackgroundColor(previewBottomBgColor);


        if (displayCandidateNo)
            tvSelectedCount.setBackgroundResource(R.drawable.message_oval_blue);
    }

    @Override
    protected void initUiEventListener() {
        navBack.setOnClickListener(this);
        tvOpComplete.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(position + 1 + "/" + datas.size());
                if (displayCandidateNo) {
                    LocalMedia media = datas.get(position);
                    check.setText(String.valueOf(media.getGridItemInfoHolder().getCandidateNo()));
                    rebuildSelectPosition(media);
                }
                freshCheckStateByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        selectorArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasBeenSelected = check.isSelected();

                if (selectImages.size() >= maxSelectNum && !hasBeenSelected) {
                    /*try to add selected but over threshold*/
                    Toast.makeText(PicturePreviewActivity.this,
                            getString(R.string.message_max_num, maxSelectNum),
                            Toast.LENGTH_LONG).show();
                    check.setSelected(false);
                    return;
                }

                LocalMedia localMedia = datas.get(viewPager.getCurrentItem());

                if (hasBeenSelected)  /*update to unSelected*/
                    change2UnSelected(localMedia);
                else  /*update to selected*/
                    change2Selected(localMedia);

                onSelectNumChanged(true);
            }
        });
    }

    private void change2UnSelected(LocalMedia localMedia) {
        check.setSelected(false);

        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(localMedia.getPath())) {
                selectImages.remove(media);
                rebuildCandidateNo();
                rebuildSelectPosition(media);
                break;
            }
        }
    }

    private void change2Selected(LocalMedia localMedia) {
        check.setSelected(true);

        //handle UI animation
        Animation animation = OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in);
        check.startAnimation(animation);

        selectImages.add(localMedia);

        final int candidateNo = selectImages.size();
        localMedia.getGridItemInfoHolder().setCandidateNo(candidateNo);
        if (displayCandidateNo) {
            check.setText(String.valueOf(candidateNo));
        }
    }

    private void initViewPageAdapterData() {
        tvTitle.setText(position + 1 + "/" + datas.size());
        check.setBackgroundResource(cb_drawable);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        onSelectNumChanged(false);
        freshCheckStateByPosition(position);


        if (displayCandidateNo) {
            LocalMedia media = datas.get(position);
            check.setText(media.getGridItemInfoHolder().getCandidateNo());
            rebuildSelectPosition(media);
        }
    }

    /**
     * 选择按钮更新
     */
    private void rebuildSelectPosition(LocalMedia imageBean) {
        if (displayCandidateNo) {
            check.setText("");
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(imageBean.getPath())) {
                    final int candidateNo = media.getGridItemInfoHolder().getCandidateNo();
                    imageBean.getGridItemInfoHolder().setCandidateNo(candidateNo);
                    check.setText(String.valueOf(candidateNo));
                }
            }
        }
    }

    /**
     * rebuild candidate No. order
     */
    private void rebuildCandidateNo() {
        for (int index = 0, len = selectImages.size(); index < len; index++) {
            LocalMedia media = selectImages.get(index);
            media.getGridItemInfoHolder().setCandidateNo(index + 1);
        }
    }

    /**
     * fresh state by data
     *
     * @param position index of the data
     */
    public void freshCheckStateByPosition(int position) {
        check.setSelected(isSelected(datas.get(position)));
    }

    /**
     * check whether the given picture has been selected
     *
     * @param image the given picture to be checked
     * @return true if has been selected,false otherwise
     */
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新图片选择数量
     */
    public void onSelectNumChanged(boolean isRefresh) {
        Animation animation;
        boolean enable = selectImages.size() != 0;
        if (enable) {
            tvOpComplete.setEnabled(true);
            tvOpComplete.setAlpha(1.0f);
            animation = AnimationUtils.loadAnimation(mContext, R.anim.modal_in);
            tvSelectedCount.startAnimation(animation);
            tvSelectedCount.setVisibility(View.VISIBLE);
            tvSelectedCount.setText(String.valueOf(selectImages.size()));
            tvOpComplete.setText("已完成");
        } else {
            tvOpComplete.setEnabled(false);
            tvOpComplete.setAlpha(0.5f);
            tvSelectedCount.setVisibility(View.INVISIBLE);
            tvOpComplete.setText("请选择");
        }

        if (isRefresh) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent()
                            .setAction(Consts.BcActions.ACTION_REFRESH_DATA)
                            .putParcelableArrayListExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                                    asArrayList(selectImages)));
                }
            }, 100);
        }
    }


    private class SimpleFragmentAdapter extends FragmentPagerAdapter {

        SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final String path = datas.get(position).getPath();
            return PicturePreviewFragment.getInstance(path, selectImages);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.left_back) {
            finish();
        } else if (id == R.id.bottombar_tv_select_complete) {
            if (type == LocalMedia.TYPE_PICTURE
                    && selectMode == FunctionConfig.SELECT_MODE_MULTIPLE
                    && enableCrop)
                startMultiCrop(selectImages);
            else
                onResult(selectImages);
        }
    }

    public void onResult(List<LocalMedia> images) {
        // 因为这里是单一实例的结果集，重新用变量接收一下在返回，不然会产生结果集被单一实例清空的问题
        List<LocalMedia> result = new ArrayList<>();
        for (LocalMedia media : images) {
            result.add(media);
        }
        sendBroadcast(new Intent()
                .setAction(Consts.BcActions.ACTION_IMAGE_CROPPED)
                .putParcelableArrayListExtra(Consts.Extra.EXTRA_ARRAYLIST_LOCALMEDIA,
                        asArrayList(result)));
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }

    /**
     * @param medias to be cropped
     */
    protected void startMultiCrop(List<LocalMedia> medias) {
        if (medias != null && medias.size() > 0) {
            LocalMedia media = medias.get(0);
            String path = media.getPath();
            // 去裁剪
            MultiUCrop multiUCrop = MultiUCrop.of(Uri.parse(path),
                    Uri.fromFile(new File(getCacheDir(),
                            System.currentTimeMillis() + ".jpg")));
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
            options.cropMode(cropMode);
            multiUCrop.withOptions(options);
            multiUCrop.start(PicturePreviewActivity.this);
        }

    }

}
