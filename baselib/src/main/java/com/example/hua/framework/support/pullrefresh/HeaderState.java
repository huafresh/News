package com.example.hua.framework.support.pullrefresh;

/**
 * @author hua
 * @version 2018/7/24 9:27
 */

public enum HeaderState {

    /**
     * 初始状态
     */
    None,

    /**
     * 下拉刷新
     */
    Pull_Down_To_Refresh,

    /**
     * 释放刷新
     */
    Release_To_Refresh,

    /**
     * 刷新中
     */
    Refreshing,
}
