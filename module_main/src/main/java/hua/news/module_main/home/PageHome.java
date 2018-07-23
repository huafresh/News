package hua.news.module_main.home;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import hua.news.module_common.BroadcastManager;

import com.example.hua.framework.storage.StorageManager;
import com.example.hua.framework.json.JsonParseUtil;
import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.widget.CommonTabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_service.entitys.ColumnEntity;

import hua.news.module_main.MainActivity;
import hua.news.module_main.R;
import hua.news.module_main.R2;

import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.widget.SimpleFragmentStatePagerAdapter;



/**
 * 首页。
 * 首页分为上下两大块，上面是标题栏，下面是TabLayout + ViewPager。
 * TabLayout的每一tab都对应一个栏目实体{@link ColumnEntity}，栏目数据从服务器获取。
 * 每个栏目实体包含一个请求该页面数据的频道id（channel字段），因此页面展示的内容都是动态的。
 *
 * @author hua
 * @date 2017/6/4
 */
public class PageHome extends BaseFragment implements PageHomeContract.View {

    /**
     * 栏目接口请求类型
     */
    public static final String COLUMN_TYPE_QUERY = "query";
    public static final String COLUMN_TYPE_ADD = "add";
    public static final String COLUMN_TYPE_REMOVE = "remove";

    @BindView(R2.id.bar_bac)
    View barBac;
    @BindView(R2.id.guide_v)
    Guideline guideV;
    @BindView(R2.id.home_netease_text)
    ImageView homeNeteaseText;
    @BindView(R2.id.home_search)
    ImageView homeSearch;
    @BindView(R2.id.home_live)
    ImageView homeLive;
    @BindView(R2.id.home_tab_layout)
    CommonTabLayout homeTabLayout;
    @BindView(R2.id.home_view_pager)
    ViewPager homeViewPager;
    Unbinder unbinder;
    @BindView(R2.id.custom_column_bac)
    View customColumnBac;
    @BindView(R2.id.custom_column_container)
    FrameLayout customColumnContainer;
    private PageHomeContract.Presenter mPresenter;
    private SimpleFragmentStatePagerAdapter adapter;
    private CustomColumnContent mColumnContent;
    private ViewGroupContainer mContainer;
    private Animation mAnimShow;
    private Animation mAnimDismiss;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = LayoutInflater.from(mActivity).inflate(R.layout.page_home, container, false);
            mPresenter = new PageHomePresenter();
        }
        unbinder = ButterKnife.bind(this, mView);
        mPresenter.attachView(this);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getColumnData();
    }

    @Override
    public void initHomeTab(List<Fragment> fragments, List<String> titles) {
        adapter = new SimpleFragmentStatePagerAdapter(getChildFragmentManager(), fragments, titles);
        homeViewPager.setAdapter(adapter);
        ColorStateList colorStateList = CommonUtil.getColorStateList(mActivity,
                R.color.selector_color_main_tab, null);
        homeTabLayout.setTabTextColorList(colorStateList);
        homeTabLayout.setupWithViewPager(CommonTabLayout.STYLE_MODE_NETEASE, homeViewPager);
    }

    @Override
    public void setPresenter(PageHomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetColumnListFailed() {
        showError();
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);
    private int getId(View view){
        return rCaster.cast(view.getId());
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.home_live, R2.id.custom_column_bac})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.home_live:
                //testDetailActivity();
                //mPresenter.getColumnData();
                testPullRefresh();
                break;
            case R2.id.custom_column_bac:
                showCustomColumnPage();
                break;
            default:
                break;
        }
    }

    private void testPullRefresh() {
        Intent intent = new Intent(mActivity, TestPullToRefreshActivity.class);
        startActivity(intent);

    }


    private void showCustomColumnPage() {
        if (mColumnContent == null) {
            String column = StorageManager.getInstance(mActivity).getFromDisk(
                    PageHomePresenter.KEY_COLUMN_LIST);
            List<ColumnEntity> list = JsonParseUtil.getInstance().parseJsonToList(column, ColumnEntity.class);
            if (list == null) {
                return;
            }

            mAnimShow = AnimationUtils.loadAnimation(mActivity, R.anim.up_to_down);
            mAnimDismiss = AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up);

            mColumnContent = new CustomColumnContent(list);
            mColumnContent.setOnDismissListener(new CustomColumnContent.OnDismissListener() {
                @Override
                public void onDismiss() {
                    animDismissColumnPage();
                }
            });
            mContainer = new ViewGroupContainer(customColumnContainer);
        }
        if (!mColumnContent.isShowing()) {
            animShowColumnPage();
        }
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    View tab = ((MainActivity) mActivity).mainTabLayout;
//                    if (tab.getVisibility() != View.GONE) {
//                        mMainHandler.sendEmptyMessage(1);
//                        return;
//                    }
//                    int height = customColumnContainer.getHeight();
                    int height = 1736;
                    ValueAnimator animator = ValueAnimator.ofInt(0, height);
                    animator.setDuration(400);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("@@@hua", "onAnimationUpdate" + animation.getAnimatedValue() + "");
                                    int curHeight = (int) animation.getAnimatedValue();
                                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) customColumnContainer.getLayoutParams();
                                    lp.bottomToBottom = -1;
                                    lp.height = curHeight;
                                    customColumnContainer.setLayoutParams(lp);
                                }
                            });
                        }
                    });
                    animator.start();
                    break;
                case 2:

                    int height2 = 1736;
                    ValueAnimator animator2 = ValueAnimator.ofInt(height2, 2);
                    animator2.setDuration(400);
                    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("@@@hua", "onAnimationUpdate" + animation.getAnimatedValue() + "");
                                    int curHeight = (int) animation.getAnimatedValue();
                                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) customColumnContainer.getLayoutParams();
                                    lp.bottomToBottom = -1;
                                    lp.height = curHeight;
                                    customColumnContainer.setLayoutParams(lp);
                                }
                            });
                        }
                    });
                    animator2.start();
                    break;
                default:
                    break;
            }
        }
    };

    private void animShowColumnPage() {
        BroadcastManager.sendBroadcastSync(MainActivity.ACTION_HIDE_MAIN_TAB);
        mContainer.addContentView(mColumnContent);
        customColumnContainer.setVisibility(View.VISIBLE);
        customColumnContainer.startAnimation(mAnimShow);
    }

    private void animDismissColumnPage() {
        BroadcastManager.sendBroadcast(MainActivity.ACTION_SHOW_MAIN_TAB);
        customColumnContainer.setVisibility(View.INVISIBLE);
        customColumnContainer.startAnimation(mAnimDismiss);
    }

}
