package com.example.hua.framework.download;

/**
 * 下载状态
 * Created by hua on 2018/7/29.
 */

public enum DownloadStatus {
    /**
     * 处于队列中等待开始下载
     */
    PENDING,
    /**
     * 正在下载
     */
    DOWNLOADING,
    /**
     * 下载暂停
     */
    PAUSED,
    /**
     * 下载失败
     */
    FAILED,
    /**
     * 下载完成
     */
    FINISHED,
}
