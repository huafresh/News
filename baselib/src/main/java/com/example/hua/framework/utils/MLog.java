package com.example.hua.framework.utils;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MLog {
    /**
     * 是否输出日志
     */
    public static Boolean LOG_OUT = true;
    /**
     * 是否写入目录
     */
    public static Boolean LOG_TO_FILE = false;
    private static final int LENGTH = 5;
    private static final int V = 0x1;
    private static final int D = 0x2;
    private static final int I = 0x3;
    private static final int W = 0x4;
    private static final int E = 0x5;
    private static final int A = 0x6;
    private static final int JSON = 0x7;
    private static final int JSON_INDENT = 4;
    private static SimpleDateFormat sdf = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ", Locale.getDefault());
    /**
     * 行号分隔符
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DEFAULT_MESSAGE = "execute";
    private static Writer mWriter;
    private static String todayLogFile = "";

    private static final String LOG_TAG = "Hua_Chuang_Framework";

//    public static void d(String s) {
//        boolean isDeubg = "true".equals(ConfigManager.getInstance().getItemConfigValue("IS_DEBUG"));
//        if (isDeubg) {
//            android.util.Log.d(LOG_TAG, s);
//        }
//    }
//
//    public static void e(String s) {
//        boolean isDeubg = "true".equals(ConfigManager.getInstance().getItemConfigValue("IS_DEBUG"));
//        if (isDeubg) {
//            android.util.Log.e(LOG_TAG, s);
//        }
//    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, String msg) {
        printLog(V, tag, msg);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(byte[] bytes) throws UnsupportedEncodingException {
        printLog(D, null, new String(bytes, "UTF-8"));
    }

    public static void d(String tag, byte[] bytes) throws UnsupportedEncodingException {
        printLog(D, tag, new String(bytes, "UTF-8"));
    }

    public static void d(String tag, Object msg) {
        printLog(D, tag, msg);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object msg) {
        printLog(I, tag, msg);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object msg) {
        printLog(W, tag, msg);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object msg) {
        printLog(E, tag, msg);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object msg) {
        printLog(A, tag, msg);
    }


    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }


    public static int printMethodName(String TAG) {
        if (!LOG_OUT) {
            return -1;
        }
        String msg = "";
        StackTraceElement info = LogInfo.getInfoInternal(LENGTH);
        if (info != null) {
            msg = info.getMethodName() + " # Line " + info.getLineNumber();
        }

        return Log.i(TAG, msg);

    }

    public static int printStackTrace(String TAG) {
        if (!LOG_OUT) {
            return -1;
        }

        StackTraceElement[] stackTraceElements = new Exception()
                .getStackTrace();

        if (stackTraceElements != null) {
            Log.d(TAG, "printStackTrace:");
            for (int i = 1; i < stackTraceElements.length; i++) {
                Log.d(TAG, stackTraceElements[i].toString());
            }
        }

        return 0;
    }


    private static synchronized void writeLog2SD(String logTag, String tag, String msg) {
        if (LOG_TO_FILE) {
            try {
                mWriter.write(sdf.format(new Date()));
                mWriter.write(logTag + "/" + tag);
                mWriter.write("(" + Thread.currentThread().getId() + ") : ");
                mWriter.write(msg);
                mWriter.write("\n");
                mWriter.flush();
            } catch (IOException var6) {
                if (mWriter != null) {
                    try {
                        mWriter.close();
                        mWriter = null;
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }
                }
            }
        }

    }


    public static File createLogFile(String fileName) {
        File logFile = new File(fileName);
        return logFile;
    }


    private static void printLog(int type, String tagStr, Object objectMsg) {
        String msg;
        if (!LOG_OUT) {
            return;
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int index = 4;
        String className = stackTraceElements[index].getFileName();
        String methodName = stackTraceElements[index].getMethodName();
        int lineNumber = stackTraceElements[index].getLineNumber();
        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        StringBuilder builder = new StringBuilder();
        builder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ]");
        if (objectMsg == null) {
            msg = "log with null object";
        } else {
            msg = objectMsg.toString();
        }
        if (msg != null && type != JSON) {
            builder.append(msg);
        }
        String logStr = builder.toString();
        switch (type) {

            case V:
                Log.v(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("v", tag, msg);
                }
                break;
            case D:
                Log.d(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("d", tag, msg);
                }
                break;
            case I:
                Log.i(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("i", tag, msg);
                }
                break;
            case W:
                Log.w(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("w", tag, msg);
                }
                break;
            case E:
                Log.e(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("e", tag, msg);
                }
                break;
            case A:
                Log.d(tag, logStr);
                if (LOG_TO_FILE) {
                    writeLog2SD("a", tag, msg);
                }
                break;
            case JSON: {
                if (TextUtils.isEmpty(msg)) {
                    Log.d(tag, "Empty or null json content");
                    return;
                }
                String message = null;
                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    e(tag, e.getCause().getMessage() + "\n" + msg);
                    return;
                }
                printLine(tag, true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("|| ").append(line).append(LINE_SEPARATOR);
                }
                Log.d(tag, jsonContent.toString());
                if (LOG_TO_FILE) {
                    writeLog2SD("json", tag, jsonContent.toString());
                }
                printLine(tag, false);

            }
            break;
            default:
                break;
        }
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    public static void errorLog(Exception e) {
        if (e == null) {
            return;
        }

        printLog(E, null, e.getLocalizedMessage());
    }

    public static class LogInfo {
        private static final int LENGTH = 5;

        private static final boolean DEBUG = true;

        enum InfoKind {
            FILE_NAME,
            METHOD_NAME,
            CLASS_NAME,
            LINE_NUM
        }

        public static String getFileName() {
            return getInfo(InfoKind.FILE_NAME);
        }

        public static String getMethodName() {
            return getInfo(InfoKind.METHOD_NAME);
        }

        public static String getClassName() {
            return getInfo(InfoKind.CLASS_NAME);
        }

        public static String getLineNumber() {
            return getInfo(InfoKind.LINE_NUM);
        }

        private static String getInfo(InfoKind kind) {
            String ret = "";

            if (!DEBUG) {
                return ret;
            }

            StackTraceElement[] stackTraceElements = Thread.currentThread()
                    .getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length >= LENGTH) {
                StackTraceElement stackTraceElement = stackTraceElements[LENGTH - 1];
                switch (kind) {
                    case FILE_NAME:
                        ret = stackTraceElement.getFileName();
                        break;
                    case METHOD_NAME:
                        ret = stackTraceElement.getMethodName();
                        break;
                    case CLASS_NAME:
                        ret = stackTraceElement.getClassName();
                        break;
                    case LINE_NUM:
                        ret = Integer.toString(stackTraceElement.getLineNumber());
                        break;

                    default:
                        break;
                }
            }
            return ret;
        }

        static StackTraceElement getInfoInternal(int length) {
            StackTraceElement ret = null;

            if (!DEBUG) {
                return ret;
            }

            StackTraceElement[] stackTraceElements = Thread.currentThread()
                    .getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length >= length) {
                ret = stackTraceElements[length - 1];

            }
            return ret;
        }

    }

}