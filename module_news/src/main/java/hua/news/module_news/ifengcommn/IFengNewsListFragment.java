package hua.news.module_news.ifengcommn;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hua.framework.divider.LinearItemDecoration;
import com.example.hua.framework.support.pullrefresh.IRefreshLayout;
import com.example.hua.framework.support.pullrefresh.SupportRefreshLayout;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.ToastUtil;
import com.example.hua.framework.wrapper.imageload.ImageLoad;
import com.example.hua.framework.wrapper.loadlayout.LoadView;
import com.example.hua.framework.wrapper.popupwindow.CommonPopupWindow;
import com.example.hua.framework.wrapper.recyclerview.HeaderAndFootWrapper;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_common.constants.IFengConstant;
import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_news.R;
import hua.news.module_news.ifengdetail.IFengDetailActivity;
import hua.news.module_news.ifengdetail.IFengWebActivity;
import hua.news.module_news.ifengdetail.ImageBrowserActivity;

/**
 * 图文类新闻列表页面
 *
 * @author hua
 * @date 2017/6/4
 */
public class IFengNewsListFragment extends BaseFragment {

    private static final String KEY_CHANNEL_ID = "channel_id";
    private static final int MIN_NEWS_SUM = 15;
    Unbinder unbinder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pull_refresh)
    SupportRefreshLayout pullRefresh;

    private IFengCommonListAdapter mRecyclerAdapter;
    private String channel;
    private IFengNewsListViewModel newsListViewModel;
    private NewsDelPopContent popContent;
    private Banner banner;
    private List<IFengNewsEntity> bannerList;
    private View bannerHeaderView;
    private HeaderAndFootWrapper wrapperAdapter;

    public static IFengNewsListFragment newInstance(String channelId) {
        IFengNewsListFragment fragment = new IFengNewsListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_CHANNEL_ID, channelId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_normal_news_common;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        try {
            channel = getArguments().getString(KEY_CHANNEL_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewModelProvider.NewInstanceFactory factory =
                new IFengNewsListViewModel.Factory(mActivity.getApplication(), channel);
        newsListViewModel = ViewModelProviders.of(this, factory)
                .get(IFengNewsListViewModel.class);
        bannerList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        //初始化recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new LinearItemDecoration(mActivity));
        mRecyclerAdapter = new IFengCommonListAdapter(mActivity);
        wrapperAdapter = new HeaderAndFootWrapper(mRecyclerAdapter);
        recyclerView.setAdapter(wrapperAdapter);

        //初始化头部banner
        bannerHeaderView = getLayoutInflater().inflate(R.layout.news_detail_headerview, recyclerView, false);
        banner = bannerHeaderView.findViewById(R.id.banner);
        banner.setDelayTime(5000)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        ImageLoad.loadNormalImage(imageView, (String) path);
                    }
                })
                .setIndicatorGravity(Gravity.RIGHT)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        openDetailPage(bannerList.get(position));
                    }
                });

        showLoading();
        //请求页面数据
        newsListViewModel.refresh(IFengNewsListViewModel.ACTION_DEFAULT);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        //监听loadError页面点击
        mLoadService.addReloadListener(LoadErrorView.class.getCanonicalName(),
                new LoadView.ReLoadListener() {
                    @Override
                    public void onReLoad(View v) {
                        showLoading();
                        newsListViewModel.refresh(IFengNewsListViewModel.ACTION_DEFAULT);
                    }
                });

        //监听列表项点击
        wrapperAdapter.setOnItemClickListener(new MultiItemRvAdapter.OnItemClickListener<IFengNewsEntity>() {
            @Override
            public void onClick(View view, IFengNewsEntity data, int position) {
                openDetailPage(data);
            }
        });

        mRecyclerAdapter.setOnCancelClickListener(new IFengCommonListAdapter.OnCancelClickListener() {
            @Override
            public void onCancel(View view, IFengNewsEntity data) {
                showDelPop(view, data.getStyle().getBackreason());
            }
        });

        //刷新监听
        pullRefresh.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(IRefreshLayout refreshLayout) {
                newsListViewModel.refresh(IFengNewsListViewModel.ACTION_DOWN);
            }
        });
        pullRefresh.setOnLoadMoreListener(new IRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore(IRefreshLayout refreshLayout) {
                newsListViewModel.refresh(IFengNewsListViewModel.ACTION_UP);
            }
        });

        //监听常规新闻列表数据
        newsListViewModel.listLiveData.observe(this, new Observer<List<IFengNewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<IFengNewsEntity> iFengNewsEntities) {
                onGetNewsListData(iFengNewsEntities);
            }
        });

        //监听banner新闻列表
        newsListViewModel.bannerLiveData.observe(this, new Observer<List<IFengNewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<IFengNewsEntity> iFengNewsEntities) {
                onGetBannerListData(iFengNewsEntities);
            }
        });

        //监听toast
        newsListViewModel.toast.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ToastUtil.toast(mActivity, s);
            }
        });

    }

    private void onGetBannerListData(List<IFengNewsEntity> dataList) {
        if (dataList != null && dataList.size() > 0) {

            filterBadData(dataList);

            List<String> imgUrls = new ArrayList<>();
            List<String> titles = new ArrayList<>();

            for (IFengNewsEntity bean : dataList) {
                imgUrls.add(bean.getThumbnail());
                titles.add(bean.getTitle());
            }

            bannerList.clear();
            bannerList.addAll(dataList);

            banner.setImages(imgUrls);
            banner.setBannerTitles(titles);
            banner.start();

            wrapperAdapter.addHeadView(bannerHeaderView);
            wrapperAdapter.notifyDataSetChanged();
        } else {
            banner.stopAutoPlay();
            wrapperAdapter.removeHeadView(bannerHeaderView);
            wrapperAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 过滤脏数据，过滤规则一点点补充
     */
    private void filterBadData(List<IFengNewsEntity> dataList) {
        Iterator<IFengNewsEntity> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            IFengNewsEntity newsEntity = iterator.next();
            if (newsEntity.getLink() == null) {
                iterator.remove();
            }
        }

    }

    private void openDetailPage(IFengNewsEntity data) {
        switch (data.getType()) {
            case IFengConstant.DETAIL_TYPE_DOC:
                IFengDetailActivity.start(mActivity, data.getDocumentId());
                break;
            case IFengConstant.DETAIL_TYPE_ADVERT:
                IFengWebActivity.start(mActivity, data.getLink().getWeburl());
                break;
            case IFengConstant.DETAIL_TYPE_SLIDE:
                ImageBrowserActivity.start(mActivity, data.getDocumentId());
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private void showDelPop(View view, List<String> backReasons) {
        if (popContent == null) {
            popContent = new NewsDelPopContent();
            popContent.setOnConfirmListener(new NewsDelPopContent.OnConfirmListener() {
                @Override
                public void onConfirm(List<Integer> selectedPos) {
                    Toast.makeText(mActivity, "将不再推荐该类型新闻", Toast.LENGTH_SHORT).show();
                }
            });
        }
        popContent.setBackReasons(backReasons);

        int popupHeight = popContent.getPopupHeight(mActivity);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int belowRemainY = CommonUtil.getScreenHeight(mActivity) - locations[1];

        int offsetY;
        //用于调节箭头到叉的距离
        final int margin = -5;
        if (locations[1] > CommonUtil.getScreenHeight(mActivity) / 2) {
            offsetY = belowRemainY + margin;
            popContent.setUp(false);
        } else {
            offsetY = belowRemainY - popupHeight - view.getHeight() - margin;
            popContent.setUp(true);
        }

        CommonPopupWindow.get(mActivity)
                .setContent(popContent)
                .setWidthScale(0.91f)
                .setAnimation(R.style.del_popup)
                .dismissBlackDelay(500)
                .showAtLocation(Gravity.BOTTOM, 0, offsetY);

    }

    private void onGetNewsListData(@Nullable List<IFengNewsEntity> iFengNewsEntities) {
        if (iFengNewsEntities != null) {
            if (iFengNewsEntities.size() > MIN_NEWS_SUM) {
                showComplete();
                mRecyclerAdapter.setDataList(iFengNewsEntities);
                wrapperAdapter.notifyDataSetChanged();
                refreshComplete(true);
            } else {
                //数据量不够，继续加载更多新闻
                newsListViewModel.refresh(IFengNewsListViewModel.ACTION_UP);
            }
        } else {
            showError();
            refreshComplete(false);
        }
    }

    private void refreshComplete(boolean success) {
        pullRefresh.finishLoadMore(success);
        pullRefresh.finishLoadMore(success);
    }


}
