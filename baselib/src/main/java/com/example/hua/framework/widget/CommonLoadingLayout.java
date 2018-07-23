package com.example.hua.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */
@Deprecated
public class CommonLoadingLayout extends FrameLayout {

    public CommonLoadingLayout(Context context) {
        this(context, null);
    }

    public CommonLoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }



}
