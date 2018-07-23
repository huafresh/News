package hua.news.module_main.runalone;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_main.R;

/**
 * @author hua
 * @version 2018/3/29 18:38
 */
@Route(path = RouterConstant.MODULE_LOGIN_MANAGER, priority = 1)
public class TestLoginManager implements ILoginManager {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void openAccountLogin(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.developing_notice),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openPhoneRegister(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.developing_notice),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openMailRegister(Activity activity) {
        Toast.makeText(activity, activity.getResources().getString(R.string.developing_notice),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openVerifyPhone(Context context, String mail, String phone) {
        Toast.makeText(context, context.getResources().getString(R.string.developing_notice),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public LoginUserInfo getUserInfo() {
        return null;
    }

    @Override
    public void clearUserInfo() {

    }

    @Override
    public void init(Context context) {

    }
}
