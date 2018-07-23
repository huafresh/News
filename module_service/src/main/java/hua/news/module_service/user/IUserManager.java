package hua.news.module_service.user;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author hua
 * @version 2018/4/2 9:02
 */

public interface IUserManager extends IProvider{

    void openCollectDetail(Context context);

    void openUserCenter(Context context);

}
