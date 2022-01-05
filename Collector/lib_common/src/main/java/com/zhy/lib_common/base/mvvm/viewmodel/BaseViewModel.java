/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm.viewmodel;

import android.app.Application;
import android.os.Bundle;

import com.zhy.lib_common.base.mvvm.model.BaseModel;
import com.zhy.lib_common.event.SingleLiveEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ZY on 2020/7/16.
 * BaseViewModel ViewModel基类
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IBaseViewModel, Consumer<Disposable> {
    protected M mModel;
    protected UIChangeLiveData mUIChangeLiveData;

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.mModel = model;
    }

    /**
     * 获取liveData
     *
     * @return
     */
    public UIChangeLiveData getUC() {
        if (mUIChangeLiveData == null) {
            mUIChangeLiveData = new UIChangeLiveData();
        }
        return mUIChangeLiveData;
    }

    /**
     * 初始化liveData常用属性
     */
    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<Boolean> showInitLoadViewEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Boolean> finishActivityEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;

        public SingleLiveEvent<Boolean> getShowInitLoadViewEvent() {
            return showInitLoadViewEvent = createLiveData(showInitLoadViewEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Boolean> getFinishActivityEvent() {
            return finishActivityEvent = createLiveData(finishActivityEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }
    }

    protected SingleLiveEvent createLiveData(SingleLiveEvent liveData) {
        if (liveData == null) {
            liveData = new SingleLiveEvent();
        }
        return liveData;
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }

    public void postShowInitLoadViewEvent(boolean show) {
        if (mUIChangeLiveData != null) {
            mUIChangeLiveData.showInitLoadViewEvent.postValue(show);
        }
    }

    public void postStartActivityEvent(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        mUIChangeLiveData.startActivityEvent.postValue(params);
    }


    public void postFinishActivityEvent() {
        mUIChangeLiveData.finishActivityEvent.call();
    }


    public void postOnBackPressedEvent() {
        mUIChangeLiveData.onBackPressedEvent.call();
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }


    @Override
    public void accept(Disposable disposable) {
        if (mModel != null) {
            mModel.addSubscribe(disposable);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mModel != null) {
            mModel.onCleared();
        }
    }


}
