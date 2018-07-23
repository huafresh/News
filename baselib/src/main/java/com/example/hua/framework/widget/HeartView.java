package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.hua.framework.R;

/**
 * Created by hua on 2017/9/20.
 * 使用三阶贝塞尔曲线实现的自定义心形视图。有实心和空心两种显示类型
 */

public class HeartView extends View {

    private static final int DEFAULT_STROKE_WIDTH = 4; //px
    private static final int DEFAULT_WIDTH = 60; // px
    private static final int DEFAULT_HEIGHT = 50; // px
    private static final int POINT_COUNT = 6;
    private int mHeartStyle;
    private int mStrokeColor;
    private int mFillColor;
    private int mWidth;
    private int mHeight;
    private Paint mStrokePaint;
    private Paint mFillPaint;
    private Path mPath;
    private Point[] mPoints;
    private int mStrokeWidth;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        getAttrs(context, attrs);

        mStrokePaint = new Paint();
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        mFillPaint = new Paint();
        mFillPaint.setColor(mFillColor);
        mFillPaint.setAntiAlias(true);

        mPath = new Path();
        //心的外围轨迹需要6个点来确定
        mPoints = new Point[POINT_COUNT];

    }


    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeartView);
        int defaultColor = context.getResources().getColor(R.color.colorPrimary);
        mHeartStyle = array.getInt(R.styleable.HeartView_HeartStyle, 1);
        mStrokeColor = array.getColor(R.styleable.HeartView_StrokeColor, defaultColor);
        mFillColor = array.getColor(R.styleable.HeartView_StrokeColor, defaultColor);
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.HeartView_StrokeWidth, DEFAULT_STROKE_WIDTH);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            //设为wrap_content时，父容器会传一个最大值过来，我们在传过来的最大值
            //和默认值中取一个较小的值
            int mostWidth = MeasureSpec.getSize(widthMeasureSpec);
            mWidth = Math.min(mostWidth, DEFAULT_WIDTH);
        } else {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            //设为wrap_content时，父容器会传一个最大值过来，我们在传过来的最大值
            //和默认值中取一个较小的值
            int mostHeight = MeasureSpec.getSize(heightMeasureSpec);
            mHeight = Math.min(mostHeight, DEFAULT_HEIGHT);
        } else {
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
        }

        initPointsAndPath();

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mHeartStyle == 1) { //实心
            canvas.drawPath(mPath, mFillPaint);
        } else { //空心
            canvas.drawPath(mPath, mStrokePaint);
        }
    }

    /* *
     * 初始化6个点和path。
     * path就是心形的外围轨迹，使用三阶贝塞尔曲线确定，主要就是三个点的位置确认。
     * 以下点的位置来源于网上一张图：http://blog.csdn.net/u011506583/article/details/51989868。
     */
    private void initPointsAndPath() {
        for (int i = 0; i < POINT_COUNT; i++) {
            mPoints[i] = new Point();
        }

        //画线模式下，需要进行位置补偿，使得心形被完全包围在画布内
        int strokeOffset = mHeartStyle == 2 ? mStrokeWidth : 0;
        mPoints[0].set(mWidth / 2, 17 * mHeight / 100);
        mPoints[1].set(3 * mWidth / 20, -7 * mHeight / 20 + strokeOffset);
        mPoints[2].set(-2 * mWidth / 5 + strokeOffset, 9 * mHeight / 20);
        mPoints[3].set(mWidth / 2, mHeight - strokeOffset);
        mPoints[4].set(2 * mWidth / 5 + mWidth - strokeOffset, 9 * mHeight / 20);
        mPoints[5].set(mWidth - 3 * mHeight / 20, -7 * mHeight / 20 + strokeOffset);

        mPath.moveTo(mPoints[0].x, mPoints[0].y);
        mPath.cubicTo(mPoints[1].x, mPoints[1].y,
                mPoints[2].x, mPoints[2].y,
                mPoints[3].x, mPoints[3].y);
        mPath.cubicTo(mPoints[4].x, mPoints[4].y,
                mPoints[5].x, mPoints[5].y,
                mPoints[0].x, mPoints[0].y);
    }
}
