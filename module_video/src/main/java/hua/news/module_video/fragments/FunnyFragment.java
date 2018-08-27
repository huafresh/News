package hua.news.module_video.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hua.framework.divider.LinearItemDecoration;
import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;
import com.example.hua.framework.wrapper.loadlayout.LoadView;


import hua.news.module_common.NewsPullToRefreshLayout;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_common.loadviews.LoadErrorView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_video.R;
import hua.news.module_video.R2;
import hua.news.module_video.data.CommonObserver;
import hua.news.module_video.data.VideoViewModel;
import hua.news.player.VideoView;

import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

/**
 * 搞笑视频
 *
 * @author hua
 * @date 2017/6/4
 */
public class FunnyFragment extends BaseFragment implements CommonObserver.OnChangedListener {

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.video_funny)
    NewsPullToRefreshLayout videoFunny;
    Unbinder unbinder;
    private VideoViewModel mViewModel;
    private VideoListAdapter mRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_video_funny, container, false);
        unbinder = ButterKnife.bind(this, mView);
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        mLoadService = LoadLayoutManager.getInstance().wrapper(mView);
        return mLoadService.getContainerLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        setListeners();
        addViewModelObserver();

        refreshPage();
    }

    @Override
    protected void initViews() {
        SmoothLinearLayoutManager manager = new SmoothLinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new LinearItemDecoration(mActivity));
        mRecyclerAdapter = new VideoListAdapter(mActivity);
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void setListeners() {
        mLoadService.addReloadListener(LoadErrorView.class.getCanonicalName(),
                new LoadView.ReLoadListener() {
                    @Override
                    public void onReLoad(View v) {
                        refreshPage();
                    }
                });
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                stopVideoPlay(view);
            }
        });
        mRecyclerAdapter.setOnItemClickListener(new MultiItemRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mRecyclerAdapter.playNext((MyViewHolder) recyclerView.getChildViewHolder(view));
            }
        });
    }

    private void stopVideoPlay(View view) {
        MyViewHolder holer = (MyViewHolder) recyclerView.getChildViewHolder(view);
        mRecyclerAdapter.resetHolder(holer);
        if (mRecyclerAdapter.mPlayHolder == holer) {
            mRecyclerAdapter.mPlayHolder = null;
        }
    }

    private static final int LIVEDATA_TYPE_TOAST = 0;
    private static final int LIVEDATA_TYPE_VIDEO_LIST = 1;
    private static final int LIVEDATA_TYPE_SUCCESS = 2;
    private static final int LIVEDATA_TYPE_VIDEO_PATH = 3;

    private void addViewModelObserver() {
        mViewModel.mToastInfo.observe(this,
                new CommonObserver(LIVEDATA_TYPE_TOAST, this));
        mViewModel.mVideoList.observe(this,
                new CommonObserver(LIVEDATA_TYPE_VIDEO_LIST, this));
        mViewModel.mIsSuccess.observe(this,
                new CommonObserver(LIVEDATA_TYPE_SUCCESS, this));
        mViewModel.mVideoPath.observe(this,
                new CommonObserver(LIVEDATA_TYPE_VIDEO_PATH, this));
    }

    @Override
    public void onChanged(int id, Object o) {
        switch (id) {
            case LIVEDATA_TYPE_TOAST:
                Toast.makeText(mActivity, (String) o, Toast.LENGTH_SHORT).show();
                break;
            case LIVEDATA_TYPE_VIDEO_LIST:
                mRecyclerAdapter.setDataList((List<?>) o);
                mRecyclerAdapter.notifyDataSetChanged();
                break;
            case LIVEDATA_TYPE_SUCCESS:
                if ((Boolean) o) {
                    showComplete();
                } else {
                    showError();
                }
                break;
            case LIVEDATA_TYPE_VIDEO_PATH:

                break;
            default:
                break;
        }
    }

    private void refreshPage() {
        showLoading();
        //触发请求数据ProcessLifecycleOwnerInitializer
        mViewModel.refreshWithPullDown();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecyclerAdapter.mPlayHolder != null) {
            VideoView videoView = mRecyclerAdapter.mPlayHolder.getView(R.id.vv_video);
            if (videoView != null && videoView.isPlaying()) {
                videoView.stop();
            }
        }
        unbinder.unbind();
    }


}
