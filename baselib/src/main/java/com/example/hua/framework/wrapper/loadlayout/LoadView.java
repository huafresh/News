package com.example.hua.framework.wrapper.loadlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * 布局抽象
 *
 * @author hua
 * @date: 2017/9/26
 */

public abstract class LoadView {

    /**
     * 获取布局的视图。每次切换视图时都会调用。
     *
     * @return 布局的视图
     */
    protected View getContentView(Context context, LoadLayout container) {
        return null;
    }

    /**
     * 给此LoadView设置唯一标识，后续进行布局替换时需要传入这个id
     * 默认的实现是使用全类名
     *
     * @return LoadView唯一标识
     */
    protected String createLoadViewId() {
        return getClass().getCanonicalName();
    }

    /**
     * 可点击视图的id。
     *
     * @return ids
     */
    protected List<Integer> clickableIds() {
        return Collections.emptyList();
    }

}
