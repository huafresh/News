package com.example.hua.framework.wrapper.emoji.core;

import java.util.List;

/**
 * Author: hua
 * Created: 2017/10/10
 * Description:
 * 一个表情包实体
 */

public class EmojiEntity {

    private int emojiType;
    private String emojiDirName;
    private List<String> emojiNames;

    public int getEmojiType() {
        return emojiType;
    }

    public void setEmojiType(int emojiType) {
        this.emojiType = emojiType;
    }

    public String getEmojiDirName() {
        return emojiDirName;
    }

    public void setEmojiDirName(String emojiDirName) {
        this.emojiDirName = emojiDirName;
    }

    public List<String> getEmojiNames() {
        return emojiNames;
    }

    public void setEmojiNames(List<String> emojiNames) {
        this.emojiNames = emojiNames;
    }
}
