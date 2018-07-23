package hua.music.module_live;

import android.content.Context;
import android.support.v4.app.Fragment;


import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.music.module_live.home.PageLive;
import hua.news.module_service.live.ILiveFragmentManager;

/**
 * 直播类新闻页面管理类
 *
 * @author thinkive
 * @date 2017/9/30
 */
@Route(path = RouterConstant.MODULE_LIVE_FRAGMENT_MANAGER_IMPL)
public class LiveFragmentManager implements ILiveFragmentManager {

    @Override
    public Fragment getLiveFragment() {
        return new PageLive();
    }

    @Override
    public void init(Context context) {

    }
}
