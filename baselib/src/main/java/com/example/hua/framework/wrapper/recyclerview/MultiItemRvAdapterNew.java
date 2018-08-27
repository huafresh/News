package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * recyclerView通用多类型适配器
 *
 * @author hua
 * @date 2017/8/14
 */

@SuppressWarnings("unchecked")
public abstract class MultiItemRvAdapterNew extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "MultiItemRvAdapter";
    protected final Context mContext;
    private List<Object> mDataList;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnTouchListener mOnTouchListener;

    private ItemViewDelegateManager itemViewDelegateManager;

    public MultiItemRvAdapterNew(Context mContext) {
        this.mContext = mContext;
        this.itemViewDelegateManager = new ItemViewDelegateManager();
    }

    /**
     * 获取item数据列表
     */
    public List<?> getDataList() {
        return Collections.unmodifiableList(mDataList);
    }

    /**
     * 设置item数据列表
     */
    public void setDataList(List<?> list) {
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

    public void addItemViewDelegate(IItemViewDelegate<?> itemViewDelegate){
        itemViewDelegateManager.addItemViewDelegate(itemViewDelegate);
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewDelegateManager.getItemViewType(mDataList.get(position), position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IItemViewDelegate itemViewDelegate = itemViewDelegateManager.getItemViewDelegate(viewType);
        if (itemViewDelegate == null) {
            throw new IllegalArgumentException("no layoutId was set");
        }
        return MyViewHolder.createViewHolder(mContext, itemViewDelegate.layoutId(), parent);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Object itemData = getItemData(position);
        if (itemData == null) {
            itemData = mDataList != null ? mDataList.get(position) : null;
        }
        setListeners(holder.itemView, holder.getLayoutPosition(), itemData);
        itemViewDelegateManager.convert(holder,itemData,position);
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
    protected Object getItemData(int position) {
        return null;
    }

    private void setListeners(View itemView, final int position, final Object data) {
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
