package com.example.hua.framework.storage;

import android.content.Context;


/**
 * 数据存储管理。
 * 支持存储键值对数据到内存、数据库等等中。
 *
 * @author hua
 * @version 2017/6/5
 */
public class StorageManager {

    private MemoryStorage mMemoryStorage;
    private DiskStorage mDiskStorage;
    private static StorageManager sInstance;

    private StorageManager(Context context) {
        mMemoryStorage = new MemoryStorage();
        mDiskStorage = new DiskStorage(context.getApplicationContext());
    }

    public static StorageManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (StorageManager.class) {
                if (sInstance == null) {
                    sInstance = new StorageManager(context);
                }
            }
        }
        return sInstance;
    }


    /**
     * 存储到内存
     *
     * @param key  键值
     * @param data 数据
     */
    public void saveToMemory(String key, Object data) {
        mMemoryStorage.save(key, data);
    }

    /**
     * 加密存储到内存
     *
     * @param key     键值
     * @param content 数据
     */
    public void encryptSaveToMemory(String key, String content) {
        mMemoryStorage.saveEncrypt(key, content);
    }

    /**
     * 从内存获取数据
     *
     * @param key 键值
     * @return 数据
     */
    public Object getFromMemory(String key) {
        return mMemoryStorage.get(key);
    }

    /**
     * 从内存获取解密后的数据
     *
     * @param key 键值
     * @return 解密后的数据
     */
    public String decryptGetFromMemory(String key) {
        return mMemoryStorage.getDecrypt(key);
    }

    /**
     * 存储字符串到磁盘。
     *
     * @param key     键值
     * @param content 要存储的内容
     */
    public void saveToDisk(String key, String content) {
        mDiskStorage.save(key, content);
    }

    /**
     * 加密存储字符串到磁盘。
     *
     * @param key     键值
     * @param content 要存储的内容
     */
    public void encryptSaveToDisk(String key, String content) {
        mDiskStorage.saveEncrypt(key, content);
    }

    /**
     * 从本地获取字符串
     *
     * @param key 键值
     * @return 本地存储的字符串
     */
    public String getFromDisk(String key) {
        return mDiskStorage.get(key);
    }

    /**
     * 从本地获取解密后的字符串
     *
     * @param key 键值
     * @return 解密后的字符串
     */
    public String decryptGetFromDisk(String key) {
        return mDiskStorage.getDecrypt(key);
    }

}
