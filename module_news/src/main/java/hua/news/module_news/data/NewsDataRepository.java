package hua.news.module_news.data;

import android.os.Bundle;

import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.network.MySimpleDisposable;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.network.NetworkHelper;
import com.example.hua.framework.network.RxFlowableHelper;

import java.util.List;

import hua.news.module_common.FrameworkInitializer;
import hua.news.module_common.constants.UrlCosntant;
import hua.news.module_service.entitys.CollectBean;
import hua.news.module_service.entitys.NormalNewsDetailBean;
import hua.news.module_service.entitys.NormalNewsEntity;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * 请求新闻数据管理类
 *
 * @author hua
 * @date 2017/8/18
 */

public class NewsDataRepository implements INewsDataRepository {


    private final Retrofit mRetrofit;
    private final INewsDataApi mNewsRequestApi;

    public NewsDataRepository() {
        mRetrofit = NetworkHelper.getRetrofit(UrlCosntant.URL_BASE);
        mNewsRequestApi = mRetrofit.create(INewsDataApi.class);
    }

    public static NewsDataRepository getInstance() {
        return NewsDataRepository.HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final NewsDataRepository sInstance = new NewsDataRepository();
    }

    @Override
    public void getNormalNewsLastedList(String channel, int count, final CallBack callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.getNormalNewsList(
                UrlCosntant.URL_CHANNEL_NEWS_LIST, channel, String.valueOf(count), "0", "");
        handleToNormalNewsList(callBack, response);
    }

    private void handleToNormalNewsList(final CallBack callBack, Flowable<ResponseBody> response) {
        RxFlowableHelper.handlerToList(response, NormalNewsEntity.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable<List<NormalNewsEntity>>() {
                    @Override
                    protected void onSuccess(List<NormalNewsEntity> list) {
                        callBackSuccess(callBack, list);
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void getNormalNewsListByTime(String channel, int count, String time, CallBack callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.getNormalNewsList(
                UrlCosntant.URL_CHANNEL_NEWS_LIST, channel, String.valueOf(count), "1", time);
        handleToNormalNewsList(callBack, response);
    }

    @Override
    public void addCollect(String user_id, String news_id, final CallBack<?> callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.addCollect(
                UrlCosntant.URL_COLLECT_ADD, user_id, news_id);
        RxFlowableHelper.handleIsOk(response)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable() {
                    @Override
                    protected void onSuccess(Object o) {
                        callBackSuccess(callBack, "收藏成功");
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void cancelCollect(String user_id, String news_id, final CallBack<?> callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.addCollect(
                UrlCosntant.URL_COLLECT_CANCEL, user_id, news_id);
        RxFlowableHelper.handleIsOk(response)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable() {
                    @Override
                    protected void onSuccess(Object o) {
                        callBackSuccess(callBack, "取消收藏成功");
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void queryCollectList(String user_id, final CallBack<?> callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.queryCollectList(
                UrlCosntant.URL_COLLECT_QUERY, user_id);
        RxFlowableHelper.handlerToList(response, CollectBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable<List<CollectBean>>() {
                    @Override
                    protected void onSuccess(List<CollectBean> list) {
                        callBackSuccess(callBack, list);
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void getNormalNewsDetail(String news_id, final CallBack callBack) {
        Flowable<ResponseBody> response = mNewsRequestApi.getNewsDetail(
                UrlCosntant.URL_NORMAL_NEWS_DETAIL, news_id);
        RxFlowableHelper.handlerToList(response, NormalNewsDetailBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable<List<NormalNewsDetailBean>>() {
                    @Override
                    protected void onSuccess(List<NormalNewsDetailBean> list) {
                        callBackSuccess(callBack, list.get(0));
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    private void callBackFailed(CallBack<?> callBack, NetResultErrorException e) {
        if (callBack != null) {
            Bundle error = new Bundle();
            error.putString(CallBack.ERROR_INFO, e.getError_info());
            callBack.onError(FrameworkInitializer.getInstance().getContext(), error);
        }
    }

    private void callBackSuccess(CallBack callBack, Object o) {
        if (callBack != null) {
            callBack.onSuccess(FrameworkInitializer.getInstance().getContext(), o);
        }
    }


}
