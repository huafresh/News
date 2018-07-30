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
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 经典下拉刷新头
 *
 * @author hua
 * @version 2018/7/24 9:35
 */

public abstract class BaseClassicHeader extends FrameLayout implements IHeader {

    /**
     * 文本
     */
    protected static final String PULL_TO_REFRESH_TEXT = "下拉可以刷新";
    protected static final String RELEASE_TO_REFRESH_TEXT = "松开后刷新";
    protected static final String REFRESHING_TEXT = "正在刷新中";
    protected static final String LAST_REFRESH_TIME_TEXT = "上次刷新时间: HH:mm:ss";
    protected DateFormat mFormat = new SimpleDateFormat(LAST_REFRESH_TIME_TEXT, Locale.getDefault());

    private Context context;
    private ImageView arrowImageView;
    private ImageView loadingImageView;
    private TextView titleTextView;
    private TextView lastTimeTextView;
    private LinearInterpolator linearInterpolator;

    public BaseClassicHeader(Context context) {
        this(context, null);
    }

    public BaseClassicHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseClassicHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        this.linearInterpolator = new LinearInterpolator();
    }

    @NonNull
    @Override
    public View getView() {
        View headerView = LayoutInflater.from(context).inflate(layoutId(), this, false);
        arrowImageView = headerView.findViewById(arrowImageId());
        loadingImageView = headerView.findViewById(loadingImageId());
        titleTextView = headerView.findViewById(titleTextViewId());
        lastTimeTextView = headerView.findViewById(lastTimeTextViewId());
        updateLastTime(new Date());
        addView(headerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void onStateChanged(IRefreshLayout refreshLayout, HeaderState oldState, HeaderState newState) {
        switch (newState) {
            case None:
                updateLastTime(new Date());
                stopLoading();
                Log.e("@@@hua","state none");
                break;
            case Pull_Down_To_Refresh:
                titleTextView.setText(PULL_TO_REFRESH_TEXT);
                arrowImageView.setVisibility(VISIBLE);
                loadingImageView.setVisibility(INVISIBLE);
                arrowImageView.animate().rotation(0);
                Log.e("@@@hua","state Pull_Down_To_Refresh");
                break;
            case Release_To_Refresh:
                titleTextView.setText(RELEASE_TO_REFRESH_TEXT);
                arrowImageView.animate().rotation(180);
                Log.e("@@@hua","state Release_To_Refresh");
                break;
            case Refreshing:
                titleTextView.setText(REFRESHING_TEXT);
                arrowImageView.setVisibility(INVISIBLE);
                loadingImageView.setVisibility(VISIBLE);
                startLoading();
                Log.e("@@@hua","state Refreshing");
                break;
            default:
                break;
        }
    }

    private void updateLastTime(Date date){
        lastTimeTextView.setText(mFormat.format(date));
    }

    private void stopLoading() {
        Drawable drawable = loadingImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        } else {
            loadingImageView.animate()
                    .setInterpolator(linearInterpolator)
                    .rotation(0).setDuration(300);
        }
    }

    private void startLoading() {
        Drawable drawable = loadingImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        } else {
            loadingImageView.animate()
                    .setInterpolator(linearInterpolator)
                    .rotation(36000)
                    .setDuration(100000);
        }
    }

    protected abstract @LayoutRes
    int layoutId();

    protected abstract @IdRes
    int arrowImageId();

    protected abstract @IdRes
    int loadingImageId();

    protected abstract @IdRes
    int titleTextViewId();

    protected abstract @IdRes
    int lastTimeTextViewId();

}
