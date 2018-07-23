package hua.news.module_video;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.video.IVideoFragmentManager;
import hua.news.module_video.fragments.FunnyFragment;


/**
 * 视频类新闻Fragment管理类
 *
 * @author hua
 * @version 2017/11/16 9:53
 */
@Route(path = RouterConstant.MODULE_VIDEO_FRAGMENT_MANAGER_IMPL)
public class VideoNewsFragmentManager implements IVideoFragmentManager{


    @Override
    public Fragment getVideoHomeFragment() {
        return new PageVideo();
    }

    @Override
    public Fragment getFunnyFragment() {
        return new FunnyFragment();
    }

    @Override
    public void init(Context context) {

    }
}
