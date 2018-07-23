package com.example.hua.framework.network;

import io.reactivex.Observable;
import okhttp3.Request;

/**
 * http请求
 *
 * @author hua
 * @version 2018/3/20 11:24
 */

public interface IHttpRequest {

//    /**
//     * 异步发起网络请求。
//     *
//     * @param request  请求参数
//     * @param callBack 响应回调
//     */
//    void request(Request request, HttpCallBack<?> callBack);

//    /**
//     * 同步发起网络请求。
//     *
//     * @param request 请求参数
//     * @param <T>     返回结果类型
//     * @return 返回结果，会自动解析
//     */
//    <T> T request(Request request);

    /**
     * 创建请求接口实例。参考Retrofit2.0
     *
     * @param api 定义的接口类型
     * @return T     接口实例
     */
    <T> T create(Class<T> api);

    <T> T create(String baseUrl, Class<T> api);

}
