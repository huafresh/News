package com.example.hua.framework.support.pullrefresh;

/**
 * @author hua
 * @version 2018/7/24 9:27
 */

public enum FooterState {
    /**
     * 初始状态
     */
    None,

    /**
     * 上拉加载
     */
    Pull_Up_To_Load,

    /**
     * 释放加载
     */
    Release_To_Load,

    /**
     * 加载中
     */
    Loading,
}
