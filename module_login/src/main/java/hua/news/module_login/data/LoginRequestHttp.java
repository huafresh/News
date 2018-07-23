package hua.news.module_login.data;

import android.os.Bundle;

import hua.news.module_common.FrameworkInitializer;

import com.example.hua.framework.mvpbase.CallBack;

import hua.news.module_common.base.BaseApplication;
import hua.news.module_common.utils.ConfigManager;

import com.example.hua.framework.storage.StorageManager;
import com.example.hua.framework.network.MySimpleDisposable;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.network.NetworkHelper;
import com.example.hua.framework.network.RxFlowableHelper;
import com.example.hua.framework.utils.FileUtil;

import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_login.LoginManager;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;


/**
 * 登录请求的http实现。
 * 这一层，是对登录请求的处理
 *
 * @author hua
 * @date 2017/8/5
 */

public class LoginRequestHttp implements LoginRequest {

    private LoginRequestProvider mLoginProvider;
    private final Retrofit mRetrofit;
    private final LoginRequestApi loginRequestApi;

    public LoginRequestHttp() {
        mLoginProvider = LoginRequestProvider.getInstance();
        mRetrofit = NetworkHelper.getRetrofit(LoginConstant.URL_BASE);
        loginRequestApi = mRetrofit.create(LoginRequestApi.class);
        Annotation a;
    }

    public static LoginRequestHttp getInstance() {
        return LoginRequestHttp.HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final LoginRequestHttp sInstance = new LoginRequestHttp();
    }


    @Override
    public void sendVerifyCode(String phone, CallBack<?> callBack) {
        mLoginProvider.sendVerifyCode(phone, callBack);
    }

    @Override
    public void CheckVerifyCode(String code, CallBack<?> callBack) {
        mLoginProvider.CheckVerifyCode(code, callBack);
    }


