package com.example.hua.framework.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * @author hua
 * @version 2018/3/20 14:21
 */

class HttpRequestImpl implements IHttpRequest {
    private Retrofit retrofit;

    public static HttpRequestImpl getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final HttpRequestImpl sInstance = new HttpRequestImpl();
    }

    public HttpRequestImpl() {
        //this.retrofit = HttpRequest.getInstance().retrofit;
    }

    @Override
    public <T> T create(Class<T> api) {
        return retrofit.create(api);
    }

    @Override
    public <T> T create(String baseUrl, Class<T> api) {
        return retrofit.newBuilder()
                .baseUrl(baseUrl)
                .build()
                .create(api);
    }

}
