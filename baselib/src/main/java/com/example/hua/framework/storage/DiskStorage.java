package com.example.hua.framework.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.hua.framework.utils.EncryptUtil;

/**
 * 持久化存储键值对形式的数据
 *
 * @author hua
 * @version 2017/10/22 11:21
 */

public class DiskStorage implements IStorage<String, String> {

    private DBOpenHelper dbOpenHelper;
    private final String tableName;
    private String whereClause;

    public DiskStorage(Context context) {
        dbOpenHelper = new DBOpenHelper(context, DBOpenHelper.START_VERSION);
        tableName = MapTable.TABLE_NAME;
        whereClause = MapTable.Column.COLUMN_KEY + "=?";
    }

    @Override
    public boolean save(String key, String value) {
        SQLiteDatabase writableDatabase = null;
        try {
            writableDatabase = dbOpenHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(MapTable.Column.COLUMN_KEY, key);
        values.put(MapTable.Column.COLUMN_VALUE, value);

        String existValue = get(key);
        boolean isSuccess = false;
        if (!TextUtils.isEmpty(existValue)) {
            int ret = writableDatabase.update(tableName, values, whereClause, new String[]{key});
            isSuccess = ret > 0;
        } else {
            long ret = writableDatabase.insert(tableName, null, values);
            isSuccess = ret != -1;
        }

        return isSuccess;
    }

    @Override
    public String get(String key) {
        SQLiteDatabase readableDatabase = null;
        try {
            readableDatabase = dbOpenHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Cursor cursor = readableDatabase.query(MapTable.TABLE_NAME, new String[]{MapTable.Column.COLUMN_VALUE},
                whereClause, new String[]{key}, null, null, null);

        String value = null;
        if (cursor.moveToFirst()) {
            value = cursor.getString(0);
        }
        cursor.close();

        return value;
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
        String encryptData = get(key);
        return EncryptUtil.decryptByAES(encryptData, key);
    }

    @Override
    public boolean delete(String key) {
        SQLiteDatabase writableDatabase = null;
        try {
            writableDatabase = dbOpenHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        int ret = writableDatabase.delete(tableName, whereClause, new String[]{key});

        return ret > 0;
    }

    @Override
    public boolean clear() {
        SQLiteDatabase readableDatabase = null;
        try {
            readableDatabase = dbOpenHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Cursor cursor = readableDatabase.query(tableName, null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex(MapTable.Column.COLUMN_KEY));
            delete(key);
        }

        cursor.close();

        return true;
    }


}
