package hua.news.module_common.constants;

/**
 * 一般通用常量
 *
 * @author hua
 * @date 2017/6/11
 */

public class NewsConstant {

    /**
     * 新闻的类型
     */
    public static final int NEWS_TYPE_PICUTRE = 0;
    public static final int NEWS_TYPE_VIDEO = 1;
    public static final int NEWS_TYPE_LIVE = 2;


    //------------------持久缓存数据key开始-------------------//


    /**
     * 视频列表最后一次最小时间，毫秒字符串
     */
    public static final String KEY_VIDEO_LIST_MIN_TIME = "key_video_list_min_time";
    /**
     * 视频列表最后一次最小时间，毫秒字符串
     */
    public static final String KEY_VIDEO_LIST_MAX_TIME = "key_video_list_max_time";

    //------------------持久缓存数据key结束-------------------//

    /**
     * 头像图片存储相对路径
     */
    public static final String AVATAR_LOCAL_PATH = "/files/avatar/avatar.png";

    /**
     * configuration.xml文件相关配置项常量
     */





    /**
     * 频道名称
     */
    public static final String CHANNEL_MAIN = "main";
    public static final String CHANNEL_FUN = "fun";
    public static final String CHANNEL_FINANCE = "finance";
    public static final String CHANNEL_SOCIETY = "society";
    public static final String CHANNEL_NBA = "NBA";
    public static final String CHANNEL_HISTORY = "history";
    public static final String CHANNEL_SPORT = "sport";

    /**
     * 新闻列表展示类型
     */
    public static final int ITEM_TYPE_ONE = 1; //一图+文字
    public static final int ITEM_TYPE_THREE = 2; //三图+文字
    public static final int ITEM_TYPE_ONE_BIG = 3; //一大图+文字

    /**
     * 打开新闻详情页面传递数据所使用的key
     */
    public static final String KEY_BUNDLE_NEWS_ID = "news_id";
    public static final String KEY_BUNDLE_SHOW_TYPE = "show_type";
    public static final String KEY_BUNDLE_FOLLOW_SUM = "follow_sum";
    public static final String KEY_BUNDLE_DATA = "data";


    /**
     * 新闻详情页面html模板的存储位置
     */
    public static final String DETAIL_HTML_PATH = "detail.html";


    //--------------本地广播action开始-----------------//


    //--------------本地广播action结束-----------------//


}
