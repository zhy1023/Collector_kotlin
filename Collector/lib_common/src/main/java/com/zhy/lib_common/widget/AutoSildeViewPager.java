package com.zhy.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by ZY on 2019/2/12
 * DESC: class AutoSildeViewPager
 */
public class AutoSildeViewPager extends ViewPager {
    private boolean isSlide = true;

    public AutoSildeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSlide && super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide && super.onInterceptTouchEvent(ev);
    }
}