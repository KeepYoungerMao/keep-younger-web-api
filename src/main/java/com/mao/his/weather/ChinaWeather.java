package com.mao.his.weather;

import com.mao.entity.response.Response;
import com.mao.service.MainService;
import com.mao.util.HttpUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

/**
 * 中国天气数据请求
 * 来源于中国天气网
 * @author : create by zongx at 2020/4/9 18:16
 */
public class ChinaWeather {

    private static final String WEATHER_URL = "http://wthrcdn.etouch.cn/weather_mini?city=%s";

    public static void getWeather(RoutingContext ctx){
        String city = ctx.request().getParam("city");
        if (SU.isEmpty(city))
            ctx.response().end(Response.error("param [city] is needed",ctx.request().path()));
        else {
            String url = String.format(WEATHER_URL,city);
            WebClient webClient = HttpUtil.getWebClient(ctx.vertx());
            webClient.getAbs(url)
                    .putHeader(MainService.CONTENT_TYPE,MainService.CONTENT_TYPE_NAME)
                    .putHeader("Content-Encoding","gzip, deflate, br")
                    .send(handler -> {
                        if (handler.succeeded()){
                            ctx.response().end(handler.result().bodyAsString());
                        } else {
                            ctx.response().end(Response.error("cannot connect etouch api.",ctx.request().path()));
                        }
                    });
        }
    }

}
