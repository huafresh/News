package hua.news.module_login.pages.verifyregister;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.mvpbase.CallBack;

import hua.news.module_login.LoginManager;
import hua.news.module_login.data.LoginRequestFactory;

/**
 * 校验手机号页面presenter
 *
 * @author hua
 * @date 2017/6/29
 */

public class VerifyPhonePresenter extends BasePresenter<VerifyPhoneContract.AccountLoginView>
        implements VerifyPhoneContract.AccountLoginPresenter {

    private Context mContext;

    public VerifyPhonePresenter() {
        this.mContext = FrameworkInitializer.getInstance().getContext();
    }

    @Override
    public void sendVerifyCode() {
        String phone = getView().getPhone();
        if (!LoginManager.checkPhone(mContext, phone)) {
            return;
        }
        LoginRequestFactory.getInstance().sendVerifyCode(phone, new CallBack<String>() {
            @Override
            public void onSuccess(Context context, String data) {
                if (isViewAttached()) {
                    getView().onSendVerifyCodeSuccess(data);
                }
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (isViewAttached()) {
                    getView().onSendVerifyCodeFailed();
                    Toast.makeText(mContext, error.getString(CallBack.ERROR_INFO), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void register() {
        final String pwd = getView().getPassword();
        String verifyCode = getView().getVerifyCode();
        if (!LoginManager.checkPassword(mContext, pwd)) {
            return;
        }
        if (!LoginManager.checkVerifyCode(mContext, verifyCode)) {
            return;
        }

        getView().hideSoftInput();

        final String mail = getView().getMail();
        final String phone = getView().getPhone();
        LoginRequestFactory.getInstance().CheckVerifyCode(verifyCode, new CallBack() {
            @Override
            public void onSuccess(Context context, Object data) {
                registerNext(mail, phone, pwd);
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (isViewAttached()) {
                    Toast.makeText(mContext, error.getString(CallBack.ERROR_INFO), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerNext(String mail, String phone, String pwd) {
        LoginRequestFactory.getInstance().register(mail, phone, pwd, new CallBack<Object>() {
            @Override
            public void onSuccess(Context context, Object data) {
                if (isViewAttached()) {
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                    getView().exit();
                }
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (isViewAttached()) {
                    Toast.makeText(mContext, error.getString(CallBack.ERROR_INFO), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
