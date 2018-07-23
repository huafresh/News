package hua.news.module_common.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Author: hua
 * Created: 2017/9/21
 * Description:
 * 拦截WebView将要执行的各种操作。
 */

public class BaseWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (view instanceof MyWebView) {
            ((MyWebView) view).notifyWebViewLoadFinish();
        }
    }
}
