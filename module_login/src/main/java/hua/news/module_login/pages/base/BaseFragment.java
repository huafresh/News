package hua.news.module_login.pages.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;
import com.example.hua.framework.wrapper.loadlayout.LoadService;


/**
 * 基类fragment，做一些全局的操作
 *
 * @author hua
 * @date 2017/6/8
 */
public abstract class BaseFragment extends Fragment {

    protected View mView;
    protected LayoutInflater mLayoutInflater;
    protected Activity mActivity;
    protected LoadService mLoadService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    /**
     * 创建fragment的视图
     * @param inflater 布局构造器
     * @param container 容器
     * @return fragment的视图
     */
    protected abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 参考{@link LoadLayoutManager}。
     * 上述功能被封装进了BaseFragment，如果需要此功能，可复写此方法返回false
     */
    protected boolean isEnableLoadManager(){
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = onCreateView(inflater, container);
        View wrapperView = mView;
        if (isEnableLoadManager()) {
            mLoadService = LoadLayoutManager.getInstance().wrapper(mView);
            wrapperView = mLoadService.getContainerLayout();
        }
        return wrapperView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        initViews();
        setListeners();
    }

//    protected void showLoading(){
//        if (mLoadService != null) {
//            mLoadService.showLoadView(LoadingView.class.getCanonicalName());
//        }
//    }
//
//    protected void showError(){
//        if (mLoadService != null) {
//            mLoadService.showLoadView(LoadErrorView.class.getCanonicalName());
//        }
//    }
//
//    protected void showComplete(){
//        if (mLoadService != null) {
//            mLoadService.showLoadComplete();
//        }
//    }

    protected void initDatas() {

    }

    protected void initViews() {

    }

    protected void setListeners() {

    }

}
