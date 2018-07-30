package com.example.hua.framework.support.pullrefresh;

import android.view.View;
import android.view.ViewGroup;


/**
 * 下拉刷新组件抽象。
 * 屏蔽底层刷新库，提供统一的api
 *
 * @author hua
 * @version 2018/4/4 10:57
 */

public interface IRefreshLayout{

    void setHeader(IHeader header);

    void setContentView(View view);

    void setFooter(IFooter footer);

    void setOnRefreshListener(OnRefreshListener listener);

    void setOnLoadMoreListener(OnLoadMoreListener listener);

    void finishRefresh(boolean success);

    void finishLoadMore(boolean success);

    interface OnRefreshListener {
        void onRefresh(IRefreshLayout refreshLayout);
    }

    interface OnLoadMoreListener {
        void onLoadMore(IRefreshLayout refreshLayout);
    }

}
