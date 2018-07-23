package com.example.hua.framework.wrapper.emoji.core;

import android.content.Context;

import com.example.hua.framework.wrapper.emoji.EmojiKeyBoard;

import java.util.HashMap;
import java.util.List;


/**
 * Author: hua
 * Created: 2017/10/10
 * Description:
 * 表情键盘参数的配置，提供表情键盘显示需要的所有资源，比如：
 * 显示表情图片的布局，自定义的指示器布局，底部滚动布局等等。
 */

public abstract class EmojiKeyBoardConfig {

    /**
     * 默认情况下会根据表情包根目录下文件夹的个数来决定表情页面的个数。这里
     * 可以对参数emojiNameMap进行操作，以便控制需要加载哪些表情包以及哪些图片能够被加载
     * <p>
     * 表情包的根目录是：context.getExternalFilesDir(null)+"/"+{@link EmojiKeyBoard#EMOJI_ROOT_DIR_NAME}，
     * 可以在该目录中新建文件夹，然后放入表情图片，{@link EmojiKeyBoard}会自动加载它们
     *
     * @param emojiNameMap 本地表情包解析结果
     */
    public void adjustPageCount(HashMap<String, List<String>> emojiNameMap) {

    }

    /**
     * 创建一个表情页面。
     * 这里的表情页面是除底部滚动布局外的视图，一般包含GridView + 指示器
     *
     * @param emojiEntity 表情包实体。包含了创建一个表情页面所需的所有资源
     * @return 一个表情页面的实现
     */
    public abstract IEmojiPage createEmojiPage(Context context, EmojiEntity emojiEntity);


    /**
     * 是否显示底部滚动条
     *
     * @return true显示，false不显示
     */
    public boolean enableBottomTab() {
        return true;
    }

    /**
     * 创建底部可滑动视图
     *
     * @return 底部可滑动视图实例
     */
    public IEmojiBottomTab createBottomTab() {
        return null;
    }

    /**
     * 获取图片缓存策略
     *
     * @return 缓存接口
     */
    public IEmojiMemoryCache getMemoryCache() {
        return null;
    }
}
