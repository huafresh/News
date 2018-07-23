package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.R;
import com.example.hua.framework.interfaces.IIndicator;

/**
 * Created by hua on 2017/10/7.
 * 带指示器和ViewPager的容器。
 * 本类可以作为控件直接在xml布局中使用，可当成是简单的ViewPager。
 * <p>
 * 指示器的样式以及位置等需要继承{@link IIndicator}来实现。
 * 设置指示器有两种方式：
 * 1、xml里使用indicator属性指定使用的{@link IIndicator}类，需设置全路径
 * 2、调用{@link #setIndicator(IIndicator)}设置{@link IIndicator}实现类
 * <p>
 * 注意：此类虽然是ViewGroup，但是不要在xml里编写子View，因为会被remove。
 */
@Deprecated
public class ViewPagerIndicator extends ViewGroup {

    private static final String TAG = "ViewPagerIndicator";
    private ViewPager mViewPager;
    private String mIndicatorClassName;
    private IIndicator mIndicator;
    private Context mContext;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        obtainAttrs(context, attrs);

        addViewPager(context);

        initIndicator(context);
    }

    private void initIndicator(Context context) {
        try {
            Class<?> indicatorClass = Class.forName(mIndicatorClassName);
            mIndicator = (IIndicator) indicatorClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("obtain indicator instance failed！！！");
        }
    }

    private void addViewPager(Context context) {
        mViewPager = new ViewPager(context);
        addView(mViewPager, new LayoutPrams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void obtainAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mIndicatorClassName = array.getString(R.styleable.ViewPagerIndicator_indicator);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.equals(mViewPager)) {
                //xml里添加子View无效
                removeView(child);
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //测量自己
        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) { //wrap_content
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.equals(mViewPager)) {
                    width = child.getMeasuredWidth();
                }
            }
        } else {
            width = widthSize;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) { //wrap_content
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.equals(mViewPager)) {
                    height = child.getMeasuredHeight();
                }
            }
        } else {
            height = heightSize;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //找到指示器布局，同时决定它和ViewPager的布局位置
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.equals(mViewPager)) {
                LayoutPrams params = (LayoutPrams) child.getLayoutParams();
                final int gravity = params.mGravity;
                final int location = params.mLocation;
                //理论上通过gravity可将indicator放在上下左右位置，但是这里只支持常用的上和下
                final int left;
                final int right;
                final int top;
                final int bottom;
                final int pagerLeft;
                final int pagerRight;
                final int pagerTop;
                final int pagerBottom;
                if ((gravity & Gravity.TOP) != 0) { //指示器在顶部
                    if ((gravity & Gravity.CENTER_HORIZONTAL) != 0) {
                        left = getWidth() / 2 - child.getWidth() / 2 + params.leftMargin;
                        right = left - params.rightMargin;
                    } else {
                        left = params.leftMargin;
                        right = left - params.rightMargin;
                    }
                    top = params.topMargin;
                    bottom = top - params.bottomMargin;

                    //确定ViewPager位置
                    if (location == LayoutPrams.LOCATION_INNER) {
                        pagerTop = 0;
                        pagerBottom = getHeight();
                    } else {
                        pagerTop = child.getHeight();
                        pagerBottom = getHeight();
                    }
                    pagerLeft = 0;
                    pagerRight = getWidth();

                } else { //指示器在底部
                    if ((gravity & Gravity.CENTER_HORIZONTAL) != 0) {
                        left = getWidth() / 2 - child.getWidth() / 2 + params.leftMargin;
                        right = left - params.rightMargin;
                    } else {
                        left = params.leftMargin;
                        right = left - params.rightMargin;
                    }
                    bottom = getHeight() - params.bottomMargin;
                    top = bottom - child.getHeight() + params.topMargin;

                    //确定ViewPager位置
                    if (location == LayoutPrams.LOCATION_INNER) {
                        pagerTop = 0;
                        pagerBottom = getHeight();
                    } else {
                        pagerTop = 0;
                        pagerBottom = getHeight() - child.getHeight();
                    }
                    pagerLeft = 0;
                    pagerRight = getWidth();
                }

                child.layout(left, top, right, bottom);
                mViewPager.layout(pagerLeft, pagerTop, pagerRight, pagerBottom);
            }
        }
    }

    /**
     * 设置指示器
     *
     * @param indicator 指示器实现类
     */
    public void setIndicator(IIndicator indicator) {
        mIndicator = indicator;
    }

    /**
     * 设置页面适配器
     *
     * @param adapter 页面适配器
     */
    public void setViewPagerAdapter(PagerAdapter adapter) {
        if (adapter == null) {
            return;
        }

        mViewPager.setAdapter(adapter);
        if (mIndicator != null) {
            View mIndicatorView = mIndicator.getContentView(mContext, adapter.getCount());
            if (mIndicatorView != null) {
                LayoutParams params = mIndicatorView.getLayoutParams();
                if (params != null) {
                    params = getIndicatorDefaultLayoutParams();
                }
                addView(mIndicatorView, params);
            }
        }
    }

    private LayoutPrams getIndicatorDefaultLayoutParams() {
        LayoutPrams lp = new LayoutPrams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.mLocation = LayoutPrams.LOCATION_OUT;
        return lp;
    }

    public static class LayoutPrams extends MarginLayoutParams {
        /** 决定指示器大致的位置，可以是上下左右，水平/垂直居中 */
        public int mGravity;
        /** 决定指示器是否在内容页面里面，取下面两值之一 */
        public int mLocation;
        public static final int LOCATION_INNER = 1;
        public static final int LOCATION_OUT = 2;

        public LayoutPrams(int width, int height) {
            super(width, height);
        }
    }
}
