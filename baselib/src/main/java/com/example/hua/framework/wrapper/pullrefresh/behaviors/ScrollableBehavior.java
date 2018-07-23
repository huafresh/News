package com.example.hua.framework.wrapper.pullrefresh.behaviors;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.hua.framework.wrapper.pullrefresh.PTRLayout;

/**
 * 可滑动布局Behavior。一般不需要自定义
 *
 * @author hua
 * @version 2017/11/17 15:39
 */

public class ScrollableBehavior extends PTRLayout.Behavior<View> {

    public static final String KEY_EVENT_TYPE = "event_type";

    /**
     * 回滚事件。
     * 可选参数：
     * dest_top_margin：目标TopMargin值，int类型
     * is_anim：是否使用动画，boolean类型
     * duration：动画持续时间，long类型
     */
    public static final int EVENT_TYPE_ROLL_BACK = 0;
    public static final String KEY_PARAMS_DEST_TOP_MARGIN = "dest_top_margin";
    public static final String KEY_PARAMS_IS_ANIM = "is_anim";
    public static final String KEY_PARAMS_DURATION = "duration";

    private ValueAnimator mAnimator;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private boolean isReachTopOrBottom(View child, int deltaY) {
        boolean isBeingDragged;

        if (deltaY > 0) { //向下滑
            isBeingDragged = child != null &&
                    !child.canScrollVertically(-1);
        } else { //向上滑
            isBeingDragged = child != null &&
                    !child.canScrollVertically(1);
        }
        return isBeingDragged;
    }

    @Override
    public boolean onScroll(PTRLayout parent, View child, int dx, int dy) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

        if (isReachTopOrBottom(child, dy) || lp.topMargin > 0) {
            //改变布局参数使视图整体滑动
            lp.topMargin += dy;
            child.setLayoutParams(lp);
            child.requestLayout();
            return false;
        } else {
            //滑动内容
            child.scrollBy(-dx, -dy);
            //禁止其余布局响应手指滑动
            return true;
        }
    }

    @Override
    public boolean onReceiveEvent(PTRLayout parent, final View child, int fromType,
                                  final Bundle params) {
        if (params != null) {
            int type = params.getInt(KEY_EVENT_TYPE);
            switch (type) {
                case EVENT_TYPE_ROLL_BACK:
                    rollBack(child, params);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    private void rollBack(final View child, final Bundle params) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                doRollBack(child, params);
            }
        });
    }

    private void doRollBack(View child, Bundle params) {
        int destMargin = params.getInt(KEY_PARAMS_DEST_TOP_MARGIN);
        long duration = params.getLong(KEY_PARAMS_DURATION);
        boolean isAnim = params.getBoolean(KEY_PARAMS_IS_ANIM);
        PTRLayout.LayoutParams lp = (PTRLayout.LayoutParams) child.getLayoutParams();
        if (!isAnim) {
            lp.topMargin = destMargin;
            child.setLayoutParams(lp);
        } else {
            initAnimator();
            mAnimator.setIntValues(lp.topMargin, destMargin);
            mAnimator.setDuration(duration);
            mAnimator.addUpdateListener(new PTRLayout.UpdateListener(child));
            mAnimator.start();
        }
    }

    private void initAnimator() {
        if (mAnimator == null) {
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new LinearInterpolator());
        }
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
