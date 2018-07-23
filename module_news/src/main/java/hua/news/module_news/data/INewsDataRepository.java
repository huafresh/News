package hua.news.module_news.data;

import com.example.hua.framework.mvpbase.CallBack;

/**
 * 新闻数据仓库接口。这里是对{@link INewsDataApi}接口请求得到的原始数据的进一步处理封装
 *
 * @author hua
 * @date 2017/9/15
 */

public interface INewsDataRepository {


    /**
     * 请求最新的图文新闻列表
     *
     * @param channel  频道
     * @param count    数量
     * @param callBack
     */
    void getNormalNewsLastedList(String channel, int count, CallBack callBack);

    /**
     * 请求某一时间点之前的图文新闻列表
     *
     * @param channel  频道
     * @param count    数量
     * @param time     时间点
     * @param callBack 回调
     */
    void getNormalNewsListByTime(String channel, int count, String time, CallBack callBack);

    /**
     * 请求新闻详情数据
     *
     * @param news_id  新闻id
     * @param callBack 回调
     */
    void getNormalNewsDetail(String news_id, CallBack<?> callBack);

    /**
     * 收藏新闻
     *
     * @param user_id  用户id
     * @param news_id  新闻id
     * @param callBack 回调
     */
    void addCollect(String user_id, String news_id, CallBack<?> callBack);

    /**
     * 取消收藏新闻
     *
     * @param user_id  用户id
     * @param news_id  新闻id
     * @param callBack 回调
     */
    void cancelCollect(String user_id, String news_id, CallBack<?> callBack);

    /**
     * 查询用户收藏新闻列表
     *
     * @param user_id  用户id
     * @param callBack 回调
     */
    void queryCollectList(String user_id, CallBack<?> callBack);

}
