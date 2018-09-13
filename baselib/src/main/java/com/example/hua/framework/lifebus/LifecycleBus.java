package com.example.hua.framework.lifebus;

import android.arch.lifecycle.LifecycleOwner;

import java.util.HashMap;

/**
 * 基于Lifecycle实现的事件通知框架。自动感知生命周期
 * <p>
 * 使用方式：
 * 1、调用{@link #get(Class)}获取{@link ObserverConfig}对象，进行相关的配置。
 * 2、调用{@link ObserverConfig#observe(LifecycleOwner, EventObserver)}订阅事件
 * 3、调用{@link #post(Object)}发送事件。
 *
 * @author hua
 * @version 2018/8/27 9:05
 */
@SuppressWarnings("unchecked")
public class LifecycleBus {

    private static HashMap<Class, Object> stickyEventMap = new HashMap<>();

    public static <T> ObserverConfig<T> get(Class<T> eventType) {
        return new ObserverConfig<T>().eventType(eventType);
    }

    static <T> void observe(LifecycleOwner owner, EventObserver<T> observer, ObserverConfig<T> observerConfig) {
        EventCenter eventCenter = EventCenter.getInstance();
        eventCenter.observe(owner, buildEventObserver(observer, observerConfig));
    }

    private static <T> ObserverWrap buildEventObserver(EventObserver<T> observer,
                                                       ObserverConfig<T> observerConfig) {
        return new ObserverWrap(observerConfig.eventType, observer, observerConfig);
    }


    /**
     * 发送普通事件，只对当前已经添加的Observer有效
     *
     * @param event event
     */
    public static void post(final Object event) {
        postInner(event, false);
    }

    /**
     * 发送粘性事件，这种事件常驻内存。
     * 在观察者订阅的瞬间会将最新数据的数据回调给Observer。
     * 注意：在Observer激活期间内只会回调一次
     *
     * @param event event
     */
    public static void postSticky(final Object event) {
        postInner(event, true);
    }

    public static void removeSticky(Class eventType) {
        stickyEventMap.remove(eventType);
    }

    public static Object getStickyEvent(Class eventClass) {
        return stickyEventMap.get(eventClass);
    }

    public static void clearSticky() {
        stickyEventMap.clear();
    }

    /**
     * 把存储在map中的黏性事件发送出去。
     *
     * @param eventType 事件class
     */
    static void sendStickyEvent(Class eventType) {
        postInner(stickyEventMap.get(eventType), true);
    }

    private static void postInner(final Object event, final boolean sticky) {
        if (sticky) {
            stickyEventMap.put(event.getClass(), event);
        }

        EventCenter.getInstance().sendEvent(wrapperEvent(event, sticky));
    }

    private static EventWrap wrapperEvent(Object event, boolean sticky) {
        return new EventWrap(event, event.getClass(), sticky);
    }
}
