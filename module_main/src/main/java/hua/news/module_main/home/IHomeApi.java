package hua.news.module_main.home;


import java.util.List;

import hua.news.module_service.entitys.ColumnEntity;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * 请求栏目数据接口
 * <p>
 * Created by hua on 2017/10/2.
 */

interface IHomeApi {

    /**
     * 首页栏目操作统一接口
     *
     * @param function 唯一标识接口的后缀
     * @param userId   用户id。如果未登录则传空，那么返回的就是默认的栏目列表
     * @param type     接口请求类型。可取值：
     *                 {@link PageHome#COLUMN_TYPE_QUERY} 查询栏目列表
     *                 {@link PageHome#COLUMN_TYPE_ADD} 添加栏目（即使该栏目能在首页中显示）
     *                 {@link PageHome#COLUMN_TYPE_REMOVE} 移除栏目
     * @param columnId 栏目id。如果是查询栏目信息，可传空
     * @return 服务器返回的结果
     */
    @GET("{function}")
    Flowable<List<ColumnEntity>> getHomeColumnList(
            @Path("function") String function,
            @Header("user_id") String userId,
            @Header("type") String type,
            @Header("column_id") String columnId);


}
