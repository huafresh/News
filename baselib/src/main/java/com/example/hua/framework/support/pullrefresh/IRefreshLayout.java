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

public interface IRefreshLayout {

    void setHeader(IHeaderStyle header);

    void setContentView(View view);

    void setFooter(IFooterStyle footer);

    ViewGroup getRefreshLayout();

    void setOnRefreshListener(OnRefreshListener listener);

    void setOnLoadMoreListener(OnLoadMoreListener listener);

    void finishRefresh(boolean success);

    void finishLoadMore(boolean success);

    /**
     * 这里定义的header不是真正的header布局接口，真正的header布局接口要由具体的刷新库提供
     * 这里返回的整型常量的具体含义需要由{@link IRefreshLayout}的实现类中的{@link IRefreshLayout#setHeader}
     * 方法解释。
     */
    interface IHeaderStyle {
        int getStyle();
    }

    interface IFooterStyle {
        int getStyle();
    }

    interface OnRefreshListener {
        void onRefresh(IRefreshLayout refreshLayout);
    }

    interface OnLoadMoreListener {
        void onLoadMore(IRefreshLayout refreshLayout);
    }

}
