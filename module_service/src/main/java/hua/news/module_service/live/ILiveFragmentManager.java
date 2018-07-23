package hua.news.module_service.live;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author hua
 * @version 2018/3/26 9:25
 */

public interface ILiveFragmentManager extends IProvider {
    Fragment getLiveFragment();
}
