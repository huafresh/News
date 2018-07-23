package com.example.hua.framework.wrapper.emoji.defaults;

import android.content.Context;

import com.example.hua.framework.wrapper.emoji.core.EmojiEntity;
import com.example.hua.framework.wrapper.emoji.core.EmojiKeyBoardConfig;
import com.example.hua.framework.wrapper.emoji.core.IEmojiBottomTab;
import com.example.hua.framework.wrapper.emoji.core.IEmojiMemoryCache;
import com.example.hua.framework.wrapper.emoji.core.IEmojiPage;


/**
 * Author: hua
 * Created: 2017/10/10
 * Description:
 * 表情键盘配置的默认实现。这个实现与qq微信弹出的表情键盘基本类似。
 */

public class BaseEmojiKeyBoardConfig extends EmojiKeyBoardConfig {

    @Override
    public IEmojiPage createEmojiPage(Context context, EmojiEntity emojiEntity) {
        EmojiStandardPage emojiStandardPage = new EmojiStandardPage(emojiEntity);
        emojiStandardPage.setPageIndicator(new CircleIndicator());
        return emojiStandardPage;
    }

    @Override
    public IEmojiBottomTab createBottomTab() {
        return new EmojiStandardBottomTab();
    }

    @Override
    public IEmojiMemoryCache getMemoryCache() {
        return new EmojiLruCacheStrategy();
    }

    @Override
    public boolean enableBottomTab() {
        return false;
    }
}
