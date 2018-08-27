package com.example.hua.framework.eventbus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 版本可控制的LiveData
 *
 * @author hua
 * @version 2018/8/27 10:27
 */

public class VersionLiveData<T> extends MutableLiveData<T> {

    private int version = -1;
    private Class<T> eventType;

    VersionLiveData(Class<T> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        this.observe(owner, observer, false);
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        observeForever(observer, false);
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer, boolean sticky) {
        ObserverWrapper observerWrapper = new ObserverWrapper(version, eventType, observer, sticky);
        super.observe(owner, observerWrapper);
        //LifecycleBus.sendStickyEvent(eventType);
    }

    public void observeForever(@NonNull Observer<T> observer, boolean sticky) {
        ObserverWrapper observerWrapper = new ObserverWrapper(version, eventType, observer, sticky);
        super.observeForever(observerWrapper);
        //observerWrapper.handleSticky();
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
        version++;
    }

    class ObserverWrapper implements Observer<T> {
        private int version;
        private Class<T> clazz;
        private Observer<T> observer;
        private boolean sticky;

        ObserverWrapper(int version, Class<T> clazz, Observer<T> observer, boolean sticky) {
            this.version = version;
            this.clazz = clazz;
            this.observer = observer;
            this.sticky = sticky;
        }

        void handleSticky(){
            if (sticky) {

            }
        }

        @Override
        public void onChanged(@Nullable T o) {
            if (VersionLiveData.this.version > version) {
                observer.onChanged(o);
                version = VersionLiveData.this.version;
            }
        }
    }
}
