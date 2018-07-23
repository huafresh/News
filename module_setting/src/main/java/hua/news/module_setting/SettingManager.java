package hua.news.module_setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.setting.ISettingManager;
import hua.news.module_setting.pages.usersetting.UserSettingActivity;

/**
 * @author hua
 * @version 2018/3/30 18:06
 */
@Route(path = RouterConstant.MODULE_SETTING_MANAGER)
public class SettingManager implements ISettingManager {
    @Override
    public void openSettingHome(Context context) {
        Intent intent = new Intent(context, SettingHomeActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void openUserSetting(Context context) {
        Intent intent = new Intent(context, UserSettingActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void init(Context context) {

    }
}
