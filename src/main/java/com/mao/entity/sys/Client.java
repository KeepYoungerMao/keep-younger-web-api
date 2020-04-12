package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 客户端
 * @author zongx at 2020/4/11 11:52
 */
@Getter
@Setter
public class Client {

    private String client_id;                   //客户端id
    private String client_secret;               //客户端密钥
    private Boolean locked;                     //是否锁定
    private Boolean expired;                    //是否过期
    private Boolean enabled;                    //是否正常使用
    private Long role_id;                       //角色id

    private List<Permission> permissions;       //权限列表

    private long expire_time;                   //过期时间戳

}
