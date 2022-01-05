/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.ui;

import android.os.Bundle;
import android.view.MotionEvent;

import com.zhy.lib_common.R;
import com.zhy.lib_common.base.mvvm.BaseActivity;
import com.zhy.lib_common.base.mvvm.BaseFragment;
import com.zhy.lib_common.utils.UIUtils;

import androidx.annotation.Nullable;

/**
 * @author ； ZY
 * @date : 2020/10/10
 * @describe : App主容器
 */
public class MainActivity extends BaseActivity {
    public BaseFragment mCurrentFragment;
    public static String currentSkin;
    public static int bgColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            NavHostFragment.findNavController(fragment).popBackStack(R.id.advertise,true);
////            NavHostFragment.findNavController(fragment).setGraph(R.navigation.navigation);
        }
        if (UIUtils.isTaskRootAvail(this)) return;
    }

    @Override
    public int onBindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
    }



    @Override
    public void onBackPressed() {
        mCurrentFragment.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mCurrentFragment) mCurrentFragment.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
