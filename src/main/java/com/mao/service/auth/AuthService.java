package com.mao.service.auth;

import io.vertx.ext.web.RoutingContext;

/**
 * 授权服务
 * @author : create by zongx at 2020/5/20 14:36
 */
public interface AuthService {

    static AuthService created(){
        return new AuthServiceImpl();
    }

    /**
     * 授权拦截处理
     */
    void filter(RoutingContext ctx);

    /**
     * token获取
     */
    void authorize(RoutingContext ctx);

    /**
     * token刷新
     */
    void refresh(RoutingContext ctx);

}
