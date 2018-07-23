package hua.news.module_video.ifengdata;

import java.util.List;

import hua.news.module_service.entitys.VideoChannelBean;
import hua.news.module_service.entitys.VideoDetailBean;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author hua
 * @version 2018/4/27 14:50
 */

public interface IFengVideoApiService {

    /**
     * 获取视频频道
     *
     * @param page 目前横为1
     * @return 频道列表信息
     */
    @GET("ifengvideoList")
    Flowable<List<VideoChannelBean>> getVideoChannel(@Query("page") int page);

    /**
     * 获取视频列表
     * @param page
     * @param listType
     * @param typeId
     * @return
     */
    @GET("ifengvideoList")
    Observable<List<VideoDetailBean>> getVideoDetail(@Query("page") int page,
                                                     @Query("listtype") String listType,
                                                     @Query("typeid") String typeId);
}
