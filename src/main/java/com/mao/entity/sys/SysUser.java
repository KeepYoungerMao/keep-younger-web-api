package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户
 * @author zongx at 2020/4/10 21:06
 */
@Getter
@Setter
public class SysUser {

    private Long id;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean expired;
    private Boolean enabled;
    private Long role_id;

    private List<Permission> permissions;

}
