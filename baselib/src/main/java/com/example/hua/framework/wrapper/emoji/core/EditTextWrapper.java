package com.example.hua.framework.wrapper.emoji.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.hua.framework.wrapper.emoji.EmojiKeyBoard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Author: hua
 * Created: 2017/10/13
 * Description:
 * 支持EditText处理表情图片
 */

public class EditTextWrapper {

    private static final String EMOJI_PREFIX = "<emoji>";
    private static final String EMOJI_SUFFIX = "<emoji/>";
    private EditText mEditText;

    public EditTextWrapper(EditText mEditText) {
        this.mEditText = mEditText;
    }

    /**
     * 发送事件到EditText。
     * 对于删除事件，需做特殊处理
     *
     * @param event 事件对象
     */
    public void dispatchKeyEvent(KeyEvent event) {
        if (mEditText != null) {
            mEditText.dispatchKeyEvent(event);
        }
    }

    /**
     * 在EditText光标所在处插入文本或图片
     *
     * @param text 需要插入的内容
     */
    public void insertText(CharSequence text) {

        if (TextUtils.isEmpty(text) || mEditText == null) {
            return;
        }

        int curSelection = mEditText.getSelectionStart();
        StringBuilder builder = new StringBuilder(mEditText.getText());
        builder.insert(curSelection, text);
        setText(builder.toString());
        mEditText.setSelection(curSelection + text.length());
    }

    /**
     * 设置EditText的内容
     * 自动解析形如："<emoji>emojiDir:emojiName<emoji/>"的字符串
     *
     * @param text 要设置的EditText的内容
     */
    public void setText(CharSequence text) {
        String regex = "<emoji>(.+?)<emoji/>";
        Pattern patternEmotion = Pattern.compile(regex);
        Matcher matcherEmotion = patternEmotion.matcher(text);
        SpannableString spannableString = new SpannableString(text);
        while (matcherEmotion.find()) {
            try {
                String emoji = matcherEmotion.group();
                String dirAndName = emoji.replace(EMOJI_PREFIX, "").replace(EMOJI_SUFFIX, "");
                String[] splits = dirAndName.split(":");
                String dir = splits[0];
                String name = splits[1];
                String path = EmojiKeyBoard.buildEmojiAbsolutePath(mEditText.getContext(), dir, name);
                IEmojiMemoryCache<String, Bitmap> memoryCache = EmojiKeyBoard.getInstance().getCacheStrategy();
                Bitmap bitmap = memoryCache.getEmoji(path);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(path);
                }
                ImageSpan span = new ImageSpan(mEditText.getContext(), bitmap);
                int start = matcherEmotion.start();
                spannableString.setSpan(span, start, start + emoji.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mEditText.setText(spannableString);
    }
}
