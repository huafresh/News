package com.example.hua.framework.download.core;

import android.support.annotation.Nullable;

/**
 * 持久化存储下载数据
 * Created by hua on 2018/8/4.
 */

public interface IDownloadDatabase {

    boolean saveDownloadRecord(String key, DownloadRecord record);

    @Nullable DownloadRecord loadDownloadRecord(String key);
}
