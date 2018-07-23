package hua.news.module_setting.pages.usersetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hua.news.module_common.utils.ConfigManager;
import com.example.hua.framework.tools.avatar.AvatarHelper;
import com.example.hua.framework.tools.avatar.OnCropOverListener;
import com.example.hua.framework.utils.RCaster;
import com.example.hua.framework.wrapper.imageload.ImageLoad;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_setting.R;
import hua.news.module_setting.R2;

/**
 * 个人设置页面
 *
 * @author hua
 * @date 2017/7/17
 */

public class UserSettingActivity extends BaseActivity implements UserSettingContract.View {
    @BindView(R2.id.tv_mail)
    TextView tvMail;
    @BindView(R2.id.rl_setting_avatar)
    RelativeLayout rlSettingAvatar;
    @BindView(R2.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R2.id.rl_setting_nick_name)
    RelativeLayout rlSettingNickName;
    @BindView(R2.id.tv_sex)
    TextView tvSex;
    @BindView(R2.id.rl_setting_sex)
    RelativeLayout rlSettingSex;
    @BindView(R2.id.tv_birth)
    TextView tvBirth;
    @BindView(R2.id.rl_setting_birth)
    RelativeLayout rlSettingBirth;
    @BindView(R2.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R2.id.rl_setting_device_name)
    RelativeLayout rlSettingDeviceName;
    @BindView(R2.id.rl_setting_hide_name)
    RelativeLayout rlSettingHideName;
    @BindView(R2.id.tv_level)
    TextView tvLevel;
    @BindView(R2.id.rl_setting_level)
    RelativeLayout rlSettingLevel;
    @BindView(R2.id.btn_logout)
    Button btnLogout;
    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    private UserSettingContract.Presenter mPresenter;
    private AvatarHelper mAvatarHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_user_setting);

        ButterKnife.bind(this);
        setPresenter(new UserSettingPresenter());
        mPresenter.attachView(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private ILoginManager loginManager;

    private void initView() {
        setTitle(getResources().getString(R.string.setting_user_setting));

        mAvatarHelper = new AvatarHelper(this);
        mAvatarHelper.setOnCropImageOverListerner(new OnCropOverListener() {
            @Override
            public void onCropOver(int resultCode, Intent data) {
                mPresenter.updateAvatar(loginManager.getUserInfo().getUser_id(),
                        mAvatarHelper.getCropResultPath());
            }
        });

        initUserInfos();
    }

    private void initUserInfos() {
        updateAvatar();

    }

    private void updateAvatar() {
        String pathPrefix = ConfigManager.getInstance().getItemConfigValue("URL_BASE");
        String avatarPath = loginManager.getUserInfo().getAvatar_path();
        if (!TextUtils.isEmpty(avatarPath)) {
            ImageLoad.loadRoundImage(ivAvatar, pathPrefix+avatarPath);
        } else {
            Bitmap defaultAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_head);
            ivAvatar.setImageBitmap(defaultAvatar);
        }
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);
    private int getId(View view){
        return rCaster.cast(view.getId());
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.rl_setting_avatar, R2.id.rl_setting_nick_name,
            R2.id.rl_setting_sex, R2.id.rl_setting_birth, R2.id.rl_setting_device_name,
            R2.id.rl_setting_hide_name, R2.id.rl_setting_level, R2.id.btn_logout})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.rl_setting_avatar:
                mAvatarHelper.popupChangeAvatar();
                break;
            case R2.id.rl_setting_nick_name:
                break;
            case R2.id.rl_setting_sex:
                break;
            case R2.id.rl_setting_birth:
                break;
            case R2.id.rl_setting_device_name:
                break;
            case R2.id.rl_setting_hide_name:
                break;
            case R2.id.rl_setting_level:
                break;
            case R2.id.btn_logout:
                // TODO: 2017/7/18 本来应该是广播全世界退出登录的，这里简单处理
                loginManager.clearUserInfo();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAvatarHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setPresenter(UserSettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUpdateAvatarSuccess() {
        updateAvatar();
    }

}
