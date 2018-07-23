package com.example.hua.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hua on 2017/7/3.
 * 仿网易点击式验证码。
 * 使用方法如下：
 * 1、XML中进行布局
 * 2、设置{@link OnTextChangeListener}监听以实时获取需要用户点击验证的汉字
 * 3、设置{@link OnClickFinishListener}监听以获取用户点击完毕的验证结果
 * 4、调用{@link #refresh()}刷新控件
 * 5、调用{@link #setBacDrawableId}设置随机显示的背景图片资源id
 */

public class ClickVerifyCode extends AppCompatImageView {

    //最大显示的文字个数
    private static final int MAX_TEXT_SUM = 6;
    //显示文字的大小
    private static final int TEXT_SIZE = 50; //px
    //显示文字的颜色
    private static final int TEXT_COLOR = 0xff000000;
    //确认勾的颜色
    private static final int CONFIRM_COLOR = 0xffffffff;
    //确认勾外圆的颜色
    private static final int CIRCLE_OUT_COLOR = 0xddffffff;
    //确认勾内圆的颜色
    private static final int CIRCLE_IN_COLOR = 0xff3F51B5;
    //上下文
    private Context mContext;
    //画笔
    private Paint mPaint;
    //控件的宽度
    private int width;
    //控件的高度
    private int height;
    //一个汉字的高度，同时也是宽度
    private int mTextHeight;
    //保存已经画好的汉字的中心坐标以及旋转角度等信息
    private List<TextInfoBean> mTextInfoList;
    //保存勾的中点信息
    private List<Point> mHookInfoList;
    //保存干扰曲线绘制的关键点
    private List<Point> mCurveInfoList;
    //干扰曲线绘制的关键点个数
    private static final int MAX_CURVE_POINT_SUM = 7;
    //干扰横线的个数
    private static final int MAX_LINE_SUM = 3;
    //保存干扰横线的起始点
    private List<Point> mLineStartList;
    //方便复用
    private Rect mTextBounds = new Rect();
    //监听手势
    private GestureDetector mGestureDetector;
    //确认勾画笔
    private Paint mConfirmPaint;
    //确认勾左边的长度
    private static final int LEFT_HOOK_LENGTH = 13;
    //确认勾右边的长度
    private static final int RIGHT_HOOK_LENGTH = LEFT_HOOK_LENGTH + 6;
    //确认勾的宽度
    private static final int HOOK_WIDTH = 4;
    //外圆半径
    private static final int CIRCLE_OUT_RADIUS = 20;
    //内圆半径
    private static final int CIRCLE_IN_RADIUS = CIRCLE_OUT_RADIUS - 3;
    //最多可显示的确认勾的个数
    private static final int MAX_CONFIRM_SUM = 3;
    //判断用户点击时是否正确的系数，值越大，越容易通过验证
    private static final float CORRECT_SCALE = 0.15f;
    private OnClickFinishListener mClickFinishListener;
    private OnTextChangeListener mOnTextChangeListener;
    private int[] mDrawableIds;
    //用于绘制干扰曲线
    private Path mPath;


    public ClickVerifyCode(Context context) {
        this(context, null);
    }

    public ClickVerifyCode(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickVerifyCode(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        //获取自定义属性
        obtainAttrs(attrs);
    }

    /**
     * 用于保存一个随机生成的汉字的位置信息
     */
    private class TextInfoBean {
        //用于drawText
        private Point drawPoint;
        //汉字中心点
        private Point centerPoint;
        //汉字旋转角度
        private float degree;
        //汉字
        private String text;


        Point getDrawPoint() {
            return drawPoint;
        }

        void setDrawPoint(Point drawPoint) {
            this.drawPoint = drawPoint;
        }

        Point getCenterPoint() {
            return centerPoint;
        }

        void setCenterPoint(Point centerPoint) {
            this.centerPoint = centerPoint;
        }

        float getDegree() {
            return degree;
        }

        void setDegree(float degree) {
            this.degree = degree;
        }

        String getText() {
            return text;
        }

        void setText(String text) {
            this.text = text;
        }
    }

    private void init(Context context) {
        mContext = context;
        //初始化汉字画笔
        mPaint = new Paint();
        mPaint.setTextSize(TEXT_SIZE);
        mPaint.setColor(TEXT_COLOR);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1f);
        //初始化确认勾画笔
        mConfirmPaint = new Paint();
        mConfirmPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mConfirmPaint.setStrokeWidth(HOOK_WIDTH);
        mConfirmPaint.setAntiAlias(true);
        //10为预留空间，保证汉字旋转也能正常显示
        mTextHeight = (int) (mPaint.descent() - mPaint.ascent()) + 10;

        mTextInfoList = new ArrayList<>();
        mHookInfoList = new ArrayList<>();
        mCurveInfoList = new ArrayList<>();
        mLineStartList = new ArrayList<>();
        mPath = new Path();

        //监听点击事件
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                handleConfirmed(e.getX(), e.getY());
                return true;
            }
        });
    }

    private void handleConfirmed(float x, float y) {

        if (mHookInfoList.size() < MAX_CONFIRM_SUM) {
            Point point = new Point((int) x, (int) y);
            mHookInfoList.add(point);
            invalidate();

            //勾的数量已达到最大值，则回调接口通知外部使用者验证情况
            if (mHookInfoList.size() == MAX_CONFIRM_SUM) {
                if (mClickFinishListener != null) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccess = resolveClickResult();
                            mClickFinishListener.onFinish(isSuccess);
                            if (isSuccess) {
                                dismiss();
                            }
                        }
                    }, 800); //这里延时让最后一个勾画完
                }
            }
        }
    }

    //刷新页面的内容
    private void refreshViewContent() {
        //随机生成汉字以及位置
        mTextInfoList.clear();
        for (int i = 0; i < MAX_TEXT_SUM; i++) {
            //产生随机旋转角度
            float degree = getRandomDegree();
            float x;
            float y;
            //产生随机汉字
            String text = getChineseText();
            do {
                //产生随机位置
                x = getRandomX();
                y = getRandomY();
            } while (isOverlap(x, y, text));

            //保存信息
            TextInfoBean posInfo = new TextInfoBean();
            posInfo.setDrawPoint(new Point((int) x, (int) y));
            posInfo.setCenterPoint(new Point((int) getCenterX(text, x), (int) getCenterY(text, y)));
            posInfo.setDegree(degree);
            posInfo.setText(text);

            mTextInfoList.add(posInfo);
        }

        //需要点击验证的汉字改变，回调接口
        if (mOnTextChangeListener != null) {
            mOnTextChangeListener.onChange(getClickText());
        }

        //清空勾的位置信息
        mHookInfoList.clear();

        //随机设置一个背景图
        if (mDrawableIds != null) {
            int i = new Random().nextInt(mDrawableIds.length - 1);
            setImageResource(mDrawableIds[i]);
        }

        //随机生成干扰曲线的关键点
        mCurveInfoList.clear();
        for (int i1 = 0; i1 < MAX_CURVE_POINT_SUM; i1++) {
            //每个点的x轴偏移
            int offset = (int) (width * 1.0f / (MAX_CURVE_POINT_SUM - 1) * i1);
            Point point = new Point(offset, (int) getRandomY());
            mCurveInfoList.add(point);
        }
        //对4、6、8、、、等点进行矫正，使其有利于画曲线
        redressPoint();

        //随机生成干扰横线的起始点
        mLineStartList.clear();
        for (int i = 0; i < MAX_LINE_SUM; i++) {
            Point point = new Point((int) getRandomX(), (int) getRandomY());
            mLineStartList.add(point);
        }
    }

    private void redressPoint() {
        for (int i = 0; i < mCurveInfoList.size(); i++) {
            //矫正峰值点
            if (i > 2 && i % 2 == 1) {
                int prePreY = mCurveInfoList.get(i - 2).y;
                int preY = mCurveInfoList.get(i - 1).y;
                int curY = mCurveInfoList.get(i).y;

                if (prePreY < preY && curY < preY ||
                        prePreY > preY && curY > preY) { //需要矫正
                    curY += (preY - curY) * 2;
                }
                if (curY < 0) {
                    curY = 0;
                }
                if (curY > height) {
                    curY = height;
                }
                mCurveInfoList.get(i).y = curY;
            }

            //矫正0值点
            if (i > 1 && i % 2 == 0) {
                mCurveInfoList.get(i).y = mCurveInfoList.get(i - 2).y;
            }
        }
    }


    private void obtainAttrs(AttributeSet attrs) {
        //do nop

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) { //wrap_content
            width = mTextHeight * MAX_TEXT_SUM;
        } else {
            width = Math.max(mTextHeight * MAX_TEXT_SUM, MeasureSpec.getSize(widthMeasureSpec));
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) { //wrap_content
            height = mTextHeight;
        } else {
            height = Math.max(mTextHeight, MeasureSpec.getSize(heightMeasureSpec));
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        refreshViewContent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画汉字
        drawText(canvas);
        //画干扰曲线
        drawCurve(canvas);
        //画干扰横线
        drawLine(canvas);
        //画勾
        drawHook(canvas);
    }

    private void drawLine(Canvas canvas) {
        mPaint.setStrokeWidth(3f);
        for (int i = 0; i < MAX_LINE_SUM; i++) {
            Point point = mLineStartList.get(i);
            canvas.drawLine(point.x, point.y, point.x + mTextHeight/2, point.y, mPaint);
        }
    }

    private void drawCurve(Canvas canvas) {
        mPath.reset();
        //移到起点
        Point firstPoint = mCurveInfoList.get(0);
        mPath.moveTo(firstPoint.x, firstPoint.y);
        //矫正后的0值点纵坐标都是一致的，在一条水平线上
        int zeroY = firstPoint.y;

        //每90度对应的横向像素值
        int pixesPer90Degree = width / (MAX_CURVE_POINT_SUM - 1);

        //区域数量
        int areaSum = (MAX_CURVE_POINT_SUM - 1) / 2;

        //从左到右连接每个区域的每个点
        for (int i = 1; i < areaSum + 1; i++) {
            //区域峰值
            int maxY = Math.abs(zeroY - mCurveInfoList.get(i * 2 - 1).y);

            for (int x = (i - 1) * pixesPer90Degree * 2; x < i * pixesPer90Degree * 2; x++) {
                mPath.lineTo(x, zeroY - getSin(90 * 1.0f / pixesPer90Degree * x) * maxY);
            }
        }

//        for (int i = areaSum; i > 0; i--) {
//            //区域峰值
//            int maxY = Math.abs(zeroY - mCurveInfoList.get(i * 2 - 1).y);
//
//            for (int x = i * pixesPer90Degree * 2 - 1; x >= (i - 1) * pixesPer90Degree * 2; x--) {
//                mPath.lineTo(x, zeroY - getSin(90 * 1.0f / pixesPer90Degree * x) * maxY);
//            }
//        }

        canvas.drawPath(mPath, mPaint);
    }

    private void redressBacPoint() {

    }

    /**
     * 解析用户点击的正确性。
     * 简单起见，这里让用户选择的汉字就是随机生成的第1，3，5个汉字。
     * 判断方法是使用勾股定理判断两个点的距离是否小于一定的值
     *
     * @return 是否验证通过
     */
    private boolean resolveClickResult() {
        if (mHookInfoList != null && mTextInfoList != null &&
                mTextInfoList.size() > mHookInfoList.size() * 2 - 1) {
            for (int i = 0; i < mHookInfoList.size(); i++) {
                Point confirmPoint = mHookInfoList.get(i);
                Point textCenterPoint = mTextInfoList.get(2 * i + 1).getCenterPoint();
                //阈值
                float threshold = (float) ((height / 2.0f * Math.sqrt(2) + CIRCLE_OUT_RADIUS) * CORRECT_SCALE);

                if (!(getPointDistance(confirmPoint, textCenterPoint) < threshold)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void drawHook(Canvas canvas) {
        for (Point point : mHookInfoList) {

            int centerX = point.x;
            int centerY = point.y;

            /* * 画圆* */

            //画外圆
            mConfirmPaint.setColor(CIRCLE_OUT_COLOR);
            canvas.drawCircle(centerX, centerY, CIRCLE_OUT_RADIUS, mConfirmPaint);
            //画内圆
            mConfirmPaint.setColor(CIRCLE_IN_COLOR);
            canvas.drawCircle(centerX, centerY, CIRCLE_IN_RADIUS, mConfirmPaint);

            /* * 画勾* */

            mConfirmPaint.setColor(CONFIRM_COLOR);

            //勾的边长
            float left = (getSin(45) * LEFT_HOOK_LENGTH);
            float right = (getSin(45) * RIGHT_HOOK_LENGTH);

            //勾的起始坐标（算法请在纸上理解一波 ~ - ~ ）
            float startX = centerX - (left + right) / 2;
            float startY = centerY - (left - right / 2);

            //画勾左边直线
            canvas.drawLine(startX, startY, startX + left, startY + left, mConfirmPaint);
            //画勾右边直线
            float offset = getSin(45) * HOOK_WIDTH / 2;
            canvas.drawLine(startX + left - offset, startY + left + offset, startX + left + right,
                    startY + left - right, mConfirmPaint);
        }
    }

    private float getSin(float degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

    private void drawText(Canvas canvas) {
        mPaint.setStrokeWidth(1f);
        for (TextInfoBean infoBean : mTextInfoList) {
            Point drawPoint = infoBean.getDrawPoint();
            float degree = infoBean.getDegree();
            drawRotateText(canvas, drawPoint.x, drawPoint.y, degree, infoBean.getText());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private float getRandomX() {
        return (float) (Math.random() * (width - mTextHeight));

    }

    private float getRandomY() {
        return (float) (Math.random() * (height - mTextHeight) - mPaint.ascent());
    }

    /**
     * 随机获取一个旋转角度，范围是[-45 , 45]
     *
     * @return 旋转角度
     */
    private float getRandomDegree() {
        return (float) (Math.random() * 90 - 45);
    }

    /**
     * 判断生成的x，y坐标，将来画出来的文字是否会与之前的文字重叠
     *
     * @param randomX x
     * @param randomY y
     * @return 是否重叠
     */
    private boolean isOverlap(float randomX, float randomY, String text) {
        mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        int randomCenterX = (int) getCenterX(text, randomX);
        int randomCenterY = (int) getCenterY(text, randomY);

        boolean isOverlap = false;

        for (TextInfoBean bean : mTextInfoList) {
            Point centerPoint = bean.getCenterPoint();
            //勾股定理
            float pointDistance = getPointDistance(centerPoint, new Point(randomCenterX, randomCenterY));
            if (pointDistance > 0 && pointDistance < mTextHeight * Math.sqrt(2)) {
                isOverlap = true;
                break;
            }
        }
        return isOverlap;
    }

    //计算两点的距离
    private float getPointDistance(Point point1, Point point2) {
        return (float) Math.sqrt(Math.pow(point1.x - point2.x, 2)
                + Math.pow(point1.y - point2.y, 2));
    }

    private float getCenterX(String text, float x) {
        mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        return x + mTextBounds.width() / 2;
    }

    private float getCenterY(String text, float y) {
        mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        return y - (mTextBounds.height() / 2 - mPaint.descent());
    }

    /**
     * 随机获取一个汉字。
     * <p>
     * 说明：
     * 1.汉字机内码的字节范围是0xB0A0 ~ 0xF7FF
     * 高8位范围是0xB0 ~ 0xF7
     * 低8位范围是0xA1 ~ 0xFE
     *
     * @return 随机汉字
     */
    private String getChineseText() {
        Random random = new Random();
        String s = "";
        byte[] bytes = new byte[2];
        //高8位
        bytes[0] = (byte) (random.nextInt(0xF7 - 0xB0 + 1) + 0xB0);
        //低8位
        bytes[1] = (byte) (random.nextInt(0xFE - 0xA1 + 1) + 0xA1);
        try {
            s = new String(bytes, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 画一个有一定旋转角度的汉字
     *
     * @param canvas 画布
     * @param x      x坐标，同{@link Canvas#drawText}参数
     * @param y      y坐标，同{@link Canvas#drawText}参数
     * @param degree 旋转的角度
     * @param text   文字
     */
    private void drawRotateText(Canvas canvas, float x, float y, float degree, String text) {
        canvas.save();
        mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        int width = mTextBounds.width();
        int height = mTextBounds.height();
        //base line到文字中心的垂直距离
        int tmpY = (int) (height / 2 - mPaint.descent());
        canvas.translate(x + width / 2, y - tmpY);
        canvas.rotate(degree, 0, 0);
        canvas.drawText(text, -width / 2, tmpY, mPaint);
        canvas.restore();
    }

    public void refresh() {
        refreshViewContent();
        invalidate();
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void dismiss() {
        setVisibility(INVISIBLE);
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    public void setBacDrawableId(@DrawableRes int[] ids) {
        mDrawableIds = ids;
    }

    private String getClickText() {
        String text = "";
        for (int i = 0; i < MAX_CONFIRM_SUM; i++) {
            TextInfoBean posInfo = mTextInfoList.get(i * 2 + 1);
            if (posInfo != null) {
                text += "\"" + posInfo.getText() + "\"";
            }
        }
        return text;
    }

    /**
     * 点击结束监听,图中达到{@link #MAX_CONFIRM_SUM}个勾的时候调用
     */
    public interface OnClickFinishListener {

        void onFinish(boolean isSuccess);
    }

    /**
     * 设置点击结束时的监听
     */
    public void setOnClickFinishListener(OnClickFinishListener l) {
        mClickFinishListener = l;
        setClickable(true);
    }


    /**
     * 界面刷新时，需要点击完成验证的汉字也会改变，若改变则会回调此监听
     */
    public interface OnTextChangeListener {

        //text是格式化的需要点击验证的汉字，比如："滋""只""付"
        void onChange(String text);
    }

    /**
     * 设置文字变化的监听
     */
    public void setOnTextChangeListener(OnTextChangeListener l) {
        mOnTextChangeListener = l;
    }

}
