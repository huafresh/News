package com.example.hua.framework.wrapper.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * 对现有的recyclerView adapter进行装饰，使其能够添加头部和尾部布局。
 * 适用于增加需求的情况，如果是要设计新的adapter，可以直接继承{@link MultiItemRvAdapter}
 * 然后把头部和尾部当成特殊的item
 *
 * @author hua
 * @date 2017/8/14
 */
public class HeaderAndFootWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeadViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();
    private static final int REAL_ITEM_TYPE = 0;
    private static final int BASE_ITEM_TYPE_HEAD = 100;
    private static final int BASE_ITEM_TYPE_FOOT = 200;

    public HeaderAndFootWrapper(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (mHeadViews.get(viewType) != null) {
            itemView = mHeadViews.get(viewType);
        } else if (mFootViews.get(viewType) != null) {
            itemView = mFootViews.get(viewType);
        }

        RecyclerView.ViewHolder viewHolder = null;
        if (itemView != null) {
            parent.removeView(itemView);
            viewHolder = new MyViewHolder(itemView);
        } else {
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getRealSum() > 0 &&  //有真实item
                position >= getHeadSum() &&  //略过头部布局
                position < getRealSum() + getHeadSum()) { //略过底部布局

            final int realPos = position - getHeadSum();
            Object data = null;
            if (mAdapter instanceof MultiItemRvAdapter) {
                final MultiItemRvAdapter adapter = (MultiItemRvAdapter) this.mAdapter;
                data = adapter.mDataList.get(realPos);
                adapter.multiConvert((MyViewHolder) holder, data, realPos);
            } else { //一般的adapter
                mAdapter.onBindViewHolder(holder, realPos);
                //对于一般的adapter，因为拿不到item的data，所以这里做不了点击事件处理
                //data = mAdapter.getItemId(realPos);
            }

            final Object finalData = data;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(v, finalData, realPos);
                    }
                }
            });
        }
    }

    private MultiItemRvAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MultiItemRvAdapter.OnItemClickListener<?> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<D> {
        /**
         * item被点击时调用
         *
         * @param view     被点击的item
         * @param data     被点击的item的数据
         * @param position item的位置
         */
        void onClick(View view, D data, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeadSum()) {
            return mHeadViews.keyAt(position);
        } else if (getItemCount() - position < getFootSum() + 1) {
            return mFootViews.keyAt(getFootSum() - getItemCount() + position);
        }
        return REAL_ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return getRealSum() + getHeadSum() + getFootSum();
    }

    private int getRealSum() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    private int getHeadSum() {
        return mHeadViews != null ? mHeadViews.size() : 0;
    }

    private int getFootSum() {
        return mFootViews != null ? mFootViews.size() : 0;
    }

    /**
     * 添加一个头部布局
     *
     * @param view
     */
    public void addHeadView(View view) {
        if (mHeadViews != null) {
            mHeadViews.put(mHeadViews.size() + BASE_ITEM_TYPE_HEAD, view);
        } else {
            mHeadViews = new SparseArray<>();
            mHeadViews.put(mHeadViews.size() + BASE_ITEM_TYPE_HEAD, view);
        }
    }

    public void removeHeadView(View view) {
        if (mHeadViews != null) {
            int index = mHeadViews.indexOfValue(view);
            if (index != -1) {
                mHeadViews.removeAt(index);
            }
        }
    }

    /**
     * 添加一个底部布局
     *
     * @param view
     */
    public void addFootView(View view) {
        if (mFootViews != null) {
            mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOT, view);
        } else {
            mFootViews = new SparseArray<>();
            mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOT, view);
        }
    }

}
