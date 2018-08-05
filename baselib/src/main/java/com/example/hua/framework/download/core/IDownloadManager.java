package com.example.hua.framework.download.core;

import android.support.annotation.Nullable;

/**
 * 下载管理器抽象，对上层屏蔽下载管理的实现细节。
 * Created by hua on 2018/7/29.
 */

public interface IDownloadManager {

    /**
     * start the download.
     * this method will build a default {@link DownloadRequest},
     * and then call {@link #start(DownloadRequest, DownloadListener)}
     *
     * @param url      url for the download
     * @param listener callback
     * @return an ID for the download. This ID is used to make future
     * calls related to this download.
     */
    String start(String url, DownloadListener listener);

    /**
     * start the download.
     * if the download does't exist, this call will create a download and
     * return a unique identifier;
     * if the download is in {@link DownloadStatus#DOWNLOADING} or
     * {@link DownloadStatus#PENDING}state, this call will do nothing;
     * if the download is in {@link DownloadStatus#PAUSED} or
     * {@link DownloadStatus#FAILED}state, this call will resume it by
     * call {@link #resume(String)};
     * if the download is in{@link DownloadStatus#FINISHED} state,
     * this method will call {@link DownloadListener#onDownLoadFinished(DownloadRecord, String)} immediately.
     *
     * @param request  request for this download
     * @param listener callback
     * @return an ID for the download. This ID is used to make future
     * calls related to this download.
     */
    String start(DownloadRequest request, DownloadListener listener);

    /**
     * pause the download.
     * if the download does't exist, this call will throw {@link UnsupportedOperationException}
     * else, it will make this download into {@link DownloadStatus#PAUSED} state and stop downloading.
     *
     * @param id an ID for the download.
     * @return true success, false otherwise
     */
    boolean pause(String id) throws UnsupportedOperationException;

    /**
     * resume the download.
     * if the download does't exist, this call will throw {@link UnsupportedOperationException}
     * else, it will make this download into {@link DownloadStatus#DOWNLOADING} state and restart download
     *
     * @param id an ID for the download.
     * @return true success, false otherwise
     */
    boolean resume(String id) throws UnsupportedOperationException;

    /**
     * get the {@link DownloadStatus} related to this download.
     * if the download does't exist, this call will throw {@link UnsupportedOperationException}
     *
     * @param id an ID for the download.
     * @return the download status
     */
    @Nullable
    DownloadStatus getDownloadStatus(String id) throws UnsupportedOperationException;

    /**
     * get the saved path of the file which download by this download.
     * if the download does't exist, this call will
     * throw {@link UnsupportedOperationException}
     * for more information about this download, call {@link #queryDownloadRecord(String)}
     *
     * @param id an ID for the download.
     * @return the path of the file
     */
    @Nullable
    String getFilePath(String id) throws UnsupportedOperationException;

    /**
     * query download record.
     * if the download does't exist, this call will
     * throw {@link UnsupportedOperationException}
     *
     * @param id an ID for the download.
     * @return {@link DownloadRecord}
     */
    DownloadRecord queryDownloadRecord(String id) throws UnsupportedOperationException;

    /**
     * delete the download.
     * if one of the download does't exist, this call will
     * throw {@link UnsupportedOperationException}, and none of download will be deleted,
     * otherwise, it will delete all downloads related to ids.
     * Each download will no longer be accessible through the download manager.
     * attention: this call will delete anything, include the file which download by this download.
     *
     * @param ids IDs
     * @return true success, false otherwise
     */
    boolean delete(String... ids) throws UnsupportedOperationException;
}
