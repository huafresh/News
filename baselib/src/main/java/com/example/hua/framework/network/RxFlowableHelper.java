package com.example.hua.framework.network;

import android.text.TextUtils;

import com.example.hua.framework.json.JsonParseUtil;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.network.NetworkConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by hua on 2017/8/11.
 * rx数据流的处理过程中，有时候某些步骤会重复执行，可以使用此类简化
 */
@Deprecated
public class RxFlowableHelper {

    /**
     * 对服务器返回的原始数据进行简单的判断处理
     *
     * @param org 数据源
     * @return 处理后的Flowable
     */
    public static Flowable handleIsOk(Flowable<ResponseBody> org) {
        return org.subscribeOn(Schedulers.newThread())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        try {
                            return responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new NetResultErrorException("服务器异常", -1, e);
                        }
                    }
                }).map(new Function<String, JSONObject>() {
                    @Override
                    public JSONObject apply(String s) throws Exception {
                        try {
                            return new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new NetResultErrorException("服务器返回非json数据", -1, e);
                        }
                    }
                }).doOnNext(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject jsonObject) throws Exception {
                        if (jsonObject.has(NetworkConstant.ERROR_NO)) {
                            int error_no = jsonObject.optInt(NetworkConstant.ERROR_NO);
                            if (error_no != 0) {
                                String error_info = null;
                                if (jsonObject.has(NetworkConstant.ERROR_INFO)) {
                                    error_info = jsonObject.optString(NetworkConstant.ERROR_INFO);
                                } else {
                                    error_info = "未知服务器异常";
                                }
                                throw new NetResultErrorException(error_info, error_no);
                            }
                        } else {
                            throw new NetResultErrorException("服务器返回数据有误", -1);
                        }
                    }
                });
    }

    /**
     * 将服务器返回的原始数据流转换为Bean
     *
     * @param org   这一波数据流处理的源头
     * @param model bean模型
     * @return 处理后的Flowable
     */
    public static <T> Flowable<T> handleToBean(Flowable<ResponseBody> org, final Class<?> model) {
        return handlerToList(org, model)
                .map(new Function<List<?>, T>() {
                    @Override
                    public T apply(List<?> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            return (T) list.get(0);
                        }
                        throw new NetResultErrorException("解析结果集没有数据", -1);
                    }
                });
    }

    /**
     * 将服务器返回的原始数据流转换为List。
     * 处理流程：
     * 1、RequestBody -> String -> JSONObject -> List<Bean>
     *
     * @param org   数据源
     * @param model bean模型
     * @return 发射List数据的数据源
     */
    public static <T> Flowable<List<T>> handlerToList(Flowable<ResponseBody> org, final Class<T> model) {
        return org.subscribeOn(Schedulers.newThread()).map(new Function<ResponseBody, String>() {
            @Override
            public String apply(ResponseBody responseBody) throws Exception {
                try {
                    return responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new NetResultErrorException("服务器异常", -1, e);
                }
            }
        }).map(new Function<String, JSONObject>() {
            @Override
            public JSONObject apply(String s) throws Exception {
                try {
                    return new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new NetResultErrorException("服务器返回非json数据", -1, e);
                }
            }
        }).map(new Function<JSONObject, List<T>>() {
            @Override
            public List<T> apply(JSONObject jsonObject) throws Exception {
                int error_no = -1;
                String error_info = "";

                if (jsonObject.has(NetworkConstant.ERROR_NO)) {
                    error_no = jsonObject.optInt(NetworkConstant.ERROR_NO);
                    if (error_no == 0) {
                        if (jsonObject.has(NetworkConstant.RESULTS)) {
                            JSONArray jsonArray = jsonObject.optJSONArray(NetworkConstant.RESULTS);
                            List<T> list = JsonParseUtil.getInstance().parseJsonToList(jsonArray, model);
                            if (list != null) {
                                return list;
                            } else {
                                error_info = "json数据解析出错";
                            }
                        }
                    }
                }

                //运行到这里说明出现问题
                if (TextUtils.isEmpty(error_info)) {
                    if (jsonObject.has(NetworkConstant.ERROR_INFO)) {
                        error_info = jsonObject.optString(NetworkConstant.ERROR_INFO);
                    } else {
                        if (TextUtils.isEmpty(error_info)) {
                            error_info = "未知错误";
                        }
                    }
                }
                throw new NetResultErrorException(error_info, error_no);
            }
        });
    }


}
