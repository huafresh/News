package hua.news.module_news.detail;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

/**
 * 图文类新闻mvp接口定义
 *
 * @author thinkive
 * @date 2017/9/29
 */

public interface NewsDetailContract {

    interface View extends IBaseView<NewsDetailContract.Presenter> {

        /**
         * 设置跳转类型。
         * 跳转类型决定了Activity返回时的动作
         *
         * @param type 跳转类型
         * @see NewsDetailActivity.JumpType
         */
        void setJumpType(@NewsDetailActivity.JumpType int type);

        /**
         * 添加收藏或者取消收藏后调用
         *
         * @param type      add表示添加，cancel表示取消
         * @param isSuccess 是否成功
         */
        void onCollectResult(String type, boolean isSuccess);
    }

    interface Presenter extends IBasePresenter<NewsDetailContract.View> {
        /**
         * 添加收藏
         * @param news_id 新闻id
         */
        void addCollect(String news_id);

        /**
         * 取消收藏
         * @param news_id 新闻id
         */
        void cancelCollect(String news_id);
    }

}
