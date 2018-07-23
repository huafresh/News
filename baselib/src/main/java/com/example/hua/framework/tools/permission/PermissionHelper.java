package com.example.hua.framework.tools.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.0动态申请权限帮助类。
 * 使用方法：
 * 1、调用{@link #requestPermissions}系列方法申请所需权限
 * 2、重写activity的{@link Activity#onRequestPermissionsResult}方法，
 * 并调用此类的{@link #onRequestPermissionsResult}方法
 *
 * @author hua
 * @date 2017/7/31
 */

public class PermissionHelper {
    /** 请求码，依次递增 */
    private static int mRequestCode = 0;

    /** 保存权限申请回调 */
    private static SparseArray<PermissionListener> mPermissionListeners;

    /** 保存已被通过的权限 */
    private static SparseArray<List<String>> mGrantedArray;

    /** 保存未被通过的权限 */
    private static SparseArray<List<String>> mDeniedArray;

    private final Activity mActivity;


    private PermissionHelper(Activity activity) {
        mActivity = activity;
        if (mPermissionListeners == null) {
            mPermissionListeners = new SparseArray<>();
        }
        if (mGrantedArray == null) {
            mGrantedArray = new SparseArray<>();
        }
        if (mDeniedArray == null) {
            mDeniedArray = new SparseArray<>();
        }
    }


    public static PermissionHelper get(Activity activity){
        return new PermissionHelper(activity);
    }

    /**
     * 判断给定的权限是否被允许了
     *
     * @param permission 权限名称
     * @return 是否被允许
     */
    public boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请单个权限
     *
     * @param permission 权限名称
     */
    public void requestPermission(String permission) {
        requestPermissions(new String[]{permission});
    }

    /**
     * 申请单个权限
     *
     * @param permission 权限名称
     */
    public void requestPermission(String permission, PermissionListener listener) {
        requestPermissions(new String[]{permission}, listener);
    }

    /**
     * 申请多个权限
     *
     * @param permissions 权限名称
     */
    public void requestPermissions(String[] permissions) {
        requestPermissions(permissions, null);
    }


    /**
     * 申请多个权限
     *
     * @param permissions 权限名称
     * @param listener    权限申请结果回调
     */
    public synchronized void requestPermissions(String[] permissions, PermissionListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0才需要动态申请权限
            if (listener != null) {
                listener.onGranted(permissions);
            }
            return;
        }

        int code = mRequestCode++;
        List<String> grantedList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();

        for (String permission : permissions) {
            if (checkPermission(permission)) {
                //本来已经通过的权限无需再申请
                grantedList.add(permission);
            } else {
                deniedList.add(permission);
            }
        }

        //对未通过的权限进行动态申请
        if (deniedList.size() > 0) {
            ActivityCompat.requestPermissions(mActivity, deniedList.toArray(new String[]{}), code);
            mPermissionListeners.put(code, listener);
            mGrantedArray.put(code, grantedList);
            mDeniedArray.put(code, deniedList);
        } else {
            //没有需要申请的权限，直接回调
            if (listener != null) {
                listener.onGranted(grantedList.toArray(new String[]{}));
            }
        }
    }

    /**
     * Activity权限申请回调。
     * 调用此方法时无需对参数做其他处理
     *
     * @param requestCode  请求码
     * @param permissions  权限列表
     * @param grantResults 权限申请结果
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults) {
        //根据请求码取出权限列表和回调
        List<String> grantedList = mGrantedArray.get(requestCode);
        List<String> deniedList = mDeniedArray.get(requestCode);
        PermissionListener listener = mPermissionListeners.get(requestCode);

        //判断之前没允许的权限现在是否被允许了
        for (int j = 0; j < deniedList.size(); j++) {
            String permission = deniedList.get(j);
            for (int i = 0; i < permissions.length; i++) {
                if (permission.equals(permissions[i])) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        grantedList.add(permission);
                        deniedList.remove(permission);
                        j--;
                    }
                }
            }
        }

        //执行回调
        if (listener != null) {
            if (grantedList.size() > 0) {
                listener.onGranted(grantedList.toArray(new String[]{}));
            }
            if (deniedList.size() > 0) {
                listener.onDenied(deniedList.toArray(new String[]{}));
            }
        }

        //申请权限完毕，清空相应数据
        mPermissionListeners.remove(requestCode);
        mGrantedArray.remove(requestCode);
        mDeniedArray.remove(requestCode);
    }

}
