package hua.news.module_news.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.FileUtil;
import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.wrapper.emoji.EmojiKeyBoard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.FrameworkInitializer;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_common.constants.NewsConstant;
import hua.news.module_service.entitys.NormalNewsDetailBean;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_news.R;
import hua.news.module_news.R2;

/**
 * 图文类新闻详情页面
 *
 * @author hua
 * @date 2017/9/18
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {

    public static final String TAG = "NewsDetailActivity";
    private static String DETAIL_TEMPLATE_CONTENT = null;

    /**
     * 收藏操作的类型
     **/
    public static final String COLLECT_TYPE_ADD = "add";
    public static final String COLLECT_TYPE_CANCEL = "cancel";

    @BindView(R2.id.tv_bottom_follow_sum)
    TextView tvBottomFollowSum;
    @BindView(R2.id.iv_bottom_collect)
    ImageView ivBottomCollect;
    @BindView(R2.id.iv_bottom_share)
    ImageView ivBottomShare;
    @BindView(R2.id.view_follow_click_bac)
    View viewFollowBac;
    @BindView(R2.id.view_collect_click_bac)
    View viewCollectBac;
    @BindView(R2.id.view_share_click_bac)
    View viewShareBac;
    @BindView(R2.id.cl_detail_bottom_before)
    ConstraintLayout clDetailBottomBefore;
    @BindView(R2.id.edt_input_comment)
    EditText edtInputComment;
    @BindView(R2.id.cl_detail_bottom_after)
    ConstraintLayout clDetailBottomAfter;
    @BindView(R2.id.view_edit_click_bac)
    View viewEditClickBac;
    @BindView(R2.id.write_img)
    ImageView writeImg;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.iv_bottom_follow_image)
    ImageView ivBottomFollowImage;
    @BindView(R2.id.ll_follow)
    LinearLayout llFollow;
    @BindView(R2.id.view_send_click_bac)
    View viewSendClickBac;
    @BindView(R2.id.iv_expression_normal)
    ImageView ivExpressionNormal;
    @BindView(R2.id.iv_expression_input)
    ImageView ivExpressionInput;
    @BindView(R2.id.emoji_keyboard_container)
    LinearLayout emojiKeyBoardContainer;


    /**
     * 详情页跳转登录页面之前，需要先设置此变量，以便返回后执行相应操作
     */
    private int jumpType = -1;
    public static final int JUMP_TYPE_COLLECT = 0;
    public static final int JUMP_TYPE_REPLY = 1;

    /**
     * presenter
     **/
    private NewsDetailContract.Presenter mPresenter;

    /**
     * 当前显示的新闻的id
     **/
    private String mNewsId;
    private View mBottomBar;
    private MenuItem mCollectMenuItem;

    /**
     * 用于标记当前底部布局所处的状态
     **/
    private int mCurBottomState;
    private static final int BOTTOM_STATE_NORMAL = 0;
    private static final int BOTTOM_STATE_INPUT_TEXT = 1;
    private static final int BOTTOM_STATE_INPUT_EMOJI = 2;

    private String mFollowSum;


    @IntDef({
            JUMP_TYPE_COLLECT,
            JUMP_TYPE_REPLY
    })
    @interface JumpType {
    }

    /**
     * 静态方法打开Activity，避免漏传参数
     *
     * @param activity 用于打开Activity
     * @param bean     打开一个详情页所需的所有数据
     */
    public static void openDetailPage(Activity activity, NormalNewsDetailBean bean) {
        if (bean == null) {
            return;
        }
        Bundle arguments = new Bundle();
        arguments.putInt(NewsConstant.KEY_BUNDLE_SHOW_TYPE, bean.getSkip_type());
        String htmlData = "";
        try {
            htmlData = buildHtmlData(bean.getTitle(), bean.getSource(), bean.getTime(),
                    replaceImage(bean.getContent(), bean.getImages()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "openDetailPage: " + htmlData);
        arguments.putString(NewsConstant.KEY_BUNDLE_NEWS_ID, bean.getNews_id());
        arguments.putString(NewsConstant.KEY_BUNDLE_DATA, htmlData);
        arguments.putInt(NewsConstant.KEY_BUNDLE_FOLLOW_SUM, bean.getReply_count());
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtras(arguments);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    private static String buildHtmlData(String title, String source, String time, String content) {
        if (DETAIL_TEMPLATE_CONTENT == null) {
            Context context = FrameworkInitializer.getInstance().getContext();
            try {
                InputStream in = context.getAssets().open(NewsConstant.DETAIL_HTML_PATH);
                ByteArrayOutputStream array = new ByteArrayOutputStream();
                if (FileUtil.readFromSteamToStream(in, array)) {
                    DETAIL_TEMPLATE_CONTENT = array.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DETAIL_TEMPLATE_CONTENT.replace("<!--TITLE-->", title)
                .replace("<!--SOURCE-->", source)
                .replace("<!--TIME-->", time)
                .replace("<!--CONTENT-->", content);
    }

    /* *
     * 替换形如<!--IMG#0-->的图片占位符
     */
    private static String replaceImage(String content, List<String> images) {
        for (int i = 0; i < images.size(); i++) {
            String curImage = "<!--IMG#" + i + "-->";
            String nextImage = "<!--IMG#" + (i + 1) + "-->";
            //给两个连续的图片换一行
            content = content.replace(curImage + nextImage, curImage + "<br/>" + nextImage);
            String url = images.get(i);
            content = content.replace(curImage, "<img src=\"" + url + "\"/>");
        }
        return content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NewsDetailPresenter();
        mPresenter.attachView(this);

        ARouter.getInstance().inject(this);

        startDetailFragment(getIntent());
        mBottomBar = setBottombar(R.layout.news_detail_bottombar);
        ButterKnife.bind(this);
        initBottomBar();

        setListeners();
    }

    private View setBottombar(int news_detail_bottombar) {
        // TODO: 2018/4/2
        return null;
    }

    @Autowired
    ILoginManager loginManager;

    @Override
    protected void onResume() {
        super.onResume();
        switch (jumpType) {
            case JUMP_TYPE_COLLECT:
                if (loginManager != null && loginManager.isLogin()) {
                    mPresenter.addCollect(mNewsId);
                }
                break;
            case JUMP_TYPE_REPLY:
                break;
        }
        jumpType = -1;
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissEmojiKeyBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startDetailFragment(intent);
        initToolBar();
        initBottomBar();
    }

    @Override
    public void onBackPressed() {
        switch (mCurBottomState) {
            case BOTTOM_STATE_NORMAL:
                break;
            case BOTTOM_STATE_INPUT_TEXT:
                if (isSystemKeyBoardShowing()) {
                    CommonUtil.hideSystemKeyBoard(edtInputComment);
                } else {
                    changeBottomIntoNormal();
                    mCurBottomState = BOTTOM_STATE_NORMAL;
                }
                return;
            case BOTTOM_STATE_INPUT_EMOJI:
                changeBottomIntoNormal();
                mCurBottomState = BOTTOM_STATE_NORMAL;
                return;
        }
        super.onBackPressed();
    }

    private void initToolBar() {

    }

    private void startDetailFragment(Intent intent) {
        NormalNewsDetailFragment fragment = new NormalNewsDetailFragment();
        Bundle extras = intent.getExtras();
        fragment.setArguments(extras);
        setContent(fragment);

        mNewsId = extras.getString(NewsConstant.KEY_BUNDLE_NEWS_ID);
    }

    private void initBottomBar() {
        tvBottomFollowSum.setText(mFollowSum);
    }

    @Override
    protected void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.detail_more_white));

        TextView followSumView = new TextView(this);
        //设置文本
        Bundle extras = getIntent().getExtras();
        mFollowSum = extras.getString(NewsConstant.KEY_BUNDLE_FOLLOW_SUM);
        if (TextUtils.isEmpty(mFollowSum)) {
            mFollowSum = "0";
        }
        followSumView.setText(String.format("%s跟帖", mFollowSum));
        //设置文本颜色
        followSumView.setTextColor(getResources().getColor(R.color.color_white));
        //设置padding
        int paddingPixel = CommonUtil.dip2px(this, 5);
        followSumView.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        //设置布局位置
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.END;

        toolbar.addView(followSumView, lp);

        followSumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/10/2 打开评论页面
            }
        });
    }

    private void setListeners() {
        /* *
         * 因为无法监听系统键盘的弹出（或者很麻烦），这里就监听EditText的点击事件好了。
         * 点击则肯定要弹出系统键盘的。这时候要把表情键盘隐藏
         */
        edtInputComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissEmojiKeyBoard();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_more, menu);
        mCollectMenuItem = menu.findItem(R.id.more_collect);
        if (isUserCollect()) {
            mCollectMenuItem.setIcon(R.drawable.detail_menu_collect_selected);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private boolean isUserCollect() {
        if (loginManager != null && loginManager.isLogin()) {
            String curCollect = loginManager.getUserInfo().getCollect();
            return curCollect.contains(mNewsId);
        }
        return false;
    }

    @Override
    public void setJumpType(@JumpType int type) {
        jumpType = type;
    }

    @Override
    public void onCollectResult(String type, boolean isSuccess) {
        switch (type) {
            case COLLECT_TYPE_ADD:
                onAddCollect(isSuccess);
                break;
            case COLLECT_TYPE_CANCEL:
                onCancelCollect(isSuccess);
                break;
            default:
                break;
        }
    }

    private void onCancelCollect(boolean isSuccess) {
        if (isSuccess) {
            mCollectMenuItem.setIcon(R.drawable.detail_menu_collect_normal);
            ivBottomCollect.setImageResource(R.drawable.detail_menu_collect_normal);
        }
    }

    private void onAddCollect(boolean isSuccess) {
        if (isSuccess) {
            mCollectMenuItem.setIcon(R.drawable.detail_menu_collect_selected);
            mCollectMenuItem.setTitle(getString(R.string.detail_more_collect_cancel));
            ivBottomCollect.setImageResource(R.drawable.detail_menu_collect_selected);
        }
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R2.id.more_collect:
                mPresenter.addCollect(mNewsId);
                break;
            case R2.id.more_screen_shot:

                break;
            case R2.id.more_text_size:

                break;
            case R2.id.more_wrong:

                break;
            case R2.id.more_share:

                break;
            case R2.id.more_night_mode:

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);

    private int getId(View view) {
        return rCaster.cast(view.getId());
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.tv_bottom_follow_sum, R2.id.iv_bottom_collect, R2.id.iv_bottom_share,
            R2.id.view_follow_click_bac, R2.id.view_collect_click_bac, R2.id.view_share_click_bac,
            R2.id.view_edit_click_bac, R2.id.ll_follow, R2.id.view_send_click_bac,
            R2.id.iv_expression_normal, R2.id.iv_expression_input
    })
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.tv_bottom_follow_sum:
                break;
            case R2.id.iv_bottom_share:
                break;
            case R2.id.view_follow_click_bac:
                break;
            case R2.id.iv_bottom_collect:
            case R2.id.view_collect_click_bac:
                mPresenter.addCollect(mNewsId);
                break;
            case R2.id.view_share_click_bac:
                break;
            case R2.id.view_edit_click_bac:
                changeBottomIntoInput(false);
                break;
            case R2.id.ll_follow:
                break;
            case R2.id.view_send_click_bac:
                break;
            case R2.id.iv_expression_normal:
                changeBottomIntoInput(true);
                break;
            case R2.id.iv_expression_input:
                onClickEmoji();
                break;
        }
    }

    private void onClickEmoji() {
        if (mCurBottomState == BOTTOM_STATE_INPUT_TEXT) {
            mCurBottomState = BOTTOM_STATE_INPUT_EMOJI;
            showEmojiKeyBoard();
        } else if (mCurBottomState == BOTTOM_STATE_INPUT_EMOJI) {
            mCurBottomState = BOTTOM_STATE_INPUT_TEXT;
            showSystemKeyBoard();
        }
    }

    private void showEmojiKeyBoard() {
        if (!EmojiKeyBoard.getInstance().isShowing()) {
            if (isSystemKeyBoardShowing()) {
                CommonUtil.hideSystemKeyBoard(edtInputComment);
            }
            EmojiKeyBoard.getInstance().show(edtInputComment, emojiKeyBoardContainer);
        }
    }

    private void dismissEmojiKeyBoard() {
        if (EmojiKeyBoard.getInstance().isShowing()) {
            EmojiKeyBoard.getInstance().dismiss();
        }
    }

    private void showSystemKeyBoard() {
        if (!isSystemKeyBoardShowing()) {
            if (EmojiKeyBoard.getInstance().isShowing()) {
                EmojiKeyBoard.getInstance().dismiss();
            }
            CommonUtil.showSystemKeyBoard(edtInputComment);
        }
    }

    private void dismissSystemKeyBoard() {
        if (isSystemKeyBoardShowing()) {
            CommonUtil.hideSystemKeyBoard(edtInputComment);
        }
    }

    private void changeBottomIntoInput(boolean isFromEmoji) {
        clDetailBottomBefore.setVisibility(View.GONE);
        clDetailBottomAfter.setVisibility(View.VISIBLE);
        edtInputComment.requestFocus();
        CommonUtil.showSystemKeyBoard(edtInputComment);
        if (isFromEmoji) {
            mCurBottomState = BOTTOM_STATE_INPUT_EMOJI;
        } else {
            mCurBottomState = BOTTOM_STATE_INPUT_TEXT;
        }
    }

    private void changeBottomIntoNormal() {
        clDetailBottomBefore.setVisibility(View.VISIBLE);
        clDetailBottomAfter.setVisibility(View.GONE);
    }

}
