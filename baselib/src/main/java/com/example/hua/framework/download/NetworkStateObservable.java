package com.example.hua.framework.download;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.ArraySet;

import com.example.hua.framework.download.core.INetworkObserver;

import java.util.Iterator;
import java.util.Set;

/**
 * @author hua
 * @version 1.0
 * @since 2018/8/18
 */
class NetworkStateObservable {

    private Context context;
    private Set<INetworkObserver> observers = new ArraySet<>();
    @SuppressLint("StaticFieldLeak")
    private static NetworkStateObservable sInstance;

    private NetworkStateObservable(Context context) {
        this.context = context.getApplicationContext();
        NetworkReceiver networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, filter);
    }

    static NetworkStateObservable get(Context context) {
        if (sInstance == null) {
            synchronized (NetworkStateObservable.class) {
                if (sInstance == null) {
                    sInstance = new NetworkStateObservable(context);
                }
            }
        }
        return sInstance;
    }

    void add(INetworkObserver observer) {
        observers.add(observer);
        if (isNetworkAvailable(observer)) {
            observer.onAvailable();
        } else {
            observer.onDisAvailable();
        }
    }

    void remove(INetworkObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (INetworkObserver observer : observers) {
            if (isNetworkAvailable(observer)) {
                observer.onAvailable();
            } else {
                observer.onDisAvailable();
            }
        }
    }

    private boolean isNetworkAvailable(INetworkObserver observer) {
        int networkType = observer.networkType();
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.getType() >= networkType;
        }
        return false;
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            notifyObservers();
        }
    }


}
