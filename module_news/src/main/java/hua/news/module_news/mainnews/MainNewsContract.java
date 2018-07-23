package hua.news.module_news.mainnews;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

import java.util.List;

import hua.news.module_service.entitys.NormalNewsEntity;

/**
 * Created by hua on 2017/6/10.
 * 首页头条契约类
 */

public class MainNewsContract {

    interface View extends IBaseView<MainNewsContract.Presenter> {
        /**
         * 获取到要闻数据时调用
         *
         * @param dataList
         */
        void onGetMainNewsData(List<NormalNewsEntity> dataList);
    }

    interface Presenter extends IBasePresenter<MainNewsContract.View> {
        /**
         * 获取要闻数据
         */
        void getMainNewsData();
    }

}
