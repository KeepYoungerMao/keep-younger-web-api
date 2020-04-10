package com.mao.service.auth;

import com.mao.entity.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;

/**
 * 认证
 * @author : create by zongx at 2020/4/10 14:43
 */
public class AuthService {

    private static final AuthProvider provider = new JDBCAuthProvider();
    static final String USERNAME = "username";
    static final String PASSWORD = "password";

    public static void token(RoutingContext ctx){
        JsonObject data = new JsonObject()
                .put(USERNAME,ctx.request().getParam(USERNAME))
                .put(PASSWORD,ctx.request().getParam(PASSWORD));
        provider.authenticate(data, handler -> {
            if (handler.succeeded()){
                User user = handler.result();
                ctx.setUser(user);
                ctx.response().end(Response.ok(user));
            } else {
                ctx.response().end(Response.error(handler.cause().getMessage()));
            }
        });
    }

    public static void flush(RoutingContext ctx){
        ctx.response().end(Response.ok(ctx.user()));
    }

}
