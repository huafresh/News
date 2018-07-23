package com.example.hua.framework.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * 自定义RecyclerView支持minHeight、maxHeight、minWidth、maxWidth等属性
 *
 * @author hua
 * @version 2018/4/17 17:05
 */

public class MinMaxRecyclerView extends RecyclerView {
    public static final String UNIT_DIP = "dip";
    public static final String ANDROID_NAME_SPACE = "http://schemas.android.com/apk/res/android";

    private int minWidth = -1;
    private int maxWidth = -1;
    private int minHeight = -1;
    private int maxHeight = -1;

    public MinMaxRecyclerView(Context context) {
        super(context);
    }

    public MinMaxRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MinMaxRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            String minHeightString = attrs.getAttributeValue(ANDROID_NAME_SPACE, "minHeight");
            minHeight = dp2px(Float.valueOf(dropUnitEndStr(minHeightString)).intValue());

            String maxHeightString = attrs.getAttributeValue(ANDROID_NAME_SPACE, "maxHeight");
            maxHeight = dp2px(Float.valueOf(dropUnitEndStr(maxHeightString)).intValue());

            String minWidthString = attrs.getAttributeValue(ANDROID_NAME_SPACE, "minWidth");
            minWidth = dp2px(Float.valueOf(dropUnitEndStr(minWidthString)).intValue());

            String maxWidthString = attrs.getAttributeValue(ANDROID_NAME_SPACE, "maxWidth");
            maxWidth = dp2px(Float.valueOf(dropUnitEndStr(maxWidthString)).intValue());
        }
    }

    private static String dropUnitEndStr(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s.substring(0, s.length() - UNIT_DIP.length());
        }
        return "-1";
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (minHeight >= 0) {
            setMeasuredDimension(getMeasuredWidth(), Math.max(getMeasuredHeight(), minHeight));
        }

        if (maxHeight >= 0) {
            setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeight(), maxHeight));
        }

        if (minWidth >= 0) {
            setMeasuredDimension(Math.max(getMeasuredWidth(), minWidth), getMeasuredHeight());
        }

        if (maxWidth >= 0) {
            setMeasuredDimension(Math.min(getMeasuredWidth(), maxWidth), getMeasuredHeight());
        }
    }
}
