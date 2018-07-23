package hua.news.module_user.collect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hua.framework.wrapper.loadlayout.LoadView;
import com.example.hua.framework.divider.LinearItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.NewsPullToRefreshLayout;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_service.entitys.CollectBean;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_user.R;
import hua.news.module_user.R2;


/**
 * Author: hua
 * Created: 2017/9/29
 * Description:
 * 收藏详情页面
 */

public class CollectDetailActivity extends BaseActivity {

    @BindView(R2.id.collect_detail_recycler)
    RecyclerView collectDetailRecycler;
    @BindView(R2.id.iv_collect_no_collect)
    ImageView ivCollectNoCollect;
    @BindView(R2.id.pull_refresh)
    NewsPullToRefreshLayout pullRefresh;
    private CollectDetailAdapter mCollectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_collect_detail);
        initDatas();
        ButterKnife.bind(this);
    }

    private void initDatas() {
        showLoading();
        mLoadService.addReloadListener(LoadErrorView.class.getCanonicalName(), new LoadView.ReLoadListener() {
            @Override
            public void onReLoad(View v) {
                getCollectListData();
            }
        });
        pullRefresh.setOnRefreshingListener(new NewsPullToRefreshLayout.OnRefreshingListener() {
            @Override
            public void onRefreshing() {
                getCollectListData();
            }
        });
        getCollectListData();
    }

    private ILoginManager loginManager;

    private void getCollectListData() {
//        NewsDataRepository.getInstance().queryCollectList(loginManager.getUserInfo().getUser_id(),
//                new CallBack<List<CollectBean>>() {
//                    @Override
//                    public void onSuccess(Context context, List<CollectBean> dataList) {
//                        if (!isFinishing()) {
//                            showComplete();
//                            showCollectRecycler(dataList);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Context context, Bundle error) {
//                        if (isFinishing()) {
//                            Toast.makeText(context, error.getString(NetworkConstant.ERROR_INFO),
//                                    Toast.LENGTH_SHORT).show();
//                            showError();
//                        }
//                    }
//                });
    }

    private void showCollectRecycler(List<CollectBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            ivCollectNoCollect.setVisibility(View.INVISIBLE);
            collectDetailRecycler.setVisibility(View.VISIBLE);

            collectDetailRecycler.setLayoutManager(new LinearLayoutManager(this));
            collectDetailRecycler.addItemDecoration(new LinearItemDecoration(this));
            mCollectAdapter = new CollectDetailAdapter(this);
            mCollectAdapter.setDataList(dataList);
            collectDetailRecycler.setAdapter(mCollectAdapter);
        } else {
            ivCollectNoCollect.setVisibility(View.VISIBLE);
            collectDetailRecycler.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle(getString(R.string.collect_detail_title));

        TextView extraView = getExtraView();
        extraView.setVisibility(View.VISIBLE);
        extraView.setText(getString(R.string.collect_detail_edit));

    }

    @OnClick(R2.id.iv_collect_no_collect)
    public void onViewClicked() {

    }
}
