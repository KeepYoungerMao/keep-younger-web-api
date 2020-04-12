package com.mao.service.auth;

import com.mao.config.MybatisConfig;
import com.mao.config.cache.LoginClientCache;
import com.mao.entity.response.Response;
import com.mao.entity.response.Token;
import com.mao.entity.sys.Client;
import com.mao.entity.sys.Permission;
import com.mao.mapper.sys.UserMapper;
import com.mao.service.MainService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 认证
 * @author : create by zongx at 2020/4/10 14:43
 */
public class AuthService {

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";

    public static void authorize(RoutingContext ctx){
        String client_id = ctx.request().getParam(CLIENT_ID);
        String client_secret = ctx.request().getParam(CLIENT_SECRET);
        String path = ctx.request().path();
        if (SU.isEmpty(client_id))
            ctx.response().end(Response.error("needed param client_id",path));
        else if (SU.isEmpty(client_secret))
            ctx.response().end(Response.error("need param client_secret",path));
        else {
            SqlSession session = MybatisConfig.getSession();
            UserMapper mapper = session.getMapper(UserMapper.class);
            Client client = mapper.getClientById(client_id);
            String s = MainService.checkClient(client, true, false);
            if (null != s) {
                session.close();
                ctx.response().end(Response.error(s,path));
            } else if (!client_secret.equals(client.getClient_secret())) {
                session.close();
                ctx.response().end(Response.error("invalid param client_secret",path));
            } else {
                List<Permission> permissions = mapper.getPermissionByRoleId(client.getRole_id());
                session.close();
                client.setPermissions(permissions);
                Token token = new Token(SU.uuid(),SU.uuid());
                LoginClientCache.save(token,client);
                ctx.response().end(Response.ok(token));
            }
        }
    }

    public static void refresh(RoutingContext ctx){
        String refresh_token = ctx.request().getParam("refresh_token");
        String path = ctx.request().path();
        if (SU.isEmpty(refresh_token))
            ctx.response().end(Response.error("need param refresh_token",path));
        else {
            Token token = LoginClientCache.refresh(refresh_token);
            if (null == token)
                ctx.response().end(Response.error("refresh failed. please request a new token",path));
            else
                ctx.response().end(Response.ok(token));
        }
    }

}
