package com.example.hua.framework.widget;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * 简单的用来展示fragment的viewPager适配器
 *
 * @author hua
 * @date 2017/6/5
 */
public class SimpleFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titles;

    public SimpleFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        this(fm, fragmentList, null);
    }


    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles != null ? titles.get(position) : "";
    }

}
