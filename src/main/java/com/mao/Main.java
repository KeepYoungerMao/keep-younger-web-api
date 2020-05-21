package com.mao;

import io.vertx.core.Vertx;

/**
 * debug主程序
 * 生产环境使用Vert.X官方推荐主程序
 * @author zongx at 2020/4/7 21:36
 */
public class Main {

    /**
     * 程序启动输入参数
     * 为一个密钥
     * 用于解密Mysql用户密码
     */
    public static String key = "";

    public static void main(String[] args) {
        if (null == args || args.length <= 0)
            throw new RuntimeException("missing startup parameter");
        else {
            key = args[0];
            System.out.println("get startup parameter: " + key);
        }
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
        System.out.println("service api started...");
    }

}
