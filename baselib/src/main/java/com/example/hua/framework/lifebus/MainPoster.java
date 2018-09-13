package com.example.hua.framework.lifebus;

import android.os.Handler;
import android.os.Looper;

/**
 * @author hua
 * @version 2018/8/29 15:10
 */

class MainPoster<T> implements IPoster<T> {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final EventObserver<T> observer, final T event) {
        if (isMainThread()) {
            observer.onEvent(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    observer.onEvent(event);
                }
            });
        }
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
