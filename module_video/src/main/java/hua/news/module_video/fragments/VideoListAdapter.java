package hua.news.module_video.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hua.framework.wrapper.imageload.ImageLoad;

import org.videolan.libvlc.MediaPlayer;

import java.util.HashMap;

import hua.news.module_service.entitys.VideoNewsEntity;
import hua.news.module_video.R;
import hua.news.player.OnPreparedListener;
import hua.news.player.VLCPlayerService;
import hua.news.player.VideoView;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;

/**
 * 视频列表适配器
 *
 * @author hua
 * @version 2017/11/23 14:40
 */

public class VideoListAdapter extends SingleRvAdapter<VideoNewsEntity> {


    private boolean isNoticeNext = false;

    /**
     * 当前正在播放的VideoView
     */
    MyViewHolder mPlayHolder;

    public VideoListAdapter(Context context) {
        this(context.getApplicationContext(), R.layout.item_video_list);
    }

    public VideoListAdapter(Context context, int layoutId) {
        super(context.getApplicationContext(), layoutId);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = super.onCreateViewHolder(parent, viewType);
        VideoView videoView = holder.getView(R.id.vv_video);

        //添加控制器视图
        final BaseMediaController controller = new BaseMediaController();
        videoView.setMediaController(controller);
        controller.hideController();

        //添加缓存加载中视图
        LoadingContent loadingContent = new LoadingContent();
        videoView.addContentView(loadingContent);
        loadingContent.setOnBufferStartListener(new LoadingContent.OnBufferStartListener() {
            @Override
            public void onBufferStart() {
                controller.hideButton();
            }
        });

        return holder;
    }

