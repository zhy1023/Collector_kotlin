/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-9-18 下午7:33
 *  FileName:BaseFragmentView.java
 */

package com.zhy.lib_common.base.mvp;

import android.os.Bundle;


import com.zhy.lib_common.base.mvvm.BaseFragment;

import androidx.annotation.Nullable;

/**
 * Created by ZY on 2019/9/18
 * DESC: class BaseFragmentView
 */
public abstract class BaseFragmentView<P extends BasePresenter, CONTRACT> extends BaseFragment {
    protected P p;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = getPresenter();
        p.bindView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p.unBindView();
        p.getmDisposableList().dispose();
    }

    public abstract CONTRACT getContract();

    public abstract P getPresenter();
}
