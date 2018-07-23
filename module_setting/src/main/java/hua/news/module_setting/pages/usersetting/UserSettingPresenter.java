package hua.news.module_setting.pages.usersetting;

import com.example.hua.framework.mvpbase.BasePresenter;

/**
 * Created by hua on 2017/7/12.
 * 个人设置presenter层
 */

public class UserSettingPresenter extends BasePresenter<UserSettingContract.View>
        implements UserSettingContract.Presenter {

    @Override
    public void updateAvatar(String userId,String path) {
//        LoginRequestProvider.getInstance().modifyAvatar(userId,path, new CallBack<Object>() {
//            @Override
//            public void onSuccess(Context context, Object data) {
//                if (isViewAttached()) {
//                    getView().onUpdateAvatarSuccess();
//                }
//            }
//
//            @Override
//            public void onError(Context context,Bundle error) {
//                Toast.makeText(FrameworkInitializer.getInstance().getContext(), "头像上传失败",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}
