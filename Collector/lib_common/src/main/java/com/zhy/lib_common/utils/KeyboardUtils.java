/*
 * Copyright (c) 2018 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: LY
 *  Create on:  18-3-19 下午4:56
 *  FileName:KeyboardUtils.java
 */

package com.zhy.lib_common.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by LY on 2018/3/19.
 */

public class KeyboardUtils {

    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm)
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm)
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view  控件view
     * @param event 焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view) {
        if (!(view instanceof EditText)) return;
        int[] location = {0, 0};
        try {
            view.getLocationInWindow(location);
            int left = location[0], top = location[1], right = left
                    + view.getWidth(), bootom = top + view.getHeight();
            // （判断是不是EditText获得焦点）判断焦点位置坐标是否在控件所在区域内，如果位置在控件区域外，则隐藏键盘
            if (event.getRawX() < left || event.getRawX() > right
                    || event.getY() < top || event.getRawY() > bootom) {
                hideSoftInput(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
