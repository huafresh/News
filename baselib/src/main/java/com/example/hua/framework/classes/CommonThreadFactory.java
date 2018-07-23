package com.example.hua.framework.classes;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author hua
 * @version 2017/11/27 14:34
 */
@Deprecated
public class CommonThreadFactory implements ThreadFactory {
    private String name;

    public CommonThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, name);
    }
}
