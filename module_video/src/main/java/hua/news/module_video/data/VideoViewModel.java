package hua.news.module_video.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.storage.StorageManager;

import java.util.ArrayList;
import java.util.List;

import hua.news.module_common.base.BaseApplication;
import hua.news.module_common.constants.NewsConstant;
import hua.news.module_service.entitys.VideoNewsEntity;
import hua.news.module_service.entitys.VideoPathEntity;

/**
 * 视频类新闻数据ViewModel
 *
 * @author hua
 * @version 2017/11/22 17:36
 * @see ViewModel
 */

public class VideoViewModel extends AndroidViewModel {

    /** 刷新一次请求的视频数量 */
    private static final int REFRESH_COUNT = 20;

    public MutableLiveData<List<VideoNewsEntity>> mVideoList;
    public MutableLiveData<String> mToastInfo;
    public MutableLiveData<Boolean> mIsSuccess;
    public LiveData<List<VideoPathEntity>> mVideoPath;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        mVideoList = new MutableLiveData<>();
        mToastInfo = new MutableLiveData<>();
        mIsSuccess = new MutableLiveData<>();
        mVideoPath = Transformations.map(mVideoList, new Function<List<VideoNewsEntity>, List<VideoPathEntity>>() {
            @Override
            public List<VideoPathEntity> apply(List<VideoNewsEntity> input) {
                return convertToPath(input);
            }
        });
    }

    @NonNull
    private List<VideoPathEntity> convertToPath(List<VideoNewsEntity> input) {
        List<VideoPathEntity> pathList = new ArrayList<>();
        if (input != null) {
            for (VideoNewsEntity videoNewsEntity : input) {
                VideoPathEntity pathEntity = new VideoPathEntity();
                pathEntity.setUrl_360p(videoNewsEntity.get_360p_url());
                pathEntity.setUrl_480p(videoNewsEntity.get_480p_url());
                pathEntity.setUrl_720p(videoNewsEntity.get_720p_url());
                pathList.add(pathEntity);
            }
        }
        return pathList;
    }


    /**
     * 下拉刷新
     */
    public void refreshWithPullDown() {
        String time = StorageManager.getInstance(BaseApplication.getContext())
                .getFromDisk(NewsConstant.KEY_VIDEO_LIST_MAX_TIME);
        VideoDataRespository.getInstance().getVideoNewsListByTime(REFRESH_COUNT, time, false,
                new CallBack() {
                    @Override
                    public void onSuccess(Context context, Object data) {
                        mVideoList.setValue((List<VideoNewsEntity>) data);
                        saveTime((List<VideoNewsEntity>) data);
                        mIsSuccess.setValue(Boolean.TRUE);
                    }

                    @Override
                    public void onError(Context context, Bundle error) {
                        mToastInfo.setValue(error.getString(CallBack.ERROR_INFO));
                        mIsSuccess.setValue(Boolean.FALSE);
                    }
                });
    }

    /**
     * 上拉加载
     */
    public void refreshWithPullUp() {
        String time = StorageManager.getInstance(BaseApplication.getContext())
                .getFromDisk(NewsConstant.KEY_VIDEO_LIST_MIN_TIME);
        VideoDataRespository.getInstance().getVideoNewsListByTime(REFRESH_COUNT, time, true,
                new CallBack() {
                    @Override
                    public void onSuccess(Context context, Object data) {
                        mVideoList.setValue((List<VideoNewsEntity>) data);
                        saveTime((List<VideoNewsEntity>) data);
                        mIsSuccess.setValue(Boolean.TRUE);
                    }

                    @Override
                    public void onError(Context context, Bundle error) {
                        mToastInfo.setValue(error.getString(CallBack.ERROR_INFO));
                        mIsSuccess.setValue(Boolean.FALSE);
                    }
                });
    }

    private void saveTime(List<VideoNewsEntity> list) {
        if (list != null) {
            long minTime = -1;
            long maxTime = -1;
            for (VideoNewsEntity entity : list) {
                long time = Long.valueOf(entity.getDate());
                if (minTime == -1 && maxTime == -1) {
                    minTime = time;
                    maxTime = time;
                } else {
                    minTime = Math.min(maxTime, Math.min(minTime, time));
                    maxTime = Math.max(maxTime, Math.max(minTime, time));
                }
            }
            Context context = BaseApplication.getContext();
            StorageManager.getInstance(context).saveToDisk(NewsConstant.KEY_VIDEO_LIST_MIN_TIME,
                    String.valueOf(minTime));
            StorageManager.getInstance(context).saveToDisk(NewsConstant.KEY_VIDEO_LIST_MAX_TIME,
                    String.valueOf(maxTime));
        }
    }

}
