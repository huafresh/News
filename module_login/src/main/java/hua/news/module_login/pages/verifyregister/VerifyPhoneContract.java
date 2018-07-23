package hua.news.module_login.pages.verifyregister;

import com.example.hua.framework.mvpbase.IBasePresenter;

import hua.news.module_login.pages.base.ILoginBaseView;

/**
 * 校验手机号页面契约类
 *
 * @author hua
 * @date 2017/6/29
 */

public class VerifyPhoneContract {

    interface AccountLoginView extends ILoginBaseView<AccountLoginPresenter> {

        /**
         * 获取到短信验证码
         *
         * @param code 短信验证码
         */
        void onSendVerifyCodeSuccess(String code);

        /**
         * 获取短信验证码失败
         */
        void onSendVerifyCodeFailed();

        /**
         * 获取邮箱
         *
         * @return 邮箱
         */
        String getMail();

        /**
         * 获取手机号
         *
         * @return 手机号
         */
        String getPhone();

        /**
         * 获取短信验证码
         *
         * @return 短信验证码
         */
        String getVerifyCode();

        /**
         * 获取密码
         *
         * @return 密码
         */
        String getPassword();

    }

    interface AccountLoginPresenter extends IBasePresenter<AccountLoginView> {

        /**
         * 发送短信验证码
         */
        void sendVerifyCode();

        /**
         * 注册邮箱
         */
        void register();
    }
}
