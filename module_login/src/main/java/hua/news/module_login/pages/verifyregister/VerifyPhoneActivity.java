package hua.news.module_login.pages.verifyregister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.framework.utils.CommonUtil;

import hua.news.module_login.R;
import hua.news.module_login.R2;
import hua.news.module_login.pages.base.LoginBaseActivity;
import hua.news.module_login.tools.EditTextManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 验证手机号页面
 *
 * @author hua
 * @date 2017/6/28
 */

public class VerifyPhoneActivity extends LoginBaseActivity<VerifyPhoneContract.AccountLoginPresenter>
        implements VerifyPhoneContract.AccountLoginView {

    public static final String KEY_PHONE = "key_phone";
    public static final String KEY_MAIL = "key_mail";

    private static final int COUNTER_TIME = 60000;
    private static final int COUNTER_UNIT = 1000;
    @BindView(R2.id.tv_phone)
    TextView tvPhone;
    @BindView(R2.id.edt_verify_code)
    EditText edtVerifyCode;
    @BindView(R2.id.ll_verify_code_cancel)
    LinearLayout llVerifyCodeCancel;
    @BindView(R2.id.tv_send_verify_code)
    TextView tvSendVerifyCode;
    @BindView(R2.id.edt_password)
    EditText edtPassword;
    @BindView(R2.id.ll_password_cancel)
    LinearLayout llPasswordCancel;
    @BindView(R2.id.ll_eye)
    LinearLayout llEye;
    @BindView(R2.id.btn_register)
    Button btnRegister;


    private String mMail;
    private String mPhone;
    private CounterTimer mTimer;

    public static void open(Context context, String mail, String phone) {
        Intent intent = new Intent(context, VerifyPhoneActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_MAIL, mail);
        intent.putExtra(KEY_PHONE, phone);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_verify_phone);

        initDatas();
        initViews();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R2.id.tv_send_verify_code)
    public void onClickSendVerifyCode() {
        mTimer.start();
        mPresenter.sendVerifyCode();
    }

    @OnClick(R2.id.ll_verify_code_cancel)
    public void onClickAccountCancel() {
        edtVerifyCode.setText("");
    }

    @OnClick(R2.id.ll_password_cancel)
    public void onClickPasswordCancel() {
        edtPassword.setText("");
    }

    @OnClick(R2.id.ll_eye)
    public void onClickEye() {
        edtPassword.setText("");
    }

    @OnClick(R2.id.btn_register)
    public void onClickRegister() {
        mPresenter.register();
    }

    @Override
    public void exit() {
        //此时exit表示登录或者注册成功，此时需要关闭所有的中间页面
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        // TODO: 2017/11/14 想其他办法
        super.exit();
    }

    private void initDatas() {
        ButterKnife.bind(this);
        setPresenter(new VerifyPhonePresenter());
        mPresenter.attachView(this);
        //获取参数
        mMail = getIntent().getStringExtra(KEY_MAIL);
        mPhone = getIntent().getStringExtra(KEY_PHONE);
        //初始化定时器
        mTimer = new CounterTimer(COUNTER_TIME, COUNTER_UNIT);
    }

    private void initViews() {
        setTitle("注册");
        tvPhone.setText(CommonUtil.formatPhone(mPhone));
        //获取验证码
        mPresenter.sendVerifyCode();
    }

    private void setListeners() {

        edtVerifyCode.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncVerifyCodeCancel();
            }
        });

        edtPassword.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncPasswordCancel();
            }
        });

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mPresenter.register();
                    return true;
                }
                return false;
            }
        });


    }

    private void syncVerifyCodeCancel() {
        //不为空且有焦点时显示cancel按钮
        if (!TextUtils.isEmpty(getVerifyCode()) && edtVerifyCode.isFocused()) {
            llVerifyCodeCancel.setVisibility(View.VISIBLE);
        } else {
            llVerifyCodeCancel.setVisibility(View.GONE);
        }
    }

    private void syncPasswordCancel() {
        //不为空且有焦点时显示cancel按钮
        if (!TextUtils.isEmpty(getPassword()) && edtPassword.isFocused()) {
            llPasswordCancel.setVisibility(View.VISIBLE);
        } else {
            llPasswordCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public String getVerifyCode() {
        return edtVerifyCode.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return edtPassword.getText().toString().trim();
    }

    @Override
    public void onSendVerifyCodeSuccess(String code) {
        if (!TextUtils.isEmpty(code)) {
            mTimer.start();
            tvSendVerifyCode.setEnabled(false);
            edtVerifyCode.setText(code);
            edtPassword.requestFocus();
        }
    }

    @Override
    public void onSendVerifyCodeFailed() {
        tvSendVerifyCode.setEnabled(true);
        tvSendVerifyCode.setText("重新发送");
    }

    @Override
    public String getMail() {
        return mMail;
    }

    @Override
    public String getPhone() {
        return mPhone;
    }

    private class CounterTimer extends CountDownTimer {

        public CounterTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvSendVerifyCode.setText("重新发送(" + millisUntilFinished / COUNTER_UNIT + ")");
        }

        @Override
        public void onFinish() {
            tvSendVerifyCode.setText("重新发送");
            tvSendVerifyCode.setEnabled(true);
        }
    }

}
