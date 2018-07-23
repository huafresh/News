package hua.news.module_news.ifengdata;

import java.util.List;

import hua.news.module_news.ifengcommn.IFengNewsListEntity;
import hua.news.module_news.ifengdetail.IFengNewsDetail;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author hua
 * @version 2018/4/3 15:57
 */

public interface IFengNewsApiService {

    /**
     * 请求新闻列表。
     *
     * @param id      频道id
     * @param action  动作。
     *                1：下拉 down
     *                2：上拉 up
     *                3：默认 default
     * @param pullNum 操作次数，递增
     * @return 列表数据
     */
    @GET("ClientNews")
    Flowable<List<IFengNewsListEntity>> getNewsList(@Query("id") String id,
                                                    @Query("action") String action,
                                                    @Query("pullNum") int pullNum
    );

    /**
     * 获取新闻详情，aid以“sub”开头时调用
     *
     * @param aid 文章id
     * @return 新闻详情
     */
    @GET("api_vampire_article_detail")
    Flowable<IFengNewsDetail> getNewsArticleWithSub(@Query("aid") String aid);

    /**
     * 获取新闻详情，aid以“cammp”开头时调用（baseUrl不一致）
     *
     * @param url baseUrl
     * @param aid 新闻id
     * @return 新闻详情
     */
    @GET
    Flowable<IFengNewsDetail> getNewsArticleWithCmpp(@Url String url,
                                                     @Query("aid") String aid);

}
