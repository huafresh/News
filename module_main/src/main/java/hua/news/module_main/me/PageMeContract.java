package hua.news.module_main.me;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

/**
 * Created by hua on 2017/6/10.
 * 我页面契约类
 */

public class PageMeContract {

    interface View extends IBaseView<PageMeContract.Presenter> {

    }

    interface Presenter extends IBasePresenter<PageMeContract.View> {

    }

}
