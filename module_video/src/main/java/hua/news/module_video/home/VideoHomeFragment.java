package hua.news.module_video.home;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.widget.CommonTabLayout;
import com.example.hua.framework.widget.SimpleFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_service.video.IVideoFragmentManager;
import hua.news.module_video.R;
import hua.news.module_video.R2;


/**
 * 视频Tab。
 *
 * @author hua
 * @date 2017/6/4
 */
public class VideoHomeFragment extends BaseFragment {

    @BindView(R2.id.video_bar)
    RelativeLayout videoBar;
    @BindView(R2.id.video_tab_layout)
    CommonTabLayout videoTabLayout;
    @BindView(R2.id.video_view_pager)
    ViewPager videoViewPager;
    Unbinder unbinder;

    @Autowired
    IVideoFragmentManager videoFragmentManager;


    @Override
    protected int getLayoutId() {
        return R.layout.page_video;
    }

    @Override
    protected void initViews() {
        ARouter.getInstance().inject(this);

        List<Fragment> fragments = new ArrayList<>();
        //fragments.add(new FragmentVideoBeauty());
        // fragments.add(new FragmentFilm());
        if (videoFragmentManager != null) {
            fragments.add(videoFragmentManager.getFunnyFragment());
        }
        //fragments.add(new FragmentRecom());
        List<String> titles = new ArrayList<>();
//        titles.add(getResources().getString(R.string.page_video_beauty));
//        titles.add(getResources().getString(R.string.page_video_film));
        titles.add(getResources().getString(R.string.page_video_funny));
//        titles.add(getResources().getString(R.string.page_video_recom));
        SimpleFragmentStatePagerAdapter adapter = new SimpleFragmentStatePagerAdapter(getChildFragmentManager(),
                fragments, titles);
        videoViewPager.setAdapter(adapter);
        ColorStateList colorStateList = mActivity.getResources().getColorStateList(
                R.color.selector_color_main_tab);
        videoTabLayout.setTabTextColorList(colorStateList);
        videoTabLayout.setupWithViewPager(CommonTabLayout.STYLE_MODE_NETEASE, videoViewPager);
    }

}
