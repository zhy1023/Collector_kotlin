/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm.model;

import android.app.Application;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by ZY on 2020/7/16.
 * BaseModel: MVVM model基类
 */
public abstract class BaseModel implements IBaseModel {
    protected Application mApplication;
    private CompositeDisposable mCompositeDisposable;

    public BaseModel(Application application) {
        mApplication = application;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 添加disposable
     *
     * @param disposable
     */
    public void addSubscribe(Disposable disposable) {
        if (null == disposable) return;
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 统一处理disposable
     */
    @Override
    public void onCleared() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

}
