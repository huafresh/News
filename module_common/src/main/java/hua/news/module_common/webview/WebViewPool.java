package hua.news.module_common.webview;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;

import hua.news.module_common.utils.ConfigManager;


/**
 * Created by hua on 2017/9/18.
 * 管理WebView的创建与销毁。维护一个{@link MyWebView}缓存池，
 */
public class WebViewPool {

    private static final int DEFAULT_WEBVIEW_COUNT = 5;
    private MyWebView[] mWebViewPool;
    private int mWebCount;
    private Context mContext;

    public static WebViewPool getInstance(Context context) {
        WebViewPool instance = HOLDER.sInstance;
        instance.init(context);
        return instance;
    }

    private static final class HOLDER {
        private static final WebViewPool sInstance = new WebViewPool();
    }

    /**
     * 初始化方法，使用前必须调用
     *
     * @param context 上下文
     */
    public void init(Context context) {
        mContext = context;

        mWebCount = -1;
        try {
            mWebCount = Integer.valueOf(ConfigManager.getInstance()
                    .getItemConfigValue("WebViewCacheCount"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (mWebCount == -1) {
            mWebCount = DEFAULT_WEBVIEW_COUNT;
        }
        mWebViewPool = new MyWebView[mWebCount];
    }

    /**
     * 从WebView缓存池中获取一个{@link MyWebView}对象，如果没有缓存，则会创建一个
     *
     * @return {@link MyWebView}对象
     */
    public MyWebView getWebView() {
        if (mWebViewPool == null) {
            throw new IllegalStateException("there is no call to the initialization method {@link init()}");
        }

        MyWebView myWebView = null;
        for (int i = 0; i < mWebCount; i++) {
            myWebView = mWebViewPool[i];
            if (myWebView != null && myWebView.getWebViewState() == MyWebView.WebViewState.ACCESSIBLE) {
                return myWebView;
            }
        }
        myWebView = mWebViewPool[0];
        if (myWebView == null) {
            myWebView = buildWebView(mContext);
            addWebView(myWebView);
        }

        myWebView.setWebViewState(MyWebView.WebViewState.ACCESSIBLE);

        return myWebView;
    }

    private static MyWebView buildWebView(Context context) {
        MyWebView webView = new MyWebView(context);
        webView.setWebViewState(MyWebView.WebViewState.INITIALIZATION);
        initWebView(webView);

        WebSettings settings = webView.getSettings();
        initWebSetting(settings);

        return webView;
    }

    private static void initWebView(MyWebView webView) {
        webView.setWebViewClient(new BaseWebViewClient());
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private static void initWebSetting(WebSettings webSettings) {
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
    }


    private void addWebView(MyWebView myWebView) {
        for (int i = 0; i < mWebCount; i++) {
            if (mWebViewPool[i] == null) {
                mWebViewPool[i] = myWebView;
                return;
            }
        }
        mWebViewPool[0] = myWebView;
    }

}