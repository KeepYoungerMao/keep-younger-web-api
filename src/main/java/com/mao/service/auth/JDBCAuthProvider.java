package com.mao.service.auth;

import com.mao.config.MybatisConfig;
import com.mao.entity.sys.Permission;
import com.mao.entity.sys.SysUser;
import com.mao.mapper.sys.UserMapper;
import com.mao.util.SU;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author : create by zongx at 2020/4/10 17:53
 */
public class JDBCAuthProvider implements AuthProvider {

    @Override
    public void authenticate(JsonObject info, Handler<AsyncResult<User>> handler) {
        String username = info.getString(AuthService.USERNAME);
        String password = info.getString(AuthService.PASSWORD);
        if (SU.isEmpty(username) || SU.isEmpty(password))
            handler.handle(Future.failedFuture(new IllegalArgumentException("authenticate failed. loss param username or password")));
        else {
            SqlSession session = MybatisConfig.getSession();
            UserMapper mapper = session.getMapper(UserMapper.class);
            SysUser sysUser = mapper.getUserByName(username);
            if (null == sysUser){
                session.close();
                handler.handle(Future.failedFuture(new IllegalArgumentException("error username")));
            } else if (!password.equals(sysUser.getPassword())){
                session.close();
                System.out.println("password error");
                handler.handle(Future.failedFuture(new IllegalArgumentException("error password")));
            } else {
                List<Permission> permissions = mapper.getPermissionByRoleId(sysUser.getRole_id());
                sysUser.setPermissions(permissions);
                session.close();
                handler.handle(Future.succeededFuture(sysUser));
            }
        }
    }

}
