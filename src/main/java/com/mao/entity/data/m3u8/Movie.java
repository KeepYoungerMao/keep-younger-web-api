package com.mao.entity.data.m3u8;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 电影列表
 * create by mao at 2020/4/19 21:41
 */
@Getter
@Setter
public class Movie extends Operation {
    private String id;            //id
    private String name;        //名称
    private String image;       //图片地址
    private String actor;       //演员
    private String type;        //类型
    private Integer type_id;    //类型id
    private String time;        //时间
    private String place;       //地点
    private Integer place_id;   //地点id
    private String weight;      //画质
}
