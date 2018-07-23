package com.example.hua.framework.wrapper.emoji.defaults;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hua.framework.interfaces.IIndicator;


/**
 * Created by hua on 2017/10/7.
 * 圆型表情指示器
 */

public class CircleIndicator implements IIndicator {

    private LinearLayout mContainer;
    private ViewPager mViewPager;

    private static final int COLOR_NORMAL = 0xffD8D8D8;
    private static final int COLOR_SELECTED = 0xffFF4081;

    private static final int CIRCLE_RADIUS = 5; // dp
    private static final int CIRCLE_LEFT_MARGIN = 10; //dp

    @Override
    public void bindViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public View getContentView(Context context, int count) {
        if (mContainer == null) {
            mContainer = new LinearLayout(context);
            mContainer.setOrientation(LinearLayout.HORIZONTAL);
            mContainer.setGravity(Gravity.CENTER);
            for (int i = 0; i < count; i++) {
                View circleView = new View(context);
                circleView.setBackgroundDrawable(getCircleShap(i == 0 ? COLOR_SELECTED : COLOR_NORMAL));
                mContainer.addView(circleView, generateLayoutParams(context, i == 0));
            }
        }

        return mContainer;
    }

    private Drawable getCircleShap(final int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(new RectShape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(color);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, CIRCLE_RADIUS, paint);
            }
        });
        return shapeDrawable;
    }

    private ViewGroup.LayoutParams generateLayoutParams(Context context, boolean first) {
        int width = 2 * CIRCLE_RADIUS;
        if (first) {
            return new ViewGroup.LayoutParams(dip2px(context, width), dip2px(context, width));
        } else {
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(dip2px(context, width), dip2px(context, width));
            params.leftMargin = dip2px(context, CIRCLE_LEFT_MARGIN);
            return params;
        }
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void setPosition(int position) {
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            View child = mContainer.getChildAt(i);
            if (i == position) {
                child.setBackgroundDrawable(getCircleShap(COLOR_SELECTED));
            } else {
                child.setBackgroundDrawable(getCircleShap(COLOR_NORMAL));
            }
        }
    }
}
