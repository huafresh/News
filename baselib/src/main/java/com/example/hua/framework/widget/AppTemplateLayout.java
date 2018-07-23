package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * app模板布局，ViewPager+Fragment+TabLayout的封装。
 * 使用此类作为ViewPager和TabLayout的父布局即可。
 *
 * @author hua
 * @version 2018/7/20 14:51
 */

public class AppTemplateLayout extends LinearLayout {

    public static final String PAGE_TITLE_DEFAULT_PREFIX = "tab";
    private int tabLayoutId = -1;
    private int[] textIds;
    private int[] normalIconIds;
    private int[] selectedIconIds;
    private List<Fragment> fragments = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private PagerAdapter defaultAdapter;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int textId = -1;
    private int iconId = -1;
    private @ColorInt
    int tabTextColor = -1;
    private @ColorInt
    int tabSelectedTextColor = -1;
    private boolean disableClearTabBacRes = false;
    private PagerAdapter pagerAdapter;
    private ColorStateList oldStateList;

    public AppTemplateLayout(Context context) {
        this(context, null);
    }

    public AppTemplateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppTemplateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewPager(this);
        findTabLayout(this);

        if (tabLayout != null) {
            clearTabBacResId();
            tabLayout.setSelectedTabIndicatorHeight(0);
            setTextPadding(tabLayout, 0);
            tabLayout.addOnTabSelectedListener(new TabSelectedListener());
        }
    }

    private void findViewPager(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewPager) {
                viewPager = (ViewPager) child;
            }
            if (child instanceof ViewGroup && viewPager == null) {
                findViewPager((ViewGroup) child);
            }
        }
    }

    private void findTabLayout(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof TabLayout) {
                tabLayout = (TabLayout) child;
            }
            if (child instanceof ViewGroup && tabLayout == null) {
                findTabLayout((ViewGroup) child);
            }
        }
    }

    public AppTemplateLayout setFragments(List<Fragment> fragments, FragmentManager fragmentManager) {
        this.fragments = fragments;
        this.views = null;
        this.defaultAdapter = new FragmentAdapterInternal(fragmentManager);
        return this;
    }

    public AppTemplateLayout setViews(List<View> views) {
        this.views = views;
        this.fragments = null;
        this.defaultAdapter = new AdapterInternal();
        return this;
    }

    public AppTemplateLayout setCustomTabView(@LayoutRes int layoutId) {
        this.tabLayoutId = layoutId;
        return this;
    }

    public AppTemplateLayout setCustomTabView(@LayoutRes int layoutId, @IdRes int textId, @IdRes int iconId) {
        this.tabLayoutId = layoutId;
        this.textId = textId;
        this.iconId = iconId;
        return this;
    }

    public AppTemplateLayout setTextIds(int[] textIds) {
        this.textIds = textIds;
        return this;
    }

    public AppTemplateLayout setTextColorIds(@ColorRes int normalColor, @ColorRes int selectedColor) {
        this.tabTextColor = context.getResources().getColor(normalColor);
        this.tabSelectedTextColor = context.getResources().getColor(selectedColor);
        return this;
    }

    public AppTemplateLayout setTextColors(@ColorInt int normalColor, @ColorInt int selectedColor) {
        this.tabTextColor = normalColor;
        this.tabSelectedTextColor = selectedColor;
        return this;
    }

    public AppTemplateLayout setIconIds(int[] normalIconIds, int[] selectedIconIds) {
        this.normalIconIds = normalIconIds;
        this.selectedIconIds = selectedIconIds;
        return this;
    }


    public void apply() {
        if (viewPager != null) {
            pagerAdapter = viewPager.getAdapter();
            if (pagerAdapter == null) {
                viewPager.setAdapter(this.defaultAdapter);
                pagerAdapter = defaultAdapter;
            }
        }

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        refreshTabs();
    }

    private void refreshTabs() {
        if (tabLayout != null) {
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    if (tabLayoutId != -1) {
                        oldStateList = tabLayout.getTabTextColors();
                        tab.setCustomView(tabLayoutId);
                    }
                    if (textIds != null) {
                        tab.setText(textIds[i]);
                    }
                    if (normalIconIds != null && selectedIconIds != null) {
                        tab.setIcon(createStateDrawableList(normalIconIds[i], selectedIconIds[i]));
                    }
                }
            }

            setIconBottomMargin(0);

            if (tabTextColor != -1 && tabSelectedTextColor != -1) {
                tabLayout.setTabTextColors(tabTextColor, tabSelectedTextColor);
            } else if (oldStateList != null) {
                tabLayout.setTabTextColors(oldStateList);
            }
        }
    }


    private StateListDrawable createStateDrawableList(int normaIconId, int selectedIconId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normalDrawable = context.getResources().getDrawable(normaIconId);
        //这里用 ENABLED_STATE_SET 无效
        stateListDrawable.addState(new int[]{-android.R.attr.state_selected}, normalDrawable);
        Drawable selectDrawable = context.getResources().getDrawable(selectedIconId);
        stateListDrawable.addState(SELECTED_STATE_SET, selectDrawable);
        return stateListDrawable;
    }

    private void clearTabBacResId() {
        if (!disableClearTabBacRes) {
            try {
                Field field = tabLayout.getClass().getDeclaredField("mTabBackgroundResId");
                field.setAccessible(true);
                field.set(tabLayout, 0);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchTab(Object item) {
        switchTab(item, false);
    }

    public void switchTab(Object item, boolean smoothScroll) {
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) == item) {
                    viewPager.setCurrentItem(i, smoothScroll);
                }
            }
        } else if (views != null) {
            for (int i = 0; i < views.size(); i++) {
                if (views.get(i) == item) {
                    viewPager.setCurrentItem(i, smoothScroll);
                }
            }
        }
    }

    public void disableClearTabBacRes() {
        this.disableClearTabBacRes = true;
    }

    /**
     * 需要自定义tab布局才有效
     *
     * @param margin margin
     */
    public void setIconBottomMargin(int margin) {
        if (tabLayout != null) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                ImageView imageView = getIconView(i);
                if (imageView != null) {
                    MarginLayoutParams lp = ((MarginLayoutParams) imageView.getLayoutParams());
                    if (lp.bottomMargin != margin) {
                        lp.bottomMargin = margin;
                        imageView.requestLayout();
                    }
                }
            }
        }
    }

    public @Nullable
    <T extends View> T getIconView(int index) {
        if (tabLayout != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    if (iconId != -1) {
                        return customView.findViewById(iconId);
                    } else {
                        return customView.findViewById(android.R.id.icon);
                    }
                }
            }
        }
        return null;
    }

    public @Nullable
    <T extends View> T getTextView(int index) {
        if (tabLayout != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    if (textId != -1) {
                        customView.findViewById(textId);
                    } else {
                        customView.findViewById(android.R.id.text1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 原生的TabLayout，文本会有一个默认的PaddingLeft和PaddingRight值，此方法可以反射修改该值
     *
     * @param padding padding值
     */
    public static void setTextPadding(TabLayout tabLayout, int padding) {
        try {
            Field paddingStart = TabLayout.class.getDeclaredField("mTabPaddingStart");
            Field paddingEnd = TabLayout.class.getDeclaredField("mTabPaddingEnd");
            paddingStart.setAccessible(true);
            paddingEnd.setAccessible(true);
            paddingStart.set(tabLayout, padding);
            paddingEnd.set(tabLayout, padding);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public View getTabCustomView(int index, int viewId) {
        if (tabLayout != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    return customView.findViewById(viewId);
                }
            }
        }
        return null;
    }

    private class AdapterInternal extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getTextString(position);
        }
    }

    private String getTextString(int position) {
        if (textIds != null) {
            return context.getResources().getString(textIds[position]);
        } else {
            return PAGE_TITLE_DEFAULT_PREFIX + position;
        }
    }

    private class FragmentAdapterInternal extends FragmentPagerAdapter {

        FragmentAdapterInternal(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getTextString(position);
        }
    }

    private class TabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, tab);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(tab.getPosition(), tab);
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int index, TabLayout.Tab tab);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public PagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public static class StaticViewPager extends ViewPager {
        private boolean isCanScroll = false;
        private boolean isSmoothScroll = false;

        public boolean isSmoothScroll() {
            return isSmoothScroll;
        }

        public void setSmoothScroll(boolean smoothScroll) {
            isSmoothScroll = smoothScroll;
        }

        public boolean isCanScroll() {
            return this.isCanScroll;
        }

        public void setCanScroll(boolean canScroll) {
            this.isCanScroll = canScroll;
        }

        public StaticViewPager(Context context) {
            super(context);
        }

        public StaticViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return isCanScroll && super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean canScrollHorizontally(int direction) {
            return isCanScroll && super.canScrollHorizontally(direction);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return isCanScroll && super.onTouchEvent(ev);
        }

        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
            super.setCurrentItem(item, isSmoothScroll && smoothScroll);
        }

        @Override
        public void setCurrentItem(int item) {
            this.setCurrentItem(item, isSmoothScroll);
        }
    }

}
