package com.example.hua.framework.wrapper.pullrefresh;

import android.view.View;
import android.view.ViewGroup;

/**
 * 工具
 * Created by hua on 2017/11/18.
 */

public class PtrUtil {

    /**
     * 改变View的TopMargin参数
     *
     * @param view   View
     * @param offset TopMargin偏移值
     */
    public static void offsetTopMargin(View view, int offset) {
        if (view != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (lp != null) {
                lp.topMargin += offset;
                view.setLayoutParams(lp);
            }
        }
    }

    /**
     * 获取View的TopMargin值
     *
     * @param view View
     * @return TopMargin值
     */
    public static int getTopMargin(View view) {
        if (view != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (lp != null) {
                return lp.topMargin;
            }
        }
        return 0;
    }

}
