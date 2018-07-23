package com.example.hua.framework.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: hua
 * Created: 2017/10/11
 * Description:
 */

public class SimpleViewPagerAdapter<T> extends PagerAdapter {

    protected List<T> dataList;
    protected Context context;

    public SimpleViewPagerAdapter(Context context) {
        this.context = context;
    }

    public SimpleViewPagerAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        if (dataList == null) {
            return new ArrayList<>();
        }
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T object = dataList.get(position);
        View view = inflateView(object, container, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 将{@link #instantiateItem(ViewGroup, int)}方法返回的Object对象转为View
     *
     * @param o Object returned by instantiateItem
     * @return the view to be displayed at the specified position
     */
    protected View inflateView(T o, ViewGroup container, int position) {
        if (o instanceof View) {
            return (View) o;
        }
        return null;
    }

}
