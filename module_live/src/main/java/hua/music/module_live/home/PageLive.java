package hua.music.module_live.home;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.example.hua.framework.widget.CommonTabLayout;
import com.example.hua.framework.widget.SimpleFragmentStatePagerAdapter;

import hua.news.module_common.base.BaseFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import hua.news.module_live.R;
import hua.news.module_live.R2;


/**
 * @author hua
 * @date 2017/6/4
 */
public class PageLive extends BaseFragment {

    @BindView(R2.id.live_tab_layout)
    CommonTabLayout liveTabLayout;
    @BindView(R2.id.live_bar)
    RelativeLayout liveBar;
    @BindView(R2.id.live_view_pager)
    ViewPager liveViewPager;
    Unbinder unbinder;

    private SimpleFragmentStatePagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mActivity).inflate(R.layout.page_live, container, false);
    }

    protected void initViews() {
        liveTabLayout.addTab(getTab(R.string.bar_live_hot));
        liveTabLayout.addTab(getTab(R.string.bar_live_type));
        liveTabLayout.addTab(getTab(R.string.bar_live_subscribe));

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.bar_live_hot));
        titles.add(getString(R.string.bar_live_type));
        titles.add(getString(R.string.bar_live_subscribe));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TempFragment.getInstance("热门"));
        fragments.add(TempFragment.getInstance("类型"));
        fragments.add(TempFragment.getInstance("订阅"));

        adapter = new SimpleFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles);
        liveViewPager.setAdapter(adapter);
        ColorStateList colorStateList = mActivity.getResources().getColorStateList(
                R.color.selector_color_live_tab);
        liveTabLayout.setTabTextColorList(colorStateList);
        liveTabLayout.setupWithViewPager(CommonTabLayout.STYLE_MODE_NETEASE, liveViewPager);
    }

    private TabLayout.Tab getTab(int textId) {
        return liveTabLayout.newTab()
                .setText(textId);
    }

}
