package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zongx at 2020/4/11 11:42
 */
@Getter
@Setter
public class SysUserSrc {

    private Long id;
    private String full_name;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean expired;
    private Boolean enabled;
    private Long role_id;
    private Long role_name;
    private String company;
    private String dept;
    private String note;
    private String image;
    private String idcard;
    private String address;
    private String qq;
    private String wx;
    private String phone;
    private String email;

}
