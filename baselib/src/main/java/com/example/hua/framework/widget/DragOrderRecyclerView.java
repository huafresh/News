package com.example.hua.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hua.framework.R;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * 可拖拽排序的控件。
 * 使用方法：
 * 1、在xml中布局或者new出来
 * 2、调用{@link #setDraggableAdapter(BaseAdapter)}方法设置数据适配器
 * 3、调用{@link #setState(int)}方法设置控件所处的状态
 * [4]、调用{@link #getDataList()}方法获取调整顺序后的数据列表
 *
 * @author hua
 * @version : 2017/10/16
 */

public class DragOrderRecyclerView extends RecyclerView {

    public static final int DEFAULT_SPAN_COUNT = 4;

    private BaseAdapter mBaseAdapter;
    private Context mContext;

    private Set<OnStateChangedListener> mStateChangedListeners;

    private ItemTouchHelper.Callback mCallBack;
    private ItemTouchHelper mTouchHelper;

    public static final int STATE_NORMAL = 1;
    public static final int STATE_DRAGGABLE = 2;

    @IntDef({STATE_NORMAL, STATE_DRAGGABLE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    private int mCurState;

    public static final int ITEM_STYLE_VERTICAL = 1;
    public static final int ITEM_STYLE_HORIZONTAL = 2;
    public static final int ITEM_STYLE_GRID = 3;

    @IntDef({ITEM_STYLE_VERTICAL, ITEM_STYLE_HORIZONTAL, ITEM_STYLE_GRID})
    @Retention(RetentionPolicy.SOURCE)

    private @interface ItemStyle {
    }

    private int mItemStyle;

    public DragOrderRecyclerView(Context context) {
        this(context, null);
    }

    public DragOrderRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragOrderRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragOrderRecyclerView);
        mItemStyle = array.getInt(R.styleable.DragOrderRecyclerView_ItemStyle, ITEM_STYLE_VERTICAL);
        array.recycle();

        mCurState = STATE_NORMAL;
    }

    /**
     * 设置item展示的样式
     *
     * @param style 可取值：
     *              {@link #ITEM_STYLE_VERTICAL} 垂直线性显示
     *              {@link #ITEM_STYLE_HORIZONTAL} 水平线性显示
     *              {@link #ITEM_STYLE_GRID} 网格显示
     */
    public void setItemStyle(@ItemStyle int style) {
        mItemStyle = style;
    }

    /**
     * 设置进入可拖拽状态
     *
     * @param state 可取值：
     *              {@link #STATE_NORMAL} 正常状态
     *              {@link #STATE_DRAGGABLE} 可拖拽状态
     */
    public void setState(@State int state) {
        if (mCurState != state) {
            mCurState = state;
            notifyStateChanged(state);
        }
    }

    /**
     * 获取当前的状态
     *
     * @return 当前的状态
     * @see #setState(int)
     */
    public int getState() {
        return mCurState;
    }

    /**
     * 状态改变监听
     *
     * @see #setState(int)
     */
    public interface OnStateChangedListener {
        /**
         * 状态改变时调用
         *
         * @param curState 当前的状态
         */
        void onStateChanged(int curState);
    }

    /**
     * 添加状态监听
     *
     * @param listener 状态监听
     * @see #setState(int)
     */
    public void addOnStateChangedListener(OnStateChangedListener listener) {
        if (mStateChangedListeners == null) {
            mStateChangedListeners = new ArraySet<>();
        }
        if (listener != null) {
            mStateChangedListeners.add(listener);
        }
    }

    /**
     * 设置数据适配器
     *
     * @param baseAdapter 数据适配器
     */
    public void setDraggableAdapter(BaseAdapter baseAdapter) {
        if (baseAdapter == null) {
            return;
        }

        mBaseAdapter = baseAdapter;
        this.setAdapter(baseAdapter);

        initRecyclerView(baseAdapter.getColumnNum());

        if (mTouchHelper == null) {
            setItemTouchDragCallBack(new DefaultItemTouchCallBack(baseAdapter));
        }

        setItemListeners();
    }

    private void initRecyclerView(int columnNum) {
        RecyclerView.LayoutManager layoutManager = null;
        switch (mItemStyle) {
            case ITEM_STYLE_VERTICAL:
                layoutManager = new LinearLayoutManager(mContext);
                break;
            case ITEM_STYLE_HORIZONTAL:
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                layoutManager = manager;
                break;
            case ITEM_STYLE_GRID:
                layoutManager = new GridLayoutManager(mContext, columnNum);
                break;
            default:
                break;
        }
        setLayoutManager(layoutManager);
    }

    private void setItemListeners() {
        mBaseAdapter.setOnItemLongClickListener(new MultiItemRvAdapter.OnItemLongClickListener<Object>() {
            @Override
            public void onLongClick(View view, Object data, int position) {
                mTouchHelper.startDrag(findViewHolderForAdapterPosition(position));
                mCurState = STATE_DRAGGABLE;
                notifyStateChanged(mCurState);
            }
        });
        mBaseAdapter.setOnTouchListener(new MultiItemRvAdapter.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event, int position) {
                int actionMasked = event.getActionMasked();
                switch (actionMasked) {
                    case MotionEvent.ACTION_DOWN:
                        if (mCurState == STATE_DRAGGABLE) {
                            mTouchHelper.startDrag(findViewHolderForAdapterPosition(position));
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void notifyStateChanged(int curState) {
        if (mStateChangedListeners != null) {
            for (OnStateChangedListener listener : mStateChangedListeners) {
                listener.onStateChanged(curState);
            }
        }
        if (mBaseAdapter != null) {
            int itemCount = mBaseAdapter.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                MyViewHolder holder = (MyViewHolder) findViewHolderForAdapterPosition(i);
                mBaseAdapter.onStateChanged(holder, mBaseAdapter.getDataList().get(i), i, curState);
            }
        }
    }

    /**
     * 通过继承此类实现{@link DragOrderRecyclerView}的数据适配器
     *
     * @param <T> 每个item对应的数据实体类型
     */
    public abstract static class BaseAdapter<T> extends SingleRvAdapter<T> {

        protected BaseAdapter(Context context, List<T> dataList, @LayoutRes int layouId) {
            super(context, dataList, layouId);
        }

        /**
         * 状态改变时调用。可以复写此方法实现进入可拖拽状态时item样式的变化
         *
         * @param curState 当前状态
         * @see #setState(int)
         */
        protected void onStateChanged(@NonNull MyViewHolder holder, T object, int position, int curState) {

        }

        /**
         * 是否使能滑动删除。默认是false
         *
         * @return 是否使能滑动删除
         */
        protected boolean enableSwipeDrop() {
            return false;
        }

        /**
         * 如果item是Grid样式，此方法的返回值会决定列数
         *
         * @return Grid的列数
         */
        protected int getColumnNum() {
            return DEFAULT_SPAN_COUNT;
        }
    }

    /**
     * 设置拖拽回调。这个回调是拖拽实现的核心类
     *
     * @param callBack 拖拽回调
     * @see ItemTouchHelper.Callback
     */
    public void setItemTouchDragCallBack(ItemTouchHelper.Callback callBack) {
        if (callBack == null) {
            return;
        }
        mCallBack = callBack;
        mTouchHelper = new ItemTouchHelper(callBack);
        mTouchHelper.attachToRecyclerView(this);
    }

    public static class DefaultItemTouchCallBack extends ItemTouchHelper.Callback {
        private BaseAdapter baseAdapter;

        public DefaultItemTouchCallBack(BaseAdapter baseAdapter) {
            this.baseAdapter = baseAdapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                swipeFlags = dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.UP |
                        ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
            } else if (layoutManager instanceof LinearLayoutManager) {
                int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                if (orientation == LinearLayoutManager.VERTICAL) {
                    dragFlags = swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                } else if (orientation == LinearLayoutManager.HORIZONTAL) {
                    dragFlags = swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            Collections.swap(baseAdapter.getDataList(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
            baseAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            if (baseAdapter.getDataList() != null) {
                baseAdapter.getDataList().remove(pos);
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return baseAdapter.enableSwipeDrop();
        }
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> getDataList() {
        if (mBaseAdapter != null) {
            return mBaseAdapter.getDataList();
        }
        return null;
    }

}
