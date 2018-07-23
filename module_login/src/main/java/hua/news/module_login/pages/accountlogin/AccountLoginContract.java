package hua.news.module_login.pages.accountlogin;

import com.example.hua.framework.mvpbase.IBasePresenter;
import hua.news.module_login.pages.base.ILoginBaseView;

/**
 * 账号登录页面契约类
 *
 * @author hua
 * @date 2017/6/29
 */

public class AccountLoginContract {

    interface AccountLoginView extends ILoginBaseView<AccountLoginPresenter> {
        /**
         * 获取账号
         *
         * @return 账号
         */
        String getAccount();

        /**
         * 获取密码
         *
         * @return 密码
         */
        String getPassword();

        /**
         * 获取第三方平台名称
         *
         * @return 第三方平台名称
         */
        String getThirdPlatName();

        /**
         * 获取第三方平台唯一码
         *
         * @return 第三方平台唯一码
         */
        String getThirdPlatUniqueId();

    }

    interface AccountLoginPresenter extends IBasePresenter<AccountLoginView> {

        /**
         * 一般的登录
         */
        void login();
        /**
         * 三方登录
         */
        void ThirdLogin(String platName);
    }
}
