package com.mao.service.auth;

import io.vertx.ext.web.RoutingContext;

/**
 * 认证
 * @author : create by zongx at 2020/4/10 14:43
 */
public class AuthService {

    public static void authorize(RoutingContext ctx){
        ctx.response().end();
    }

}
