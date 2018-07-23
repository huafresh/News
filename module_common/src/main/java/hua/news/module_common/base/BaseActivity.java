package hua.news.module_common.base;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hua.framework.storage.StorageManager;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.utils.MLog;
import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;
import com.example.hua.framework.wrapper.loadlayout.LoadService;

import java.util.ArrayList;
import java.util.List;

import hua.news.module_common.R;
import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_common.loadviews.LoadingView;

/**
 * @author hua
 * @date 2017/6/8
 * 应用基类activity。
 * 主要实现了：
 * a.activity标题栏，提供默认实现，支持定制
 * b.activity底部栏，不提供默认实现，支持定制
 * <p>
 * 使用{@link #setContent}系列方法设置中间部分显示的内容
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 日/夜模式存储key
     */
    public static final String KEY_NIGHT_MODE = "key_night_mode";

    /**
     * 日间/夜间模式相关常量
     */
    public static final String MODE_DAY = "day";
    public static final String MODE_NIGHT = "night";
    public static final String MODE_AUTO = "auto";

    private TextView tvExtra;
    private FrameLayout flContain;
    private Toolbar mToolBar;
    protected LoadService mLoadService;

    private List<OnBackPressListener> mKeyListeners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //初始化夜/日间模式
        String mode = StorageManager.getInstance(this).getFromDisk(KEY_NIGHT_MODE);
        if (setNightMode(mode)) {
            StorageManager.getInstance(this).saveToDisk(KEY_NIGHT_MODE, mode);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        CommonUtil.setStatusBarColor(this,
                CommonUtil.getColor(this, R.color.color_theme, null));

        initViews();
        if (isEnableLoadManager()) {
            mLoadService = LoadLayoutManager.getInstance().register(this);
        }
    }


    private void initViews() {
        flContain = (FrameLayout) findViewById(R.id.fl_container);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        tvExtra = (TextView) mToolBar.findViewById(R.id.tv_extra);
        initToolbar(mToolBar);
    }


    protected boolean isEnableLoadManager() {
        return true;
    }


    /**
     * 定制子类的ToolBar
     *
     * @param toolbar toolbar布局
     */
    protected void initToolbar(@NonNull Toolbar toolbar) {
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

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showLoadView(LoadingView.class.getCanonicalName());
        }
    }

    protected void showError() {
        if (mLoadService != null) {
            mLoadService.showLoadView(LoadErrorView.class.getCanonicalName());
        }
    }

    protected void showComplete() {
        if (mLoadService != null) {
            mLoadService.showLoadComplete();
        }
    }

    /**
     * 设置内容视图id
     */
    protected void setContent(@LayoutRes int id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(id, flContain, false);
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

    /**
     * 设置日间/夜间主题模式
     *
     * @param mode 模式
     *             {@link #MODE_DAY} 为日间（默认）
     *             {@link #MODE_NIGHT} 为夜间
     *             {@link #MODE_AUTO} 随系统时间变化
     * @return 是否设置成功
     */
    private boolean setNightMode(String mode) {
        boolean isSuccess = true;
        if (!TextUtils.isEmpty(mode)) {
            switch (mode) {
                case MODE_DAY:
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case MODE_NIGHT:
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case MODE_AUTO:
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    break;
                default:
                    isSuccess = false;
                    break;
            }
        } else {
            //默认为日间模式
            isSuccess = false;
        }
        if (!isSuccess) {
            MLog.e("设置日间/夜间主题模式失败，使用默认值");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        return isSuccess;
    }

    /**
     * 透明化状态栏
     *
     * @return 是否成功
     */
    protected boolean setStatusBarTranslucent() {
        CommonUtil.setStatusBarTranslucent(this);
        return true;
    }

    /**
     * 获取系统定义属性的dimension值
     *
     * @return dimension值
     */
    protected float getSystemDimension(int attr) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        int resId = typedValue.resourceId;
        return getResources().getDimension(resId);
    }

    /**
     * replace fragment
     *
     * @param id       容器id
     * @param fragment fragment对象
     */
    protected void replaceFragment(@IdRes int id, Fragment fragment) {

    }

    /**
     * 判断系统键盘是否显示。
     * 此法参考自：http://blog.csdn.net/sinat_31311947/article/details/53899166
     */
    @SuppressLint("NewApi")
    protected boolean isSystemKeyBoardShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != getSoftButtonsBarHeight();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


}
