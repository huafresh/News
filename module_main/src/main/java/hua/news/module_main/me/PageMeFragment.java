package hua.news.module_main.me;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.divider.GridItemDecoration;
import com.example.hua.framework.divider.LinearItemDecoration;

import hua.news.module_common.utils.ConfigManager;

import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.wrapper.dialog.CommonDialog;
import com.example.hua.framework.wrapper.imageload.ImageLoad;

import hua.news.module_service.entitys.TitleIconEntity;

import hua.news.module_service.entitys.SettingCommonEntity;
import hua.news.module_main.R;
import hua.news.module_main.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hua.news.module_common.base.BaseFragment;
import hua.news.module_service.setting.ISettingManager;
import hua.news.module_service.login.ILoginManager;

/**
 * 我页面Tab。
 *
 * @author hua
 * @date 2017/6/4
 */
public class PageMeFragment extends BaseFragment implements PageMeContract.View {

    @BindView(R2.id.view_head_bac)
    View viewHeadBac;
    @BindView(R2.id.tv_setting_text)
    TextView tvSettingText;
    @BindView(R2.id.iv_setting_image)
    ImageView ivSettingImage;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.tv_phone_register)
    TextView tvPhoneRegister;
    @BindView(R2.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R2.id.tv_user_nick_name)
    TextView tvUserNickName;
    @BindView(R2.id.tv_user_follow_level)
    TextView tvUserFollowLevel;
    @BindView(R2.id.tv_user_gold_sum)
    TextView tvUserGoldSum;
    @BindView(R2.id.ll_sign_in)
    LinearLayout llSignIn;
    @BindView(R2.id.iv_third_wechat_image)
    ImageView ivThirdWechatImage;
    @BindView(R2.id.tv_third_wechat_text)
    TextView tvThirdWechatText;
    @BindView(R2.id.view_third_one)
    View viewThirdOne;
    @BindView(R2.id.iv_third_sina_image)
    ImageView ivThirdSinaImage;
    @BindView(R2.id.tv_third_sina_text)
    TextView tvThirdSinaText;
    @BindView(R2.id.view_third_two)
    View viewThirdTwo;
    @BindView(R2.id.iv_third_qq_image)
    ImageView ivThirdQqImage;
    @BindView(R2.id.tv_third_qq_text)
    TextView tvThirdQqText;
    @BindView(R2.id.my_notice_card)
    CardView myNoticeCard;
    @BindView(R2.id.my_data_card)
    CardView myDataCard;
    @BindView(R2.id.my_message_recycler)
    RecyclerView myMessageRecycler;
    @BindView(R2.id.my_money_recycler)
    RecyclerView myMoneyRecycler;
    @BindView(R2.id.my_other_recycler)
    RecyclerView myOtherRecycler;
    @BindView(R2.id.scroll_view)
    ScrollView scrollView;
    Unbinder unbinder;
    @BindView(R2.id.cl_login_container)
    ConstraintLayout clLoginContainer;
    @BindView(R2.id.cl_third_login)
    ConstraintLayout clThirdLogin;
    @BindView(R2.id.cl_user_info_container)
    ConstraintLayout clUserInfoContainer;
    @BindView(R2.id.my_notice_recycler)
    RecyclerView myNoticeRecycler;
    @BindView(R2.id.my_data_recycler)
    RecyclerView myDataRecycler;

    /**
     * 我页面item的字符串资源
     */
    @BindArray(R2.array.me_my_data)
    String[] meMyDatas;
    @BindArray(R2.array.me_my_notice)
    String[] meMyNotices;
    @BindArray(R2.array.me_messages)
    String[] meMessages;
    @BindArray(R2.array.me_moneys)
    String[] meMoneys;
    @BindArray(R2.array.me_others)
    String[] meOthers;

    /**
     * 我页面item的图片资源id
     */
    private int[] meMyDataIconIds = new int[]{R.drawable.subscribe, R.drawable.collect,
            R.drawable.follow_posts, R.drawable.read};
    private int[] meMessageIconIds = new int[]{R.drawable.me_my_message, R.drawable.dynamic};
    private int[] meMoneyIconIds = new int[]{R.drawable.gold_mall, R.drawable.gold_task,
            R.drawable.me_wallet};
    private int[] meOtherIconIds = new int[]{R.drawable.offline_read, R.drawable.day_and_night,
            R.drawable.square, R.drawable.feed_back};

    private CommonItemAdapter meMessageAdapter;
    private CommonItemAdapter meMoneyAdapter;
    private CommonItemAdapter meOtherAdapter;

    private PageMeContract.Presenter mPresenter;
    private CommonDialog mLoadingDialog;
    private MyDataAdapter meMyDataAdapter;
    private MyNoticeAdapter meMyNoticeAdapter;

    @Autowired
    ILoginManager loginManager;
    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new PageMePresenter());
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = LayoutInflater.from(mActivity).inflate(R.layout.page_me, container, false);
            mLoadingDialog = new CommonDialog(mActivity);
            ARouter.getInstance().inject(this);
        }
        unbinder = ButterKnife.bind(this, mView);
        //自动登录
        autoLogin();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //根据登录状态初始化页面的所有控件
        refreshMePage();
    }

    private void refreshMePage() {
        showRecyclerItem();
        if (loginManager.isLogin()) {
            login();
        } else {
            logout();
        }
    }

    private void autoLogin() {
//        String userId = StorageManager.getInstance().getFromDisk(LoginConstant.KEY_USER_ID);
//        if (!TextUtils.isEmpty(userId)) {
//            LoginRequestFactory.getInstance().autoLogin(userId, new CallBack() {
//                @Override
//                public void onSuccess(Context context, Object data) {
//                    MLog.d("自动登录成功");
//                    refreshMePage();
//                    // TODO: 2017/8/13 由于自动登录是异步的，所以我页面会先显示未登录的页面，然后这里
//                    // TODO: 2017/8/13 回调之后才变为登录的页面，会有闪烁的感觉，试了下弹dialog，发现不行 ，后续考虑解决方案
//                }
//
//                @Override
//                public void onError(Context context, Bundle error) {
//                    MLog.e("自动登录失败，原因是:" + error.getString(CallBack.ERROR_INFO));
//                }
//            });
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        unbinder.unbind();
    }


    private void openAccountLogin() {
        if (!loginManager.isLogin()) {
            loginManager.openAccountLogin(mActivity);
        } else {

        }
    }

    private void change() {
//        String mode = StorageManager.getInstance().getFromDisk(NewsConstant.KEY_NIGHT_MODE);
//        if (!TextUtils.isEmpty(mode)) {
//            if (mode.equals("day")) {
//                mode = "night";
//            } else if (mode.equals("night")) {
//                mode = "day";
//            }
//            StorageManager.getInstance().saveToDisk(NewsConstant.KEY_NIGHT_MODE, mode);
//            mActivity.recreate();
//        } else {
//            MLog.e("无法获取当前的模式状态，切换失败");
//        }
    }

    @Override
    public void setPresenter(PageMeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void logout() {
        //设置头部布局高度
        setHeadHeight(mActivity.getResources().getDimension(R.dimen.me_head_height));
        //显示登录入口
        clLoginContainer.setVisibility(View.VISIBLE);
        //隐藏用户信息
        clUserInfoContainer.setVisibility(View.GONE);

        meMyNoticeAdapter.notifyDataSetChanged();
        meMyDataAdapter.notifyDataSetChanged();
        // TODO: 2017/7/17 暂时放这里
        loginManager.clearUserInfo();
    }

    private void login() {
        //设置头部布局高度
        float deltaH = mActivity.getResources().getDimension(R.dimen.me_head_delta_height);
        setHeadHeight(mActivity.getResources().getDimension(R.dimen.me_head_height) - deltaH);
        //隐藏登录入口
        clLoginContainer.setVisibility(View.GONE);
        clThirdLogin.setVisibility(View.GONE);
        //展示并初始化用户信息
        clUserInfoContainer.setVisibility(View.VISIBLE);
        tvUserNickName.setText(loginManager.getUserInfo().getNick_name());
        tvUserFollowLevel.setText(loginManager.getUserInfo().getFollow_level());
        tvUserGoldSum.setText(loginManager.getUserInfo().getGold_sum());
        updateAvatar();
        meMyNoticeAdapter.notifyDataSetChanged();
        meMyDataAdapter.notifyDataSetChanged();
    }

    private void showRecyclerItem() {
        GridItemDecoration gridDecoration = new GridItemDecoration(mActivity);
        gridDecoration.setDividerContainPadding(GridItemDecoration.ORITATION_VERTICAL, true);

        //我的关注
        myNoticeRecycler.setLayoutManager(new GridLayoutManager(mActivity, 2));
        myNoticeRecycler.addItemDecoration(gridDecoration);
        meMyNoticeAdapter = new MyNoticeAdapter(mActivity);
        List<TitleIconEntity> myNoticeList = generateMyNoticeDataList();
        meMyNoticeAdapter.setDataList(myNoticeList);
        myNoticeRecycler.setAdapter(meMyNoticeAdapter);

        //我的数据
        myDataRecycler.setLayoutManager(new GridLayoutManager(mActivity, 4));
        myDataRecycler.addItemDecoration(gridDecoration);
        meMyDataAdapter = new MyDataAdapter(mActivity);
        List<TitleIconEntity> myDataList = generateMyDataDataList();
        meMyDataAdapter.setDataList(myDataList);
        myDataRecycler.setAdapter(meMyDataAdapter);

        LinearItemDecoration linearDecoration = new LinearItemDecoration(mActivity);

        //我的消息等
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        myMessageRecycler.addItemDecoration(linearDecoration);
        meMessageAdapter = new CommonItemAdapter(mActivity);
        List<SettingCommonEntity> messageDataList = generateMessageDataList();
        meMessageAdapter.setDataList(messageDataList);
        myMessageRecycler.setAdapter(meMessageAdapter);

        //金币商城等
        myMoneyRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        myMoneyRecycler.addItemDecoration(linearDecoration);
        meMoneyAdapter = new CommonItemAdapter(mActivity);
        List<SettingCommonEntity> moneyDataList = generateMoneyDataList();
        meMoneyAdapter.setDataList(moneyDataList);
        myMoneyRecycler.setAdapter(meMoneyAdapter);

        //离线阅读等
        myOtherRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        myOtherRecycler.addItemDecoration(linearDecoration);
        meOtherAdapter = new CommonItemAdapter(mActivity);
        List<SettingCommonEntity> otherDataList = generateOtherDataList();
        meOtherAdapter.setDataList(otherDataList);
        myOtherRecycler.setAdapter(meOtherAdapter);

        scrollView.requestDisallowInterceptTouchEvent(false);
    }

    private List<TitleIconEntity> generateMyNoticeDataList() {
        List<TitleIconEntity> list = new ArrayList<>();
        for (String title : meMyNotices) {
            TitleIconEntity entity = new TitleIconEntity();
            entity.setTitle(title);
            entity.setExtern1("0");
            list.add(entity);
        }
        return list;
    }

    private List<TitleIconEntity> generateMyDataDataList() {
        List<TitleIconEntity> list = new ArrayList<>();
        for (int i = 0; i < meMyDatas.length; i++) {
            String title = meMyDatas[i];
            TitleIconEntity entity = new TitleIconEntity();
            entity.setTitle(title);
            entity.setIcon(meMyDataIconIds[i]);
            entity.setExtern1("0");
            list.add(entity);
        }
        return list;
    }

    private List<SettingCommonEntity> generateOtherDataList() {
        List<SettingCommonEntity> list = new ArrayList<>();
        for (int i = 0; i < meOthers.length; i++) {
            SettingCommonEntity bean = new SettingCommonEntity();
            String title = meOthers[i];
            bean.setTitle(title);
            try {
                bean.setIcon(meOtherIconIds[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 2) { //离线阅读，写死的方式虽然不好，但也没办法了
                bean.setIs_has_switch(true);
                bean.setIs_switch_on(false);
            }
            list.add(bean);
        }
        return list;
    }

    private List<SettingCommonEntity> generateMoneyDataList() {
        List<SettingCommonEntity> list = new ArrayList<>();
        for (int i = 0; i < meMoneys.length; i++) {
            SettingCommonEntity bean = new SettingCommonEntity();
            bean.setTitle(meMoneys[i]);
            try {
                bean.setIcon(meMoneyIconIds[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(bean);
        }
        return list;
    }

    private List<SettingCommonEntity> generateMessageDataList() {
        List<SettingCommonEntity> list = new ArrayList<>();
        for (int i = 0; i < meMessages.length; i++) {
            SettingCommonEntity bean = new SettingCommonEntity();
            bean.setTitle(meMessages[i]);
            try {
                bean.setIcon(meMessageIconIds[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(bean);
        }
        return list;
    }

    private void updateAvatar() {
        String pathPrefix = ConfigManager.getInstance().getItemConfigValue("URL_BASE");
        String avatarPath = loginManager.getUserInfo() == null ? null
                : loginManager.getUserInfo().getAvatar_path();
        if (!TextUtils.isEmpty(avatarPath)) {
            ImageLoad.loadRoundImage(ivUserHead, pathPrefix + avatarPath);
        } else {
            Bitmap defaultAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
            ivUserHead.setImageBitmap(defaultAvatar);
        }
    }

    private void setHeadHeight(float newHeight) {
        ViewGroup.LayoutParams params = viewHeadBac.getLayoutParams();
        params.height = (int) newHeight;
        viewHeadBac.setLayoutParams(params);
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);

    private int getId(View view) {
        return rCaster.cast(view.getId());
    }

    @Autowired
    ISettingManager settingManager;

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.btn_login, R2.id.tv_phone_register, R2.id.iv_user_head, R2.id.tv_user_nick_name,
            R2.id.tv_user_follow_level, R2.id.tv_user_gold_sum, R2.id.ll_sign_in, R2.id.iv_third_wechat_image,
            R2.id.tv_third_wechat_text, R2.id.iv_third_sina_image, R2.id.tv_third_sina_text, R2.id.iv_third_qq_image,
            R2.id.tv_third_qq_text})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.btn_login:

                loginManager.openAccountLogin(mActivity);
                break;
            case R2.id.tv_phone_register:
                loginManager.openPhoneRegister(mActivity);
                break;
            case R2.id.iv_user_head:
            case R2.id.tv_user_nick_name:
                MeJumpTool.jumpUserCenter(mActivity);
                break;
            case R2.id.tv_user_follow_level:
                break;
            case R2.id.tv_user_gold_sum:
                break;
            case R2.id.ll_sign_in:
                break;
            case R2.id.iv_third_wechat_image:
                break;
            case R2.id.tv_third_wechat_text:
                break;
            case R2.id.iv_third_sina_image:
                break;
            case R2.id.tv_third_sina_text:
                break;
            case R2.id.iv_third_qq_image:
                break;
            case R2.id.tv_third_qq_text:
                break;
            case R2.id.tv_setting_text:
            case R2.id.iv_setting_image:
                if (settingManager != null) {
                    settingManager.openSettingHome(mActivity);
                } else {
                    Toast.makeText(mActivity, getString(R.string.developing_notice),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
