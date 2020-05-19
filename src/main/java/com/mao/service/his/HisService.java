package com.mao.service.his;

import io.vertx.ext.web.RoutingContext;

/**
 * 接口调用处理类
 * @author : create by zongx at 2020/5/19 18:21
 */
public interface HisService {

    /**
     * 创建接口调用类
     * @return HisServiceImpl
     */
    static HisService created(){
        return new HisServiceImpl();
    }

    /**
     * 根据用户所在地获取用网ip
     * @param ctx 啥下文
     */
    void addressIp(RoutingContext ctx);

    /**
     * 获取天气信息
     * @param ctx 上下文
     */
    void weather(RoutingContext ctx);

    /**
     * 解析数独
     * @param ctx 上下文
     */
    void sudoKu(RoutingContext ctx);

}
