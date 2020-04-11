package com.mao.entity.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zongx at 2020/4/11 11:46
 */
@Getter
@Setter
public class Token {
    private String access_token;
    private String refresh_token;
    private int expire;
    private long timestamp;

    public Token(String access_token, String refresh_token){
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expire = 7200;
        this.timestamp = System.currentTimeMillis();
    }
}
