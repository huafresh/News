package hua.news.module_main.me;

import android.os.Handler;
import android.os.Looper;

import com.example.hua.framework.mvpbase.BasePresenter;

/**
 * 我页面presenter层
 *
 * @author hua
 * @date 2017/6/10
 */

public class PageMePresenter extends BasePresenter<PageMeContract.View>
        implements PageMeContract.Presenter {

    private Handler handler = new Handler(Looper.getMainLooper());

}
