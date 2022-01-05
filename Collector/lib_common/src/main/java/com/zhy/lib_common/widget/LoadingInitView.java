/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhy.lib_common.R;


/**
 * 页面加载动画
 */
public class LoadingInitView extends RelativeLayout {
    private final AnimationDrawable mAnimationDrawable;

    public LoadingInitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.loading_view, this);
        ImageView imgLoading = findViewById(R.id.iv_loading);
        mAnimationDrawable = (AnimationDrawable) imgLoading.getDrawable();
    }

    public void startLoading() {
        mAnimationDrawable.start();
    }

    public void stopLoading() {
        mAnimationDrawable.stop();
    }

    public void loading(boolean b) {
        if (b) {
            startLoading();
        } else {
            stopLoading();
        }
    }
}
