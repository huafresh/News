package hua.news.module_video.ifenghome;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.hua.framework.widget.CommonTabLayout;
import com.example.hua.framework.widget.SimpleFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_service.entitys.VideoChannelBean;
import hua.news.module_video.R;
import hua.news.module_video.ifengdetail.IFengDetailFragment;

/**
 * @author hua
 * @version 2018/4/27 14:44
 */

public class IFengVideoHomeFragment extends BaseFragment {

    @BindView(R.id.video_bar)
    RelativeLayout videoBar;
    @BindView(R.id.video_tab_layout)
    CommonTabLayout videoTabLayout;
    @BindView(R.id.video_view_pager)
    ViewPager videoViewPager;
    private VideoChannelLiveData videoChannelLiveData;

    @Override
    protected int getLayoutId() {
        return R.layout.page_video;
    }

    @Override
    protected void initDatas() {
        videoChannelLiveData = new VideoChannelLiveData();
    }

    @Override
    protected void initViews() {
        showLoading();
    }

    @Override
    protected void setListeners() {
        videoChannelLiveData.observe(this, new Observer<List<VideoChannelBean>>() {
            @Override
            public void onChanged(@Nullable List<VideoChannelBean> videoChannelBeans) {
                onGetVideoChannelList(videoChannelBeans);
            }
        });
    }

    private void onGetVideoChannelList(List<VideoChannelBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            List<String> titles = new ArrayList<>();
            List<Fragment> fragments = new ArrayList<>();
            for (VideoChannelBean.TypesBean bean : dataList.get(0).getTypes()) {
                titles.add(bean.getName());
                fragments.add(IFengDetailFragment.newInstance("clientvideo_" + bean.getId()));
            }
            SimpleFragmentStatePagerAdapter adapter = new SimpleFragmentStatePagerAdapter(getChildFragmentManager(),
                    fragments, titles);
            videoViewPager.setAdapter(adapter);
            videoTabLayout.setupWithViewPager(CommonTabLayout.STYLE_MODE_NETEASE, videoViewPager);
            showComplete();
        } else {
            showError();
        }
    }
}
