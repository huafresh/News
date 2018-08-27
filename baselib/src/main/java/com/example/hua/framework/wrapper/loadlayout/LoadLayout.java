package com.example.hua.framework.wrapper.loadlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 各种视图的容器
 *
 * @author hua
 * @version 2018/8/22 18:05
 */

public class LoadLayout extends ViewGroup {

    private static final String TAG = "LoadService";
    /**
     * 被包裹的原始布局
     */
    View originalView;
    /**
     * 可用来替换的布局列表
     */
    HashMap<String, LoadView> loadViewMap;

    private SparseArray<OnClickListener> clickListeners;

    private Context mContext;

    public LoadLayout(Context context) {
        this(context, null);
    }

    public LoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        loadViewMap = new HashMap<>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * 显示指定布局
     */
    public void showLoadView(String loadViewId) {
        LoadView loadView = loadViewMap.get(loadViewId);
        View showView = loadView.getContentView(mContext, this);
        ViewParent parent = showView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(showView);
        }
        removeAllViews();
        ViewGroup.LayoutParams params = null;
        params = showView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        addView(showView, params);
        setShowViewListeners(showView, loadView);
    }

    private void setShowViewListeners(View showView, LoadView loadView) {
        for (Integer id : loadView.clickableIds()) {
            showView.findViewById(id).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListeners.get(v.getId()).onClick(v);
                }
            });
        }
    }

    /**
     * 显示加载完成布局
     */
    public void showLoadComplete() {
        removeAllViews();
        addView(originalView, originalView.getLayoutParams());
    }

    /**
     * 监听视图内控件的点击事件
     *
     * @param id       要添加监听的控件的id
     * @param listener 监听
     */
    public void setOnClickListener(int id, OnClickListener listener) {
        if (clickListeners == null) {
            clickListeners = new SparseArray<>();
        }
        clickListeners.put(id, listener);
    }

}
