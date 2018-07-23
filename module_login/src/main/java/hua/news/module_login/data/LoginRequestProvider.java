package hua.news.module_login.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.CallBack;
import com.example.hua.framework.storage.StorageManager;

import hua.news.module_common.base.BaseApplication;
import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_login.LoginManager;

import com.example.hua.framework.utils.MLog;

import java.util.Random;

/**
 * 登录请求接口的Provider形式的实现。即登录请求是通过ContentProvider模拟服务器实现的。
 * 如果有搭建服务器，只需另外实现接口{@link LoginRequest}即可。
 *
 * @author hua
 * @date 2017/6/28
 * @deprecated http已实现，放弃使用。
 */
@Deprecated
public class LoginRequestProvider implements LoginRequest {

    private final ContentResolver mResolver;

    //content provider authorities
    private static final String AUTHORITIES = "com.hua.newsprovider";
    private static final String CONTENT_URI = "content://" + AUTHORITIES;

    //各种请求接口
    private static final String PATH_USER_REGISTER = "/user/register";
    private static final String PATH_USER_LOGIN = "/user/login";
    private static final String PATH_USER_CHECK_MAIL = "/user/checkmail";
    private static final String PATH_USER_AUTO = "/user/auto";
    private static final String PATH_USER_INFO = "/user/info";
    private static final String PATH_USER_NICK_NAME = "/user/modify/nickname";
    private static final String PATH_ERROR_INFO = "/error/info";

    public LoginRequestProvider() {
        mResolver = FrameworkInitializer.getInstance().getContext().getContentResolver();
    }

    public static LoginRequestProvider getInstance() {
        return LoginRequestProvider.HOLDER.sInstance;
    }


    private static final class HOLDER {
        private static final LoginRequestProvider sInstance = new LoginRequestProvider();
    }

    @Override
    public void sendVerifyCode(String phone, CallBack callBack) {
        //// TODO: 2017/6/28 发送手机号验证码 ，这里先随机生成
        Random random = new Random();
        String verifyCode = "";
        for (int i = 0; i < 4; i++) {
            int code = random.nextInt(9);
            verifyCode += code;
        }
        onSuccess(callBack, verifyCode);
        StorageManager.getInstance(BaseApplication.getContext())
                .saveToMemory("verify_code", verifyCode);
    }

    public void checkverifycode(String code, CallBack callBack) {
        String saveCode = "";
        ///StorageManager.getInstance().getFromMemory("verify_code");
        if (saveCode.equals(code)) {
            onSuccess(callBack, null);
        } else {
            Bundle error = new Bundle();
            error.putString(CallBack.ERROR_INFO, "验证码错误");
            onFaild(callBack, error);
        }
    }

