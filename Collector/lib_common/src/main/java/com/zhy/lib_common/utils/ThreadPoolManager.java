/*
 * Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-3-18 下午3:37
 *  FileName:ThreadPoolManager.java
 */

package com.zhy.lib_common.utils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZY on 2019/3/18
 * DESC: class ThreadPoolManager
 */
public class ThreadPoolManager {
    private static final String TAG = ThreadPoolManager.class.getSimpleName();
    private ExecutorService mExecutorService;

    /**
     * 创建缓存线程池
     */
    private ThreadPoolManager() {
        if (null == mExecutorService) {
            int availableProcessor = Runtime.getRuntime().availableProcessors();
            int coreNum = availableProcessor / 2;
            int maxProcessor = (availableProcessor * 2 + 1) * 2;
            mExecutorService = new ThreadPoolExecutor(coreNum > 2 ? 2 : coreNum, maxProcessor,
                    60L, TimeUnit.SECONDS, new SynchronousQueue<>(), (r, executor) -> Executors.newSingleThreadExecutor().execute(r));
        }
    }


    private static ThreadPoolManager mThreadPoolManager;

    public static ThreadPoolManager getDefault() {
        if (null == mThreadPoolManager) {
            synchronized (ThreadPoolManager.class) {
                if (null == mThreadPoolManager)
                    mThreadPoolManager = new ThreadPoolManager();
            }
        }
        return mThreadPoolManager;
    }


    public void excuteTask(Runnable runnable) {
        try {
            mExecutorService.execute(runnable);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getLocalizedMessage());
        }
    }

    /**
     * 线程注册异常时重启一个新的线程池
     */
    private void handleRejectedExecution() {
        try {
            Class cls = mExecutorService.getClass();
            Object objectExecutor = cls.getField("THREAD_POOL_EXECUTOR").get(null);
            if (objectExecutor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) objectExecutor;
                threadPoolExecutor.setRejectedExecutionHandler((r, executor) -> Executors.newSingleThreadExecutor().execute(r));
            }
            Method setDefaultExecutor = cls.getMethod("setDefaultExecutor", Executor.class);
            setDefaultExecutor.invoke(cls, objectExecutor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ExecutorService getmExecutorService() {
        return mExecutorService;
    }
}
