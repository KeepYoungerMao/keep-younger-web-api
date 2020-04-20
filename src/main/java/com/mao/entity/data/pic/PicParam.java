package com.mao.entity.data.pic;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片查询参数
 * create by mao at 2020/4/16 23:54
 */
@Getter
@Setter
public class PicParam extends Page {
    private String name;    //名称
    private Long pid;       //一级分类
    private Long sid;       //二级分类
}
