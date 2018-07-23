package com.example.hua.framework.mvpbase;

/**
 * Created by hua on 2017/6/5.
 * presenter基类接口，所有presenter都要继承此接口
 */
public interface IBasePresenter<T> {

    /**
     * 建立关联
     *
     * @param view
     */
    void attachView(T view);

    /**
     * 获取view
     *
     * @return
     */
    T getView();

    /**
     * 判断是否与view建立了关联
     *
     * @return
     */
    boolean isViewAttached();

    /**
     * 解除关联
     */
    void detachView();
}
