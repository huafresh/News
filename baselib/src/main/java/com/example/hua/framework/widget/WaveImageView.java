package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.hua.framework.R;


/**
 * 底部带波浪线的ImageView。
 * 此控件需要手动设置波浪线空白处显示的颜色，{@link #setEmptyColor(int)}.
 * 如果不设置，则取父布局的背景颜色，如果还是没有，则默认白色。
 * 个人觉得这么搞较为不优雅，但暂时无解决办法。
 *
 * @author hua
 * @version 2018/2/27 10:11
 */

public class WaveImageView extends AppCompatImageView {

    private static final int DEF_WAVE_HEIGHT = 20;
    private static final int DEF_WAVE_CYCLE_SUM = 1;
    private static final int DEF_WAVE_SECOND_COLOR = 0x66ffffff;
    private static final int DEF_WAVE_EMPTY_COLOR = 0xffffffff;
    private static final int DEF_WAVE_SPEED = 10;
    private static final int DEF_SECOND_CYCLE_MULTIPLE = 1;
    private static final int OFFSET_MULTIPLE = 1;

    private Paint mFirstPathPaint;
    private Paint mSecondPathPaint;

    private Path mFirstPath;
    private Path mSecondPath;
    private Context mContext;
    private int mScreenWidth;

    /**
     * 第一条波浪线的左边偏移值，是path的X轴方向的起点
     */
    private int mFirstLeftOffset;

    /**
     * 第二条波浪线的左边偏移值，是path的X轴方向的起点
     */
    private int mSecondLeftOffset;

    private LeftOffsetRunnable mLeftOffsetRunnable;
    private int mWaveHeight;

    /**
     * 波浪线在屏幕内展示的周期数
     */
    private int mFirstCycleSum;
    private int mSecondCycleSum;

    /**
     * 第二条波浪线突起时的颜色值
     */
    private int mWaveSecondColor;

    private int mWaveSpeed;
    private int mWidth;
    private int mHeight;

    private Drawable mParentBac;

    /**
     * 1/4周期的宽度值
     */
    private int mDeltaX;

    private int mEmptyColor;

    public WaveImageView(Context context) {
        this(context, null);
    }

    public WaveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        mScreenWidth = getScreenWidth(mContext);

        obtainAttrs(attrs, defStyle);

        mFirstPathPaint = new Paint();
        mFirstPathPaint.setStyle(Paint.Style.FILL);
        mFirstPathPaint.setColor(mEmptyColor);
        mFirstPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mFirstPathPaint.setAntiAlias(true);

        mSecondPathPaint = new Paint();
        mSecondPathPaint.setStyle(Paint.Style.FILL);
        mSecondPathPaint.setAntiAlias(true);
        mSecondPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mSecondPathPaint.setColor(mWaveSecondColor);

        mFirstLeftOffset = -mScreenWidth;
        mSecondLeftOffset = mFirstLeftOffset - mDeltaX * OFFSET_MULTIPLE;
        mLeftOffsetRunnable = new LeftOffsetRunnable();
        mFirstPath = new Path();
        mSecondPath = new Path();

