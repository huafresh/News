package hua.news.module_main.runalone;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.picturenews.IPictureFragmentManager;

/**
 * @author hua
 * @version 2018/4/2 16:31
 */
@Route(path = RouterConstant.MODULE_PICTURE_FRAGMENT_MANAGER_IMPL, priority = 1)
public class TestPictureFragmentManager implements IPictureFragmentManager {
    @Override
    public Fragment getPictureNewsFragment(String channel) {
        return TempFragment.getInstance(channel);
    }

    @Override
    public Fragment getMainNews() {
        return null;
    }

    @Override
    public Fragment getNewsHomeFragment() {
        return null;
    }

    @Override
    public void init(Context context) {

    }

}
