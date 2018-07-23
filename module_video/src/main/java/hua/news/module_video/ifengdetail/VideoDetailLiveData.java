package hua.news.module_video.ifengdetail;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import hua.news.module_common.net.IFengApi;
import hua.news.module_service.entitys.VideoDetailBean;
import hua.news.module_video.ifengdata.IFengVideoApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hua
 * @version 2018/4/28 14:52
 */

public class VideoDetailLiveData extends MutableLiveData<List<VideoDetailBean>> {
    private String typeId;
    private IFengVideoApiService iFengVideoApiService;
    private int pageNum = 1;

    public VideoDetailLiveData(String typeId) {
        this.typeId = typeId;
        iFengVideoApiService = IFengApi.getInstance().createIFengService(IFengVideoApiService.class);
    }

    @Override
    protected void onActive() {
        getData();
    }

    public void getData(){
        pageNum = 1;
        iFengVideoApiService.getVideoDetail(pageNum,"list",typeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoDetailBean>>() {
                    @Override
                    public void accept(List<VideoDetailBean> videoDetailBeans) throws Exception {
                        if (pageNum==1) {
                            setValue(videoDetailBeans);
                        } else {
                            List<VideoDetailBean> oldList = getValue();
                            if (oldList != null) {
                                oldList.addAll(videoDetailBeans);
                                setValue(oldList);
                            }
                        }
                    }
                });
    }

    public void loadMore(){
        pageNum++;
        getData();
    }

}
