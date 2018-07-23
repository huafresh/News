package hua.news.module_user.usercenter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import hua.news.module_service.entitys.LoginUserInfo;
import hua.news.module_user.R;


/**
 * Created by hua on 2017/7/11.
 * 个人信息中心跟帖历史记录数据adapter
 */

public class UserFollowHistoryAdapter extends SingleRvAdapter<LoginUserInfo.FollowHistoryInfo> {

    public UserFollowHistoryAdapter(Context context) {
        this(context, R.layout.item_user_center_history);
    }

    @Override
    public boolean hasDivider(int position) {
        if (position == 0) { //空的头部view，不需要分割线
            return false;
        }
        return super.hasDivider(position);
    }

    public UserFollowHistoryAdapter(Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void convert(MyViewHolder holder, LoginUserInfo.FollowHistoryInfo data, int position) {
        // TODO: 2017/7/11  
    }
}
