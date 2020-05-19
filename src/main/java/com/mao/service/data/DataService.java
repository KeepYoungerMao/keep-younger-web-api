package com.mao.service.data;

import io.vertx.ext.web.RoutingContext;

/**
 * 数据请求处理
 * @author : create by zongx at 2020/5/19 18:15
 */
public interface DataService {

    /**
     * 创建数据处理类
     * @return DataServiceImpl
     */
    static DataService created(){
        return new DataServiceImpl();
    }

    /**
     * 数据列表的查询
     * @param ctx 上下文
     */
    void searchList(RoutingContext ctx);

    /**
     * 数据详情的查询
     * @param ctx 上下文
     */
    void searchItem(RoutingContext ctx);

}
