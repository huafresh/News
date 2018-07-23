package com.example.hua.framework.wrapper.loadlayout;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * <p>
 * 有时界面的显示可能会包含加载中视图、加载错误视图和没有数据视图等。
 * 如果每个页面都分别包含上述视图，然后在需要的时候进行显示和隐藏。这种实现方式显然不够优雅。
 * 使用此类可以轻松简便的实现上述的需求。实现参考了网友实现的LoadSir。
 * <p>
 * 使用方法：
 * 1、调用{@link #createLoadViewPool}方法创建视图池
 * 2、调用{@link #register}方法注册需要进行布局替换的目标，可以是Activity或者view。
 * 在Fragment中使用有点不同，需在onCreateView中添加如下代码：
 * <code>
 * View view = inflater.inflate(R.layout.fragment_normal_news_detail,container,false);
 * LoadService mLoadService = LoadLayoutManager.getInstance().register(view);
 * wrapperView = mLoadService.getContainerLayout();
 * return wrapperView;
 * </code>
 * <p>
 * 3、根据拿到的{@link LoadService}实例，调用{@link LoadService#showLoadView}方法传入特定布局的标识，
 * 显示特定布局。
 *
 * @author hua
 * @version : 2017/9/26
 */

public class LoadLayoutManager {
    private static final String TAG = "LoadManager";
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

    private void addLoadBuilder(LoadViewPool loadViewPool) {
        if (loadViewPool != null) {
            mLoadBuilders.put(0, loadViewPool);
        }
    }

    private void addLoadBuilder(LoadViewPool loadViewPool, int type) {
        if (loadViewPool != null) {
            mLoadBuilders.put(type, loadViewPool);
        }
    }

    public LoadService register(Object target) {
        return register(target, 0);
    }

    /**
     * 注册需要应用加载等布局的目标，可以是Activity、Fragment、View
     *
     * @param target 目标
     * @param type   标识使用哪一个已添加的{@link LoadViewPool}
     * @return 调用此对象的showLoadView方法显示特定的布局
     */
    public LoadService register(Object target, int type) {
        LoadService loadService = null;
        try {
            loadService = createLoadServiceByTarget(target);

            LoadViewPool loadViewPool = mLoadBuilders.get(type);
            List<LoadView> loadViews = loadViewPool.getLoadViews();
            HashMap<String, LoadView> loadViewMap = new HashMap<>();
            for (LoadView loadView : loadViews) {
                loadViewMap.put(loadView.onCreateLoadViewId(), loadView);
            }
            loadService.loadViewMap = loadViewMap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("register: register %s error", target.getClass().getSimpleName()));
        }
        return loadService;
    }

    private static LoadService createLoadServiceByTarget(Object target) {
        LoadService loadService = null;
        ViewGroup oldParent = null;
        View oldView = null;
        Context context = null;

        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            oldParent = (ViewGroup) activity.findViewById(android.R.id.content);
            oldView = oldParent.getChildAt(0);
            context = activity;
        } else if (target instanceof View) {
            oldView = (View) target;
            oldParent = (ViewGroup) oldView.getParent();
            context = oldView.getContext();
        } else {
            Log.e(TAG, "createLoadServiceByTarget: unsupport target");
            return null;
        }

        loadService = new LoadService(context);
        loadService.oldView = oldView;

        if (oldParent != null) {
            int index = oldParent.indexOfChild(oldView);
            oldParent.removeView(oldView);
            //对target是view时，当外界调用mLoadService.getContainerLayout()时，返回的应该始终是
            //loadService.mLayoutContainer，因此不需要add。
            if (target instanceof Activity) {
                oldParent.addView(loadService.mLayoutContainer, index, oldView.getLayoutParams());
            }
        }

        //给target裹上我们的布局容器
        loadService.mLayoutContainer.addView(oldView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return loadService;
    }


    public static LoadViewPool createLoadViewPool() {
        return createLoadViewPool(0);
    }

    /**
     * 获取参数构造器
     *
     * @param type 布局池的唯一标识
     * @return 布局池实例
     */
    public static LoadViewPool createLoadViewPool(int type) {
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

        public void register() {
            LoadLayoutManager.getInstance().addLoadBuilder(this);
        }

        List<LoadView> getLoadViews() {
            return mLoadViews;
        }
    }


}
