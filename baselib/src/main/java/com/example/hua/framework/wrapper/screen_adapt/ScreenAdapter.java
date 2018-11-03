package com.example.hua.framework.wrapper.screen_adapt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 屏幕适配分为两种情况讨论：
 * <p>
 * 1、如果页面刚开始开发，则可以调用{@link #adapt(Activity)}对整个页面进行适配。
 * 适配方案参考今日头条的。
 * <p>
 * 2、如果是局部UI调整，一般来说，只需要按之前设计时的比例就行了，虽然会偏大or
 * 偏小，但是页面整体看起来是可以的。但是可能会遇到局部UI也要精准匹配设计图的情况。
 * 这时候可以使用{@link #adapt(View)}方法进行适配。这个方法的适配方案是根据View的
 * 各种属性值反推原来的dp值，然后再用设计图的尺寸重新计算每个属性值，最后设置回去。
 * 目前只对常见的属性做了处理，可以调用{@link #addAttrType(AttrType)}进行扩展，
 * 参考{@link AttrType}。
 *
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
public class ScreenAdapter {

    private List<AttrType> attrTypes = new ArrayList<>();
    private Context context;

    public static ScreenAdapter get() {
        return Instance.S_INSTANCE;
    }

    private static final class Instance {
        @SuppressLint("StaticFieldLeak")
        private static final ScreenAdapter S_INSTANCE = new ScreenAdapter();
    }

    private ScreenAdapter() {
        attrTypes.addAll(AttrType.getAttrTypeList());
    }

    public void adapt(Activity activity) {
        if (context == null) {
            context = activity.getApplicationContext();
            DesignScreen.init(context);
        }
    }

    public void adapt(final View view) {
        if (context == null) {
            context = view.getContext().getApplicationContext();
            DesignScreen.init(context);
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                List<View> views = new ArrayList<>();
                collectViews(view, views);

                for (View v : views) {
                    for (AttrType attrType : attrTypes) {
                        attrType.adapt(v);
                    }
                    ViewGroup.LayoutParams params = v.getLayoutParams();
                    v.setLayoutParams(params);
                }
            }
        });
    }

    private void collectViews(View view, List<View> result) {
        if (view instanceof ViewGroup) {
            result.add(view);
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                collectViews(child, result);
            }
        } else {
            result.add(view);
        }
    }

    public void addAttrType(AttrType attrType) {
        attrTypes.add(attrType);
    }

}
