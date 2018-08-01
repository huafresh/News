package com.example.hua.framework.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 提供下载服务
 *
 * @author hua
 * @version 2018/5/3 20:07
 */
@Deprecated
public class DownloadService extends Service {

    private IBinder binder;
    private DownloadManager downloadManager;
    private File file;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
        downloadManager = (DownloadManager) getSystemService(Service.DOWNLOAD_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private long start(String url, String dirPath, String fileName) {
        return downloadManager.enqueue(buildRequest(url, dirPath, fileName));
    }

    private DownloadManager.Request buildRequest(String url, String dirPath, String name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if (TextUtils.isEmpty(dirPath)) {
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, name);
        } else {
            if (dirPath.endsWith(File.separator)) {
                file = new File(dirPath + name);
            } else {
                file = new File(dirPath + File.separator + name);
            }
            request.setDestinationUri(Uri.fromFile(file));
        }
        return request;
    }

    private class DownloadBinder extends Binder {

        private long start(String url, String dirPath, String fileName) {
            return DownloadService.this.start(url, dirPath, fileName);
        }

        public int getProgress(long downId) {
//            DownloadManager.COLUMN_ID
            return 0;
        }
    }

    public static void start(Context context, String url, IDownloadListener listener) {
        start(context, url, null, listener);
    }

    public static void start(Context context, String url, String fileName, IDownloadListener listener) {
        start(context, url, null, fileName, listener);
    }

    /**
     * 启动一个下载任务
     *
     * @param context  Context
     * @param url      下载地址
     * @param dirPath  文件存储目录，必须是外部存储器的目录
     * @param fileName 文件的名称，包含后缀
     * @param listener 监听下载过程
     */
    public static void start(Context context, String url, String dirPath, String fileName, IDownloadListener listener) {
        Intent intent = new Intent(context, DownloadService.class);
        DownloadServiceConnection connection = new DownloadServiceConnection(listener, url, dirPath, fileName);
        // TODO: 2018/5/4 测试下同一个activity中多次绑定，onServiceConnected会不会走
        context.bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    private static class DownloadServiceConnection implements ServiceConnection {

        private static final String THREAD_DOWNLOAD_NAME = "thread-download";
        private static final int WHAT_PROGRESS = 0;
        private IDownloadListener listener;
        private String url;
        private String dirPath;
        private String fileName;
        private DownloadBinder binder;
        private ThreadPoolExecutor threadPoolExecutor;
        private Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_PROGRESS:
                        if (listener != null) {
                            listener.onUpdate(downId, msg.arg1);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        private long downId;

        public DownloadServiceConnection(IDownloadListener listener, String url,
                                         String dirPath, String fileName) {
            this.listener = listener;
            this.url = url;
            this.dirPath = dirPath;
            this.fileName = fileName;
            //创建轮询下载进度的线程池
            createThreadPool();
        }

        private void createThreadPool() {
            threadPoolExecutor = new ThreadPoolExecutor(
                    1,
                    1,
                    3,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread(r, THREAD_DOWNLOAD_NAME);
                }
            }
            );
        }

        @Override
        public void onServiceConnected(ComponentName comName, IBinder service) {
            binder = (DownloadBinder) service;
            if (binder != null) {
                downId = binder.start(url, dirPath, fileName);
                startProgressThread();
                if (listener != null) {
                    listener.onStart(downId);
                }

            }
        }

        private void startProgressThread() {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //将下载进度回调主线程
                    Message message = handler.obtainMessage();
                    message.arg1 = binder.getProgress(downId);
                    message.sendToTarget();

                    //继续轮询
                    threadPoolExecutor.execute(this);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        private static class DownloadRunnable implements Runnable {

            @Override
            public void run() {

            }
        }

    }

}
