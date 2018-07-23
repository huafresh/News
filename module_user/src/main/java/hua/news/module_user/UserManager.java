package hua.news.module_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;

import hua.news.module_common.constants.RouterConstant;
import hua.news.module_service.user.IUserManager;
import hua.news.module_user.collect.CollectDetailActivity;
import hua.news.module_user.usercenter.UserCenterActivity;

/**
 * @author hua
 * @version 2018/4/2 9:04
 */
@Route(path = RouterConstant.MODULE_USER_MANAGER)
public class UserManager implements IUserManager {
    @Override
    public void openCollectDetail(Context context) {
        Intent intent = new Intent(context, CollectDetailActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void openUserCenter(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void init(Context context) {

    }
}
