package com.example.hua.framework.wrapper.pullrefresh.behaviors;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import java.util.Timer;
import java.util.TimerTask;

import com.example.hua.framework.wrapper.pullrefresh.PTRLayout;
import com.example.hua.framework.wrapper.pullrefresh.PtrUtil;

/**
 * 下拉刷新头部布局Behavior。
 *
 * @author hua
 * @version 2017/11/17 14:37
 * @see PTRLayout.Behavior
 */

public class HeaderBehavior extends PTRLayout.Behavior<View> {

    /**
     * 是否是重新布局
     */
    private boolean isReLayout = false;

    /**
     * 当前的拉动刷新状态
     */
    private PTRLayout.State mCurState = PTRLayout.State.NONE;
    private ValueAnimator mHeaderAnimator;

    public static final String KEY_EVENT_TYPE = "event_type";

    /**
     * 刷新完成事件。
     */
    public static final int EVENT_TYPE_REFRESH_COMPLETE = 0;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onLayout(PTRLayout parent, View child) {
        if (!isReLayout) {
            //初始时，隐藏自身
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childH = child.getMeasuredHeight();
            if (childH > 0 && lp.topMargin != -childH) {
                lp.topMargin = -childH;
                child.setLayoutParams(lp);
            }
        }
    }

    @Override
    public boolean onScroll(PTRLayout parent, View child, int dx, int dy) {
        //改变child的布局参数以达到滑动的目的
        PtrUtil.offsetTopMargin(child, dy);
        isReLayout = true;
        updateRefreshStateByHeight(parent, child);
        return false;
    }

    private void updateRefreshStateByHeight(PTRLayout parent, View child) {
        //处于刷新状态时，不改变状态
        if (mCurState == PTRLayout.State.REFRESHING) {
            return;
        }

        int topMargin = PtrUtil.getTopMargin(child);
        PTRLayout.State newState = null;
        if (topMargin < 0) {
            newState = PTRLayout.State.PULLING_TO_REFRESH;
        } else { //完全显示
            newState = PTRLayout.State.RELEASE_TO_REFRESH;
        }

        if (newState != mCurState) {
            mCurState = newState;
            parent.notifyStateChanged(child, mCurState, newState);
        }
    }

    @Override
    public void onFingerUp(PTRLayout parent, View child) {
        switch (mCurState) {
            case NONE:
                break;
            case PULLING_TO_REFRESH:
                rollBackHeader(child, -child.getHeight(), parent);
                break;
            case RELEASE_TO_REFRESH:
            case REFRESHING:
                rollBackHeader(child, 0, parent);
                break;
            case COMPLETE:
                break;
            default:
                break;
        }
    }


    private void rollBackHeader(final View child, final int destMargin, final PTRLayout parent) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                doRollBackHeader(child, destMargin, parent);
            }
        });
    }

    private void doRollBackHeader(final View child, int destMargin, final PTRLayout parent) {
        initHeaderAnimator();

        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int curMargin = lp.topMargin;
        mHeaderAnimator.setIntValues(curMargin, destMargin);
        mHeaderAnimator.addUpdateListener(new PTRLayout.UpdateListener(child));
        mHeaderAnimator.addListener(new AnimatorListener(parent, child));
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mHeaderAnimator.start();
            }
        });

        //通知内容布局回滚
        Bundle params = new Bundle();
        params.putInt(ScrollableBehavior.KEY_EVENT_TYPE, ScrollableBehavior.EVENT_TYPE_ROLL_BACK);
        params.putInt(ScrollableBehavior.KEY_PARAMS_DEST_TOP_MARGIN, destMargin + child.getHeight());
        params.putBoolean(ScrollableBehavior.KEY_PARAMS_IS_ANIM, true);
        params.putLong(ScrollableBehavior.KEY_PARAMS_DURATION, PTRLayout.ROLL_BACK_DURATION);
        parent.sendBehaviorEvent(PTRLayout.LayoutParams.LAYOUT_TYPE_HEADER,
                PTRLayout.LayoutParams.LAYOUT_TYPE_SCROLLABLE, params);
    }

    private class AnimatorListener extends AnimatorListenerAdapter {
        private PTRLayout parent;
        private View child;

        /**
         * 用于设置刷新超时
         */
        private Timer timer;

        private AnimatorListener(PTRLayout parent, View child) {
            this.parent = parent;
            this.child = child;
            timer = new Timer();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mCurState == PTRLayout.State.RELEASE_TO_REFRESH) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //刷新超时进入刷新完成状态
                        if (mCurState != PTRLayout.State.COMPLETE) {
                            PTRLayout.State newState = PTRLayout.State.COMPLETE;
                            parent.notifyStateChanged(child, mCurState, newState);
                            mCurState = newState;
                        }
                    }
                }, PTRLayout.REFRESHING_TIME_OUT);
                PTRLayout.State newState = PTRLayout.State.REFRESHING;
                mCurState = newState;
                parent.notifyStateChanged(child, mCurState, newState);
            }
        }
    }

    private void initHeaderAnimator() {
        if (mHeaderAnimator == null) {
            mHeaderAnimator = new ValueAnimator();
            mHeaderAnimator.setDuration(PTRLayout.ROLL_BACK_DURATION);
            mHeaderAnimator.setInterpolator(new LinearInterpolator());
        }
        if (mHeaderAnimator.isRunning()) {
            mHeaderAnimator.cancel();
        }
    }

    @Override
    public boolean onReceiveEvent(PTRLayout parent, View child, int fromType, Bundle params) {
        if (params != null) {
            int type = params.getInt(HeaderBehavior.KEY_EVENT_TYPE);
            switch (type) {
                case EVENT_TYPE_REFRESH_COMPLETE:
                    rollBackHeader(child, -child.getHeight(), parent);
                    PTRLayout.State newState = PTRLayout.State.COMPLETE;
                    if (newState != mCurState) {
                        mCurState = newState;
                        parent.notifyStateChanged(child, mCurState, newState);
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onReceiveEvent(parent, child, fromType, params);
    }


}
