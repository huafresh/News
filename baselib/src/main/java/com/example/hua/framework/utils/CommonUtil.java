package com.example.hua.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类，聚集各种不好分类的工具
 *
 * @author hua
 * @date 2017/6/8
 */
public class CommonUtil {

    public static final boolean isUpLOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static final boolean isUpKITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    /**
     * 设置状态栏颜色。需要4.4以上，否则无法设置。
     *
     * @param activity Activity
     * @param color    颜色
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (!isUpKITKAT) {
            return;
        }

        if (isUpLOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
            return;
        }

        //透明化状态栏
        setStatusBarTranslucent(activity);
        //添加一个与状态栏等高的view到布局中
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View childView = contentView.getChildAt(0);
        if (childView != null && childView instanceof LinearLayout) {
            //偏移内容布局
            ((FrameLayout.LayoutParams) childView.getLayoutParams()).topMargin = getStatusBarHeight(activity);
            //添加状态栏布局
            View statusView = new View(activity);
            statusView.setBackgroundColor(color);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            contentView.addView(statusView, params);
        }
    }

    /**
     * 透明化状态栏。4.4以下无效
     *
     * @param activity Activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        if (isUpKITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(lp);
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context Context
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }


    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$");

    private static final Pattern HAVE_LETTER_PATTERN =
            Pattern.compile("^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?![,\\.#%'\\+\\*\\-:;^_`]+$)[,\\" +
                    ".#%'\\+\\*\\-:;^_`0-9A-Za-z]{8,16}$");

    private static final Pattern POSTCODE_PATTERN = Pattern.compile("^[1-9][0-9]{5}$");

    /**
     * 正则判断邮箱是否合法
     *
     * @param email 邮箱
     * @return 是否合法
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    /**
     * 正则判断手机号是否合法
     *
     * @param mobile 手机号
     * @return 是否合法
     */
    public static boolean isPhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        //总结起来就是第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9
        Matcher m = PHONE_PATTERN.matcher(mobile);
        return m.matches();
    }


    /**
     * 正则判断邮编是否合法
     *
     * @param postcode 邮编
     * @return 是否合法
     */
    public static boolean isPostcode(String postcode) {
        if (TextUtils.isEmpty(postcode)) {
            return false;
        }
        Matcher m = POSTCODE_PATTERN.matcher(postcode);
        return m.matches();
    }

    /**
     * 正则判断身份证是否合法
     *
     * @param idcard
     * @return
     */
    public static boolean isIDCardLegal(String idcard) {
        if (TextUtils.isEmpty(idcard)) {
            return false;
        }
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return idcard.matches(regx) || idcard.matches(reg1) || idcard.matches(regex);
    }

    /**
     * 把手机号格式化形如134****7892的形式
     *
     * @param phone 手机号
     */
    public static String formatPhone(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            CharSequence middle = phone.subSequence(3, 7);
            CharSequence suffix = phone.subSequence(7, 11);
            String result;
            if (!middle.equals(suffix)) {
                result = phone.replace(middle, "****");
            } else {
                result = phone.subSequence(0, 4) + "****" + suffix;
            }
            return result;
        }
        return null;
    }

    /**
     * 将毫秒单位的时间转换为：hh:mm:ss形式的字符串
     *
     * @param ms 毫秒时间
     * @return 格式化后的字符串
     */
    public static String formatTime(long ms) {
        long sec = ms / 1000;
        long min = sec / 60;
        long hour = min / 60;

        long showMin = min - hour * 60;
        long showSec = sec - min * 60;

        String minString = format(showMin);
        String secString = format(showSec);

        if (hour > 0) {
            return hour + ":" + minString + ":" + secString;
        } else {
            return minString + ":" + secString;
        }
    }

    private static String format(long value) {
        String result = "";
        if (value == 0) {
            result = "00";
        } else if (value < 10 && value > 0) {
            result = "0" + value;
        } else if (value >= 10) {
            result = value + "";
        }
        return result;
    }

    /**
     * 正则判断字符串是否由字母、数字、符号组成
     *
     * @param text 字符串
     * @return
     */
    public static boolean isHaveLetter(String text) {
        Matcher m = HAVE_LETTER_PATTERN.matcher(text);
        return m.matches();
    }

    /**
     * 获取ColorStateList，主要是兼容高版本
     *
     * @return ColorStateList
     */
    public static ColorStateList getColorStateList(Context context, int color,
                                                   @Nullable Resources.Theme theme) {
        if (isUpM) {
            return context.getResources().getColorStateList(color, theme);
        } else {
            return context.getResources().getColorStateList(color);
        }
    }

    public static final boolean isUpM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    /**
     * 获取颜色资源id对应的颜色值，主要是兼容高版本
     *
     * @return 颜色值
     */
    public static int getColor(Context context, @ColorRes int color,
                               @Nullable Resources.Theme theme) {
        if (isUpM) {
            return context.getResources().getColor(color, theme);
        } else {
            return context.getResources().getColor(color);
        }
    }


    /**
     * 隐藏系统键盘
     *
     * @param view 提供windowToken
     */
    public static void hideSystemKeyBoard(View view) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示系统键盘
     *
     * @param view 接收系统键盘的输入
     */
    public static void showSystemKeyBoard(View view) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断WiFi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && (info.getType() == ConnectivityManager.TYPE_WIFI);
        }
        return false;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue px
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        return outMetric.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetric);
        return outMetric.heightPixels;
    }

}
