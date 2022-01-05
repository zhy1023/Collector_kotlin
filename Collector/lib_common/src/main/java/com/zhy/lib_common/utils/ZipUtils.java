package com.zhy.lib_common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by qianwei on 2017/11/20.
 */

public class ZipUtils {

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    /**
     * 含子目录的文件压缩
     *
     * @throws Exception
     */
    // 第一个参数就是需要解压的文件，第二个就是解压的目录  
    public static boolean upZipFile(String zipFile, String folderPath) {
        ZipFile zipfile;
        try {
            // 转码为GBK格式，支持中文  
            zipfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zipfile.entries();
        ZipEntry ze;
        File dirFile = new File(folderPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {                 // 列举的压缩文件里面的各个文件，判断是否为目录
                String dirStr = folderPath + ze.getName();
                dirStr.trim();
                File f = new File(dirStr);
                f.mkdir();
                continue;
            }
            OutputStream os;
            FileOutputStream fos;
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is;
            try {
                is = new BufferedInputStream(zipfile.getInputStream(ze));
            } catch (IOException e) {
                return false;
            }
            int readLen;
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            zipfile.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        absFileName = absFileName.replace("\\", "/");
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String subStr;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                subStr = dirs[i];
                ret = new File(ret, subStr);
            }
            if (!ret.exists())
                ret.mkdirs();
            subStr = dirs[dirs.length - 1];
            ret = new File(ret, subStr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }


    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @param isDelete
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void upZipFile(File zipFile, String folderPath, boolean isDelete) {

        String strZipName = zipFile.getName();
        folderPath += "/" + strZipName.substring(0, strZipName.lastIndexOf("."));

        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf;
        try {
            zf = new ZipFile(zipFile);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                if (entry.isDirectory()) {
                    String dirstr = entry.getName();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }

                InputStream in = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                str = new String(str.getBytes("8859_1"), "GB2312");
                FileUtils.createNewMediaFile(folderPath);
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }

                OutputStream out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                in.close();
                out.close();
            }

            if (isDelete) {
                zipFile.delete();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
