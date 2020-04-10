package com.mao.service.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author : create by zongx at 2020/4/10 18:01
 */
@Getter
@Setter
public class Account implements User {

    private String username;
    private String password;
    private Set<String> authorities;

    @Override
    public User isAuthorized(String authority, Handler<AsyncResult<Boolean>> handler) {
        boolean success = false;
        for (String s : authorities) {
            if (s.equals(authority)){
                success = true;
                break;
            }
        }
        handler.handle(Future.succeededFuture(success));
        return this;
    }

    @Override
    public User clearCache() {
        AuthService.USERS.remove(this.username);
        return this;
    }

    @Override
    public JsonObject principal() {
        return new JsonObject().put("user",this);
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }

}
