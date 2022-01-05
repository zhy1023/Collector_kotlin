package com.zhy.lib_common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ZY on 2018/8/27
 * DESC: class ToastUtils
 */
public class ToastUtils {
    private static Toast mToast;

    public static void showToastMessage(Context context, String msg) {
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showToastMessage(Context context, int msgId) {
        mToast = Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showLongToastMessage(Context context, int msgId) {
        mToast = Toast.makeText(context, context.getString(msgId), Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
