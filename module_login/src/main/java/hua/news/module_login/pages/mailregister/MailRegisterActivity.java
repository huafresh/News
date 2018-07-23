package hua.news.module_login.pages.mailregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hua.news.module_login.R;
import hua.news.module_login.R2;
import hua.news.module_login.tools.EditTextManager;
import hua.news.module_login.pages.base.LoginBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邮箱注册页面
 *
 * @author hua
 * @date 2017/6/28
 */

public class MailRegisterActivity extends LoginBaseActivity<MailRegisterContract.MailRegisterPresenter>
        implements MailRegisterContract.MailRegisterView {

    @BindView(R2.id.edt_mail)
    EditText edtMail;
    @BindView(R2.id.tv_mail_suffix)
    TextView tvMailSuffix;
    @BindView(R2.id.edt_phone)
    EditText edtPhone;
    @BindView(R2.id.tv_verify_code)
    TextView tvVerifyCode;
    @BindView(R2.id.btn_next)
    Button btnNext;
    @BindView(R2.id.ll_mail_cancel)
    LinearLayout llMailCancel;
    @BindView(R2.id.ll_phone_cancel)
    LinearLayout llPhoneCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_mail_register);

        ButterKnife.bind(this);
        setPresenter(new MailRegisterPresenter());
        mPresenter.attachView(this);
        setTitle("邮箱注册");

        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void setListeners() {
        edtMail.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncMailCancel();
            }
        });
        edtMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                syncMailCancel();
            }
        });

        edtPhone.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncPhoneCancel();
            }
        });
        edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                syncPhoneCancel();
            }
        });
        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mPresenter.next();
                    return true;
                }
                return false;
            }
        });
    }

    private void syncMailCancel() {
        if (!TextUtils.isEmpty(getMail()) && edtMail.isFocused()) {
            llMailCancel.setVisibility(View.VISIBLE);
        } else {
            llMailCancel.setVisibility(View.GONE);
        }
    }

    private void syncPhoneCancel() {
        if (!TextUtils.isEmpty(getPhone()) && edtPhone.isFocused()) {
            llPhoneCancel.setVisibility(View.VISIBLE);
        } else {
            llPhoneCancel.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSelectMailSuffix(String suffix) {
        tvMailSuffix.setText(suffix);
    }

    @Override
    public String getMail() {
        return edtMail.getText().toString().trim() + tvMailSuffix.getText();
    }

    @Override
    public String getPhone() {
        return edtPhone.getText().toString().trim();
    }

    @OnClick({R2.id.tv_mail_suffix, R2.id.btn_next, R2.id.ll_mail_cancel, R2.id.ll_phone_cancel})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_mail_suffix) {
            mPresenter.openMailSuffixDialog(this);

        } else if (i == R.id.btn_next) {
            mPresenter.next();

        } else if (i == R.id.ll_mail_cancel) {
            edtMail.setText("");

        } else if (i == R.id.ll_phone_cancel) {
            edtPhone.setText("");

        }
    }
}
