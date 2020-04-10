package com.mao;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * debug主程序
 * 生产环境使用Vert.X官方推荐主程序
 * @author zongx at 2020/4/7 21:36
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
        log.info("service api started...");
    }

}
