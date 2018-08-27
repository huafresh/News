package com.example.hua.framework.download;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import dalvik.annotation.TestTarget;
import junitx.util.PrivateAccessor;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DownloadTaskTest {

    @Test
    public void testStatic() throws Exception {
        String result = null;
        try {
            Context appContext = InstrumentationRegistry.getTargetContext();
            String dirPath = appContext.getCacheDir().getAbsolutePath();
             result = (String) PrivateAccessor.invoke(DownloadTask.class, "generateFileName",
                    new Class[]{String.class, String.class},
                    new Object[]{dirPath, "test_rename.txt"});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Assert.assertEquals("test_rename(2).txt", result);
    }

}
