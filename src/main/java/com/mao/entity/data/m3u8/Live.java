package com.mao.entity.data.m3u8;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 电视播放地址
 * create by mao at 2020/4/16 23:17
 */
@Getter
@Setter
public class Live extends Operation {
    private Long id;            //主键
    private String name;        //名称
    private String url;         //播放地址
    private Integer type;       //类型
    private String kind;        //视频画质
    private Boolean useful;     //是否可播放
}
