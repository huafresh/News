package com.example.hua.framework.lifebus;

/**
 * @author hua
 * @version 2018/8/29 14:57
 */

interface IPoster<T> {

    /**
     * 决定事件如何post
     *
     * @param observer EventObserver
     * @param event    event
     */
    void post(EventObserver<T> observer, T event);

}
