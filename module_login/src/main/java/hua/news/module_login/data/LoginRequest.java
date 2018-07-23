package hua.news.module_login.data;

import com.example.hua.framework.mvpbase.CallBack;

/**
 * 登录请求接口
 *
 * @author hua
 * @date 2017/6/28
 */

public interface LoginRequest {

    /**
     * 发送手机号短信验证码
     *
     * @param phone    手机号
     * @param callBack 回调
     */
    void sendVerifyCode(String phone, CallBack<?> callBack);

    /**
     * 校验短信验证码是否正确
     *
     * @param code     短信验证码
     * @param callBack 回调
     */
    void CheckVerifyCode(String code, CallBack<?> callBack);

    /**
     * 注册账号
     *
     * @param mail     邮箱
     * @param phone    手机号，可以为null
     * @param pwd      密码
     * @param callBack 回调
     */
    void register(String mail, String phone, String pwd, CallBack<?> callBack);

    /**
     * 账号登录
     *
     * @param account   账号（可以是手机号、邮箱），第三方登录时传平台唯一码
     * @param pwd       密码（第三方登录时传平台名称
     *                  sina 新浪微博
     *                  qq QQ
     *                  weixin 微信）
     * @param loginType 登录类型
     *                  201 表示手机号登录
     *                  202 表示邮箱登录
     *                  203 表示第三方登录
     * @param callBack  回调
     */
    void login(String account, String pwd, String loginType, CallBack<?> callBack);

    /**
     * 自动登录
     *
     * @param userId
     * @param callBack
     */
    void autoLogin(String userId, CallBack<?> callBack);

    /**
     * 检查账号是否已经注册过
     *
     * @param account
     * @param type     类型
     *                 201 表示判断手机号
     *                 202 表示判断邮箱
     * @param callBack
     */
    void checkRegister(String account, String type, CallBack<?> callBack);

    /**
     * 请求用户信息
     *
     * @param userId   用户唯一编号
     * @param callBack 回调
     */
    void requestUserInfo(String userId, CallBack<?> callBack);

    /**
     * 修改用户昵称
     *
     * @param userId   用户唯一编号
     * @param nickName 新的昵称
     * @param callBack 回调
     */
    void modifyNickName(String userId, String nickName, CallBack<?> callBack);


    /**
     * 修改用户头像
     *
     * @param userId   用户唯一编号
     * @param path     头像存储路径
     * @param callBack 回调
     */
    void modifyAvatar(String userId, String path, CallBack<?> callBack);

}
