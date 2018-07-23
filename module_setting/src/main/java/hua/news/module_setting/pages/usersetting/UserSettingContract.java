package hua.news.module_setting.pages.usersetting;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

/**
 * Created by hua on 2017/7/12.
 * 个人设置页面契约类
 */

public class UserSettingContract {

    interface View extends IBaseView<Presenter> {

        //头像上传成功
        void onUpdateAvatarSuccess();
    }

    interface Presenter extends IBasePresenter<UserSettingContract.View> {

        //上传更新头像
        void updateAvatar(String userId,String path);
    }


}
