package com.example.hua.framework.eventbus;

import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * 基于Lifecycle实现的事件bus。自动感知生命周期
 *
 * @author hua
 * @version 2018/8/27 9:05
 */
@SuppressWarnings("unchecked")
public class LifecycleBus {

    private static HashMap<Class, VersionLiveData> liveDataMap = new HashMap<>();
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static HashMap<Class, Object> stickyEvents = new HashMap<>();

    public static <T> VersionLiveData<T> getLiveData(Class<T> eventType) {
        return getLiveDataInner(eventType);
    }

    @NonNull
    private static <T> VersionLiveData<T> getLiveDataInner(Class<T> eventType) {
        VersionLiveData<T> result = (VersionLiveData<T>) liveDataMap.get(eventType);
        if (result == null) {
            result = new VersionLiveData<>(eventType);
            liveDataMap.put(eventType, result);
        }
        return result;
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static void post(final Object event) {
        post(event, false);
    }

    public static void postSticky(final Object event) {
        post(event, true);
    }

    public static void removeSticky(Class eventType) {
        stickyEvents.remove(eventType);
    }

    public static void clearSticky() {
        stickyEvents.clear();
    }

    static void sendStickyEvent(Class eventType) {
        Object event = stickyEvents.get(eventType);
        post(event);
    }

    private static void post(final Object event, final boolean sticky) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!sticky) {
                    MutableLiveData liveData = getLiveDataInner(event.getClass());
                    liveData.setValue(event);
                } else {
                    stickyEvents.put(event.getClass(), event);
                }
            }
        };

        if (isMainThread()) {
            r.run();
        } else {
            handler.post(r);
        }
    }
}
