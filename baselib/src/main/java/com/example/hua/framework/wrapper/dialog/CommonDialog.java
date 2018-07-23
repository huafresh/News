package com.example.hua.framework.wrapper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hua.framework.interfaces.IWindow;

/**
 * 通用的{@link Dialog}
 *
 * @author hua
 * @date 2017/6/5
 * @see IWindow
 */

public class CommonDialog implements IWindow.IContainer {

    private Dialog mDialog;
    private Context mContext;
    private IWindow.IContentView mIContentView;

    public CommonDialog(@NonNull Context context) {
        initDialog(context);
    }

    private void initDialog( Context context) {
        mContext = context;
        mDialog = new Dialog(context);
    }

    /**
     * 设置Dialog 布局ID。
     *
     * @param layoutResId id
     */
    public CommonDialog setView(@LayoutRes int layoutResId) {
        View view = LayoutInflater.from(mContext).inflate(layoutResId, null);
        if (view != null) {
            dismiss();
            mDialog.setContentView(view);
            show();
        }
        return this;
    }

    /**
     * @param view 设置Dialog布局
     */
    public CommonDialog setView(View view) {
        if (view != null) {
            dismiss();
            mDialog.setContentView(view);
            show();
        }
        return this;
    }

    @Override
    public void addContentView(IWindow.IContentView contentView) {
        if (contentView != null) {
            View view = contentView.getContentView(mContext);
            if (view != null) {
                dismiss();
                mDialog.setContentView(view);
                mIContentView = contentView;
                contentView.onAttachToContainer(this);
            }
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    /**
     * 设置dialog样式
     *
     * @param style dialog样式
     */
    public void setDialogStyle(@StyleRes int style) {
        mDialog = new Dialog(mContext, style);
    }

    /**
     * 显示dialog
     */
    public void show() {
        mDialog.show();
    }

    /**
     * 隐藏dialog
     */
    public void dismiss() {
        mDialog.dismiss();
        if (mIContentView != null) {
            mIContentView.onDetachContainer(this);
            mIContentView = null;
        }
    }

    /**
     * 设置dialog隐藏时的监听
     *
     * @param l 监听
     */
    public void setOnDialogDismissListener(DialogInterface.OnDismissListener l) {
        mDialog.setOnDismissListener(l);
    }

    @Override
    public void removeContentView(IWindow.IContentView contentView) {
        dismiss();
    }
}
