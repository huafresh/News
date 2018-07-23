package com.example.hua.framework.mvpbase;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * presenter基类
 *
 * @author hua
 * @date 2017/6/5
 */
public abstract class BasePresenter<T> implements IBasePresenter<T> {

    /**
     * View接口类型的弱引用
     */
    protected Reference<T> mViewRef;

    /**
     * 建立关联
     *
     * @param view 要与本类建立关联的View对象
     */
    @Override
    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 获取view
     *
     * @return 获取到的View对象
     */
    @Override
    public T getView() {
        return mViewRef.get();
    }

    /**
     * 判断是否与view建立了关联
     * 当{@link #attachView(Object)}执行过之后，本方法就会返回true
     *
     * @return 是否已经关联过View类。
     */
    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 解除本类与View的关联
     */
    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
