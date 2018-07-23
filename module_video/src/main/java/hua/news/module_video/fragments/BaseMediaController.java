package hua.news.module_video.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hua.framework.utils.CommonUtil;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import hua.news.module_video.R;
import hua.news.player.IWindow;
import hua.news.player.VideoView;

/**
 * 此类实现了一个通用的视频播放控制器。子类可以随意控制控制器里的元素。
 * 也可以通过复写{@link #onCustomContent(View)}方法来增加新的元素。
 *
 * @author hua
 * @version 2017/11/3 13:59
 */

public class BaseMediaController implements View.OnClickListener, IWindow.IContentView {


    protected Context mContext;
    protected ImageButton ibVideoPlay;
    protected ImageButton ibVideoPause;
    protected View viewBottomBac;
    protected ImageButton ibVoiceOpen;
    protected ImageButton ibVoiceClose;
    protected TextView tvCurrentTime;
    protected ImageButton ibFullScreen;
    protected TextView tvTotalTime;
    protected View mContentView;
    protected SeekBar mSeekBar;
    protected View mControllerBac;
    protected View mControllerPlayPause;
    protected View mBottomContainer;

    /**
     * 控制器显示后自动隐藏时间间隔
     */
    protected static final int HIDE_DELAY = 5; //s


    /**
     * 控制视图依附的容器
     */
    protected VideoView mVideoView;

    /**
     * 控制器是否显示
     */
    private boolean isShow = true;

    /**
     * 是否处于静音模式
     */
    private static boolean isVoiceClose = false;

    /**
     * SeekBar是否被拖拽
     */
    private boolean isSeekBarTrackingTouch = false;

