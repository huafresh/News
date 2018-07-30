package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.example.hua.framework.R;

/**
 * 经典上拉加载更多默认实现
 *
 * @author hua
 * @version 2018/7/27 10:36
 */

public class ClassicRefreshFooter extends BaseClassicFooter {

    public ClassicRefreshFooter(Context context) {
        this(context, null);
    }

    public ClassicRefreshFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }


    @Override
    protected int layoutId() {
        return R.layout.default_refresh_footer;
    }

    @Override
    protected int loadingImageId() {
        return R.id.iv_loading;
    }

    @Override
    protected int textViewId() {
        return R.id.tv_text;
    }

}
