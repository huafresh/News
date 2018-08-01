package com.example.hua.framework.download;

/**
 * download listener
 * Created by hua on 2018/7/29.
 */

public interface DownloadListener {

    /**
     * Notifies the start of the download.
     *
     * @param request the started download request
     */
    void onDownloadStarted(DownloadRequest request);

    /**
     * Notifies the progress of the download.
     *
     * @param record a {@link DownloadRecord} recorded everything related to this download.
     */
    void onProgressUpdate(DownloadRecord record);

    /**
     * Notifies the pause of the download.
     * This may be caused by a manual pause by the user or the network is not connected.
     *
     * @param record a {@link DownloadRecord} recorded everything related to this download.
     */
    void onDownloadPaused(DownloadRecord record);

    /**
     * Notifies the retrieve of the download.
     * This may be caused by a manual resume by the user or the network is reconnected.
     *
     * @param record a {@link DownloadRecord} recorded everything related to this download.
     */
    void onDownloadResumed(DownloadRecord record);

    /**
     * Notifies the finish of the download.
     *
     * @param record   a {@link DownloadRecord} recorded everything related to this download.
     * @param filePath the saved path of the downloaded file.
     */
    void onDownLoadFinished(DownloadRecord record, String filePath);

    /**
     * Notifies the fail of the download.
     * a failed download can be resume by call {@link DownloadManager#start}
     *
     * @param record  a {@link DownloadRecord} recorded everything related to this download.
     * @param message description of the reason for the failure
     */
    void onDownloadFailed(DownloadRecord record, String message);

}
