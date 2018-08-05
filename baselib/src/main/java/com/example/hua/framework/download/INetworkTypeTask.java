package com.example.hua.framework.download;

/**
 * 下载任务在满足条件时，回调指定方法。
 * Created by hua on 2018/7/29.
 */

interface INetworkTypeTask {

    int getAllowNetworkType();

    void onAvailable();

    void onDisAvailable();

}
