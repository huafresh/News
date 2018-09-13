package com.example.hua.framework.lifebus;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

/**
 * @author hua
 * @version 2018/8/29 10:32
 */

public class ObserverConfig<T> {
    Class<T> eventType;
    Lifecycle.Event removeEvent = Lifecycle.Event.ON_DESTROY;
    /**
     * 注意与{@link #removeEvent}的区别。
     * inActiveEvent决定是否能接收到事件。
     * removeEvent决定是否remove Observer
     */
    Lifecycle.Event inActiveEvent = Lifecycle.Event.ON_DESTROY;
    boolean sticky = false;
    ThreadMode threadMode = ThreadMode.MAIN;

    ObserverConfig() {
    }

    public ObserverConfig<T> eventType(Class<T> eventType) {
        this.eventType = eventType;
        return this;
    }

    public ObserverConfig<T> removeEvent(Lifecycle.Event removeEvent) {
        this.removeEvent = removeEvent;
        return this;
    }

    public ObserverConfig<T> inActiveEvent(Lifecycle.Event inActiveEvent) {
        this.inActiveEvent = inActiveEvent;
        return this;
    }

    public ObserverConfig<T> threadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
        return this;
    }

    public void observe(LifecycleOwner owner, EventObserver<T> observer) {
        observeInner(owner, observer, false);
    }

    public void observeSticky(LifecycleOwner owner, EventObserver<T> observer) {
        observeInner(owner, observer, true);
    }

    private void observeInner(LifecycleOwner owner, EventObserver<T> observer, boolean sticky) {
        this.sticky = sticky;
        LifecycleBus.observe(owner, observer, this);
    }

}
