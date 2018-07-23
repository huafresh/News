package hua.news.module_login.tools;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hua.framework.mvpbase.CallBack;

import hua.news.module_login.data.LoginRequestFactory;

/**
 * 检查邮箱或者手机号是否注册。
 * 通过{@link OnSuccessListener}监听都没有注册时的回调
 *
 * @author hua
 * @date 2017/8/9
 */

public class CheckRegisterModel {

    public void checkRegister(String mail, String phone, final OnSuccessListener listener){
        LoginRequestFactory.getInstance().checkRegister(phone, "201", new CallBack<Object>() {
            @Override
            public void onSuccess(Context context, Object data) {
                Toast.makeText(context,"该手机号已经注册，请直接登录",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (listener != null) {
                    listener.onSuccess(context);
                }
            }
        });

        LoginRequestFactory.getInstance().checkRegister(mail, "202", new CallBack<Object>() {
            @Override
            public void onSuccess(Context context, Object data) {
                Toast.makeText(context,"该邮箱已经注册，请直接登录",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Context context, Bundle error) {
                if (listener != null) {
                    listener.onSuccess(context);
                }
            }
        });
    }

    public interface OnSuccessListener{
        /**
         * 邮箱和手机号都没有注册时调用
         * @param context Context
         */
        void onSuccess(Context context);
    }

}
