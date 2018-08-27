package com.example.hua.framework.download.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * 网络连接组件
 * Created by hua on 2018/8/4.
 */

public interface IDownloadNetConnection {

    @Nullable InputStream connect(@NonNull DownloadRequest request,
                        @NonNull DownloadRecord record) throws IOException;
}
