package com.example.hua.framework.network.adapters;

import android.text.TextUtils;

import com.example.hua.framework.json.JsonParseUtil;
import com.example.hua.framework.network.NetResultErrorException;
import com.example.hua.framework.network.NetworkConstant;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author hua
 * @version 2018/3/22 16:42
 */

@SuppressWarnings("ALL")
public class ListConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof ParameterizedType) {
            //处理形如List<Object>的类型
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() instanceof List) {
                return new ListConverterAdapter(parameterizedType.getActualTypeArguments()[0].getClass());
            } else {
                return null;
            }
        }
        return new ListConverterAdapter(type.getClass());
    }

    private class ListConverterAdapter implements Converter<ResponseBody, Object> {
        private Class<?> model;

        public ListConverterAdapter(Class<?> model) {
            this.model = model;
        }

        @Override
        public Object convert(ResponseBody value) throws IOException {
            try {
                String result = value.string();

//                int error_no = -1;
//                String error_info = "";
//
//                if (jsonObject.has(NetworkConstant.ERROR_NO)) {
//                    error_no = jsonObject.optInt(NetworkConstant.ERROR_NO);
//                    if (error_no == 0) {
//                        if (jsonObject.has(NetworkConstant.RESULTS)) {
//                            JSONArray jsonArray = jsonObject.optJSONArray(NetworkConstant.RESULTS);
//                            List<T> list = JsonParseUtil.getInstance().parseJsonToList(jsonArray, model);
//                            if (list != null) {
//                                return list;
//                            } else {
//                                error_info = "json数据解析出错";
//                            }
//                        }
//                    }
//                }
//
//                //运行到这里说明出现问题
//                if (TextUtils.isEmpty(error_info)) {
//                    if (jsonObject.has(NetworkConstant.ERROR_INFO)) {
//                        error_info = jsonObject.optString(NetworkConstant.ERROR_INFO);
//                    } else {
//                        if (TextUtils.isEmpty(error_info)) {
//                            error_info = "未知错误";
//                        }
//                    }
//                }
//                throw new NetResultErrorException(error_info, error_no);

                return JsonParseUtil.getInstance().parseJsonToList(result, model);
            } finally {
                value.close();
            }
        }
    }

}
