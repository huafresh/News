package com.example.hua.framework.json;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * json解析工具，支持替换框架。默认使用Jackson
 *
 * @author hua
 * @date 2017/6/5
 */
public class JsonParseUtil {

    private IJsonParseFactory mFactory;
    private IJsonParse mJsonParse;

    public static JsonParseUtil getInstance() {
        return Holder.sInstance;
    }

    private static final class Holder {
        private static final JsonParseUtil sInstance = new JsonParseUtil();
    }

    private JsonParseUtil() {
        mFactory = new DefaultJsonParseFactory();
        mJsonParse = mFactory.getJsonParse();
    }

    private static class DefaultJsonParseFactory implements IJsonParseFactory {
        @NonNull
        @Override
        public IJsonParse getJsonParse() {
            return new JacksonParse();
        }
    }

    /**
     * 自定义解析框架
     *
     * @param factory IJsonParseFactory
     */
    public void setFactory(IJsonParseFactory factory) {
        if (factory != null) {
            mFactory = factory;
            mJsonParse = mFactory.getJsonParse();
            if (mJsonParse == null) {
                throw new IllegalArgumentException("getJsonParse() method can not return null");
            }
        }
    }

    /**
     * json字符串解析成list列表
     *
     * @param cls list集合元素的类型
     * @return list对象
     */
    public <T> List<T> parseJsonToList(String json, Class<T> cls) {
        return mJsonParse.parseJsonToList(json, cls);
    }

    /**
     * JSONArray解析成list列表
     *
     * @param cls list集合元素的类型
     * @return list对象
     */
    public <T> List<T> parseJsonToList(JSONArray jsonArray, Class<T> cls) {
        return mJsonParse.parseJsonToList(jsonArray.toString(), cls);
    }

    /**
     * json字符串解析成bean对象
     *
     * @return bean对象
     */
    public <T> T parseJsonToObject(String json, Class<T> model) {
        return mJsonParse.parseJsonToObject(json, model);
    }

    /**
     * 对象转json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    public String parseObjectToString(Object object) {
        return mJsonParse.parseObjectToString(object);
    }

}
