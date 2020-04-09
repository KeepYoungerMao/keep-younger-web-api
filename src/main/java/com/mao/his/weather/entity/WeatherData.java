package com.mao.his.weather.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 天气数据
 * @author zongx at 2020/4/9 21:11
 */
@Getter
@Setter
public class WeatherData {

    private YesterdayWeather yesterday;         //昨日天气情况
    private String city;                        //城市
    private String aqi;                         //城市id
    private List<ForecastWeather> forecast;     //未来天气情况
    private String ganmao;                      //预防信息
    private String wendu;                       //平均温度

}
