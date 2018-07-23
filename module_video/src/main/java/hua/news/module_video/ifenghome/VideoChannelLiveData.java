package hua.news.module_video.ifenghome;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import hua.news.module_common.net.IFengApi;
import hua.news.module_service.entitys.VideoChannelBean;
import hua.news.module_video.ifengdata.IFengVideoApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hua
 * @version 2018/4/26 19:53
 */

public class VideoChannelLiveData extends MutableLiveData<List<VideoChannelBean>> {

    private IFengVideoApiService service;

    public VideoChannelLiveData() {
        service = IFengApi.getInstance().createIFengService(IFengVideoApiService.class);
    }

    @Override
    protected void onActive() {
        super.onActive();
        getData();
    }

    public void getData(){
        service.getVideoChannel(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VideoChannelBean>>() {
                    @Override
                    public void accept(List<VideoChannelBean> videoChannelBeans) throws Exception {
                        setValue(videoChannelBeans);
                    }
                });

    }
}
