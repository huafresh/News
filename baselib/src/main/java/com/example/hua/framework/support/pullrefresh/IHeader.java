package com.example.hua.framework.support.pullrefresh;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

/**
 * @author hua
 * @version 2018/7/24 9:04
 */

public interface IHeader {

    /**
     * 获取实体视图
     *
     * @return 实体视图
     */
    @NonNull
    View getView();

    /**
     * 获取变换方式 {@link SpinnerStyle} 必须返回 非空
     *
     * @return 变换方式
     */
    @NonNull
    SpinnerStyle getSpinnerStyle();

    /**
     * 状态改变事件 {@link HeaderState}
     *
     * @param refreshLayout RefreshLayout
     * @param oldState      改变之前的状态
     * @param newState      改变之后的状态
     */
    void onStateChanged(IRefreshLayout refreshLayout, HeaderState oldState, HeaderState newState);

}
