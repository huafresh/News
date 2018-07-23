package com.example.hua.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by hua on 2017/6/16.
 * 主要解决嵌套ListView或者RecyclerView的滑动冲突
 */

public class CommonScrollView extends LinearLayout {


    private int startY;

    public CommonScrollView(Context context) {
        super(context);
    }

    public CommonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int deltaY = y - startY;
                changeTransitionY(deltaY);
                startY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    private void changeTransitionY(int deltaY) {
        View directChild = getChildAt(0);
        if (directChild != null) {
            LinearLayout.LayoutParams lp = (LayoutParams) directChild.getLayoutParams();

            int childTop = directChild.getTop();
            if (childTop < 0) {
                if (deltaY > 0) {
                    lp.topMargin += deltaY;
                }
            }
            if (directChild.getBottom() > 0) {
                if (deltaY < 0) {
                    lp.topMargin += deltaY;
                }
            }

            directChild.setLayoutParams(lp);
        }
    }

}
