package com.example.hua.framework.download;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.hua.framework.download.core.DownloadListener;
import com.example.hua.framework.download.core.DownloadRecord;
import com.example.hua.framework.download.core.DownloadRequest;
import com.example.hua.framework.download.core.DownloadStatus;
import com.example.hua.framework.download.core.IDownloadDatabase;
import com.example.hua.framework.download.core.IDownloadManager;
import com.example.hua.framework.download.core.IdCreator;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 下载管理实现
 * Created by hua on 2018/8/4.
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
class DownloadManagerImpl implements IDownloadManager {

    private ExecutorService executorService;
    private HashMap<String, DownloadTask> taskMap;
    private static IDownloadManager downloadManager;
    private Context context;
    private IdCreator defaultIdCreator;
    private IDownloadDatabase downloadDatabase;
    private String defaultSaveDir;

    private DownloadManagerImpl(Context context) {
        this.context = context;
        executorService = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE,
                5, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new DownloadThreadFactory());
        this.taskMap = new HashMap<>();
        this.defaultIdCreator = new Md5IdCreator();
        this.downloadDatabase = new DefaultDownloadDatabase(context);
        this.defaultSaveDir = getDefaultSaveDir(context);
    }

    private static String getDefaultSaveDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = context.getExternalFilesDir(null);
            if (file != null) {
                return file.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * obtain a {@link IDownloadManager} instance
     *
     * @param context Context
     * @return {@link IDownloadManager}
     */
    public static IDownloadManager getInstance(Context context) {
        if (downloadManager == null) {
            synchronized (DownloadManagerImpl.class) {
                if (downloadManager == null) {
                    downloadManager = new DownloadManagerImpl(context);
                }
            }
        }
        return downloadManager;
    }


    @Override
    public String start(String url, DownloadListener listener) {
        return start(DownloadRequest.newRequest(url), listener);
    }

    @Override
    public String start(DownloadRequest request, DownloadListener listener) {
        checkRequest(request);

        String id = defaultIdCreator.create(request);

        DownloadRecord record = downloadDatabase.loadDownloadRecord(id);
        if (record == null) {
            record = createDownloadRecord(request, id);
            record.setStatus(DownloadStatus.PENDING);
            downloadDatabase.saveDownloadRecord(id, record);
        }

        DownloadTask task = taskMap.get(id);
        if (task == null) {
            task = buildDownloadTask(request, new WeakDownloadListener(listener), record);
            //将task加入队列，在网络状态允许时自动开始 or 暂停下载
            NetworkTypeUtil.get(context).add(task);
            taskMap.put(id, task);
        } else {
            task.updateDownloadListener(new WeakDownloadListener(listener));
        }

        task.start();

        return id;
    }

    private DownloadRecord createDownloadRecord(DownloadRequest request, String id) {
        DownloadRecord record;
        record = new DownloadRecord();
        record.setId(id);
        String saveDir = request.getSaveDir();
        if (TextUtils.isEmpty(saveDir)) {
            saveDir = defaultSaveDir;
        }
        record.setDirPath(saveDir);
        String name = record.getName();
        if (TextUtils.isEmpty(name)) {
            name = getNameFromUrl(request.getUrl());
        }
        record.setName(name);
        return record;
    }

    private static String getNameFromUrl(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index);
    }

    private DownloadTask buildDownloadTask(DownloadRequest request,
                                           DownloadListener listener,
                                           DownloadRecord record) {
        return new DownloadTask(
                this,
                request,
                new WeakDownloadListener(listener),
                downloadDatabase,
                new OkHttpDownloadConnection(),
                record,
                executorService);
    }

    private static void checkRequest(DownloadRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("download request can not be null");
        }

        if (TextUtils.isEmpty(request.getUrl())) {
            throw new IllegalArgumentException("download url can not be empty");
        }

        if (!request.getUrl().startsWith("http://") && !request.getUrl().startsWith("https://")) {
            throw new IllegalArgumentException("download url must be http protocol");
        }
    }

    @Override
    public boolean pause(String id) throws UnsupportedOperationException {
        DownloadRecord record = downloadDatabase.loadDownloadRecord(id);
        if (record == null) {
            throw new UnsupportedOperationException("please call start first");
        }

        DownloadTask task = taskMap.get(id);
        if (task != null) {
            task.pause(true);
            return true;
        }

        return false;
    }

    @Override
    public boolean resume(String id) throws UnsupportedOperationException {
        DownloadRecord record = downloadDatabase.loadDownloadRecord(id);
        if (record == null) {
            throw new UnsupportedOperationException("please call start first");
        }

        DownloadTask task = taskMap.get(id);
        if (task != null) {
            task.resume();
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public DownloadStatus getDownloadStatus(String id) throws UnsupportedOperationException {
        DownloadRecord record = queryDownloadRecord(id);
        return record.getStatus();
    }

    @Nullable
    @Override
    public String getFilePath(String id) throws UnsupportedOperationException {
        DownloadRecord record = queryDownloadRecord(id);
        return record.getSavePath();
    }

    @Override
    public DownloadRecord queryDownloadRecord(String id) throws UnsupportedOperationException {
        DownloadRecord record = downloadDatabase.loadDownloadRecord(id);
        if (record == null) {
            throw new UnsupportedOperationException("please call start first");
        }
        return record;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean delete(String... ids) throws UnsupportedOperationException {
        for (String id : ids) {
            DownloadRecord record = downloadDatabase.loadDownloadRecord(id);
            if (record == null) {
                throw new UnsupportedOperationException("please call start first");
            }
        }

        for (String id : ids) {
            DownloadRecord record = downloadDatabase.loadDownloadRecord(id);

            //清除内存中正在运行的task
            DownloadTask task = taskMap.remove(id);
            if (task != null) {
                NetworkTypeUtil.get(context)
                        .remove(task);
                task.cancel();
            }

            //清除下载的文件
            File file = new File(record.getSavePath());
            file.delete();
        }

        return true;
    }

    private static class DownloadThreadFactory implements ThreadFactory {
        private static int count = 0;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            if (r instanceof DownloadTask) {
                String url = ((DownloadTask) r).getDownloadUrl();
                thread.setName("download thread#" + count + " url=" + url);
            } else {
                thread.setName("download thread#" + count);
            }

            count++;

            return thread;
        }
    }

    private static class WeakDownloadListener implements DownloadListener {
        private WeakReference<DownloadListener> listener;

        WeakDownloadListener(DownloadListener listener) {
            this.listener = new WeakReference<DownloadListener>(listener);
        }

        @Override
        public void onDownloadStarted(DownloadRequest request) {
            if (listener.get() != null) {
                listener.get().onDownloadStarted(request);
            }
        }

        @Override
        public void onProgressUpdate(DownloadRecord record) {
            if (listener.get() != null) {
                listener.get().onProgressUpdate(record);
            }
        }

        @Override
        public void onDownloadPaused(DownloadRecord record) {
            if (listener.get() != null) {
                listener.get().onDownloadPaused(record);
            }
        }

        @Override
        public void onDownloadResumed(DownloadRecord record) {
            if (listener.get() != null) {
                listener.get().onDownloadResumed(record);
            }
        }

        @Override
        public void onDownLoadFinished(DownloadRecord record, String filePath) {
            if (listener.get() != null) {
                listener.get().onDownLoadFinished(record, filePath);
            }
        }

        @Override
        public void onDownloadFailed(DownloadRecord record, String message) {
            if (listener.get() != null) {
                listener.get().onDownloadFailed(record, message);
            }
        }
    }

//    private static class DownloadTaskWrapper implements INetworkTypeTask{
//
//        private DownloadTask downloadTask;
//        private DownloadRequest request;
//
//        public DownloadTaskWrapper(DownloadTask downloadTask, DownloadRequest request) {
//            this.downloadTask = downloadTask;
//            this.request = request;
//        }
//
//        @Override
//        public int getAllowNetworkType() {
//            return request.getAllowNetworkType();
//        }
//
//        @Override
//        public void onAvailable() {
//
//        }
//
//        @Override
//        public void onDisAvailable() {
//
//        }
//    }

}
