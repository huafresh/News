package hua.news.module_common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import hua.news.module_common.base.BaseApplication;


/**
 * 本地广播的简单封装。适用于Intent只有action的情形
 *
 * @author hua
 * @version 2017/10/23 10:20
 */
@Deprecated
public class BroadcastManager {

    public static void registerReceiver(BroadcastReceiver receiver, String[] actions) {
        Context context = BaseApplication.getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        localBroadcastManager.registerReceiver(receiver, filter);
    }

    public static void unRegisterReceiver(BroadcastReceiver receiver) {
        Context context = BaseApplication.getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }

    public static void sendBroadcast(String action) {
        Context context = BaseApplication.getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent();
        intent.setAction(action);
        localBroadcastManager.sendBroadcast(intent);
    }

    public static void sendBroadcastSync(String action) {
        Context context = BaseApplication.getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent();
        intent.setAction(action);
        localBroadcastManager.sendBroadcastSync(intent);
    }
}
