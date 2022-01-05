/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.zhy.lib_common.application.BaseApplication;


/**
 * @author ； ZY
 * @date : 2020/5/19
 * @describe :
 */
public class ScreenUtils {
    private static final String SCREE_PARAMETER_720 = "720x1280";
    private static final String SCREE_PARAMETER_1080 = "1080x1920";
    private static final String SCREE_PARAMETER_1440 = "1440x2560";
    private static final String SCREE_PARAMETER_1600 = "2560x1600";


    /**
     * 返回当前手机屏幕尺寸
     *
     * @return
     */
    public static String makeSize(Context context) {
        int screenWidth = getScreenWidth(context);
        if (screenWidth == 720) {
            return SCREE_PARAMETER_720;
        } else if (screenWidth == 1080) {
            return SCREE_PARAMETER_1080;
        } else if (screenWidth == 1440) {
            return SCREE_PARAMETER_1440;
        } else if (screenWidth == 1600) {
            return SCREE_PARAMETER_1600;
        }
        return SCREE_PARAMETER_1080;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 获取顶部状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * 获取底部导航栏高度
     *
     * @param context
     * @return
     */
    public static int getBottomNavigationHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }


    //虚拟导航栏显示/隐藏
    public static void setNavigationListener(final View rootView, final NavigationListener navigationListener) {
        if (rootView == null || navigationListener == null) {
            return;
        }
        if (getRealHeight() != getScreenHeight(BaseApplication.mContext)) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                int rootViewHeight;

                @Override
                public void onGlobalLayout() {
                    int viewHeight = rootView.getHeight();
                    if (rootViewHeight != viewHeight) {
                        rootViewHeight = viewHeight;
                        if (viewHeight == getRealHeight()) {
                            //隐藏虚拟按键
                            if (navigationListener != null) {
                                navigationListener.hide();
                            }
                        } else {
                            //显示虚拟按键
                            if (navigationListener != null) {
                                navigationListener.show();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     */
    public static int getRealHeight() {
        WindowManager windowManager = (WindowManager) BaseApplication.mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.heightPixels;
    }


    public interface NavigationListener {
        void show();

        void hide();
    }


    /**
     * 当前点击位置是否在被点击View内部
     */
    public static boolean isClickTargetView(MotionEvent ev, View view) {
        if (null == view) return false;
        int[] location = {0, 0};
        try {
            view.getLocationInWindow(location);
            int left = location[0], top = location[1], right = left
                    + view.getWidth(), bootom = top + view.getHeight();
            if (ev.getRawX() >= left && ev.getRawX() <= right
                    && ev.getY() >= top && ev.getRawY() <= bootom) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 监听点击位置是否在被点击View之外
     */
    public static void setOutClickViewListener(MotionEvent ev, View view, IOutViwClickListener listener) {
        if (null == view) return;
        int[] location = {0, 0};
        try {
            view.getLocationInWindow(location);
            int left = location[0], top = location[1], right = left
                    + view.getWidth(), bootom = top + view.getHeight();
              /*float rawX = ev.getRawX();
            float rawY = ev.getRawY();
            float X = ev.getX();
            float Y = ev.getY();
          LogUtils.i("left :" + String.valueOf(left) + " right :" + String.valueOf(right)
                    + " top :" + String.valueOf(top) + " bottom :" + String.valueOf(bootom));

            LogUtils.i("rawX :" + String.valueOf(rawX) + " X :" + String.valueOf(X)
                    + " Y :" + String.valueOf(Y) + " rawY :" + String.valueOf(rawY));*/
            if (ev.getRawX() < left || ev.getRawX() > right
                    || ev.getY() < top || ev.getRawY() > bootom) {
                if (null != listener) listener.OnOutClickView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface IOutViwClickListener {
        void OnOutClickView();
    }

}
