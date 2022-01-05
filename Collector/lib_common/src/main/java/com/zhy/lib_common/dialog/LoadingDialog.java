package com.zhy.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.lib_common.R;

import java.util.Objects;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {
    private String mMessage;
    private boolean mCancelable;
    private RotateAnimation mRotateAnimation;
    private ImageView iv_loading;
    private Context mContext;

    public LoadingDialog(@NonNull Context context, String message) {
        this(context, R.style.LoadingDialog, message, false);
    }

    public LoadingDialog(@NonNull Context context, int themeResId, String message, boolean cancelable) {
        super(context, themeResId);
        mMessage = message;
        mCancelable = cancelable;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        // 设置窗口大小
        WindowManager windowManager = Objects.requireNonNull(getWindow()).getWindowManager();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.5f;
        attributes.width = screenWidth / 3;
        attributes.height = attributes.width;
        getWindow().setAttributes(attributes);
        setCancelable(mCancelable);

        TextView tv_loading = findViewById(R.id.tv_loading);
        iv_loading = findViewById(R.id.iv_loading);
        tv_loading.setText(mMessage);
        iv_loading.measure(0, 0);
        mRotateAnimation = new RotateAnimation(0, 360, iv_loading.getMeasuredWidth() >> 1, iv_loading.getMeasuredHeight() >> 1);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(2000);
        mRotateAnimation.setRepeatCount(-1);
        iv_loading.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != mRotateAnimation)
            mRotateAnimation.cancel();
    }

    @Override
    public void show() {
        super.show();
        if (null != iv_loading && null != mRotateAnimation)
            iv_loading.startAnimation(mRotateAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 屏蔽返回键
            return mCancelable;
        }
        return super.onKeyDown(keyCode, event);
    }
}
