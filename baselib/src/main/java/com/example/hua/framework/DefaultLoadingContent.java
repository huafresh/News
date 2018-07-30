package com.example.hua.framework;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hua.framework.interfaces.IWindow;
import com.example.hua.framework.support.pullrefresh.IRefreshLayout;

/**
 * 默认使用的进度对话框。
 * 实现是一个圆圈 + 文本
 *
 * @author hua
 * @date 2017/6/5
 * @see IWindow
 */

public class DefaultLoadingContent extends BaseLoadingContentView {

    private View mContentView;
    private TextView mTextView;

    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
            mTextView = (TextView) mContentView.findViewById(R.id.loading_text);
        }
        return mContentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {

    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        mContentView = null;
        mTextView = null;
    }

    @Override
    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mTextView.setText(text);
        }
    }
}
