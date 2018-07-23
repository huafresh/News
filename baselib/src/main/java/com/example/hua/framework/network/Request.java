package com.example.hua.framework.network;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 对应一个Http请求
 *
 * @author hua
 * @version 2018/4/3 17:27
 */

public class Request {
    private Retrofit retrofit;
    private OkHttpClient.Builder okHttpBuilder;
    private List<Interceptor> interceptors;
    private String baseUrl;

    public Request(Retrofit retrofit, OkHttpClient.Builder okHttpBuilder) {
        this.retrofit = retrofit;
        this.okHttpBuilder = okHttpBuilder;
        this.interceptors = new ArrayList<>();
    }

    public Request(Retrofit retrofit) {
        this(retrofit, null);
    }

    public Request addInterceptor(Interceptor interceptor) {
        interceptors.add( interceptor);
        return this;
    }

    public Request setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public <T> T create(Class<T> api) {
        if (okHttpBuilder == null) {
            okHttpBuilder = new OkHttpClient.Builder();
        }

        boolean needReBuild = false;

        for (Interceptor interceptor : interceptors) {
            okHttpBuilder.addInterceptor(interceptor);
            needReBuild = true;
        }

        if (TextUtils.isEmpty(baseUrl)) {
            needReBuild = true;
        }

        Retrofit retrofit;
        if (needReBuild) {
            retrofit = this.retrofit.newBuilder()
                    .client(okHttpBuilder.build())
                    .baseUrl(baseUrl)
                    .build();
        } else {
            retrofit = this.retrofit;
        }

        return retrofit.create(api);
    }

}
