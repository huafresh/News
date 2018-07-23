package hua.news.module_login.pages.phoneregister;

import com.example.hua.framework.mvpbase.IBasePresenter;

import hua.news.module_login.pages.base.ILoginBaseView;

/**
 * 手机号注册页面契约类
 *
 * @author hua
 * @date 2017/6/30
 */

public class PhoneRegisterContract {

    interface MailRegisterView extends ILoginBaseView<MailRegisterPresenter> {
        /**
         * 获取邮箱
         * @return 邮箱
         */
        String getMail();

        /**
         * 获取手机号
         * @return 手机号
         */
        String getPhone();
    }


    interface MailRegisterPresenter extends IBasePresenter<MailRegisterView> {
        /**
         * 手机号注册下一步
         */
        void next();
    }
}
