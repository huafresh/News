package hua.news.module_user.usercenter;

import com.example.hua.framework.mvpbase.IBasePresenter;
import com.example.hua.framework.mvpbase.IBaseView;

/**
 * Created by hua on 2017/7/12.
 * 用户信息中心契约类
 */

public class UserCenterContract {

    interface View extends IBaseView<Presenter> {

        //头像上传成功
        void onUpdateAvatarSuccess();
    }

    interface Presenter extends IBasePresenter<UserCenterContract.View> {
        //上传更新头像
        void updateAvatar(String userId,String path);
    }


}
