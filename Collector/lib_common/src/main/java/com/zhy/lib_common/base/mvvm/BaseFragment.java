/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhy.lib_common.R;
import com.zhy.lib_common.base.mvvm.view.IBaseView;
import com.zhy.lib_common.dialog.LoadingDialog;
import com.zhy.lib_common.ui.MainActivity;
import com.zhy.lib_common.utils.BuildConfig;
import com.zhy.lib_common.utils.DataUtils;
import com.zhy.lib_common.utils.KeyboardUtils;
import com.zhy.lib_common.utils.LogUtils;
import com.zhy.lib_common.utils.ScreenUtils;
import com.zhy.lib_common.utils.StatusBarUtil;
import com.zhy.lib_common.utils.ThreadUtil;
import com.zhy.lib_common.utils.ToastUtils;
import com.zhy.lib_common.utils.UIUtils;
import com.zhy.lib_common.widget.LoadingInitView;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


/**
 * Created by ZY on 2020/7/16.
 * MVVM Fragment基类
 */
public abstract class BaseFragment extends Fragment implements IBaseView {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    public MainActivity mActivity;
    protected View mView;
    protected TextView mTxtTitle;
    protected Toolbar mToolbar;

    protected LoadingInitView mLoadingInitView;

    protected FrameLayout mViewStubContent;
    private ViewStub mViewStubInitLoading;
    protected LoadingDialog mLoadingDialog;
    private boolean isViewCreated = false;
    private boolean isViewVisable = false;
    protected SharedPreferences sp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (com.zhy.lib_common.ui.MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_root, container, false);
            initCommonView(mView);
            initView(mView);
            setStatusBar();
        }
        return mView;
    }

    /**
     * 适配沉浸式状态栏
     */
    protected void setStatusBar() {
        if (!isSetStatusBarColor()) return;
        setTranslucentTop();
        setRootViewBgColor();
//        StatusBarUtil.setColor(mActivity, MainActivity.bgColor, 0);
    }

    /**
     * 不涉及皮肤等级的状态栏色织
     */
    protected void setDefaultStatusBar() {
        setTranslucentTop();
        setNormalRootViewBgColor(R.color.bg_colorPrimary_default);
        StatusBarUtil.setColor(mActivity, getResources().getColor(R.color.bg_colorPrimary_default), 0);
    }

    /**
     * 设置顶部状态栏padding
     */
    protected void setTranslucentTop() {
        if (null != mViewStubContent)
            mViewStubContent.setPadding(0, ScreenUtils.getStatusBarHeight(mActivity), 0, 0);
    }


    /**
     * 设置根布局背景色
     */
    protected void setRootViewBgColor() {
        if (null != mViewStubContent)
            mViewStubContent.setBackgroundColor(MainActivity.bgColor);
    }


    /**
     * 设置普通等级下的页面背景色
     */
    protected void setNormalRootViewBgColor(int color) {
        if (null != mViewStubContent)
            mViewStubContent.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * 设置底部状态栏padding
     */
    protected void setTranslucentBottom() {
        if (null != mViewStubContent)
            mViewStubContent.setPadding(0, 0, 0, ScreenUtils.getBottomNavigationHeight(mActivity));
    }

    /**
     * 设置顶、底部状态栏padding
     */
    protected void setTranslucent() {
        if (null != mViewStubContent)
            mViewStubContent.setPadding(0, ScreenUtils.getStatusBarHeight(mActivity), 0, ScreenUtils.getBottomNavigationHeight(mActivity));
    }


    /**
     * 设置状态栏明亮模式
     * 状态栏字体颜色 黑色字体
     */
    protected void setLightMode(int color) {
        if (BuildConfig.hasM()) StatusBarUtil.setLightMode(mActivity);
        setNormalRootViewBgColor(color);
        StatusBarUtil.setColor(mActivity, getResources().getColor(color), 0);
    }



    /**
     * 设置状态栏暗黑模式
     * 状态栏字体颜色默认白色字体
     */
    protected void setDarkMode() {
        if (BuildConfig.hasM()) StatusBarUtil.setDarkMode(mActivity);
    }

    public void initCommonView(View view) {
        ViewStub mViewStubToolbar = view.findViewById(R.id.view_stub_toolbar);
        mViewStubContent = view.findViewById(R.id.view_stub_content);
        mViewStubInitLoading = view.findViewById(R.id.view_stub_init_loading);

        if (enableToolbar()) {
            mViewStubToolbar.setLayoutResource(onBindToolbarLayout());
            View viewTooBbar = mViewStubToolbar.inflate();
            initTooBar(viewTooBbar);
        }
        initConentView(mViewStubContent);
    }

    public void initConentView(ViewGroup root) {
        LayoutInflater.from(mActivity).inflate(onBindLayout(), root, true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (null != mActivity) mActivity.mCurrentFragment = this;
        if (isViewCreated) return;
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        //如果启用了懒加载就进行懒加载，否则就进行预加载
        if (enableLazyData()) {
            lazyLoad();
        } else {
            initData();
        }
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.i(TAG,"isVisibleToUser :" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        isViewVisable = isVisibleToUser;
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad();
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewVisable = true;
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad();
        }
    }


    /**
     * 懒加载
     */
    private void lazyLoad() {
        //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        LogUtils.i(getFragmentTag(), "lazyLoad start...");
        if (isViewCreated && isViewVisable) {
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isViewVisable = false;
        }
    }

    //默认不启用懒加载
    public boolean enableLazyData() {
        return false;
    }


    /**
     * 初始化ToolBar信息
     *
     * @param view
     */
    protected void initTooBar(View view) {
        mToolbar = view.findViewById(R.id.toolbar_root);
        mTxtTitle = view.findViewById(R.id.toolbar_title);
        if (mToolbar != null) {
            mActivity.setSupportActionBar(mToolbar);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
        }
        if (mTxtTitle != null) {
            mTxtTitle.setText(getToolbarTitle());
        }
    }

    /**
     * 获取布局资源ID
     *
     * @return
     */
    public abstract int onBindLayout();

    public abstract void initView(View view);

    public void initView() {
    }

    /**
     * 获取ToolBar标题
     *
     * @return
     */
    protected String getToolbarTitle() {
        return getString(R.string.app_name);
    }


    public void initListener() {
    }

    protected void initData() {
    }

    /**
     * 获取fragment TAG
     *
     * @return
     */
    protected String getFragmentTag() {
        return TAG;
    }

    /**
     * 是否需要设置沉浸式
     *
     * @return
     */
    public boolean isSetStatusBarColor() {
        return true;
    }

    /**
     * 是否需要ToolBar
     *
     * @return
     */
    public boolean enableToolbar() {
        return false;
    }

    /**
     * 获取ToolBar 通用布局
     *
     * @return
     */
    public int onBindToolbarLayout() {
        return R.layout.common_toolbar;
    }


    /**
     * 吐司
     *
     * @param msgId
     */
    public void showToast(int msgId) {
        ToastUtils.showToastMessage(getActivity(), msgId);
    }

    public void showToast(String msg) {
        ToastUtils.showToastMessage(getActivity(), msg);
    }

    public void showLongToast(int msgId) {
        ToastUtils.showLongToastMessage(getActivity(), msgId);
    }

    /**
     * 处理网络连接失败
     */
    public boolean isNetworkConnected() {
        if (UIUtils.isNetworkConnected(getActivity())) return true;
//        showToast(R.string.neterror);
        showInitLoadView(false);
        return false;
    }

    /**
     * @param editText
     * @param errorMsg
     * @return
     */
    public boolean isEditTextEmpty(EditText editText, int errorMsg) {
        if (DataUtils.isEmpty(editText)) {
            showToast(errorMsg);
            return true;
        }
        return false;
    }


    /**
     * 页面加载动画
     *
     * @param show
     */
    public void showInitLoadView(boolean show) {
        if (mLoadingInitView == null) {
            View view = mViewStubInitLoading.inflate();
            mLoadingInitView = view.findViewById(R.id.view_init_loading);
        }
        mLoadingInitView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingInitView.loading(show);
    }

    /**
     * 显示加载对话框
     */
    protected void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    /**
     * 关闭加载对话框
     */
    protected void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    public void navigation(int navigationId) {
        navigation(mView, navigationId);
    }

    /**
     * 导向指定id的fragment
     */
    public void navigation(View v, int navigationId) {
        try {
            Navigation.findNavController(v).navigate(navigationId);
        } catch (Exception e) {
            LogUtils.e(TAG,e.getMessage());
            ThreadUtil.sleep(10);
            Navigation.findNavController(v).navigate(navigationId);
        }
    }

    /**
     * 携参导向指定id的fragment
     */
    public void navigation(int navigationId, Bundle bundle) {
        navigation(mView, navigationId, bundle);
    }


    /**
     * 携参导向指定id的fragment
     */
    public void navigation(View v, int navigationId, Bundle bundle) {
        try {
            Navigation.findNavController(v).navigate(navigationId, bundle);
        } catch (Exception e) {
            LogUtils.e(TAG,e.getMessage());
            ThreadUtil.sleep(10);
            Navigation.findNavController(v).navigate(navigationId, bundle);
        }
    }

    /**
     * 返回上一级并出栈
     */
    public void navigationBack() {
        Navigation.findNavController(mView).popBackStack();
    }

    /**
     * 导向指定fragment
     *
     * @param navigationId 指定的fragment navigationId
     * @param isInClusive  是否让指定的fragment出栈
     */
    public void navigation2Fragment(int navigationId, boolean isInClusive) {
        Navigation.findNavController(mView).popBackStack(navigationId, isInClusive);
    }


    /**
     * 导向上一级
     */
    public void navigateUp() {
        Navigation.findNavController(mView).navigateUp();
    }

    /**
     * 处理Android返回键
     */
    public void onBackPressed() {
        navigationBack();
    }


    /**
     * 处理主容器分发事件
     */
    public void dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = mActivity.getCurrentFocus();
            KeyboardUtils.hideKeyboard(ev, view);
            handleActionDownEvent(ev);
        }
    }

    /**
     * 处理ACTION_DOWN事件
     */
    protected void handleActionDownEvent(MotionEvent ev) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
