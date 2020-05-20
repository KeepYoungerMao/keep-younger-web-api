package com.mao.service.auth;

import com.mao.MainVerticle;
import com.mao.config.MybatisConfig;
import com.mao.config.cache.LoginClientCache;
import com.mao.entity.response.Response;
import com.mao.entity.response.Token;
import com.mao.entity.sys.Client;
import com.mao.entity.sys.Permission;
import com.mao.mapper.sys.UserMapper;
import com.mao.service.BaseService;
import com.mao.service.MainService;
import com.mao.util.SU;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 授权
 * @author : create by zongx at 2020/4/10 14:43
 */
public class AuthServiceImpl extends BaseService implements AuthService {

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    /**
     * token过滤
     * @param ctx 上下文
     */
    public void filter(RoutingContext ctx) {
        ctx.response().putHeader(MainService.CONTENT_TYPE,MainService.CONTENT_TYPE_NAME);
        if (MainVerticle.application.isNeed_authorize())
            authorization(ctx.request(), handler -> {
                if (handler.failed())
                    ctx.response().end(Response.error(handler.cause().getMessage()));
                else
                    ctx.next();
            });
        else
            ctx.next();
    }

    /**
     * token获取
     * 1.获取参数client_id和client_secret
     * 2.根据参数获取client数据
     * 3.client数据检查
     * 4.创建并返回token
     */
    public void authorize(RoutingContext ctx){
        String client_id = ctx.request().getParam(CLIENT_ID);
        String client_secret = ctx.request().getParam(CLIENT_SECRET);
        String data;
        if (SU.isEmpty(client_id))
            data = error("needed param client_id");
        else if (SU.isEmpty(client_secret))
            data = error("need param client_secret");
        else {
            SqlSession session = MybatisConfig.getSession();
            UserMapper mapper = session.getMapper(UserMapper.class);
            Client client = mapper.getClientById(client_id);
            String s = checkClient(client, true, false);
            if (null != s) {
                data = error(s);
            } else if (!client_secret.equals(client.getClient_secret())) {
                data = error("invalid param client_secret");
            } else {
                List<Permission> permissions = mapper.getPermissionByRoleId(client.getRole_id());
                client.setPermissions(permissions);
                Token token = new Token(SU.uuid(),SU.uuid());
                LoginClientCache.save(token,client);
                data = ok(token);
            }
            session.close();
        }
        ctx.response().write(data);
        ctx.next();
    }

    /**
     * token刷新
     */
    public void refresh(RoutingContext ctx){
        String refresh_token = ctx.request().getParam(REFRESH_TOKEN);
        String data;
        if (SU.isEmpty(refresh_token))
            data = error("need param refresh_token");
        else {
            Token token = refresh(refresh_token);
            data = null == token ? error("refresh failed. please request a new token") : ok(token);
        }
        ctx.response().write(data);
        ctx.next();
    }

    /**
     * 客户端参数的检查
     * @param client 客户端数据
     * @param first 是否是首次查询，false表示在授权验证使用中
     * @param inUse 是否是正在使用的数据，true表示在授权验证使用中
     * @return null / 错误提示
     */
    public String checkClient(Client client, boolean first, boolean inUse){
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
     * 1.获取需要授权的url列表
     * 2.获取客户端发送的验证token。可通过header发送Authorization，可通过链接发送access_token。优先header
     * 3.验证token是否正确
     * @param request HttpServerRequest
     * @param handler 处理
     */
    public void authorization(HttpServerRequest request, Handler<AsyncResult<Void>> handler){
        boolean send = false;
        String path = request.path();
        for (String p : MainVerticle.application.getAuthorize_path()) {
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

    /**
     * token的刷新
     * 1.获取到旧token
     * 2.根据access_token获取客户端数据
     * 3.新建token
     * 4.重新储存client数据和新token
     * 5.返回新token
     * 返回null表示刷新失败
     * @param refresh_token refresh_token
     * @return 新的token
     */
    private Token refresh(String refresh_token){
        Token oldToken = LoginClientCache.getToken(refresh_token);
        if (null == oldToken)
            return null;
        Client client = LoginClientCache.getClient(oldToken.getAccess_token());
        if (null == client)
            return null;
        String s = checkClient(client, false, false);
        if (null != s)
            return null;
        Token newToken = new Token(SU.uuid(),SU.uuid());
        LoginClientCache.save(newToken,client);
        return newToken;
    }

}
