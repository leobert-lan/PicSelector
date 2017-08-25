package thirdparty.leobert.pvselectorlib.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.model.PictureConfig;
import thirdparty.leobert.pvselectorlib.widget.PreviewViewPager;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/** if preview needed after selected done, use this activity can fast
 * implement requirement*/
public class PictureExternalPreviewActivity extends FragmentActivity {
    private ImageButton navBack;
    private TextView tvTitle;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList<>();
    private int position = 0;
    private SimpleFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_external_preview);

        initUiInstance();
        initUiEventListener();

        position = getIntent().getIntExtra(Consts.Extra.EXTRA_POSITION, 0);
        images = (List<LocalMedia>) getIntent()
                .getSerializableExtra(Consts.Extra.EXTRA_PREVIEW_SELECT_LIST);

        initViewPageAdapterData();
    }

    private void initUiInstance() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        navBack = (ImageButton) findViewById(R.id.left_back);
        viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
    }

    private void initUiEventListener() {
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViewPageAdapterData() {
        tvTitle.setText(position + 1 + "/" + images.size());
        adapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(position + 1 + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class SimpleFragmentAdapter extends FragmentPagerAdapter {

        SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PicturePreviewFragment.getInstance(images.get(position).getPath(),
                    images);
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureConfig.getPictureConfig().resultCallback = null;
        PictureConfig.pictureConfig = null;
    }
}
