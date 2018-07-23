package com.example.hua.framework.wrapper.emoji.core;

/**
 * Author: hua
 * Created: 2017/10/11
 * Description:
 * 表情图片内存缓存接口
 */

public interface IEmojiMemoryCache<K,V> {

    void putEmoji(K key, V value);

    V getEmoji(K key);
}
