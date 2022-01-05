/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-9-18 上午9:40
 *  FileName:BaseView.java
 */

package com.zhy.lib_common.base.mvp;

import android.os.Bundle;


import com.zhy.lib_common.base.mvvm.BaseActivity;

import androidx.annotation.Nullable;

/**
 * Created by ZY on 2019/9/18
 * DESC: class BaseView
 */
public abstract class BaseView<P extends BasePresenter, CONTRACT> extends BaseActivity {
    protected P p;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = getPresenter();
        p.bindView(this);
    }

    public abstract CONTRACT getContract();

    public abstract P getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p.unBindView();
        p.getmDisposableList().dispose();
    }
}
