package com.example.hua.framework.download.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 下载任务
 * Created by hua on 2018/8/4.
 */

public interface IDownloadTask {

    void start();

    void pause();

    void delete();
}
