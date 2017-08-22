package thirdparty.leobert.pvselectorlib.ui;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import thirdparty.leobert.pvselectorlib.Consts;
import thirdparty.leobert.pvselectorlib.R;

public class VideoDisplayActivity extends PVBaseActivity
        implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private String videoPath = "";
    private ImageView left_back;
    private MediaController mMediaController;
    private VideoView mVideoView;
    private ImageView iv_play;
    private int mPositionWhenPaused = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_video_play);

        videoPath = getIntent().getStringExtra(Consts.Extra.EXTRA_PREVIEW_VIDEO_PATH);

        left_back = (ImageView) findViewById(R.id.left_back);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setMediaController(mMediaController);
        left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.start();
                iv_play.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            translucentStatusBarCompact_19();
        else
            translucentStatusBarCompact_14();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void translucentStatusBarCompact_19() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void translucentStatusBarCompact_14() {
        // use full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void onStart() {
        // Play Video
        new Thread(new Runnable() {
            @Override
            public void run() {
                mVideoView.setVideoPath(videoPath);
                mVideoView.start();
            }
        }).start();
        super.onStart();
    }

    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();

        super.onPause();
    }

    public void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }

        super.onResume();
    }

    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        iv_play.setVisibility(View.VISIBLE);
    }
}
