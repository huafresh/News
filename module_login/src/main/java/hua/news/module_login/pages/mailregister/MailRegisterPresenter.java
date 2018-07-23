package hua.news.module_login.pages.mailregister;

import android.app.Activity;
import android.content.Context;

import hua.news.module_common.FrameworkInitializer;
import com.example.hua.framework.mvpbase.BasePresenter;
import com.example.hua.framework.wrapper.dialog.CommonDialog;
import hua.news.module_login.LoginManager;
import hua.news.module_login.tools.CheckRegisterModel;

/**
 * 邮箱注册页面逻辑处理
 *
 * @author hua
 * @date 2017/6/30
 */

public class MailRegisterPresenter extends BasePresenter<MailRegisterContract.MailRegisterView>
        implements MailRegisterContract.MailRegisterPresenter {


    @Override
    public void openMailSuffixDialog(Activity activity) {
        CommonDialog commonDialog = new CommonDialog(activity);
        SelectMailContent selectMailContent = new SelectMailContent();
        selectMailContent.setOnDialogDimissListener(new SelectMailContent.OnDialogDimissListener() {
            @Override
            public void onDismiss(String suffix) {
                if (isViewAttached()) {
                    getView().onSelectMailSuffix(suffix);
                }
            }
        });
        commonDialog.addContentView(selectMailContent);
        commonDialog.show();
    }

    @Override
    public void next() {
        final String mail = getView().getMail();
        final String phone = getView().getPhone();

        Context context = FrameworkInitializer.getInstance().getContext();
        //判断邮箱以及手机号是否合法
        if (!LoginManager.checkMial(context, mail)) {
            return;
        }
        if (!LoginManager.checkPhone(context, phone)) {
            return;
        }

        //邮箱和手机号没有被注册时再执行下一步
        CheckRegisterModel model = new CheckRegisterModel();
        model.checkRegister(mail, phone, new CheckRegisterModel.OnSuccessListener() {
            @Override
            public void onSuccess(Context context) {
                getView().hideSoftInput();
                getView().exit();
                new LoginManager().openVerifyPhone(context, mail, phone);
            }
        });
    }


}
