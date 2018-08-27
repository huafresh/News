package com.example.hua.framework.wrapper.recyclerview;

import android.util.SparseArray;

/**
 * @author hua
 * @version 1.0
 * @since 2018/8/26
 */
@SuppressWarnings("ALL")
class ItemViewDelegateManager {

    private SparseArray<IItemViewDelegate> itemViewDelegates;

    void addItemViewDelegate(IItemViewDelegate itemViewDelegate) {
        if (itemViewDelegates == null) {
            itemViewDelegates = new SparseArray<>();
        }
        itemViewDelegates.put(itemViewDelegates.size() + 1, itemViewDelegate);
    }

    <T> int getItemViewType(T t, int position) {
        if (itemViewDelegates != null) {
            for (int i = 0; i < itemViewDelegates.size(); i++) {
                IItemViewDelegate itemViewDelegate = itemViewDelegates.valueAt(i);
                if (itemViewDelegate.isCanHandle(t, position)) {
                    return itemViewDelegates.keyAt(i);
                }
            }
        }
        return 0;
    }

    IItemViewDelegate getItemViewDelegate(int viewType) {
        if (itemViewDelegates != null) {
            return itemViewDelegates.get(viewType);
        }
        return null;
    }

    void convert(MyViewHolder holder, Object itemData, int position) {
        if (itemViewDelegates != null) {
            for (int i = 0; i < itemViewDelegates.size(); i++) {
                IItemViewDelegate itemViewDelegate = itemViewDelegates.valueAt(i);
                if (itemViewDelegate.isCanHandle(itemData, position)) {
                    itemViewDelegate.convert(holder, itemData, position);
                }
            }
        }
    }
}
