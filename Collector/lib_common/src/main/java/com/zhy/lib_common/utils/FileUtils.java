package com.zhy.lib_common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.zhy.lib_common.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.FileProvider;

/**
 * Created by ZY on 2019/2/26
 * DESC: class FileUtils
 */
public class FileUtils {


    /**
     * 根据地址获取当前地址下的所有目录和文件
     *
     * @param path 存储卡根目录
     * @return 文件集合
     */
    public static List<File> getAllFileListByDirPath(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        List<File> result = new ArrayList<>();
        if (files == null)
            return new ArrayList<>();

        Collections.addAll(result, files);
        Collections.sort(result, new FileComparator());
        return result;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0项";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件长度
     */
    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * SD卡是否可用
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }

    /**
     * long时间转换成日期
     */
    public static String longToString(long currentTime) {
        return new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA).format(new Date(currentTime));
    }


    /**
     * 获取文件类型图片资源id
     *
     * @param exte
     * @return
     */
    public static int getResourceId(String exte) {
        for (int i = 0; i < extensions.length; i++) {
            String[] extension = extensions[i];
            for (String a : extension) {
                if (a.equalsIgnoreCase(exte))
                    return extensions_imgId[i];
            }
        }
        return R.drawable.file_unknown;
    }

    private static final String[][] extensions = {
            {"txt", "log", "xml", "html", "java", "swift", "c", "h", "cpp", "m", "plist", "storyboard"},
            {"doc", "docx", "dotx"},
            {"gif", "jpg", "png", "bmp", "jpeg", "tif", "ico"},
            {"mp3", "wma", "ogg", "ape", "flac"},
            {"pdf"},
            {"xls", "xlsx"},
            {"rar", "zip", "tgz", "tar", "jar", "7z", "bz2"},
            {"rmvb", "asf", "avi", "divx", "dv", "flv", "gxf", "m1v", "m2v", "m2ts", "m4v", "mkv", "mov", "mp2", "mp4", "mpeg", "mpeg1", "mpeg2",
                    "mpeg4", "mpg", "mts", "mxf", "ogg", "ogm", "ps", "ts", "vob", "wmv", "a52", "aac", "ac3", "dts", "m4a", "m4p", "mka", "mod", "mp1",
                    "mp2", "rm", "tp"},
            {"ppt", "pptx"}
    };


    private static final int[] extensions_imgId = {R.drawable.file_notes, R.drawable.file_word,
            R.drawable.file_img, R.drawable.file_music, R.drawable.file_pdf, R.drawable.file_excle,
            R.drawable.file_compress_package, R.drawable.file_movie, R.drawable.file_ppt};



    public static boolean isImgIcon(String str) {
        for (String s : extensions[2]) {
            if (s.equals(str)) return true;
        }
        return false;
    }

    /**
     * 是否为视频文件
     *
     * @param str 文件名后缀
     * @return
     */
    public static boolean isVideoFile(String str) {
        for (String s : extensions[7]) {
            if (s.equals(str)) return true;
        }
        return false;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS == 0) {
            fileSizeString = "0B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) fileS / (1024 * 1024 * 1024))
                    + "G";
        }
        return fileSizeString;
    }


    public static String getMIMEType(String fileName) {
        String type = "*/*";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fileName.substring(dotIndex + 1).toLowerCase(Locale.CHINA);
        if (end.equals("")) return type;
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
        return type;
    }


    /**
     * 获取对应文件的Uri
     *
     * @param intent 相应的Intent
     * @param file   文件对象
     * @return
     */
    public static Uri getUri(Context mContext, Intent intent, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext,
                    mContext.getPackageName() + ".fileprovider",
                    file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    public static void deleteAllFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File subFile : files) {
                    deleteAllFile(subFile);
                }
            }
            file.delete();
        }
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


    public static void createNewMediaFile(String path) {
        File noMediaFile = new File(path + File.separator + ".nomedia");
        if (!noMediaFile.exists()) {
            try {
                boolean result = noMediaFile.createNewFile();
            } catch (IOException io) {
            }
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

    /**
     * 校验文件名称是否合法
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isValidFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) != '.') return true;
        }
        return false;
    }

    /**
     * 是否是合法的文件名
     *
     * @param name
     * @return
     */
    public static boolean isValidateFileName(String name) {
        if (TextUtils.isEmpty(name)) return false;
        String regEx = "^[^？*|“<>:/]{1,256}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        return m.matches();
    }


    //调用系统分享
    public static void shareFile(List<File> downloads, Activity activity) {
        boolean multiple = downloads.size() > 1;
        Intent intent = new Intent(multiple ? Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND);
        StringBuilder stringBuilder = new StringBuilder();

        if (multiple) {
            ArrayList<Uri> uris = new ArrayList<>();
            for (int i = 0; i < downloads.size(); i++) {
                File file = downloads.get(i);
                Uri uri = FileOperationUtils.getFileUri(file, activity);
                uris.add(uri);
                stringBuilder.append(file.getName());
                if (i != downloads.size() - 1) {
                    stringBuilder.append(".");
                }
            }
            intent.setType("*/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            File file = downloads.get(0);
            String mimeType = FileOperationUtils.getMimeType(file);
            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_STREAM, FileOperationUtils.getFileUri(file, activity));
            stringBuilder.append(file.getName());
        }
        String shareText = getShareTitle(stringBuilder.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        activity.startActivity(Intent.createChooser(intent, shareText));
    }

    private static String getShareTitle(String text) {
        if (text.length() > 25) {
            text = text.substring(0, 25);
            if (text.endsWith(".")) {
                text = text + "..";
            } else {
                text = text + "...";
            }
        }
        return text;
    }





}
