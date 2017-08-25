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
import thirdparty.leobert.pvselectorlib.Logger;
import thirdparty.leobert.pvselectorlib.R;

public class VideoDisplayActivity extends PVBaseActivity
        implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private String videoPath = "";
    private VideoView mVideoView;
    private ImageView ivStartPlay;
    private int mPositionWhenPaused = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_video_play);

        videoPath = getIntent().getStringExtra(Consts.Extra.EXTRA_PREVIEW_VIDEO_PATH);

        ImageView navBack = (ImageView) findViewById(R.id.left_back);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        ivStartPlay = (ImageView) findViewById(R.id.iv_play);
        MediaController mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setMediaController(mMediaController);
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.start();
                ivStartPlay.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.i(getClass().getSimpleName(), "[video play on error]\r"
                + "what:" + getErrorType(what)
                + "\rextra:" + getErrorExtra(extra));

        //not handled, onCompletion will be called
        return false;
    }

    private String getErrorType(int what) {
        String ret;
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                ret = "MEDIA_ERROR_SERVER_DIED";
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
            default:
                ret = "MEDIA_ERROR_UNKNOWN";
                break;
        }
        return ret;
    }

    private String getErrorExtra(int extra) {
        String ret;
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                ret = "MEDIA_ERROR_IO";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                ret = "MEDIA_ERROR_UNSUPPORTED";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                ret = "MEDIA_ERROR_TIMED_OUT";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
            default:
                ret = "MEDIA_ERROR_MALFORMED";
                break;
        }
        return ret;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ivStartPlay.setVisibility(View.VISIBLE);
    }
}
