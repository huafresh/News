package hua.news.module_news.ifengcommn;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import hua.news.module_common.constants.IFengConstant;
import hua.news.module_common.net.IFengApi;
import hua.news.module_news.ifengdata.IFengNewsApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 新闻列表页面ViewModel
 *
 * @author hua
 * @version 2018/4/24 16:33
 */

public class IFengNewsListViewModel extends AndroidViewModel {

    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    private String channel;
    private int pullNum = 0;
    private IFengNewsApiService iFengApiService;

    /**
     * banner新闻列表
     */
    public MutableLiveData<List<IFengNewsEntity>> bannerLiveData = new MutableLiveData<>();

    /**
     * 普通新闻列表
     */
    public MutableLiveData<List<IFengNewsEntity>> listLiveData = new MutableLiveData<>();

    public MutableLiveData<String> toast = new MutableLiveData<>();

    public IFengNewsListViewModel(@NonNull Application application, String channel) {
        super(application);
        this.channel = channel;
        iFengApiService = IFengApi.getInstance().createIFengService(IFengNewsApiService.class);
    }

    /**
     * 刷新页面
     *
     * @param action 刷新方向，下拉或者上拉
     */
    public void refresh(@Actions final String action) {
        if (TextUtils.isEmpty(channel)) {
            return;
        }

        iFengApiService.getNewsList(channel, action, pullNum)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<IFengNewsListEntity>>() {
                    @Override
                    public void onNext(List<IFengNewsListEntity> iFengNewsListEntities) {
                        if (iFengNewsListEntities == null || iFengNewsListEntities.size() == 0) {
                            listLiveData.setValue(null);
                            toast.setValue("暂无数据");
                        } else {
                            //这里iFengNewsListEntities每一个item就是对应某种类型的新闻，比如说banner，list和top
                            //并且第一个item一定是list类型
                            List<IFengNewsEntity> oldResult = listLiveData.getValue();
                            List<IFengNewsEntity> newResult = iFengNewsListEntities.get(0).getItem();
                            listLiveData.setValue(composeList(action, oldResult, newResult));
                            pullNum++;

                            //处理banner类型
                            boolean hasBanner = false;
                            for (IFengNewsListEntity bean : iFengNewsListEntities) {
                                if (IFengConstant.POSITION_TYPE_BANNER.equals(bean.getType())) {
                                    bannerLiveData.setValue(bean.getItem());
                                    hasBanner = true;
                                }
                            }
                            if (!hasBanner) {
                                bannerLiveData.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        //此时不知道是刷新，还是首次加载，触发一下回调就行
                        listLiveData.setValue(listLiveData.getValue());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private List<IFengNewsEntity> composeList(String action,
                                              List<IFengNewsEntity> old,
                                              List<IFengNewsEntity> result) {
        if (ACTION_DEFAULT.equals(action) || ACTION_DOWN.equals(action)) {
            return result;
        } else {
            if (old != null) {
                old.addAll(result);
            } else {
                old = result;
            }
            return old;
        }
    }

    @StringDef({ACTION_DEFAULT, ACTION_DOWN, ACTION_UP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Actions {

    }

    @Override
    protected void onCleared() {
        pullNum = 0;
    }

    public static class Factory extends ViewModelProviders.DefaultFactory {
        private String channel;
        private Application application;

        public Factory(@NonNull Application application, String channel) {
            super(application);
            this.channel = channel;
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (IFengNewsListViewModel.class.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getConstructor(Application.class, String.class).newInstance(application, channel);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                }
            }
            return super.create(modelClass);
        }
    }

}
