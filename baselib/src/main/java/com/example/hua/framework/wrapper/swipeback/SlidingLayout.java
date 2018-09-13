package com.example.hua.framework.wrapper.swipeback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hua
 * @version 2018/9/10 17:31
 */

public class SlidingLayout extends FrameLayout {

    /**
     * Edge flag indicating that the left edge should be affected.
     */
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;

    /**
     * Edge flag indicating that the top edge should be affected.
     */
    public static final int EDGE_TOP = ViewDragHelper.EDGE_TOP;

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

    /**
     * Edge flag set indicating all edges should be affected.
     */
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_TOP | EDGE_BOTTOM;

    private static final float DEFAULT_OFFSET_THRESHOLD = 0.5f;
    private static final int DEFAULT_SPEED_THRESHOLD = 1000;
    private static final int DEFAULT_SHADOW_WIDTH = 20;
    private static final int DEFAULT_SCRIM_COLOR = 0xff000000;
    private static final int DEFAULT_SCRIM_BASE_ALPHA = 0x99;
    private static final int FULL_ALPHA = 255;

    private SparseArray<SlidingThreshold> thresholds = new SparseArray<>(4);
    private int enableEdges = 0;

    private Context context;
    private ViewDragHelper viewDragHelper;
    private ViewDragHelper.Callback dragHelperCallBack;
    private ISlidingPage page;
    private Drawable shadow;
    private int shadowWidth;
    private int scrimColor = DEFAULT_SCRIM_COLOR;
    private int scrimBaseAlpha = DEFAULT_SCRIM_BASE_ALPHA;

    private View contentView;
    private int contentLeft;
    private int contentTop;

    private Rect mTmpRect = new Rect();

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        dragHelperCallBack = new DragHelperCallBack();
        viewDragHelper = ViewDragHelper.create(this, dragHelperCallBack);

        initDefaultThreshold();
        initDefaultShadow();

        setEnableEdge(EDGE_LEFT);
    }

    private void initDefaultShadow() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gradientDrawable.setColors(new int[]{0x00ffffff, 0xffffffff});

        shadow = gradientDrawable;
        shadowWidth = dp2px(context, DEFAULT_SHADOW_WIDTH);
    }

    private static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    private void initDefaultThreshold() {
        for (int i = 0; i < 4; i++) {
            int edge = 1 << i;
            thresholds.put(edge, new SlidingThreshold(DEFAULT_OFFSET_THRESHOLD, DEFAULT_SPEED_THRESHOLD));
        }
    }

    public SlidingLayout wrap(ISlidingPage page) {
        this.page = page;

        removeAllViews();

        View originView = page.getView();
        if (originView != null) {
            ViewGroup parent = (ViewGroup) originView.getParent();
            if (parent != null) {
                parent.removeView(originView);
                ViewGroup.LayoutParams originParams = originView.getLayoutParams();
                addView(originView);
                parent.addView(this, originParams);
            } else {
                addView(originView);
            }
        }

        contentView = originView;

        return this;
    }

    /**
     * 使能可滑动的边界。目前仅支持Left
     *
     * @param edge see {@link #EDGE_LEFT} and so on
     */
    SlidingLayout setEnableEdge(int edge) {
        this.enableEdges = enableEdges | (edge & EDGE_ALL) & EDGE_LEFT;
        return this;
    }

    /**
     * set offset threshold for specific edge
     *
     * @param offset from 0 to 1
     * @param edge   see {@link #EDGE_LEFT} and so on
     */
    public SlidingLayout setOffsetThreshold(float offset, int edge) {
        for (int i = 0; i < 4; i++) {
            int tmpEdge = 1 << i;
            if (checkEdge(edge, tmpEdge)) {
                SlidingThreshold threshold = thresholds.get(tmpEdge);
                if (threshold == null) {
                    threshold = new SlidingThreshold();
                }
                threshold.offset = offset;
                thresholds.put(tmpEdge, threshold);
            }
        }
        return this;
    }

    /**
     * set  fling speed for specific edge
     *
     * @param speed fling speed, in px
     * @param edge  see {@link #EDGE_LEFT} and so on
     */
    public SlidingLayout setFlingThreshold(float speed, int edge) {
        for (int i = 0; i < 4; i++) {
            int tmpEdge = 1 << i;
            if (checkEdge(edge, tmpEdge)) {
                SlidingThreshold threshold = thresholds.get(tmpEdge);
                if (threshold == null) {
                    threshold = new SlidingThreshold();
                }
                threshold.speed = speed;
                thresholds.put(tmpEdge, threshold);
            }
        }
        return this;
    }

    /**
     * @param drawable null for disable shadow
     */
    public void setShadow(@Nullable Drawable drawable) {
        if (drawable != null) {
            shadowWidth = drawable.getIntrinsicWidth();
            if (shadowWidth == -1) {
                shadowWidth = dp2px(context, DEFAULT_SHADOW_WIDTH);
            }
        }
        shadow = drawable;
    }


