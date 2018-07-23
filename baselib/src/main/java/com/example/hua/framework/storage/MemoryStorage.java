package com.example.hua.framework.storage;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.example.hua.framework.utils.EncryptUtil;

/**
 * 存储数据到内存
 *
 * @author hua
 * @version 2017/10/21 11:26
 */

public class MemoryStorage implements IStorage<String, Object> {

    private static ArrayMap<String, Object> sArrayMap = new ArrayMap<>();

    @Override
    public boolean save(String key, Object value) {
        sArrayMap.put(key, value);
        return true;
    }

    @Override
    public Object get(String key) {
        return sArrayMap.get(key);
    }

    @Override
    public boolean saveEncrypt(String key, String cotent) {
        String encryptData = EncryptUtil.encryptByAES(cotent, key);
        if (!TextUtils.isEmpty(encryptData)) {
            return save(key, encryptData);
        }
        return false;
    }

    @Override
    public String getDecrypt(String key) {
        String encryptData = (String) get(key);
        return EncryptUtil.decryptByAES(encryptData, key);
    }

    @Override
    public boolean delete(String key) {
        return sArrayMap.remove(key) != null;
    }

    @Override
    public boolean clear() {
        sArrayMap.clear();
        return true;
    }
}
