package com.example.hua.framework.wrapper.adapt;

import android.util.DisplayMetrics;

/**
 * @author hua
 * @version 2018/9/19 16:28
 */

public class DisplayMetricsInfo {
    float density;
    int densityDpi;
    float scaledDensity;
    int widthPixels;
    int heightPixels;

    /**
     * 将对象存储的信息恢复到{@code displayMetrics}
     */
    protected void restore(DisplayMetrics displayMetrics) {
        displayMetrics.density = density;
        displayMetrics.densityDpi = densityDpi;
        displayMetrics.scaledDensity = scaledDensity;
    }

    /**
     * 将{@code displayMetrics}的信息存储到此对象
     */
    protected void save(DisplayMetrics displayMetrics) {
        this.density = displayMetrics.density;
        this.densityDpi = displayMetrics.densityDpi;
        this.scaledDensity = displayMetrics.scaledDensity;
        this.widthPixels = displayMetrics.widthPixels;
        this.heightPixels = displayMetrics.heightPixels;
    }
}
