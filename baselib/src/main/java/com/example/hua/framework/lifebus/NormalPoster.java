package com.example.hua.framework.lifebus;

/**
 * @author hua
 * @version 2018/8/29 15:13
 */

class NormalPoster<T> implements IPoster<T> {
    @Override
    public void post(EventObserver<T> observer, T event) {
        observer.onEvent(event);
    }
}
