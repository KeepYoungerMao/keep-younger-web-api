package com.mao.entity.data.pic;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片列表数据
 * create by mao at 2020/4/16 23:56
 */
@Getter
@Setter
public class Pic extends Operation {
    private String id;          //id
    private String name;        //name
    private Integer prl;        //图片路径一级文件夹
    private Integer srl;        //图片路径二级文件夹
    private Long pid;           //一级分类
    private Long sid;           //二级分类
    private String s_image;     //小图片地址
}
