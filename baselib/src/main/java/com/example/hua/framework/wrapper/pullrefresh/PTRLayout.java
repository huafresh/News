package com.example.hua.framework.wrapper.pullrefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.hua.framework.R;
import com.example.hua.framework.wrapper.pullrefresh.behaviors.HeaderBehavior;
import com.example.hua.framework.wrapper.pullrefresh.behaviors.ScrollableBehavior;

/**
 * 下拉刷新/上拉加载控件。
 * <p>
 * 上下拉刷新可以大致分为三个部分：下拉刷新头部布局、可滑动内容布局、上拉加载底部布局。为应对多样化的
 * 样式需求，以上布局全部由外部提供。本类管理三个部分的视图时，其本质可以通俗
 * 的理解为：随着用户手指的滑动，视图有怎么样的行为，因此模仿CoordinatorLayout的Behavior类实
 * 现了{@link Behavior}，通过自定义各种不同的{@link Behavior}，可以实现各种各样的刷新需求。
 * 在behaviors包下实现了一些常用的Behavior。使用这些Behavior或者稍作自定义基本
 * 上能满足大部分的需求了。
 * <p>
 * 使用方法：
 * 上面提到了本控件主要是有三部分视图组成的，因此使用上，需要通过各种途径在PTRLayout发挥作用前告诉
 * PTRLayout三个视图是什么以及他们各自使用的Behavior是什么。可以在xml里直接布局，也可以代码里new出来，
 * 或者混合起来都可以。
 * <p>
 * 1、xml布局使用：
 * xml中作为父布局使用。然后添加三个字View，分别作为头部布局、内容布局、底部布局，再分别设置他们的
 * layout_type属性为header、scrollable、footer。如果不设置，那么PTRLayout将会按顺序依次将子view当成
 * 头部布局、可滑动布局、底部布局。
 * <p>
 * 2、代码中使用：
 * 代码中使用时，需要先使能上下拉刷新功能。
 * 调用系列set方法把视图设置进来即可
 * {@link #setHeaderLayout(View)} 设置头部布局
 * {@link #setScrollableView(View)} 设置可滑动内容布局
 * {@link #setFooterLayout(View)} 设置底部布局
 *
 * @author hua
 * @date 2017/8/14
 */

public class PTRLayout extends ViewGroup {

    /**
     * 刷新状态超时时间
     */
    public static final int REFRESHING_TIME_OUT = 10000;
    /**
     * 手指释放时回滚的时间
     */
    public static final int ROLL_BACK_DURATION = 300;
    /**
     * 最小阻尼系数
     */
    public static final float mMinDamp = 0.4f;
    /**
     * 最大阻尼系数
     */
    public static final float mMaxDamp = 0.05f;
    private static final boolean isKITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    private static final int SPEED_UNITS = 1000; //s
    private static final int MIN_SPEED = 1000; // 1000px/s
    private static final int MAX_CHILD_COUNT = 3;
    private Context mContext;
    /**
     * 三大块布局以及他们的Behavior
     */
    private View mHeaderView;
    private View mFooterView;
    private View mScrollableView;
    /**
     * 上下拉使能标志位
     */
    private boolean mIsPullDownEnable = false;
    private boolean mIsPullUpEnable = false;
    /**
     * 记录手指位置
     */
    private float mLastTouchX;
    private float mLastTouchY;
    /**
     * 内容视图是否正在被拖拽
     */
    private boolean mIsBeingDragged;
    private VelocityTracker mVelocityTracker;
    /**
     * 是否正在快速滑动
     */
    private boolean isFling;
    /**
     * 当前生效的手指的id
     */
    private int mActivePointerId;
    private List<OnRefreshStateChangedListener> mListeners;


    public PTRLayout(Context context) {
        this(context, null);
    }