    @Override
    public void register(String mail, String phone, String pwd, CallBack callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_MAIL, LoginConstant.PARAMS_KEY_PHONE,
                LoginConstant.PARAMS_KEY_PASSWORD};
        String[] values = {mail, phone, pwd};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_REGISTER), keys, null, values, null);

        if (result != null) {
            LoginUserInfo userInfo = resolveLoginInfo(result);
            afterLoginSuccess(null, userInfo);
            onSuccess(callBack, resolveLoginInfo(result));
            result.close();
        } else {
            onFaild(callBack, getErrorInfo(PATH_USER_REGISTER));
        }
    }

    private Bundle getErrorInfo(String function) {
        Cursor cursor = mResolver.query(Uri.parse(CONTENT_URI + PATH_ERROR_INFO), null, function, null, null);
        String errorInfo = "";
        if (cursor != null && cursor.moveToFirst()) {
            errorInfo = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_ERROR_INFO));
            cursor.close();
        }
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(errorInfo)) {
            errorInfo = "未知异常";
        }
        bundle.putString(CallBack.ERROR_INFO, errorInfo);
        return bundle;
    }

    @Override
    public void login(String account, String pwd, String loginType, CallBack callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_ACCOUNT, LoginConstant.PARAMS_KEY_PASSWORD,
                LoginConstant.PARAMS_KEY_LOGIN_TYPE};
        String[] values = {account, pwd, loginType};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_LOGIN), keys, null, values, null);

        if (result != null) {
            LoginUserInfo loginUserInfo = resolveLoginInfo(result);
            afterLoginSuccess(account, loginUserInfo);
            onSuccess(callBack, loginUserInfo);
            result.close();
        } else {
            onFaild(callBack, getErrorInfo(PATH_USER_LOGIN));
        }
    }

    @Override
    public void CheckVerifyCode(String code, CallBack<?> callBack) {

    }

    @Override
    public void checkRegister(String account, String type, CallBack<?> callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_ACCOUNT};
        String[] values = {account};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_CHECK_MAIL), keys, null, values, null);

        if (result != null) {
            onSuccess(callBack, null);
            result.close();
        } else {
            onFaild(callBack, null);
        }
    }

    @Override
    public void modifyAvatar(String userId, String path, CallBack<?> callBack) {
        // TODO: 2017/7/14 实际这里是上传头像，这里只是把头像存储在了本地
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //FileUtil.saveBitmap(bitmap, FileUtil.getFileFromExternal(NewsConstant.AVATAR_LOCAL_PATH, true),90);
        onSuccess(callBack, null);
    }

    private void afterLoginSuccess(String account, LoginUserInfo loginUserInfo) {
        // TODO: 2017/7/11 编造临时假数据
        initLoginUserInfo(loginUserInfo);

        LoginManager.sCurLoginUserInfo = loginUserInfo;
        //保存userId用于自动登录
        //StorageManager.getInstance().saveToDisk(LoginConstant.KEY_USER_ID, loginUserInfo.getUserId());
        //保存最后一次登录成功的账号
        if (!TextUtils.isEmpty(account)) {
            StorageManager.getInstance(BaseApplication.getContext())
                    .saveToDisk(LoginConstant.KEY_LAST_LOGIN_ACCOUNT, account);
        }
    }

    private void initLoginUserInfo(LoginUserInfo loginUserInfo) {
//        loginUserInfo.setNickName(loginUserInfo.getMail());
//
//        loginUserInfo.setFollowLevel("跟帖局局长");
//        loginUserInfo.setGoldSum("666");
//        loginUserInfo.setMyNoticeSum("0");
//        loginUserInfo.setMyFansSum("0");
//        loginUserInfo.setMySubscribeSum("0");
//        loginUserInfo.setMyCollectSum("0");
//        loginUserInfo.setMyFollowSum("1");
//        loginUserInfo.setMyReadSum("10");
//
//        loginUserInfo.setFollowHistoryInfoList(new ArrayList<LoginUserInfo.FollowHistoryInfo>());
//
//        for (int i = 0; i < 20; i++) {
//            LoginUserInfo.FollowHistoryInfo historyInfo = new LoginUserInfo.FollowHistoryInfo();
//            historyInfo.setTitle("看见大海变成这样请立即上岸，一天内8人溺水4人死亡，边腐边升的彤则并被双开");
//            historyInfo.setContent("不作死就不会死");
//            historyInfo.setLikeSum("23");
//            historyInfo.setTime("一分钟前");
//            loginUserInfo.getFollowHistoryInfoList().add(historyInfo);
//        }

    }

    @Override
    public void autoLogin(String userId, CallBack callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_USER_ID};
        String[] values = {userId};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_AUTO), keys, null, values, null);

        if (result != null) {
            LoginUserInfo userInfo = resolveLoginInfo(result);
            afterLoginSuccess(null, userInfo);
            onSuccess(callBack, userInfo);
            result.close();
        } else {
            onFaild(callBack, getErrorInfo(PATH_USER_AUTO));
        }
    }

    @Override
    public void modifyNickName(String userId, String nickName, CallBack callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_USER_ID, LoginConstant.PARAMS_KEY_NICK_NAME};
        String[] values = {userId, nickName};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_NICK_NAME), keys, null, values, null);

        if (result != null) {
            LoginUserInfo userInfo = resolveLoginInfo(result);
            afterLoginSuccess(null, userInfo);
            onSuccess(callBack, resolveLoginInfo(result));
            result.close();
        } else {
            onFaild(callBack, getErrorInfo(PATH_USER_NICK_NAME));
        }
    }

    @Override
    public void requestUserInfo(String userId, CallBack callBack) {
        String[] keys = {LoginConstant.PARAMS_KEY_USER_ID};
        String[] values = {userId};
        Cursor result = mResolver.query(Uri.parse(CONTENT_URI + PATH_USER_INFO), keys, null, values, null);

        if (result != null) {
            LoginUserInfo userInfo = resolveLoginInfo(result);
            afterLoginSuccess(null, userInfo);
            onSuccess(callBack, resolveLoginInfo(result));
            result.close();
        } else {
            onFaild(callBack, getErrorInfo(PATH_USER_INFO));
        }
    }

    private void onSuccess(CallBack callBack, Object o) {
        if (callBack != null) {
            callBack.onSuccess(FrameworkInitializer.getInstance().getContext(), o);
        }
    }

    private void onFaild(CallBack callBack, Bundle error) {
        if (callBack != null) {
            callBack.onError(FrameworkInitializer.getInstance().getContext(), error);
        }
    }

    /**
     * 从cursor中解析得到loginUserInfo对象
     *
     * @param cursor
     * @return
     */
    private LoginUserInfo resolveLoginInfo(Cursor cursor) {
        LoginUserInfo userInfo = new LoginUserInfo();

        //只取第一个，一般情况也只会有一个
        if (cursor.moveToFirst()) {
            try {
                String userId = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_USER_ID));
                String nickName = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_NICK_NAME));
                String bindPhone = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_BIND_PHONE));
                String mail = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_MAIL));
                String thirdPlat = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_THIRD_PLAT));
                String thirdUniqueid = cursor.getString(cursor.getColumnIndex(LoginConstant.COLUMN_THIRD_UNIQUE_ID));
//
//                userInfo.setUserId(userId);
//                //userInfo.setBindPhone(bindPhone);
//                userInfo.setMail(mail);
//                userInfo.setThirdPlat(thirdPlat);
//                userInfo.setThirdUniqueId(thirdUniqueid);
//                userInfo.setNickName(nickName);

            } catch (Exception e) {
                MLog.e("解析cursor里的用户信息失败，可能是列名不正确");
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return userInfo;
    }

}
