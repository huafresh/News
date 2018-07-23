package hua.news.module_service.video;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author hua
 * @version 2018/3/26 9:19
 */

public interface IVideoFragmentManager extends IProvider {
    Fragment getVideoHomeFragment();

    Fragment getFunnyFragment();
}
