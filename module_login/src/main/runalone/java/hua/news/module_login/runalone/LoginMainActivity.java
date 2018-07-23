package hua.news.module_login.runalone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_login.LoginManager;
import hua.news.module_login.R;

/**
 * @author hua
 * @version 2018/3/29 18:58
 */

public class LoginMainActivity extends AppCompatActivity {

    @BindView(R.id.is_login)
    Button isLogin;
    @BindView(R.id.phone_register)
    Button phoneRegister;
    @BindView(R.id.mail_register)
    Button mailRegister;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.verify)
    Button verify;
    @BindView(R.id.user_info)
    Button userInfo;
    private LoginManager loginManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        ButterKnife.bind(this);
        loginManager = new LoginManager();
    }

    @OnClick({R.id.is_login, R.id.phone_register, R.id.mail_register, R.id.login, R.id.verify, R.id.user_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.is_login:
                Toast.makeText(this, loginManager.isLogin() + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.phone_register:
                loginManager.openPhoneRegister(this);
                break;
            case R.id.mail_register:
                loginManager.openMailRegister(this);
                break;
            case R.id.login:
                if (!loginManager.isLogin()) {
                    loginManager.openAccountLogin(this);
                } else {
                    Toast.makeText(this, "已登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.verify:
                loginManager.openVerifyPhone(this, "2587168099@123.com", "15079004661");
                break;
            case R.id.user_info:
                if (loginManager.isLogin()) {
                    Toast.makeText(this, loginManager.getUserInfo()+"", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
