package com.mao.entity;

import lombok.Getter;

import java.util.Map;

/**
 * 服务器信息
 * @author zongx at 2020/4/7 22:28
 */
@Getter
public class Server {

    private final String name;
    private final String link;
    private final String status;
    private final String version;
    private final String description;
    private final long start;

    public Server(Map<String, String> properties){
        this.name = properties.get("server.name");
        this.link = properties.get("server.link");
        this.status = "ok";
        this.version = properties.get("server.version");
        this.description = properties.get("server.description");
        this.start = System.currentTimeMillis();
    }

}
