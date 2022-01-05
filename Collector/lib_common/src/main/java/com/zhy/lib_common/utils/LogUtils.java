/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-12-19 上午10:20
 *  FileName:LogUtils.java
 */

package com.zhy.lib_common.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log。
 */
public class LogUtils {
    private static final String customTagPrefix = "PgyClient";
    private static final boolean isSaveLog = false;                                  // 是否把保存日志到SD卡中
    private static final String ROOT = StorageUtils.getStoragePath() + "/pgyvpn/";                                  // SD卡中的根目录
    private static final String PATH_LOG_INFO = ROOT + "log/";
    private static final String SPIT = " | ";

    private LogUtils() {
    }


    private static boolean allowV = false;
    private static boolean allowD = false;
    private static boolean allowI = true;
    private static boolean allowW = true;
    private static boolean allowE = true;

    @SuppressLint("DefaultLocale")
    private static String generateTag(StackTraceElement caller) {

//        String tag = "%s.%s(Line:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        int lineNumber = caller.getLineNumber();
        String tag = lineNumber < 100 ? "(Line: %d)" : "(Line:%d)";
        tag = String.format(tag, /*callerClazzName,caller.getMethodName(),*/
                lineNumber);
        tag = customTagPrefix + ":" + tag;
        return tag;
    }

    /**
     * 返回的数据格式是<ol><li>edqwiuyhe</li><li>diuqwh</li></ol>
     * 需要转换成edqwiuyhe/ndiuqwh
     *
     * @param logs
     * @return
     */
    public static String formatLogs(String logs) {
        if (null != logs && logs.length() > 0) {
            StringBuilder buffer = new StringBuilder();
            logs = logs.replaceAll("<ol>", "").replaceAll("</ol>", "")
                    .replaceAll("</li>", "");
            String[] log = logs.split("<li>");
            for (String s : log) {
                buffer.append(s).append("\n");
            }

            buffer.subSequence(2, buffer.length() - 2);
            return buffer.toString();
        } else {
            return "";
        }
    }


    /**
     * 自定义的logger
     */
    private static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);
    }

    public static void d(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void e(String TAG, String content) {
        if (!allowE) return;
        if (TextUtils.isEmpty(content)) content = "no error msg !";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag + SPIT + TAG, content);
        } else {
            Log.e(tag + SPIT + TAG, content);
        }
        if (isSaveLog) {
            point(PATH_LOG_INFO, tag + SPIT + TAG, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;
        if (TextUtils.isEmpty(content)) content = "no error msg !";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void i(String content) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void i(String TAG, String content) {
        if (!allowI) return;
        if (TextUtils.isEmpty(content)) content = "no msg !";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag + SPIT + TAG, content);
        } else {
            Log.i(tag + SPIT + TAG, content);
        }

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag + SPIT + TAG, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }

    }

    public static void v(String content) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }


    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.SIMPLIFIED_CHINESE);
            path = path + "log.txt";
            String time = dateFormat.format(date);
            File file = new File(path);
            if (file.exists() && file.length() > 10 * 1024 * 1024)  //Log日志大于10m 删除重新创建
                file.delete();
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        if (null == formatter) return null;
        return formatter.format(msg, args);
    }

    private static boolean isSDAva() {
        return BuildConfig.hasQ() || Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists();
    }

}
