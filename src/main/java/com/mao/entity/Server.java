package com.mao.entity;

import lombok.Getter;

import java.util.Properties;

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
    private final String ip;
    private final int port;
    private final long start;

    public Server(Properties properties) throws IllegalArgumentException{
        this.name = properties.getProperty("server.name");
        this.link = properties.getProperty("server.link");
        this.status = "ok";
        this.version = properties.getProperty("server.version");
        this.description = properties.getProperty("server.description");
        this.ip = properties.getProperty("server.ip");
        this.port = Integer.parseInt(properties.getProperty("server.port"));
        this.start = System.currentTimeMillis();
    }

}
