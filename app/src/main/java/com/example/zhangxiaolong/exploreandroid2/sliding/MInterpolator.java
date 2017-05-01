package com.example.zhangxiaolong.exploreandroid2.sliding;

import android.view.animation.Interpolator;

/**
 * Created by zhangxiaolong on 2017/4/27.
 */

public class MInterpolator implements Interpolator {
    private final float factor = 0.4f;

    @Override
    public float getInterpolation(float x) {
        //pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1
        double result = Math.pow(2,-10 * x) * Math.sin(( x - factor / 4) * (2 * Math.PI) / factor) + 1;
        return (float) result;
    }
}
