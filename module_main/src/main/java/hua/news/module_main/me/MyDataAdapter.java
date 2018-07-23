package hua.news.module_main.me;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import hua.news.module_service.entitys.TitleIconEntity;

import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import hua.news.module_main.R;

import hua.news.module_service.login.ILoginManager;

/**
 * Created by hua on 2017/6/16.
 * 我的数据（订阅、收藏等等）一栏adapter
 */

public class MyDataAdapter extends SingleRvAdapter<TitleIconEntity> {

    @Autowired
    ILoginManager loginManager;

    public MyDataAdapter(Context context) {
        this(context, R.layout.item_me_my_data);
    }

    public MyDataAdapter(Context context, @LayoutRes int layouId) {
        super(context, layouId);
        ARouter.getInstance().inject(this);

    }

    @Override
    protected void convert(MyViewHolder holder, TitleIconEntity data, int position) {
        holder.setText(R.id.my_data_text, data.getTitle());
        if (loginManager != null && loginManager.isLogin()) {
            showNumber(holder);
            holder.setText(R.id.my_data_number, data.getExtern1());
        } else {
            dismissNumber(holder);
            holder.setImageResId(R.id.my_data_image, data.getIcon());
        }
    }

    private void showNumber(MyViewHolder holder) {
        holder.setVisibility(R.id.my_data_number, View.VISIBLE);
        holder.setVisibility(R.id.my_data_image, View.INVISIBLE);
    }

    private void dismissNumber(MyViewHolder holder) {
        holder.setVisibility(R.id.my_data_number, View.INVISIBLE);
        holder.setVisibility(R.id.my_data_image, View.VISIBLE);
    }
}
