package hua.news.module_news.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.divider.LinearItemDecoration;
import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;
import com.example.hua.framework.wrapper.loadlayout.LoadView;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_common.NewsPullToRefreshLayout;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_service.entitys.NormalNewsEntity;
import hua.news.module_news.R;
import hua.news.module_news.R2;
import hua.news.module_news.data.NewsDataHelper;

/**
 * 图文类新闻列表页面
 *
 * @author hua
 * @date 2017/6/4
 */
public class CommonListFragment extends BaseFragment implements CommonListContract.View {

    private static final String KEY_CHANNEL = "channel";
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.pull_refresh)
    NewsPullToRefreshLayout pullRefresh;
    Unbinder unbinder;
    private MultiItemRvAdapter mRecyclerAdapter;
    private CommonListContract.Presenter mPresenter;
    private int channel;
    private View mView;

    public static CommonListFragment newInstance(String channel) {
        CommonListFragment fragment = new CommonListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_CHANNEL, channel);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = mLayoutInflater.inflate(R.layout.fragment_normal_news_common, container, false);
            mLoadService = LoadLayoutManager.getInstance().register(mView);
            mPresenter = new CommonListPresenter();
            mPresenter.attachView(this);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mLoadService.getContainerLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        initViews();
        setListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    protected void initDatas() {
        try {
            channel = getArguments().getInt(KEY_CHANNEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initViews() {
        //初始化刷新布局
        pullRefresh.setPullDownRefreshEnable(true);
        pullRefresh.setOnRefreshingListener(new NewsPullToRefreshLayout.OnRefreshingListener() {
            @Override
            public void onRefreshing() {
                loadPageData();
            }
        });

        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new LinearItemDecoration(mActivity));
        mRecyclerAdapter = new CommonListAdapter(mActivity);
        recyclerView.setAdapter(mRecyclerAdapter);

        //请求页面数据
        showLoading();
        loadPageData();
    }

    @Override
    protected void setListeners() {
        mLoadService.addReloadListener(LoadErrorView.class.getCanonicalName(),
                new LoadView.ReLoadListener() {
                    @Override
                    public void onReLoad(View v) {
                        showLoading();
                        loadPageData();
                    }
                });

        mRecyclerAdapter.setOnItemClickListener(new MultiItemRvAdapter.OnItemClickListener<NormalNewsEntity>() {
            @Override
            public void onClick(View view, NormalNewsEntity data, int position) {
                NewsDataHelper.getInstance().showNormalNewsDetail(mActivity, data);
            }
        });
    }

    private void loadPageData() {
        // TODO: 2017/10/2 此处应根据频道获取对应的新闻列表信息
        mPresenter.getHeadlinesLatestDataList(15);
    }

    @Override
    public void setPresenter(CommonListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetHeadlinesNewsDataListSuccess(List<NormalNewsEntity> dataList) {
        mLoadService.showLoadComplete();
        mRecyclerAdapter.setDataList(dataList);
        mRecyclerAdapter.notifyDataSetChanged();
        pullRefresh.setHeaderRefreshComplete(0);
    }

    @Override
    public void onGetHeadlinesNewsDataListFailed() {
        showError();
        pullRefresh.setHeaderRefreshComplete(0);
    }
}
