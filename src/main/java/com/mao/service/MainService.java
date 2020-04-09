package com.mao.service;

import com.mao.MainVerticle;
import com.mao.entity.response.Response;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;

/**
 * 基本请求处理
 * @author zongx at 2020/4/7 21:54
 */
public class MainService {

    public static final String CONTENT_TYPE = "Content-type";
    public static final String CONTENT_TYPE_NAME = "application/json;charset=utf-8";

    public static void index(RoutingContext ctx){
        ctx.response().end(Response.ok(MainVerticle.server));
    }

    public static void permission(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(Response.permission(path));
    }

    public static void notFound(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(Response.notFound(path));
    }

    public static void notAllowed(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().end(Response.notAllowed(path));
    }

    public static void error(RoutingContext ctx){
        //错误打印
        ctx.failure().printStackTrace();
        String message = ctx.failure().getMessage();
        String path = ctx.request().path();
        ctx.response().end(SU.isEmpty(message) ? Response.error(path) : Response.error(message,path));
    }

    public static void filter(RoutingContext ctx){
        ctx.response().putHeader(CONTENT_TYPE,CONTENT_TYPE_NAME);
        ctx.next();
    }

    private static final String SRC_FILE_PRE = "D:";

    /**
     * 静态资源：图片资源转发
     */
    public static void image(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().sendFile(SRC_FILE_PRE + path);
    }

}
