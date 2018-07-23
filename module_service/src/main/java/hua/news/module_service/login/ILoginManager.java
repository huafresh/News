package hua.news.module_service.login;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

import hua.news.module_service.entitys.LoginUserInfo;


/**
 * 登录模块对外服务
 *
 * @author hua
 * @version 2018/3/24 17:16
 */

public interface ILoginManager extends IProvider{

    boolean isLogin();

    void openAccountLogin(Context context);

    void openPhoneRegister(Context context);

    void openMailRegister(Activity activity);

    void openVerifyPhone(Context context, String mail, String phone);

    LoginUserInfo getUserInfo();

    void clearUserInfo();

}
