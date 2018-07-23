package hua.news.module_common.webview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by hua on 2017/9/18.
 * 自定义的WebView页面需要继承此类
 */

public abstract class BaseWebFragment extends Fragment {

    /** 与此页面关联的WebView */
    private MyWebView mMyWebView;
    /** Fragment页面视图 */
    protected View mRootView;
    protected Activity mActivity;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = context;
    }

    /**
     * 创建页面视图，子类需要把传入进来的WebView对象添加到合适的容器中。
     *
     * @param webView 从WebView缓存池中获取的，不为空
     * @return 此Fragment要展示的页面
     */
    protected abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                         @Nullable Bundle savedInstanceState, @NonNull MyWebView webView);

    /**
     * WebView加载内容时调用
     * @param webView 要加载内容的WebView
     */
    protected abstract void onLoadPage(MyWebView webView);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyWebView = WebViewPool.getInstance(getActivity()).getWebView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = onCreateView(inflater, container, savedInstanceState, mMyWebView);
        return mRootView;
    }

    @Override
    @CallSuper
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadPage(mMyWebView);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        mMyWebView.recycler();
    }



}
