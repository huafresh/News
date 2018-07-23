package com.example.hua.framework.wrapper.emoji.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.interfaces.IIndicator;

/**
 * Created by hua on 2017/10/8.
 * 表情页面接口
 */

public interface IEmojiPage {

    /** 获取页面视图，注意需要包含指示器布局 */
    View createContentView(Context context, ViewGroup container);

    /** 设置页面指示器 */
    void setPageIndicator(IIndicator IIndicator);

    /** 设置删除表情的名称，默认会使用“emoji_delete” */
   void setDeleteEmojiName(String deleteEmojiName);

    /** 绑定输入的目标 */
    void bindTarget(EditTextWrapper target);

}
