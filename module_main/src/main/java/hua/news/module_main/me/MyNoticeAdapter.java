package hua.news.module_main.me;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import hua.news.module_service.entitys.TitleIconEntity;

import hua.news.module_service.login.ILoginManager;

import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import hua.news.module_main.R;

/**
 * Created by hua on 2017/6/16.
 * 我的关注以及我的粉丝adapter
 */

public class MyNoticeAdapter extends SingleRvAdapter<TitleIconEntity> {

    @Autowired
    ILoginManager loginManager;

    public MyNoticeAdapter(Context context) {
        this(context, R.layout.item_me_my_notice);
    }

    public MyNoticeAdapter(Context context, @LayoutRes int layouId) {
        super(context, layouId);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void convert(MyViewHolder holder, TitleIconEntity data, int position) {
        holder.setText(R.id.tv_my_notice_text, data.getTitle());
        if (loginManager != null && loginManager.isLogin()) {
            holder.setVisibility(R.id.tv_my_notice_number, View.VISIBLE);
            holder.setText(R.id.tv_my_notice_number, data.getExtern1());
        } else {
            holder.setVisibility(R.id.tv_my_notice_number, View.GONE);
        }
    }
}
