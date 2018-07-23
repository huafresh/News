package hua.news.module_login.pages.accountlogin;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.utils.CommonUtil;

import hua.news.module_login.data.LoginConstant;
import hua.news.module_login.data.LoginRequestFactory;

/**
 * 账号登录页面presenter
 *
 * @author hua
 * @date 2017/6/29
 */

public class AccountLoginPresenter extends BasePresenter<AccountLoginContract.AccountLoginView>
        implements AccountLoginContract.AccountLoginPresenter {


    @Override
    public void login() {
        String account = getView().getAccount();
        String loginType = "";
        if (CommonUtil.isPhone(account)) {
            loginType = LoginConstant.LOGIN_TYPE_PHONE;
        } else if (CommonUtil.isEmail(account)) {
            loginType = LoginConstant.LOGIN_TYPE_MAIL;
        } else {
            Toast.makeText(FrameworkInitializer.getInstance().getContext(),
                    "账号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        getView().hideSoftInput();
        String password = getView().getPassword();
        getView().showLoading("请稍后");
        LoginRequestFactory.getInstance().login(account, password, loginType, new CallBack() {
            @Override
            public void onSuccess(Context context, Object data) {
                if (isViewAttached()) {
                    getView().closeLoading();
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    getView().exit();
                }
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (isViewAttached()) {
                    getView().closeLoading();
                    Toast.makeText(context, error.getString(CallBack.ERROR_INFO),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void ThirdLogin(String platName) {
        switch (platName) {
            case LoginConstant.LOGIN_THIRD_SINA:
                break;
            case LoginConstant.LOGIN_THIRD_QQ:
                break;
            case LoginConstant.LOGIN_THIRD_WECHAT:
                break;
            default:
                break;
        }

        ThridLoginNext();
    }

    private void ThridLoginNext() {
        String loginType = LoginConstant.LOGIN_TYPE_THIRD;

        // TODO: 2017/6/29
        LoginRequestFactory.getInstance().login("", "", loginType, new CallBack() {
            @Override
            public void onSuccess(Context context, Object data) {
                if (isViewAttached()) {
                    getView().closeLoading();
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    getView().exit();
                }
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (isViewAttached()) {
                    getView().closeLoading();
                    Toast.makeText(context, error.getString(CallBack.ERROR_INFO),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
