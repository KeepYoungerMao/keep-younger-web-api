package com.mao.his.weather.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 昨日天气信息类
 * @author zongx at 2020/4/9 21:09
 */
@Getter
@Setter
public class YesterdayWeather {

    private String date;            //日期
    private String high;            //最高温度
    private String fx;              //风向
    private String low;             //最低温度
    private String fl;              //风力
    private String type;            //天气情况

}
