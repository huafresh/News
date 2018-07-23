package hua.news.module_main.home;

import android.view.ViewGroup;

import com.example.hua.framework.interfaces.IWindow;

/**
 * 使用ViewGroup作为容器的简单实现
 *
 * @author hua
 * @version 2017/10/22 12:33
 * @see IWindow
 */

public class ViewGroupContainer implements IWindow.IContainer {
    private ViewGroup mViewGroup;

    public ViewGroupContainer(ViewGroup mViewGroup) {
        this.mViewGroup = mViewGroup;
    }

    @Override
    public void addContentView(IWindow.IContentView contentView) {
        if (contentView != null) {
            mViewGroup.addView(contentView.getContentView(mViewGroup.getContext()));
            contentView.onAttachToContainer(this);
        }
    }

    @Override
    public void removeContentView(IWindow.IContentView contentView) {
        if (contentView != null) {
            mViewGroup.removeView(contentView.getContentView(mViewGroup.getContext()));
            contentView.onDetachContainer(this);
        }
    }
}
