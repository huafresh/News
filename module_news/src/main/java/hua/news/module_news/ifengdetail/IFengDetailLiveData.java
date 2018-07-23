package hua.news.module_news.ifengdetail;

import android.arch.lifecycle.LiveData;

import hua.news.module_common.constants.IFengConstant;
import hua.news.module_common.net.IFengApi;
import hua.news.module_news.ifengdata.IFengNewsApiService;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hua
 * @version 2018/4/8 20:15
 */

public class IFengDetailLiveData extends LiveData<IFengNewsDetail> {
    private String aid;
    private IFengNewsApiService iFengApiService;

    public IFengDetailLiveData(String aid) {
        this.aid = aid;
        iFengApiService = IFengApi.getInstance().createIFengService(IFengNewsApiService.class);
    }

    public void getData() {
        Flowable<IFengNewsDetail> result = null;
        if (aid.startsWith("sub")) {
            result = iFengApiService.getNewsArticleWithSub(aid);
        } else {
            String url = IFengConstant.sGetNewsArticleCmppApi + IFengConstant.sGetNewsArticleDocCmppApi;
            result = iFengApiService.getNewsArticleWithCmpp(url, aid);
        }

        result.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IFengNewsDetail>() {
                    @Override
                    public void accept(IFengNewsDetail iFengNewsDetail) throws Exception {
                        setValue(iFengNewsDetail);
                    }
                });
    }
}
