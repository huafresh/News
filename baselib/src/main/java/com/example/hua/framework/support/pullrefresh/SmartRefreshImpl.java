package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * 刷新组件底层使用{@link SmartRefreshLayout}库
 *
 * @author hua
 * @version 2018/4/4 11:15
 */

public class SmartRefreshImpl implements IRefreshLayout {

    private SmartRefreshLayout smartRefreshLayout;
    private Context context;


    public SmartRefreshImpl(Context context) {
        this.context = context;
        this.smartRefreshLayout = new SmartRefreshLayout(context);
    }

    @Override
    public void setHeader(IHeaderStyle header) {
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
    }

    @Override
    public void setContentView(View view) {
        smartRefreshLayout.setRefreshContent(view);
    }

    @Override
    public void setFooter(IFooterStyle footer) {
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(context));
    }

    @Override
    public ViewGroup getRefreshLayout() {
        return smartRefreshLayout;
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener listener) {
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                listener.onRefresh(SmartRefreshImpl.this);
            }
        });
    }

    @Override
    public void setOnLoadMoreListener(final OnLoadMoreListener listener) {
        smartRefreshLayout.setOnLoadMoreListener(new com.scwang.smartrefresh.layout.listener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                listener.onLoadMore(SmartRefreshImpl.this);
            }
        });
    }

    @Override
    public void finishRefresh(boolean success) {
        smartRefreshLayout.finishRefresh(success);
    }

    @Override
    public void finishLoadMore(boolean success) {
        smartRefreshLayout.finishLoadMore(success);
    }
}
