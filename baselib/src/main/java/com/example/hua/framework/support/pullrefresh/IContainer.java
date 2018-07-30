package com.example.hua.framework.support.pullrefresh;

import android.view.ViewGroup;

/**
 * @author hua
 * @version 2018/7/27 11:26
 */
interface IContainer {

    /**
     * 返回的ViewGroup也就是包裹头部、内容、底部布局的容器布局。
     * 不同的底层实现返回不一样
     *
     * @return 容器布局
     */
    ViewGroup getContainer();

}
