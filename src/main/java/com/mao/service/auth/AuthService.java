package com.mao.service.auth;

import com.mao.MainVerticle;
import com.mao.config.MybatisConfig;
import com.mao.config.cache.LoginClientCache;
import com.mao.entity.response.Response;
import com.mao.entity.response.Token;
import com.mao.entity.sys.Client;
import com.mao.entity.sys.Permission;
import com.mao.mapper.sys.UserMapper;
import com.mao.util.SU;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
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
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    public void authorize(RoutingContext ctx){
        String client_id = ctx.request().getParam(CLIENT_ID);
        String client_secret = ctx.request().getParam(CLIENT_SECRET);
        if (SU.isEmpty(client_id))
            ctx.response().end(Response.error("needed param client_id"));
        else if (SU.isEmpty(client_secret))
            ctx.response().end(Response.error("need param client_secret"));
        else {
            SqlSession session = MybatisConfig.getSession();
            UserMapper mapper = session.getMapper(UserMapper.class);
            Client client = mapper.getClientById(client_id);
            String s = checkClient(client, true, false);
            if (null != s) {
                session.close();
                ctx.response().end(Response.error(s));
            } else if (!client_secret.equals(client.getClient_secret())) {
                session.close();
                ctx.response().end(Response.error("invalid param client_secret"));
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

    public void refresh(RoutingContext ctx){
        String refresh_token = ctx.request().getParam(REFRESH_TOKEN);
        if (SU.isEmpty(refresh_token))
            ctx.response().end(Response.error("need param refresh_token"));
        else {
            Token token = LoginClientCache.refresh(refresh_token);
            if (null == token)
                ctx.response().end(Response.error("refresh failed. please request a new token"));
            else
                ctx.response().end(Response.ok(token));
        }
    }

    /**
     * 客户端参数的检查
     * @param client 客户端数据
     * @param first 是否是首次查询，false表示在授权验证使用中
     * @param inUse 是否是正在使用的数据，true表示在授权验证使用中
     * @return null / 错误提示
     */
    public static String checkClient(Client client, boolean first, boolean inUse){
        if (null == client)
            return first ? "invalid param client_id" : "invalid param authorization";
        if (client.getLocked())
            return "client cannot use because of locked";
        if (client.getExpired())
            return "client cannot use because of expired";
        if (!client.getEnabled())
            return "client failure. cannot use";
        if (inUse && System.currentTimeMillis() >= client.getExpire_time())
            return "access token expired. please request a new one";
        return null;
    }

    /**
     * 授权方法
     * @param request HttpServerRequest
     * @param handler 处理
     */
    public static void authorization(HttpServerRequest request, Handler<AsyncResult<Void>> handler){
        boolean send = false;
        String path = request.path();
        for (String p : MainVerticle.FILTER_PATH) {
            if (SU.match(p,path)){
                String authorization = request.getHeader(AUTHORIZATION);
                if (SU.isEmpty(authorization))
                    authorization = request.getParam(ACCESS_TOKEN);
                if (SU.isEmpty(authorization))
                    handler.handle(Future.failedFuture("request resource need authorization param"));
                else {
                    Client client = LoginClientCache.getClient(authorization);
                    String s = checkClient(client,false,true);
                    if (null != s)
                        handler.handle(Future.failedFuture(s));
                    else
                        handler.handle(Future.succeededFuture());
                }
                send = true;
                break;
            }
        }
        if (!send)
            handler.handle(Future.succeededFuture());
    }

}
