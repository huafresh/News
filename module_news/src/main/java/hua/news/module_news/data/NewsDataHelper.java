package hua.news.module_news.data;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hua.framework.LoadingDialog;
import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.network.NetworkConstant;

import hua.news.module_service.entitys.NormalNewsDetailBean;
import hua.news.module_service.entitys.NormalNewsEntity;
import hua.news.module_news.detail.NewsDetailActivity;

/**
 * 获取新闻数据的帮助
 *
 * @author hua
 * @date 2017/6/10
 */

public class NewsDataHelper {

    private Activity mActivity;
    private LoadingDialog mLoadingDialog;

    public static NewsDataHelper getInstance() {
        return HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final NewsDataHelper sInstance = new NewsDataHelper();
    }

    /**
     * 打开图文新闻详情页面
     *
     * @param activity
     * @param bean
     */
    public void showNormalNewsDetail(final Activity activity, final NormalNewsEntity bean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(activity);
        }
        mLoadingDialog.show("请稍后...");

        NewsDataRepository.getInstance().getNormalNewsDetail(bean.getNews_id(), new CallBack<NormalNewsDetailBean>() {
            @Override
            public void onSuccess(Context context, NormalNewsDetailBean data) {
                if (!activity.isFinishing()) {
                    mLoadingDialog.dismiss();
                    data.setReply_count(bean.getReply_count());
                    NewsDetailActivity.openDetailPage(activity, data);
                }
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (!activity.isFinishing()) {
                    mLoadingDialog.dismiss();
                    Toast.makeText(context, error.getString(NetworkConstant.ERROR_INFO), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
