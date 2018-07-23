package hua.news.module_main.home;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import hua.news.module_common.FrameworkInitializer;

import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.network.HttpRequest;
import com.example.hua.framework.storage.StorageManager;
import com.example.hua.framework.network.MySimpleDisposable;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.json.JsonParseUtil;

import java.util.ArrayList;
import java.util.List;

import com.example.hua.framework.utils.MLog;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_main.R;

import hua.news.module_common.base.BaseApplication;
import hua.news.module_service.entitys.ColumnEntity;
import hua.news.module_service.picturenews.IPictureFragmentManager;
import hua.news.module_service.login.ILoginManager;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static hua.news.module_main.home.PageHome.COLUMN_TYPE_QUERY;
import static hua.news.module_common.constants.NewsConstant.NEWS_TYPE_LIVE;
import static hua.news.module_common.constants.NewsConstant.NEWS_TYPE_PICUTRE;
import static hua.news.module_common.constants.NewsConstant.NEWS_TYPE_VIDEO;
import static hua.news.module_common.constants.UrlCosntant.URL_COLUMN_LIST;

/**
 * 首页页面的Presenter层
 * <p>
 * Created by hua on 2017/6/7.
 */
class PageHomePresenter extends BasePresenter<PageHomeContract.View>
        implements PageHomeContract.Presenter {

    @Autowired(name = RouterConstant.MODULE_LOGIN_MANAGER)
    ILoginManager loginManager;

    @Autowired(name = RouterConstant.MODULE_PICTURE_FRAGMENT_MANAGER_IMPL)
    IPictureFragmentManager pictureFragmentManager;

    /**
     * 缓存栏目信息到内存和磁盘中使用的key，
     * 当内存有缓存时不会访问服务器。
     */
    public static final String KEY_COLUMN_LIST = "key_column_list";

    public PageHomePresenter() {
        ARouter.getInstance().inject(this);
    }

    /**
     * 业务处理流程，只要有一步成功则停止下一步：
     * 1、先看内存有没有栏目信息
     * 2、再看数据库有没有
     * 3、然后访问网络获取
     * 4、最后使用默认栏目数据
     * 这是本人第一次使用rx处理业务逻辑，特此记录。
     * 这种流式数据处理太美妙了，流水线式处理，后续要大量运用。
     */
    @Override
    public void getColumnData() {
        final Context context = BaseApplication.getContext();
        String column = (String) StorageManager.getInstance(context).getFromMemory(KEY_COLUMN_LIST);
        Flowable<String> flowable = Flowable.just(column != null ? column : "");
        flowable.subscribeOn(Schedulers.newThread()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                String s2 = null;
                if (TextUtils.isEmpty(s)) {

                    s2 = StorageManager.getInstance(context).getFromDisk(KEY_COLUMN_LIST);
                }
                return s2;
            }
        }).flatMap(new Function<String, Flowable<List<ColumnEntity>>>() {
            @Override
            public Flowable<List<ColumnEntity>> apply(String s) throws Exception {
                if (TextUtils.isEmpty(s)) {
                    return getColumnFromNet();
                } else {
                    List<ColumnEntity> list = JsonParseUtil.getInstance().parseJsonToList(s, ColumnEntity.class);
                    return Flowable.just(list);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<List<ColumnEntity>>() {
            @Override
            protected void onSuccess(List<ColumnEntity> columnEntities) {
                if (columnEntities != null) {
                    //更新缓存
                    updateCacheColumn(JsonParseUtil.getInstance().parseObjectToString(columnEntities));
                    handleColumnList(columnEntities);
                }
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                if (isViewAttached()) {

                    MLog.e("获取栏目信息失败！！！");

                    //这个请求不允许失败，否则整个页面就是空白了。
                    List<ColumnEntity> dataList = generateTempData();
                    handleColumnList(dataList);
                    //更新缓存
                    updateCacheColumn(JsonParseUtil.getInstance().parseObjectToString(dataList));
                }
            }
        });
    }

    private Flowable<List<ColumnEntity>> getColumnFromNet() {
        IHomeApi homeApi = HttpRequest.getInstance().newApiService().create(IHomeApi.class);
        String userId = null;
        if (loginManager != null && loginManager.isLogin()) {
            userId = loginManager.getUserInfo().getUser_id();
        }
        return homeApi.getHomeColumnList(URL_COLUMN_LIST, userId,
                COLUMN_TYPE_QUERY, null);
    }

    private void updateCacheColumn(String column) {
        Context context = BaseApplication.getContext();
        StorageManager.getInstance(context).saveToMemory(KEY_COLUMN_LIST, column);
        StorageManager.getInstance(context).saveToDisk(KEY_COLUMN_LIST, column);
    }

    private void handleColumnList(List<ColumnEntity> dataList) {
        if (dataList == null) {
            return;
        }

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (ColumnEntity entity : dataList) {
            Fragment fragment = getFragment(entity.getType(), entity.getChannel());
            if (fragment != null) {
                fragments.add(fragment);
            }
            titles.add(entity.getName());
        }

        if (isViewAttached()) {
            getView().initHomeTab(fragments, titles);
        }
    }

    private Fragment getFragment(int type, String channel) {
        Fragment fragment = null;
        switch (type) {
            case NEWS_TYPE_PICUTRE:
                if (pictureFragmentManager != null) {
                    fragment = pictureFragmentManager.getPictureNewsFragment(channel);
                }
                break;
            case NEWS_TYPE_VIDEO:
                break;
            case NEWS_TYPE_LIVE:
                break;
            default:
                break;
        }

        if (fragment == null) {
            throw new RuntimeException("获取fragment失败，你确定测试代码写了?");
        }

        return fragment;
    }

    private List<ColumnEntity> generateTempData() {
        List<ColumnEntity> columnList = new ArrayList<>();
        addColumnBean(columnList, 1, getName(R.string.page_home_headlines));
        addColumnBean(columnList, 2, getName(R.string.page_home_video));
        addColumnBean(columnList, 3, getName(R.string.page_home_fun));
        addColumnBean(columnList, 4, getName(R.string.page_home_sport));
        addColumnBean(columnList, 5, getName(R.string.page_home_city));
        addColumnBean(columnList, 6, getName(R.string.page_home_netease_number));
        addColumnBean(columnList, 7, getName(R.string.page_home_money));
        addColumnBean(columnList, 8, getName(R.string.page_home_technology));
        addColumnBean(columnList, 9, getName(R.string.page_home_car));
        addColumnBean(columnList, 10, getName(R.string.page_home_society));
        addColumnBean(columnList, 11, getName(R.string.page_home_nba));
        addColumnBean(columnList, 12, getName(R.string.page_home_fun_moment));
        addColumnBean(columnList, 13, getName(R.string.page_home_joke));
        addColumnBean(columnList, 14, getName(R.string.page_home_history));
        addColumnBean(columnList, 15, getName(R.string.page_home_game));
        addColumnBean(columnList, 16, getName(R.string.page_home_beauty));
        return columnList;
    }

    private void addColumnBean(List<ColumnEntity> list, int num, String name) {
        ColumnEntity bean = new ColumnEntity();

        bean.setAdd(true);
        bean.setChannel("fadfadsf");
        bean.setColumn_id(String.valueOf(num));
        bean.setType(NEWS_TYPE_PICUTRE);
        bean.setName(name);

        list.add(bean);
    }

    private String getName(@StringRes int id) {
        return FrameworkInitializer.getInstance().getContext().getResources().getString(id);
    }

}