    @Override
    protected void convert(final MyViewHolder holder, final VideoNewsEntity data, final int position) {
        displayInitUI(holder);
        //初始化基本元素
        holder.setText(R.id.tv_title, data.getTitle());
        ImageView imageView = holder.getView(R.id.iv_avatar);
//        ImageLoad.loadNormalImage(imageView, data.getAuthor().getAvatar_path());
//        holder.setText(R.id.tv_name, data.getAuthor().getNick_name());
        holder.setText(R.id.tv_from, data.getCategory_name());
        holder.setText(R.id.tv_comment_count, data.getComment_count());
        holder.setText(R.id.tv_duration, CommonUtil.formatTime(data.getDuration() * 1000));

        //初始化封面和开始按钮点击事件
        final ImageView cover = holder.getView(R.id.iv_cover);
        final ImageButton imageButton = holder.getView(R.id.ib_video_play2);
        ImageLoad.loadNormalImage(cover, data.getCover_path());
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay(holder);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay(holder);
            }
        });

        //初始化Surface
        final VideoView videoView = holder.getView(R.id.vv_video);
        View surface = videoView.getSurface();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) surface.getLayoutParams();
        params.width = data.getWidth();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        surface.setLayoutParams(params);
    }

    private static void displayInitUI(MyViewHolder holder) {
        holder.setVisibility(R.id.tv_duration, View.VISIBLE);
        holder.setVisibility(R.id.iv_cover, View.VISIBLE);
        holder.setVisibility(R.id.ib_video_play2, View.VISIBLE);
        holder.setVisibility(R.id.tv_video_next_notice, View.INVISIBLE);
    }

    private void startPlay(final MyViewHolder holder) {
        holder.setVisibility(R.id.ib_video_play2, View.GONE);
        holder.setVisibility(R.id.iv_cover, View.GONE);
        holder.setVisibility(R.id.tv_duration, View.GONE);
        final VideoView videoView = holder.getView(R.id.vv_video);
        videoView.prepare(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                doStartPlay(holder);
            }
        });

        if (mPlayHolder != null && mPlayHolder != holder) {
            resetHolder(mPlayHolder);
        }
        mPlayHolder = holder;

        isNoticeNext = false;
    }

    private void doStartPlay(MyViewHolder holder) {
        final VideoView videoView = holder.getView(R.id.vv_video);
        int pos = holder.getAdapterPosition();
        VideoNewsEntity data = (VideoNewsEntity) getDataList().get(pos);
        HashMap<String, String> params = new HashMap<>(1);
        params.put(VLCPlayerService.KEY_ASPECT_RATIO, null);
        videoView.initPlayer(params);

        if (CommonUtil.isWifiConnected(videoView.getContext())) {
            videoView.setVideoUri(Uri.parse(data.get_720p_url()));
        } else {
            videoView.setVideoUri(Uri.parse(data.get_360p_url()));
        }

        VideoEventListener listener = new VideoEventListener(this, holder);
        videoView.addEventListener(listener);
        videoView.setTag(listener);
        videoView.start();
    }

    void resetHolder(MyViewHolder holder) {
        VideoView videoView = holder.getView(R.id.vv_video);
        if (mPlayHolder == holder) {
            VideoEventListener listener = (VideoEventListener) videoView.getTag();
            videoView.removeEventListener(listener);
            videoView.stop();
        }
        displayInitUI(holder);
        BaseMediaController controller = (BaseMediaController) videoView.getMediaController();
        if (controller != null) {
            controller.hideController();
        }
    }

    void playNext(final MyViewHolder holder) {
        final RecyclerView parent = (RecyclerView) holder.itemView.getParent();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        //因为需要知道何时滑动结束以及自定义滑动，因此必须使用自定义LayoutManager
        //
        if (layoutManager instanceof SmoothLinearLayoutManager) {
            final int nextPos = holder.getAdapterPosition() + 1;
            parent.smoothScrollToPosition(nextPos);
            ((SmoothLinearLayoutManager) layoutManager).smoothScroller
                    .setOnSmoothOverListener(new MySmoothScroller.OnSmoothOverListener() {
                        @Override
                        public void onSmoothOver() {
                            //根据当前holder的View，寻找RecyclerView的下一个child，
                            //不为空则使用下一个child的holder进行播放
                            View nextChild = null;
                            for (int i = 0; i < parent.getChildCount(); i++) {
                                View child = parent.getChildAt(i);
                                if (child == holder.itemView) {
                                    nextChild = parent.getChildAt(i + 1);
                                }
                            }
                            if (nextChild != null) {
                                mPlayHolder = null;
                                startPlay((MyViewHolder) parent.getChildViewHolder(nextChild));
                            }
                        }
                    });
        }
    }

    /**
     * 处理视频播放事件
     */
    private static class VideoEventListener extends VideoView.EventListener {

        private static final int TIME_SHOW_NEXT_NOTICE = 3; //s

        private MyViewHolder holder;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        private VideoListAdapter mAdapter;
        private boolean isNotice = false;

        public VideoEventListener(VideoListAdapter adapter, MyViewHolder holder) {
            this.mAdapter = adapter;
            this.holder = holder;
        }

        @Override
        protected void onEvent2(VideoView.Event event) {

            switch (event.type) {
                case VideoView.Event.BufferStart:
                    break;
                case VideoView.Event.BufferCompleted:
                    break;
                case MediaPlayer.Event.TimeChanged:
                    onTimeChanged(holder);
                    break;
                case MediaPlayer.Event.Paused:
                    onPaused(holder);
                    break;
                case MediaPlayer.Event.Stopped:
                    //刚开始播放会有一个Stopped事件？？？？
                    onStoped(holder);
                    break;
                case MediaPlayer.Event.EndReached:
                    onEndReached(holder);
                    break;
                case MediaPlayer.Event.EncounteredError:
                    onError(holder);
                    break;
                default:
                    break;
            }
        }

        private void onTimeChanged(MyViewHolder holder) {
            VideoView videoView = holder.getView(R.id.vv_video);
            long timeToEnd = (videoView.getLength() - videoView.getTime()) / 1000;
            //快播放结束时，提示将要自动播放下一条
            if (timeToEnd < TIME_SHOW_NEXT_NOTICE) {
                showNextNotice(holder);
            }
        }

        private void onPaused(MyViewHolder holder) {

        }

        private void onStoped(MyViewHolder holder) {

        }

        private void onEndReached(MyViewHolder holder) {
            mAdapter.resetHolder(holder);
            mAdapter.playNext(holder);
        }

        private void onError(MyViewHolder holder) {
            mAdapter.resetHolder(holder);
        }

        private void showNextNotice(final MyViewHolder holder) {
            if (isNotice) {
                return;
            }
            final View noticeView = holder.getView(R.id.tv_video_next_notice);
            noticeView.setVisibility(View.VISIBLE);
            //从右边弹出
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) noticeView.getLayoutParams();
            final float animDistance = lp.rightMargin + noticeView.getMeasuredWidth();
            ObjectAnimator.ofFloat(noticeView, "translationX", animDistance, 0)
                    .setDuration(700)
                    .start();
            noticeView.setVisibility(View.INVISIBLE);
            //定时隐藏
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    noticeView.setVisibility(View.INVISIBLE);
                    ObjectAnimator.ofFloat(noticeView, "translationX", 0, animDistance)
                            .setDuration(700)
                            .start();
                }
            }, TIME_SHOW_NEXT_NOTICE * 1000);
            isNotice = true;
        }

    }


}
