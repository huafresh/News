package com.example.hua.framework.support.pullrefresh;

import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;


/**
 * 下拉刷新组件抽象。
 * 屏蔽底层刷新库，提供统一的api
 *
 * @author hua
 * @version 2018/4/4 10:57
 */

public interface IRefreshLayout {

    /**
     * 设置下拉刷新头部布局。
     * 如果需求是经典的下拉刷新头，则继承{@link BaseClassicHeader}做简单自定义即可。
     * 如果连简单自定义也懒得写，
     * 那么可以使用默认的下拉刷新头{@link ClassicRefreshHeader}，不需要设置，默认即可使用。
     *
     * @param header IHeader
     */
    void setHeader(IHeader header);

    /**
     * 设置下拉刷新内容布局。
     * 使用{@link SupportRefreshLayout}包裹内容布局即可。
     * 也可以手动设置。
     *
     * @param view View
     */
    void setContentView(View view);

    /**
     * 使能 or 禁止上拉加载更多
     *
     * @param enable 使能 or 禁止。默认禁止
     */
    void enableLoadMore(boolean enable);

    /**
     * 设置上拉加载布局。
     * 如果需求是经典的上拉加载布局，则继承{@link BaseClassicFooter}做简单自定义即可。
     * 同时也提供了默认的上拉加载实现{@link ClassicRefreshHeader}，不需要设置，默认即可使用。
     * 注意先调用{@link #enableLoadMore(boolean)}开启
     *
     * @param footer IFooter
     */
    void setFooter(IFooter footer);

    /**
     * 设置下拉刷新监听
     *
     * @param listener OnRefreshListener
     */
    void setOnRefreshListener(OnRefreshListener listener);

    /**
     * 设置上拉加载监听
     *
     * @param listener OnLoadMoreListener
     */
    void setOnLoadMoreListener(OnLoadMoreListener listener);

    /**
     * 结束下拉刷新
     *
     * @param success 是否成功，暂时没用
     */
    void finishRefresh(boolean success);

    /**
     * 结束上拉加载
     *
     * @param success 是否成功，暂时没用
     */
    void finishLoadMore(boolean success);

    /**
     * @return 是否支持自动加载更多
     */
    boolean isSupportAutoLoadMore();

    /**
     * 使能 or 禁止自动加载更多。默认禁止。此方法仅在{@link #isSupportAutoLoadMore()}返回true时有效
     *
     * @param enable 使能 or 禁止
     */
    void enableAutoLoadMore(boolean enable);

    interface OnRefreshListener {
        void onRefresh(IRefreshLayout refreshLayout);
    }

    interface OnLoadMoreListener {
        void onLoadMore(IRefreshLayout refreshLayout);
    }


}
