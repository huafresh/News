package com.example.hua.framework.network;

import com.example.hua.framework.network.interceptors.LogginInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 网络请求帮助类
 *
 * @author hua
 * @date 2017/8/11
 */
@SuppressWarnings("ALL")
@Deprecated
public class NetworkHelper {

    /**
     * 创建一个通用的Retrofit对象
     *
     * @param baseUrl
     * @return
     */
    public static Retrofit getRetrofit(String baseUrl) {
        OkHttpClient mClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LogginInterceptor())
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }

}
