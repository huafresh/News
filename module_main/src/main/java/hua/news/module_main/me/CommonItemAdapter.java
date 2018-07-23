package hua.news.module_main.me;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;

import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;
import hua.news.module_service.entitys.SettingCommonEntity;
import hua.news.module_main.R;

/**
 * Created by hua on 2017/6/16.
 * 通用的item适配器
 */

public class CommonItemAdapter extends SingleRvAdapter<SettingCommonEntity> {

    public CommonItemAdapter(Context context) {
        this(context, R.layout.item_me_common);
    }

    public CommonItemAdapter(Context context, @LayoutRes int layouId) {
        super(context, layouId);
    }

    @Override
    protected void convert(MyViewHolder holder, SettingCommonEntity bean, int position) {
        if (bean.getIcon() != 0) {
            holder.setVisibility(R.id.common_item_image, View.VISIBLE);
            holder.setImageResId(R.id.common_item_image, bean.getIcon());
        } else {
            holder.setVisibility(R.id.common_item_image, View.GONE);
        }

        setText(holder, R.id.common_item_title, bean.getTitle());
        setText(holder, R.id.common_item_description, bean.getDescription());

        if (bean.is_has_switch()) {
            holder.setVisibility(R.id.common_item_right_arrow, View.GONE);
            holder.setVisibility(R.id.common_item_switch_compat, View.VISIBLE);
            holder.setSwitchOnOff(R.id.common_item_switch_compat, bean.is_switch_on());
        } else {
            holder.setVisibility(R.id.common_item_right_arrow, View.VISIBLE);
            holder.setVisibility(R.id.common_item_switch_compat, View.GONE);
        }
    }

    private void setText(MyViewHolder holder, @IdRes int id, String text) {
        if (!TextUtils.isEmpty(text)) {
            holder.setVisibility(id, View.VISIBLE);
            holder.setText(id, text);
        } else {
            holder.setVisibility(id, View.GONE);
        }
    }

}
