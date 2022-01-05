/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.zhy.lib_common.application.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ； ZY
 * @date : 2020/9/9
 * @describe :AndroidQ存储路径适配
 */
public class StorageUtils {

    public static final Object UPLOAD_PATH = "upload";

    private static final String CUSTOM_PACKAGE = "custom";
    private static final String DANDELION = "dandelion";

    public static final String PGY_FILE_DOWNLOAD_PATH = "Download" + File.separator + "zhy" + File.separator + "download";



    //获取外部Download路径

    /**
     * @param context 下载路劲
     *                context.getExternalFilesDir(Environment.Environment.DIRECTORY_DOWNLOADS)
     *                <p>
     *                视频文件
     *                context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
     *                <p>
     *                音频文件
     *                context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
     *                <p>
     *                图片文件
     *                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
     * @return
     */
    public static String getExternalStorageDownloadPath(Context context, String path) {
        return String.valueOf(context.getExternalFilesDir(TextUtils.isEmpty(path) ? Environment.DIRECTORY_DOWNLOADS : path));
    }

    /**
     * AndroidQ以下版本存储路劲
     *
     * @return
     */
    public static String getOldStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "oray" + File.separator + "pgy";
    }

    public static String getOldSmbDownloadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "Download"
                + File.separator + "dandelion" + File.separator + "download";
    }


    /**
     * AndroidQ存储路劲
     *
     * @return
     */
    public static String getStoragePath() {
        return BuildConfig.hasQ() ? getExternalStorageDownloadPath(BaseApplication.mContext, "")
                : getOldStoragePath();
    }


    /**
     * SMB文件下载路径
     *
     * @return
     */
    public static String getSmbDownloadStoragePath() {
        return BuildConfig.hasQ() ? getExternalStorageDownloadPath(BaseApplication.mContext, "smb/download")
                : getOldSmbDownloadPath();
    }

    /**
     * 存储图片文件
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean SavePictureFile(Context context, File file) {
        if (file == null) {
            return false;
        }
        Uri uri = insertFileIntoMediaStore(context, file, true);
        return SaveFile(context, file, uri);
    }

    /**
     * 存储视频文件
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean SaveVideoFile(Context context, File file) {
        if (file == null) {
            return false;
        }
        Uri uri = insertFileIntoMediaStore(context, file, false);
        return SaveFile(context, file, uri);
    }

    /**
     * 存储文件
     *
     * @param context
     * @param file
     * @param uri
     * @return
     */
    private static boolean SaveFile(Context context, File file, Uri uri) {
        if (uri == null) {
            LogUtils.e("url is null");
            return false;
        }
        LogUtils.i("SaveFile: " + file.getName());
        ContentResolver contentResolver = context.getContentResolver();

        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "w");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (parcelFileDescriptor == null) {
            LogUtils.e("parcelFileDescriptor is null");
            return false;
        }

        FileOutputStream outputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LogUtils.e(e.toString());
            try {
                outputStream.close();
            } catch (IOException ex) {
                LogUtils.e(ex.toString());
            }
            return false;
        }

        try {
            copy(inputStream, outputStream);
        } catch (IOException e) {
            LogUtils.e(e.toString());
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LogUtils.e(e.toString());
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                LogUtils.e(e.toString());
            }
        }

        return true;
    }

    //注意当文件比较大时该方法耗时会比较大
    private static void copy(InputStream ist, OutputStream ost) throws IOException {
        byte[] buffer = new byte[4096];
        int byteCount;
        while ((byteCount = ist.read(buffer)) != -1) {
            ost.write(buffer, 0, byteCount);
        }
        ost.flush();
    }

    //创建视频或图片的URI
    private static Uri insertFileIntoMediaStore(Context context, File file, boolean isPicture) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, isPicture ? "image/jpeg" : "video/mp4");
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, file.lastModified());
        }

        Uri uri = null;
        try {
            uri = context.getContentResolver().insert(
                    (isPicture ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    , contentValues
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
