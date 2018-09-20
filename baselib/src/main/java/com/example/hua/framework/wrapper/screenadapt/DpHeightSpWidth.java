package com.example.hua.framework.wrapper.screenadapt;

/**
 * @author hua
 * @version 2018/9/20 9:12
 */

 class DpHeightSpWidth implements IDisplayMetricsFactory {

    @Override
    public DisplayMetricsInfo create(DisplayMetricsInfo origin, MetaDataInfo metaDataInfo) {
        DisplayMetricsInfo result = new DisplayMetricsInfo();
        result.density = origin.heightPixels * 1.0f / metaDataInfo.designHeight;
        result.densityDpi = (int) (result.density * 160);
        float ratio = origin.scaledDensity * 1.0f / origin.density;
        result.scaledDensity = ratio * (origin.widthPixels * 1.0f / metaDataInfo.designWidth);
        return result;
    }
}
