package com.example.hua.framework.download;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.IntDef;

/**
 * 下载请求
 * Created by hua on 2018/7/29.
 */

public class DownloadRequest {

    public static final int NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    @IntDef({NETWORK_TYPE_WIFI, NETWORK_TYPE_MOBILE})
    public @interface NetworkType {
    }

    private Uri uri;
    private String url;
    private String saveDirPath;
    private String saveName;
    private @NetworkType
    int allowNetworkType;

    public static DownloadRequest newRequest(String url){
        return new DownloadRequest(url);
    }

    private DownloadRequest(String url) {
        this.url = url;
        this.uri = Uri.parse(url);
        this.allowNetworkType = NETWORK_TYPE_WIFI;
    }

    public DownloadRequest setUrl(String url) {
        this.url = url;
        this.uri = Uri.parse(url);
        return this;
    }

    public DownloadRequest setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
        return this;
    }

    public DownloadRequest setSaveName(String saveName) {
        this.saveName = saveName;
        return this;
    }

    public DownloadRequest setAllowNetworkType(int allowNetworkType) {
        this.allowNetworkType = allowNetworkType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public String getSaveName() {
        return saveName;
    }

    public int getAllowNetworkType() {
        return allowNetworkType;
    }
}
