package hua.news.module_news.mainnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_service.entitys.NormalNewsEntity;
import hua.news.module_news.R;
import hua.news.module_news.R2;

/**
 * Created by hua on 2017/6/4.
 */
public class PageMainNews extends BaseFragment implements MainNewsContract.View{

    @BindView(R2.id.main_news_bar)
    RelativeLayout mainNewsBar;

    private MainNewsContract.Presenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mActivity).inflate(R.layout.page_main,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
    }

    protected void initDatas() {
        mPresenter = new MainNewsPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    protected void initViews() {
        mPresenter.getMainNewsData();
    }

    @Override
    public void setPresenter(MainNewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetMainNewsData(List<NormalNewsEntity> dataList) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showComplete();
            }
        });
    }

}
