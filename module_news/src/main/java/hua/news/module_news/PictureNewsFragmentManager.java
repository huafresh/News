package hua.news.module_news;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_news.home.NewsHomeFragment;
import hua.news.module_service.picturenews.IPictureFragmentManager;
import hua.news.module_news.common.CommonListFragment;
import hua.news.module_news.mainnews.PageMainNews;


/**
 * 图文类新闻页面管理
 * <p>
 * 关于这块的设计思路是这样的：
 * 每个Tab页请求到栏目数据后，根据每个栏目的新闻类型选择展示的是
 * 图文类新闻、视频类新闻还是直播类新闻。然后分别使用各自的页面管理类
 * 获取要展示的Fragment。
 * <p>
 * 类似的还有：
 * 视频类新闻页面管理
 * 直播类新闻页面管理
 *
 * @author hua
 * @date 2017/10/1
 */
@Route(path = RouterConstant.MODULE_PICTURE_FRAGMENT_MANAGER_IMPL)
public class PictureNewsFragmentManager implements IPictureFragmentManager {
    public static final int CHANNEL_HEADLINES = 0;
    public static final int CHANNEL_FUN = 1;
    public static final int CHANNEL_SPORT = 1;

    @Override
    public Fragment getPictureNewsFragment(String channel) {
        // TODO: 2017/10/2 后续要根据频道id 返回对应的fragment实例
        return CommonListFragment.newInstance(channel);
    }

    @Override
    public Fragment getMainNews() {
        return new PageMainNews();
    }

    @Override
    public Fragment getNewsHomeFragment() {
        return new NewsHomeFragment();
    }

    @Override
    public void init(Context context) {

    }
}
