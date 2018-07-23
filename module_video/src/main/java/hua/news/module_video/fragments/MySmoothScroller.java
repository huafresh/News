package hua.news.module_video.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义LinearLayoutManager的smoothScrollToPosition方法
 *
 * @author hua
 * @version 2017/11/27 10:10
 */

public class MySmoothScroller extends LinearSmoothScroller {

    /**
     * 找到目标后移动到屏幕中央的时间
     */
    public static final int SHOW_TIME = 1000;
    private static final int SMOOTH_OVER = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SMOOTH_OVER:
                    if (mOnSmoothOverListener != null) {
                        mOnSmoothOverListener.onSmoothOver();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public MySmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int calculateTimeForDeceleration(int dx) {
        return SHOW_TIME;
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        super.onTargetFound(targetView, state, action);
        final RecyclerView parent = (RecyclerView) targetView.getParent();
        int offset = parent.getHeight() / 2 - targetView.getHeight() / 2;
        action.setDy(action.getDy() + offset);

        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mHandler.hasMessages(SMOOTH_OVER)) {
                        mHandler.removeMessages(SMOOTH_OVER);
                    }
                    parent.removeOnScrollListener(this);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        mHandler.sendEmptyMessageDelayed(SMOOTH_OVER, SHOW_TIME);
    }

    private OnSmoothOverListener mOnSmoothOverListener;

    public interface OnSmoothOverListener {
        /**
         * 滑动到屏幕中央时调用。
         * 实现思路是：延时{@link #SHOW_TIME}毫秒发送一个滑动结束的消息。
         * 因为动画的执行时间就是{@link #SHOW_TIME}。否则如果中途停止滑动，
         * 而消息还没发出，那么就移除消息。此时此方法不会被调用。
         */
        void onSmoothOver();
    }

    public void setOnSmoothOverListener(OnSmoothOverListener listener) {
        mOnSmoothOverListener = listener;
    }


}
