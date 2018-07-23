package hua.news.module_video.data;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * @author hua
 * @version 2017/11/22 17:10
 */

public interface IVideoDataApi {

    /**
     * 请求视频类型新闻的列表信息
     *
     * @param function 接口后缀
     * @param count    请求数量
     * @param isPullUp 是否是上拉发起的请求
     *                 下拉刷新，返回time之后的数据
     *                 上拉加载，返回time之前的数据
     * @param time     最后刷新时间。为空返回最新列表数据
     * @return 新闻列表信息
     */
    @GET("{function}")
    Flowable<ResponseBody> getVideoNewsList(
            @Path("function") String function,
            @Header("count") String count,
            @Header("isPullUp") String isPullUp,
            @Header("time") String time);

}
