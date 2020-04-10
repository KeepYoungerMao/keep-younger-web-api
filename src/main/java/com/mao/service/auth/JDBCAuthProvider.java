package com.mao.service.auth;

import com.mao.util.SU;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * @author : create by zongx at 2020/4/10 17:53
 */
public class JDBCAuthProvider implements AuthProvider {

    @Override
    public void authenticate(JsonObject info, Handler<AsyncResult<User>> handler) {
        String username = info.getString("username");
        if (SU.isEmpty(username))
            handler.handle(Future.failedFuture("authenticate failed. loss param username or password"));
        else {
            Account account = AuthService.USERS.get(username);
            handler.handle(Future.succeededFuture(account));
        }
    }

}
