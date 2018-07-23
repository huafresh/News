package com.example.hua.framework.wrapper.loadlayout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */
public class LoadService {

    public static final String TAG = "LoadService";
    /**
     * 显示布局的容器，LoadService操作的对象
     */
    FrameLayout mLayoutContainer;
    /**
     * 原布局
     */
    View oldView;
    /**
     * 可用来替换的布局列表
     */
    HashMap<String, LoadView> loadViewMap;
    /**
     * 重新加载监听
     */
    private HashMap<String, List<LoadView.ReLoadListener>> mReloadListenerMap;

    private Context mContext;

    public LoadService(Context context) {
        mContext = context;
        mReloadListenerMap = new HashMap<>();
        mLayoutContainer = new FrameLayout(mContext);
        loadViewMap = new HashMap<>();
    }

    /**
     * 显示指定布局
     */
    public void showLoadView(String loadViewId) {
        try {
            LoadView loadView = loadViewMap.get(loadViewId);
            View showView = loadView.getContentView(mContext, mLayoutContainer, this);
            mLayoutContainer.removeAllViews();
            ViewGroup.LayoutParams params = null;
            params = showView.getLayoutParams();
            if (params == null) {
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
            mLayoutContainer.addView(showView, params);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("showLoadView: show %s error", loadViewId));
        }
    }

    /**
     * 显示加载完成布局
     */
    public void showLoadComplete() {
        mLayoutContainer.removeAllViews();
        mLayoutContainer.addView(oldView, oldView.getLayoutParams());
    }

    /**
     * 添加重新加载监听
     *
     * @param loadViewId 要添加监听的布局的唯一标识
     * @param listener   监听
     */
    public void addReloadListener(String loadViewId, LoadView.ReLoadListener listener) {
        List<LoadView.ReLoadListener> listenerList = mReloadListenerMap.get(loadViewId);
        if (listenerList == null) {
            listenerList = new ArrayList<>();
        }
        listenerList.add(listener);
        mReloadListenerMap.put(loadViewId, listenerList);
    }

    /**
     * 执行重新加载监听
     */
    public void executeReloadListeners(String loadViewId, View v) {
        List<LoadView.ReLoadListener> listeners = mReloadListenerMap.get(loadViewId);
        if (listeners != null) {
            for (LoadView.ReLoadListener listener : listeners) {
                listener.onReLoad(v);
            }
        }
    }

    public View getContainerLayout() {
        return mLayoutContainer;
    }

}
