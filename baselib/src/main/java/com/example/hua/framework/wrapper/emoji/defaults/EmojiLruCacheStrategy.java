package com.example.hua.framework.wrapper.emoji.defaults;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.example.hua.framework.wrapper.emoji.core.IEmojiMemoryCache;


/**
 * Author: hua
 * Created: 2017/10/11
 * Description:
 * 表情图片缓存策略LruCache实现
 */

public class EmojiLruCacheStrategy implements IEmojiMemoryCache<String, Bitmap> {
    private LruCache<String, Bitmap> mLurCache;
    private static final int MAX_SIZE = 10 * 1024 * 1024; //10M

    public EmojiLruCacheStrategy() {
        mLurCache = new LruCache<>(MAX_SIZE);
    }

    @Override
    public void putEmoji(String key, Bitmap value) {
        mLurCache.put(key, value);
    }

    @Override
    public Bitmap getEmoji(String key) {
        return mLurCache.get(key);
    }

}
