/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhy.lib_common.base.mvvm.viewmodel.BaseViewModel;

import java.util.Map;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by ZY on 2020/7/16.
 * BaseMvvmFragment  Fragment MVVM基类
 */
public abstract class BaseMvvmFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment {
    protected V mBinding;
    protected VM mViewModel;
    private int viewModelId;

    @Override
    public void initConentView(ViewGroup root) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), onBindLayout(), root, true);
        initViewModel();
        initBaseViewObservable();
        initViewObservable();
    }

    private void initViewModel() {
        mViewModel = createViewModel();
        viewModelId = onBindVariableId();
        if (mBinding != null) {
            mBinding.setVariable(viewModelId, mViewModel);
        }
        getLifecycle().addObserver(mViewModel);
    }

    public VM createViewModel() {
        return new ViewModelProvider(this, onBindViewModelFactory()).get(onBindViewModel());
    }

    public abstract Class<VM> onBindViewModel();

    public abstract ViewModelProvider.Factory onBindViewModelFactory();

    public abstract void initViewObservable();

    public abstract int onBindVariableId();

    protected void initBaseViewObservable() {
        mViewModel.getUC().getShowInitLoadViewEvent().observe(this, (Observer<Boolean>) this::showInitLoadView);

        mViewModel.getUC().getStartActivityEvent().observe(this, (Observer<Map<String, Object>>) params -> {
            Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            startActivity(clz, bundle);
        });
        mViewModel.getUC().getFinishActivityEvent().observe(this, (Observer<Void>) v -> {
            if (mActivity != null) {
                mActivity.finish();
            }
        });
        mViewModel.getUC().getOnBackPressedEvent().observe(this, (Observer<Void>) v -> {
            if (mActivity != null) {
                mActivity.onBackPressed();
            }
        });
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
