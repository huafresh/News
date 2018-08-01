package com.example.hua.framework.support.pullrefresh;

import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.wrapper.pullrefresh.PTRLayout;

/**
 * 刷新组件底层使用{@link PTRLayout}。
 * 该控件还不完善，暂不使用
 *
 * @author hua
 * @version 2018/4/4 11:15
 */

abstract class PTRRefreshImpl extends BaseRefreshLayout {

    @Override
    public void setHeader(IHeader header) {

    }

    @Override
    public void setContentView(View view) {

    }

    @Override
    public void setFooter(IFooter footer) {

    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {

    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {

    }

    @Override
    public void finishRefresh(boolean success) {

    }

    @Override
    public void finishLoadMore(boolean success) {

    }

    @Override
    public boolean isSupportAutoLoadMore() {
        return false;
    }


    @Override
    public ViewGroup getContainer() {
        return null;
    }
}
