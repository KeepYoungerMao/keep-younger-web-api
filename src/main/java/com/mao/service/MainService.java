package com.mao.service;

import com.mao.MainVerticle;
import com.mao.entity.response.Response;
import com.mao.service.auth.AuthService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;

/**
 * 基本请求处理
 * @author zongx at 2020/4/7 21:54
 */
public class MainService {

    /**
     * 所有接口统一发送形式
     */
    public static final String CONTENT_TYPE = "Content-type";
    public static final String CONTENT_TYPE_NAME = "application/json;charset=utf-8";

    /**
     * 首页返回服务器信息
     */
    public void index(RoutingContext ctx){
        ctx.response().end(Response.ok(MainVerticle.server));
    }

    /**
     * 401错误捕捉
     */
    public void permission(RoutingContext ctx){
        ctx.response().end(Response.permission());
    }

    /**
     * 404错误捕捉
     */
    public void notFound(RoutingContext ctx){
        ctx.response().end(Response.notFound());
    }

    /**
     * 405错误捕捉
     */
    public void notAllowed(RoutingContext ctx){
        ctx.response().end(Response.notAllowed());
    }

    /**
     * 500错误捕捉
     */
    public void error(RoutingContext ctx){
        //错误打印
        ctx.failure().printStackTrace();
        String message = ctx.failure().getMessage();
        ctx.response().end(SU.isEmpty(message) ? Response.error() : Response.error(message));
    }

    /**
     * 拦截器
     * 1.为所有请求添加 application/json 返回格式
     * 2.授权拦截
     */
    public void filter(RoutingContext ctx){
        ctx.response().putHeader(CONTENT_TYPE,CONTENT_TYPE_NAME);
        if (MainVerticle.server.isNeedAuthorize())
            AuthService.authorization(ctx.request(), handler -> {
                if (handler.failed())
                    ctx.response().end(Response.error(handler.cause().getMessage()));
                else
                    ctx.next();
            });
        else
            ctx.next();
    }

    /**
     * 静态资源：图片资源转发
     */
    public void image(RoutingContext ctx){
        String path = ctx.request().path();
        path = path.substring(5);
        ctx.response().sendFile(MainVerticle.IMAGE_FILE_LOCAL_PATH_PRE + path);
    }

}
