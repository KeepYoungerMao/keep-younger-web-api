package com.mao.util;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/**
 * http请求工具类
 * @author : create by zongx at 2020/4/9 14:19
 */
public class HttpUtil {

    /**
     * 获取vert.X web client
     * 用vert.X的http客户端，它那个异步handler还真不能做成工具类
     * 因为查处的数据返回不过去。
     */
    public static WebClient getWebClient(Vertx vertx){
        WebClientOptions options = new WebClientOptions()
                .setKeepAlive(false)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
                .setConnectTimeout(1000);
        return WebClient.create(vertx,options);
    }

}
