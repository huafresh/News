package hua.news.module_main.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import com.example.hua.framework.widget.DragOrderRecyclerView;


import java.util.List;

import hua.news.module_service.entitys.ColumnEntity;
import hua.news.module_main.R;

/**
 * {@link DragOrderRecyclerView}控件的自定义数据适配器
 *
 * @author hua
 * @version 2017/10/17 20:14
 */

public class DragOrderRecyclerViewAdapter extends DragOrderRecyclerView.BaseAdapter<ColumnEntity> {

    public DragOrderRecyclerViewAdapter(Context context, List<ColumnEntity> dataList) {
        this(context, dataList, R.layout.item_draggable);
    }

    public DragOrderRecyclerViewAdapter(Context context, List<ColumnEntity> dataList, int mLayoutId) {
        super(context, dataList, mLayoutId);
    }

    @Override
    public void convert(@NonNull MyViewHolder holder, ColumnEntity entity, int position) {
        holder.setVisibility(R.id.iv_remove, View.INVISIBLE);
        holder.setText(R.id.tv_column_name, entity.getName());

    }

    @Override
    protected void onStateChanged(@NonNull MyViewHolder holder, ColumnEntity object, int position, int curState) {
        if (curState == DragOrderRecyclerView.STATE_NORMAL) {
            holder.setVisibility(R.id.iv_remove, View.INVISIBLE);
        } else if(curState == DragOrderRecyclerView.STATE_DRAGGABLE){
            holder.setVisibility(R.id.iv_remove, View.VISIBLE);
        }
    }
}
