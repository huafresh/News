package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.example.hua.framework.R;

/**
 * 经典下拉刷新头部默认实现
 *
 * @author hua
 * @version 2018/7/27 10:36
 */

public class ClassicRefreshHeader extends BaseClassicHeader {

    public ClassicRefreshHeader(Context context) {
        this(context, null);
    }

    public ClassicRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }


    @Override
    protected int layoutId() {
        return R.layout.default_refresh_header;
    }

    @Override
    protected int arrowImageId() {
        return R.id.iv_arrow;
    }

    @Override
    protected int loadingImageId() {
        return R.id.iv_loading;
    }

    @Override
    protected int titleTextViewId() {
        return R.id.tv_title;
    }

    @Override
    protected int lastTimeTextViewId() {
        return R.id.tv_last_time;
    }
}
