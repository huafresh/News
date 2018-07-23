package com.example.hua.framework.wrapper.emoji.defaults;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.framework.interfaces.IIndicator;
import com.example.hua.framework.utils.CommonUtil;

import com.example.hua.framework.wrapper.emoji.EmojiKeyBoard;
import com.example.hua.framework.wrapper.emoji.core.EditTextWrapper;
import com.example.hua.framework.wrapper.emoji.core.EmojiEntity;
import com.example.hua.framework.wrapper.emoji.core.IEmojiMemoryCache;
import com.example.hua.framework.wrapper.emoji.core.IEmojiPage;
import com.example.hua.framework.utils.SimpleViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by hua on 2017/10/8.
 * 一个经典表情页面的实现。
 * 3 x 7列放置表情图片，最后一个是删除键，下方是圆点指示器。
 * 如果需求有细微的更改，可以继承此类稍作调整即可
 */

public class EmojiStandardPage implements IEmojiPage {

    public static final int COLUMN_COUNT = 7;

    public static final int ROW_COUNT = 3;

    public static final int PER_PAGE_COUNT = COLUMN_COUNT * ROW_COUNT;

    private static final String DEFAULT_DELETE_EMOJI_NAME = "emoji_delete.png";
    /** 没有表情图片的名称 */
    private static final String EMPTY_EMOJI_NAME = "empty";

    /** 表情输入的目标 */
    private EditTextWrapper mEditTextWrapper;

    /** 创建此表情页面使用的资源 */
    private EmojiEntity mEmojiEntity;

    /** 指示器 */
    private IIndicator mIndicator;

    /** 当前页面图片来源的目录名称 */
    private final String mEmojiDirName;

    /** 表情页面的高度，会被初始化为1/4屏幕高度 */
    private int mEmojiPageHeight = 0;

    /** 删除表情的名称 */
    private String mDeleteEmojiName;

    /** 当前手指触摸GridView的状态 */
    private int mCurMotionAction = -1;

    private Handler mMainHandle = new Handler(Looper.getMainLooper());


    public EmojiStandardPage(EmojiEntity emojiEntity) {
        mEmojiEntity = emojiEntity;
        mEmojiDirName = mEmojiEntity.getEmojiDirName();
        mDeleteEmojiName = resolveDefaultDeleteEmojiName(emojiEntity);
    }

    private String resolveDefaultDeleteEmojiName(EmojiEntity emojiEntity) {
        List<String> emojiNames = emojiEntity.getEmojiNames();
        if (emojiNames != null) {
            for (String emojiName : emojiNames) {
                if (emojiName.equals(DEFAULT_DELETE_EMOJI_NAME)) {
                    return emojiName;
                }
            }
        }
        return null;
    }

    @Override
    public View createContentView(Context context, ViewGroup container) {

        List<String> emojiNames = mEmojiEntity.getEmojiNames();
        int pageCount = calculatePageCount(emojiNames);

        mEmojiPageHeight = CommonUtil.getScreenHeight(context) / 4;

        //显示表情图片的ViewPager
        ViewPager viewPager = createViewPager(context);
        List<View> viewList = createGridViews(context, pageCount);
        SimpleViewPagerAdapter pagerAdapter = new SimpleViewPagerAdapter(context,viewList);
        viewPager.setAdapter(pagerAdapter);

        //指示器布局
        mIndicator.bindViewPager(viewPager);

        //添加进容器
        LinearLayout mPageContainer = new LinearLayout(context);
        mPageContainer.setOrientation(LinearLayout.VERTICAL);
        mPageContainer.addView(viewPager);
        mPageContainer.addView(mIndicator.getContentView(context, pageCount));

        int topAndBottomPadding = dip2px(context, 10);
        mPageContainer.setPadding(0, topAndBottomPadding, 0, topAndBottomPadding);

        return mPageContainer;
    }


    private int calculatePageCount(List<String> emojiNames) {
        if (emojiNames != null) {
            int size = emojiNames.size();
            if (size % PER_PAGE_COUNT == 0) {
                return size / PER_PAGE_COUNT;
            } else {
                return size / PER_PAGE_COUNT + 1;
            }
        }
        return 0;
    }

    @NonNull
    private ViewPager createViewPager(Context context) {
        ViewPager viewPager = new ViewPager(context);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mIndicator != null) {
                    mIndicator.setPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setLayoutParams(onCreateViewPagerParams(viewPager));
        return viewPager;
    }

