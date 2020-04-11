package com.mao.entity.sys;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户
 * @author zongx at 2020/4/10 21:06
 */
@Getter
@Setter
public class SysUser implements User {

    private Long id;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean expired;
    private Boolean enabled;
    private Long role_id;

    private List<Permission> permissions;

    @Override
    public User isAuthorized(String authority, Handler<AsyncResult<Boolean>> handler) {
        boolean success = false;
        for (Permission p : permissions) {
            if (p.getName().equals(authority)){
                success = true;
                break;
            }
        }
        handler.handle(Future.succeededFuture(success));
        return this;
    }

    @Override
    public User clearCache() {
        return this;
    }

    @Override
    public JsonObject principal() {
        return new JsonObject().put("username",username);
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }

}
