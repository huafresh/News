package com.example.hua.framework.wrapper.screenadapt;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.hua.framework.R;


/**
 * @author hua
 * @version 2018/9/19 18:47
 */

public class AutoAdaptAttrInfo {

    public boolean disable;
    public BaseFlag baseFlag;

    static AutoAdaptAttrInfo inflateAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoAdapt);
        boolean disable = ta.getBoolean(R.styleable.AutoAdapt_disable, false);
        int baseFlag = ta.getInt(R.styleable.AutoAdapt_baseFlag, -1);
        ta.recycle();

        AutoAdaptAttrInfo attrInfo = new AutoAdaptAttrInfo();
        attrInfo.disable = disable;
        attrInfo.baseFlag = BaseFlag.get(baseFlag);

        return attrInfo;
    }

}
