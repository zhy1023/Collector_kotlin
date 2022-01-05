package com.zhy.lib_common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by LY on 2017/12/7.
 */

public class SlideViewPager extends ViewPager {

    private int startX;
    private int criticalValue = 250;

    public interface onSlideListener{
        void onLeftSide();
        void onRightSide();
    }

    private onSlideListener mListener;

    public void setOnSlideListener(onSlideListener listener){
        this.mListener = listener;
    }

    public SlideViewPager(Context context) {
        super(context);
    }

    public SlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCriticalValue(int criticalValue) {
        this.criticalValue = criticalValue;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startX = (int) ev.getX();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if ((startX - ev.getX()) > criticalValue && (getCurrentItem() == getAdapter().getCount() - 1)) {
                if (null != mListener)
                    mListener.onRightSide();
            }
            if ((ev.getX() - startX) > criticalValue && (getCurrentItem() == 0)) {
                if (null != mListener)
                    mListener.onLeftSide();
            }
        }
        return super.onTouchEvent(ev);
    }
}
