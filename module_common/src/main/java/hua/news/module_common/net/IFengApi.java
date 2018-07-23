package hua.news.module_common.net;

import com.example.hua.framework.network.HttpRequest;
import com.example.hua.framework.network.interceptors.LogginInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import hua.news.module_common.constants.IFengConstant;
import io.reactivex.Flowable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author hua
 * @version 2018/4/3 15:57
 */

public class IFengApi {

    private HashMap<Class, Object> cacheService = new HashMap<>();

    /**
     * 公共参数
     */
    public static final Interceptor sQueryParameterInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request;
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("uid", "864678036850608")
                    .addQueryParameter("devid", "864678036850608")
                    .addQueryParameter("proid", "ifengnews")
                    .addQueryParameter("vt", "5")
                    .addQueryParameter("publishid", "6103")
                    .addQueryParameter("screen", "1080x1920")
                    .addQueryParameter("df", "androidphone")
                    .addQueryParameter("os", "android_22")
                    .addQueryParameter("nw", "wifi")
                    .build();
            request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        }
    };

    public <T> T createIFengService(Class<T> service) {
        Object o = cacheService.get(service);
        if (o == null) {
            Object newO = HttpRequest.getInstance().newApiService()
                    .addInterceptor(sQueryParameterInterceptor)
                    //后续这里优化，因为HttpRequest整体初始化时已经添加过了，但是顺序不对
                    .addInterceptor(new LogginInterceptor())
                    .setBaseUrl(IFengConstant.BASE_URL)
                    .create(service);
            cacheService.put(service,newO);
            o = newO;
        }
        return (T) o;
    }

    public static IFengApi getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final IFengApi sInstance = new IFengApi();
    }

}
