package com.example.hua.framework.lifebus;

/**
 * @author hua
 * @version 2018/8/29 9:31
 */

public interface EventObserver<T> {
    /**
     * 接收到事件时调用
     *
     * @param event event
     */
    void onEvent(T event);
}
