package com.example.hua.framework.download;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听网络可用状态
 * Created by hua on 2018/7/29.
 */

class NetworkTypeUtil {
    private Context context;
    private List<NetworkTypeTask> tasks = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static NetworkTypeUtil sInstance;

    private NetworkTypeUtil(Context context) {
        this.context = context.getApplicationContext();
        Receiver receiver = new Receiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }

    static NetworkTypeUtil get(Context context) {
        if (sInstance == null) {
            synchronized (NetworkTypeUtil.class) {
                if (sInstance == null) {
                    sInstance = new NetworkTypeUtil(context);
                }
            }
        }
        return sInstance;
    }

    void enqueue(NetworkTypeTask task) {
        tasks.add(task);
    }

    private void runTasks() {
        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            NetworkTypeTask task = tasks.get(0);
            if (isNetworkAvailable(task)) {
                task.run();
                tasks.remove(task);
            }
        }
    }

    private boolean isNetworkAvailable(NetworkTypeTask task) {
        int allowNetworkType = task.getAllowNetworkType();
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.getType() >= allowNetworkType;
        }
        return false;
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            runTasks();
        }
    }

}
