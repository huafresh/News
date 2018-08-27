package hua.news.module_common.base;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.utils.ToastUtil;
import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;
import com.example.hua.framework.wrapper.loadlayout.LoadService;

import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_common.loadviews.LoadingView;

/**
 * 基类fragment。
 *
 * @author hua
 * @date 2017/6/8
 */
public abstract class BaseFragment extends Fragment {

    protected LayoutInflater mLayoutInflater;
    protected Activity mActivity;
    protected LoadService mLoadService;
    protected View rootView;
    protected BaseViewModel baseViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        int layoutId = getLayoutId();
        if (layoutId != -1) {
            if (rootView == null) {
                rootView = inflater.inflate(layoutId, container, false);
            }
            mLoadService = LoadLayoutManager.getInstance().wrapper(rootView);
            view = mLoadService.getContainerLayout();
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        initViews();
        setListeners();
    }

    @CallSuper
    protected void initDatas() {

    }

    @CallSuper
    protected void initViews() {

    }

    @CallSuper
    protected void setListeners() {
        baseViewModel.toast.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!TextUtils.isEmpty(s)) {
                    ToastUtil.toast(mActivity, s);
                }
            }
        });
        baseViewModel.pageStatus.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer) {
                    case BaseViewModel.STATUS_LOADING:
                        showLoading();
                        break;
                    case BaseViewModel.STATUS_ERROR:
                        showError();
                        break;
                    case BaseViewModel.STATUS_COMPLETE:
                        showComplete();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    protected int getLayoutId() {
        return -1;
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showLoadView(LoadingView.class.getCanonicalName());
        }
    }

    protected void showError() {
        if (mLoadService != null) {
            mLoadService.showLoadView(LoadErrorView.class.getCanonicalName());
        }
    }

    protected void showComplete() {
        if (mLoadService != null) {
            mLoadService.showLoadComplete();
        }
    }

}
