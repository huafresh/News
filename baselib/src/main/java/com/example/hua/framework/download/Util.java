package com.example.hua.framework.download;

import java.io.File;

/**
 * 工具类
 *
 * @author hua
 * @version 1.0
 * @since 2018/8/19
 */
class Util {

    /**
     * 如果目标文件存在，则以（1），（2）等后缀区别命名
     */
    static String generateFileName(String dirPath, String targetName) {
        File dirFile = new File(dirPath);
        int count = 0;
        for (File file : dirFile.listFiles()) {
            String fileName = file.getName();
            boolean isNameRepeat = fileName.equals(targetName) ||
                    fileName.equals(insertRepeatCount(targetName, count));
            if (file.isFile() && isNameRepeat) {
                count++;
            }
        }
        if (count > 0) {
            return insertRepeatCount(targetName, count);
        } else {
            return targetName;
        }
    }

    private static String insertRepeatCount(String name, int count) {
        return name.replace(".", "(" + count + ").");
    }

}
