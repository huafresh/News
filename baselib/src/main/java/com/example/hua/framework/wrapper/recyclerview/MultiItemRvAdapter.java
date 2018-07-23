package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerView通用多类型适配器
 *
 * @author hua
 * @date 2017/8/14
 */

public abstract class MultiItemRvAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "MultiItemRvAdapter";
    protected final Context mContext;
    protected int mLayoutId;
    protected List<T> mDataList;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnTouchListener mOnTouchListener;

    public MultiItemRvAdapter(Context context) {
        this(context, -1);
    }

    public MultiItemRvAdapter(Context mContext, int mLayoutId) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
    }

    /**
     * 获取item数据列表
     *
     * @return
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置item数据列表
     *
     * @param list
     */
    public void setDataList(List<T> list) {
        if (list == null) {
            Log.e(TAG, "setDataList: data list is null");
            return;
        }
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();
        mDataList.addAll(list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(parent, viewType);
        if (layoutId == -1) {
            layoutId = mLayoutId;
        }
        if (layoutId == -1) {
            throw new IllegalArgumentException("no layoutId is set");
        }
        View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        T itemData = getItemData(position);
        if (itemData == null) {
            itemData = mDataList != null ? mDataList.get(position) : null;
        }
        setListeners(holder.itemView, holder.getLayoutPosition(), itemData);
        multiConvert(holder, itemData, position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    /**
     * 获取item绑定的bean
     *
     * @param position 位置
     * @return Object 绑定的bean
     */
    protected T getItemData(int position) {
        return null;
    }

    private void setListeners(View itemView, final int position, final T data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v, data, position);
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onLongClick(v, data, position);
                }
                return true;
            }
        });

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnTouchListener != null) {
                    return mOnTouchListener.onTouch(v, event, position);
                }
                return false;
            }
        });
    }

    /**
     * 具体的bean与item绑定时调用此方法
     *
     * @param holder   itemView holder
     * @param data     item对应的bean
     * @param position item位置
     */
    protected abstract void multiConvert(MyViewHolder holder, T data, int position);

    /**
     * 提供item布局id
     *
     * @param parent   recyclerView
     * @param viewType 该item的类型
     * @return 布局id
     */
    protected
    @LayoutRes
    int getLayoutId(ViewGroup parent, int viewType) {
        return -1;
    }

    public void setOnItemClickListener(OnItemClickListener<?> listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<?> listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }


    public interface OnItemClickListener<D> {
        /**
         * item被点击时调用
         *
         * @param view     被点击的item
         * @param position item的位置
         */
        void onClick(View view, D data, int position);
    }

    public interface OnItemLongClickListener<D> {
        /**
         * item被长按时调用
         *
         * @param view     被长按的item
         * @param position item的位置
         */
        void onLongClick(View view, D data, int position);
    }

    public interface OnTouchListener {
        /**
         * item被触摸时调用
         *
         * @param v        被触摸的item
         * @param event    触摸事件
         * @param position item的位置
         * @return 是否消费
         */
        boolean onTouch(View v, MotionEvent event, int position);
    }


}
