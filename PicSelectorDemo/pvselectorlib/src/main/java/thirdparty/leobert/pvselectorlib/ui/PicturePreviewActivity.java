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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.MultiUCrop;
import com.yalantis.ucrop.Options;
import com.yalantis.ucrop.dialog.OptAnimationLoader;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.util.ToolbarUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.FinishActionConsumer;
import thirdparty.leobert.pvselectorlib.broadcast.consumers.MultiCropCompleteActionConsumer;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;
import thirdparty.leobert.pvselectorlib.observable.ImagesObservable;
import thirdparty.leobert.pvselectorlib.widget.PreviewViewPager;

public class PicturePreviewActivity extends PVBaseActivity
        implements View.OnClickListener {
    private ImageButton navBack;
    private TextView tv_img_num, tv_title, tv_ok;
    private RelativeLayout select_bar_layout;
    private PreviewViewPager viewPager;
    private int position;
    private RelativeLayout rl_title;
    private LinearLayout ll_check;
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

        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        navBack = (ImageButton) findViewById(R.id.left_back);
        viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        select_bar_layout = (RelativeLayout) findViewById(R.id.select_bar_layout);
        check = (TextView) findViewById(R.id.check);
        navBack.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.bottombar_tv_select_complete);
        tv_img_num = (TextView) findViewById(R.id.bottombar_tv_select_count);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_ok.setOnClickListener(this);

        rl_title.setBackgroundColor(backgroundColor);
        ToolbarUtil.setColorNoTranslucent(this, backgroundColor);
        tv_ok.setTextColor(completeTxtColor);
        select_bar_layout.setBackgroundColor(previewBottomBgColor);

        position = getIntent().getIntExtra(Consts.Extra.EXTRA_POSITION, 0);
        boolean is_bottom_preview = getIntent()
                .getBooleanExtra(Consts.Extra.EXTRA_FROM_BOTTOMBAR_PREVIEW, false);
        if (is_bottom_preview) {
            // 底部预览按钮过来
            datas = (List<LocalMedia>) getIntent().getSerializableExtra(Consts.Extra.EXTRA_PREVIEW_LIST);
        } else {
            datas = ImagesObservable.getInstance().readLocalMedias();
        }

        if (displayCandidateNo) {
            tv_img_num.setBackgroundResource(R.drawable.message_oval_blue);
        }

        selectImages = (List<LocalMedia>) getIntent()
                .getSerializableExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST);

        initViewPageAdapterData();
        ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 刷新图片列表中图片状态
                boolean hasBeenSelected = check.isSelected();

                if (selectImages.size() >= maxSelectNum && !hasBeenSelected) { /*try to add selected but onLimit*/
                    Toast.makeText(PicturePreviewActivity.this,
                            getString(R.string.message_max_num, maxSelectNum),
                            Toast.LENGTH_LONG).show();
                    check.setSelected(false);
                    return;
                }

                LocalMedia localMedia = datas.get(viewPager.getCurrentItem());

                if (hasBeenSelected) { /*update to unSelected*/
                    check.setSelected(false);

                    for (LocalMedia media : selectImages) {
                        if (media.getPath().equals(localMedia.getPath())) {
                            selectImages.remove(media);
                            rebuildCandidateNo();
                            rebuildSelectPosition(media);
                            break;
                        }
                    }

                } else { /*update to selected*/
//                    hasBeenSelected = true;
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

                onSelectNumChanged(true);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(position + 1 + "/" + datas.size());
                if (displayCandidateNo) {
                    LocalMedia media = datas.get(position);
                    check.setText(String.valueOf(media.getGridItemInfoHolder().getCandidateNo()));
                    rebuildSelectPosition(media);
                }
                onImageChecked(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initViewPageAdapterData() {
        tv_title.setText(position + 1 + "/" + datas.size());
        adapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        check.setBackgroundResource(cb_drawable);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        onSelectNumChanged(false);
        onImageChecked(position);
        if (displayCandidateNo) {
            tv_img_num.setBackgroundResource(R.drawable.message_oval_blue);
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
     * 更新选择的顺序
     */
    private void rebuildCandidateNo() {
        for (int index = 0, len = selectImages.size(); index < len; index++) {
            LocalMedia media = selectImages.get(index);
            media.getGridItemInfoHolder().setCandidateNo(index + 1);
        }
    }

    /**
     * 判断当前图片是否选中
     *
     * @param position
     */
    public void onImageChecked(int position) {
        check.setSelected(isSelected(datas.get(position)));
    }

    /**
     * 当前图片是否选中
     *
     * @param image
     * @return
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
        Animation animation = null;
        boolean enable = selectImages.size() != 0;
        if (enable) {
            tv_ok.setEnabled(true);
            tv_ok.setAlpha(1.0f);
            animation = AnimationUtils.loadAnimation(mContext, R.anim.modal_in);
            tv_img_num.startAnimation(animation);
            tv_img_num.setVisibility(View.VISIBLE);
            tv_img_num.setText(selectImages.size() + "");
            tv_ok.setText("已完成");
        } else {
            tv_ok.setEnabled(false);
            tv_ok.setAlpha(0.5f);
            tv_img_num.setVisibility(View.INVISIBLE);
            tv_ok.setText("请选择");
        }

        if (isRefresh) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(new Intent()
                            .setAction("app.action.refresh.data")
                            .putExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST,
                                    (Serializable) selectImages));
                }
            }, 100);
        }
    }


    public class SimpleFragmentAdapter extends FragmentPagerAdapter {

        public SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PicturePreviewFragment fragment = PicturePreviewFragment.getInstance(datas.get(position).getPath(), selectImages);
            return fragment;
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
            if (selectMode == FunctionConfig.SELECT_MODE_MULTIPLE
                    && enableCrop && type == LocalMedia.TYPE_PICTURE) {
                // 是图片和选择压缩并且是多张，调用批量压缩
                startMultiCrop(selectImages);
            } else {
                onResult(selectImages);
            }
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
                .putExtra(Consts.Extra.EXTRA_SERIALIZABLE_RESULT, (Serializable) result));
        finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
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
            options.background_color(backgroundColor);
            options.cropMode(cropMode);
            multiUCrop.withOptions(options);
            multiUCrop.start(PicturePreviewActivity.this);
        }

    }

}
