package com.mao.his.weather;

import com.mao.entity.response.Response;
import com.mao.his.weather.entity.ForecastWeather;
import com.mao.his.weather.entity.Result;
import com.mao.util.HttpUtil;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中国天气数据请求
 * 来源于中国天气网
 * @author : create by zongx at 2020/4/9 18:16
 */
public class ChinaWeather {

    private static final String WEATHER_URL = "http://wthrcdn.etouch.cn/weather_mini?city=%s";

    /**
     * 根据城市名称获取该城市的天气信息
     * api来源于中国天气网
     * 数据为GZIP数据，需要解压
     * 数据包含CDATA标记，json处理时不需要，去除。
     */
    public static void getWeather(RoutingContext ctx){
        String city = ctx.request().getParam("city");
        if (SU.isEmpty(city))
            ctx.response().end(Response.error("param [city] is needed",ctx.request().path()));
        else {
            String url = String.format(WEATHER_URL,city);
            WebClient webClient = HttpUtil.getWebClient(ctx.vertx());
            webClient.getAbs(url).send(handler -> {
                if (handler.succeeded()){
                    String s = HttpUtil.unGZIP(handler.result().body().getBytes());
                    if (SU.isNotEmpty(s)){
                        ctx.response().end(Response.ok(transResult(s)));
                    } else {
                        ctx.response().end(Response.error("read weather data error.",ctx.request().path()));
                    }
                } else {
                    ctx.response().end(Response.error("cannot connect etouch api.",ctx.request().path()));
                }
            });
        }
    }

    /**
     * 天气数据处理
     */
    private static Result transResult(String json){
        Result result = JsonUtil.json2obj(json, Result.class);
        if (null == result || 1000 != result.getStatus())
            return new Result(500,"data request failed");
        return translateWeatherData(result);
    }

    /**
     * 修缮数据
     * @param result 天气数据
     * @return result
     */
    private static Result translateWeatherData(Result result){
        result.getData().getYesterday().setFl(removeK(result.getData().getYesterday().getFl()));
        for(ForecastWeather weather : result.getData().getForecast()){
            weather.setFengli(removeK(weather.getFengli()));
        }
        return result;
    }

    /**
     * 去除<![CDATA[]]>
     * @param str 字符串
     * @return str
     */
    private static String removeK(String str){
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)]]>.*");
        Matcher m = p.matcher(str);
        if(m.matches()){
            return m.group(1);
        }
        return str;
    }

}
