package com.zhy.lib_common.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by ZY on 2018/12/14
 * DESC: class PointEvaluator
 */
public class PointEvaluator implements TypeEvaluator<PointF> {

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        return new PointF(x, y);
    }
}
