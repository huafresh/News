package com.example.hua.framework.mvpbase;

import android.content.Context;
import android.os.Bundle;

/**
 * 通用回调
 *
 * @author hua
 * @date 2017/6/10
 * @deprecated 后续网络请求使用lifecycle + rx，拒绝使用回调
 */
@Deprecated
public interface CallBack<T> {

    String ERROR_NO = "error_no";
    String ERROR_INFO = "error_info";

    /**
     * 请求成功时调用
     *
     * @param context Context
     * @param data    数据
     */
    void onSuccess(Context context, T data);

    /**
     * 请求失败时调用
     *
     * @param context Context
     * @param error   错误信息
     */
    void onError(Context context, Bundle error);

}
