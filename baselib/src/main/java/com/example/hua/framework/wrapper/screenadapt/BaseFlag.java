package com.example.hua.framework.wrapper.adapt;

import android.support.annotation.Nullable;

/**
 * DP和SP单位可以分别设置适配维度。
 * 默认DP单位以宽为维度，SP单位以高为维度。
 *
 * @author hua
 * @version 2018/9/20 10:18
 */

public enum BaseFlag {
    DP_WIDTH_SP_WIDTH,
    DP_WIDTH_SP_HEIGHT,
    DP_HEIGHT_SP_WIDTH,
    DP_HEIGHT_SP_HEIGHT;

    static @Nullable
    BaseFlag get(int value) {
        switch (value) {
            case 1:
                return DP_WIDTH_SP_WIDTH;
            case 2:
                return DP_WIDTH_SP_HEIGHT;
            case 3:
                return DP_HEIGHT_SP_WIDTH;
            case 4:
                return DP_HEIGHT_SP_HEIGHT;
            default:
                break;
        }
        return null;
    }
}
