package com.example.hua.framework.download;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.hua.framework.download.core.DownloadRecord;
import com.example.hua.framework.download.core.DownloadRequest;
import com.example.hua.framework.download.core.IDownloadNetConnection;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 使用OkHttp下载
 * Created by hua on 2018/8/4.
 */

class OkHttpDownloadConnection implements IDownloadNetConnection {
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public @Nullable InputStream connect(@NonNull DownloadRequest request,
                        @NonNull DownloadRecord record) throws IOException {
        InputStream input = null;
        long downloadedSize = record.getDownloadedSize();
        Request okHttpRequest = new Request.Builder()
                .url(request.getUrl())
                .header("range", "bytes=" + downloadedSize + "-")
                .build();

        Call call = okHttpClient.newCall(okHttpRequest);
        ResponseBody body = call.execute().body();
        if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null) {
                record.setMimeType(mediaType.toString());
            }
            record.setTotalSize(body.contentLength());
            input = body.byteStream();
        }

        return input;
    }
}