    /**
     * 这里返回的布局参数高度必须是确定值。否则指示器不会显示
     *
     * @return 布局参数
     */
    protected LinearLayout.LayoutParams onCreateViewPagerParams(ViewPager viewPager) {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mEmojiPageHeight);
    }

    private List<View> createGridViews(Context context, int pageCount) {
        List<View> views = new ArrayList<>();
        List<String> totalEmojiNames = mEmojiEntity.getEmojiNames();
        removeDeleteEmojiName(totalEmojiNames);
        int index = 0;
        for (int i = 0; i < pageCount; i++) {
            List<String> emojiNames = new ArrayList<>();
            for (int l = 0; l < PER_PAGE_COUNT; l++) {
                if (!TextUtils.isEmpty(mDeleteEmojiName)) {
                    if (l == PER_PAGE_COUNT - 1) { //最后一个表情图片替换为删除图片
                        emojiNames.add(mDeleteEmojiName);
                        continue;
                    }
                }
                String name = "";
                try {
                    //index用于标识当前从totalEmojiName拿图片名称的下标，每拿一次加1
                    name = totalEmojiNames.get(index++);
                } catch (Exception e) {
                    e.printStackTrace();
                    //添加占位名称，那么删除图片就永远在GridView最后一个
                    name = EMPTY_EMOJI_NAME;
                }
                emojiNames.add(name);
            }
            views.add(createGridView(context, emojiNames));
        }
        return views;
    }

    private void removeDeleteEmojiName(List<String> emojiNames) {
        for (String emojiName : emojiNames) {
            if (emojiName.equals(mDeleteEmojiName)) {
                emojiNames.remove(emojiName);
                return;
            }
        }
    }

    private GridView createGridView(Context context, List<String> emojiNames) {
        GridView gridView = new GridView(context);
        gridView.setNumColumns(COLUMN_COUNT);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((TextView) view).getText().toString();
                if (buildEmojiText(mEmojiDirName, mDeleteEmojiName).equals(text)) { //按删除键
                    sendDeleteEvent();
                } else {
                    mEditTextWrapper.insertText(text);
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((TextView) view).getText().toString();
                if (buildEmojiText(mEmojiDirName, mDeleteEmojiName).equals(text)) {
                    SendDeleteEventThread thread = new SendDeleteEventThread();
                    thread.start();
                }
                return true;
            }
        });
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCurMotionAction = event.getAction();
                return false;
            }
        });
        GridViewAdapter adapter = new GridViewAdapter(context, mEmojiDirName, emojiNames);
        gridView.setAdapter(adapter);
        onCreateGrideView(gridView);
        return gridView;
    }

    private class SendDeleteEventThread extends Thread{
        @Override
        public void run() {
            int delayTime = 200;
            while (mCurMotionAction != MotionEvent.ACTION_UP) {
                //发送删除事件需在主线程执行
                mMainHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        sendDeleteEvent();
                    }
                });
                /* *
                 * 这里的延时处理大致模拟出了软键盘长按删除键的效果。
                 * 本人才疏学浅，不晓得有没有“长按删除”这样的事件，
                 * 有的话就好了，直接发送给EditText即可。
                 */
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delayTime -= 50;
                if (delayTime == 50 || delayTime < 0) {
                    delayTime = 50;
                }
            }
        }
    }

    private void sendDeleteEvent() {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
        mEditTextWrapper.dispatchKeyEvent(event);
    }

    protected void onCreateGrideView(GridView gridView) {

    }

    @Override
    public void setPageIndicator(IIndicator indicator) {
        this.mIndicator = indicator;
    }

    @Override
    public void setDeleteEmojiName(String deleteEmojiName) {
        mDeleteEmojiName = deleteEmojiName;
    }

    @Override
    public void bindTarget(EditTextWrapper target) {
        mEditTextWrapper = target;
    }

    private class GridViewAdapter extends BaseAdapter {

        private final Context mContext;
        private final List<String> mEmojiNames;
        private final String mEmojiDirName;


        private GridViewAdapter(Context mContext, String dirPath, List<String> emojiNames) {
            this.mContext = mContext;
            mEmojiDirName = dirPath;
            mEmojiNames = emojiNames;
        }

        @Override
        public int getCount() {
            return mEmojiNames != null ? mEmojiNames.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mEmojiNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            if (convertView != null) {
                view = convertView;
            } else {
                view = onCreateGridItemView(mContext);
                if (view == null || !(view instanceof TextView)) {
                    throw new IllegalArgumentException("create gridView item view can not be null," +
                            "and must be instanceof TextView");
                }
            }

            TextView textView = (TextView) view;
            String emojiName = mEmojiNames.get(position);
            if (emojiName.equals(EMPTY_EMOJI_NAME)) {
                textView.setText("");
            } else {
                SpannableString text = new SpannableString(buildEmojiText(mEmojiDirName, emojiName));
                String cacheKey = EmojiKeyBoard.buildEmojiAbsolutePath(mContext, mEmojiDirName, emojiName);
                IEmojiMemoryCache<String, Bitmap> cacheStrategy = EmojiKeyBoard.getInstance().getCacheStrategy();
                Bitmap emojiBitmap = cacheStrategy.getEmoji(cacheKey);
                if (emojiBitmap == null) {
                    emojiBitmap = BitmapFactory.decodeFile(cacheKey);
                    cacheStrategy.putEmoji(cacheKey, emojiBitmap);
                }
                ImageSpan imageSpan = new ImageSpan(mContext, emojiBitmap);
                text.setSpan(imageSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(text);
            }

            return view;
        }

    }

    @NonNull
    private String buildEmojiText(String dir, String emojiName) {
        return "<emoji>" + dir + ":" + emojiName + "<emoji/>";
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    protected TextView onCreateGridItemView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        int paddingTopAndBottom = dip2px(context, 10);
        textView.setPadding(0, paddingTopAndBottom, 0, paddingTopAndBottom);
        return textView;
    }

}
