package com.example.hua.framework.wrapper.swipeback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hua
 * @version 2018/9/11 10:07
 */

public class ActivitySlidingPage implements ISlidingPage {
    private Activity activity;
    private ViewGroup decorView;

    public ActivitySlidingPage(Activity activity) {
        this.activity = activity;
        this.decorView = (ViewGroup) activity.getWindow().getDecorView();

//        activity.getWindow().addFlags(Window.fet);
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        decorView.invalidate();
    }

    @Override
    public View getView() {
        if (decorView != null && decorView.getChildCount() > 0) {
            return decorView.getChildAt(0);
        }
        return null;
    }
}
