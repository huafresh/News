package hua.news.module_news.mainnews;

import com.example.hua.framework.mvpbase.BasePresenter;

/**
 * Created by hua on 2017/6/10.
 */

public class MainNewsPresenter extends BasePresenter<MainNewsContract.View>
        implements MainNewsContract.Presenter{

    @Override
    public void getMainNewsData() {
        //// TODO: 2017/6/10 获取头条数据

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isViewAttached()){
                    getView().onGetMainNewsData(null);
                }
            }
        }).start();

    }
}
