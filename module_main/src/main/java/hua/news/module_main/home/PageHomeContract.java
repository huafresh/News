package hua.news.module_main.home;

import android.support.v4.app.Fragment;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

import java.util.List;

/**
 * 首页契约类
 * <p>
 * Created by hua on 2017/6/5.
 */
class PageHomeContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 初始化页面的TabLayout
         *
         * @param fragments Fragment集合
         * @param titles    标题
         */
        void initHomeTab(List<Fragment> fragments, List<String> titles);

        /**
         * 获取栏目信息失败时调用
         */
        void onGetColumnListFailed();

    }

    interface Presenter extends IBasePresenter<View> {

        /**
         * 请求服务器获取栏目数据
         */
        void getColumnData();
    }
}
