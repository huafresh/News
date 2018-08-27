package com.example.hua.framework.download.core;

import android.support.annotation.Nullable;

/**
 * 持久化存储下载记录
 */

public interface IDownloadDatabase {

    boolean saveDownloadRecord(String key, DownloadRecord record);

    @Nullable DownloadRecord loadDownloadRecord(String key);

    boolean deleteDownloadRecord(String key);

}
