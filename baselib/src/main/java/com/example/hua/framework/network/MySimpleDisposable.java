package com.example.hua.framework.network;


import android.text.TextUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * Rxjava订阅者接口封装。
 * 并且对错误异常进行了转义
 *
 * @author hua
 * @date 2017/8/11
 */
@Deprecated
public abstract class MySimpleDisposable<T> extends DisposableSubscriber<T> {
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 数据流执行成功时调用
     *
     * @param t 数据流类型
     */
    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable e) {
        if (e instanceof NetResultErrorException) {
            onFailed((NetResultErrorException) e);
        } else {
            String error_info = null;
            error_info = convertException(e);
            if (TextUtils.isEmpty(error_info)) {
                error_info = e.getClass().getSimpleName();
            }
            onFailed(new NetResultErrorException(error_info, -1024, e));
        }
    }

    private String convertException(Throwable throwable){
        String s = null;
        if(throwable instanceof SocketTimeoutException){
            s = "网络连接超时";
        } else if(throwable instanceof ConnectException){
            s = "网络异常";
        }
        return s;
    }

    /**
     * 数据流执行失败时调用
     *
     * @param e 失败异常
     */
    protected abstract void onFailed(NetResultErrorException e);

}
