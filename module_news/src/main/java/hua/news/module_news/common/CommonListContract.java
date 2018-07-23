package hua.news.module_news.common;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

import java.util.List;

import hua.news.module_service.entitys.NormalNewsEntity;

/**
 * 图文新闻列表页面mvp接口定义
 * Created by hua on 2017/6/10.
 */

interface CommonListContract {

    interface View extends IBaseView<CommonListContract.Presenter> {

        /**
         * 获取图文新闻数据失败
         */
        void onGetHeadlinesNewsDataListFailed();

        /**
         * 获取图文新闻数据成功
         *
         * @param dataList 图文新闻数据
         */
        void onGetHeadlinesNewsDataListSuccess(List<NormalNewsEntity> dataList);
    }

    interface Presenter extends IBasePresenter<CommonListContract.View> {
        /**
         * 获取头条新闻最新列表
         *
         * @param count 数量
         */
        void getHeadlinesLatestDataList(int count);

        /**
         * 获取头条新闻某一时间点之前的列表
         *
         * @param count 数量
         * @param time  时间点
         */
        void getHeadlinesDataListByTime(int count, String time);
    }

}
