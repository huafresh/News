package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.framework.R;


import java.lang.reflect.Field;
import java.util.List;

import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.MLog;

/**
 * TabLayoutWidget是对原生TabLayout的扩展。主要实现了以下需求：
 * 1、不搭配ViewPager使用TabLayout时，需要自行设置监听用来监听tab的选择，然后展示不同的页面，
 * 使用此类只需调用{@link #setupWithViews}即可。
 * <p>
 * 2、实现了网易新闻tab的效果，可以调用{@link #setupWithViewPager(int, ViewPager)}传入不同style来
 * 切换。并且tab的间隔以及下划线停靠的位置可以在xml里自定义。
 * <p>
 * 3、反射实现了几个修改原生TabLayout默认值的方法，比如：
 * {@link #setTabPadding},
 * {@link #setScrollableTabMinWidth}
 *
 * @author hua
 * @date 2017/6/4
 */
public class CommonTabLayout extends TabLayout {

    private List<Fragment> mFragments;
    private int mContainId;
    private List<Tab> mTabList;
    private FragmentManager mFragmentManager;
    private Context mContext;

    private float mStayPositionOffset;
    private int mTabViewInterval;
    private int mCurrentMode;
    private boolean isScrollFromViewPager = false;
    private int mTabTextSize;
    private int mTabPadding;
    private int mTabScrollableWidth;
    private ColorStateList mTabTextColorList;

    private static final float DEFAULT_STAY_POSITION_OFFSET = 5 * 1.0f / 6; //[-1,1]
    private static final int DEFAULT_TABVIEW_INTERVAL = 32; //px
    private static final float DEFAULT_TAB_TEXT_SIZE = 12; //sp
    private static final int DEFAULT_TAB_PADDING = 20; //px
    private static final int DEFAULT_TAB_SCROLLABLE_WIDTH = 40; //px


    /**
     * 此模式为默认模式，使用此模式，本类与{@link TabLayout}一致
     */
    public static final int STYLE_MODE_NORMAL = 0;

    /**
     * 设置此模式，{@link R.styleable#CommonTabLayout_StayPositionOffset}和
     * {@link R.styleable#CommonTabLayout_TabViewInterval}属性才有效。
     * 具体效果参考网易新闻的tab实现
     */
    public static final int STYLE_MODE_NETEASE = 1;


    public CommonTabLayout(Context context) {
        this(context, null);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getAttrs(attrs);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);
        mStayPositionOffset = array.getFloat(R.styleable.CommonTabLayout_StayPositionOffset,
                DEFAULT_STAY_POSITION_OFFSET);

        mTabViewInterval = array.getDimensionPixelSize(R.styleable.CommonTabLayout_TabViewInterval,
                DEFAULT_TABVIEW_INTERVAL);

        mTabTextSize = array.getDimensionPixelSize(R.styleable.CommonTabLayout_tabTextSize,
                CommonUtil.sp2px(mContext, DEFAULT_TAB_TEXT_SIZE));

        mTabPadding = array.getDimensionPixelSize(R.styleable.CommonTabLayout_tabPaddingWrapper,
                DEFAULT_TAB_PADDING);
        setTabPadding(mTabPadding);

        mTabScrollableWidth = array.getDimensionPixelSize(R.styleable.CommonTabLayout_tabMinScrollableWidth,
                DEFAULT_TAB_SCROLLABLE_WIDTH);
        setScrollableTabMinWidth(mTabScrollableWidth);

