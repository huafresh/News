package com.example.hua.framework.download.core;

/**
 * 监听网络可用状态
 *
 * @author hua
 * @version 1.0
 * @since 2018/8/18
 */
public interface INetworkObserver {

    int networkType();

    void onAvailable();

    void onDisAvailable();
}
