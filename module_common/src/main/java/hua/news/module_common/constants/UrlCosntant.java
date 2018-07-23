package hua.news.module_common.constants;

import hua.news.module_common.utils.ConfigManager;

/**
 * @author hua
 * @version 2018/3/26 13:43
 */

public class UrlCosntant {

    /**
     * 本地主机地址
     */
    public static final String URL_BASE =
            ConfigManager.getInstance().getItemConfigValue("URL_BASE");

    /**
     * 获取栏目列表
     */
    public static final String URL_COLUMN_LIST =
            ConfigManager.getInstance().getItemConfigValue("URL_COLUMN_LIST");

    /**
     * 获取不同频道的新闻列表
     */
    public static final String URL_CHANNEL_NEWS_LIST =
            ConfigManager.getInstance().getItemConfigValue("URL_CHANNEL_NEWS_LIST");

    /**
     * 获取新闻详情
     */
    public static final String URL_NORMAL_NEWS_DETAIL =
            ConfigManager.getInstance().getItemConfigValue("URL_NORMAL_NEWS_DETAIL");

    /**
     * 添加收藏
     */
    public static final String URL_COLLECT_ADD =
            ConfigManager.getInstance().getItemConfigValue("URL_COLLECT_ADD");

    /**
     * 取消收藏
     */
    public static final String URL_COLLECT_CANCEL =
            ConfigManager.getInstance().getItemConfigValue("URL_COLLECT_CANCEL");

    /**
     * 查询收藏列表
     */
    public static final String URL_COLLECT_QUERY =
            ConfigManager.getInstance().getItemConfigValue("URL_COLLECT_QUERY");

    /**
     * 获取视频新闻列表
     */
    public static final String URL_VIDEO_LIST =
            ConfigManager.getInstance().getItemConfigValue("URL_VIDEO_LIST");

}
