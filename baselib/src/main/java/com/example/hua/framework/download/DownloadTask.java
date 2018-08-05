package com.example.hua.framework.download;

import com.example.hua.framework.download.core.DownloadListener;
import com.example.hua.framework.download.core.DownloadRecord;
import com.example.hua.framework.download.core.DownloadRequest;
import com.example.hua.framework.download.core.DownloadStatus;
import com.example.hua.framework.download.core.IDownloadDatabase;
import com.example.hua.framework.download.core.IDownloadManager;
import com.example.hua.framework.download.core.IDownloadNetConnection;
import com.example.hua.framework.download.core.IDownloadTask;

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
class DownloadTask implements IDownloadTask, INetworkTypeTask {

    private static final long REFRESH_TIME = 500; //ms
    private static final int RETRY_TIMES = 1;
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
    private boolean cancelFlag = false;
    private boolean executed = false;
    private boolean pauseByser = false;

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
        int times = 0;
        while (true) {
            InputStream netInput = null;
            RandomAccessFile raf = null;
            try {
                times++;
                record.setStatus(DownloadStatus.DOWNLOADING);
                netInput = netConnection.connect(request, record);
                checkDownloadedFile();

                raf = new RandomAccessFile(record.getSavePath(), "rw");
                FileChannel fileChannel = raf.getChannel();
                MappedByteBuffer outBuff = fileChannel.map(FileChannel.MapMode.READ_WRITE,
                        record.getDownloadedSize(), record.getTotalSize());

                byte[] buff = new byte[1024 * 4];
                int len = -1;
                while (!cancelFlag && !pauseFlag &&
                        (len = netInput.read(buff, 0, buff.length)) != -1) {
                    outBuff.put(buff, 0, len);
                    downloadedSize += len;
                    if (System.currentTimeMillis() - lastRefreshTime > REFRESH_TIME) {
                        record.setDownloadedSize(downloadedSize);
                        lastRefreshTime = System.currentTimeMillis();
                        notifyProgressUpdate();
                    }
                }

                if (pauseFlag) {
                    record.setStatus(DownloadStatus.PAUSED);
                    downloadDatabase.saveDownloadRecord(taskId, record);
                    lock.wait();
                } else if (cancelFlag) {
                    record.setStatus(DownloadStatus.FAILED);
                    record.setErrorMessage("取消下载");
                    downloadDatabase.saveDownloadRecord(taskId, record);
                    notifyFailed();
                } else {
                    //finished
                    record.setStatus(DownloadStatus.FINISHED);
                    notifyFinished();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (times > RETRY_TIMES) {
                    record.setStatus(DownloadStatus.FAILED);
                    record.setErrorMessage(e.getMessage());
                    downloadDatabase.saveDownloadRecord(taskId, record);
                    notifyFailed();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                record.setStatus(DownloadStatus.FAILED);
                record.setErrorMessage(e.getMessage());
                downloadDatabase.saveDownloadRecord(taskId, record);
                notifyFailed();
            } catch (Exception e) {
                e.printStackTrace();
                record.setStatus(DownloadStatus.FAILED);
                record.setErrorMessage(e.getMessage());
                downloadDatabase.saveDownloadRecord(taskId, record);
                notifyFailed();
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


    private void checkDownloadedFile() throws IOException {
        String savePath = record.getSavePath();
        File downloadedFile = new File(savePath);
        if (!downloadedFile.exists()) {
            //创建下载文件
            downloadedFile.createNewFile();
        } else if (record.getStatus() == DownloadStatus.PENDING) {
            //说明原来存在同名文件
            StringBuilder nameBuilder = new StringBuilder(record.getName());
            int indexDot = nameBuilder.indexOf(".");
            nameBuilder.insert(indexDot - 1, "(bat)");
            record.setName(nameBuilder.toString());
        } else {
            //下载已经开始，且文件存在，则校验文件大小
            if (downloadedFile.length() != record.getDownloadedSize()) {
                downloadedFile.delete();
                downloadedFile.createNewFile();
            }
        }
    }

    void updateDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    String  getDownloadUrl(){
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

    private void notifyFailed() {
        if (listener != null) {
            listener.onDownloadFailed(record, record.getErrorMessage());
        }
    }

    void start() {
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
                notifyFinished();
                break;
            case FAILED:
                notifyFailed();
                break;
            default:
                break;
        }
    }

    @Override
    public void pause(boolean byUser) {
        pauseFlag = true;
    }

    @Override
    public void resume() {
        pauseFlag = false;
        lock.notifyAll();
    }

    @Override
    public void cancel() {
        cancelFlag = true;
    }

    @Override
    public int getAllowNetworkType() {
        return request.getAllowNetworkType();
    }

    @Override
    public void onAvailable() {
        if (!executed) {
            executorService.submit(this);
        } else if (!pauseFlag) {
            resume();
        }
    }

    @Override
    public void onDisAvailable() {
        pause(false);
    }
}
