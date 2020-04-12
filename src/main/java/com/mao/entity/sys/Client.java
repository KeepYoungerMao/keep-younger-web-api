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

    private String client_id;                   //
    private String client_secret;               //
    private Boolean locked;
    private Boolean expired;
    private Boolean enabled;
    private Long role_id;

    private List<Permission> permissions;

}
