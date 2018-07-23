package com.example.hua.framework;

import com.example.hua.framework.interfaces.IWindow;

/**
 * 内容接口的扩展，增加针对加载对话框的方法
 *
 * @author hua
 * @version 2017/10/22 13:57
 * @see IWindow
 */

public abstract class BaseLoadingContentView implements IWindow.IContentView {

    /**
     * 设置显示的文本
     *
     * @param text 显示的文本
     */
    protected void setText(CharSequence text) {

    }

    /**
     * 设置加载进度
     * @param progress
     */
    protected void setProgress(int progress){

    }

    /**
     * 获取当前加载进度
     * @return
     */
    protected int getProgress(){
        return 0;
    }


}
