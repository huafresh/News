package com.example.hua.framework.tools.avatar;

import android.content.Intent;

/**
 * 裁剪结束回调
 *
 * @author hua
 * @date 2017/7/31
 */

public interface OnCropOverListener {
    /**
     * 裁剪结束时调用
     *
     * @param resultCode 结果码
     * @param data       返回的数据
     */
    void onCropOver(int resultCode, Intent data);
}
