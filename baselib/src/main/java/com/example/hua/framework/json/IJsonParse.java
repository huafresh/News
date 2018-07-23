package com.example.hua.framework.json;

import org.json.JSONArray;

import java.util.List;

/**
 * json解析接口
 *
 * @author hua
 * @version 2018/3/20 10:33
 */

public interface IJsonParse {
    /**
     * json字符串解析成list列表
     *
     * @param cls list集合元素的类型
     * @return list对象
     */
    <T> List<T> parseJsonToList(String json, Class<T> cls);

    /**
     * json字符串解析成bean对象
     *
     * @return bean对象
     */
    <T> T parseJsonToObject(String json, Class<T> model);

    /**
     * 对象转json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    String parseObjectToString(Object object);
}
