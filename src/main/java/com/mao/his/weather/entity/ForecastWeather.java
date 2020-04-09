package com.mao.his.weather.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 未来天气信息类
 * @author zongx at 2020/4/9 21:10
 */
@Getter
@Setter
public class ForecastWeather {

    private String date;            //日期
    private String high;            //最高温度
    private String fengli;          //风力
    private String low;             //最低温度
    private String fengxiang;       //风向
    private String type;            //天气情况

}
