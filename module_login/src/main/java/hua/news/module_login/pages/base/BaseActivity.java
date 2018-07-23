package hua.news.module_login.pages.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.example.hua.framework.utils.CommonUtil;

import hua.news.module_login.R;

/**
 * @author hua
 * @date 2017/6/8
 */
public abstract class BaseActivity extends AppCompatActivity {

    private TextView tvExtra;
    private FrameLayout flContain;
    private Toolbar mToolBar;

    private List<OnBackPressListener> mKeyListeners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initViews();
        //设置状态栏颜色，实现沉浸式状态栏
        CommonUtil.setStatusBarColor(this,
                CommonUtil.getColor(this, R.color.colorPrimary, null));
    }

    private void initViews() {
        flContain = findViewById(R.id.fl_container);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        tvExtra = (TextView) findViewById(R.id.tv_extra);
        onInitDefaultToolbar(mToolBar);
    }


    /**
     * 定制子类的ToolBar
     *
     * @param toolbar toolbar布局
     */
    protected void onInitDefaultToolbar(@NonNull Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.activity_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.textStyle_16_white);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //使item图标可见
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置内容视图id
     */
    protected void setContent(@LayoutRes int id) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View contentView = mInflater.inflate(id, flContain, false);
        setContent(contentView);
    }

    /**
     * 设置内容视图
     */
    protected void setContent(View view) {
        if (view != null) {
            flContain.removeAllViews();
            flContain.addView(view);
        }
    }

    /**
     * 设置内容视图fragment
     */
    protected void setContent(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fl_container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * toolbar右边文本
     */
    protected TextView getExtraView() {
        return tvExtra;
    }

    /**
     * 设置标题栏的显示以及隐藏
     */
    public void setToolBarVisibility(boolean visible) {
        if (mToolBar != null) {
            mToolBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    @CallSuper
    public void onBackPressed() {
        if (mKeyListeners != null) {
            for (OnBackPressListener listener : mKeyListeners) {
                if (listener.onBackPress()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public interface OnBackPressListener {
        /**
         * 按物理返回键时被调用。
         *
         * @return 返回true表示消费掉返回事件，false则不消费。不消费的返回事件会继续传递给其他监听。
         */
        boolean onBackPress();
    }

    /**
     * 添加返回键监听。
     * 请注意在适当的时候调用{@link #unRegisterOnBackPressListener}移除监听
     *
     * @param listener 要添加的监听
     * @see OnBackPressListener
     */
    public void registerOnBackPressListener(OnBackPressListener listener) {
        if (mKeyListeners == null) {
            mKeyListeners = new ArrayList<>();
        }
        if (listener != null) {
            mKeyListeners.add(listener);
        }
    }

    /**
     * 移除监听
     *
     * @param listener 被移除的监听
     */
    public void unRegisterOnBackPressListener(OnBackPressListener listener) {
        if (mKeyListeners != null) {
            mKeyListeners.remove(listener);
        }
    }

    /**
     * 清除所有监听
     */
    public void clearOnBackPressListener() {
        if (mKeyListeners != null) {
            mKeyListeners.clear();
        }
    }

}
