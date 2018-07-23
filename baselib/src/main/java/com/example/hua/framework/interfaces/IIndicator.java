package com.example.hua.framework.interfaces;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 页面指示器接口
 *
 * @author hua
 * @date 2017/10/7
 */

public interface IIndicator {

    /**
     * 绑定ViewPager对象
     *
     * @param viewPager ViewPager对象
     */
    void bindViewPager(ViewPager viewPager);

    /**
     * 获取指示器实体视图
     *
     * @param context Context
     * @param count   页面的数量
     * @return 实体视图
     */
    View getContentView(Context context, int count);

    /**
     * 设置指示器选中的位置
     *
     * @param position 指示器的位置
     */
    void setPosition(int position);


}
