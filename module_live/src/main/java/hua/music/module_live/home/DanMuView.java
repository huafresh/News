package hua.music.module_live.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.hua.framework.classes.CommonThreadFactory;


/**
 * @author hua
 * @version 2017/12/4 14:22
 */

public class DanMuView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread mDrawThread;

    /**
     * 将要绘制的弹幕对象队列，按时间顺序排列。
     */
    private ArrayList<DanMu> mDanMuDataList = new ArrayList<>();

    /**
     * 当前正在屏幕中的弹幕对象集合
     */
    private List<DanMu> mDrawingList = new ArrayList<>();

    /**
     * 下次展示弹幕的时间起点。
     * 首次时，此值被初始化为当前系统时间减1.5s
     */
    private long mCurShowTime = 0;

    /**
     * 弹幕首次绘制时，会以当前时间为标准选择弹出为过时的弹幕。
     * 非首次时则按顺序弹出弹幕。
     */
    private boolean isFirst;

    /**
     * 显示在同一列的弹幕时间间隔
     */
    public static final long SAME_VERTICAL_LINE_TIME = 3000;

    /**
     * 文字画笔，全屏仅此一支
     */
    private Paint mTextPaint;
    private Rect mRect;

    public DanMuView(Context context) {
        this(context, null);
    }

    public DanMuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanMuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mRect = new Rect();
        isFirst = true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mDrawThread == null) {
            mDrawThread = new DrawThread(holder);
        }
        mDrawThread.isRunning = true;
        mDrawThread.start();
        isFirst = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mDrawThread != null) {
            mDrawThread.isRunning = false;
        }
    }

    private class DrawThread extends Thread {

        private boolean isRunning = false;
        private SurfaceHolder holder;

        private DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    onSurfaceViewDraw(canvas);
                    //刷新帧率
                    Thread.sleep(25);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onSurfaceViewDraw(canvas);
    }

    private void onSurfaceViewDraw(Canvas canvas) {
        //清屏
        canvas.drawColor(Color.BLACK);

        if (isFirst) {
            isFirst = false;
            mCurShowTime = System.currentTimeMillis() - 1500;
        }

        //选出mCurShowTime ~ mCurShowTime + SAME_VERTICAL_LINE_TIME时间段内的弹幕
        //每一帧有一列弹幕对象进入屏幕
        List<DanMu> tempList = new ArrayList<>();
        for (DanMu danMu : mDanMuDataList) {
            if (danMu.getTime() > mCurShowTime &&
                    danMu.getTime() < mCurShowTime + SAME_VERTICAL_LINE_TIME) {
                tempList.add(danMu);
                mCurShowTime = Math.max(danMu.getTime(), mCurShowTime);
            }
        }
        initFirstEnterDanMu(tempList);
        mDrawingList.addAll(tempList);

        //按弹幕各自的速度移动他们的坐标
        Iterator<DanMu> iterator = mDrawingList.iterator();
        while (iterator.hasNext()) {
            DanMu danMu = iterator.next();
            danMu.setX(danMu.getX() - danMu.getSpeed());
            mTextPaint.setTextSize(danMu.getTextSize());
            mTextPaint.getTextBounds(danMu.getText(), 0, danMu.getText().length(), mRect);
            int danMuWidth = mRect.right - mRect.left;
            if (danMu.getX() <= -danMuWidth) {
                DanMuFactory.getInstance().put(danMu);
                iterator.remove();
            }
            //invalidate();
        }

        //Everything is ok , let's draw all of the elements in the mDrawingList
        for (DanMu danMu : mDrawingList) {
            mTextPaint.setTextSize(danMu.getTextSize());
            mTextPaint.setColor(danMu.getTextColor());
            canvas.drawText(danMu.getText(), danMu.getX(), danMu.getY(), mTextPaint);
        }

    }

    private void initFirstEnterDanMu(List<DanMu> list) {
        if (list != null) {
            float y = 0;
            for (int i = 0; i < list.size(); i++) {
                DanMu danMu = list.get(i);
                danMu.setRecyclable(false);
                danMu.setX(getWidth());
                mTextPaint.setTextSize(danMu.getTextSize());
                float height = mTextPaint.descent() - mTextPaint.ascent();
                if (i == 0) {
                    y = height / 2 + mTextPaint.descent();
                }
                danMu.setY(y);
                y += height;
            }
        }
    }

    /**
     * 添加一条弹幕到绘制队列里。
     * 使用{@link DanMuFactory#getDanMu}获取对象。
     *
     * @param danMu 弹幕对象
     */
    public void addDanMu(DanMu danMu) {
        if (danMu == null) {
            return;
        }
        boolean added = false;
        for (int i = 0; i < mDanMuDataList.size(); i++) {
            long addTime = danMu.getTime();
            if (addTime >= mDanMuDataList.get(i).getTime()) {
                mDanMuDataList.add(i + 1, danMu);
                added = true;
                break;
            }
        }
        if (!added) {
            mDanMuDataList.add(danMu);
        }
        if (mDanMuDataList.size() == 1) {
            isFirst = true;
        }
        //invalidate();
    }

    /**
     * {@link DanMu}对象创建工厂。
     * 对象缓存的机制是：
     * 维护一个弹幕缓冲集合，存放被标记为不在屏幕内的弹幕对象，使用{@link #getDanMu}方法时
     * 优先从中获取对象(先入先出原则)，如果该缓冲集合为空，那么new新对象；
     * 从屏幕中移出的弹幕对象会被添加到缓冲集合中。
     * <p>
     * 有个问题是如果某一瞬间弹幕非常多，而后又恢复平常。那么便会产生大量的缓存对象存在集合中，如果
     * 后续弹幕的需求量一直很少，则会造成相当一部分弹幕对象存在内存里，造成浪费。
     * 为避免这种情况，采取的办法是：
     * 当弹幕的数量超过某一阈值时，则启动自动回收判定，判定的逻辑是弹幕数量在指定的时间内是否增加，
     * 如果判定通过，那么回收一半对象。如此往复，直到判定失败或者弹幕数量低于16个，则停止判定。
     * <p>
     * 确定不需要弹幕对象时，应及时调用{@link #recycler()}方法回收所有弹幕对象。
     *
     * @author hua
     * @version 2017/12/4 19:36
     */

    public static class DanMuFactory {

        private ScheduledExecutorService mScheduledService;
        /**
         * 判定有没有更多弹幕的时间间隔
         */
        private static final int NO_MORE_MAX_TIME = 5 * 1000;

        /**
         * 弹幕对象缓存池
         */
        private static List<DanMu> mDanMuPool = new ArrayList<>();

        /**
         * 池中最小对象个数
         */
        private static final int MIN_DAN_MU_COUNT = 16;

        /**
         * 阈值。超过此值开始判定回收
         */
        private static final int MAX_DAN_MU_COUNT = 256;

        /**
         * 默认的弹幕文字大小，px
         */
        private static final int DEFAULT_TEXT_SIZE = 50;

        /**
         * 默认的弹幕文字颜色
         */
        private static final int DEFAULT_TEXT_COLOR = 0xffffffff;

        /**
         * 默认的弹幕滑动最小速度，px/s
         */
        private static final int DEFAULT_MIN_SPEED = 5;

        /**
         * 默认的弹幕滑动最大速度，px/s
         */
        private static final int DEFAULT_MAX_SPEED = 15;

        /**
         * 实时记录缓存池的最大数量
         */
        private int lastMaxSize = 0;
        /**
         * 是否正在判定是否回收
         */
        private boolean isJudging = false;

        public static DanMuFactory getInstance() {
            return Holder.sInstance;
        }

        private static final class Holder {
            private static final DanMuFactory sInstance = new DanMuFactory();
        }

        private DanMuFactory() {
            mScheduledService = new ScheduledThreadPoolExecutor(1,
                    new CommonThreadFactory(DanMuFactory.class.getCanonicalName()));
        }

        /**
         * 获取弹幕对象
         *
         * @param text 弹幕内容
         * @return 弹幕对象
         */
        public DanMu getDanMu(String text) {
            return getDanMu(text, System.currentTimeMillis());
        }

        /**
         * 获取弹幕对象。
         *
         * @param text 弹幕内容
         * @param time 弹幕时间
         * @return 弹幕对象
         */
        public synchronized DanMu getDanMu(String text, long time) {
            if (mDanMuPool == null) {
                mDanMuPool = new ArrayList<>();
            }
            Iterator<DanMu> iterator = mDanMuPool.iterator();
            DanMu danMu = null;
            if (iterator.hasNext()) {
                danMu = iterator.next();
                iterator.remove();
            } else {
                danMu = buildNewDanMu(text, time);
            }
            return danMu;
        }

        @NonNull
        private static DanMu buildNewDanMu(String text, long time) {
            DanMu danMu;
            danMu = new DanMu(text, time);
            danMu.setTextSize(DEFAULT_TEXT_SIZE);
            danMu.setTextColor(DEFAULT_TEXT_COLOR);
            danMu.setSpeed(DEFAULT_MIN_SPEED);
            danMu.setRecyclable(false);
            return danMu;
        }

        private synchronized void put(DanMu danMu) {
            if (mDanMuPool == null) {
                mDanMuPool = new ArrayList<>();
            }
            if (danMu != null && !mDanMuPool.contains(danMu)) {
                danMu.setRecyclable(true);
                mDanMuPool.add(danMu);

                int curSize = mDanMuPool.size();
                if (curSize > MAX_DAN_MU_COUNT) {
                    if (curSize > lastMaxSize) {
                        if (!isJudging) {
                            startJudge(curSize);
                            isJudging = true;
                        }
                        lastMaxSize = curSize;
                    }
                }
            }
        }

        /**
         * 回收所有弹幕对象
         */
        public void recycler() {
            mDanMuPool.clear();
            mDanMuPool = null;
        }

        private class JudgeRunnable implements Runnable {
            private int size;

            private JudgeRunnable(int size) {
                this.size = size;
            }

            @Override
            public void run() {
                if (lastMaxSize == size) {
                    //说明最大值没变，那么回收一半对象
                    recyclerHalf();
                    if (mDanMuPool.size() > MIN_DAN_MU_COUNT) {
                        //继续判定
                        startJudge(lastMaxSize);
                    }
                } else {
                    isJudging = false;
                }
            }
        }

        private void startJudge(int curSize) {
            mScheduledService.schedule(new JudgeRunnable(curSize),
                    NO_MORE_MAX_TIME, TimeUnit.MILLISECONDS);
        }

        private void recyclerHalf() {
            List<DanMu> list = mDanMuPool;
            int recyclerCount = list.size() / 2;
            for (int i = 0; i < recyclerCount; i++) {
                //获取时如果存在则会自动remove
                getDanMu("recycler");
            }
        }
    }

    public static class DanMu {
        private String text;
        private int textSize;
        private int textColor;
        private long time;
        private int speed;
        /**
         * 是否可以回收
         */
        private boolean isRecyclable;

        /**
         * 弹幕在屏幕中的坐标
         */
        private float x;
        private float y;

        private DanMu(String text, long time) {
            this.text = text;
            if (time > 0) {
                this.time = time;
            }
        }

        public boolean isRecyclable() {
            return isRecyclable;
        }

        public void setRecyclable(boolean recyclable) {
            isRecyclable = recyclable;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getText() {
            return text;
        }

        /**
         * 设置弹幕的内容
         *
         * @param text 内容
         */
        public void setText(String text) {
            this.text = text;
        }

        public int getTextSize() {
            return textSize;
        }

        /**
         * 设置弹幕内容的字体大小
         *
         * @param textSize 字体大小
         */
        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public int getTextColor() {
            return textColor;
        }

        /**
         * 设置弹幕内容的字体颜色
         *
         * @param textColor 字体颜色
         */
        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public long getTime() {
            return time;
        }

        /**
         * 设置弹幕的时间。
         * 时间会决定弹幕出现的顺序
         *
         * @param time 时间。毫秒单位
         */
        public void setTime(long time) {
            this.time = time;
        }

        public int getSpeed() {
            return speed;
        }

        /**
         * 设置弹幕滚动的速度。
         * 速度都是正的，固定向左移动
         *
         * @param speed 滚动的速度。px/s
         */
        public void setSpeed(int speed) {
            if (speed < 0) {
                this.speed = 0;
            } else {
                this.speed = speed;
            }
        }

    }

}
