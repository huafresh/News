package com.example.hua.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具。
 * Android中的文件存储解析可以参考：
 * http://blog.csdn.net/Holmofy/article/details/53439025
 *
 * @author hua
 * @date 2017/7/13
 */

public class FileUtil {

    /**
     * 判断sdcard是否存在
     *
     * @return
     */
    public static boolean isSdcardExist() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 拷贝文件。拷贝后文件名称一致.
     * 支持拷贝文件和目录.
     * 如果目标文件存在，则会覆盖内容
     *
     * @param srcPath     源文件全路径
     * @param destDirPath 目标文件夹全路径
     * @return 是否成功
     */
    public static boolean copyFile(String srcPath, String destDirPath) {
        boolean isSuccess = false;

        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        try {
            File srcFile = new File(srcPath);
            File destDirFile = new File(destDirPath);
            if (!srcFile.exists() || !destDirFile.exists()) {
                return isSuccess;
            }

            String srcFileName = srcFile.getName();
            File destFile = new File(destDirFile.getAbsolutePath() + "/" + srcFileName);
            if (!destFile.exists()) {
                if (srcFile.isFile()) {
                    if (!destFile.createNewFile()) {
                        return isSuccess;
                    }
                } else {
                    if (!destDirFile.mkdir()) {
                        return isSuccess;
                    }
                }
            }

            fileIn = new FileInputStream(srcFile);
            fileOut = new FileOutputStream(destFile);

            isSuccess = readFromSteamToStream(fileIn, fileOut);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    /**
     * 从一个流中读取数据到另一个流。此方法已经处理了流的关闭
     *
     * @param in 输入流
     * @param os 输出流
     * @return 是否成功
     */
    public static boolean readFromSteamToStream(InputStream in, OutputStream os) {
        boolean isSuccess = false;
        if (in == null || os == null) {
            return false;
        }
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 根据文件的路径解析得到文件的扩展名
     *
     * @param path 文件的路径
     * @return 文件扩展名
     */
    public static String getFileNameExt(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取给定路径的File对象，如果文件不存在或者上级目录不存在，则自动创建
     *
     * @param path 文件的全路径
     * @return 给定路径的File对象
     */
    @Nullable
    public static File getFile(String path) {
        File file = new File(path);
        File dirFile = file.getParentFile();

        //按需创建上级目录
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }

        //按需创建文件
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return file;
    }

    /**
     * 存储bitmap到本地
     *
     * @param bitmap 要保存的Bitmap
     * @param path   存储的绝对路径
     * @return 是否保存成功
     */
    public static boolean saveBitmap(Bitmap bitmap, File path, int quality) {
        boolean isSuccess = false;
        if (bitmap != null && path != null) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isSuccess;
    }

}
