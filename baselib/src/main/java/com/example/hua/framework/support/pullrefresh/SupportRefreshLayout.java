package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 通用刷新布局，屏蔽底层实现。
 * 只需要使用此控件包裹需要添加刷新功能的布局即可。
 *
 * @author hua
 * @version 2018/4/4 10:52
 */

public class SupportRefreshLayout extends FrameLayout implements IRefreshLayout {

    private BaseRefreshLayout refreshLayout;
    private IHeader header;
    private IFooter footer;
    private IHeader header;
    private Context context;

    public SupportRefreshLayout(Context context) {
        this(context, null);
    }

    public SupportRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SupportRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        refreshLayout = new SmartRefreshImpl(context);
        refreshLayout.setHeader(new ClassicRefreshHeader(context));
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SupportRefreshLayout can host only one direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SupportRefreshLayout can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SupportRefreshLayout can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SupportRefreshLayout can host only one direct child");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        calculateContentView();
    }

    private void calculateContentView() {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            removeView(child);
            refreshLayout.setContentView(child);
            addView(refreshLayout.getContainer(), childParams);
        }
    }

    @Override
    public void setHeader(IHeader header) {
        refreshLayout.setHeader(header);
    }

    @Override
    public void setContentView(View view) {
        refreshLayout.setContentView(view);
    }

    @Override
    public void enableLoadMore(boolean enable) {
        refreshLayout.enableLoadMore(enable);
        if (enable && this.header == null) {
            refreshLayout.setFooter(new ClassicRefreshFooter(context));
        }
    }

    @Override
    public void setFooter(IFooter footer) {
        refreshLayout.setFooter(footer);
        this.footer = footer;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        refreshLayout.setOnRefreshListener(listener);
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        refreshLayout.setOnLoadMoreListener(listener);
    }

    @Override
    public void finishRefresh(boolean success) {
        refreshLayout.finishRefresh(success);
    }

    @Override
    public void finishLoadMore(boolean success) {
        refreshLayout.finishLoadMore(success);
    }

    @Override
    public boolean isSupportAutoLoadMore() {
        return refreshLayout.isSupportAutoLoadMore();
    }

    @Override
    public void enableAutoLoadMore(boolean enable) {
        refreshLayout.enableAutoLoadMore(enable);
    }

    private static class DefaultRefreshLayoutFactory implements IRefreshLayoutFactory {

        @Override
        public IRefreshLayout create(Context context) {
            return new SmartRefreshImpl(context);
        }
    }

    /**
     * 底层刷新布局实现类工厂接口。
     * 默认实现是使用{@link SmartRefreshLayout}下拉刷新库
     *
     * @deprecated 底层实现不应该给上层替换
     */
    @Deprecated
    interface IRefreshLayoutFactory {
        IRefreshLayout create(Context context);
    }


}
