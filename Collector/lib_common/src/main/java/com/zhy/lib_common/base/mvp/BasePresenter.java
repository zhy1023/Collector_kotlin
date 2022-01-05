/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-9-18 上午9:40
 *  FileName:BasePresenter.java
 */

package com.zhy.lib_common.base.mvp;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ZY on 2019/9/18
 * DESC: class BasePresenter
 */
public abstract class BasePresenter<V, M extends BaseModel, CONTRACT> {
    protected M m;
    private CompositeDisposable mDisposableList;
    private WeakReference<V> vWeakReference;

    public BasePresenter() {
        this.m = getModel();
        mDisposableList = new CompositeDisposable();
    }


    public void bindView(V v) {
        vWeakReference = new WeakReference<>(v);
    }

    public void unBindView() {
        if (null != vWeakReference) {
            vWeakReference.clear();
            vWeakReference = null;
            System.gc();
        }
    }

    public V getView() {
        if (null != vWeakReference) {
            return vWeakReference.get();
        }
        return null;
    }

    public CompositeDisposable getmDisposableList() {
        return mDisposableList;
    }


    public abstract CONTRACT getContract();

    public abstract M getModel();
}
