package com.example.hua.framework.wrapper.screen_adapt;

import android.view.View;

import java.util.List;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 * @deprecated 目测没必要
 */
@Deprecated
public interface ITypeCollector {

    /**
     * 判断是否可以处理给定类型的View
     */
    boolean canHandle(Class cls);

    /**
     * 对给定类型的view，收集属性
     */
    List<AttrType> collect(Class cls);

}