    public PTRLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PTRLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        resolveChildViews();
    }

    private void resolveChildViews() {
        int count = getChildCount();
        if (count > MAX_CHILD_COUNT) {
            throw new IllegalArgumentException("PTRLayout can only host "
                    + MAX_CHILD_COUNT + " child");
        }
        int[] unConfirm = new int[2];

        //初步解析
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int layoutType = lp.getLayoutType();
            if (layoutType == LayoutParams.LAYOUT_TYPE_HEADER) {
                mHeaderView = child;
                lp.setBehavior(new HeaderBehavior());
            } else if (layoutType == LayoutParams.LAYOUT_TYPE_SCROLLABLE) {
                mScrollableView = child;
                lp.setBehavior(new ScrollableBehavior());
            } else if (layoutType == LayoutParams.LAYOUT_TYPE_FOOTER) {
                mFooterView = child;
            } else {
                unConfirm[i] = -1;
            }
        }

        //解析未确认的Child，根据位置判断
        for (int i = 0; i < unConfirm.length; i++) {
            if (unConfirm[i] != -1) {
                continue;
            }
            View child = getChildAt(i);
            if (mHeaderView == null) {
                mHeaderView = child;
            }
            if (mScrollableView == null) {
                mScrollableView = child;
            }
            if (mFooterView == null) {
                mFooterView = child;
            }
        }

        enablePullDownOrUp();
    }

    private void enablePullDownOrUp() {
        if (mHeaderView != null) {
            mIsPullDownEnable = true;
        }
        if (mFooterView != null) {
            mIsPullUpEnable = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getMeasuredWidth() > width) {
                    width = child.getMeasuredWidth();
                }
            }
        } else {
            width = widthSize;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getMeasuredHeight() > height) {
                    height = child.getMeasuredHeight();
                }
            }
        } else {
            height = heightSize;
        }

        setMeasuredDimension(width, height);
    }

    /* *
     * 根据当前偏移的距离，计算阻尼系数
     */
    private float getDampByOffset(PTRLayout refreshLayout, View view, int deltaY) {
//        int maxOffset = getScreenHeight() / 2;
//        int curOffset = getTopAndBottomOffset();
//        if (isReachTop(view) && deltaY > 0 &&
//                refreshLayout.getHeaderRefreshState() != IRefreshLayout.State.REFRESHING) {
//            float fraction = (mMaxDamp - mMinDamp) / maxOffset;
//            return mMinDamp + fraction * curOffset;
//        } else if (isReachBottom(view) && deltaY < 0) {
//            float fraction = (mFooterMaxDamp - mFooterMinDamp) / maxOffset;
//            return mFooterMinDamp - fraction * curOffset;
//        }
        return 1.0f;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        initVelocityTracker();
        mVelocityTracker.addMovement(ev);

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                isFling = false;
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //有非主要手指按下
                int index = ev.getActionIndex();
                mLastTouchX = ev.getX(index);
                mLastTouchY = ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //有非主要手指抬起时，此时需要把滑动的焦点还给还在屏幕中的手指
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                float x = ev.getX(pointerIndex);
                float y = ev.getY(pointerIndex);
                int deltaX = Math.round(x - mLastTouchX);
                int deltaY = Math.round(y - mLastTouchY);

                if (!mIsBeingDragged) {
                    mIsBeingDragged = isShouldBeingDragged(deltaY);
                }

                if (mIsBeingDragged) {
                    int count = getChildCount();
                    List<View> views = new ArrayList<>();
                    View header = null;
                    View scrollable = null;
                    View footer = null;
                    for (int i = 0; i < count; i++) {
                        View child = getChildAt(i);
                        LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        if (lp.getLayoutType() == LayoutParams.LAYOUT_TYPE_HEADER) {
                            header = child;
                        } else if (lp.getLayoutType() == LayoutParams.LAYOUT_TYPE_SCROLLABLE) {
                            scrollable = child;
                        } else if (lp.getLayoutType() == LayoutParams.LAYOUT_TYPE_FOOTER) {
                            footer = child;
                        }
                    }

                    if (scrollable != null) {
                        views.add(scrollable);
                    }
                    if (header != null) {
                        views.add(header);
                    }
                    if (footer != null) {
                        views.add(footer);
                    }

                    for (View child : views) {
                        boolean stop = scrollChild(child, deltaX, deltaY);
                        if (stop) {
                            break;
                        }
                    }
                }

                mLastTouchX = ev.getX(pointerIndex);
                mLastTouchY = ev.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:

                if (mIsBeingDragged) {
                    final float speed = getVelocityY();
                    if (speed > MIN_SPEED) {
                        notifyFling(speed);
                    }
                }

                notifyFingerUp();

                RecyclerVelocityTracker();

                mActivePointerId = -1;
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren(l, t, r, b);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            if (isKITKAT) {
                return new LayoutParams((LayoutParams) lp);
            } else {
                return new LayoutParams(lp);
            }
        }
        return new LayoutParams(lp);
    }

    void layoutChildren(int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int childLeft;
            int childTop;

            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                            lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.RIGHT:
                    childLeft = parentRight - width - lp.rightMargin;
                    break;
                case Gravity.LEFT:
                default:
                    childLeft = parentLeft + lp.leftMargin;
                    break;
            }

            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    childTop = parentTop + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                            lp.topMargin - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = parentBottom - height - lp.bottomMargin;
                    break;
                default:
                    childTop = parentTop + lp.topMargin;
                    break;
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height);

            Behavior behavior = lp.getBehavior();
            if (behavior != null) {
                behavior.onLayout(this, child);
            }
        }
    }

    private void initVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastTouchX = ev.getX(newPointerIndex);
            mLastTouchY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private boolean isShouldBeingDragged(int deltaY) {
        boolean isBeingDragged;

        if (deltaY > 0) { //向下滑
            isBeingDragged = mScrollableView != null &&
                    !mScrollableView.canScrollVertically(-1);
        } else { //向上滑
            isBeingDragged = mScrollableView != null &&
                    !mScrollableView.canScrollVertically(1);
        }
        return isBeingDragged;
    }

    private boolean scrollChild(View child, int dx, int dy) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        Behavior behavior = lp.getBehavior();
        return behavior != null && behavior.onScroll(this, child, dx, dy);
    }

    private float getVelocityY() {
        if (mVelocityTracker != null) {
            mVelocityTracker.computeCurrentVelocity(SPEED_UNITS); //速度单位
            return mVelocityTracker.getYVelocity();
        }
        return 0;
    }

    private void notifyFling(float speed) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Behavior behavior = getBehavior(child);
            if (behavior != null) {
                behavior.onFing(this, child, speed);
            }
        }
    }

    private void notifyFingerUp() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Behavior behavior = getBehavior(child);
            if (behavior != null) {
                behavior.onFingerUp(this, child);
            }
        }
    }

    private void RecyclerVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private Behavior getBehavior(View child) {
        if (child != null) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            return lp.getBehavior();
        }
        return null;
    }

    /**
     * 使能下拉刷新
     *
     * @param enable 是否使能
     */
    public void setPullDownRefreshEnable(boolean enable) {
        mIsPullDownEnable = enable;
    }

    /**
     * 使能上拉加载
     *
     * @param enable 是否使能
     */
    public void setPullUpLoadEnable(boolean enable) {
        mIsPullUpEnable = enable;
    }

    public void setHeaderLayout(View header) {
        addHeaderLayout(header, null);
    }

    private void addHeaderLayout(View header, LayoutParams params) {
        if (header != null) {
            if (mHeaderView != null) {
                removeView(mHeaderView);
            }
            mHeaderView = header;
            if (params == null) {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setLayoutType(LayoutParams.LAYOUT_TYPE_HEADER);
                lp.setBehavior(new HeaderBehavior());
                addView(header, lp);
            } else {
                addView(header, params);
            }
            enablePullDownOrUp();
        }
    }

    /**
     * 设置拉动刷新控件的头部布局
     *
     * @param header 头部布局
     * @param params 布局参数
     */
    public void setHeaderLayout(View header, LayoutParams params) {
        addHeaderLayout(header, params);
    }

    public void setFooterLayout(View footer) {
        addFooterLayout(footer, null);
    }

    private void addFooterLayout(View footer, LayoutParams params) {
        if (footer != null) {
            if (mFooterView != null) {
                removeView(mFooterView);
            }
            mFooterView = footer;
            if (params == null) {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setLayoutType(LayoutParams.LAYOUT_TYPE_FOOTER);
                //lp.setBehavior(new HeaderBehavior());
                addView(footer, 0);
            } else {
                addView(footer, 0, params);
            }
        }
    }

    /**
     * 设置拉动刷新控件的底部布局
     *
     * @param footer 底部布局
     * @param params 布局参数
     */
    public void setFooterLayout(View footer, LayoutParams params) {
        addFooterLayout(footer, params);
    }

    /**
     * 设置拉动刷新控件的可滑动内容布局
     *
     * @param scrollableView 可滑动内容布局
     * @param params         布局参数
     */
    public void setScrollableView(View scrollableView, LayoutParams params) {
        addScrollableLayout(scrollableView, params);
    }

    private void addScrollableLayout(View scrollableView, LayoutParams params) {
        if (scrollableView != null) {
            if (mScrollableView != null) {
                removeView(mScrollableView);
            }
            mScrollableView = scrollableView;
            if (params == null) {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setLayoutType(LayoutParams.LAYOUT_TYPE_SCROLLABLE);
                lp.setBehavior(new ScrollableBehavior());
                addView(scrollableView, lp);
            } else {
                addView(scrollableView, params);
            }
        }
    }

    /**
     * 添加头部 / 底部状态改变的监听。注意添加时必须要在相应布局已经set的情况下
     *
     * @param listener 监听
     */
    public void addStateChangeListener(OnRefreshStateChangedListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (listener != null) {
            mListeners.add(listener);
        }
    }

    /**
     * 移除头部 / 底部状态改变的监听。注意添加时必须要在相应布局已经set的情况下
     *
     * @param listener 监听
     */
    public void removeStateChangeListener(OnRefreshStateChangedListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    /**
     * 通知状态改变
     *
     * @param child    状态改变的布局
     * @param oldState 旧状态
     * @param newState 新状态
     */
    public void notifyStateChanged(View child, PTRLayout.State oldState, PTRLayout.State newState) {
        if (mListeners != null) {
            for (OnRefreshStateChangedListener listener : mListeners) {
                listener.onStateChanged(child, oldState, newState);
            }
        }
    }

    /**
     * 设置刷新结束。
     *
     * @param type 布局类型
     */
    public void setRefreshComplete(@LayoutParams.LayoutType int type) {
        Bundle params = new Bundle();
        params.putInt(HeaderBehavior.KEY_EVENT_TYPE, HeaderBehavior.EVENT_TYPE_REFRESH_COMPLETE);
        sendBehaviorEvent(LayoutParams.LAYOUT_TYPE_NONE, type, params);
    }

    /**
     * 给指定布局类型的Behavior发送通知事件。
     * 指定布局类型的Behavior的{@link Behavior#onReceiveEvent}方法会被回调。
     *
     * @param fromType 从哪来
     * @param toType   到哪去
     * @param params   事件参数
     */
    public void sendBehaviorEvent(@LayoutParams.LayoutType int fromType,
                                  @LayoutParams.LayoutType int toType,
                                  Bundle params) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.getLayoutType() == toType) {
                Behavior behavior = lp.getBehavior();
                if (behavior != null) {
                    behavior.onReceiveEvent(this, child, fromType, params);
                }
            }
        }
    }

    /**
     * 给所有其他布局类型的Behavior广播事件。
     * 被通知的布局的Behavior的{@link Behavior#onReceiveEvent}方法会被回调。
     *
     * @param fromType 从哪来
     * @param params   事件参数
     */
    public void broadcastBehaviorEvent(@LayoutParams.LayoutType int fromType,
                                       Bundle params) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.getLayoutType() != fromType) {
                Behavior behavior = lp.getBehavior();
                if (behavior != null) {
                    behavior.onReceiveEvent(this, child, fromType, params);
                }
            }
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getScrollableView() {
        return mScrollableView;
    }

    public void setScrollableView(View scrollableView) {
        addScrollableLayout(scrollableView, null);
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 头部 / 底部布局所处的刷新状态
     */
    public enum State {
        /**
         * 初始状态
         */
        NONE,
        /**
         * 拉动刷新状态
         */
        PULLING_TO_REFRESH,
        /**
         * 释放刷新状态
         */
        RELEASE_TO_REFRESH,
        /**
         * 正在刷新状态
         */
        REFRESHING,
        /**
         * 刷新完成状态
         */
        COMPLETE
    }

    public interface OnRefreshStateChangedListener {
        /**
         * 刷新状态改变时调用
         *
         * @param child    改变状态的视图
         * @param oldState 旧状态
         * @param newState 新状态
         */
        void onStateChanged(View child, PTRLayout.State oldState, PTRLayout.State newState);
    }

    public static class UpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private View child;

        public UpdateListener(View child) {
            this.child = child;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PTRLayout.LayoutParams lp = (PTRLayout.LayoutParams) child.getLayoutParams();
            lp.topMargin = (int) animation.getAnimatedValue();
            child.setLayoutParams(lp);
        }
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        public static final int LAYOUT_TYPE_NONE = -1;
        public static final int LAYOUT_TYPE_HEADER = 0;
        public static final int LAYOUT_TYPE_SCROLLABLE = 1;
        public static final int LAYOUT_TYPE_FOOTER = 2;

        /**
         * 布局的类型。
         * 可取值：
         * {@link #LAYOUT_TYPE_NONE} 无。当普通视图处理
         * {@link #LAYOUT_TYPE_HEADER} 头部布局
         * {@link #LAYOUT_TYPE_SCROLLABLE} 可滑动布局
         * {@link #LAYOUT_TYPE_FOOTER} 底部布局
         */
        private int mLayoutType = -1;

        /**
         * {@link Behavior}
         */
        private Behavior mBehavior;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.PTRLayout_Layout);
            mLayoutType = a.getInt(R.styleable.PTRLayout_Layout_layout_type, LAYOUT_TYPE_NONE);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public LayoutParams(LayoutParams source) {
            super(source);
            mLayoutType = source.getLayoutType();
            mBehavior = source.getBehavior();
        }

        /**
         * 设置布局类型
         *
         * @return 布局类型
         * @see #mLayoutType
         */
        public int getLayoutType() {
            return mLayoutType;
        }

        /**
         * 设置布局类型
         *
         * @param type 布局类型
         * @see #mLayoutType
         */
        public void setLayoutType(@LayoutType int type) {
            this.mLayoutType = type;
        }

        /**
         * 获取Behavior
         *
         * @return Behavior
         * @see Behavior
         */
        public Behavior getBehavior() {
            return mBehavior;
        }

        /**
         * 设置Behavior
         *
         * @param behavior Behavior
         * @see Behavior
         */
        public void setBehavior(Behavior behavior) {
            this.mBehavior = behavior;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @IntDef({LAYOUT_TYPE_NONE, LAYOUT_TYPE_HEADER,
                LAYOUT_TYPE_SCROLLABLE, LAYOUT_TYPE_FOOTER})
        public @interface LayoutType {
        }
    }

    /**
     * 模仿CoordinatorLayout#Behavior类实现。
     * 子View需要实现此类决定自身的布局和滑动行为。
     *
     * @param <V> child类型
     */
    public abstract static class Behavior<V extends View> {

        /**
         * 当{@link PTRLayout}对child进行布局时调用，首先
         * 会按照FrameLayout的布局规则对child进行布局。
         * child的Behavior可以复写此方法对自身的布局进行调整。
         * <p>
         * 注意android中每次重新布局都会把View的onMeasure和onLayout方法走一遍
         * 因此此方法会被多次调用。
         *
         * @param parent 父布局
         * @param child  Behavior所属实体视图
         */
        public void onLayout(PTRLayout parent, V child) {

        }

        /**
         * PTRLayout进入拖拽状态后，会拦截所有滑动事件（在该次事件序列内），
         * 然后不断回调此方法，子类应该复写此方法以实现相应的滑动逻辑。
         *
         * @param parent 父布局
         * @param child  Behavior所属实体视图
         * @param dx     手指水平方向滑动的距离
         * @param dy     手指垂直方向滑动的距离
         * @return 是否禁止其他child滑动。如果禁止，那么剩余未回调的其他child不会回调此方法。
         * 此方法回调的优先级是：内容布局 > 头部布局 > 底部布局
         * @see {@link PTRLayout}
         */
        public boolean onScroll(PTRLayout parent, V child, int dx, int dy) {
            return false;
        }

        /**
         * 手指抬起时调用。
         * 可以复写实现手指抬起时视图滑动回滚
         *
         * @param parent 父布局
         * @param child  Behavior所属实体视图
         */
        public void onFingerUp(PTRLayout parent, V child) {

        }

        /**
         * 快速滑动时调用
         *
         * @param parent 父布局
         * @param child  Behavior所属实体视图
         * @param speed  快速滑动速度
         */
        public void onFing(PTRLayout parent, V child, float speed) {

        }

        /**
         * 接收到其他Behavior的事件时调用
         *
         * @param parent   父布局
         * @param child    Behavior所属实体视图
         * @param fromType 其他Behavior的布局类型
         * @param params   事件参数
         * @return 是否拦截。返回true，那么对于广播类型的事件将不会继续传递
         */
        public boolean onReceiveEvent(PTRLayout parent, V child,
                                      @LayoutParams.LayoutType int fromType, Bundle params) {
            return false;
        }
    }

}