    /**
     * 是否连接到视频播放器
     */
    private boolean isAttach = false;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    protected EventListener mEventListener;

    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(context).inflate(
                    R.layout.video_media_controller, null);
            findViews();
            initViews();
            setListeners();
            mContentView = onCustomContent(mContentView);
            mContext = context;
        }
        return mContentView;
    }

    /**
     * 复写此方法往控制器界面添加自定义的控件。
     *
     * @param view 默认的控制器视图，是一个FrameLayout容器。
     * @return 自定义后的视图。
     */
    protected View onCustomContent(View view) {
        return view;
    }

    private void findViews() {
        ibVideoPlay = mContentView.findViewById(R.id.ib_video_play);
        ibVideoPause = mContentView.findViewById(R.id.ib_video_pause);
        viewBottomBac = mContentView.findViewById(R.id.view_bottom_bac);
        ibVoiceOpen = mContentView.findViewById(R.id.ib_voice_open);
        ibVoiceClose = mContentView.findViewById(R.id.ib_voice_close);
        tvCurrentTime = mContentView.findViewById(R.id.tv_current_time);
        ibFullScreen = mContentView.findViewById(R.id.ib_full_screen);
        tvTotalTime = mContentView.findViewById(R.id.tv_total_time);
        mSeekBar = mContentView.findViewById(R.id.sb_seek_bar);
        mControllerBac = mContentView.findViewById(R.id.view_controller_bac);
        mControllerPlayPause = mContentView.findViewById(R.id.controller_play_pause);
        mBottomContainer = mContentView.findViewById(R.id.controller_bottom_container);
    }

    private void initViews() {
        mEventListener = new EventListener();
        mSeekBar.setMax(100);
    }

    private void setListeners() {
        ibVideoPlay.setOnClickListener(this);
        ibVideoPause.setOnClickListener(this);
        ibVoiceOpen.setOnClickListener(this);
        ibVoiceClose.setOnClickListener(this);
        ibFullScreen.setOnClickListener(this);
        mControllerBac.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }


    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarTrackingTouch = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarTrackingTouch = false;
            mVideoView.setTime((long) (seekBar.getProgress() * 1.0f / 100 * mVideoView.getLength()));
        }
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        mVideoView = (VideoView) container;
        mVideoView.addEventListener(mEventListener);

        int curState = mVideoView.getCurState();
        if (curState == Media.State.NothingSpecial) {
            mBottomContainer.setVisibility(View.GONE);
        } else {
            mBottomContainer.setVisibility(View.VISIBLE);
        }

        refreshController();

        isAttach = true;
    }

    /**
     * 复写此类以便获取到播放器的通知事件（如暂停，开始，缓冲等等。。。）
     *
     * @param event 事件
     */
    protected void onEventReceive(VideoView.Event event) {

    }

    private class EventListener extends VideoView.EventListener {

        @Override
        protected void onEvent2(VideoView.Event event) {
            onEventReceive(event);
            switch (event.type) {
                case MediaPlayer.Event.MediaChanged:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Buffering:
                    refreshController();
                    break;
                case MediaPlayer.Event.TimeChanged:
                    updateCurTime();
                    updateSeekBarPos();
                    break;
                case MediaPlayer.Event.EncounteredError:
                    if (mVideoView != null) {
                        mVideoView.stop();
                        mVideoView = null;
                        Toast.makeText(mContentView.getContext(), "播放出错", Toast.LENGTH_SHORT).show();
                    }
                default:
                    break;
            }
        }
    }

    private void refreshController() {
        if (mVideoView.isBuffering()) {
            hideButton();
        } else {
            if (mVideoView.isPlaying()) {
                setPauseImage();
            } else {
                setPlayImage();
            }
        }

        updateVoiceState();
        updateCurTime();
        updateMaxTime();
        updateSeekBarPos();
    }

    @Override
    public void onClick(View v) {
        if (mVideoView == null) {
            return;
        }

        int id = v.getId();
        if (id == R.id.ib_video_play) {
            if (mVideoView.getCurState() == Media.State.Paused) {
                mVideoView.resume();
            } else {
                mVideoView.start();
            }
        } else if (id == R.id.ib_video_pause) {
            mVideoView.pause();
        } else if (id == R.id.ib_voice_open) {
            setVoiceClose();
        } else if (id == R.id.ib_voice_close) {
            setVoiceOpen();
        } else if (id == R.id.ib_full_screen) {
            onFullScreen();
        } else if (id == R.id.view_controller_bac) {
            onclickViewBac();
        }
    }

    /**
     * 复写此方法实现全屏播放
     */
    protected void onFullScreen() {

    }

    private void onclickViewBac() {
        if (isShow) {
            hideController();
        } else {
            showController();
        }
    }

    private void setVoiceOpen() {
        ibVoiceOpen.setVisibility(View.VISIBLE);
        ibVoiceClose.setVisibility(View.GONE);
    }

    private void setVoiceClose() {
        ibVoiceOpen.setVisibility(View.GONE);
        ibVoiceClose.setVisibility(View.VISIBLE);
    }

    private void setPauseImage() {
        ibVideoPlay.setVisibility(View.GONE);
        ibVideoPause.setVisibility(View.VISIBLE);
    }

    private void setPlayImage() {
        ibVideoPlay.setVisibility(View.VISIBLE);
        ibVideoPause.setVisibility(View.GONE);
    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        mVideoView.removeEventListener(mEventListener);
        mVideoView = null;
        isAttach = false;
    }

    /**
     * 显示控制器
     */
    public void showController() {
        if (!isShow) {
            mBottomContainer.setVisibility(View.VISIBLE);
            if (!mVideoView.isBuffering()) {
                mControllerPlayPause.setVisibility(View.VISIBLE);
            }

            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideController();
                }
            }, HIDE_DELAY * 1000);

            isShow = true;
        }
    }

    /**
     * 隐藏控制器
     */
    public void hideController() {
        if (isShow) {
            mBottomContainer.setVisibility(View.GONE);
            mControllerPlayPause.setVisibility(View.GONE);
            isShow = false;
        }
    }

    /**
     * 只隐藏按钮
     */
    public void hideButton() {
        if (mControllerPlayPause.getVisibility() != View.GONE) {
            mControllerPlayPause.setVisibility(View.GONE);
        }
    }

    private void updateVoiceState() {
        if (isVoiceClose) {
            setVoiceClose();
        } else {
            setVoiceOpen();
        }
    }

    private void updateCurTime() {
        final long curTime = mVideoView.getTime();
        tvCurrentTime.setText(CommonUtil.formatTime(curTime));
    }

    private void updateMaxTime() {
        final long length = mVideoView.getLength();
        if (length != -1) {
            tvTotalTime.setText(CommonUtil.formatTime(length));
        }
    }

    private void updateSeekBarPos() {
        if (!isSeekBarTrackingTouch) {
            final long curTime = mVideoView.getTime();
            if (curTime != -1) {
                float percent = curTime * 1.0f / mVideoView.getLength();
                mSeekBar.setProgress(Math.round(percent * 100));
            }
        }
    }

    /**
     * 是否与视频播放器连接
     *
     * @return 是否连接
     */
    public boolean isAttach() {
        return isAttach;
    }

    /**
     * 断开与视频播放器的连接
     */
    public void detach() {
        if (mVideoView != null) {
            mVideoView.removeMediaController(this);
        }
    }
}
