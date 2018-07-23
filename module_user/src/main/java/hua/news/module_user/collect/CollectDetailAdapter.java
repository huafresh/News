package hua.news.module_user.collect;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import hua.news.module_service.entitys.CollectBean;
import hua.news.module_user.R;

/**
 * Author: hua
 * Created: 2017/9/29
 * Description:
 * 收藏页面适配器
 */

public class CollectDetailAdapter extends SingleRvAdapter<CollectBean> {

    public CollectDetailAdapter(Context context) {
        super(context, R.layout.item_collect_detail);
    }

    public CollectDetailAdapter(Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void convert(MyViewHolder holder, CollectBean data, int position) {
        holder.setText(R.id.text, data.getTitle());
    }
}
