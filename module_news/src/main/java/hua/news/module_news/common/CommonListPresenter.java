package hua.news.module_news.common;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.mvpbase.CallBack;

import java.util.List;

import hua.news.module_common.constants.NewsConstant;
import hua.news.module_service.entitys.NormalNewsEntity;
import hua.news.module_news.data.NewsDataRepository;

/**
 * 图文类新闻presenter
 * Created by hua on 2017/6/10.
 */

class CommonListPresenter extends BasePresenter<CommonListContract.View>
        implements CommonListContract.Presenter {


    @Override
    public void getHeadlinesLatestDataList(int count) {
        NewsDataRepository.getInstance().getNormalNewsLastedList(
                NewsConstant.CHANNEL_MAIN, count, getListCallBack);
    }

    @Override
    public void getHeadlinesDataListByTime(int count, String time) {
        NewsDataRepository.getInstance().getNormalNewsListByTime(
                NewsConstant.CHANNEL_MAIN, count, time, getListCallBack);
    }

    private CallBack getListCallBack = new CallBack() {
        @Override
        public void onSuccess(Context context, Object data) {
            if (isViewAttached()) {
                List<NormalNewsEntity> dataList = (List<NormalNewsEntity>) data;
                getView().onGetHeadlinesNewsDataListSuccess(dataList);
            }
        }

        @Override
        public void onError(Context context, Bundle error) {
            if (isViewAttached()) {
                getView().onGetHeadlinesNewsDataListFailed();
                Toast.makeText(context, error.getString(CallBack.ERROR_INFO),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}
