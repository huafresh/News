package com.example.hua.framework.network;

import com.example.hua.framework.network.interceptors.LogginInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * 网络请求统一入口。
 * 以支持后续底层网络请求框架的切换。
 * 默认使用retrofit+okhttp作为底层网络请求框架
 *
 * @author hua
 * @version 2018/3/20 11:28
 */
public class HttpRequest {

    private IHttpFactory mHttpFactory;
    private IHttpRequest mHttpRequest;

    Retrofit retrofit;
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    public static HttpRequest getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final HttpRequest sInstance = new HttpRequest();
    }

    private HttpRequest() {
        mHttpFactory = new DefaultHttpFactory();
        mHttpRequest = mHttpFactory.getHttpRequest();
    }

    private class DefaultHttpFactory implements IHttpFactory {
        @Override
        public IHttpRequest getHttpRequest() {
            return HttpRequestImpl.getInstance();
        }
    }

    /**
     * 修改网络请求底层实现
     *
     * @param factory {@link IHttpFactory}
     */
    public void setHttpRequestFactory(IHttpFactory factory) {
        if (factory != null) {
            mHttpFactory = factory;
            mHttpRequest = factory.getHttpRequest();
            if (mHttpRequest == null) {
                throw new IllegalArgumentException("getHttpRequest() method can not return null");
            }
        }
    }

    public Request newApiService() {
        return new Request(retrofit, clientBuilder);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * 用于配置全局的http请求参数
     */
    public final static class Builder {
        private String baseUrl;
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<CallAdapter.Factory> callAdapters = new ArrayList<>();
        private List<Converter.Factory> converters = new ArrayList<>();


        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public Builder addConverterFactory(Converter.Factory converter) {
            converters.add(converter);
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory callAdapter) {
            callAdapters.add(callAdapter);
            return this;
        }


        public void build() {
            OkHttpClient.Builder clientBuild = HttpRequest.getInstance().clientBuilder;
            for (Interceptor interceptor : interceptors) {
                clientBuild.addInterceptor(interceptor);
            }

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientBuild.build());
            for (CallAdapter.Factory callAdapter : callAdapters) {
                builder.addCallAdapterFactory(callAdapter);
            }

            for (Converter.Factory converter : converters) {
                builder.addConverterFactory(converter);
            }

            HttpRequest.getInstance().retrofit = builder.build();
        }

    }

}
