package com.mao.service;

import com.mao.MainVerticle;
import com.mao.entity.response.ResponseData;
import io.vertx.ext.web.RoutingContext;

/**
 * 基本请求处理
 * @author zongx at 2020/4/7 21:54
 */
public class MainService {

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_NAME = "application/json;charset=utf-8";

    public static void index(RoutingContext ctx){
        ctx.response().end(ResponseData.ok(MainVerticle.server));
    }

    public static void permission(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(ResponseData.permission("",path));
    }

    public static void notFound(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(ResponseData.notFound(path));
    }

    public static void error(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(ResponseData.error("500 server error",path));
    }

    public static void filter(RoutingContext ctx){
        ctx.response().putHeader(CONTENT_TYPE,CONTENT_TYPE_NAME);
        ctx.next();
    }

}
