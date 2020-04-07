package com.mao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 服务器信息
 * @author zongx at 2020/4/7 22:28
 */
@Getter
@Setter
@NoArgsConstructor
public class Server {

    private final String name = "keep-younger-web-api";
    private final String link = "http://www.keepyounger.top";
    private final String status = "ok";
    private final String version = "1.0.0";
    private final String description = "vert.X web application for keep younger";
    private final long start = System.currentTimeMillis();

}
