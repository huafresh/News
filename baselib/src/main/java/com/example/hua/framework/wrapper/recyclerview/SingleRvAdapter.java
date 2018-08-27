package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * recyclerView adapter的进一步封装，适用于item类型单一的情况。
 *
 * @author hua
 * @date 2017/6/16
 */

public abstract class SingleRvAdapter<T> extends MultiItemRvAdapterNew {

    public SingleRvAdapter(Context context, @LayoutRes int layoutId) {
        this(context, null, layoutId);
    }

    protected SingleRvAdapter(Context context, List<T> dataList, @LayoutRes final int layoutId) {
        super(context);
        addItemViewDelegate(new IItemViewDelegate<T>() {
            @Override
            public int layoutId() {
                return layoutId;
            }

            @Override
            public boolean isCanHandle(T data, int position) {
                return true;
            }

            @Override
            public void convert(MyViewHolder viewHolder, T data, int position) {
                SingleRvAdapter.this.convert(viewHolder, data, position);
            }
        });

        setDataList(dataList);
    }

    /**
     * 决定item是否有分割线，这对边界的item无效
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
