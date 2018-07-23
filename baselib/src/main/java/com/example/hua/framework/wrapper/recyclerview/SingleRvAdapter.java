package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;

import java.util.List;

/**
 * recyclerView adapter的进一步封装，适用于item类型单一的情况。
 *
 * @author hua
 * @date 2017/6/16
 */

public abstract class SingleRvAdapter<T> extends MultiItemRvAdapter<T> {

    public SingleRvAdapter(Context context, @LayoutRes int layoutId) {
        this(context, null, layoutId);
    }

    protected SingleRvAdapter(Context context, List<T> dataList, @LayoutRes int layouId) {
        super(context, layouId);
        setDataList(dataList);
    }

    @Override
    protected void multiConvert(MyViewHolder holder, Object data, int position) {
        convert(holder, (T) data, position);
    }

    /**
     * 决定item是否有分割线，这对边界的item无效
     *
     * @param position
     * @return
     */
    public boolean hasDivider(int position) {
        return true;
    }

    /**
     * 具体的bean与item绑定时调用此方法
     *
     * @param holder   itemView holder
     * @param data     bean
     * @param position item位置
     */
    protected abstract void convert(MyViewHolder holder, T data, int position);

}
