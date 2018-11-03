package com.example.hua.framework.wrapper.screen_adapt;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/3
 */
public class DesignScreen {

    private static int designWidth = -1;
    private static int designHeight = -1;
    private static int widthPixels;
    private static int heightPixels;
    private static float density;
    private static float scaledDensity;

    static void init(Context context) {
        try {
            String packageName = context.getPackageName();
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            designWidth = info.metaData.getInt("design_width_px", -1);
            designHeight = info.metaData.getInt("design_height_px", -1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (designWidth == -1 || designHeight == -1) {
            throw new IllegalArgumentException("you must config \"design_width_px\" and " +
                    "\"design_height_px\" in your androidManifest.xml");
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        density = displayMetrics.density;
        scaledDensity = displayMetrics.scaledDensity;
    }

    public static int getNewPxForDp(int curPx) {
        float dp = curPx * 1.0f / density;
        float newDensity = widthPixels * 1.0f / designWidth;
        return (int) (newDensity * dp);
    }

    public static int getNewPxForSp(float curSp) {
        float sp = curSp * 1.0f / scaledDensity;
        float ratio = scaledDensity / density;
        float newDensity = widthPixels * 1.0f / designWidth;
        float newScaledDensity = ratio * newDensity;
        return (int) (newScaledDensity * sp);
    }

}
