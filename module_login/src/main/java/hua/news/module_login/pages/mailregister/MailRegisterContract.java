package hua.news.module_login.pages.mailregister;

import android.app.Activity;

import com.example.hua.framework.mvpbase.IBasePresenter;

import hua.news.module_login.pages.base.ILoginBaseView;

/**
 * 邮箱注册页面契约类
 *
 * @author hua
 * @date 2017/6/30
 */

public class MailRegisterContract {

    interface MailRegisterView extends ILoginBaseView<MailRegisterPresenter> {

        /**
         * 邮箱后缀选择框返回
         *
         * @param suffix 选择的后缀
         */
        void onSelectMailSuffix(String suffix);

        /**
         * 获取邮箱
         *
         * @return
         */
        String getMail();

        /**
         * 获取手机号
         *
         * @return
         */
        String getPhone();

    }


    interface MailRegisterPresenter extends IBasePresenter<MailRegisterView> {

        /**
         * 打开邮箱后缀选择框
         *
         * @param activity
         */
        void openMailSuffixDialog(Activity activity);

        /** 邮箱注册下一步 */
        void next();
    }
}
