package hua.news.module_common.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/9/18.
 * webView封装
 */

public class MyWebView extends WebView {

    private int mWebViewState = WebViewState.NONE;
    private List<LoadFinishedListener> mLoadFinishListeners;

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }

    public interface WebViewState {
        /** 不可用状态 */
        int NONE = 0;
        /** 初始化状态 */
        int INITIALIZATION = 1;
        /** 可用状态 */
        int ACCESSIBLE = 2;
        /** 正在加载状态 */
        int LOADING = 3;
        /** 加载完毕状态 */
        int COMPLETED = 4;
    }

    public int getWebViewState() {
        return mWebViewState;
    }

    /**
     * 回收WeibView，使之可以被复用
     */
    public void recycler() {
        this.mWebViewState = WebViewState.ACCESSIBLE;

        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }

        if (mLoadFinishListeners != null) {
            mLoadFinishListeners.clear();
        }
    }

    void setWebViewState(int state){
        this.mWebViewState = state;
    }

    public void addLoadFinishListener(LoadFinishedListener l){
        if (mLoadFinishListeners == null) {
            mLoadFinishListeners = new ArrayList<>();
        }
        if (l != null) {
            mLoadFinishListeners.add(l);
        }
    }

    /**
     * 页面加载完毕的回调
     */
    public interface LoadFinishedListener{
        void onLoadFinish(MyWebView webView);
    }

    void notifyWebViewLoadFinish(){
        if (mLoadFinishListeners != null) {
            for (LoadFinishedListener listener : mLoadFinishListeners) {
                listener.onLoadFinish(this);
            }
        }
        mWebViewState = WebViewState.COMPLETED;
    }

    /**
     * 通过html地址加载html
     */
    public void loadUrl(String url){
        super.loadUrl(url);
        mWebViewState = MyWebView.WebViewState.LOADING;
    }

    /**
     * 加载html格式的字符串
     */
    public void loadData(String htmlString){
        super.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
        mWebViewState = MyWebView.WebViewState.LOADING;
    }

}
