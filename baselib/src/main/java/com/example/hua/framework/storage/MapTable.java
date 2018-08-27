package com.example.hua.framework.storage;

import android.provider.BaseColumns;

import java.security.PublicKey;

/**
 * 键值对数据表，以数据库的形式持久化存储key-value形式的数据。
 * 注意key值得长度不能超过1000。
 *
 * @author hua
 * @version 2017/10/22 10:44
 */

public class MapTable {

    public static final String TABLE_NAME = "table_map";
    public static String CREATE_SQL;

    static {
        CREATE_SQL = "build table if not exists " + TABLE_NAME + "(" +
                Column._ID + " integer primary key autoincrement," +
                Column.COLUMN_KEY + " varchar(1000)," +
                Column.COLUMN_VALUE + " text)";
    }

    public interface Column extends BaseColumns {
        String COLUMN_KEY = "key";
        String COLUMN_VALUE = "value";
    }

}
