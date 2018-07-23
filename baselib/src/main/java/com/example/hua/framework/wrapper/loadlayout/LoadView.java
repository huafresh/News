package com.example.hua.framework.wrapper.loadlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 布局抽象
 *
 * @date: 2017/9/26
 * @author hua
 */

public abstract class LoadView {

    /**
     * 获取布局的视图
     *
     * @return 布局的视图
     */
    protected View getContentView(Context context, ViewGroup container, LoadService loadService) {
        return null;
    }

    /**
     * 给此LoadView设置唯一标识
     * 默认的实现是使用全类名
     *
     * @return LoadView唯一标识
     */
    protected String onCreateLoadViewId() {
        return getClass().getCanonicalName();
    }

    public interface ReLoadListener {
        /**
         * 重新加载时调用
         */
        void onReLoad(View v);
    }
}
