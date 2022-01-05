package com.zhy.lib_common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * 检测权限工具类
 */
public class PermissionUtils {

    /**
     * 第一次检查权限，用在打开应用的时候请求应用需要的所有权限
     *
     * @param context
     * @param requestCode 请求码
     * @param permission  权限数组
     * @return
     */
    public static boolean checkPermission(Context context, int requestCode, String[] permission) {
        List<String> permissions = new ArrayList<String>();
        for (String per : permission) {
            int permissionCode = ActivityCompat.checkSelfPermission(context, per);
            if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                permissions.add(per);
            }
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测是否有存储权限
     *
     * @param context
     * @param requestCode
     * @return
     */
    public static boolean hasStoragePermission(Context context, int requestCode) {
        if (!BuildConfig.hasM()) return true;
        return PermissionUtils.checkPermission(context, requestCode,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE});

    }


    public static boolean hasPermission(Context context, final String... permissions) {
        return !BuildConfig.hasM() || AndPermission.hasPermissions(context, permissions);
    }


    /**
     * 第二次检查权限，用在某个操作需要某个权限的时候调用
     *
     * @param context
     * @param requestCode 请求码
     * @param permission  权限数组
     * @return
     */
    public static boolean checkPermissionSecond(Context context, int requestCode, String[] permission) {

        List<String> permissions = new ArrayList<String>();
        for (String per : permission) {
            int permissionCode = ActivityCompat.checkSelfPermission(context, per);
            if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                permissions.add(per);
            }
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), requestCode);

            /*跳转到应用详情，让用户去打开权限*/
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
            context.startActivity(localIntent);
            return false;
        } else {
            return true;
        }
    }
}
