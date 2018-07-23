package com.example.hua.framework.json;

import android.support.annotation.NonNull;

/**
 * 工厂接口，决定使用哪个json框架
 *
 * @author hua
 * @version 2018/3/20 10:45
 */

public interface IJsonParseFactory {
    /**
     * 获取json解析实现类
     * @return IJsonParse
     */
    @NonNull
    IJsonParse getJsonParse();
}
