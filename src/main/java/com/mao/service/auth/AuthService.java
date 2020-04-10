package com.mao.service.auth;

import com.mao.entity.response.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 认证
 * @author : create by zongx at 2020/4/10 14:43
 */
public class AuthService {

    private static final AuthProvider provider = new JDBCAuthProvider();
    public static final Map<String, Account> USERS = new HashMap<>();

    public static void token(RoutingContext ctx){
        String username = ctx.request().getParam("username");
        String password = ctx.request().getParam("password");
        Account account = getUserByName(username);
        USERS.put(username,account);
        ctx.response().end(Response.ok(account));
    }

    public static void flush(RoutingContext ctx){
        String username = ctx.request().getParam("username");
        JsonObject jsonObject = new JsonObject().put("username",username);
        provider.authenticate(jsonObject, handler -> {
            if (handler.succeeded())
                ctx.response().end(Response.ok(handler.result()));
            else
                ctx.response().end(Response.error("authenticate failed"));
        });
    }

    public static Account getUserByName(String name){
        Account account = new Account();
        account.setUsername(name);
        account.setPassword("admin");
        account.setAuthorities(new HashSet<String>(){{this.add("a");this.add("b");}});
        return account;
    }

}
