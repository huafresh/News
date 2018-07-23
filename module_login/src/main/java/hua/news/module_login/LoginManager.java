package hua.news.module_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.hua.framework.storage.StorageManager;
import com.example.hua.framework.utils.CommonUtil;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_service.login.ILoginManager;
import hua.news.module_login.pages.accountlogin.AccountLoginActivity;
import hua.news.module_login.pages.mailregister.MailRegisterActivity;
import hua.news.module_login.pages.verifyregister.VerifyPhoneActivity;
import hua.news.module_login.pages.phoneregister.PhoneRegisterActivity;
import hua.news.module_login.data.LoginConstant;

/**
 * 登录模块对外接口
 *
 * @author hua
 * @date 2017/6/27
 */
@Route(path = RouterConstant.MODULE_LOGIN_MANAGER)
public class LoginManager implements ILoginManager {

    public static LoginUserInfo sCurLoginUserInfo;

    @Override
    public void init(Context context) {

    }

    /**
     * 打开账号登录页面
     *
     * @param context
     */
    @Override
    public void openAccountLogin(Context context) {
        Intent intent = new Intent(context, AccountLoginActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        String account = StorageManager.getInstance(context).getFromDisk(LoginConstant.KEY_LAST_LOGIN_ACCOUNT);
        intent.putExtra(AccountLoginActivity.KEY_ACCOUNT, account);
        context.startActivity(intent);
    }

    /**
     * 打开手机号注册页面
     *
     * @param context
     */
    @Override
    public void openPhoneRegister(Context context) {
        Intent intent = new Intent(context, PhoneRegisterActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    /**
     * 打开邮箱注册页面
     *
     * @param activity
     */
    @Override
    public void openMailRegister(Activity activity) {
        Intent intent = new Intent(activity, MailRegisterActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 打开手机号校验页面
     *
     * @param context
     */
    @Override
    public void openVerifyPhone(Context context, String mail, String phone) {
        VerifyPhoneActivity.open(context, mail, phone);
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        return sCurLoginUserInfo != null;
    }

    @Override
    public LoginUserInfo getUserInfo(){
        return sCurLoginUserInfo;
    }

    @Override
    public void clearUserInfo() {
        sCurLoginUserInfo = null;
    }

    /**
     * 检查邮箱是否合法
     *
     * @param context
     * @param mail
     * @return
     */
    public static boolean checkMial(Context context, String mail) {

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(context, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Character.isLetter(mail.charAt(0))) {
            Toast.makeText(context, "邮箱须以字母开头", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mail.length() < 6) {
            Toast.makeText(context, "邮箱长度须大于等于6", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检查手机号是否合法
     *
     * @param context
     * @param phone
     * @return
     */
    public static boolean checkPhone(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonUtil.isPhone(phone)) {
            Toast.makeText(context, "手机号格式有误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检查密码是否合法
     *
     * @param context
     * @param password
     * @return
     */
    public static boolean checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, "密码长度不能小于6", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches("^(?!\\d+$)[0-9a-zA-Z]{6,}")) { //正则判断是否全为数字
            Toast.makeText(context, "密码过于简单，换一个吧", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检查短信验证码是否合法
     *
     * @param context
     * @param code
     * @return
     */
    public static boolean checkVerifyCode(Context context, String code) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (code.length() != 4) {
            Toast.makeText(context, "验证码格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