        ViewCompat.postOnAnimation(this, mLeftOffsetRunnable);
    }

    private void obtainAttrs(AttributeSet attrs, int defStyle) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.WaveImageView);
        mWaveHeight = array.getDimensionPixelSize(R.styleable.WaveImageView_wave_height, DEF_WAVE_HEIGHT);
        mFirstCycleSum = array.getInt(R.styleable.WaveImageView_wave_cycle, DEF_WAVE_CYCLE_SUM);
        mFirstCycleSum = Math.max(1, mFirstCycleSum);
        int multiple = array.getInt(R.styleable.WaveImageView_wave_second_cycle_multiple, DEF_SECOND_CYCLE_MULTIPLE);
        mSecondCycleSum = mFirstCycleSum * multiple;
        mWaveSecondColor = array.getColor(R.styleable.WaveImageView_wave_second_color, DEF_WAVE_SECOND_COLOR);
        mWaveSpeed = array.getDimensionPixelSize(R.styleable.WaveImageView_wave_speed, DEF_WAVE_SPEED);
        mDeltaX = mScreenWidth / (mFirstCycleSum * 4);
        mEmptyColor = array.getColor(R.styleable.WaveImageView_wave_empty_color, DEF_WAVE_EMPTY_COLOR);
        array.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mParentBac == null) {
            ViewGroup parent = (ViewGroup) getParent();
            mParentBac = parent.getBackground();
            if (mParentBac instanceof ColorDrawable) {
                if (mEmptyColor == DEF_WAVE_EMPTY_COLOR) {
                    mEmptyColor = ((ColorDrawable) mParentBac).getColor();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
    }

    /**
     * 波浪线绘制思路：
     * 一共绘制2 * screenWidth长度的波浪线，根据Offset决定其起点
     * Offset的取值范围是[-screenWidth，0]，Offset循环逐渐递增，
     * 由此产生波浪线效果。
     */
    private void drawWave(Canvas canvas) {
        mFirstPath.reset();
        mSecondPath.reset();

        final int minHeight = mHeight - mWaveHeight;
        final int maxHeight = mHeight;
        final int middleHeight = (maxHeight + minHeight) / 2;
        int i = 0, j = 0;

        //第一条波浪线的path
        mFirstPath.moveTo(mFirstLeftOffset, 0);
        mFirstPath.lineTo(mFirstLeftOffset, middleHeight);
        //波浪线长度要确保大于屏幕宽度
        int count = (mScreenWidth - mFirstLeftOffset) / mDeltaX + 1;
        for (i = 1, j = 0; i <= count; i += 2, j++) {
            int height = j % 2 == 0 ? maxHeight : minHeight;
            mFirstPath.quadTo(mFirstLeftOffset + mDeltaX * i, height,
                    mFirstLeftOffset + mDeltaX * (i + 1), middleHeight);
        }
        mFirstPath.lineTo(mFirstLeftOffset + mDeltaX * (i + 1), maxHeight);
        mFirstPath.lineTo(mFirstLeftOffset, maxHeight);
        mFirstPath.close();

        //第二条波浪线的path
        mSecondPath.moveTo(mSecondLeftOffset, 0);
        mSecondPath.lineTo(mSecondLeftOffset, middleHeight);
        int count2 = (mScreenWidth - mSecondLeftOffset) / mDeltaX + 1;
        for (i = 1, j = 0; i <= count2; i += 2, j++) {
            int height = j % 2 == 0 ? maxHeight : minHeight;
            mSecondPath.quadTo(mSecondLeftOffset + mDeltaX * i, height,
                    mSecondLeftOffset + mDeltaX * (i + 1), middleHeight);
        }
        mSecondPath.lineTo(mSecondLeftOffset + mDeltaX * (i + 1), maxHeight);
        mSecondPath.lineTo(mSecondLeftOffset, maxHeight);
        mSecondPath.close();

        //绘制
        canvas.drawPath(mSecondPath, mSecondPathPaint);
        mFirstPathPaint.setColor(mEmptyColor);
        canvas.drawPath(mFirstPath, mFirstPathPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics outMetric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetric);
            return outMetric.widthPixels;
        }
        return -1;
    }

    /**
     * 设置空白处的颜色
     *
     * @param color 颜色值
     */
    public void setEmptyColor(int color) {
        mEmptyColor = color;
    }

    private class LeftOffsetRunnable implements Runnable {

        @Override
        public void run() {
            int offset = Math.min(-mFirstLeftOffset, mWaveSpeed);
            mFirstLeftOffset += offset;
            if (mFirstLeftOffset == 0) {
                mFirstLeftOffset = -mScreenWidth;
            }
            mSecondLeftOffset = mFirstLeftOffset - mDeltaX * OFFSET_MULTIPLE;

            invalidate();

            ViewCompat.postOnAnimation(WaveImageView.this, this);
        }
    }

}
