package com.mao.service;

import com.mao.MainVerticle;
import com.mao.entity.response.Response;
import com.mao.service.auth.AuthService;
import com.mao.util.SU;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * 基本请求处理
 * @author zongx at 2020/4/7 21:54
 */
public class MainService {

    private static final Logger log = LoggerFactory.getLogger(MainService.class);

    //所有接口统一发送形式
    public static final String CONTENT_TYPE = "Content-type";
    public static final String CONTENT_TYPE_NAME = "application/json;charset=utf-8";

    public static void index(RoutingContext ctx){
        log.info("load server message");
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

    /**
     * 拦截器
     * 1.为所有请求添加 application/json 返回格式
     * 2.授权拦截
     */
    public static void filter(RoutingContext ctx){
        ctx.response().putHeader(CONTENT_TYPE,CONTENT_TYPE_NAME);
        if (MainVerticle.server.isNeedAuthorize())
            AuthService.authorization(ctx.request(), handler -> {
                if (handler.failed())
                    ctx.response().end(Response.error(handler.cause().getMessage(),ctx.request().path()));
                else
                    ctx.next();
            });
        else
            ctx.next();
    }

    /**
     * 静态资源：图片资源转发
     */
    public static void image(RoutingContext ctx){
        String path = ctx.request().path();
        path = path.substring(5);
        ctx.response().sendFile(MainVerticle.IMAGE_FILE_LOCAL_PATH_PRE + path);
    }

}
