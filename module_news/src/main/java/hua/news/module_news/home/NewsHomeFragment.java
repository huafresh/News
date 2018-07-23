package hua.news.module_news.home;

import android.arch.lifecycle.Observer;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.widget.CommonTabLayout;
import com.example.hua.framework.widget.SimpleFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_news.R;
import hua.news.module_news.ifengcommn.IFengNewsListFragment;

/**
 * 图文新闻首页
 *
 * @author hua
 * @version 2018/5/4 11:23
 */

public class NewsHomeFragment extends BaseFragment {

    @BindView(R.id.home_tab_layout)
    CommonTabLayout homeTabLayout;
    @BindView(R.id.custom_column_bac)
    View customColumnBac;
    @BindView(R.id.home_view_pager)
    ViewPager homeViewPager;
    Unbinder unbinder;
    private NewsChannelLiveData channelLiveData;
    private SimpleFragmentStatePagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_home;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        channelLiveData = new NewsChannelLiveData(mActivity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ColorStateList colorStateList = CommonUtil.getColorStateList(mActivity,
                R.color.selector_color_main_tab, null);
        homeTabLayout.setTabTextColorList(colorStateList);
        adapter = new SimpleFragmentStatePagerAdapter(getChildFragmentManager());
        homeViewPager.setAdapter(adapter);
        homeTabLayout.setupWithViewPager(CommonTabLayout.STYLE_MODE_NETEASE, homeViewPager);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        channelLiveData.observe(this, new Observer<List<IFengColumnEntity>>() {
            @Override
            public void onChanged(@Nullable List<IFengColumnEntity> iFengColumnEntities) {
                List<Fragment> fragments = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (IFengColumnEntity entity : iFengColumnEntities) {
                    fragments.add(IFengNewsListFragment.newInstance(entity.getChannel_id()));
                    titles.add(entity.getName());
                }
                adapter.setFragmentList(fragments);
                adapter.setTitles(titles);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
