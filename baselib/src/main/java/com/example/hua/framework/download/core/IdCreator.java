package com.example.hua.framework.download.core;

/**
 * 生成downloadId
 * Created by hua on 2018/8/5.
 */

public interface IdCreator {

    String create(DownloadRequest request);
}
