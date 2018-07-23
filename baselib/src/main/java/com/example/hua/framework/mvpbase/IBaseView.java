package com.example.hua.framework.mvpbase;

/**
 * view层基类接口，所有view层都要继承此接口
 *
 * @author hua
 * @date 2017/6/5
 */
public interface IBaseView<T> {
    void setPresenter(T presenter);
}
