package com.mao.service;

import com.mao.MainVerticle;
import com.mao.config.cache.LoginClientCache;
import com.mao.entity.response.Response;
import com.mao.entity.sys.Client;
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

    public static void filter(RoutingContext ctx){
        String path = ctx.request().path();
        ctx.response().putHeader(CONTENT_TYPE,CONTENT_TYPE_NAME);
        boolean has = true;
        for (String p : MainVerticle.FILTER_PATH) {
            if (path.startsWith(p)){
                String authorization = ctx.request().getHeader("Authorization");
                if (SU.isEmpty(authorization)){
                    authorization = ctx.request().getParam("access_token");
                }
                if (SU.isEmpty(authorization))
                    ctx.response().end(Response.error("request resource need authorization param",path));
                else {
                    Client client = LoginClientCache.getClient(authorization);
                    String s = checkClient(client,false,true);
                    if (null != s)
                        ctx.response().end(Response.error(s,path));
                    else
                        ctx.next();
                }
                has = false;
                break;
            }
        }
        if (has)
            ctx.next();
    }

    public static String checkClient(Client client, boolean first, boolean inUse){
        if (null == client)
            return first ? "invalid param client_id" : "invalid param authorization";
        if (client.getLocked())
            return "client cannot use because of locked";
        if (client.getExpired())
            return "client cannot use because of expired";
        if (!client.getEnabled())
            return "old client. cannot use";
        if (inUse && System.currentTimeMillis() >= client.getExpire_time())
            return "access token expired. please request a new one";
        return null;
    }

    /**
     * 静态资源系统路径前缀
     */
    private static final String SRC_FILE_PRE = "D:";

    /**
     * 静态资源：图片资源转发
     */
    public static void image(RoutingContext ctx){
        String path = ctx.request().path();
        path = path.substring(5);
        ctx.response().sendFile(SRC_FILE_PRE + path);
    }

}
