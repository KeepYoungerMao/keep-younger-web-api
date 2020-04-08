package com.mao.entity;

import java.util.Map;

/**
 * 服务器信息
 * @author zongx at 2020/4/7 22:28
 */
public class Server {

    public final String name;
    public final String link;
    public final String status;
    public final String version;
    public final String description;
    public final long start;

    public Server(Map<String, String> properties){
        this.name = properties.get("server.name");
        this.link = properties.get("server.link");
        this.status = properties.get("ok");
        this.version = properties.get("server.version");
        this.description = properties.get("server.description");
        this.start = System.currentTimeMillis();
    }

}
