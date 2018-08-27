package com.example.hua.framework.wrapper.loadlayout;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * <p>
 * 有时界面的显示可能会包含以下几个视图：加载中视图、加载错误视图和没有数据视图等。
 * 如果每个页面都分别包含上述视图，然后在需要的时候进行显示和隐藏的话，工作量就大了。
 * 使用此类可以轻松简便的实现上述需求。实现参考了网友实现的LoadSir。
 * <p>
 * 使用方法：
 * 1、调用{@link #buildLoadViewPool}方法创建视图池，即加载中视图、加载错误视图和没有数据视图等等
 * 2、调用{@link #wrapper}方法包裹需要进行布局替换的目标，可以是Activity或者view。
 * <p>
 * 3、使用返回的{@link LoadLayout}控制相关视图的显示和隐藏
 *
 * @author hua
 * @version : 2017/9/26
 */

public class LoadLayoutManager {
    private static final String TAG = "LoadLayoutManager";
    public static final int DEFAULT_POOL_TYPE = 0;
    private SparseArray<LoadViewPool> mLoadBuilders;

    private LoadLayoutManager() {
        mLoadBuilders = new SparseArray<>();
    }

    public static LoadLayoutManager getInstance() {
        return HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final LoadLayoutManager sInstance = new LoadLayoutManager();
    }

    private void addLoadBuilder(LoadViewPool loadViewPool, int type) {
        if (loadViewPool != null) {
            mLoadBuilders.put(type, loadViewPool);
        }
    }

    public LoadLayout wrapper(Activity target) {
        return wrapper(target, 0);
    }

    public LoadLayout wrapper(Activity target, int type) {
        return wrapper((Object) target, type);
    }

    public LoadLayout wrapper(View target) {
        return wrapper(target, 0);
    }

    public LoadLayout wrapper(View target, int type) {
        return wrapper((Object) target, type);
    }

    /**
     * 注册需要应用加载等布局的目标，可以是Activity、Fragment、View
     *
     * @param target 目标
     * @param type   标识使用哪一个已添加的{@link LoadViewPool}
     * @return 可调用此对象的showLoadView方法显示特定的布局
     */
    private LoadLayout wrapper(Object target, int type) {
        LoadLayout loadLayout = null;
        try {
            loadLayout = createLoadLayoutForTarget(target);

            LoadViewPool loadViewPool = mLoadBuilders.get(type);
            List<LoadView> loadViews = loadViewPool.getLoadViewPool();
            HashMap<String, LoadView> loadViewMap = new HashMap<>();
            for (LoadView loadView : loadViews) {
                loadViewMap.put(loadView.createLoadViewId(), loadView);
            }
            loadLayout.loadViewMap = loadViewMap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("wrapper: wrapper %s error", target.getClass().getSimpleName()));
        }
        return loadLayout;
    }

    private static LoadLayout createLoadLayoutForTarget(Object target) {
        LoadLayout loadLayout = null;
        ViewGroup oldParent = null;
        View originalView = null;
        Context context = null;

        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            oldParent = (ViewGroup) activity.findViewById(android.R.id.content);
            originalView = oldParent.getChildAt(0);
            context = activity.getApplicationContext();
        } else if (target instanceof View) {
            originalView = (View) target;
            oldParent = (ViewGroup) originalView.getParent();
            context = originalView.getContext().getApplicationContext();
        } else {
            Log.e(TAG, "createLoadLayoutForTarget: unsupport target");
            return null;
        }

        loadLayout = new LoadLayout(context);
        loadLayout.originalView = originalView;

        if (oldParent != null) {
            int index = oldParent.indexOfChild(originalView);
            oldParent.removeView(originalView);
            if (target instanceof Activity) {
                oldParent.addView(loadLayout, index, originalView.getLayoutParams());
            }
        }

        //给target裹上我们的布局容器
        loadLayout.addView(originalView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return loadLayout;
    }


    public static LoadViewPool buildLoadViewPool() {
        return buildLoadViewPool(DEFAULT_POOL_TYPE);
    }


    public static LoadViewPool buildLoadViewPool(int type) {
        return new LoadViewPool(type);
    }

    /**
     * 布局池，存放用于替换的布局
     */
    public static class LoadViewPool {
        private int type;
        private List<LoadView> mLoadViews;

        private LoadViewPool(int type) {
            this.type = type;
            mLoadViews = new ArrayList<>();
        }

        public LoadViewPool addLoadView(LoadView view) {
            if (view != null) {
                mLoadViews.add(view);
            }
            return this;
        }

        public void build() {
            LoadLayoutManager.getInstance().addLoadBuilder(this, type);
        }

        List<LoadView> getLoadViewPool() {
            return mLoadViews;
        }
    }


}