//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            if (child == contentView) {
//                contentView.layout(
//                        contentLeft,
//                        contentTop,
//                        contentLeft + contentView.getMeasuredWidth(),
//                        contentTop + contentView.getMeasuredHeight());
//            }
//        }
//    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);

        drawShadow(canvas);
        drawScrim(canvas);

        return ret;
    }

    private void drawScrim(Canvas canvas) {
        if (contentView != null) {
            float scrollPercent = getViewScrollPercentX(contentView);
            int newAlpha = (int) (scrimBaseAlpha - (scrimBaseAlpha * scrollPercent));
            int color = Color.argb(newAlpha,
                    Color.red(scrimColor),
                    Color.green(scrimColor),
                    Color.blue(scrimColor));
            canvas.save();
            canvas.clipRect(0, 0, contentView.getLeft(), getHeight());
            canvas.drawColor(color);
            canvas.restore();
        }
    }

    private void drawShadow(Canvas canvas) {
        if (shadow != null && contentView != null) {
            shadow.setBounds(contentView.getLeft() - shadowWidth,
                    contentView.getTop(),
                    contentView.getLeft(),
                    contentView.getBottom());
            float scrollPercent = getViewScrollPercentX(contentView);
            int newAlpha = (int) (FULL_ALPHA - (FULL_ALPHA * scrollPercent));
            shadow.setAlpha(newAlpha);
            shadow.draw(canvas);
        }
    }

    private static boolean checkEdge(int edge, int checkedEdge) {
        return (edge & checkedEdge) != 0;
    }

    private List<SlidingListener> mSlidingListeners;

    void notifyListenerOffset(float offset) {
        if (mSlidingListeners != null) {
            for (SlidingListener listener : mSlidingListeners) {
                listener.onSliding(page, offset);
            }
        }
    }

    void notifyListenerFinish() {
        if (mSlidingListeners != null) {
            for (SlidingListener listener : mSlidingListeners) {
                listener.onFinishSlide();
            }
        }
    }

    /**
     * 添加监听。
     * 请注意在适当的时候调用{@link #removeSlidingListener}移除监听
     *
     * @param listener 要添加的监听
     * @see SlidingListener
     */
    public void addSlidingListener(SlidingListener listener) {
        if (mSlidingListeners == null) {
            mSlidingListeners = new ArrayList<>();
        }
        mSlidingListeners.add(listener);
    }

    /**
     * 移除监听
     *
     * @param listener 被移除的监听
     */
    public void removeSlidingListener(SlidingListener listener) {
        if (mSlidingListeners != null) {
            mSlidingListeners.remove(listener);
        }
    }

    /**
     * 清除所有监听
     */
    public void clearSlidingListener() {
        if (mSlidingListeners != null) {
            mSlidingListeners.clear();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean process = checkEdge(enableEdges, EDGE_LEFT) |
                checkEdge(enableEdges, EDGE_RIGHT) |
                checkEdge(enableEdges, EDGE_TOP) |
                checkEdge(enableEdges, EDGE_BOTTOM);
        if (process) {
            viewDragHelper.processTouchEvent(event);
        }
        return process;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    float getViewScrollPercentX(View child) {
        return (child.getLeft() - getLeft()) * 1.0f / getWidth();
    }

    float getViewScrollPercentY(View child) {
        return (child.getTop() - getTop()) * 1.0f / getHeight();
    }

    private class DragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int resultLeft = child.getLeft();

            if (left < 0 && checkEdge(enableEdges, EDGE_RIGHT)) {
                resultLeft = left;
            } else if (left > 0 && checkEdge(enableEdges, EDGE_LEFT)) {
                resultLeft = left;
            }

            return resultLeft;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int resultTop = child.getTop();

            if (top < 0 && checkEdge(enableEdges, EDGE_BOTTOM)) {
                resultTop = top;
            } else if (top > 0 && checkEdge(enableEdges, EDGE_TOP)) {
                resultTop = top;
            }

            return resultTop;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return contentView == child;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            notifyListenerOffset(getViewScrollPercentX(changedView));
            contentLeft = left;
            contentTop = top;
            invalidate();
        }

        @Override
        public void onViewReleased(@NonNull View child, float xvel, float yvel) {
            int toLeft = 0;
            int toTop = 0;
            boolean finish = false;

            float offsetX = getViewScrollPercentX(child);
            if (offsetX > 0) {
                if (offsetX > thresholds.get(EDGE_LEFT).offset ||
                        Math.abs(xvel) > thresholds.get(EDGE_LEFT).speed && xvel > 0) {
                    toLeft = getWidth() + shadowWidth;
                    finish = true;
                } else {
                    toLeft = 0;
                }
            } else if (offsetX < 0) {
                offsetX = Math.abs(offsetX);
                if (offsetX > thresholds.get(EDGE_RIGHT).offset ||
                        Math.abs(xvel) > thresholds.get(EDGE_RIGHT).speed && xvel < 0) {
                    toLeft = 0;
                } else {
                    toLeft = getWidth();
                }
            }

            float offsetY = getViewScrollPercentY(child);
            if (offsetY > 0) {
                if (offsetY > thresholds.get(EDGE_TOP).offset ||
                        Math.abs(yvel) > thresholds.get(EDGE_TOP).speed && yvel > 0) {
                    toTop = getHeight();
                } else {
                    toTop = 0;
                }
            } else if (offsetY < 0) {
                offsetY = Math.abs(offsetY);
                if (offsetY > thresholds.get(EDGE_BOTTOM).offset ||
                        Math.abs(yvel) > thresholds.get(EDGE_BOTTOM).speed && yvel < 0) {
                    toTop = getHeight();
                } else {
                    toTop = 0;
                }
            }

            viewDragHelper.settleCapturedViewAt(toLeft, toTop);

            invalidate();

            if (finish) {
                notifyListenerFinish();
            }
        }
    }

}
