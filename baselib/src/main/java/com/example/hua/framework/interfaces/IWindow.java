package com.example.hua.framework.interfaces;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 此接口提供{@link IContainer}和{@link IContentView}两个接口，
 * 是对容器+内容的抽象
 *
 * @author hua
 * @version 2017/6/29
 */
public interface IWindow {

    /**
     * 容器接口。
     */
    interface IContainer {
        /**
         * 往容器添加内容
         *
         * @param contentView 要添加的内容
         */
        void addContentView(IContentView contentView);

        /**
         * 移除指定内容
         *
         * @param contentView 要移除的内容
         */
        void removeContentView(IContentView contentView);
    }

    /**
     * 内容接口
     */
    interface IContentView {

        /**
         * 获取内容实体视图
         *
         * @param context Context
         * @return 内容实体视图
         */
        View getContentView(Context context);

        /**
         * 被添加进容器时调用
         *
         * @param container 被添加进的容器
         */
        void onAttachToContainer(IContainer container);

        /**
         * 被移除出容器时调用
         *
         * @param container 移除内容的容器
         */
        void onDetachContainer(IContainer container);
    }

}
