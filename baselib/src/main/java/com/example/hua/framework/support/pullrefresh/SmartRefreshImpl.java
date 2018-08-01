package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.*;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * 刷新组件底层使用{@link SmartRefreshLayout}库
 *
 * @author hua
 * @version 2018/4/4 11:15
 */

class SmartRefreshImpl extends BaseRefreshLayout {

    private SmartRefreshLayout smartRefreshLayout;
    private Context context;


    public SmartRefreshImpl(Context context) {
        this.context = context;
        this.smartRefreshLayout = new SmartRefreshLayout(context);
        smartRefreshLayout.setEnableAutoLoadMore(false);
    }

    @Override
    public void setHeader(IHeader header) {
        if (header != null) {
            smartRefreshLayout.setRefreshHeader(new SmartClassicRefreshHeader(header, this));
//            smartRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        }
    }

    @Override
    public void setContentView(View view) {
        if (view != null) {
            smartRefreshLayout.setRefreshContent(view);
        }
    }

    @Override
    public void enableLoadMore(boolean enable) {

    }

    @Override
    public void setFooter(IFooter footer) {
        if (footer != null) {
            smartRefreshLayout.setRefreshFooter(new SmartClassicRefreshFooter(footer, this));
        }
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

    @Override
    public boolean isSupportAutoLoadMore() {
        return true;
    }

    @Override
    public void enableAutoLoadMore(boolean enable) {
        smartRefreshLayout.setEnableAutoLoadMore(enable);
    }

    @Override
    public ViewGroup getContainer() {
        return smartRefreshLayout;
    }

    @NonNull
    private static com.scwang.smartrefresh.layout.constant.SpinnerStyle spinnerStyleConvert(SpinnerStyle style) {
        com.scwang.smartrefresh.layout.constant.SpinnerStyle result;
        switch (style) {
            case Translate:
                result = com.scwang.smartrefresh.layout.constant.SpinnerStyle.Translate;
                break;
            case FixedBehind:
                result = com.scwang.smartrefresh.layout.constant.SpinnerStyle.FixedBehind;
                break;
            default:
                result = com.scwang.smartrefresh.layout.constant.SpinnerStyle.Translate;
                break;
        }
        return result;
    }


    private static class SmartClassicRefreshHeader implements RefreshHeader {
        private IHeader header;
        private IRefreshLayout refreshLayout;
        private View headerView;
        private HeaderState curState = HeaderState.None;

        public SmartClassicRefreshHeader(IHeader header, IRefreshLayout refreshLayout) {
            this.header = header;
            this.refreshLayout = refreshLayout;
        }

        @NonNull

        @Override
        public View getView() {
            if (headerView == null) {
                headerView = header.getView();
            }
            return headerView;
        }

        @NonNull
        @Override
        public com.scwang.smartrefresh.layout.constant.SpinnerStyle getSpinnerStyle() {
            return spinnerStyleConvert(header.getSpinnerStyle());
        }

        @Override
        public void setPrimaryColors(int... colors) {

        }

        @Override
        public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

        }

        @Override
        public void onPulling(float percent, int offset, int height, int extendHeight) {

        }

        @Override
        public void onReleasing(float percent, int offset, int height, int extendHeight) {

        }

        @Override
        public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

        }

        @Override
        public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

        }

        @Override
        public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
            return 0;
        }

        @Override
        public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

        }

        @Override
        public boolean isSupportHorizontalDrag() {
            return false;
        }

        @Override
        public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
            HeaderState state = headerStateConvert(newState);
            if (state != null && curState != state) {
                header.onStateChanged(this.refreshLayout, curState, state);
                curState = state;
            }
        }

        private static HeaderState headerStateConvert(RefreshState state) {
            switch (state) {
                case None:
                    return HeaderState.None;
                case PullDownToRefresh:
                    return HeaderState.Pull_Down_To_Refresh;
                case ReleaseToRefresh:
                    return HeaderState.Release_To_Refresh;
                case Refreshing:
                case RefreshReleased:
                    return HeaderState.Refreshing;
                default:
                    return null;
            }
        }
    }

    private static class SmartClassicRefreshFooter implements RefreshFooter {
        private IFooter footer;
        private IRefreshLayout refreshLayout;
        private View footerView;
        private FooterState curState;

        public SmartClassicRefreshFooter(IFooter footer, IRefreshLayout refreshLayout) {
            this.footer = footer;
            this.refreshLayout = refreshLayout;
        }

        @NonNull

        @Override
        public View getView() {
            if (footerView == null) {
                footerView = footer.getView();
            }
            return footerView;
        }

        @NonNull
        @Override
        public com.scwang.smartrefresh.layout.constant.SpinnerStyle getSpinnerStyle() {
            return spinnerStyleConvert(footer.getSpinnerStyle());
        }

        @Override
        public void setPrimaryColors(int... colors) {

        }

        @Override
        public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

        }

        @Override
        public void onPulling(float percent, int offset, int height, int extendHeight) {

        }

        @Override
        public void onReleasing(float percent, int offset, int height, int extendHeight) {

        }

        @Override
        public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

        }

        @Override
        public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

        }

        @Override
        public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
            return 0;
        }

        @Override
        public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

        }

        @Override
        public boolean isSupportHorizontalDrag() {
            return false;
        }

        @Override
        public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
            FooterState state = footerStateConvert(newState);
            if (state != null && state != curState) {
                footer.onStateChanged(this.refreshLayout, curState, state);
                curState = state;
            }
        }

        private static FooterState footerStateConvert(RefreshState state) {
            switch (state) {
                case None:
                    return FooterState.None;
                case PullUpToLoad:
                    return FooterState.Pull_Up_To_Load;
                case ReleaseToLoad:
                    return FooterState.Release_To_Load;
                case Loading:
                case LoadReleased:
                    return FooterState.Loading;
                default:
                    return null;
            }
        }

        @Override
        public boolean setNoMoreData(boolean noMoreData) {
            return false;
        }
    }


}
