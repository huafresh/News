package hua.news.module_user.usercenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.divider.LinearItemDecoration;
import com.example.hua.framework.tools.avatar.AvatarHelper;
import com.example.hua.framework.tools.avatar.OnCropOverListener;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.wrapper.imageload.ImageLoad;
import com.example.hua.framework.wrapper.recyclerview.HeaderAndFootWrapper;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_common.utils.ConfigManager;
import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_user.R;
import hua.news.module_user.R2;

/**
 * Created by hua on 2017/7/11.
 * 个人中心页面
 */

public class UserCenterActivity extends BaseActivity implements UserCenterContract.View {

    @BindView(R2.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R2.id.tv_user_nick_name)
    TextView tvUserNickName;
    @BindView(R2.id.tv_user_follow_level)
    TextView tvUserFollowLevel;
    @BindView(R2.id.iv_user_sex)
    ImageView ivUserSex;
    @BindView(R2.id.app_bar)
    AppBarLayout appBar;
    @BindView(R2.id.tv_my_notice_number)
    TextView tvMyNoticeNumber;
    @BindView(R2.id.ll_my_notice)
    LinearLayout llMyNotice;
    @BindView(R2.id.tv_my_fans_number)
    TextView tvMyFansNumber;
    @BindView(R2.id.ll_my_fans)
    LinearLayout llMyFans;
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.ll_user_info)
    LinearLayout llUserInfo;
    @BindView(R2.id.card_view)
    CardView cardView;
    @BindView(R2.id.collaps_toolbar)
    CollapsingToolbarLayout collapsToolbar;
    private UserFollowHistoryAdapter adapter;

    //触发开始隐藏的偏移量
    private static final int START_OFFSET = 10;
    //完全隐藏的偏移量
    private static final int END_OFFSET = 100;
    //最大可滑动的距离
    public int max_offset = 0;

    private UserCenterContract.Presenter mPresenter;
    private AvatarHelper mAvatarHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        if (setStatusBarTranslucent()) {
            addHeadHeight();
        }
        setPresenter(new UserCenterPresenter());
        mPresenter.attachView(this);

        initDatas();
        initViews();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void addHeadHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0及以上才需要增加
            CollapsingToolbarLayout.LayoutParams lp =
                    (CollapsingToolbarLayout.LayoutParams) llUserInfo.getLayoutParams();
            lp.topMargin += CommonUtil.getStatusBarHeight(this);
            llUserInfo.setLayoutParams(lp);
        }
    }

    private void initDatas() {
        //使toolbar正式接替actionbar
        toolbar.setTitle(""); //在后面设置无效
        setSupportActionBar(toolbar);

    }

    @Autowired
    ILoginManager loginManager;

    private void initViews() {
        if (loginManager == null) {
            return;
        }
        LoginUserInfo userInfo = loginManager.getUserInfo();
        //初始化RecyclerView IAdapter
        adapter = new UserFollowHistoryAdapter(this);
        adapter.setDataList(userInfo.getFollow_history_info_list());

        //RecyclerView头部添加一个空的view，高度是24dp
        View emptyView = new View(this);
        LinearLayout emptyGroup = new LinearLayout(this);
        emptyGroup.addView(emptyView, ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(this, 24));
        HeaderAndFootWrapper wrapper = new HeaderAndFootWrapper(adapter);
        wrapper.addHeadView(emptyGroup);

        //初始化RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearItemDecoration itemDecoration = new LinearItemDecoration(this);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.follow_history_item_divider));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        //设置昵称、跟帖等级等等数据
        tvUserNickName.setText(userInfo.getNick_name());
        tvUserFollowLevel.setText(userInfo.getFollow_level());
        if (userInfo.isIs_man()) {
            ivUserSex.setImageResource(R.drawable.man);
        } else {
            ivUserSex.setImageResource(R.drawable.woman);
        }
        tvMyNoticeNumber.setText(userInfo.getMy_notice_sum());
        tvMyFansNumber.setText(userInfo.getMy_fans_sum());
        //更新头像
        updateAvatar();

        //设置折叠后的toolbar标题
        collapsToolbar.setTitle(userInfo.getNick_name());
    }

    private void updateAvatar() {
        String pathPrefix = ConfigManager.getInstance().getItemConfigValue("URL_BASE");
        String avatarPath = loginManager.getUserInfo().getAvatar_path();
        if (!TextUtils.isEmpty(avatarPath)) {
            ImageLoad.loadRoundImage(ivUserHead, pathPrefix + avatarPath);
        } else {
            Bitmap defaultAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
            ivUserHead.setImageBitmap(defaultAvatar);
        }
    }

    private void setListeners() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                doOnOffsetChanged(verticalOffset);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAvatarHelper = new AvatarHelper(this);
        mAvatarHelper.setOnCropImageOverListerner(new OnCropOverListener() {
            @Override
            public void onCropOver(int resultCode, Intent data) {
                mPresenter.updateAvatar(loginManager.getUserInfo().getUser_id(),
                        mAvatarHelper.getCropResultPath());
            }
        });
    }

    private void doOnOffsetChanged(int verticalOffset) {
        max_offset = getMaxOffset();

        int offset = Math.abs(verticalOffset);
        float alpha;
        if (offset < START_OFFSET) {
            alpha = 1;
        } else if (offset > END_OFFSET) {
            alpha = 0;
        } else {
            alpha = 1 - (offset - START_OFFSET) * 1.0f / (END_OFFSET - START_OFFSET);
        }

        float textAlpha = 0;
        //过1/3再开始显示标题
        int startOffset = END_OFFSET + (max_offset - END_OFFSET) / 3;
        if (alpha == 0 && offset > startOffset) {
            textAlpha = (offset - startOffset) * 1.0f / (max_offset - startOffset);
        }

        //跟随滑动设置透明度
        llUserInfo.setAlpha(alpha);
        cardView.setAlpha(alpha);

        //设置toolbar标题的显示及隐藏
        if (textAlpha != 0) {
            collapsToolbar.setTitleEnabled(true);
            setToolbarTitleAlpha(textAlpha);
        } else {
            collapsToolbar.setTitleEnabled(false);
        }
    }

    private int getMaxOffset() {
        if (max_offset == 0) {
            max_offset = (int) (appBar.getHeight() - (CommonUtil.getStatusBarHeight(this) +
                    getSystemDimension(android.R.attr.actionBarSize)));
        }
        return max_offset;
    }

    private void setToolbarTitleAlpha(float alpha) {
        collapsToolbar.setCollapsedTitleTextColor(Color.argb((int) (255 * alpha), 0xff, 0xff, 0xff));
        collapsToolbar.setExpandedTitleColor(Color.argb((int) (255 * alpha), 0xff, 0xff, 0xff));
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);

    private int getId(View view) {
        return rCaster.cast(view.getId());
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.iv_user_head, R2.id.ll_my_notice, R2.id.ll_my_fans})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.iv_user_head:
                mAvatarHelper.popupChangeAvatar();
                break;
            case R2.id.ll_my_notice:
                break;
            case R2.id.ll_my_fans:
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(UserCenterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAvatarHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AvatarHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onUpdateAvatarSuccess() {
        updateAvatar();
    }

}
