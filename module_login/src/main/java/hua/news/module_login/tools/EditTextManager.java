package hua.news.module_login.tools;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.hua.framework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用于管理页面中的EditText以及被同步的View。
 * <p>
 * 调用{@link #addEditText(EditText, String)}添加EditText，
 * 调用{@link #addSyncView(View)}设置需要被同步的按钮。
 * 如果view被enable时有其他操作，可以使用{@link #setOnEnableListener(View, OnEnableListener)}设置监听
 * 确定不需要同步时请调用{@link #recycle}
 * <p>
 * example :
 * mManager = new EditTextManager();
 * mManager.addEditText(mEdPhoneNum, EditTextManager.LEGAL_MODE_PHONE);
 * mManager.addSyncView(mIvIconPhone);
 * mManager.addSyncView(mTvSendVerifyCode);
 * .....
 * mManager.recycle();
 *
 * @author hua
 * @date 2017/6/23
 */

public class EditTextManager {

    //所有的EditText集合
    private HashMap<EditText, String> mEditTextMap;
    //页面中被同步的view
    private List<View> mSyncViews;
    //被同步view的监听集合
    private HashMap<View, OnEnableListener> mSyncListeners;

    //只要不为空即合法
    public static final String LEGAL_MODE_NORMAL = "normal";
    //输入手机号合法
    public static final String LEGAL_MODE_PHONE = "phone";
    //输入邮箱合法
    public static final String LEGAL_MODE_MAIL = "mail";

    //输入大于4位时合法
    public static final String LEGAL_MODE_MORE_FORE = "legal_mode_more_fore";
    //输入大于6位时合法
    public static final String LEGAL_MODE_MORE_SIX = "legal_mode_more_six";


    public EditTextManager() {
        mEditTextMap = new HashMap<>();
        mSyncViews = new ArrayList<>();
        mSyncListeners = new HashMap<>();
    }

    private boolean isLegal(String text, String flag) {
        boolean legal = false;
        switch (flag) {
            case LEGAL_MODE_NORMAL:
                legal = !TextUtils.isEmpty(text);
                break;
            case LEGAL_MODE_PHONE:
                legal = CommonUtil.isPhone(text);
                break;
            case LEGAL_MODE_MAIL:
                legal = CommonUtil.isEmail(text);
                break;
            case LEGAL_MODE_MORE_FORE:
                legal = !TextUtils.isEmpty(text) && text.length() >= 4;
                break;
            case LEGAL_MODE_MORE_SIX:
                legal = !TextUtils.isEmpty(text) && text.length() >= 6;
            default:
                break;
        }
        return legal;
    }

    private void syncViews() {
        //先置enable，一旦发现非法，则置为false
        boolean isEnable = true;
        for (EditText editText : mEditTextMap.keySet()) {
            String syncFlag = mEditTextMap.get(editText);
            String text = editText.getText().toString().trim();
            boolean isLegal = isLegal(text, syncFlag);
            if (!isLegal) {
                isEnable = false;
                break;
            }
        }
        if (mSyncViews != null) {
            for (View mSyncView : mSyncViews) {
                mSyncView.setEnabled(isEnable);
                if (mSyncListeners.containsKey(mSyncView)) {
                    OnEnableListener listener = mSyncListeners.get(mSyncView);
                    if (listener != null) {
                        if (isEnable) {
                            listener.onEnable();
                        } else {
                            listener.onDisable();
                        }
                    }
                }
            }
        }
    }


    /**
     * 把你界面中的EditText设置进来。
     *
     * @param editText 输入框
     * @param flag     输入框的合法判断类型
     */
    public void addEditText(EditText editText, String flag) {
        if (editText != null && !mEditTextMap.containsKey(editText)) {
            mEditTextMap.put(editText, flag);
            editText.addTextChangedListener(new EditTextWatch() {
                @Override
                public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                    syncViews();
                }
            });
        }
    }

    /**
     * 设置需要同步的按钮
     */
    public void addSyncView(View view) {
        if (mSyncViews == null) {
            mSyncViews = new ArrayList<>();
        }
        mSyncViews.add(view);
        view.setEnabled(false);
    }

    /**
     * 释放资源
     */
    public void recycle() {
        mEditTextMap.clear();
        mEditTextMap = null;
        mSyncViews.clear();
        mSyncViews = null;
        mSyncListeners.clear();
        mSyncListeners = null;
    }

    /**
     * 用于简化{@link TextWatcher}的接口数量，适用于只需知道text变化的情况
     */
    public static abstract class EditTextWatch implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            onChange(charSequence, i, i1, i2);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        /**
         * EditText输入内容有变化时调用
         */
        public abstract void onChange(CharSequence charSequence, int i, int i1, int i2);
    }

    /**
     * 给view设置enable的监听
     *
     * @param view
     * @param listener
     */
    public void setOnEnableListener(View view, OnEnableListener listener) {
        if (mSyncViews.contains(view)) {
            mSyncListeners.put(view, listener);
        }
    }

    /**
     * 当被同步的view enable状态改变时调用，满足特殊需求
     */
    public interface OnEnableListener {
        void onEnable();

        void onDisable();
    }

}
