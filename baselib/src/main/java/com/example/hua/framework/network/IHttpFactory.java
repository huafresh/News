package com.example.hua.framework.network;

/**
 * @author hua
 * @version 2018/3/20 13:53
 */

public interface IHttpFactory {
    /**
     * 提供网络请求具体实现
     *
     * @return IHttpRequest
     */
    IHttpRequest getHttpRequest();
}
