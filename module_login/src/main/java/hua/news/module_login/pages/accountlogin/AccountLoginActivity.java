package hua.news.module_login.pages.accountlogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hua.news.module_login.R;
import hua.news.module_login.R2;
import hua.news.module_login.pages.base.LoginBaseActivity;
import hua.news.module_login.tools.EditTextManager;
import hua.news.module_login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账号登录页面
 *
 * @author hua
 * @date 2017/6/26
 */

public class AccountLoginActivity extends LoginBaseActivity<AccountLoginContract.AccountLoginPresenter>
        implements AccountLoginContract.AccountLoginView {

    public static final String KEY_ACCOUNT = "key_account";
    public static final int REQUEST_CODE = 1;

    @BindView(R2.id.edt_account)
    EditText edtAccount;
    @BindView(R2.id.ll_account_cancel)
    LinearLayout llAccountCancel;
    @BindView(R2.id.edt_pwd)
    EditText edtPwd;
    @BindView(R2.id.ll_pwd_cancel)
    LinearLayout llPwdCancel;
    @BindView(R2.id.ll_forget_pwd)
    LinearLayout llForgetPwd;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.we_chat_login)
    ImageView weChatLogin;
    @BindView(R2.id.sina_login)
    ImageView sinaLogin;
    @BindView(R2.id.qq_login)
    ImageView qqLogin;
    @BindView(R2.id.rl_register)
    RelativeLayout rlRegister;

    private String mAccount;
    private EditTextManager mLoginManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_account_login);

        ButterKnife.bind(this);
        setPresenter(new AccountLoginPresenter());
        mPresenter.attachView(this);

        initViews();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mLoginManager.recycle();
    }

    private void initViews() {
        setTitle("登录");
        //初始化账号输入框
        mAccount = getIntent().getStringExtra(KEY_ACCOUNT);
        if (!TextUtils.isEmpty(mAccount)) {
            edtAccount.setText(mAccount);
            edtPwd.requestFocus();
        }

        //同步登录按钮
        mLoginManager = new EditTextManager();
        mLoginManager.addEditText(edtAccount, EditTextManager.LEGAL_MODE_NORMAL);
        mLoginManager.addEditText(edtPwd, EditTextManager.LEGAL_MODE_NORMAL);
        mLoginManager.addSyncView(btnLogin);
    }

    private void setListeners() {

        edtAccount.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncAccountCancel();
            }
        });


        edtAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                syncAccountCancel();
            }
        });

        edtPwd.addTextChangedListener(new EditTextManager.EditTextWatch() {
            @Override
            public void onChange(CharSequence charSequence, int i, int i1, int i2) {
                syncPasswordCancel();
            }
        });

        edtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                syncPasswordCancel();
            }
        });

        edtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mPresenter.login();
                    return true;
                }
                return false;
            }
        });

    }

    @OnClick({R2.id.ll_account_cancel, R2.id.ll_pwd_cancel, R2.id.btn_login, R2.id.rl_register})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ll_account_cancel) {
            edtAccount.setText("");

        } else if (i == R.id.ll_pwd_cancel) {
            edtPwd.setText("");

        } else if (i == R.id.btn_login) {
            mPresenter.login();

        } else if (i == R.id.rl_register) {
            new LoginManager().openMailRegister(this);

        } else {
        }
    }


    private void syncAccountCancel() {
        //不为空且有焦点时显示cancel按钮
        if (!TextUtils.isEmpty(getAccount()) && edtAccount.isFocused()) {
            setAccountCancelVisibility(true);
        } else {
            setAccountCancelVisibility(false);
        }
    }

    private void syncPasswordCancel() {
        //不为空且有焦点时显示cancel按钮
        if (!TextUtils.isEmpty(getPassword()) && edtPwd.isFocused()) {
            setPwdCancelVisibility(true);
        } else {
            setPwdCancelVisibility(false);
        }
    }

    private void setAccountCancelVisibility(boolean visible) {
        llAccountCancel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setPwdCancelVisibility(boolean visible) {
        llPwdCancel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    @Override
    public String getAccount() {
        return edtAccount.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return edtPwd.getText().toString().trim();
    }

    @Override
    public String getThirdPlatName() {
        return null;
    }

    @Override
    public String getThirdPlatUniqueId() {
        return null;
    }

}
