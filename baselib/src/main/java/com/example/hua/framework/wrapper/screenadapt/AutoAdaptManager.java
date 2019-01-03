package com.example.hua.framework.wrapper.screenadapt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;

/**
 * @author hua
 * @version 2018/9/19 10:34
 */

public class AutoAdaptManager {

    private boolean enable;
    private static final String KEY_DESIGN_WIDTH = "design_width_in_px";
    private static final String KEY_DESIGN_HEIGHT = "design_height_in_px";
    private static final String KEY_ENABLE_ADAPT = "key_auto_adapt_enable_adapt";
    private Context context;
    private DisplayMetricsInfo originDisplayInfo;
    private HashMap<BaseFlag, IDisplayMetricsFactory> baseDisplayMetrics;
    private MetaDataInfo metaDataInfo;


    public static AutoAdaptManager getInstance() {
        return Holder.S_INSTANCE;
    }

    private static final class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final AutoAdaptManager S_INSTANCE = new AutoAdaptManager();
    }

    private AutoAdaptManager() {

    }

    /**
     * init framework
     *
     * @param application Application
     */
    public void init(Application application) {
        this.context = application;
        originDisplayInfo = new DisplayMetricsInfo();
        originDisplayInfo.save(application.getResources().getDisplayMetrics());
        setAutoAdaptListener(new DefaultAdaptListener());
        application.registerActivityLifecycleCallbacks(new ActivityCallback());
        this.enable = true;
    }

    public void Adapt(Activity activity) {
        this.Adapt(activity, BaseFlag.DP_WIDTH_SP_HEIGHT);
    }

    /**
     * 使activity使能屏幕适配，必须在super之前调用
     *
     * @param activity Activity
     * @param baseFlag 适配维度
     */
    public void Adapt(Activity activity, BaseFlag baseFlag) {
        if (!enable) {
            Log.d("AutoAdaptManager", "autoAdapt was canceled, ignore adapt. activity = " + activity.getClass().getName());
            return;
        }

        if (context == null) {
            throw new IllegalStateException("call AutoAdaptManager#init() first");
        }

        ensureNewDisplayMetricsInfo();
        ensureMetaData();
        try {
            LayoutInflaterCompat.setFactory2(activity.getLayoutInflater(), new InflaterFactory(baseFlag));
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.getIntent().putExtra(KEY_ENABLE_ADAPT, true);
    }

    private void ensureMetaData() {
        getMetaDataInfo();
    }

    private void ensureNewDisplayMetricsInfo() {
        if (baseDisplayMetrics == null) {
            baseDisplayMetrics = new HashMap<>(4);
            baseDisplayMetrics.put(BaseFlag.DP_WIDTH_SP_WIDTH, new DpWidthSpWidth());
            baseDisplayMetrics.put(BaseFlag.DP_WIDTH_SP_HEIGHT, new DpWidthSpHeight());
            baseDisplayMetrics.put(BaseFlag.DP_HEIGHT_SP_WIDTH, new DpHeightSpWidth());
            baseDisplayMetrics.put(BaseFlag.DP_HEIGHT_SP_HEIGHT, new DpHeightSpHeight());
        }
    }

    private MetaDataInfo getMetaDataInfo() {
        if (metaDataInfo == null) {
            metaDataInfo = new MetaDataInfo();
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(context
                        .getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    metaDataInfo.designWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                    metaDataInfo.designHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
            }
        }
        return metaDataInfo;
    }

    /**
     * 取消适配。全局生效
     */
    public void disable() {
        restoreMetricsInfo();
        this.enable = false;
    }

    public void enable() {
        this.enable = true;
    }

    private void restoreMetricsInfo() {
        originDisplayInfo.restore(context.getResources().getDisplayMetrics());
    }

    private AutoAdaptListener mAutoAdaptListener;

    public interface AutoAdaptListener {
        /**
         * execute adapt.
         * 适配的原理就是在LayoutInflater创建View之前，动态的修改系统DisplayMetrics的值。
         * 参考：https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
         *
         * @param context        Context
         * @param attrInfo       attr info resolve from xml, if not set, use default value.
         * @param displayMetrics change this object base on {@code attrInfo}
         */
        void adapt(Context context, AutoAdaptAttrInfo attrInfo, DisplayMetrics displayMetrics);
    }

    public void setAutoAdaptListener(AutoAdaptListener listener) {
        mAutoAdaptListener = listener;
    }

    private class DefaultAdaptListener implements AutoAdaptListener {

        @Override
        public void adapt(Context context, AutoAdaptAttrInfo attrInfo, DisplayMetrics displayMetrics) {
            if (!attrInfo.disable) {
                IDisplayMetricsFactory factory = baseDisplayMetrics.get(attrInfo.baseFlag);
                if (factory != null) {
                    factory.create(originDisplayInfo, getMetaDataInfo()).restore(displayMetrics);
                }
            } else {
                restoreMetricsInfo();
            }
        }
    }


    private class InflaterFactory implements LayoutInflater.Factory2 {
        private BaseFlag defaultBaseFlag;

        InflaterFactory(BaseFlag baseFlag) {
            this.defaultBaseFlag = baseFlag;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            AutoAdaptAttrInfo attrInfo = AutoAdaptAttrInfo.inflateAttr(context, attrs);
            if (attrInfo.baseFlag == null) {
                attrInfo.baseFlag = defaultBaseFlag;
            }

            if (mAutoAdaptListener != null) {
                mAutoAdaptListener.adapt(context, attrInfo, context.getResources().getDisplayMetrics());
            }

            return null;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return this.onCreateView(null, name, context, attrs);
        }
    }

    private class ActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            boolean enable = activity.getIntent().getBooleanExtra(KEY_ENABLE_ADAPT, false);
            if (!enable) {
                restoreMetricsInfo();
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
