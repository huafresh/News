package hua.news.module_video.data;

import android.os.Bundle;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.network.MySimpleDisposable;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.network.NetworkHelper;
import com.example.hua.framework.network.RxFlowableHelper;

import java.util.List;

import hua.news.module_common.constants.UrlCosntant;
import hua.news.module_service.entitys.VideoNewsEntity;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * @author hua
 * @version 2017/11/22 17:16
 * @see IVideoDataRespository
 */

public class VideoDataRespository implements IVideoDataRespository {

    private final Retrofit mRetrofit;
    private final IVideoDataApi mVideoRequestApi;

    public VideoDataRespository() {
        mRetrofit = NetworkHelper.getRetrofit(UrlCosntant.URL_BASE);
        mVideoRequestApi = mRetrofit.create(IVideoDataApi.class);
    }

    public static VideoDataRespository getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final VideoDataRespository sInstance = new VideoDataRespository();
    }

    @Override
    public void getVideoNewsListByTime(int count, String time, boolean isPullUp, final CallBack callBack) {
        Flowable<ResponseBody> response = mVideoRequestApi.getVideoNewsList(
                UrlCosntant.URL_VIDEO_LIST, String.valueOf(count),
                isPullUp ? "1" : "0", time);
        RxFlowableHelper.handlerToList(response, VideoNewsEntity.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable<List<VideoNewsEntity>>() {
                    @Override
                    protected void onSuccess(List<VideoNewsEntity> videoNewsEntities) {
                        callBackSuccess(callBack, videoNewsEntities);
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });

    }

    private void callBackSuccess(CallBack callBack, Object o) {
        if (callBack != null) {
            callBack.onSuccess(FrameworkInitializer.getInstance().getContext(), o);
        }
    }


    private void callBackFailed(CallBack<?> callBack, NetResultErrorException e) {
        if (callBack != null) {
            Bundle error = new Bundle();
            error.putString(CallBack.ERROR_INFO, e.getError_info());
            callBack.onError(FrameworkInitializer.getInstance().getContext(), error);
        }
    }

}
