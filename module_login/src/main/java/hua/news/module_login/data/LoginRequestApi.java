package hua.news.module_login.data;

import hua.news.module_service.entitys.LoginUserInfo;
import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * 登录请求Retrofit api 接口
 *
 * @author hua
 * @date 2017/8/11
 */

public interface LoginRequestApi {

    /**
     * 发送手机号短信验证码
     *
     * @param phone 手机号
     * @return 服务器响应流
     */
    @GET
    Flowable<ResponseBody> sendVerifyCode(@Header("phone") String phone);

    /**
     * 校验短信验证码是否正确
     *
     * @param code 短信验证码
     * @return 服务器响应流
     */
    @GET
    Flowable<ResponseBody> checkVerifyCode(@Header("code") String code);

    /**
     * 注册账号
     *
     * @param function 接口名称
     * @param mail     邮箱
     * @param phone    手机号
     * @param pwd      密码
     * @return 服务器响应流
     */
    @GET("{function}")
    Flowable<ResponseBody> register(@Path("function") String function,
                                    @Header("mail") String mail,
                                    @Header("phone") String phone,
                                    @Header("password") String pwd);

    /**
     * 账号登录
     *
     * @param function  接口名称
     * @param account   账号（可以是手机号、邮箱），第三方登录时传平台唯一码
     * @param pwd       密码（第三方登录时传平台名称
     *                  sina 新浪微博
     *                  qq QQ
     *                  weixin 微信）
     * @param loginType 登录类型
     *                  201 表示手机号登录
     *                  202 表示邮箱登录
     *                  203 表示第三方登录
     * @return 服务器响应流
     */
    @GET("{function}")
    Flowable<LoginUserInfo> login(@Path("function") String function,
                                  @Header("account") String account,
                                  @Header("password") String pwd,
                                  @Header("login_type") String loginType);

    /**
     * 检查账号是否已经注册过
     *
     * @param function 接口名称
     * @param account
     * @param type     类型
     *                 201 表示判断手机号
     *                 202 表示判断邮箱
     * @return 服务器响应流
     */
    @GET("{function}")
    Flowable<ResponseBody> checkRegister(@Path("function") String function,
                                         @Header("account") String account,
                                         @Header("type") String type);

    /**
     * 请求用户信息
     *
     * @param function 接口名称
     * @param userId   用户编号
     * @return 服务器响应流
     */
    @GET("{function}")
    Flowable<ResponseBody> requestUserInfo(@Path("function") String function,
                                           @Header("user_id") String userId);


    /**
     * 修改用户昵称
     *
     * @param function 接口名称
     * @param userId   用户唯一编号
     * @param nickName 新的昵称
     * @return 服务器响应流
     */
    @GET("{function}")
    Flowable<ResponseBody> modifyNickName(@Path("function") String function,
                                          @Header("user_id") String userId,
                                          @Header("nick_name") String nickName);


    /**
     * 修改用户头像
     *
     * @param function   接口名称
     * @param userId     用户id
     * @param avatarBody 是需要上传的头像的MultipartBody
     * @return 服务器响应流
     */
    @Multipart
    @POST("{function}")
    Flowable<ResponseBody> modifyAvatar(@Path("function") String function,
                                        @Header("user_id") String userId,
                                        @Part("avatar") MultipartBody avatarBody);

}
