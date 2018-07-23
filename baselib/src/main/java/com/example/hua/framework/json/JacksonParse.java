package com.example.hua.framework.json;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson框架实现json解析
 *
 * @author hua
 * @version 2018/3/20 10:34
 */

public class JacksonParse implements IJsonParse {
    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeFactory typeFactory = TypeFactory.defaultInstance();


    @Override
    public <T> List<T> parseJsonToList(String json, Class<T> cls) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        List list = null;
        try {
            list = objectMapper.readValue(json,
                    typeFactory.constructCollectionType(ArrayList.class, cls));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public <T> T parseJsonToObject(String json, Class<T> model) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        T t = null;
        try {
            t = objectMapper.readValue(json, model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }


    @Override
    public String parseObjectToString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
