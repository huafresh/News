package hua.news.module_news.ifengdetail;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.widget.CustomScrollView;
import com.example.hua.framework.wrapper.imageload.ImageLoad;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_news.R;
import hua.news.module_news.utils.Util;

/**
 * @author hua
 * @version 2018/4/8 19:34
 */

public class IFengDetailActivity extends BaseActivity {

    public static final String KEY_AID = "aid";
    private static final int CRITICAL_VALUE = 40;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.bt_like)
    Button btLike;
    @BindView(R.id.ConstraintLayout)
    RelativeLayout ConstraintLayout;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.scrollview)
    CustomScrollView scrollview;
    private IFengDetailLiveData detailLiveData;
    private String aid;
    private View toolbarLayout;
    private ToolBarHolder toolBarHolder;

    public static void start(Activity activity, String aid) {
        Intent intent = new Intent(activity, IFengDetailActivity.class);
        intent.putExtra(KEY_AID, aid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_ifeng_detail);
        ButterKnife.bind(this);
        initWebview();
        initDatas();
        setListeners();
    }

    private void initWebview() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webview.loadUrl("file:///android_asset/detail.html");
    }

    private void initDatas() {
        aid = getIntent().getStringExtra(KEY_AID);
        detailLiveData = new IFengDetailLiveData(aid);
    }

    private void setListeners() {
        showLoading();
        detailLiveData.getData();
        detailLiveData.observe(this, new Observer<IFengNewsDetail>() {
            @Override
            public void onChanged(@Nullable IFengNewsDetail iFengNewsDetail) {
                if (iFengNewsDetail == null) {
                    showError();
                } else {
                    showComplete();
                    onGetData(iFengNewsDetail);
                }
            }
        });

        scrollview.setOnScrollChangedListener(new CustomScrollView.OnScrollChangedListener() {
            @Override
            public void onChanged(int l, int t, int oldl, int oldt) {
//                if (ConstraintLayout.getHeight() > t) {
//                    toolBarHolder.toolbarRight.setVisibility(View.GONE);
//                } else {
//                    toolBarHolder.toolbarRight.setVisibility(View.VISIBLE);
//                }
                toolBarHolder.toolbarRight.setVisibility(View.VISIBLE);

                float alpha = (ConstraintLayout.getHeight() - t) * 1.0f / CRITICAL_VALUE;
                alpha = Math.min(alpha, 1);
                toolBarHolder.toolbarRight.setAlpha(1 - alpha);
            }
        });

    }

    private void onGetData(@Nullable IFengNewsDetail bean) {
        tvTitle.setText(bean.getBody().getTitle());
        tvUpdateTime.setText(Util.formatTime(bean.getBody().getUpdateTime()));
        if (bean.getBody().getSubscribe() != null) {
            ImageLoad.loadRoundImage(ivLogo, bean.getBody().getSubscribe().getLogo());
            tvName.setText(bean.getBody().getSubscribe().getCateSource());
            ImageLoad.loadRoundImage(toolBarHolder.ivLogoTop, bean.getBody().getSubscribe().getLogo());
            toolBarHolder.tvNameTop.setText(bean.getBody().getSubscribe().getCateSource());
            toolBarHolder.tvUpdateTimeTop.setText(bean.getBody().getSubscribe().getCatename());
        } else {
            tvName.setText(bean.getBody().getSource());
            toolBarHolder.tvNameTop.setText(bean.getBody().getSource());
            toolBarHolder.tvUpdateTimeTop.setText(!TextUtils.isEmpty(bean.getBody().getAuthor())
                    ? bean.getBody().getAuthor() : bean.getBody().getEditorcode());
        }

        String url = "javascript:show_content(\'" + bean.getBody().getText() + "\')";
        webview.loadUrl(url);
    }


    @Override
    protected void initToolbar(@NonNull Toolbar toolbar) {
        toolbarLayout = getLayoutInflater().inflate(R.layout.ifeng_detail_toolbar, toolbar, false);
        toolBarHolder = new ToolBarHolder(toolbarLayout);
        toolbar.addView(toolbarLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.setBackgroundColor(CommonUtil.getColor(this, android.R.color.white, null));
    }


    @OnClick({R.id.iv_logo, R.id.tv_name, R.id.bt_like, R.id.ll_back, R.id.bt_like_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                break;
            case R.id.tv_name:
                break;
            case R.id.bt_like:
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_like_top:
                break;
            default:
                break;
        }
    }

    static class ToolBarHolder {
        @BindView(R.id.ll_back)
        LinearLayout llBack;
        @BindView(R.id.iv_logo_top)
        ImageView ivLogoTop;
        @BindView(R.id.tv_name_top)
        TextView tvNameTop;
        @BindView(R.id.tv_updateTime_top)
        TextView tvUpdateTimeTop;
        @BindView(R.id.bt_like_top)
        Button btLikeTop;
        @BindView(R.id.toolbar_right)
        android.support.constraint.ConstraintLayout toolbarRight;

        ToolBarHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
