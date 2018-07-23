package com.example.hua.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.hua.framework.utils.PictureUtil;

/**
 * Created by hua on 2017/7/17.
 * 圆形图片
 */
@Deprecated
public class CircleImage extends AppCompatImageView {

    private int width;
    private int height;
    private Context mContext;
    private Drawable mRoundDrawable;

    public CircleImage(Context context) {
        this(context, null);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRoundDrawable == null) {
            mRoundDrawable = PictureUtil.roundPicture(mContext, getDrawable());
            mRoundDrawable.setBounds(0, 0, width, height);
        }
        mRoundDrawable.draw(canvas);
    }
}
