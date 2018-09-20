package com.example.hua.framework.wrapper.adapt;

/**
 * @author hua
 * @version 2018/9/20 9:08
 */

public interface IDisplayMetricsFactory {
    /**
     * 创建DisplayMetricsInfo对象
     *
     * @param origin       origin DisplayMetricsInfo
     * @param metaDataInfo manifest中配置的meta-data信息
     * @return new DisplayMetricsInfo instance
     */
    DisplayMetricsInfo create(DisplayMetricsInfo origin, MetaDataInfo metaDataInfo);
}
