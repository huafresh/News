package com.example.hua.framework.lifebus;

/**
 * @author hua
 * @version 2018/8/28 9:11
 */

class EventWrap<T> {
    T event;
    Class<T> eventType;
    boolean sticky;

    EventWrap(T event, Class<T> eventType, boolean sticky) {
        this.event = event;
        this.eventType = eventType;
        this.sticky = sticky;
    }
}