        array.recycle();
    }

    private void init(Context context) {
        mContext = context;
        addOnTabSelectedListener(new WrapperTabSelectedListener());
        mCurrentMode = STYLE_MODE_NORMAL;
    }

    private class WrapperTabSelectedListener implements OnTabSelectedListener {
        @Override
        public void onTabSelected(Tab tab) {
            if (mTabList != null) {
                addContentToContain(tab);
            }
        }

        @Override
        public void onTabUnselected(Tab tab) {
            if (mTabList != null) {
                removeContentFromContain(tab);
            }
        }

        @Override
        public void onTabReselected(Tab tab) {
            //no op
        }
    }

    private void removeContentFromContain(Tab tab) {
        int pos = mTabList.indexOf(tab);
        Fragment fragment = mFragments.get(pos);
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.detach(fragment);
            MLog.d(fragment.getTag() + ",,,detach");
            ft.commit();
        }
    }

    private void addContentToContain(Tab tab) {
        setTabSelected(tab, true);
        int pos = mTabList.indexOf(tab);
        if (mFragmentManager != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            String name = makeFragmentName(mContainId, pos);
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment == null) {
                fragment = mFragments.get(pos);
                ft.add(mContainId, fragment, makeFragmentName(mContainId, pos));
                MLog.d(fragment.getTag() + ",,,add");
            } else {
                ft.attach(fragment);
                MLog.d(fragment.getTag() + ",,,attach");
            }
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 为fragment生成名称
     *
     * @param viewId
     * @param pos
     * @return
     */
    private String makeFragmentName(int viewId, long pos) {
        return "news_main_switch:" + viewId + ":" + pos;
    }

    /**
     * 从fragment的名称获取该fragment在异常销毁前与tab对应的位置。
     *
     * @param tag
     * @return
     * @see {@link #makeFragmentName}
     */
    private int GetPosFromFragmentTag(String tag) {
        String[] strings = tag.split(":");
        String pos = strings[2];
        return pos == null ? -1 : Integer.valueOf(pos);
    }

    /**
     * 关联tab、视图列表、展示视图的容器。目前暂时只支持试图为fragment的情况
     *
     * @param fm           展示fragment时使用
     * @param tabs         tab列表
     * @param fragments    tab对应的视图列表
     * @param containId    展示视图的容器
     * @param saveInstance 异常恢复时保存的状态
     */
    public void setupWithViews(FragmentManager fm, List<Tab> tabs, List<Fragment> fragments,
                               @IdRes int containId, Bundle saveInstance) {
        mFragmentManager = fm;
        mTabList = tabs;
        mFragments = fragments;
        mContainId = containId;

        if (saveInstance != null) {
            restoreSaveInstance(fragments, fm);
        }

        setupTabs(tabs, fragments, getLastPos(saveInstance, fragments, fm));
    }

    private void restoreSaveInstance(List<Fragment> fragments, FragmentManager fm) {
        List<Fragment> saveList = fm.getFragments();
        if (saveList != null) {
            for (Fragment fragment : saveList) {
                int pos = GetPosFromFragmentTag(fragment.getTag());
                fragments.remove(pos);
                fragments.add(pos, fragment);
            }
        }
    }

    //获取异常发生时，容器内显示的fragment在fragment列表中的位置
    private int getLastPos(Bundle savedInstanceState, List<Fragment> fragments, FragmentManager fm) {
        int pos = 0;
        if (savedInstanceState != null) {
            Fragment fragment = fm.findFragmentById(mContainId);
            if (fragment != null) {
                pos = fragments.indexOf(fragment);
            }
        }
        if (pos == -1) { //极端情况下，保存的fragment没有一个是当前在mContainId中显示的
            pos = 0;
        }
        return pos;
    }

    private void setupTabs(List<Tab> tabs, List<Fragment> views, int pos) {
        removeAllTabs();

        if (tabs != null) {
            int count = Math.min(getSize(tabs), getSize(views));
            for (int i = 0; i < count; i++) {
                if (i == pos) {
                    addTabWrapper(tabs.get(i), true);
                } else {
                    addTabWrapper(tabs.get(i), false);
                }
            }
        }
    }


    private int getSize(List list) {
        return list != null ? list.size() : 0;
    }

    //首次启动时，tab会全部处于selected状态，处理一下
    private void addTabWrapper(Tab tab, boolean isSelect) {
        setTabSelected(tab, isSelect);
        addTab(tab, isSelect);
    }

    private void setTabSelected(Tab tab, boolean isSelected) {
        View customView = tab.getCustomView();
        if (customView != null) {
            ImageView imageView = (ImageView) customView.findViewById(android.R.id.icon);
            imageView.setSelected(isSelected);
            TextView textView = (TextView) customView.findViewById(android.R.id.text1);
            textView.setSelected(isSelected);
        }
    }

    /**
     * 设置Tab图片的bottomMargin值，调用此方法需要自定义tabView，并且布局中存在ImageView使用系统id
     *
     * @param margin Margin值
     */
    public void setTextIconMargin(int margin) {
        for (int i = 0; i < getTabCount(); i++) {
            Tab tab = getTabAt(i);
            if (tab != null) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    ImageView mCustomIconView = (ImageView) customView.findViewById(android.R.id.icon);
                    MarginLayoutParams lp = ((MarginLayoutParams) mCustomIconView.getLayoutParams());
                    if (lp.bottomMargin != margin) {
                        lp.bottomMargin = margin;
                        mCustomIconView.requestLayout();
                    }
                }
            }
        }
    }

    /**
     * 原生的TabLayout，文本会有一个默认的PaddingLeft和PaddingRight值，调用此方法可以修改该值
     *
     * @param padding
     */
    public void setTabPadding(int padding) {
        try {
            Field paddingStart = TabLayout.class.getDeclaredField("mTabPaddingStart");
            Field paddingEnd = TabLayout.class.getDeclaredField("mTabPaddingEnd");
            paddingStart.setAccessible(true);
            paddingEnd.setAccessible(true);
            paddingStart.set(this, padding);
            paddingEnd.set(this, padding);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置tab的最小宽度，设置此值tab不会有最大宽度，它会在文本变长时变大。
     * 这是与在xml中设置TabMinWidth的区别
     *
     * @param width
     */
    public void setScrollableTabMinWidth(int width) {
        try {
            Field field = TabLayout.class.getDeclaredField("mScrollableTabMinWidth");
            field.setAccessible(true);
            field.set(this, width);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置下划线在滑动时停留的位置，取值范围之[-1, 1]，为1是最左边，-1是最右边
     *
     * @param offset
     * @see {@link R.styleable#CommonTabLayout_StayPositionOffset}
     */
    public void setStayPositionOffset(int offset) {
        mStayPositionOffset = offset;
    }

    /**
     * 设置tabVie之间的间隔。
     *
     * @param interval
     * @see {@link R.styleable#CommonTabLayout_TabViewInterval}
     */
    public void setTabViewInterval(int interval) {
        mTabViewInterval = interval;
        refreshTabMargin();
    }

    /**
     * 给TabLayout设置ViewPager
     *
     * @param mode      tab的显示模式。可取值：
     *                  {@link #STYLE_MODE_NETEASE} 网易样式
     *                  {@link #STYLE_MODE_NORMAL} TabLayout默认样式
     * @param viewPager 要设置的ViewPager
     */
    public void setupWithViewPager(int mode, ViewPager viewPager) {
        if (mode == STYLE_MODE_NETEASE) {
            setupWithViewPager(viewPager);
            viewPager.clearOnPageChangeListeners();
            viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListenerWrapper(this));
            resolveTabFromPageAdapter(viewPager);
            mCurrentMode = STYLE_MODE_NETEASE;
        } else if (mode == STYLE_MODE_NORMAL) {
            setupWithViewPager(viewPager);
            mCurrentMode = STYLE_MODE_NORMAL;
        }
    }

    void resolveTabFromPageAdapter(ViewPager viewPager) {
        removeAllTabs();

        //根据adapter添加tab
        PagerAdapter mPagerAdapter = viewPager.getAdapter();
        if (mPagerAdapter != null) {
            final int count = mPagerAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Tab tab = newTab().setCustomView(R.layout.item_text_tab)
                        .setText(mPagerAdapter.getPageTitle(i));
                setTabTextSizeAndColor(tab);
                addTab(tab, i == viewPager.getCurrentItem());
            }
        }

        //修改tabView的margin值
        refreshTabMargin();
    }

    private void setTabTextSizeAndColor(Tab tab) {
        View customView = tab.getCustomView();
        if (customView != null) {
            TextView textView = (TextView) customView.findViewById(android.R.id.text1);
            textView.setTextSize(CommonUtil.px2sp(mContext, mTabTextSize));
            if (mTabTextColorList != null) {
                textView.setTextColor(mTabTextColorList);
            }
        }
    }

    /**
     * 设置tab文本的状态颜色
     *
     * @param colorList
     */
    public void setTabTextColorList(ColorStateList colorList) {
        mTabTextColorList = colorList;
    }

    private void refreshTabMargin() {
        for (int i = 0; i < getTabCount(); i++) {
            View tabView = getTabViewAt(i);
            if (tabView != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.leftMargin = mTabViewInterval / 2;
                lp.rightMargin = mTabViewInterval / 2;
                tabView.setLayoutParams(lp);
            }
        }
    }


    @Override
    public void scrollTo(int x, int y) {
        switch (mCurrentMode) {
            case STYLE_MODE_NORMAL:
                // in normal style mode, just scroll as TabLayout
                super.scrollTo(x, y);
                break;
            case STYLE_MODE_NETEASE:
                //in netease style mode, we only scroll when user touch the ViewPager
                if (isScrollFromViewPager) {
                    super.scrollTo(x, y);
                    isScrollFromViewPager = false;
                }
                break;
            default:
                //something was wrong, just do as default
                super.scrollTo(x, y);
                break;
        }

    }


    private class TabLayoutOnPageChangeListenerWrapper extends TabLayoutOnPageChangeListener {

        private TabLayoutOnPageChangeListenerWrapper(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (mCurrentMode == STYLE_MODE_NETEASE) {
                //in netease mode , we calculate the scrollX ourselves
                calculateScrollX(position, positionOffset);
            }
        }
    }

    private void calculateScrollX(int position, float positionOffset) {
        View selectedChild = getTabViewAt(position);
        View nextChild = getTabViewAt(position + 1);
        View firstChild = getTabViewAt(0);
        if (selectedChild != null && firstChild != null) {
            int selectedWidth = selectedChild.getWidth();
            int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
            /* *
             * in the old , we use selectedChilde's width to calculate onScroll value。
             * however, selected child is uncertainly,so the onScroll value will uncertainly too,
             * this will case problem . so, we use first child to make sure onScroll value unique.
             */
            int firstTabWidth = firstChild.getWidth();
            float offset = mStayPositionOffset * (getWidth() / 2 - firstTabWidth / 2 - mTabViewInterval / 2);
            int scrollBase = (int) (selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2) + offset);
            int scrollOffset = (int) (((selectedWidth + nextWidth) * 0.5f + mTabViewInterval) * positionOffset);
            int scrollX = (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                    ? scrollBase + scrollOffset
                    : scrollBase - scrollOffset;

            isScrollFromViewPager = true;
            scrollTo(scrollX, 0);
        }
    }

    private View getTabViewAt(int pos) {
        Tab tab = getTabAt(pos);
        if (tab != null) {
            View customView = tab.getCustomView();
            if (customView != null) {
                return (View) customView.getParent();
            }
        }
        return null;
    }

}
