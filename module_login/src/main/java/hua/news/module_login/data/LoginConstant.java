package hua.news.module_login.data;

import hua.news.module_common.utils.ConfigManager;

/**
 * 登录模块常量
 *
 * @author hua
 * @date 2017/6/28
 */

public class LoginConstant {

    /**
     * Provider接口参数的key，必须使用以下key来传递参数。
     */
    public static final String PARAMS_KEY_ACCOUNT = "account";
    public static final String PARAMS_KEY_PHONE = "phone";
    public static final String PARAMS_KEY_MAIL = "mail";
    public static final String PARAMS_KEY_PASSWORD = "password";
    public static final String PARAMS_KEY_LOGIN_TYPE = "login_type";
    public static final String PARAMS_KEY_USER_ID = "user_id";
    public static final String PARAMS_KEY_NICK_NAME = "nick_name";

    /**
     * provider中用户表的列名
     */
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NICK_NAME = "nick_name";
    public static final String COLUMN_BIND_PHONE = "bind_phone";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_THIRD_PLAT = "third_plat";
    public static final String COLUMN_THIRD_UNIQUE_ID = "third_unique_id";

    /**
     * provider中错误信息表的列名
     */
    //接口的path，唯一标识接口
    public static final String COLUMN_ERROR_PATH = "error_path";
    public static final String COLUMN_ERROR_INFO = "error_info";


    /**
     * 登录类型常量定义
     */
    public static final String LOGIN_TYPE_PHONE = "201";
    public static final String LOGIN_TYPE_MAIL= "202";
    public static final String LOGIN_TYPE_THIRD= "203";

    /**
     * 第三方平台名称
     */
    public static final String LOGIN_THIRD_SINA = "sina";
    public static final String LOGIN_THIRD_QQ = "qq";
    public static final String LOGIN_THIRD_WECHAT = "weixin";

    /**
     * user id保存key
     */
    public static final String KEY_USER_ID = "key_user_id";

    /**
     * 最后一次登录账号保存key
     */
    public static final String KEY_LAST_LOGIN_ACCOUNT = "key_last_login_account";


    public static final String URL_BASE =
            ConfigManager.getInstance().getItemConfigValue("URL_BASE");

}
