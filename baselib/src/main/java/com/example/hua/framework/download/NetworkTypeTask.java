package com.example.hua.framework.download;

/**
 * 下载任务在满足条件时，方可执行
 * Created by hua on 2018/7/29.
 */

interface NetworkTypeTask extends Runnable {

    int getAllowNetworkType();

}
