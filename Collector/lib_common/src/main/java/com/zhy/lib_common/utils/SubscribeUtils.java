/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-4-12 下午2:19
 *  FileName:SubscribeUtils.java
 */

package com.zhy.lib_common.utils;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZY on 2019/4/12
 * DESC: class SubscribeUtils
 */
public class SubscribeUtils {

    /**
     * 实现切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> switchMain() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 实现切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 取消订阅操作
     *
     * @param disposables
     */
    public static void disposable(Disposable... disposables) {
        if (null != disposables && disposables.length > 0) {
            for (Disposable disposable : disposables) {
                if (null != disposable && !disposable.isDisposed()) disposable.dispose();
            }
        }
    }
}
