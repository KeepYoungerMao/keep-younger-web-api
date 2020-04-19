package com.mao.entity.data.m3u8;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * 电影参数
 * create by mao at 2020/4/19 21:49
 */
@Getter
@Setter
public class MovieParam extends Page {
    private String name;
    private String actor;
    private String type;
    private Integer type_id;        //类型id，由type推出
    private Integer time;
    private String place;
    private Integer place_id;       //地点id，由place推出
}
