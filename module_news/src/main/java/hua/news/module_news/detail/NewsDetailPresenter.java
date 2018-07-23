package hua.news.module_news.detail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.mvpbase.CallBack;

import hua.news.module_common.FrameworkInitializer;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_news.data.NewsDataRepository;


/**
 * 图文类新闻presenter
 * @author thinkive
 * @date 2017/9/29
 */

public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View>
        implements NewsDetailContract.Presenter {

    @Autowired
    ILoginManager loginManager;

    public NewsDetailPresenter() {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void addCollect(String news_id) {
        if (loginManager != null) {
            if (loginManager.isLogin()) {
                String localCollect = loginManager.getUserInfo().getCollect();
                if (!TextUtils.isEmpty(localCollect) && localCollect.contains(news_id)) {
                    return;
                } else {
                    localCollect += news_id + ";";
                }
                doAdd(news_id, localCollect);
            } else {
                getView().setJumpType(NewsDetailActivity.JUMP_TYPE_COLLECT);
                loginManager.openAccountLogin(FrameworkInitializer.getInstance().getContext());
            }
        }
    }

    private void doAdd(String news_id, final String localCollect) {
        NewsDataRepository.getInstance().addCollect(loginManager.getUserInfo().getUser_id(),
                news_id, new CallBack<Object>() {
                    @Override
                    public void onSuccess(Context context, Object data) {
                        if (isViewAttached()) {
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                            getView().onCollectResult(NewsDetailActivity.COLLECT_TYPE_ADD, true);
                        }
                        loginManager.getUserInfo().setCollect(localCollect);
                    }

                    @Override
                    public void onError(Context context, Bundle error) {
                        if (isViewAttached()) {
                            Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
                            getView().onCollectResult(NewsDetailActivity.COLLECT_TYPE_ADD, false);
                        }
                    }
                });
    }

    @Override
    public void cancelCollect(String news_id) {
        if (!(loginManager != null && loginManager.isLogin())) {
            return;
        }
        String localCollect = loginManager.getUserInfo().getCollect();
        if (TextUtils.isEmpty(localCollect) && !localCollect.contains(news_id)) {
            return;
        } else {
            localCollect = localCollect.replace(news_id, "");
        }
        doCancel(news_id, localCollect);
    }

    private void doCancel(String news_id, final String localCollect) {
        NewsDataRepository.getInstance().cancelCollect(loginManager.getUserInfo().getUser_id(),
                news_id, new CallBack<Object>() {
                    @Override
                    public void onSuccess(Context context, Object data) {
                        if (isViewAttached()) {
                            Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                            getView().onCollectResult(NewsDetailActivity.COLLECT_TYPE_CANCEL, true);
                        }
                        loginManager.getUserInfo().setCollect(localCollect);
                    }

                    @Override
                    public void onError(Context context, Bundle error) {
                        if (isViewAttached()) {
                            Toast.makeText(context, "取消收藏失败", Toast.LENGTH_SHORT).show();
                            getView().onCollectResult(NewsDetailActivity.COLLECT_TYPE_CANCEL, false);
                        }
                    }
                });
    }
}
