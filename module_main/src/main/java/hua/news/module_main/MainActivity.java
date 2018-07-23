package hua.news.module_main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import hua.news.module_common.BroadcastManager;

import com.example.hua.framework.interfaces.IOnBackPress;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.widget.CommonTabLayout;
import com.example.hua.framework.widget.DisableHorScrollViewPager;
import com.example.hua.framework.widget.SimpleFragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.live.ILiveFragmentManager;
import hua.news.module_service.picturenews.IPictureFragmentManager;
import hua.news.module_service.video.IVideoFragmentManager;


import hua.news.module_main.home.PageHome;
import hua.news.module_main.me.PageMeFragment;

/**
 * app主页面。
 * 整个主页分为5个tab，分别是：首页、要闻、直播、视频、我
 * 5个tab使用ViewPager展示，每一个tab都对应一个Fragment。
 *
 * @author hua
 * @version 2017/10/23
 */
@Route(path = RouterConstant.MAIN)
public class MainActivity extends AppCompatActivity implements IOnBackPress {

    /**
     * 隐藏主页的底部tab
     */
    public static final String ACTION_HIDE_MAIN_TAB = "action_hide_main_tab";
    /**
     * 显示主页的底部tab
     */
    public static final String ACTION_SHOW_MAIN_TAB = "action_show_main_tab";

    @BindView(R2.id.main_tab_layout)
    public CommonTabLayout mainTabLayout;
    @BindView(R2.id.main_view_pager)
    DisableHorScrollViewPager mainViewPager;
    private List<OnBackPressListener> mBackListeners;
    private Animation mShowAnim;
    private Animation mDismissAnim;

    @Autowired(name = RouterConstant.MODULE_LIVE_FRAGMENT_MANAGER_IMPL)
    ILiveFragmentManager liveFragmentManager;
    @Autowired(name = RouterConstant.MODULE_PICTURE_FRAGMENT_MANAGER_IMPL)
    IPictureFragmentManager pictureFragmentManager;
    @Autowired(name = RouterConstant.MODULE_VIDEO_FRAGMENT_MANAGER_IMPL)
    IVideoFragmentManager videoFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonUtil.setStatusBarColor(this, CommonUtil.getColor(this,
                R.color.color_theme, getTheme()));

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        initViews();
        BroadcastManager.registerReceiver(mReceiver, actions);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.unRegisterReceiver(mReceiver);
    }

    private void initViews() {
        List<Fragment> fragments = new ArrayList<>();
        initFragments(fragments);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(
                getSupportFragmentManager(), fragments);
        mainViewPager.setAdapter(adapter);

        mainTabLayout.setupWithViewPager(mainViewPager);
        mainTabLayout.removeAllTabs();
        mainTabLayout.addTab(getTab(R.drawable.selector_home, R.string.page_home));
        mainTabLayout.addTab(getTab(R.drawable.selector_main, R.string.page_main));
        mainTabLayout.addTab(getTab(R.drawable.selector_live, R.string.page_live));
        mainTabLayout.addTab(getTab(R.drawable.selector_video, R.string.page_video));
        mainTabLayout.addTab(getTab(R.drawable.selector_me, R.string.page_me));
        mainTabLayout.setTextIconMargin(0);

        mShowAnim = AnimationUtils.loadAnimation(this, R.anim.main_tab_show);
        mDismissAnim = AnimationUtils.loadAnimation(this, R.anim.main_tab_dismiss);
    }


    private void initFragments(List<Fragment> fragments) {
        fragments.add(new PageHome());
        if (pictureFragmentManager != null) {
            fragments.add(pictureFragmentManager.getMainNews());
        } else {
            fragments.add(getTempFragment(getString(R.string.page_main)));
        }
        if (liveFragmentManager != null) {
            fragments.add(liveFragmentManager.getLiveFragment());
        } else {
            fragments.add(getTempFragment(getString(R.string.page_main)));
        }
        if (videoFragmentManager != null) {
            fragments.add(videoFragmentManager.getVideoHomeFragment());
        } else {
            fragments.add(getTempFragment(getString(R.string.page_main)));
        }
        fragments.add(new PageMeFragment());
    }

    private Fragment getTempFragment(String name) {
        if (pictureFragmentManager != null) {
            return pictureFragmentManager.getPictureNewsFragment(name);
        }
        throw new RuntimeException("没写测试的fragment吧?");
    }

    private TabLayout.Tab getTab(int imageId, int textId) {
        return mainTabLayout.newTab()
                .setCustomView(R.layout.item_main_tab)
                .setIcon(imageId)
                .setText(textId);
    }

    private String[] actions = new String[]{ACTION_HIDE_MAIN_TAB, ACTION_SHOW_MAIN_TAB};
    private Receiver mReceiver = new Receiver();

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_HIDE_MAIN_TAB:
                    hideBottomTab();
                    break;
                case ACTION_SHOW_MAIN_TAB:
                    showBottomTab();
                    break;
                default:
                    break;
            }
        }
    }

    private void hideBottomTab() {
        mainTabLayout.setVisibility(View.GONE);
        mainTabLayout.startAnimation(mDismissAnim);
    }

    private void showBottomTab() {
        mainTabLayout.setVisibility(View.VISIBLE);
        mainTabLayout.startAnimation(mShowAnim);
    }


    @Override
    public void onBackPressed() {
        if (mBackListeners != null) {
            for (OnBackPressListener listener : mBackListeners) {
                if (listener.onBackPress()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void addOnBackPressListener(OnBackPressListener listener) {
        if (mBackListeners == null) {
            mBackListeners = new ArrayList<>();
        }
        if (listener != null) {
            mBackListeners.add(listener);
        }
    }

    @Override
    public void removeOnBackPressListener(OnBackPressListener listener) {
        if (mBackListeners != null) {
            mBackListeners.remove(listener);
        }
    }
}
