package hua.news.module_news.ifengdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_news.R;

/**
 * @author hua
 * @version 2018/4/9 17:07
 */

public class IFengWebActivity extends BaseActivity {

    public static final String URL = "url";
    public static final int MAX_PROGRESS = 100;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.webview_advert)
    WebView webviewAdvert;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public static void start(Activity activity, String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(activity, "weburl不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        url = "http://dmpclick.deliver.ifeng.com/jump16?url=http%3A%2F%2Fdol.deliver.ifeng.com%2Fc%3Fz%3Difeng%26la%3D0%26si%3D2%26cg%3D1%26c%3D1%26ci%3D2%26or%3D17740%26l%3D66754%26bg%3D66754%26b%3D114101%26u%3Dhttp%3A%2F%2Fclickc.admaster.com.cn%2Fc%2Fa105796%2Cb2430778%2Cc3139%2Ci0%2Cm101%2C8a2%2C8b2%2Ch";
        Intent intent = new Intent(activity, IFengWebActivity.class);
        intent.putExtra(URL, url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_web);

        ButterKnife.bind(this);

        webviewAdvert.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webviewAdvert.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pbProgress.setProgress(newProgress);
                if (newProgress == MAX_PROGRESS) {
                    pbProgress.setVisibility(View.GONE);
                } else {
                    pbProgress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
            }
        });
        WebSettings settings = webviewAdvert.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        String url = getIntent().getStringExtra(URL);
        webviewAdvert.loadUrl(url);
    }

    @Override
    protected void initToolbar(@NonNull Toolbar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
       back();
    }

    private void back(){
//        if (webviewAdvert.canGoBack()) {
//            webviewAdvert.goBack();
//        } else {
//            finish();
//        }
        finish();
    }
}
