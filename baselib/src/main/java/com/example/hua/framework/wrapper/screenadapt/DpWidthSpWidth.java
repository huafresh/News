package com.example.hua.framework.wrapper.adapt;

/**
 * @author hua
 * @version 2018/9/20 9:12
 */

class DpWidthSpWidth implements IDisplayMetricsFactory {

    @Override
    public DisplayMetricsInfo create(DisplayMetricsInfo origin, MetaDataInfo metaDataInfo) {
        DisplayMetricsInfo result = new DisplayMetricsInfo();
        result.density = origin.widthPixels * 1.0f / metaDataInfo.designWidth;
        result.densityDpi = (int) (result.density * 160);
        float ratio = origin.scaledDensity * 1.0f / origin.density;
        result.scaledDensity = ratio * result.density;
        return result;
    }
}
