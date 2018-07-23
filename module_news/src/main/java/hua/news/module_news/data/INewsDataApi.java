package hua.news.module_news.data;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hua on 2017/8/18.
 * 新闻数据retrofit api接口
 */

public interface INewsDataApi {

    /**
     * 请求图文类型新闻的列表信息
     *
     * @param channel  频道名称
     * @param count    请求数量
     * @param isPullUp 是否是上拉发起的请求
     * @param time     如果isPullUp为true，则此参数为当前列表中最后一条新闻的发布时间。否则此参数无效
     * @return 新闻列表信息
     */
    @GET("{function}")
    Flowable<ResponseBody> getNormalNewsList(
            @Path("function") String function,
            @Header("channel") String channel,
            @Header("count") String count,
            @Header("isPullUp") String isPullUp,
            @Header("time") String time);

    /**
     * 获取新闻详情
     *
     * @param function 接口地址
     * @param news_id  新闻id
     * @return 新闻详情
     */
    @GET("{function}")
    Flowable<ResponseBody> getNewsDetail(
            @Path("function") String function,
            @Header("news_id") String news_id);


    /**
     * 添加一条收藏
     *
     * @param function 接口地址
     * @param user_id  用户id
     * @param news_id  要收藏的新闻id
     * @return 是否成功
     */
    @GET("{function}")
    Flowable<ResponseBody> addCollect(
            @Path("function") String function,
            @Header("user_id") String user_id,
            @Header("news_id") String news_id);

    /**
     * 取消一条收藏
     *
     * @param function 接口地址
     * @param user_id  用户id
     * @param news_id  要取消收藏的新闻id
     * @return 是否成功
     */
    @GET("{function}")
    Flowable<ResponseBody> cancelCollect(
            @Path("function") String function,
            @Header("user_id") String user_id,
            @Header("news_id") String news_id);

    /**
     * 查询用户的收藏列表信息
     *
     * @param function 接口地址
     * @param user_id  用户id
     * @return 收藏列表
     */
    @GET("{function}")
    Flowable<ResponseBody> queryCollectList(
            @Path("function") String function,
            @Header("user_id") String user_id);


}
