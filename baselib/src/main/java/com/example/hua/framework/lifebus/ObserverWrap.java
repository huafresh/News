package com.example.hua.framework.lifebus;

import android.arch.lifecycle.Lifecycle;

/**
 * @author hua
 * @version 2018/8/29 9:50
 */

class ObserverWrap<T> implements EventObserver<EventWrap<T>> {

    Class<T> eventType;
    EventObserver<T> observer;
    private ObserverConfig<T> config;
    private boolean active = true;
    boolean removed = false;

    private boolean sendSticky = false;
    private IPoster<T> poster;

    ObserverWrap(Class<T> eventType, EventObserver<T> observer, ObserverConfig<T> config) {
        this.eventType = eventType;
        this.observer = observer;
        this.config = config;
        this.poster = createPoster();
    }

    private IPoster<T> createPoster() {
        switch (config.threadMode) {
            case POSTING:
                return new NormalPoster<>();
            case MAIN:
                return new MainPoster<>();
            default:
                break;
        }
        return new MainPoster<>();
    }

    void onStateChanged(Lifecycle.Event event) {
        active = !(event.compareTo(config.inActiveEvent) >= 0);
        removed = event.compareTo(config.removeEvent) >= 0;
    }

    void update(ObserverWrap observerWrap){
        //not support yet
    }

    @Override
    public void onEvent(final EventWrap<T> event) {
        if (active) {
            //不管是sticky事件还是普通事件，都会post到这里。
            //因此需要结合Observer的实际需求，按需发送事件。
            //目前的策略就是：
            //1、sticky事件不会发到非sticky的Observer
            //2、非sticky事件不会发到sticky的Observer
            //3、针对某个具体的Observer实例，sticky事件只会在订阅瞬间发送一次
            if (!event.sticky && !isSticky()) {
                poster.post(observer, event.event);
            } else if (event.sticky && isSticky()) {
                if (!sendSticky) {
                    poster.post(observer, event.event);
                    sendSticky = true;
                }
            }
        }
    }

    boolean isSticky() {
        return config.sticky;
    }


    @Override
    public int hashCode() {
        return observer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObserverWrap) {
            return observer.equals(((ObserverWrap) obj).observer);
        }
        return super.equals(obj);
    }
}
