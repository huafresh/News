package hua.news.module_news.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.framework.LoadingDialog;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.widget.HeartView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_common.webview.BaseWebFragment;
import hua.news.module_common.webview.MyWebView;
import hua.news.module_news.R;
import hua.news.module_news.R2;

/**
 * WebView加载新闻详情页面
 *
 * @author hua
 * @date 2017/9/18
 */

public class NormalNewsDetailFragment extends BaseWebFragment {

    @BindView(R2.id.ll_webview_contain)
    LinearLayout llWebviewContain;
    @BindView(R2.id.love_view_bac)
    View loveViewBac;
    @BindView(R2.id.iv_love)
    HeartView ivLove;
    @BindView(R2.id.tv_love)
    TextView tvLove;
    @BindView(R2.id.view_divider_left)
    View viewDividerLeft;
    @BindView(R2.id.tv_share_to)
    TextView tvShareTo;
    @BindView(R2.id.view_divider_right)
    View viewDividerRight;
    @BindView(R2.id.iv_wechat_share)
    ImageView ivWechatShare;
    @BindView(R2.id.iv_wechat_comment_share)
    ImageView ivWechatCommentShare;
    @BindView(R2.id.iv_sina_share)
    ImageView ivSinaShare;
    Unbinder unbinder;
    @BindView(R2.id.cl_bottom_article)
    ConstraintLayout clBottomArticle;
    private LoadingDialog mLoadingDialog;

    @Override
    protected View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                @Nullable Bundle savedInstanceState, @NonNull MyWebView webView) {
        View view = inflater.inflate(R.layout.fragment_normal_news_detail, container, false);
        LinearLayout webViewContain = (LinearLayout) view.findViewById(R.id.ll_webview_contain);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = CommonUtil.dip2px(mActivity, 16);
        lp.leftMargin = lp.rightMargin = lp.topMargin = margin;
        webViewContain.addView(webView, lp);

        mLoadingDialog = new LoadingDialog(mActivity);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onLoadPage(MyWebView webView) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String data = arguments.getString("data");
            mLoadingDialog.show("正在加载...");
            clBottomArticle.setVisibility(View.INVISIBLE);
            setBottomBarVisibility(false);
            webView.addLoadFinishListener(new MyWebView.LoadFinishedListener() {
                @Override
                public void onLoadFinish(MyWebView webView) {
                    mLoadingDialog.dismiss();
                    clBottomArticle.setVisibility(View.VISIBLE);
                    setBottomBarVisibility(true);
                }
            });
            webView.loadData(data);
        }
    }

    private void setBottomBarVisibility(boolean isVisible){
        if (mActivity instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) mActivity;
//            activity.setBottomBarVisibility(isVisible);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);
    private int getId(View view){
        return rCaster.cast(view.getId());
    }

    @OnClick({R2.id.love_view_bac, R2.id.iv_wechat_share, R2.id.iv_wechat_comment_share, R2.id.iv_sina_share})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.love_view_bac:
                break;
            case R2.id.iv_wechat_share:
                break;
            case R2.id.iv_wechat_comment_share:
                break;
            case R2.id.iv_sina_share:
                break;
            default:
                break;
        }
    }
}
