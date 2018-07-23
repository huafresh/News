package com.example.hua.framework.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以禁止水平滑动的ViewPager
 *
 * @author hua
 * @version 2017/11/14 20:13
 */

public class DisableHorScrollViewPager extends ViewPager{

    private boolean mScrollable = false;

    public DisableHorScrollViewPager(Context context) {
        super(context);
    }

    public DisableHorScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scrollable){
        mScrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mScrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mScrollable && super.onTouchEvent(ev);
    }

}
