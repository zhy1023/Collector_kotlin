package com.zhy.lib_common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by qianwei on 2017/11/21.
 */

public class FileOperationUtils {

    private static final String TAG = FileOperationUtils.class.getSimpleName();

    public static void deleteAllFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) deleteAllFile(subFile);
            }
            file.delete();
        }
    }


    /**
     * 删除某个文件下后缀为的文件
     *
     * @param filePath   文件夹路径
     * @param fileSuffix 文件类型
     */
    public static void deleteAllFile(String filePath, String fileSuffix) {
        File fileDir = new File(filePath);
        if (fileDir.exists() && fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            for (File file : files) {
                if (file.toString().endsWith(fileSuffix)) {
                    file.delete();
                }
            }
        }
    }

    public static Uri getFileUri(File file, Context context) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName
                    + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 复制沙盒私有文件到Download公共目录下（应用删除文件依旧存在）
     *
     * @param context     上下文环境
     * @param file        待拷贝文件
     * @param displayName 复制后文件要显示的文件名称带后缀（如xx.txt）
     */
    public static void copyPrivateFileToDownload(Context context, String displayName,File file) {
        if (!BuildConfig.hasQ()) return;
        ContentValues values = new ContentValues();
        //values.put(MediaStore.Images.Media.DESCRIPTION, "This is a file");
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, FileUtils.getMIMEType(displayName));  //MediaStore对应类型名
        values.put(MediaStore.Files.FileColumns.TITLE, displayName);
        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, StorageUtils.PGY_FILE_DOWNLOAD_PATH);  //公共目录下目录名

        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;  //内部存储的Download路径
        ContentResolver resolver = context.getContentResolver();
        Uri insertUri = resolver.insert(external, values);  //使用ContentResolver创建需要操作的文件
        copyFile(file, insertUri, resolver);
    }

    public static void copyFile(File file, Uri insertUri, ContentResolver resolver) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(file);
                if (insertUri != null) {
                    outputStream = resolver.openOutputStream(insertUri);
                }
                if (outputStream != null) {
                    byte[] buffer = new byte[4096];
                    int byteCount;
                    while ((byteCount = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, byteCount);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                UIUtils.closeResource(inputStream, outputStream);
            }
    }


    /**
     * 将SAF存储框架获取的文件Uri存储到私有沙盒目录
     * （耗时放在子线程处理，应用删除文件随之删除）
     *
     * @param context 上下文环境
     * @param uri     文件Uri
     */
    public static Flowable<String> copyUriFileToPrivateSD(Context context, Uri uri) {
        return Flowable.create(e -> {
            DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
            if (null == documentFile) return;
            String fileName = documentFile.getName();//获取文件名，带后缀的

            OutputStream outputStream = null;
            InputStream inputStream = null;
            FileOutputStream fos;
            //设置私有沙盒存储路径
            String uploadPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                    File.separator + StorageUtils.UPLOAD_PATH + File.separator + fileName;
            File realFile = new File(uploadPath);
            if (null != realFile.getParentFile() && !realFile.getParentFile().exists())
                realFile.getParentFile().mkdirs();
            try {
                fos = new FileOutputStream(realFile);
                outputStream = new BufferedOutputStream(fos);
                inputStream = new BufferedInputStream(context.getContentResolver().openInputStream(uri));

                int byteCount;
                byte[] buf = new byte[4096];
                while ((byteCount = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, byteCount);
                }

                if (!e.isCancelled()) {
                    e.onNext(uploadPath);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }


    public static String copyUriFile2PrivateSD(Context context, Uri uri) {
        String uploadPath = "";
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
        if (null == documentFile) return uploadPath;
        String fileName = documentFile.getName();//获取文件名，带后缀的

        OutputStream outputStream = null;
        InputStream inputStream = null;
        FileOutputStream fos;
        //设置私有沙盒存储路径
        uploadPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + StorageUtils.UPLOAD_PATH + File.separator + fileName;
        File realFile = new File(uploadPath);
        if (null != realFile.getParentFile() && !realFile.getParentFile().exists())
            realFile.getParentFile().mkdirs();
        try {
            fos = new FileOutputStream(realFile);
            outputStream = new BufferedOutputStream(fos);
            inputStream = new BufferedInputStream(context.getContentResolver().openInputStream(uri));

            int byteCount;
            byte[] buf = new byte[4096];
            while ((byteCount = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, byteCount);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return uploadPath;
    }

    /**
     * 将Assert目录下文件拷贝到SD卡目录下
     *
     * @param context
     * @param path
     */
    public static void copyAssertToSD(Context context, String path, String fileName) {
        // 获取资产管理者
        AssetManager am = context.getAssets();
        try {
            // 打开资产目录下的文件
            InputStream inputStream = am.open(fileName);
            File file = new File(path, fileName);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    return false;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (!temp.exists()) {
                    return false;
                } else if (!temp.isFile()) {
                    return false;
                } else if (!temp.canRead()) {
                    return false;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    if (!TextUtils.isEmpty(temp.getName()) && (temp.getName().contains(".jpg") || temp.getName().contains(".png")))
                        createNewMediaFile(newPath);
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createNewMediaFile(String path) {
        File noMediaFile = new File(path + File.separator + ".nomedia");
        if (!noMediaFile.exists()) {
            try {
                boolean result = noMediaFile.createNewFile();
                LogUtils.i(TAG, "createResult>>>" + result);
            } catch (IOException io) {
                LogUtils.i(TAG, "createFail>>>");
            }
        }
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) return null;
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) return null;
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 获取文件的MIME
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) return "file/*";
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(type)) return type;
        return "file/*";
    }

}
