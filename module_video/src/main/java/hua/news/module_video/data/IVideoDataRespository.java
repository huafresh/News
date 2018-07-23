package hua.news.module_video.data;

import com.example.hua.framework.mvpbase.CallBack;

/**
 * 视频类新闻数据仓库接口。
 * 这里是对{@link IVideoDataApi}接口请求得到的原始数据的进一步处理封装
 *
 * @author hua
 * @date 2017/9/15
 */

public interface IVideoDataRespository {

    /**
     * 请求某一时间点之前的图文新闻列表
     *
     * @param count    数量
     * @param isPullUp 是否是上拉发起的请求
     * @param time     时间点
     * @param callBack 回调
     */
    void getVideoNewsListByTime(int count, String time, boolean isPullUp, CallBack callBack);


}
