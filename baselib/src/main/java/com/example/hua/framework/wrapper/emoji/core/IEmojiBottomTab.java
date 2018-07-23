package com.example.hua.framework.wrapper.emoji.core;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Author: hua
 * Created: 2017/10/10
 * Description:
 * 此接口定义了一个底部可滑动的tab页
 */

public interface IEmojiBottomTab {

    /** 绑定ViewPager对象 */
    void bindViewPager(ViewPager viewPager);

    /** 提供内容视图 */
    View createContentView();

}
