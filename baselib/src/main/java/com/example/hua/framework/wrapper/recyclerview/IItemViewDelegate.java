package com.example.hua.framework.wrapper.recyclerview;

import android.support.annotation.LayoutRes;

/**
 * @author hua
 * @version 1.0
 * @since 2018/8/26
 */
public interface IItemViewDelegate<T> {

    @LayoutRes
    int layoutId();

    boolean isCanHandle(T data, int position);

    void convert(MyViewHolder viewHolder, T data, int position);

}