    @Override
    public void register(final String mail, String phone, String pwd, final CallBack<?> callBack) {
        String function = ConfigManager.getInstance().getItemConfigValue("URL_REGISTER");
        Flowable<ResponseBody> flowable1 = loginRequestApi.register(function, mail, phone, pwd);
        Flowable<LoginUserInfo> flowable2 = RxFlowableHelper.handleToBean(flowable1, LoginUserInfo.class);
        flowable2.doOnNext(new Consumer<LoginUserInfo>() {
            @Override
            public void accept(LoginUserInfo loginUserInfo) throws Exception {
                afterLogin(loginUserInfo);
                StorageManager.getInstance(BaseApplication.getContext())
                        .saveToDisk(LoginConstant.KEY_LAST_LOGIN_ACCOUNT, mail);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleDisposable<LoginUserInfo>() {
                    @Override
                    protected void onSuccess(LoginUserInfo loginUserInfo) {
                        callBackSuccess(callBack, loginUserInfo);
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void login(final String account, String pwd, String loginType, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_LOGIN");
        loginRequestApi.login(suffix, account, pwd, loginType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new MySimpleDisposable<LoginUserInfo>() {
                    @Override
                    protected void onSuccess(LoginUserInfo loginUserInfo) {
                        afterLogin(loginUserInfo);
                        StorageManager.getInstance(BaseApplication.getContext())
                                .saveToDisk(LoginConstant.KEY_LAST_LOGIN_ACCOUNT, account);
                        callBackSuccess(callBack, loginUserInfo);
                    }

                    @Override
                    protected void onFailed(NetResultErrorException e) {
                        callBackFailed(callBack, e);
                    }
                });
    }

    @Override
    public void autoLogin(String userId, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_USER_INFO");
        Flowable<ResponseBody> f1 = loginRequestApi.requestUserInfo(suffix, userId);
        Flowable<LoginUserInfo> f2 = RxFlowableHelper.handleToBean(f1, LoginUserInfo.class);
        f2.doOnNext(new Consumer<LoginUserInfo>() {
            @Override
            public void accept(LoginUserInfo loginUserInfo) throws Exception {
                afterLogin(loginUserInfo);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<LoginUserInfo>() {
            @Override
            protected void onSuccess(LoginUserInfo loginUserInfo) {
                callBackSuccess(callBack, loginUserInfo);
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                callBackFailed(callBack, e);
            }
        });
    }

    @Override
    public void requestUserInfo(String userId, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_USER_INFO");
        Flowable<ResponseBody> f1 = loginRequestApi.requestUserInfo(suffix, userId);
        Flowable<LoginUserInfo> f2 = RxFlowableHelper.handleToBean(f1, LoginUserInfo.class);
        f2.observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<LoginUserInfo>() {
            @Override
            protected void onSuccess(LoginUserInfo loginUserInfo) {
                callBackSuccess(callBack, null);
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                callBackFailed(callBack, e);
            }
        });
    }

    @Override
    public void checkRegister(String account, String type, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_CHECK_REGISTER");
        Flowable<ResponseBody> f1 = loginRequestApi.checkRegister(suffix, account, type);
        Flowable<LoginUserInfo> f2 = RxFlowableHelper.handleToBean(f1, LoginUserInfo.class);
        f2.observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<LoginUserInfo>() {
            @Override
            protected void onSuccess(LoginUserInfo loginUserInfo) {
                callBackSuccess(callBack, null);
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                callBackFailed(callBack, e);
            }
        });
    }

    @Override
    public void modifyNickName(String userId, String nickName, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_MODIFY_NICK_NAME");
        Flowable<ResponseBody> f1 = loginRequestApi.modifyNickName(suffix, userId, nickName);
        Flowable<LoginUserInfo> f2 = RxFlowableHelper.handleToBean(f1, LoginUserInfo.class);
        f2.observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<LoginUserInfo>() {
            @Override
            protected void onSuccess(LoginUserInfo loginUserInfo) {
                LoginManager.sCurLoginUserInfo = loginUserInfo;
                callBackSuccess(callBack, loginUserInfo);
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                callBackFailed(callBack, e);
            }
        });
    }

    @Override
    public void modifyAvatar(String userId, String path, final CallBack<?> callBack) {
        String suffix = ConfigManager.getInstance().getItemConfigValue("URL_MODIFY_AVATAR");
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), new File(path));
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("", userId + "_avatar." + FileUtil.getFileNameExt(path), body)
                .build();
        Flowable<ResponseBody> f1 = loginRequestApi.modifyAvatar(suffix, userId, multipartBody);
        Flowable<LoginUserInfo> f2 = RxFlowableHelper.handleToBean(f1, LoginUserInfo.class);
        f2.observeOn(AndroidSchedulers.mainThread()).subscribe(new MySimpleDisposable<LoginUserInfo>() {
            @Override
            protected void onSuccess(LoginUserInfo loginUserInfo) {
                LoginManager.sCurLoginUserInfo = loginUserInfo;
                callBackSuccess(callBack, loginUserInfo);
            }

            @Override
            protected void onFailed(NetResultErrorException e) {
                callBackFailed(callBack, e);
            }
        });
    }

    private void callBackFailed(CallBack<?> callBack, NetResultErrorException e) {
        if (callBack != null) {
            Bundle error = new Bundle();
            error.putString(CallBack.ERROR_INFO, e.getError_info());
            callBack.onError(FrameworkInitializer.getInstance().getContext(), error);
        }
    }

    private void callBackSuccess(CallBack callBack, Object o) {
        if (callBack != null) {
            callBack.onSuccess(FrameworkInitializer.getInstance().getContext(), o);
        }
    }

    private void afterLogin(LoginUserInfo loginUserInfo) {
        // TODO: 2017/7/11 编造临时假数据
        initLoginUserInfo(loginUserInfo);

        //保存userId用于自动登录
        StorageManager.getInstance(BaseApplication.getContext())
                .saveToDisk(LoginConstant.KEY_USER_ID,
                        loginUserInfo.getUser_id());

        LoginManager.sCurLoginUserInfo = loginUserInfo;

    }

    private void initLoginUserInfo(LoginUserInfo loginUserInfo) {
        loginUserInfo.setNick_name(loginUserInfo.getMail());

        loginUserInfo.setFollow_level("跟帖局局长");
        loginUserInfo.setGold_sum("666");
        loginUserInfo.setMy_notice_sum("0");
        loginUserInfo.setMy_fans_sum("0");
        loginUserInfo.setMy_subscribe_sum("0");
        loginUserInfo.setMy_collect_sum("0");
        loginUserInfo.setMy_follow_sum("1");
        loginUserInfo.setMy_read_sum("10");

        loginUserInfo.setFollow_history_info_list(new ArrayList<LoginUserInfo.FollowHistoryInfo>());

        for (int i = 0; i < 20; i++) {
            LoginUserInfo.FollowHistoryInfo historyInfo = new LoginUserInfo.FollowHistoryInfo();
            historyInfo.setTitle("看见大海变成这样请立即上岸，一天内8人溺水4人死亡，边腐边升的彤则并被双开");
            historyInfo.setContent("不作死就不会死");
            historyInfo.setLike_sum("23");
            historyInfo.setTime("一分钟前");
            loginUserInfo.getFollow_history_info_list().add(historyInfo);
        }

    }

}
