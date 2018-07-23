package hua.news.module_setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.example.hua.framework.utils.RCaster;

import hua.news.module_setting.pages.usersetting.UserSettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_common.base.BaseActivity;
import hua.news.module_service.login.ILoginManager;

/**
 * Created by hua on 2017/7/11.
 * 设置页面
 */

public class SettingHomeActivity extends BaseActivity {

    @BindView(R2.id.tv_setting_user)
    TextView tvSettingUser;
    @BindView(R2.id.tv_setting_third_plat)
    TextView tvSettingThirdPlat;
    @BindView(R2.id.tv_font)
    TextView tvFont;
    @BindView(R2.id.rl_setting_font)
    RelativeLayout rlSettingFont;
    @BindView(R2.id.rl_setting_read_preference)
    RelativeLayout rlSettingReadPreference;
    @BindView(R2.id.tv_font_size)
    TextView tvFontSize;
    @BindView(R2.id.rl_setting_font_size)
    RelativeLayout rlSettingFontSize;
    @BindView(R2.id.rl_setting_push_main)
    RelativeLayout rlSettingPushMain;
    @BindView(R2.id.rl_setting_push_follow)
    RelativeLayout rlSettingPushFollow;
    @BindView(R2.id.rl_setting_wifi)
    RelativeLayout rlSettingWifi;
    @BindView(R2.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R2.id.rl_setting_clear_cache)
    RelativeLayout rlSettingClearCache;
    @BindView(R2.id.rl_setting_scan)
    RelativeLayout rlSettingScan;
    @BindView(R2.id.tv_setting_about)
    TextView tvSettingAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_setting);

        ButterKnife.bind(this);

        initViews();
    }

    @Autowired
    ILoginManager loginManager;

    private void initViews() {
        setTitle(getResources().getString(R.string.setting_title));
    }

    private RCaster rCaster = new RCaster(R.class, R2.class);

    private int getId(View view) {
        return rCaster.cast(view.getId());
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick({R2.id.tv_setting_user, R2.id.tv_setting_third_plat, R2.id.rl_setting_font,
            R2.id.rl_setting_read_preference, R2.id.rl_setting_font_size, R2.id.rl_setting_push_main,
            R2.id.rl_setting_push_follow, R2.id.rl_setting_wifi, R2.id.tv_cache_size,
            R2.id.rl_setting_clear_cache, R2.id.rl_setting_scan, R2.id.tv_setting_about})
    public void onViewClicked(View view) {
        switch (getId(view)) {
            case R2.id.tv_setting_user:
                if (loginManager != null) {
                    if (!loginManager.isLogin()) {
                        loginManager.openAccountLogin(this);
                    } else {
                        Intent intent = new Intent(this, UserSettingActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R2.id.tv_setting_third_plat:
                break;
            case R2.id.rl_setting_font:
                break;
            case R2.id.rl_setting_read_preference:
                break;
            case R2.id.rl_setting_font_size:
                break;
            case R2.id.rl_setting_push_main:
                break;
            case R2.id.rl_setting_push_follow:
                break;
            case R2.id.rl_setting_wifi:
                break;
            case R2.id.tv_cache_size:
                break;
            case R2.id.rl_setting_clear_cache:
                break;
            case R2.id.rl_setting_scan:
                break;
            case R2.id.tv_setting_about:
                break;
        }
    }
}
