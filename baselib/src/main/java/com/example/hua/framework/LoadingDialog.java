package com.example.hua.framework;

import android.app.Activity;
import android.text.TextUtils;

import com.example.hua.framework.wrapper.dialog.CommonDialog;


/**
 * 加载对话框。
 * 默认使用的布局UI是{@link DefaultLoadingContent}
 *
 * @author hua
 * @version 2017/10/22 13:55
 */

public class LoadingDialog {

    private CommonDialog mCommonDialog;
    private BaseLoadingContentView mContentView;

    public LoadingDialog(Activity activity) {
        this(activity, null);
    }

    public LoadingDialog(Activity activity, BaseLoadingContentView mContentView) {
        mCommonDialog = new CommonDialog(activity);
        if (mContentView != null) {
            this.mContentView = mContentView;
        } else {
            this.mContentView = new DefaultLoadingContent();
        }
    }

    public void show() {
        show(null);
    }

    public void show(CharSequence text){
        mCommonDialog.addContentView(mContentView);
        if (!TextUtils.isEmpty(text)) {
            mContentView.setText(text);
        }
        mCommonDialog.show();
    }

    public void dismiss(){
        mCommonDialog.dismiss();
    }

}
