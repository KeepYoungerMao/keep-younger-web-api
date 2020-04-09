package com.mao.his.weather.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 接收API天气信息结果类
 * @author zongx at 2020/4/9 21:12
 */
@Getter
@Setter
@NoArgsConstructor
public class Result {

    private WeatherData data;       //天气信息
    private int status;             //接收状态码
    private String desc;            //接收情况：OK

    public Result(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
