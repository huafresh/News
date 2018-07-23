package com.example.hua.framework.network.interceptors;

import android.support.annotation.NonNull;

import com.example.hua.framework.utils.MLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 拦截网络请求，打印日志
 *
 * @author hua
 * @date 2017/8/11
 */

public class LogginInterceptor implements Interceptor {

    public static final String TAG = "@@@hua";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //这里拿到的request是下一个拦截器将要处理的，我们可以拿出来做做手脚
        //然后丢进链条中再执行它的proceed方法
        //根据上面的分析，proceed方法内部会依次执行底层的拦截器，嵌套下去
        //最后获取到response后又会嵌套返回，我们此时又可以对返回的response动动手脚再返回给上层
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        printRequestInfo(request.url().url().toString().replace("%2f","/"), responseBody.string());
        return response;
    }

    private void printRequestInfo(String url, String json) {
        MLog.d(TAG, "网络请求，url: " + url);
        MLog.d(TAG, "网络请求返回: ");
        MLog.json(TAG, json);
    }

}
