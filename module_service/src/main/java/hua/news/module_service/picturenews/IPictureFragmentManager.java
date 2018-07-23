package hua.news.module_service.picturenews;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author hua
 * @version 2018/3/24 17:32
 */

public interface IPictureFragmentManager extends IProvider {
    Fragment getPictureNewsFragment(String channel);

    /**
     * @deprecated 删掉要闻fragment。
     */
    @Deprecated
    Fragment getMainNews();

    Fragment getNewsHomeFragment();
}
