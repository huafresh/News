package hua.news.module_service.setting;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author hua
 * @version 2018/3/30 18:07
 */

public interface ISettingManager extends IProvider {

    void openSettingHome(Context context);

    void openUserSetting(Context context);

}
