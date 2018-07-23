package hua.news.module_video.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import hua.news.module_video.R;
import hua.news.player.IWindow;
import hua.news.player.VideoView;

/**
 * 视频缓冲时显示的加载中视图
 *
 * @author hua
 * @version 2017/11/28 13:44
 */

public class LoadingContent implements IWindow.IContentView {

    private View mContentView;
    private ImageView mLoadingImage;
    private TextView mLoadingSpeed;
    private VideoView mVideoView;

    private boolean shouldResume = false;
    private EventListener listener;

    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(context).inflate(
                    R.layout.content_loading_controller, null);
            mLoadingImage = mContentView.findViewById(R.id.video_loading_image);
            mLoadingSpeed = mContentView.findViewById(R.id.video_loading_speed);

            listener = new EventListener();
        }
        return mContentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        if (container instanceof VideoView) {
            mVideoView = (VideoView) container;
        } else {
            throw new IllegalArgumentException(LoadingContent.class.getCanonicalName() +
                    "can only be attached to " + VideoView.class.getCanonicalName());
        }
        mVideoView.addEventListener(listener);
        mContentView.setVisibility(View.GONE);
    }

    private class EventListener extends VideoView.EventListener {

        @Override
        protected void onEvent2(VideoView.Event event) {
            switch (event.type) {
                case VideoView.Event.BufferStart:
                    onBufferStart();
                    break;
                case VideoView.Event.BufferCompleted:
                    onBufferCompleted();
                    break;
                default:
                    break;
            }
        }

        private void onBufferStart(){
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                shouldResume = true;
            }

            mContentView.setVisibility(View.VISIBLE);
            NetWorkSpeedHelp.getInstance().startCalculateSpeed(new NetWorkSpeedHelp.OnUpdateListener() {
                @Override
                public void onUpdate(long speed) {
                    mLoadingSpeed.setVisibility(View.VISIBLE);
                    String speedString = speed > 1024 ? speed / 1024 + "kb/s" : speed + "b/s";
                    mLoadingSpeed.setText(speedString);
                }
            });

            if (mOnBufferStartListener != null) {
                mOnBufferStartListener.onBufferStart();
            }
        }

        private void onBufferCompleted(){
            mContentView.setVisibility(View.GONE);
            mLoadingSpeed.setVisibility(View.GONE);
            if (shouldResume) {
                mVideoView.resume();
                shouldResume = false;
            }
        }
    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        mVideoView.removeEventListener(listener);
        mVideoView = null;
    }

    private OnBufferStartListener mOnBufferStartListener;

    public interface OnBufferStartListener{
        /**
         * 开始缓存时调用
         */
        void onBufferStart();
    }

    public void setOnBufferStartListener(OnBufferStartListener listener){
        mOnBufferStartListener = listener;
    }


}
