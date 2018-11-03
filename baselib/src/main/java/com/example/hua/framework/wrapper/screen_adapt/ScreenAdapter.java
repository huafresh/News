package com.example.hua.framework.wrapper.screen_adapt;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 屏幕适配分为两种情况讨论：
 * 1、如果页面刚开始开发，则可以调用{@link #adapt(Activity)}对整个页面进行适配。
 * 2、如果是局部UI调整，一般来说，只需要按之前设计时的比例就行了，虽然会偏大or
 * 偏小，但是页面整体看起来是可以的。但是可能会遇到局部UI也要精准匹配设计图的情况。
 * 这时候可以使用{@link #adapt(View)}方法进行适配。这个方法受限于适配方案，因此
 * 只对常见的尺寸有效。
 *
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
public class ScreenAdapter {

    private List<ITypeCollector> collectors = new ArrayList<>();

    public static ScreenAdapter get() {
        return Instance.S_INSTANCE;
    }

    private static final class Instance {
        private static final ScreenAdapter S_INSTANCE = new ScreenAdapter();
    }

    private ScreenAdapter() {
        collectors.add(new AbsTypeCollector.Base());
        collectors.add(new AbsTypeCollector.TextView());
    }

    public void adapt(Activity activity) {

    }

    /**
     * 对局部UI进行尺寸适配。调用此方法需要View已经测量完毕，
     * 因为适配方案就是根据View的各种属性值反推原来的dp值，
     * 再用设计图的尺寸重新计算每个属性值。
     *
     * 目前支持了常用的属性，可以参考{@link AttrType}。
     * 可以调用{@link #addCollector(ITypeCollector)}进行扩展
     * @param view View
     */
    public void adapt(View view) {
        List<View> views = new ArrayList<>();
        collectViews(view, views);

        for (View v : views) {
            for (ITypeCollector collector : collectors) {
                List<AttrType> attrTypes = collector.collect(v.getClass());
                for (AttrType attrType : attrTypes) {
                    attrType.adapt(v);
                }
            }
        }
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

    public void addCollector(ITypeCollector collector){
        collectors.add(collector);
    }

}
