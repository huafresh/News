package hua.news.module_login.tools;

import android.os.Bundle;

/**
 * 第三方登录。调用相应平台的登录方法，如果授权成功，用户信息存储在{@link #mCurThirdLoginUserInfo}
 *
 * @author hua
 * @date 2017/6/23
 */

public class ThirdLoginManager {

    public static final String KEY_PLAT_TYPE = "key_plat_type";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ICON = "user_icon";

    //存储当前授权登录成功后的用户信息
    private static Bundle mCurThirdLoginUserInfo;

    //private ThirdLoginCallBack mCallBack;

    public static ThirdLoginManager getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ThirdLoginManager INSTANCE = new ThirdLoginManager();
    }

//    /**
//     * 新浪登录
//     */
//    public void sinaLogin(ThirdLoginCallBack callBack) {
//        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
//        login(callBack, platform);
//    }
//
//    /**
//     * qq登录
//     */
//    public void qqLogin(ThirdLoginCallBack callBack) {
//        Platform platform = ShareSDK.getPlatform(QQ.NAME);
//        login(callBack, platform);
//    }
//
//    /**
//     * 微信登录
//     */
//    public void weiXinLogin(ThirdLoginCallBack callBack) {
//        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//        login(callBack, platform);
//    }
//
//    private void login(ThirdLoginCallBack callBack, Platform platform) {
//        mCallBack = callBack;
//        platform.setPlatformActionListener(new CommonPlatformActionListener());
//        platform.showUser(null);
//    }
//
//    private void loginSuccess(ThirdLoginCallBack callBack, Platform platform) {
//        Bundle bundle = new Bundle();
//        bundle.putString(KEY_PLAT_TYPE, platform.getName());
//        bundle.putString(KEY_USER_ID, platform.getDb().getUserId());
//        bundle.putString(KEY_USER_NAME, platform.getDb().getUserName());
//        bundle.putString(KEY_USER_ICON, platform.getDb().getUserIcon());
//        if (callBack != null) {
//            callBack.onSuccess(bundle);
//        }
//        mCurThirdLoginUserInfo = bundle;
//    }
//
//    /**
//     * 第三方登录监听
//     */
//    public interface ThirdLoginCallBack {
//
//        /**
//         * 登录成功时调用
//         *
//         * @param userInfo 用户信息
//         */
//        void onSuccess(Bundle userInfo);
//
//        /**
//         * 登录失败时调用
//         *
//         * @param error 失败信息
//         */
//        void onError(String error);
//    }
//
//    private class CommonPlatformActionListener implements PlatformActionListener {
//
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            loginSuccess(mCallBack, platform);
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            if (mCallBack != null) {
//                mCallBack.onError(throwable.getMessage());
//            }
//            mCurThirdLoginUserInfo = null;
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            if (mCallBack != null) {
//                mCallBack.onError("授权失败");
//            }
//            mCurThirdLoginUserInfo = null;
//        }
//    }

    /**
     * 获取当前授权登录的用户信息
     *
     * @return 用户信息
     */
    public Bundle getCurLoginUserInfo() {
        return mCurThirdLoginUserInfo;
    }
}
