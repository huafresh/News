package com.example.hua.framework.download;

import com.example.hua.framework.download.core.DownloadListener;
import com.example.hua.framework.download.core.DownloadRecord;
import com.example.hua.framework.download.core.DownloadRequest;
import com.example.hua.framework.download.core.DownloadStatus;
import com.example.hua.framework.download.core.IDownloadDatabase;
import com.example.hua.framework.download.core.IDownloadManager;
import com.example.hua.framework.download.core.IDownloadNetConnection;
import com.example.hua.framework.download.core.IDownloadTask;
import com.example.hua.framework.download.core.INetworkObserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;

/**
 * 下载任务，执行下载，并回调监听和存储下载记录等。
 * Created by hua on 2018/8/4.
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
class DownloadTask implements IDownloadTask, INetworkObserver, Runnable {

    private static final int RETRY_MAX_TIMES = 2;
    private static final long REFRESH_TIME_PERIOD = 500; //ms
    private static final int BUFF_SIZE = 1024 * 4;
    private IDownloadManager downloadManager;
    private DownloadRequest request;
    private String url;
    private String taskId;
    private DownloadRecord record;
    private DownloadListener listener;
    private IDownloadDatabase downloadDatabase;
    private IDownloadNetConnection netConnection;
    private ExecutorService executorService;
    private long lastRefreshTime = 0L;
    private long downloadedSize;
    private final Object lock = new Object();
    private boolean pauseFlag = false;
    private boolean deleteFlag = false;
    private boolean executed = false;
    boolean pauseByUser = false;

    DownloadTask(IDownloadManager downloadManager,
                 DownloadRequest request,
                 DownloadListener listener,
                 IDownloadDatabase downloadDatabase,
                 IDownloadNetConnection netConnection,
                 DownloadRecord record,
                 ExecutorService executorService) {
        this.downloadManager = downloadManager;
        this.request = request;
        this.url = request.getUrl();
        this.netConnection = new OkHttpDownloadConnection();
        this.lastRefreshTime = System.currentTimeMillis();
        this.downloadedSize = record.getDownloadedSize();
        this.listener = listener;
        this.downloadDatabase = downloadDatabase;
        this.netConnection = netConnection;
        this.record = record;
        this.taskId = record.getId();
        this.executorService = executorService;
    }

    @Override
    public void run() {
        int tryTimes = -1;
        while (true) {
            InputStream netInput = null;
            RandomAccessFile raf = null;
            try {
                tryTimes++;

                checkDownloadedFile();

                //下载连接
                netInput = netConnection.connect(request, record);
                if (netInput == null) {
                    Thread.sleep(1000);
                    if (tryTimes > RETRY_MAX_TIMES) {
                        throw new IOException("网络连接异常");
                    }
                    continue;
                }

                record.setStatus(DownloadStatus.DOWNLOADING);
                raf = new RandomAccessFile(record.getSavePath(), "rw");
                FileChannel fileChannel = raf.getChannel();
                MappedByteBuffer outBuff = fileChannel.map(FileChannel.MapMode.READ_WRITE,
                        record.getDownloadedSize(), record.getTotalSize());

                byte[] buff = new byte[BUFF_SIZE];
                int len = -1;
                while (!deleteFlag &&
                        !pauseFlag &&
                        (len = netInput.read(buff, 0, buff.length)) != -1) {
                    outBuff.put(buff, 0, len);
                    downloadedSize += len;
                    if (System.currentTimeMillis() - lastRefreshTime > REFRESH_TIME_PERIOD) {
                        record.setDownloadedSize(downloadedSize);
                        lastRefreshTime = System.currentTimeMillis();
                        notifyProgressUpdate();
                    }
                }

                if (pauseFlag) {
                    record.setStatus(DownloadStatus.PAUSED);
                    downloadDatabase.saveDownloadRecord(taskId, record);
                    lock.wait();
                } else if (deleteFlag) {
                    break;
                } else {
                    //finished
                    record.setStatus(DownloadStatus.FINISHED);
                    notifyFinished();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (tryTimes > RETRY_MAX_TIMES) {
                    notifyFailedWithMessage(e.getMessage());
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                notifyFailedWithMessage(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                notifyFailedWithMessage(e.getMessage());
                break;
            } finally {
                if (netInput != null) {
                    try {
                        netInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void notifyFailedWithMessage(String errorMessage) {
        record.setStatus(DownloadStatus.FAILED);
        record.setErrorMessage(errorMessage);
        downloadDatabase.saveDownloadRecord(taskId, record);
        if (listener != null) {
            listener.onDownloadFailed(record, record.getErrorMessage());
        }
    }


    private void checkDownloadedFile() throws IOException {
        String savePath = record.getSavePath();
        File downloadedFile = new File(savePath);
        if (!downloadedFile.exists()) {
            //创建下载文件
            downloadedFile.createNewFile();
        } else {
            //文件存在，则校验文件大小
            if (downloadedFile.length() != record.getDownloadedSize()) {
                downloadedFile.delete();
                downloadedFile.createNewFile();
            }
        }
    }


    void updateDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    String getDownloadUrl() {
        return url;
    }

    private void notifyFinished() {
        if (listener != null) {
            listener.onDownLoadFinished(record, record.getSavePath());
        }
    }

    private void notifyProgressUpdate() {
        if (listener != null) {
            listener.onProgressUpdate(record);
        }
    }


    @Override
    public void start() {
        DownloadStatus status = record.getStatus();
        switch (status) {
            case PENDING:
            case DOWNLOADING:
                //ignore
                break;
            case PAUSED:
                resume();
                break;
            case FINISHED:
                File downloadFile = new File(record.getSavePath());
                if (downloadFile.exists()) {
                    notifyFinished();
                } else {
                    //文件被删除，重新开始下载
                    startDownloadThread();
                }
                break;
            case FAILED:
                startDownloadThread();
                break;
            default:
                break;
        }
    }

    private void startDownloadThread() {
        executorService.submit(this);
        executed = true;
    }


    @Override
    public void pause() {
        pauseFlag = true;
    }

    void resume() {
        pauseFlag = false;
        lock.notifyAll();
    }

    @Override
    public void delete() {
        deleteFlag = true;
    }

    @Override
    public int networkType() {
        return request.getAllowNetworkType();
    }

    @Override
    public void onAvailable() {
        if (!executed) {
            startDownloadThread();
        } else if (!pauseByUser) {
            start();
        }
    }

    @Override
    public void onDisAvailable() {
        pause();
    }
}
