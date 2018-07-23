package hua.news.module_login.pages.phoneregister;

import android.content.Context;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.BasePresenter;

import hua.news.module_login.LoginManager;
import hua.news.module_login.tools.CheckRegisterModel;

/**
 * 手机号注册页面逻辑处理
 *
 * @author hua
 * @date 2017/6/30
 */

public class PhoneRegisterPresenter extends BasePresenter<PhoneRegisterContract.MailRegisterView>
        implements PhoneRegisterContract.MailRegisterPresenter {


    @Override
    public void next() {
        final String mail = getView().getMail();
        final String phone = getView().getPhone();

        if (!LoginManager.checkPhone(FrameworkInitializer.getInstance().getContext(), phone)) {
            return;
        }

        getView().hideSoftInput();
        //邮箱和手机号没有被注册时再执行下一步
        CheckRegisterModel model = new CheckRegisterModel();
        model.checkRegister(mail, phone, new CheckRegisterModel.OnSuccessListener() {
            @Override
            public void onSuccess(Context context) {
                getView().exit();
                new LoginManager().openVerifyPhone(context, mail, phone);
            }
        });
    }


}
