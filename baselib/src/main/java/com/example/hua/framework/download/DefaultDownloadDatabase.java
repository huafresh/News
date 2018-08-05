package com.example.hua.framework.download;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.hua.framework.download.core.DownloadRecord;
import com.example.hua.framework.download.core.IDownloadDatabase;
import com.example.hua.framework.json.JsonParseUtil;
import com.example.hua.framework.storage.StorageManager;

/**
 * 持久存储下载记录。
 * Created by hua on 2018/8/4.
 */

class DefaultDownloadDatabase implements IDownloadDatabase {

    private Context context;

    DefaultDownloadDatabase(Context context) {
        this.context = context;
    }

    @Override
    public boolean saveDownloadRecord(String key, DownloadRecord record) {
        String recordStr = JsonParseUtil.getInstance().parseObjectToString(record);
        StorageManager.getInstance(context).saveToDisk(key, recordStr);
        return true;
    }

    @Nullable
    @Override
    public DownloadRecord loadDownloadRecord(String key) {
        String recordStr = StorageManager.getInstance(context).getFromDisk(key);
        return JsonParseUtil.getInstance().parseJsonToObject(recordStr, DownloadRecord.class);
    }
}
