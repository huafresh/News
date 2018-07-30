package com.example.hua.framework.support.pullrefresh;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

/**
 * 经典上拉加载更多
 *
 * @author hua
 * @version 2018/7/24 9:35
 */

public abstract class BaseClassicFooter extends FrameLayout implements IFooter {

    /**
     * 文本
     */
    protected static final String PULL_TO_LOAD_TEXT = "上拉可以加载";
    protected static final String RELEASE_TO_LOAD_TEXT = "松开后加载";
    protected static final String LOADING_TEXT = "正在加载下一页";

    private Context context;
    private ImageView loadingImageView;
    private TextView textView;

    public BaseClassicFooter(Context context) {
        this(context, null);
    }

    public BaseClassicFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseClassicFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
    }

    @NonNull
    @Override
    public View getView() {
        View footerView = LayoutInflater.from(context).inflate(layoutId(), this, false);
        loadingImageView = footerView.findViewById(loadingImageId());
        textView = footerView.findViewById(textViewId());
        addView(footerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void onStateChanged(IRefreshLayout refreshLayout, FooterState oldState, FooterState newState) {
        switch (newState) {
            case None:
                stopLoading();
                break;
            case Pull_Up_To_Load:
                textView.setText(PULL_TO_LOAD_TEXT);
                loadingImageView.setVisibility(INVISIBLE);
                break;
            case Release_To_Load:
                textView.setText(RELEASE_TO_LOAD_TEXT);
                break;
            case Loading:
                textView.setText(LOADING_TEXT);
                loadingImageView.setVisibility(VISIBLE);
                startLoading();
                break;
            default:
                break;
        }
    }

    private void stopLoading() {
        Drawable drawable = loadingImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        } else {
            loadingImageView.animate().rotation(0).setDuration(300);
        }
    }

    private void startLoading() {
        Drawable drawable = loadingImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        } else {
            loadingImageView.animate().rotation(36000).setDuration(100000);
        }
    }

    protected abstract @LayoutRes
    int layoutId();

    protected abstract @IdRes
    int loadingImageId();

    protected abstract @IdRes
    int textViewId();

}
