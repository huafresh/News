package hua.news.module_login.pages.base;

import android.view.View;

import com.example.hua.framework.mvpbase.IBaseView;

/**
 * 登录相关页面的通用view接口
 *
 * @author hua
 * @date 2017/6/29
 */

public interface ILoginBaseView<T> extends IBaseView<T> {

    /**
     * 显示加载中进度条
     */
    void showLoading(CharSequence text);

    /**
     * 关闭加载中进度条
     */
    void closeLoading();

    /**
     * 显示系统键盘
     */
    void showSoftInput(View view);

    /**
     * 隐藏系统键盘
     */
    void hideSoftInput();

    /**
     * 关闭页面
     */
    void exit();

}
