package com.mao.entity.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端
 * @author zongx at 2020/4/11 11:52
 */
@Getter
@Setter
public class Client {

    private String client_id;                   //
    private String client_name;                 //
    private String client_secret;               //
    private String grant_type;                  //
    private String redirect_url;                //
    private String access_token_validity;       //
    private String refresh_token_validity;      //
    private String auto_approve;                //

}
