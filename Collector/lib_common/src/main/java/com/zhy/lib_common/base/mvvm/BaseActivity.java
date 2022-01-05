/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.lib_common.R;
import com.zhy.lib_common.application.BaseApplication;
import com.zhy.lib_common.base.mvvm.view.IBaseView;
import com.zhy.lib_common.dialog.LoadingDialog;
import com.zhy.lib_common.utils.ScreenUtils;
import com.zhy.lib_common.utils.StatusBarUtil;
import com.zhy.lib_common.utils.ToastUtils;
import com.zhy.lib_common.utils.UIUtils;
import com.zhy.lib_common.widget.LoadingInitView;


/**
 * Created by ZY on 2020/7/16.
 * MVVM Activity基类
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected BaseApplication mApplication;
    protected TextView mTxtTitle;
    protected Toolbar mToolbar;
    protected LoadingInitView mLoadingInitView;
    protected LoadingDialog mLoadingDialog;
    protected FrameLayout mViewStubContent;
    private ViewStub mViewStubToolbar;
    private ViewStub mViewStubInitLoading;
    private ViewGroup mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getSupportActionBar()) getSupportActionBar().hide();
//        setStatusBar();
        super.setContentView(R.layout.activity_root);
        mContentView = findViewById(android.R.id.content);
        initCommonView();
        initContentView();
        initView();
        initListener();
        mApplication = (BaseApplication) getApplication();
    }

    /**
     * 设置沉浸式
     */
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this, 0);
    }

    protected void setStatusTranslucent() {
        if (null != mViewStubContent)
            mViewStubContent.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, ScreenUtils.getBottomNavigationHeight(this));
    }


    protected void initCommonView() {
        mViewStubToolbar = findViewById(R.id.view_stub_toolbar);
        mViewStubContent = findViewById(R.id.view_stub_content);
        mViewStubInitLoading = findViewById(R.id.view_stub_init_loading);

        if (enableToolbar()) {
            mViewStubToolbar.setLayoutResource(onBindToolbarLayout());
            View view = mViewStubToolbar.inflate();
            initToolbar(view);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mViewStubContent != null) {
            initContentView(layoutResID);
        }
    }

    public void initContentView() {
        initContentView(onBindLayout());
    }

    private void initContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mViewStubContent, false);
        mViewStubContent.setId(android.R.id.content);
        mContentView.setId(View.NO_ID);
        mViewStubContent.removeAllViews();
        mViewStubContent.addView(view);
    }

    /**
     * 初始化ToolBar 信息
     *
     * @param view
     */
    protected void initToolbar(View view) {
        mToolbar = view.findViewById(R.id.toolbar_root);
        mTxtTitle = view.findViewById(R.id.toolbar_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }

    /**
     * ToolBar变化切换
     *
     * @param title
     * @param color
     */
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mTxtTitle != null && !TextUtils.isEmpty(title)) {
            mTxtTitle.setText(title);
        }
        //可以再次覆盖设置title
        String tootBarTitle = getTootBarTitle();
        if (mTxtTitle != null && !TextUtils.isEmpty(tootBarTitle)) {
            mTxtTitle.setText(tootBarTitle);
        }
    }

    /**
     * 获取ToolBar通用布局
     *
     * @return
     */
    public int onBindToolbarLayout() {
        return R.layout.common_toolbar;
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    public abstract int onBindLayout();

    public abstract void initView();


    public void initListener() {
    }

    /**
     * 获取ToolBar标题
     *
     * @return
     */
    public String getTootBarTitle() {
        return "";
    }

    /**
     * 是否开启ToolBar
     *
     * @return
     */
    public boolean enableToolbar() {
        return false;
    }

    public void showInitLoadView(boolean show) {
        if (mLoadingInitView == null) {
            View view = mViewStubInitLoading.inflate();
            mLoadingInitView = view.findViewById(R.id.view_init_loading);
        }
        mLoadingInitView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingInitView.loading(show);
    }

    /**
     * 吐司提示
     *
     * @param msgId 吐司内容
     */
    public void showToast(int msgId) {
        ToastUtils.showToastMessage(getApplication(), msgId);
    }

    public void showToast(String msg) {
        ToastUtils.showToastMessage(getApplication(), msg);
    }

    protected void showLongToast(int msgId) {
        ToastUtils.showLongToastMessage(getApplication(), msgId);
    }


    /**
     * 处理网络连接失败
     */
    protected boolean isNetworkConnected() {
        if (UIUtils.isNetworkConnected(getContext())) return true;
        showInitLoadView(false);
//        showToast(R.string.neterror);
        return false;
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public Context getContext() {
        return this;
    }


}
