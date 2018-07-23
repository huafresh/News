package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 通用刷新布局，屏蔽底层实现
 *
 * @author hua
 * @version 2018/4/4 10:52
 */

public class SupportRefreshLayout extends FrameLayout implements IRefreshLayout {

    private static final int STYLE_COMM = 0;
    private IHeaderStyle headerStyle = new DefaultHeaderStyle();
    private IFooterStyle footerStyle = new DefaultFooterStyle();

    private static IRefreshLayoutFactory sRefreshLayoutFactory = new DefaultRefreshLayoutFactory();

    private IRefreshLayout refreshLayout;

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
        refreshLayout = sRefreshLayoutFactory.create(context);
        refreshLayout.setHeader(headerStyle);
        refreshLayout.setFooter(footerStyle);
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
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            removeView(child);
            refreshLayout.setContentView(child);
            addView(refreshLayout.getRefreshLayout(), childParams);
        }
    }

    @Override
    public void setHeader(IHeaderStyle header) {
        refreshLayout.setHeader(header);
    }

    @Override
    public void setContentView(View view) {
        refreshLayout.setContentView(view);
    }

    @Override
    public void setFooter(IFooterStyle footer) {
        refreshLayout.setFooter(footer);
    }

    @Override
    public ViewGroup getRefreshLayout() {
        return refreshLayout.getRefreshLayout();
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

    public void setHeaderStyle(IHeaderStyle headerStyle) {
        this.headerStyle = this.headerStyle;
    }

    public void setFooterStyle(IFooterStyle footerStyle) {
        this.footerStyle = this.footerStyle;
    }

    private static class DefaultHeaderStyle implements IHeaderStyle{

        @Override
        public int getStyle() {
            return STYLE_COMM;
        }
    }

    private static class DefaultFooterStyle implements IFooterStyle{

        @Override
        public int getStyle() {
            return STYLE_COMM;
        }
    }

    private static class DefaultRefreshLayoutFactory implements IRefreshLayoutFactory {

        @Override
        public IRefreshLayout create(Context context) {
            return new SmartRefreshImpl(context);
        }
    }

    public interface IRefreshLayoutFactory {
        IRefreshLayout create(Context context);
    }

    public static void setsRefreshLayoutFactory(IRefreshLayoutFactory factory){
        SupportRefreshLayout.sRefreshLayoutFactory = factory;
    }
}
