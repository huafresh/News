package com.example.hua.framework.utils;

import android.content.Context;

/**
 * @author hua
 * @version 2018/5/4 16:24
 */

public class ToastUtil {

    private static android.widget.Toast sToast;

    public static void toast(Context context, String s) {
        if (sToast == null) {
            sToast = android.widget.Toast.makeText(context, s, android.widget.Toast.LENGTH_SHORT);
            sToast.show();
        } else {
            sToast.setText(s);
            sToast.show();
        }
    }

}
