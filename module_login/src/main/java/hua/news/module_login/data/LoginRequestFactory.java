package hua.news.module_login.data;

/**
 * 控制请求接口不同的实现，以此达到解耦的目的
 *
 * @author hua
 * @date 2017/8/8
 */

public class LoginRequestFactory {

    public static LoginRequest getInstance(String type){
        return LoginRequestHttp.getInstance();
    }

    public static LoginRequest getInstance(){
        return LoginRequestHttp.getInstance();
    }

}
