package hua.news.module_login.pages.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.hua.framework.LoadingDialog;

import com.example.hua.framework.utils.CommonUtil;

/**
 * 登录页面的base activity，主要处理了一些常用的view层操作
 *
 * @author hua
 * @date 2017/6/29
 */

public class LoginBaseActivity<T> extends BaseActivity implements ILoginBaseView<T> {

    protected LoadingDialog mLoadingDialog;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog(this);
    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading(CharSequence text) {
        if (mLoadingDialog != null) {
            mLoadingDialog.show(text);
        }
    }

    @Override
    public void closeLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showSoftInput(View view) {
        CommonUtil.showSystemKeyBoard(view);
    }

    @Override
    public void hideSoftInput() {
        CommonUtil.hideSystemKeyBoard(findViewById(android.R.id.content).getRootView());
    }

    @Override
    public void exit() {
        finish();
    }
}
