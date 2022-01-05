package com.zhy.lib_common.application;

import android.app.Application;
import android.content.Context;

/**
 * @Author ；zhy
 * @ClassName: BaseApplication
 * @Date : 2020/12/24 15:34
 * @Describe :
 */
public class BaseApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
