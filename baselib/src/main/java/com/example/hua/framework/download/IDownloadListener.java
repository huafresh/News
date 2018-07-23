package com.example.hua.framework.download;

import android.support.annotation.MainThread;

/**
 * @author hua
 * @version 2018/5/3 20:08
 */

public interface IDownloadListener {

    @MainThread
    void onStart(long id);

    @MainThread
    void onUpdate(long id, int progress);

    @MainThread
    void onCanceled(long id);

    @MainThread
    void onFailed(long id, Exception e);

    @MainThread
    void onCompleted(long id, String path);
}
