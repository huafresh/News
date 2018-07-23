package hua.news.module_login.pages.phoneregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import hua.news.module_login.R;
import hua.news.module_login.R2;
import hua.news.module_login.pages.base.LoginBaseActivity;
import hua.news.module_login.tools.EditTextManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 手机号注册页面
 *
 * @author hua
 * @date 2017/6/27
 */

public class PhoneRegisterActivity extends LoginBaseActivity<PhoneRegisterContract.MailRegisterPresenter>
        implements PhoneRegisterContract.MailRegisterView {

    @BindView(R2.id.edt_phone)
    EditText edtPhone;
    @BindView(R2.id.btn_next)
    Button btnNext;
    @BindView(R2.id.ll_phone_cancel)
    LinearLayout llPhoneCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_phone_register);
        ButterKnife.bind(this);
        setPresenter(new PhoneRegisterPresenter());
        mPresenter.attachView(this);
        setTitle("手机号快速注册");
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void setListeners() {
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
    }

    private void syncPhoneCancel() {
        if (!TextUtils.isEmpty(getPhone()) && edtPhone.isFocused()) {
            llPhoneCancel.setVisibility(View.VISIBLE);
        } else {
            llPhoneCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public String getMail() {
        return getPhone() + "@163.com";
    }

    @Override
    public String getPhone() {
        return edtPhone.getText().toString().trim();
    }

    @OnClick({R2.id.ll_phone_cancel, R2.id.btn_next})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ll_phone_cancel) {
            edtPhone.setText("");

        } else if (i == R.id.btn_next) {
            mPresenter.next();

        } else {
        }
    }
}
